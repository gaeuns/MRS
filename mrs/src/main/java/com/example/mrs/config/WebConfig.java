package com.example.mrs.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String absolutePath = new File("uploads").getAbsolutePath().replace("\\", "/") + "/";

        System.out.println("정적 리소스 매핑 경로: " + absolutePath);

        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:///" + absolutePath);
    }
}
