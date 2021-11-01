package com.example.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(new LogCostInterceptor())
//                .addPathPatterns("/**")
//                .order(0);
//        registry.addInterceptor(new Log2CostInterceptor())
//                .addPathPatterns("/**")
//                .excludePathPatterns("/book/selectall")
//                .order(1);
//    }


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //获取文件的真实路径
        String path = "D:\\static\\uploadfile\\";
        String os = System.getProperty("os.name");
        if (os.toLowerCase().startsWith("win")) {
            // 上传路径映射 会使spring boot的自动配置失效
            registry.addResourceHandler("/file/uploadfile/**").
                    addResourceLocations("file:" + path);

        }
    }
}
