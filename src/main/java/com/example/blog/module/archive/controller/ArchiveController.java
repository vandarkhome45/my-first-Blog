package com.example.blog.module.archive.controller;

import com.example.blog.common.Result;
import com.example.blog.module.blog.entity.BlogEntity;
import com.example.blog.module.archive.service.ArchiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 归档和时间线控制器
 * 模块：归档和时间线模块
 * 
 * API 接口：
 * - GET /archive - 获取所有归档数据
 * - GET /archive/timeline - 获取时间线数据
 * - GET /archive/{year}/{month} - 获取指定年月的文章
 * - GET /archive/latest - 获取最新文章
 * - GET /archive/random - 获取随机推荐文章
 * - GET /archive/stats - 获取归档统计信息
 */
@RestController
@RequestMapping("/archive")
public class ArchiveController {

    @Autowired
    private ArchiveService archiveService;

    /**
     * 获取所有归档数据（按年月分组）
     * GET /archive
     * 
     * @return 归档数据
     */
    @GetMapping
    public Result<Map<String, List<BlogEntity>>> getAllArchives() {
        return Result.success(archiveService.getAllArchives());
    }

    /**
     * 获取时间线数据
     * GET /archive/timeline
     * 
     * @return 时间线数据（包含月份、文章数、文章列表）
     */
    @GetMapping("/timeline")
    public Result<List<Map<String, Object>>> getTimeline() {
        return Result.success(archiveService.getTimeline());
    }

    /**
     * 获取指定年月的归档文章
     * GET /archive/2024/1
     * 
     * @param year 年份
     * @param month 月份
     * @return 该月的文章列表
     */
    @GetMapping("/{year}/{month}")
    public Result<List<BlogEntity>> getArchiveByMonth(
            @PathVariable Integer year,
            @PathVariable Integer month) {
        
        List<BlogEntity> articles = archiveService.getArchiveByMonth(year, month);
        
        Map<String, Object> response = new HashMap<>();
        response.put("year", year);
        response.put("month", month);
        response.put("count", articles.size());
        response.put("articles", articles);
        
        return Result.success(articles);
    }

    /**
     * 获取最新文章
     * GET /archive/latest?limit=5
     * 
     * @param limit 返回数量限制（默认 10）
     * @return 最新文章列表
     */
    @GetMapping("/latest")
    public Result<List<BlogEntity>> getLatestArticles(
            @RequestParam(value = "limit", defaultValue = "10") int limit) {
        
        List<BlogEntity> latest = archiveService.getLatestArticles(limit);
        
        Map<String, Object> response = new HashMap<>();
        response.put("count", latest.size());
        response.put("articles", latest);
        
        return Result.success(latest);
    }

    /**
     * 获取随机推荐文章
     * GET /archive/random?limit=5
     * 
     * @param limit 返回数量限制（默认 5）
     * @return 随机文章列表
     */
    @GetMapping("/random")
    public Result<List<BlogEntity>> getRandomArticles(
            @RequestParam(value = "limit", defaultValue = "5") int limit) {
        
        List<BlogEntity> random = archiveService.getRandomArticles(limit);
        
        Map<String, Object> response = new HashMap<>();
        response.put("count", random.size());
        response.put("articles", random);
        
        return Result.success(random);
    }

    /**
     * 获取归档统计信息
     * GET /archive/stats
     * 
     * @return 统计信息
     */
    @GetMapping("/stats")
    public Result<Map<String, Object>> getArchiveStats() {
        return Result.success(archiveService.getArchiveStats());
    }
}
