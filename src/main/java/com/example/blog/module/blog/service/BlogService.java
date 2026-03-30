package com.example.blog.module.blog.service;

import com.example.blog.module.blog.entity.BlogEntity;
import com.example.blog.module.blog.repository.BlogRepository;
import com.example.blog.common.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 博客文章服务层
 * 模块：博客核心模块
 */
@Service
public class BlogService {

    @Autowired
    private BlogRepository blogRepository;

    /**
     * 新增或更新博客
     * @param blog 博客实体
     * @return 保存后的博客
     */
    public BlogEntity save(BlogEntity blog) {
        return blogRepository.save(blog);
    }

    /**
     * 查询所有博客
     * @return 博客列表
     */
    public List<BlogEntity> findAll() {
        return blogRepository.findAll();
    }

    /**
     * 根据 ID 查询博客
     * @param id 博客 ID
     * @return 博客实体，不存在返回 null
     */
    public BlogEntity findById(Integer id) {
        return blogRepository.findById(id).orElse(null);
    }

    /**
     * 删除博客
     * @param id 博客 ID
     */
    public void deleteById(Integer id) {
        blogRepository.deleteById(id);
    }

    /**
     * 分页查询
     * @param page 页码（从 0 开始）
     * @param size 每页大小
     * @return 分页结果
     */
    public PageResult<BlogEntity> page(int page, int size) {
        Page<BlogEntity> pageData = blogRepository.findAll(PageRequest.of(page, size));
        return new PageResult<>(
                pageData.getContent(),
                pageData.getTotalElements()
        );
    }

    /**
     * 按分类查询
     * @param category 分类名称
     * @return 博客列表
     */
    public List<BlogEntity> findByCategory(String category) {
        return blogRepository.findByCategory(category);
    }

    /**
     * 按标签查询
     * @param tag 标签关键词
     * @return 博客列表
     */
    public List<BlogEntity> findByTag(String tag) {
        return blogRepository.findByTagsContaining(tag);
    }
}
