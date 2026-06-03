package com.merkle.xp.whitelabel.core.services.impl;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(
		name = "Engage 2.0 Api Configuration!!!", 
		description = "This configuration reads the values to make API calls")
public @interface ApiConfiguration {
	
	@AttributeDefinition(name="Name",description="Name of the api configuration")
	public String name();

	@AttributeDefinition(name="Service Url",description="Service url for the api calls")
	public String serviceUrl();
	
	@AttributeDefinition(name="Username",description="Username for the api calls")
	public String username();

	@AttributeDefinition(name="Password",description="Password for the api calls")
	public String password();
}
