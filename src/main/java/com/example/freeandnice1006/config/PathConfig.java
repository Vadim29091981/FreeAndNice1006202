package com.example.freeandnice1006.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
@Configuration
@Getter
public class PathConfig {
    @Value("${swagger.url.path}")
    private String swaggerUrlPath;
}
