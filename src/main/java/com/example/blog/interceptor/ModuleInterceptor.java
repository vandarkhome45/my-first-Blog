package com.example.blog.interceptor;

import com.example.blog.config.ModuleConfig;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 模块功能拦截器
 * 用于检查各个功能模块是否启用
 */
@Component
public class ModuleInterceptor implements HandlerInterceptor {

    @Autowired
    private ModuleConfig moduleConfig;

    @Override
    public boolean preHandle(HttpServletRequest request, 
                           HttpServletResponse response, 
                           Object handler) throws Exception {
        
        String uri = request.getRequestURI();
        
        // 检查评论模块
        if (uri.startsWith("/comment")) {
            if (!moduleConfig.isCommentEnabled()) {
                response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write(
                    "{\"code\":503,\"msg\":\"评论系统已禁用\"}"
                );
                return false;
            }
        }
        
        // 后续可以在这里添加其他模块的检查
        
        return true;
    }
}
