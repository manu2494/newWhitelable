/**
 * 
 */
package com.merkle.xp.whitelabel.core.models.impl;

import static com.adobe.cq.wcm.core.components.models.Download.PN_DESCRIPTION_FROM_ASSET;
import static com.adobe.cq.wcm.core.components.models.Download.PN_DISPLAY_FILENAME;
import static com.adobe.cq.wcm.core.components.models.Download.PN_DISPLAY_FORMAT;
import static com.adobe.cq.wcm.core.components.models.Download.PN_DISPLAY_SIZE;
import static com.adobe.cq.wcm.core.components.models.Download.PN_HIDE_TITLE_LINK;
import static com.adobe.cq.wcm.core.components.models.Download.PN_INLINE;
import static com.adobe.cq.wcm.core.components.models.Download.PN_TITLE_FROM_ASSET;
import static com.adobe.cq.wcm.core.components.models.Download.PN_TITLE_TYPE;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.commons.mime.MimeTypeService;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.InjectionStrategy;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import com.adobe.cq.dam.cfm.ContentElement;
import com.adobe.cq.dam.cfm.ContentFragment;
import com.adobe.cq.wcm.core.components.commons.link.Link;
import com.day.cq.commons.jcr.JcrConstants;
import com.day.cq.dam.api.Asset;
import com.day.cq.dam.api.DamConstants;
import com.day.cq.wcm.api.designer.Style;
import com.drew.lang.annotations.NotNull;
import com.drew.lang.annotations.Nullable;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.merkle.xp.whitelabel.core.bean.DownloadListBean;
import com.merkle.xp.whitelabel.core.models.DownloadListModel;
import com.merkle.xp.whitelabel.core.utils.link.LinkManager;

import lombok.Getter;

