package com.example.Apollon.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/images/uploads/**")

                .addResourceLocations("file:///C:/Users/user/IdeaProjects/Apollon/src/main/resources/static/images/uploads/");

        registry.addResourceHandler("/uploadFile/uploadImgs/**")
                .addResourceLocations("file:///C:/Users/user/IdeaProjects/Apollon/src/main/resources/static/uploadFile/uploadImgs/");

        registry.addResourceHandler("/uploadFile/uploadMusics/**")
                .addResourceLocations("file:///C:/Users/user/IdeaProjects/Apollon/src/main/resources/static/uploadFile/uploadMusics/");

    }
}