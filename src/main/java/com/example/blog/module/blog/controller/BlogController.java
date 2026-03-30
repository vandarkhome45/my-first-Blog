package com.example.blog.module.blog.controller;

import com.example.blog.common.Result;
import com.example.blog.module.blog.entity.BlogEntity;
import com.example.blog.module.blog.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 博客文章控制器
 * 模块：博客核心模块
 */
@RestController
@RequestMapping("/blog")
public class BlogController {

    @Autowired
    private BlogService blogService;

    /**
     * 新增博客
     * POST /blog
     */
    @PostMapping
    public Result<BlogEntity> createBlog(@RequestBody BlogEntity blog) {
        return Result.success(blogService.save(blog));
    }

    /**
     * 查询所有博客
     * GET /blog
     */
    @GetMapping
    public Result<List<BlogEntity>> getAllBlogs() {
        return Result.success(blogService.findAll());
    }

    /**
     * 根据 ID 查询博客
     * GET /blog/{id}
     */
    @GetMapping("/{id}")
    public Result<BlogEntity> getBlog(@PathVariable Integer id) {
        BlogEntity blog = blogService.findById(id);
        if (blog == null) {
            return Result.error("博客不存在");
        }
        return Result.success(blog);
    }

    /**
     * 删除博客
     * DELETE /blog/{id}
     */
    @DeleteMapping("/{id}")
    public Result<String> deleteBlog(@PathVariable Integer id) {
        blogService.deleteById(id);
        return Result.success("删除成功");
    }

    /**
     * 更新博客
     * PUT /blog/{id}
     */
    @PutMapping("/{id}")
    public Result<BlogEntity> updateBlog(
            @PathVariable Integer id, 
            @RequestBody BlogEntity blog) {
        
        BlogEntity oldBlog = blogService.findById(id);
        if (oldBlog == null) {
            return Result.error("博客不存在");
        }

        oldBlog.setTitle(blog.getTitle());
        oldBlog.setContent(blog.getContent());
        oldBlog.setCategory(blog.getCategory());
        oldBlog.setTags(blog.getTags());

        return Result.success(blogService.save(oldBlog));
    }

    /**
     * 分页查询
     * GET /blog/page?page=0&size=10
     */
    @GetMapping("/page")
    public Result<com.example.blog.common.PageResult<BlogEntity>> page(
            @RequestParam int page,
            @RequestParam int size) {
        return Result.success(blogService.page(page, size));
    }

    /**
     * 按分类查询
     * GET /blog/category?category=Java
     */
    @GetMapping("/category")
    public Result<List<BlogEntity>> getByCategory(
            @RequestParam String category) {
        return Result.success(blogService.findByCategory(category));
    }

    /**
     * 按标签查询
     * GET /blog/tag?tag=Spring
     */
    @GetMapping("/tag")
    public Result<List<BlogEntity>> getByTag(
            @RequestParam String tag) {
        return Result.success(blogService.findByTag(tag));
    }
}
