package com.example.blog.module.search.controller;

import com.example.blog.common.Result;
import com.example.blog.module.blog.entity.BlogEntity;
import com.example.blog.module.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 搜索控制器
 * 模块：搜索模块
 * 
 * API 接口：
 * - GET /search?q=keyword - 全文搜索
 * - GET /search/advanced - 高级搜索
 * - GET /search/category/{category} - 按分类搜索
 * - POST /search/highlight - 关键词高亮
 */
@RestController
@RequestMapping("/search")
public class SearchController {

    @Autowired
    private SearchService searchService;

    /**
     * 全文搜索
     * GET /search?q=Spring Boot
     * 
     * @param keyword 搜索关键词
     * @return 搜索结果
     */
    @GetMapping
    public Result<List<BlogEntity>> search(
            @RequestParam("q") String keyword) {
        
        List<BlogEntity> results = searchService.search(keyword);
        
        // 构建响应数据
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("keyword", keyword);
        responseData.put("count", results.size());
        responseData.put("results", results);
        
        if (results.isEmpty()) {
            return Result.error("未找到相关结果");
        }
        
        return Result.success(results);
    }

    /**
     * 高级搜索
     * GET /search/advanced?q=keyword&scope=title
     * 
     * @param keyword 搜索关键词
     * @param scope 搜索范围（title/content/all）
     * @return 搜索结果
     */
    @GetMapping("/advanced")
    public Result<Map<String, Object>> advancedSearch(
            @RequestParam("q") String keyword,
            @RequestParam(value = "scope", defaultValue = "all") String scope) {
        
        List<BlogEntity> results = searchService.advancedSearch(keyword, scope);
        
        Map<String, Object> response = new HashMap<>();
        response.put("keyword", keyword);
        response.put("scope", scope);
        response.put("count", results.size());
        response.put("results", results);
        
        return Result.success(response);
    }

    /**
     * 按分类搜索
     * GET /search/category/Java?q=Spring
     * 
     * @param category 分类名称
     * @param keyword 搜索关键词
     * @return 搜索结果
     */
    @GetMapping("/category/{category}")
    public Result<List<BlogEntity>> searchByCategory(
            @PathVariable String category,
            @RequestParam(value = "q", required = false) String keyword) {
        
        List<BlogEntity> results = searchService.searchByCategory(keyword, category);
        
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("category", category);
        responseData.put("keyword", keyword);
        responseData.put("count", results.size());
        responseData.put("results", results);
        
        return Result.success(results);
    }

    /**
     * 获取带高亮的搜索结果
     * POST /search/highlight
     * Body: {"keyword": "Spring", "text": "原始内容"}
     * 
     * @param params 参数（keyword 和 text）
     * @return 高亮后的文本
     */
    @PostMapping("/highlight")
    public Result<Map<String, String>> highlight(
            @RequestBody Map<String, String> params) {
        
        String keyword = params.get("keyword");
        String text = params.get("text");
        
        if (keyword == null || text == null) {
            return Result.error("缺少必要参数");
        }
        
        String highlightedText = searchService.highlightKeyword(text, keyword);
        
        Map<String, String> response = new HashMap<>();
        response.put("original", text);
        response.put("highlighted", highlightedText);
        response.put("keyword", keyword);
        
        return Result.success(response);
    }

    /**
     * 搜索建议（自动补全）
     * GET /search/suggest?q=Spr
     * 
     * @param keyword 输入的前缀
     * @return 建议的关键词列表
     */
    @GetMapping("/suggest")
    public Result<List<String>> suggest(@RequestParam("q") String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return Result.success(List.of());
        }

        // 从现有博客标题中提取建议
        List<BlogEntity> allBlogs = searchService.search(keyword);
        List<String> suggestions = allBlogs.stream()
                .map(BlogEntity::getTitle)
                .filter(title -> title != null && title.toLowerCase().contains(keyword.toLowerCase()))
                .limit(5)  // 最多返回 5 条建议
                .toList();
        
        return Result.success(suggestions);
    }

    /**
     * 热门搜索统计
     * GET /search/popular
     * 
     * @return 热门搜索关键词（可以从日志或数据库中统计）
     * 注：当前版本返回固定值，后续可以集成统计模块
     */
    @GetMapping("/popular")
    public Result<List<String>> getPopularSearches() {
        // 临时实现，后续可以从数据库统计
        List<String> popularKeywords = List.of(
            "Spring Boot",
            "Java",
            "后端开发",
            "编程",
            "技术"
        );
        
        return Result.success(popularKeywords);
    }
}
