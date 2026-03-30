package com.example.blog.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * 功能模块配置类
 * 用于控制各个功能模块的启用/禁用
 */
@Configuration
@PropertySource("classpath:module-config.properties")
public class ModuleConfig {

    // 博客基础功能
    @Value("${module.blog.enabled:true}")
    private boolean blogEnabled;

    // 评论系统模块
    @Value("${module.comment.enabled:true}")
    private boolean commentEnabled;

    // 分类和标签模块
    @Value("${module.category.enabled:false}")
    private boolean categoryEnabled;

    @Value("${module.tag.enabled:false}")
    private boolean tagEnabled;

    // 用户认证模块
    @Value("${module.user.enabled:false}")
    private boolean userEnabled;

    @Value("${module.auth.enabled:false}")
    private boolean authEnabled;

    // 搜索模块
    @Value("${module.search.enabled:false}")
    private boolean searchEnabled;

    // 访问统计模块
    @Value("${module.statistics.enabled:false}")
    private boolean statisticsEnabled;

    // ===== Getter 方法 =====

    public boolean isBlogEnabled() {
        return blogEnabled;
    }

    public boolean isCommentEnabled() {
        return commentEnabled;
    }

    public boolean isCategoryEnabled() {
        return categoryEnabled;
    }

    public boolean isTagEnabled() {
        return tagEnabled;
    }

    public boolean isUserEnabled() {
        return userEnabled;
    }

    public boolean isAuthEnabled() {
        return authEnabled;
    }

    public boolean isSearchEnabled() {
        return searchEnabled;
    }

    public boolean isStatisticsEnabled() {
        return statisticsEnabled;
    }

    /**
     * 检查评论模块是否启用
     * @return true-启用，false-禁用
     */
    public static boolean checkCommentModule() {
        // 这个方法会在拦截器中调用
        return true; // 默认返回 true，实际检查由拦截器完成
    }
}
