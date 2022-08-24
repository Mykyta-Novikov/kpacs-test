package org.example;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

/**
 * Class that configures {@link ViewResolver} and {@link ResourceHandlerRegistry}.
 */
@Configuration
public class ResourcesConfiguration implements WebMvcConfigurer {
    /**
     * {@link ViewResolver} {@link Bean} for resolution of JSP pages views.
     */
    @Bean
    ViewResolver internalResourceViewResolver() {
        return new InternalResourceViewResolver("/WEB-INF/jsp/", ".jsp");
    }

    /**
     * Registers JS static resources to be available.
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/js/**")
                .addResourceLocations("/js/");
    }
}
