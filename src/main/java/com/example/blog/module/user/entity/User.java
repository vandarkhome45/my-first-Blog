package com.example.blog.module.user.entity;

import jakarta.persistence.*;
import java.util.Date;

/**
 * 用户实体类
 * 模块：用户认证模块
 * 
 * 功能：
 * - 用户基本信息存储
 * - 密码加密存储
 * - 角色管理
 */
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 用户名（唯一）
     */
    @Column(length = 50, nullable = false, unique = true)
    private String username;

    /**
     * 密码（加密存储）
     */
    @Column(length = 255, nullable = false)
    private String password;

    /**
     * 邮箱
     */
    @Column(length = 100, unique = true)
    private String email;

    /**
     * 手机号
     */
    @Column(length = 20)
    private String phone;

    /**
     * 昵称
     */
    @Column(length = 50)
    private String nickname;

    /**
     * 头像 URL
     */
    @Column(length = 255)
    private String avatar;

    /**
     * 个人简介
     */
    @Column(length = 500)
    private String bio;

    /**
     * 角色：ROLE_ADMIN（管理员）、ROLE_USER（普通用户）
     */
    @Column(length = 20, nullable = false)
    private String role;

    /**
     * 账户状态：ENABLED（启用）、DISABLED（禁用）
     */
    @Column(nullable = false)
    private Boolean enabled;

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

    /**
     * 最后登录时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastLoginTime;

    // ========== Getter 和 Setter ==========

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
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

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    // ========== 生命周期回调 ==========

    @PrePersist
    protected void onCreate() {
        this.createTime = new Date();
        this.updateTime = new Date();
        if (this.role == null) {
            this.role = "ROLE_USER"; // 默认普通用户
        }
        if (this.enabled == null) {
            this.enabled = true; // 默认启用
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updateTime = new Date();
    }

    // ========== 辅助方法 ==========

    /**
     * 检查是否是管理员
     */
    public boolean isAdmin() {
        return "ROLE_ADMIN".equals(this.role);
    }

    /**
     * 检查账户是否启用
     */
    public boolean isEnabled() {
        return this.enabled;
    }
}
