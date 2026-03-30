package com.example.blog.module.comment.repository;

import com.example.blog.module.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 评论数据访问层
 * 模块：评论模块
 */
@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {

    /**
     * 查询某篇博客的所有顶级评论（按创建时间倒序）
     */
    List<Comment> findByBlogIdAndParentIdIsNullOrderByCreateTimeDesc(Integer blogId);

    /**
     * 查询某条评论的所有回复
     */
    List<Comment> findByParentIdOrderByCreateTimeAsc(Integer parentId);

    /**
     * 查询某篇博客的评论总数
     */
    long countByBlogId(Integer blogId);
}
