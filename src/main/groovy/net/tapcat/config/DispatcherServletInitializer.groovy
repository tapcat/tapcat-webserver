package net.tapcat.config
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer

import javax.servlet.ServletContext
import javax.servlet.ServletException
import javax.servlet.ServletRegistration

class DispatcherServletInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    @Override
    protected Class<?>[] getRootConfigClasses() {
        [RestConfig, WebSecurityConfig]
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        [WebConfig, WebSocketConfig]
    }

    @Override
    protected String[] getServletMappings() {
        ['/']
    }

    @Override
    protected void customizeRegistration(ServletRegistration.Dynamic registration) {
        registration.setInitParameter('dispatchOptionsRequest', 'true')
    }

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        super.onStartup(servletContext)
    }
}
