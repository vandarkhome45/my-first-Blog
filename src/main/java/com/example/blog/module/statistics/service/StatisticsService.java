package com.example.blog.module.statistics.service;

import com.example.blog.module.blog.entity.BlogEntity;
import com.example.blog.module.blog.repository.BlogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 访问统计服务层
 * 模块：访问统计模块
 * 
 * 功能：
 * - 增加文章阅读量
 * - 点赞/取消点赞
 * - 获取热门文章排行
 * - 获取统计数据
 */
@Service
public class StatisticsService {

    @Autowired
    private BlogRepository blogRepository;

    /**
     * 增加文章阅读量
     * 
     * @param blogId 文章 ID
     * @return 更新后的文章
     */
    public BlogEntity incrementViewCount(Integer blogId) {
        BlogEntity blog = blogRepository.findById(blogId)
                .orElseThrow(() -> new IllegalArgumentException("文章不存在"));

        // 增加阅读量
        Integer currentViews = blog.getViewCount() != null ? blog.getViewCount() : 0;
        blog.setViewCount(currentViews + 1);
        
        return blogRepository.save(blog);
    }

    /**
     * 点赞文章
     * 
     * @param blogId 文章 ID
     * @return 更新后的文章
     */
    public BlogEntity likeArticle(Integer blogId) {
        BlogEntity blog = blogRepository.findById(blogId)
                .orElseThrow(() -> new IllegalArgumentException("文章不存在"));

        // 增加点赞数
        Integer currentLikes = blog.getLikeCount() != null ? blog.getLikeCount() : 0;
        blog.setLikeCount(currentLikes + 1);
        
        return blogRepository.save(blog);
    }

    /**
     * 取消点赞（可选功能，如果需要精确控制）
     * 注意：当前简单实现为减少点赞数，实际项目中可能需要记录用户是否点过赞
     * 
     * @param blogId 文章 ID
     * @return 更新后的文章
     */
    public BlogEntity unlikeArticle(Integer blogId) {
        BlogEntity blog = blogRepository.findById(blogId)
                .orElseThrow(() -> new IllegalArgumentException("文章不存在"));

        // 减少点赞数（不低于 0）
        Integer currentLikes = blog.getLikeCount() != null ? blog.getLikeCount() : 0;
        blog.setLikeCount(Math.max(0, currentLikes - 1));
        
        return blogRepository.save(blog);
    }

    /**
     * 获取热门文章排行（按阅读量）
     * 
     * @param limit 返回数量限制
     * @return 热门文章列表
     */
    public List<BlogEntity> getHotArticles(int limit) {
        List<BlogEntity> allBlogs = blogRepository.findAll();
        
        // 过滤掉没有阅读量的文章
        return allBlogs.stream()
                .filter(blog -> blog.getViewCount() != null && blog.getViewCount() > 0)
                .sorted((a, b) -> {
                    // 按阅读量降序排序
                    int viewCompare = b.getViewCount().compareTo(a.getViewCount());
                    if (viewCompare != 0) {
                        return viewCompare;
                    }
                    // 如果阅读量相同，按点赞数排序
                    return b.getLikeCount().compareTo(a.getLikeCount());
                })
                .limit(limit)
                .collect(Collectors.toList());
    }

    /**
     * 获取点赞最多的文章
     * 
     * @param limit 返回数量限制
     * @return 点赞最多的文章列表
     */
    public List<BlogEntity> getMostLikedArticles(int limit) {
        List<BlogEntity> allBlogs = blogRepository.findAll();
        
        return allBlogs.stream()
                .filter(blog -> blog.getLikeCount() != null && blog.getLikeCount() > 0)
                .sorted((a, b) -> b.getLikeCount().compareTo(a.getLikeCount()))
                .limit(limit)
                .collect(Collectors.toList());
    }