@Model(adaptables = SlingHttpServletRequest.class, adapters = DownloadListModel.class, resourceType = "whitelabel/components-dnc/downloadlist", defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class DownloadListModelImpl implements DownloadListModel {
	public static final String SELECTOR = "coredownload";
	public static final String INLINE_SELECTOR = "inline";

	@Inject
	private String cfPath;

	@Inject
	private String elementNames;

	@ScriptVariable
	private Resource resource;

	@Self
	private SlingHttpServletRequest request;

	@SlingObject
	private ResourceResolver resolver;

	@Override
	public String getCFPath() {
		return cfPath;
	}

	/**
	 * @return
	 */
	@Override
	public String getElementNames() {
		return elementNames;
	}

	@Self
	private LinkManager linkManager;

	@ValueMapValue
	@Default(values = StringUtils.EMPTY)
	@Getter
	private String variant;

	@ScriptVariable
	private ValueMap properties;

	private boolean titleFromAsset = false;

	private boolean descriptionFromAsset = false;

	private boolean inline = false;

	private boolean displaySize;

	private boolean displayFormat;

	private boolean displayFilename;

	private boolean hideTitleLink = false;

	@ValueMapValue(injectionStrategy = InjectionStrategy.OPTIONAL, name = JcrConstants.JCR_TITLE)
	@Nullable
	private String title;

	@ValueMapValue(injectionStrategy = InjectionStrategy.OPTIONAL, name = JcrConstants.JCR_DESCRIPTION)
	@Nullable
	private String description;

	@ValueMapValue(injectionStrategy = InjectionStrategy.OPTIONAL)
	@Nullable
	private String actionText;

	@ScriptVariable(injectionStrategy = InjectionStrategy.OPTIONAL)
	@JsonIgnore
	@Nullable
	protected Style currentStyle;

	@OSGiService
	private MimeTypeService mimeTypeService;
	private String titleType;

	private String filename;

	private String format;

	private String size;

	private String extension;

	private long lastModified = 0;

	@SuppressWarnings("rawtypes")
	private Link link;

	private DownloadListBean downloadBean;

	public List<DownloadListBean> getDownloadList() {
		return new ArrayList<>(downloadList);
	}

	@ValueMapValue(name = "useCFData", injectionStrategy = InjectionStrategy.OPTIONAL)
	@Nullable
	private boolean useCFData;

	private List<DownloadListBean> downloadList = new ArrayList<DownloadListBean>();

	@PostConstruct
	private void initModel() {
		if (currentStyle != null) {
			titleType = currentStyle.get(PN_TITLE_TYPE, String.class);
			displaySize = currentStyle.get(PN_DISPLAY_SIZE, true);
			displayFormat = currentStyle.get(PN_DISPLAY_FORMAT, true);
			displayFilename = currentStyle.get(PN_DISPLAY_FILENAME, true);
			hideTitleLink = currentStyle.get(PN_HIDE_TITLE_LINK, false);
		}
		if (useCFData) {

			if (request.getAttribute("assetCfPath") != null) {
				cfPath = request.getAttribute("assetCfPath").toString();
			}
			if (StringUtils.isNotEmpty(cfPath)) {
				Resource fragmentResource = resolver.getResource(cfPath);
				if (fragmentResource != null) {
					ContentFragment cf = fragmentResource.adaptTo(ContentFragment.class);
					if (cf != null) {
						ContentElement ele = cf.getElement(elementNames);
						if (ele != null) {
							List<String> str = Arrays.asList(cf.getElement(elementNames).getContent().split("\\s+"));
							for (String assetPath : str) {
								if (StringUtils.isNotBlank(assetPath)) {
									Resource assetResource = resolver.getResource(assetPath);
									if (assetResource != null) {
										getFileData(assetResource);
									}
								}
							}
						}
					}
				}
			}
		}
	}

	public List<DownloadListBean> getFileData(Resource assetResource) {
		String fileReference = assetResource.getPath();
		titleFromAsset = properties.get(PN_TITLE_FROM_ASSET, titleFromAsset);
		descriptionFromAsset = properties.get(PN_DESCRIPTION_FROM_ASSET, descriptionFromAsset);
		inline = properties.get(PN_INLINE, inline);
		if (StringUtils.isNotBlank(fileReference)) {
			initAssetDownload(fileReference);
			downloadList.add(initAssetDownload(fileReference));
		}

		return new ArrayList<>(downloadList);
	}

	private DownloadListBean initAssetDownload(String fileReference) {

		Resource downloadResource = resolver.getResource(fileReference);
		if (downloadResource != null) {
			Asset downloadAsset = downloadResource.adaptTo(Asset.class);
			if (downloadAsset != null) {
				lastModified = getLastModified(downloadAsset);
				filename = downloadAsset.getName();
				format = getDcFormat(downloadAsset);
				extension = getExtension(downloadAsset);
				link = linkManager.get(getDownloadUrl(downloadResource)).build();
				title = getDcTitle(downloadAsset);
				description = getDescription(downloadAsset);
				size = getRawFileSize(downloadAsset);
				downloadBean = new DownloadListBean();
				downloadBean.setFileName(filename);
				downloadBean.setFormat(format);
				downloadBean.setSize(size);
				downloadBean.setDescription(description);
				downloadBean.setExtension(extension);
				downloadBean.setLink(link);
				downloadBean.setTitle(title);
				downloadBean.setLastModified(lastModified);
			}
		}
		return downloadBean;
	}

	private String getDcFormat(Asset downloadAsset) {
		format = downloadAsset.getMetadataValue(DamConstants.DC_FORMAT);
		if (!StringUtils.isEmpty(format)) {
			format = FilenameUtils.getExtension(filename);
		}
		return extension;
	}

	private String getExtension(Asset downloadAsset) {
		format = downloadAsset.getMetadataValue(DamConstants.DC_FORMAT);
		if (!StringUtils.isEmpty(format)) {
			extension = mimeTypeService.getExtension(format);
		}

		if (StringUtils.isEmpty(extension)) {
			extension = FilenameUtils.getExtension(filename);
		}
		return extension;
	}

	private long getLastModified(Asset downloadAsset) {
		Calendar resourceLastModified = properties.get(JcrConstants.JCR_LASTMODIFIED, Calendar.class);
		if (resourceLastModified != null) {
			lastModified = resourceLastModified.getTimeInMillis();
		}
		long assetLastModified = downloadAsset.getLastModified();
		if (assetLastModified > lastModified) {
			lastModified = assetLastModified;
		}
		return lastModified;
	}

	private String getDcTitle(Asset downloadAsset) {
		if (titleFromAsset) {
			title = downloadAsset.getMetadataValue(DamConstants.DC_TITLE);
			if (StringUtils.isEmpty(title)) {
				title = downloadAsset.getMetadataValueFromJcr(JcrConstants.JCR_TITLE);
				if (StringUtils.isEmpty(title)) {
					title = downloadAsset.getName();
				}
			}
		}
		return title;
	}

	private String getRawFileSize(Asset downloadAsset) {
		long rawFileSize;
		Object rawFileSizeObject = downloadAsset.getMetadata(DamConstants.DAM_SIZE);

		if (rawFileSizeObject != null) {
			rawFileSize = (Long) rawFileSizeObject;
		} else {
			rawFileSize = downloadAsset.getOriginal().getSize();
		}
		size = FileUtils.byteCountToDisplaySize(rawFileSize);

		return size;
	}

	private String getDescription(Asset downloadAsset) {
		if (descriptionFromAsset) {
			String assetDescription = downloadAsset.getMetadataValue(DamConstants.DC_DESCRIPTION);
			if (StringUtils.isNotBlank(assetDescription)) {
				description = assetDescription;
			}
		}
		return description;
	}

	@NotNull
	private String getDownloadUrl(Resource resource) {
		StringBuilder downloadUrlBuilder = new StringBuilder();
		downloadUrlBuilder.append(resource.getPath()).append(".").append(SELECTOR).append(".");
		if (inline) {
			downloadUrlBuilder.append(INLINE_SELECTOR).append(".");
		}
		downloadUrlBuilder.append(extension);
		return downloadUrlBuilder.toString();
	}

	public String getExportedType() {
		return request.getResource().getResourceType();
	}

	public String getUrl() {
		return (link != null) ? link.getURL() : null;
	}

	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return description;
	}

	public String getActionText() {
		return actionText;
	}

	public String getTitleType() {
		return titleType;
	}

	public String getFilename() {
		return filename;
	}

	public String getFormat() {
		return format;
	}

	public String getSize() {
		return size;
	}

	public boolean displaySize() {
		return displaySize;
	}

	public boolean displayFormat() {
		return displayFormat;
	}

	public boolean displayFilename() {
		return displayFilename;
	}

	public boolean hideTitleLink() {
		return hideTitleLink;
	}

	public String getExtension() {
		return extension;
	}
}