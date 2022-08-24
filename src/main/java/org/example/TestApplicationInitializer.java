package org.example;


import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

/**
 * Initializes {@link org.springframework.context.ApplicationContext}
 * and {@link org.springframework.web.servlet.DispatcherServlet}, and sets its mapping to root,
 * marks this package and its subpackages for {@link ComponentScan},
 * and allows {@link EnableWebMvc} configuration.
 */
@Configuration
@EnableWebMvc
@ComponentScan
@PropertySource("/WEB-INF/application.properties")
public class TestApplicationInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {
    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return null;
    }

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[]{TestApplicationInitializer.class};
    }
}