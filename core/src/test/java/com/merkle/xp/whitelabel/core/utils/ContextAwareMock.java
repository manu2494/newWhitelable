package com.merkle.xp.whitelabel.core.utils;

import com.merkle.xp.whitelabel.core.config.*;

import java.lang.annotation.Annotation;

public class ContextAwareMock {

	public static TrackingConfig getTrackingConfig() {
		return new TrackingConfig() {
			@Override
			public Class<? extends Annotation> annotationType() {
				return null;
			}

			@Override
			public String rootPath() {
				return "/content/whitelabel-test/us/en/page";
			}

			@Override
			public String variableName() {
				return "datalayer";
			}

			@Override
			public boolean useArray() {
				return false;
			}

			@Override
			public String googleAnalyticsUACode() {
				return "googleUACode";
			}

			@Override
			public String launchID() {
				return "launch ID";
			}

			@Override
			public String launchPropertyID() {
				return "launch Property ID";
			}
		};
	}

	public static GeneralConfig getGeneralConfig() {
		return new GeneralConfig() {

			@Override
			public Class<? extends Annotation> annotationType() {
				return null;
			}

			@Override
			public String websiteName() {
				return "websiteName";
			}

			@Override
			public String favIcon() {
				return "FavIcon";
			}

			@Override
			public SeoSiteOrganizationConfig seoSiteOrganization() {
				return new SeoSiteOrganizationConfig() {

					@Override
					public Class<? extends Annotation> annotationType() {
						return null;
					}

					@Override
					public String name() {
						return "Organization name";
					}

					@Override
					public String url() {
						return "https://www.pinterest.com.mx/site";
					}

					@Override
					public String logo() {
						return "/content/dam/site/content/header_logo.png";
					}

					@Override
					public String countryCode() {
						return "+49";
					}

					@Override
					public String telephone() {
						return "123123";
					}

					@Override
					public SeoSiteContactPointConfig[] contactPoint() {
						return new SeoSiteContactPointConfig[] {};
					}

					@Override
					public String[] sameAs() {
						return new String[] { "https://www.facebook.com/site/", "https://www.instagram.com/site/" };
					}

					@Override
					public String jsonLD() {
						return "{}";
					}
				};
			}

			@Override
			public SeoSiteLinkConfig seoSiteLinkConfig() {
				return new SeoSiteLinkConfig() {

					@Override
					public Class<? extends Annotation> annotationType() {
						return null;
					}

					@Override
					public String url() {
						return "https://www.pinterest.com.mx/site";
					}

					@Override
					public String target() {
						return "/content/whitelabel-test/us/en/site/searching?q=";
					}
				};
			}

			@Override
			public String homePagePath() {
				return "/content/sn/global/dnc/global/en";
			}

			@Override
			public HeadScriptsOrganizationConfig headScriptsOrganizationConfig() {
				return new HeadScriptsOrganizationConfig() {
					@Override
					public Class<? extends Annotation> annotationType() {
						return null;
					}

					@Override
					public String scriptsHead() {
						return "<link rel=\"stylesheet\" href=\"#\">";
					}
				};
			}

			@Override
			public FileTypeIconMapConfig fileTypeIconMapping() {
				return null;
			}

			@Override
			public String body() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public String hreflangXDefault() {
				return "/content";
			}
		};
	}

	public static GeneralConfig getGeneralConfig1() {
		return new GeneralConfig() {

			@Override
			public Class<? extends Annotation> annotationType() {
				return null;
			}

			@Override
			public String websiteName() {
				return "websiteName";
			}

			@Override
			public String favIcon() {
				return "FavIcon";
			}

			@Override
			public SeoSiteOrganizationConfig seoSiteOrganization() {
				return new SeoSiteOrganizationConfig() {

					@Override
					public Class<? extends Annotation> annotationType() {
						return null;
					}

					@Override
					public String name() {
						return "Organization name";
					}

					@Override
					public String url() {
						return "";
					}

					@Override
					public String logo() {
						return "/content/dam/site/content/header_logo.png";
					}

					@Override
					public String countryCode() {
						return "+49";
					}

					@Override
					public String telephone() {
						return "123123";
					}

					@Override
					public SeoSiteContactPointConfig[] contactPoint() {
						return new SeoSiteContactPointConfig[] {};
					}

					@Override
					public String[] sameAs() {
						return new String[] { "https://www.facebook.com/site/", "https://www.instagram.com/site/" };
					}

					@Override
					public String jsonLD() {
						return "{}";
					}
				};
			}

			@Override
			public SeoSiteLinkConfig seoSiteLinkConfig() {
				return new SeoSiteLinkConfig() {

					@Override
					public Class<? extends Annotation> annotationType() {
						return null;
					}

					@Override
					public String url() {
						return "";
					}

					@Override
					public String target() {
						return "/content/whitelabel-test/us/en/site/searching?q=";
					}
				};
			}

			@Override
			public String homePagePath() {
				return "/content/sn/global/dnc/global/en";
			}

			@Override
			public HeadScriptsOrganizationConfig headScriptsOrganizationConfig() {
				return new HeadScriptsOrganizationConfig() {
					@Override
					public Class<? extends Annotation> annotationType() {
						return null;
					}

					@Override
					public String scriptsHead() {
						return "<link rel=\"stylesheet\" href=\"#\">";
					}
				};
			}

			@Override
			public FileTypeIconMapConfig fileTypeIconMapping() {
				return null;
			}

			@Override
			public String body() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public String hreflangXDefault() {
				return "/content";
			}
		};
	}

	public static ApiConfig getApiConfig() {
		return new ApiConfig() {
			@Override
			public Class<? extends Annotation> annotationType() {
				return null;
			}

			@Override
			public String adminApiName() {
				return "adminApi";
			}

			@Override
			public String userApiName() {
				return "userApi";
			}

		};
	}

	public static SiteDeviceConfig getDeviceConfig() {
		return new SiteDeviceConfig() {

			@Override
			public Class<? extends Annotation> annotationType() {
				return null;
			}

			@Override
			public DeviceConfig[] devices() {
				return new DeviceConfig[] { new DeviceConfig() {

					@Override
					public Class<? extends Annotation> annotationType() {
						return null;
					}

					@Override
					public int minSize() {
						return 0;
					}

					@Override
					public int maxSize() {
						return 768;
					}

					@Override
					public String redirect() {
						return "https://www.google.com";
					}
					
				}};
			};

		};
	}
}