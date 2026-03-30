package com.example.blog.module.user.controller;

import com.example.blog.common.Result;
import com.example.blog.module.user.entity.User;
import com.example.blog.module.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户认证控制器
 * 模块：用户认证模块
 * 
 * API 接口：
 * - POST /user/register - 用户注册
 * - POST /user/login - 用户登录
 * - GET /user/me - 获取当前用户信息
 * - PUT /user/profile - 更新用户资料
 * - PUT /user/password - 修改密码
 * - GET /user/{id} - 获取用户公开信息
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 用户注册
     * POST /user/register
     * Body: {"username": "张三", "password": "123456", "email": "zhangsan@example.com"}
     * 
     * @param params 注册参数
     * @return 注册结果
     */
    @PostMapping("/register")
    public Result<Map<String, Object>> register(@RequestBody Map<String, String> params) {
        String username = params.get("username");
        String password = params.get("password");
        String email = params.get("email");

        // 参数验证
        if (username == null || username.trim().isEmpty()) {
            return Result.error("用户名不能为空");
        }

        if (password == null || password.length() < 6) {
            return Result.error("密码长度至少为 6 位");
        }

        try {
            User user = userService.register(username.trim(), password, email);
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "注册成功");
            response.put("user", sanitizeUser(user));
            
            return Result.success(response);
        } catch (IllegalArgumentException e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 用户登录
     * POST /user/login
     * Body: {"username": "张三", "password": "123456"}
     * 
     * @param params 登录参数
     * @return Token 和用户信息
     */
    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody Map<String, String> params) {
        String username = params.get("username");
        String password = params.get("password");

        // 参数验证
        if (username == null || username.trim().isEmpty()) {
            return Result.error("用户名不能为空");
        }

        if (password == null || password.isEmpty()) {
            return Result.error("密码不能为空");
        }

        try {
            Map<String, Object> result = userService.login(username.trim(), password);
            return Result.success(result);
        } catch (IllegalArgumentException e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 获取当前用户信息
     * GET /user/me
     * Header: Authorization: Bearer {token}
     * 
     * @param request HttpServletRequest
     * @return 当前用户信息
     */
    @GetMapping("/me")
    public Result<User> getCurrentUser(jakarta.servlet.http.HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        
        if (header == null || !header.startsWith("Bearer ")) {
            return Result.error("未提供 Token");
        }

        String token = header.substring(7);

        try {
            User user = userService.getCurrentUser(token);
            return Result.success(user);
        } catch (IllegalArgumentException e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 更新用户资料
     * PUT /user/profile
     * Header: Authorization: Bearer {token}
     * Body: {"nickname": "小张", "email": "zhang@example.com", "phone": "13800138000", "bio": "个人简介"}
     * 
     * @param request HttpServletRequest
     * @param params 更新参数
     * @return 更新后的用户信息
     */
    @PutMapping("/profile")
    public Result<User> updateProfile(
            jakarta.servlet.http.HttpServletRequest request,
            @RequestBody Map<String, String> params) {
        
        String header = request.getHeader("Authorization");
        
        if (header == null || !header.startsWith("Bearer ")) {
            return Result.error("未提供 Token");
        }

        String token = header.substring(7);

        try {
            User currentUser = userService.getCurrentUser(token);
            String nickname = params.get("nickname");
            String email = params.get("email");
            String phone = params.get("phone");
            String bio = params.get("bio");

            User updatedUser = userService.updateUser(
                    currentUser.getId(), 
                    nickname, 
                    email, 
                    phone, 
                    bio
            );

            return Result.success(updatedUser);
        } catch (IllegalArgumentException e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 修改密码
     * PUT /user/password
     * Header: Authorization: Bearer {token}
     * Body: {"oldPassword": "旧密码", "newPassword": "新密码"}
     * 
     * @param request HttpServletRequest
     * @param params 密码参数
     * @return 修改结果
     */
    @PutMapping("/password")
    public Result<String> changePassword(
            jakarta.servlet.http.HttpServletRequest request,
            @RequestBody Map<String, String> params) {
        
        String header = request.getHeader("Authorization");
        
        if (header == null || !header.startsWith("Bearer ")) {
            return Result.error("未提供 Token");
        }

        String token = header.substring(7);

        try {
            User currentUser = userService.getCurrentUser(token);
            String oldPassword = params.get("oldPassword");
            String newPassword = params.get("newPassword");

            if (oldPassword == null || newPassword == null) {
                return Result.error("请提供旧密码和新密码");
            }

            if (newPassword.length() < 6) {
                return Result.error("新密码长度至少为 6 位");
            }

            userService.changePassword(currentUser.getId(), oldPassword, newPassword);
            return Result.success("密码修改成功");
        } catch (IllegalArgumentException e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 获取用户公开信息
     * GET /user/{id}
     * 
     * @param userId 用户 ID
     * @return 用户公开信息
     */
    @GetMapping("/{id}")
    public Result<User> getUserById(@PathVariable Integer id) {
        try {
            User user = userService.getUserById(id);
            return Result.success(user);
        } catch (IllegalArgumentException e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 移除用户对象中的敏感信息（密码等）
     */
    private User sanitizeUser(User user) {
        User sanitized = new User();
        sanitized.setId(user.getId());
        sanitized.setUsername(user.getUsername());
        sanitized.setEmail(user.getEmail());
        sanitized.setPhone(user.getPhone());
        sanitized.setNickname(user.getNickname());
        sanitized.setAvatar(user.getAvatar());
        sanitized.setBio(user.getBio());
        sanitized.setRole(user.getRole());
        sanitized.setCreateTime(user.getCreateTime());
        sanitized.setLastLoginTime(user.getLastLoginTime());
        return sanitized;
    }
}
