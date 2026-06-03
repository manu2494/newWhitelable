package com.merkle.xp.whitelabel.core.config;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.caconfig.annotation.Property;

/**
 * Context-aware configuration holding information about the SEO Site Organization Contact Point
 * This configuration is meant to be used as a context-aware resource. See <a href=
 * "https://sling.apache.org/documentation/bundles/context-aware-configuration/context-aware-configuration.html#context-aware-resources">Context-Aware
 * Resources</a>
 */
public @interface SeoSiteContactPointConfig {

  @Property(label = "Contact Type", description = "Select Contact Type data",
      property = {"widgetType=dropdown",
          "dropdownOptions=[" + "{'value':'customer support','description':'Customer support'},"
              + "{'value':'technical support','description':'Technical support'},"
              + "{'value':'billingSupport','description':'Billing support'},"
              + "{'value':'billPayment','description':'Bill payment'},"
              + "{'value':'sales','description':'Sales'},"
              + "{'value':'reservations','description':'Reservations'},"
              + "{'value':'credit card support','description':'Credit card support'},"
              + "{'value':'emergency','description':'Emergency'},"
              + "{'value':'baggage tracking','description':'Baggage tracking'},"
              + "{'value':'roadside assistance','description':'Roadside assistance'},"
              + "{'value':'package tracking','description':'Package tracking'}" + "]"})
  String contactType();

  @Property(label = "Telephone country code", description = "Please add the country code with the '+' suffix if necessary")
  String countryCode() default StringUtils.EMPTY;

  @Property(label = "Organization telephone", description = "A '-' will be automatically added between the country code and the telephone number")
  String telephone() default StringUtils.EMPTY;

}
