package com.merkle.xp.whitelabel.core.testcontext;

import static com.merkle.xp.whitelabel.core.testcontext.ContextPlugins.Merkle_FRAMEWORK;
import static org.apache.sling.testing.mock.caconfig.ContextPlugins.CACONFIG;
import static io.wcm.testing.mock.wcmio.caconfig.ContextPlugins.WCMIO_CACONFIG;

import org.apache.sling.testing.mock.sling.ResourceResolverType;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextBuilder;
import io.wcm.testing.mock.aem.junit5.AemContextCallback;

/**
 * Sets up {@link AemContext} for unit tests in this application.
 */
public final class AppAemContext {

	private AppAemContext() {
		// static methods only
	}

	/**
	 * @return {@link AemContext}
	 */
	public static AemContext newAemContext() {
		return newAemContextBuilder().build();
	}

	/**
	 * @return {@link AemContextBuilder}
	 */
	public static AemContextBuilder newAemContextBuilder() {
		return newAemContextBuilder(ResourceResolverType.RESOURCERESOLVER_MOCK).plugin(CACONFIG).plugin(WCMIO_CACONFIG)
				.plugin(Merkle_FRAMEWORK).afterSetUp(SETUP_CALLBACK);
	}

	/**
	 * @return {@link AemContextBuilder}
	 */
	public static AemContextBuilder newAemContextBuilder(ResourceResolverType resourceResolverType) {
		return new AemContextBuilder(resourceResolverType).plugin(CACONFIG).plugin(Merkle_FRAMEWORK)
				.afterSetUp(SETUP_CALLBACK);
	}

	/**
	 * Custom set up rules required in all unit tests.
	 */
	private static final AemContextCallback SETUP_CALLBACK = new AemContextCallback() {
		@Override
		public void execute(AemContext context) {
			// custom project initialization code for every unit test
		}
	};

}
