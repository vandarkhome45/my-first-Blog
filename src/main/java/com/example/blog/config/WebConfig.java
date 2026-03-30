package com.example.blog.config;

import com.example.blog.interceptor.ModuleInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Web 配置类 - 注册拦截器和静态资源处理器
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private ModuleInterceptor moduleInterceptor;

    /**
     * 上传文件存储路径（相对路径）
     */
    private final String uploadPath = "./uploads/";

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册模块功能拦截器
        registry.addInterceptor(moduleInterceptor)
                .addPathPatterns("/**")  // 拦截所有请求
                .excludePathPatterns(    // 排除静态资源和上传文件
                    "/static/**",
                    "/css/**",
                    "/js/**",
                    "/images/**",
                    "/uploads/**"        // 允许访问上传文件
                );
    }

    /**
     * 添加静态资源处理器
     * 用于访问上传的文件
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 配置上传文件的访问路径
        Path uploadPath = Paths.get(this.uploadPath);
        String absolutePath = uploadPath.toFile().getAbsolutePath();
        
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + absolutePath + "/")
                .setCachePeriod(3600);  // 缓存 1 小时
    }
}
