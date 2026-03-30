package com.example.blog.module.comment.entity;

import jakarta.persistence.*;
import java.util.Date;

/**
 * 评论实体类
 * 模块：评论系统
 */
@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 评论内容
     */
    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    /**
     * 评论者昵称
     */
    @Column(length = 50, nullable = false)
    private String author;

    /**
     * 评论者邮箱
     */
    @Column(length = 100)
    private String email;

    /**
     * 关联的博客文章 ID
     */
    @Column(nullable = false)
    private Integer blogId;

    /**
     * 父评论 ID（用于回复功能，null 表示顶级评论）
     */
    private Integer parentId;

    /**
     * 创建时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;

    // ===== 构造方法 =====

    public Comment() {
    }

    // ===== 生命周期回调 =====

    @PrePersist
    protected void onCreate() {
        this.createTime = new Date();
    }

    // ===== Getter/Setter =====

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getBlogId() {
        return blogId;
    }

    public void setBlogId(Integer blogId) {
        this.blogId = blogId;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", author='" + author + '\'' +
                ", blogId=" + blogId +
                ", parentId=" + parentId +
                ", createTime=" + createTime +
                '}';
    }
}
