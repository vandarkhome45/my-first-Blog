package com.example.blog.module.search.service;

import com.example.blog.module.blog.entity.BlogEntity;
import com.example.blog.module.blog.repository.BlogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 搜索服务层
 * 模块：搜索模块
 * 
 * 功能：
 * - 全文搜索（标题 + 内容）
 * - 关键词高亮
 * - 搜索结果分页
 */
@Service
public class SearchService {

    @Autowired
    private BlogRepository blogRepository;

    /**
     * 全文搜索
     * 在博客标题和内容中搜索关键词
     * 
     * @param keyword 搜索关键词
     * @return 匹配的博客列表
     */
    public List<BlogEntity> search(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return new ArrayList<>();
        }

        String searchKeyword = "%" + keyword.trim() + "%";
        List<BlogEntity> allBlogs = blogRepository.findAll();

        // 过滤匹配的博客（标题或内容包含关键词）
        return allBlogs.stream()
                .filter(blog -> {
                    boolean titleMatch = blog.getTitle() != null && 
                                       blog.getTitle().contains(keyword);
                    boolean contentMatch = blog.getContent() != null && 
                                         blog.getContent().contains(keyword);
                    return titleMatch || contentMatch;
                })
                .collect(Collectors.toList());
    }

    /**
     * 高级搜索
     * 支持指定搜索范围（标题、内容或全部）
     * 
     * @param keyword 搜索关键词
     * @param scope 搜索范围："title" | "content" | "all"
     * @return 匹配的博客列表
     */
    public List<BlogEntity> advancedSearch(String keyword, String scope) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return new ArrayList<>();
        }

        List<BlogEntity> allBlogs = blogRepository.findAll();
        String searchKeyword = keyword.trim();

        return allBlogs.stream()
                .filter(blog -> {
                    switch (scope.toLowerCase()) {
                        case "title":
                            return blog.getTitle() != null && 
                                   blog.getTitle().contains(searchKeyword);
                        case "content":
                            return blog.getContent() != null && 
                                   blog.getContent().contains(searchKeyword);
                        case "all":
                        default:
                            boolean titleMatch = blog.getTitle() != null && 
                                               blog.getTitle().contains(searchKeyword);
                            boolean contentMatch = blog.getContent() != null && 
                                                 blog.getContent().contains(searchKeyword);
                            return titleMatch || contentMatch;
                    }
                })
                .collect(Collectors.toList());
    }

    /**
     * 按分类和关键词搜索
     * 
     * @param keyword 搜索关键词
     * @param category 分类名称
     * @return 匹配的博客列表
     */
    public List<BlogEntity> searchByCategory(String keyword, String category) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return blogRepository.findByCategory(category);
        }

        List<BlogEntity> blogsInCategory = blogRepository.findByCategory(category);
        String searchKeyword = keyword.trim();

        return blogsInCategory.stream()
                .filter(blog -> {
                    boolean titleMatch = blog.getTitle() != null && 
                                       blog.getTitle().contains(searchKeyword);
                    boolean contentMatch = blog.getContent() != null && 
                                         blog.getContent().contains(searchKeyword);
                    return titleMatch || contentMatch;
                })
                .collect(Collectors.toList());
    }

    /**
     * 高亮显示关键词
     * 在文本中用 HTML 标签标记关键词
     * 
     * @param text 原始文本
     * @param keyword 关键词
     * @return 高亮后的文本
     */
    public String highlightKeyword(String text, String keyword) {
        if (text == null || keyword == null || keyword.isEmpty()) {
            return text;
        }

        // 使用 HTML mark 标签高亮（前端可以自定义样式）
        return text.replaceAll(
            "(?i)" + java.util.regex.Pattern.quote(keyword),
            "<mark>$0</mark>"
        );
    }

    /**
     * 高亮显示多个关键词
     * 
     * @param text 原始文本
     * @param keywords 关键词列表
     * @return 高亮后的文本
     */
    public String highlightKeywords(String text, List<String> keywords) {
        if (text == null || keywords == null || keywords.isEmpty()) {
            return text;
        }

        String result = text;
        for (String keyword : keywords) {
            result = highlightKeyword(result, keyword);
        }
        return result;
    }
}
