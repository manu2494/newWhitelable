package com.merkle.xp.whitelabel.core.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.merkle.xp.whitelabel.core.utils.constants.Constants;

public class PageUtils {

	public static final String WEEK_TYPE_TAG = "week";
	public static final String MONTH_TYPE_TAG = "month";
	public static final String MONTH_WEEK_VALUE_SEPARATOR = "-";
	public static final int MONTH_WEEK_VALUE_INDEX = 0;

	private PageUtils() {
		throw new IllegalStateException(Constants.UTILITY_CLASS);
	}

	/**
	 * Get Page of resource
	 * 
	 * @param rr       {@link ResourceResolver}
	 * @param resource {@link Resource}
	 * @return Page if existing, otherwise is false
	 */
	public static Page getPageFromResource(final ResourceResolver rr, final Resource resource) {
		if (rr != null && resource != null) {
			PageManager pageManager = rr.adaptTo(PageManager.class);
			if (pageManager != null) {
				return pageManager.getContainingPage(resource);
			}
		}

		return null;
	}

	public static String getLocale(Page currentPage) {
		if (currentPage != null) {
			Page[] locale = getLocalePages(currentPage);
			if (locale != null) {
				return getPageName(locale[1]).toLowerCase()+"_"+getPageName(locale[0]).toUpperCase();
			}
		}
		return StringUtils.EMPTY;
	}

	public static Page[] getLocalePages(final Page currentPage) {
		Page workingPage = currentPage;
		while (true) {
			Page parent = workingPage.getParent();
			if (parent == null) {
				break;
			}
			String thisPage = getPageName(workingPage);
			String parentPage = getPageName(parent);
			if (thisPage.length() == 2 && parentPage.length() == 2) {
				return new Page[] {parent, workingPage};
			}
			workingPage = parent;
		}
		return null;
	}

	private static String getPageName(Page currentPage) {
		return currentPage.getName();
	}

}
