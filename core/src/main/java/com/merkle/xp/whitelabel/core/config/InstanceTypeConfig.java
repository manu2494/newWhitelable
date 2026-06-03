package com.merkle.xp.whitelabel.core.config;

import org.apache.sling.caconfig.annotation.Property;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name = "Merkle - Instance Config Service Configuration", description = "Instance configuration with all flag related to the machine")
public @interface InstanceTypeConfig {

    @Property(label = "Is Author Instances", description = "Check if is author instance")
    boolean isAuthorInstance() default false;
    
    @Property(label = "Runmodes", description = "Environments runmodes")
	String runmodes() default "author";
}
