package com.blogapi.config.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;


@Slf4j
@Configuration
public class WebConfig implements WebMvcConfigurer {


    @Value("${file.upload-dir}")
    private String uploadDir;


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:" + uploadDir+"/")
                .setCachePeriod(30) // cache 30 second
                .resourceChain(true);

        log.info("Configured static resource handler for: file:{}/", uploadDir);
        log.info("Upload directory absolute path: {}", Paths.get(uploadDir).toAbsolutePath());

    }
}
