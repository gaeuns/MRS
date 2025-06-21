package com.example.mrs.config; // 실제 위치에 따라 변경 필요

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 현재 실행 경로 기준으로 절대 경로 설정
        String absolutePath = System.getProperty("user.dir") + "/uploads/";

        System.out.println("정적 리소스 매핑 경로: " + absolutePath); // 디버깅용 로그

        registry.addResourceHandler("/uploads/**") // URL 요청 경로
                .addResourceLocations("file:" + absolutePath); // 실제 파일 경로
    }
}
