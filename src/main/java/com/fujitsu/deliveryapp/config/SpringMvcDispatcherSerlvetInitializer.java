package com.fujitsu.deliveryapp.config;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class SpringMvcDispatcherSerlvetInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[0];
    }

    /**
     * Get spring project configuration.
     * @return SpringConfig class.
     */
    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[] {SpringConfig.class};
    }

    /**
     * Send all user's requests on dispatcher servlet.
     * @return ...
     */
    @Override
    protected String[] getServletMappings() {
        return new String[] {"/"};
    }
}
