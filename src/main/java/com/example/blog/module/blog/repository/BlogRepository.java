package com.example.blog.module.blog.repository;

import com.example.blog.module.blog.entity.BlogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 博客文章数据访问层
 * 模块：博客核心模块
 */
@Repository
public interface BlogRepository extends JpaRepository<BlogEntity, Integer> {
    
    /**
     * 按分类查询博客
     * @param category 分类名称
     * @return 博客列表
     */
    List<BlogEntity> findByCategory(String category);
    
    /**
     * 按标签模糊查询
     * @param tag 标签关键词
     * @return 博客列表
     */
    List<BlogEntity> findByTagsContaining(String tag);
}
