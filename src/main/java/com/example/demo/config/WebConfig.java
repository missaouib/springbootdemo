package com.example.demo.config;

import com.example.demo.interceptor.Log2CostInterceptor;
import com.example.demo.interceptor.LogCostInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LogCostInterceptor())
                .addPathPatterns("/**")
                .order(0);
        registry.addInterceptor(new Log2CostInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/book/selectall")
                .order(1);
    }
}
