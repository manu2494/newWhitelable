package com.merkle.xp.whitelabel.core.bean;

import com.adobe.cq.wcm.core.components.commons.link.Link;
import com.google.gson.annotations.Expose;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DownloadListBean {

	@Expose
	protected String fileName;

	@Expose
	protected String format;

	@Expose
	protected String size;

	@Expose
	protected Link link;

	@Expose
	protected String title;

	@Expose
	protected String description;

	@Expose
	protected String extension;

	@Expose
	protected long lastModified;

}
