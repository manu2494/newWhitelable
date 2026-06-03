package com.merkle.xp.whitelabel.core.services;

import org.apache.sling.api.resource.ResourceResolver;

import javax.jcr.Value;
import javax.jcr.ValueFactory;
import java.util.LinkedHashMap;
import java.util.Map;

public interface GenericListService {

	/**
	 * populating Map from Generic List
	 *
	 */
	LinkedHashMap<String, String> populateGenListMap(ResourceResolver resourceResolver, String genericListPath);

	/**
	 * returns value from Generic List
	 *
	 */
	Value[] findValueFromGenericList(ValueFactory valueFactory, Map<String, String> itemsMap, String title);

	/**
	 * finding region for the given CBU
	 *
	 */
	String findRegionValue(ResourceResolver resourceResolver, String genericListPath, String cbu);

}
