package com.merkle.xp.whitelabel.core.utils;

import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;

import com.merkle.xp.whitelabel.core.utils.constants.Constants;

public final class CommonUtils {

    private CommonUtils() {
        throw new IllegalStateException(Constants.UTILITY_CLASS);
    }

    public static <T> T getServiceFromBundle(Class<T> serviceType) {
        BundleContext bundleContext = FrameworkUtil.getBundle(CommonUtils.class).getBundleContext();
        ServiceReference<T> serviceReference = bundleContext.getServiceReference(serviceType);
        return bundleContext.getService(serviceReference);
    }

}