    /**
     * 获取全站统计信息
     * 
     * @return 统计数据
     */
    public Map<String, Object> getTotalStatistics() {
        List<BlogEntity> allBlogs = blogRepository.findAll();
        
        Map<String, Object> stats = new HashMap<>();
        
        // 总文章数
        stats.put("totalArticles", allBlogs.size());
        
        // 总阅读量
        int totalViews = allBlogs.stream()
                .mapToInt(blog -> blog.getViewCount() != null ? blog.getViewCount() : 0)
                .sum();
        stats.put("totalViews", totalViews);
        
        // 总点赞数
        int totalLikes = allBlogs.stream()
                .mapToInt(blog -> blog.getLikeCount() != null ? blog.getLikeCount() : 0)
                .sum();
        stats.put("totalLikes", totalLikes);
        
        // 平均阅读量
        double avgViews = allBlogs.isEmpty() ? 0 : (double) totalViews / allBlogs.size();
        stats.put("avgViews", Math.round(avgViews * 100.0) / 100.0);
        
        // 平均点赞数
        double avgLikes = allBlogs.isEmpty() ? 0 : (double) totalLikes / allBlogs.size();
        stats.put("avgLikes", Math.round(avgLikes * 100.0) / 100.0);
        
        // 阅读量最高的文章
        if (!allBlogs.isEmpty()) {
            BlogEntity mostViewed = allBlogs.stream()
                    .max(Comparator.comparingInt(b -> b.getViewCount() != null ? b.getViewCount() : 0))
                    .orElse(null);
            
            if (mostViewed != null) {
                Map<String, Object> mostViewedInfo = new HashMap<>();
                mostViewedInfo.put("id", mostViewed.getId());
                mostViewedInfo.put("title", mostViewed.getTitle());
                mostViewedInfo.put("views", mostViewed.getViewCount());
                stats.put("mostViewedArticle", mostViewedInfo);
            }
            
            // 点赞最高的文章
            BlogEntity mostLiked = allBlogs.stream()
                    .max(Comparator.comparingInt(b -> b.getLikeCount() != null ? b.getLikeCount() : 0))
                    .orElse(null);
            
            if (mostLiked != null) {
                Map<String, Object> mostLikedInfo = new HashMap<>();
                mostLikedInfo.put("id", mostLiked.getId());
                mostLikedInfo.put("title", mostLiked.getTitle());
                mostLikedInfo.put("likes", mostLiked.getLikeCount());
                stats.put("mostLikedArticle", mostLikedInfo);
            }
        }
        
        return stats;
    }

    /**
     * 获取单篇文章的统计信息
     * 
     * @param blogId 文章 ID
     * @return 统计信息
     */
    public Map<String, Object> getArticleStatistics(Integer blogId) {
        BlogEntity blog = blogRepository.findById(blogId)
                .orElseThrow(() -> new IllegalArgumentException("文章不存在"));
        
        Map<String, Object> stats = new HashMap<>();
        stats.put("id", blog.getId());
        stats.put("title", blog.getTitle());
        stats.put("viewCount", blog.getViewCount());
        stats.put("likeCount", blog.getLikeCount());
        
        // 计算排名
        List<BlogEntity> allBlogs = blogRepository.findAll();
        
        // 阅读量排名
        int viewRank = 1;
        for (BlogEntity b : allBlogs) {
            if ((b.getViewCount() != null ? b.getViewCount() : 0) > 
                (blog.getViewCount() != null ? blog.getViewCount() : 0)) {
                viewRank++;
            }
        }
        stats.put("viewRank", viewRank);
        
        // 点赞量排名
        int likeRank = 1;
        for (BlogEntity b : allBlogs) {
            if ((b.getLikeCount() != null ? b.getLikeCount() : 0) > 
                (blog.getLikeCount() != null ? blog.getLikeCount() : 0)) {
                likeRank++;
            }
        }
        stats.put("likeRank", likeRank);
        
        return stats;
    }
}
