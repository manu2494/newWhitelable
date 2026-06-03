package com.merkle.xp.whitelabel.core.models;

import org.osgi.annotation.versioning.ConsumerType;

import com.adobe.cq.wcm.core.components.models.Component;

/**
 * @author pooja.c.patel
 *
 */
@ConsumerType
public interface DownloadListModel extends Component {

	/**
	   * Get CF path from dialog configuration
	   * 
	   * @return String cf path
	   */
	  String getCFPath();

	  String getElementNames();
}
