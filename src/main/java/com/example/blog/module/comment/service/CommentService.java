package com.example.blog.module.comment.service;

import com.example.blog.module.comment.dto.CommentDTO;
import com.example.blog.module.comment.entity.Comment;
import com.example.blog.module.comment.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 评论服务层
 * 模块：评论模块
 */
@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    /**
     * 新增评论（顶级评论）
     */
    public Comment addComment(Integer blogId, CommentDTO dto) {
        Comment comment = new Comment();
        comment.setBlogId(blogId);
        comment.setContent(dto.getContent());
        comment.setAuthor(dto.getAuthor());
        comment.setEmail(dto.getEmail());
        return commentRepository.save(comment);
    }

    /**
     * 回复评论
     */
    public Comment replyComment(Integer parentId, CommentDTO dto) {
        // 找到父评论，获取其 blogId
        Comment parent = commentRepository.findById(parentId)
                .orElseThrow(() -> new IllegalArgumentException("父评论不存在"));

        Comment reply = new Comment();
        reply.setBlogId(parent.getBlogId());
        reply.setParentId(parentId);
        reply.setContent(dto.getContent());
        reply.setAuthor(dto.getAuthor());
        reply.setEmail(dto.getEmail());
        return commentRepository.save(reply);
    }

    /**
     * 查询某篇博客的顶级评论列表
     */
    public List<Comment> getCommentsByBlogId(Integer blogId) {
        return commentRepository.findByBlogIdAndParentIdIsNullOrderByCreateTimeDesc(blogId);
    }

    /**
     * 查询单条评论
     */
    public Comment getCommentById(Integer id) {
        return commentRepository.findById(id).orElse(null);
    }

    /**
     * 查询某条评论的所有回复
     */
    public List<Comment> getReplies(Integer parentId) {
        return commentRepository.findByParentIdOrderByCreateTimeAsc(parentId);
    }

    /**
     * 删除评论（同时删除其所有回复）
     */
    public void deleteComment(Integer id) {
        // 先删除该评论的所有回复
        List<Comment> replies = commentRepository.findByParentIdOrderByCreateTimeAsc(id);
        commentRepository.deleteAll(replies);
        // 再删除评论本身
        commentRepository.deleteById(id);
    }

    /**
     * 统计某篇博客的评论数（含回复）
     */
    public long countByBlogId(Integer blogId) {
        return commentRepository.countByBlogId(blogId);
    }
}
