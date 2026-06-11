package com.example.mrs.config;

import jakarta.servlet.MultipartConfigElement;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;

@Configuration
public class MultipartConfig {

    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize(DataSize.ofMegabytes(20));  // 20MB 파일 업로드 제한
        factory.setMaxRequestSize(DataSize.ofMegabytes(40)); // 40MB 요청 제한
        return factory.createMultipartConfig();
    }
}
