package com.merkle.xp.whitelabel.core.services.impl;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.jcr.Value;
import javax.jcr.ValueFactory;

import org.apache.sling.api.resource.ResourceResolver;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.acs.commons.genericlists.GenericList;
import com.adobe.acs.commons.genericlists.GenericList.Item;
import com.day.cq.replication.Replicator;
import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagManager;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.merkle.xp.whitelabel.core.services.GenericListService;

/**
 * Implementation of ESKO Generic List service Interface
 *
 */
@Component(service = GenericListService.class)
public class GenericListServiceImpl implements GenericListService {

	private static final Logger LOGGER = LoggerFactory.getLogger(GenericListServiceImpl.class);

	private static final String CLASS_NAME = GenericListServiceImpl.class.getSimpleName();

	@Reference
	private Replicator replicator;
	
	/**
	 * populating Map from Generic List
	 *
	 */
	@Override
	public LinkedHashMap<String, String> populateGenListMap(ResourceResolver resourceResolver, String genericListPath) {
		LinkedHashMap<String, String> itemsMap = new LinkedHashMap<>();
		PageManager pageManager = resourceResolver.adaptTo(PageManager.class);
		if (Objects.isNull(pageManager)) {
			return itemsMap;
		}
		Page genericPage = pageManager.getPage(genericListPath);
		GenericList genericList = Objects.nonNull(genericPage)? genericPage.adaptTo(GenericList.class):null;
		List<Item> itemList = genericList != null ? genericList.getItems() : null;
		if (Objects.nonNull(itemList)) {
			for (Item genericItem : itemList) {
				itemsMap.put(genericItem.getTitle(), genericItem.getValue());
			}
		} else {
			LOGGER.info("[Merkle Whitelabel] - {} - GenericListServiceImpl.populateGenListMap() Generic List corresponding to {} is not found", CLASS_NAME,
					itemsMap);
		}
		return itemsMap;
	}

	/**
	 * returns value from Generic List
	 *
	 */
	@Override
	public Value[] findValueFromGenericList(ValueFactory valueFactory, Map<String, String> itemsMap, String title) {
		String[] xmlValueArr = title.split("\\|");
		Value[] values = new Value[xmlValueArr.length];
		if (itemsMap.size() < 1) {
			return values;
		}
		int count = 0;
		for (String valueElement : xmlValueArr) {
			if (Objects.nonNull(itemsMap.get(valueElement.trim()))) {
				values[count++] = valueFactory.createValue(itemsMap.get(valueElement.trim()));
			} else {
				values[count++] = valueFactory.createValue(valueElement.trim());
			}
		}
		return values;
	}

	/**
	 * finding region for the given CBU
	 *
	 */
	@Override
	public String findRegionValue(ResourceResolver resourceResolver, String genericListPath, String cbu) {
		PageManager pageManager = resourceResolver.adaptTo(PageManager.class);
		Page genericPage = pageManager != null ? pageManager.getPage(genericListPath) : null;
		GenericList genericList = genericPage != null ? genericPage.adaptTo(GenericList.class) : null;
		return genericList != null ? genericList.lookupTitle(cbu) : null;
	}

}
