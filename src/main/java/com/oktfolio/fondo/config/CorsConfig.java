package com.oktfolio.fondo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author oktfolio oktfolio@gmail.com
 * @date 2019/09/05
 */
@Configuration
@Order(-1)
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                // 添加映射路径
                registry.addMapping("/**")
                        // 允许哪些原始域
                        .allowedOrigins("*")
                        // 是否发送 Cookie 信息
                        .allowCredentials(true)
                        // 允许哪些请求方法
                        .allowedMethods("*")
                        // 允许哪些头信息
                        .allowedHeaders("*");
            }
        };
    }
}
