package com.merkle.xp.whitelabel.core.services.impl;

import java.util.HashMap;
import java.util.Map;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.component.propertytypes.ServiceDescription;
import org.osgi.service.component.propertytypes.ServiceVendor;
import org.osgi.service.metatype.annotations.Designate;

import com.merkle.xp.whitelabel.core.services.ApiConfigurationService;

@Component(service = ApiConfigurationService.class, immediate = true, configurationPolicy = ConfigurationPolicy.OPTIONAL, reference = @Reference(name="api", service= ApiConfiguration.class, policy=ReferencePolicy.DYNAMIC, cardinality = ReferenceCardinality.MULTIPLE))
@Designate(ocd = ApiConfiguration.class, factory = true)
@ServiceDescription("Merkle - Api Configuration Service")
@ServiceVendor("Merkle Whitelabel")
public class ApiConfigurationImpl implements ApiConfigurationService {
	private static Map<String, ApiConfiguration> configs = new HashMap<>();
	
	@Activate
	public void activate(final ApiConfiguration config) {
		configs.put(config.name(), config);
	}
	
	@Deactivate
	public void deactivate(final ApiConfiguration config) {
		configs.remove(config.name());
	}
	
	@Override
	public ApiConfiguration getApi(String name) {
		return configs.get(name);
	}
	
	public String[] getConfigNames() {
		return configs.keySet().toArray(new String[configs.size()]);
	}
}
