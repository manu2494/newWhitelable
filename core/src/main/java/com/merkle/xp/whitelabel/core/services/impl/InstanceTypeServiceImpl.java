package com.merkle.xp.whitelabel.core.services.impl;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.propertytypes.ServiceDescription;
import org.osgi.service.component.propertytypes.ServiceVendor;
import org.osgi.service.metatype.annotations.Designate;

import com.merkle.xp.whitelabel.core.config.InstanceTypeConfig;
import com.merkle.xp.whitelabel.core.services.InstanceType;

import lombok.Getter;


/**
 * Merkle - Instance Config Service
 */
@Component(service = InstanceType.class, configurationPolicy = ConfigurationPolicy.OPTIONAL, immediate = true)
@ServiceDescription("Merkle - Instance Config Service")
@Designate(ocd = InstanceTypeConfig.class)
@ServiceVendor("Merkle Whitelabel")
public class InstanceTypeServiceImpl implements InstanceType {

    @Getter
    boolean isAuthorInstance;
    
    @Getter
    String runmodes;

    @Modified
    @Activate
    protected void activate(final InstanceTypeConfig config) {
        this.isAuthorInstance = config.isAuthorInstance();
        this.runmodes = config.runmodes();
    }

	@Override
	public String runmodes() {
		return this.runmodes;
	}
}
