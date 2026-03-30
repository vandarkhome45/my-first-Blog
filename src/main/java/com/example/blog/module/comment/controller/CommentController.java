package com.example.blog.module.comment.controller;

import com.example.blog.common.Result;
import com.example.blog.module.comment.dto.CommentDTO;
import com.example.blog.module.comment.entity.Comment;
import com.example.blog.module.comment.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 评论控制器
 * 模块：评论模块
 *
 * API 接口：
 * - POST /comment?blogId=1      新增评论
 * - GET  /comment/list?blogId=1 查询博客的所有评论
 * - GET  /comment/{id}          查询单条评论
 * - DELETE /comment/{id}        删除评论
 * - POST /comment/reply?parentId=1  回复评论
 * - GET  /comment/replies?parentId=1 查看回复列表
 */
@RestController
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    /**
     * 新增评论
     * POST /comment?blogId=1
     */
    @PostMapping
    public Result<Comment> addComment(
            @RequestParam Integer blogId,
            @RequestBody CommentDTO dto) {

        if (dto.getContent() == null || dto.getContent().trim().isEmpty()) {
            return Result.error("评论内容不能为空");
        }
        if (dto.getAuthor() == null || dto.getAuthor().trim().isEmpty()) {
            return Result.error("评论者昵称不能为空");
        }

        Comment comment = commentService.addComment(blogId, dto);
        return Result.success(comment);
    }

    /**
     * 查询某篇博客的所有顶级评论
     * GET /comment/list?blogId=1
     */
    @GetMapping("/list")
    public Result<List<Comment>> getCommentsByBlogId(@RequestParam Integer blogId) {
        List<Comment> comments = commentService.getCommentsByBlogId(blogId);
        return Result.success(comments);
    }

    /**
     * 查询单条评论
     * GET /comment/{id}
     */
    @GetMapping("/{id}")
    public Result<Comment> getComment(@PathVariable Integer id) {
        Comment comment = commentService.getCommentById(id);
        if (comment == null) {
            return Result.error("评论不存在");
        }
        return Result.success(comment);
    }

    /**
     * 删除评论
     * DELETE /comment/{id}
     */
    @DeleteMapping("/{id}")
    public Result<String> deleteComment(@PathVariable Integer id) {
        Comment comment = commentService.getCommentById(id);
        if (comment == null) {
            return Result.error("评论不存在");
        }
        commentService.deleteComment(id);
        return Result.success("删除成功");
    }

    /**
     * 回复评论
     * POST /comment/reply?parentId=1
     */
    @PostMapping("/reply")
    public Result<Comment> replyComment(
            @RequestParam Integer parentId,
            @RequestBody CommentDTO dto) {

        if (dto.getContent() == null || dto.getContent().trim().isEmpty()) {
            return Result.error("回复内容不能为空");
        }
        if (dto.getAuthor() == null || dto.getAuthor().trim().isEmpty()) {
            return Result.error("回复者昵称不能为空");
        }

        try {
            Comment reply = commentService.replyComment(parentId, dto);
            return Result.success(reply);
        } catch (IllegalArgumentException e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 查看某条评论的所有回复
     * GET /comment/replies?parentId=1
     */
    @GetMapping("/replies")
    public Result<List<Comment>> getReplies(@RequestParam Integer parentId) {
        List<Comment> replies = commentService.getReplies(parentId);
        return Result.success(replies);
    }
}
