package com.example.blog.module.statistics.controller;

import com.example.blog.common.Result;
import com.example.blog.module.blog.entity.BlogEntity;
import com.example.blog.module.statistics.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 访问统计控制器
 * 模块：访问统计模块
 * 
 * API 接口：
 * - POST /statistics/view/{id} - 增加阅读量
 * - POST /statistics/like/{id} - 点赞文章
 * - POST /statistics/unlike/{id} - 取消点赞
 * - GET /statistics/hot - 热门文章排行
 * - GET /statistics/most-liked - 最多点赞文章
 * - GET /statistics/total - 全站统计信息
 * - GET /statistics/article/{id} - 单篇文章统计
 */
@RestController
@RequestMapping("/statistics")
public class StatisticsController {

    @Autowired
    private StatisticsService statisticsService;

    /**
     * 增加文章阅读量
     * POST /statistics/view/{id}
     * 
     * @param blogId 文章 ID
     * @return 更新后的文章
     */
    @PostMapping("/view/{id}")
    public Result<BlogEntity> incrementViewCount(@PathVariable Integer blogId) {
        try {
            BlogEntity updatedBlog = statisticsService.incrementViewCount(blogId);
            return Result.success(updatedBlog);
        } catch (IllegalArgumentException e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 点赞文章
     * POST /statistics/like/{id}
     * 
     * @param blogId 文章 ID
     * @return 更新后的文章
     */
    @PostMapping("/like/{id}")
    public Result<BlogEntity> likeArticle(@PathVariable Integer blogId) {
        try {
            BlogEntity updatedBlog = statisticsService.likeArticle(blogId);
            return Result.success(updatedBlog);
        } catch (IllegalArgumentException e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 取消点赞
     * POST /statistics/unlike/{id}
     * 
     * @param blogId 文章 ID
     * @return 更新后的文章
     */
    @PostMapping("/unlike/{id}")
    public Result<BlogEntity> unlikeArticle(@PathVariable Integer blogId) {
        try {
            BlogEntity updatedBlog = statisticsService.unlikeArticle(blogId);
            return Result.success(updatedBlog);
        } catch (IllegalArgumentException e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 获取热门文章排行（按阅读量）
     * GET /statistics/hot?limit=10
     * 
     * @param limit 返回数量限制（默认 10）
     * @return 热门文章列表
     */
    @GetMapping("/hot")
    public Result<List<BlogEntity>> getHotArticles(
            @RequestParam(value = "limit", defaultValue = "10") int limit) {
        List<BlogEntity> hotArticles = statisticsService.getHotArticles(limit);
        return Result.success(hotArticles);
    }

    /**
     * 获取最多点赞的文章
     * GET /statistics/most-liked?limit=10
     * 
     * @param limit 返回数量限制（默认 10）
     * @return 最多点赞的文章列表
     */
    @GetMapping("/most-liked")
    public Result<List<BlogEntity>> getMostLikedArticles(
            @RequestParam(value = "limit", defaultValue = "10") int limit) {
        List<BlogEntity> mostLiked = statisticsService.getMostLikedArticles(limit);
        return Result.success(mostLiked);
    }

    /**
     * 获取全站统计信息
     * GET /statistics/total
     * 
     * @return 统计数据
     */
    @GetMapping("/total")
    public Result<Map<String, Object>> getTotalStatistics() {
        Map<String, Object> stats = statisticsService.getTotalStatistics();
        return Result.success(stats);
    }

    /**
     * 获取单篇文章的统计信息
     * GET /statistics/article/{id}
     * 
     * @param blogId 文章 ID
     * @return 统计信息
     */
    @GetMapping("/article/{id}")
    public Result<Map<String, Object>> getArticleStatistics(@PathVariable Integer blogId) {
        try {
            Map<String, Object> stats = statisticsService.getArticleStatistics(blogId);
            return Result.success(stats);
        } catch (IllegalArgumentException e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 批量获取多篇文章的统计信息
     * POST /statistics/batch
     * Body: {"ids": [1, 2, 3]}
     * 
     * @param params 文章 ID 列表
     * @return 统计信息列表
     */
    @PostMapping("/batch")
    public Result<List<Map<String, Object>>> getBatchStatistics(
            @RequestBody Map<String, List<Integer>> params) {
        
        List<Integer> ids = params.get("ids");
        if (ids == null || ids.isEmpty()) {
            return Result.error("请提供文章 ID 列表");
        }

        List<Map<String, Object>> results = ids.stream()
                .map(id -> {
                    try {
                        return statisticsService.getArticleStatistics(id);
                    } catch (Exception e) {
                        Map<String, Object> error = new HashMap<>();
                        error.put("id", id);
                        error.put("error", e.getMessage());
                        return error;
                    }
                })
                .collect(Collectors.toList());

        return Result.success(results);
    }
}
