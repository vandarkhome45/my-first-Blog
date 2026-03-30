package com.example.blog.module.blog.entity;

import jakarta.persistence.*;
import java.util.Date;

/**
 * 博客文章实体
 * 模块：博客核心模块
 */
@Entity
@Table(name = "blogs")
public class BlogEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 文章标题
     */
    @Column(length = 200, nullable = false)
    private String title;

    /**
     * 文章内容
     */
    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    /**
     * 文章原始内容（Markdown 格式）
     */
    @Column(columnDefinition = "TEXT")
    private String originalContent;

    /**
     * 渲染后的 HTML 内容
     */
    @Column(columnDefinition = "LONGTEXT")
    private String renderedContent;

    /**
     * 文章标签（逗号分隔）
     */
    @Column(length = 500)
    private String tags;

    /**
     * 文章分类
     */
    @Column(length = 50)
    private String category;

    /**
     * 阅读量（访问次数）
     */
    @Column(nullable = false)
    private Integer viewCount;

    /**
     * 点赞量
     */
    @Column(nullable = false)
    private Integer likeCount;

    /**
     * 创建时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;

    /**
     * 更新时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTime;

    // ===== 构造方法 =====

    public BlogEntity() {
    }

    // ===== 生命周期回调 =====

    @PrePersist
    protected void onCreate() {
        this.createTime = new Date();
        this.updateTime = new Date();
        // 初始化统计字段
        if (this.viewCount == null) {
            this.viewCount = 0;
        }
        if (this.likeCount == null) {
            this.likeCount = 0;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updateTime = new Date();
    }

    // ===== Getter/Setter =====

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getOriginalContent() {
        return originalContent;
    }

    public void setOriginalContent(String originalContent) {
        this.originalContent = originalContent;
    }

    public String getRenderedContent() {
        return renderedContent;
    }

    public void setRenderedContent(String renderedContent) {
        this.renderedContent = renderedContent;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getViewCount() {
        return viewCount;
    }

    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }

    public Integer getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
    }

    @Override
    public String toString() {
        return "BlogEntity{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", category='" + category + '\'' +
                ", tags='" + tags + '\'' +
                ", createTime=" + createTime +
                '}';
    }
}
