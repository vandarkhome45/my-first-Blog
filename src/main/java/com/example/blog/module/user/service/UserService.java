package com.example.blog.module.user.service;

import com.example.blog.module.user.entity.User;
import com.example.blog.module.user.repository.UserRepository;
import com.example.blog.module.user.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 用户服务层
 * 模块：用户认证模块
 * 
 * 功能：
 * - 用户注册
 * - 用户登录
 * - Token 生成和验证
 * - 用户信息管理
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * BCrypt 密码加密器
     */
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * 用户注册
     * 
     * @param username 用户名
     * @param password 密码
     * @param email 邮箱
     * @return 用户信息（不含密码）
     */
    public User register(String username, String password, String email) {
        // 检查用户名是否已存在
        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("用户名已存在");
        }

        // 检查邮箱是否已存在
        if (email != null && !email.isEmpty() && userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("邮箱已被使用");
        }

        // 创建新用户
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password)); // 密码加密
        user.setEmail(email);
        user.setNickname(username); // 默认昵称为用户名
        user.setEnabled(true);

        return userRepository.save(user);
    }

    /**
     * 用户登录
     * 
     * @param username 用户名
     * @param password 密码
     * @return Token 和用户信息
     */
    public Map<String, Object> login(String username, String password) {
        // 查找用户
        Optional<User> userOptional = userRepository.findByUsername(username);
        
        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("用户名或密码错误");
        }

        User user = userOptional.get();

        // 检查账户状态
        if (!user.getEnabled()) {
            throw new IllegalArgumentException("账户已被禁用");
        }

        // 验证密码
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("用户名或密码错误");
        }

        // 更新最后登录时间
        user.setLastLoginTime(new Date());
        userRepository.save(user);

        // 生成 Token
        String token = jwtUtil.generateToken(user.getId(), user.getUsername(), user.getRole());

        // 返回结果（不包含密码）
        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("user", sanitizeUser(user));

        return result;
    }

    /**
     * 获取当前用户信息
     * 
     * @param token JWT Token
     * @return 用户信息
     */
    public User getCurrentUser(String token) {
        if (!jwtUtil.validateToken(token)) {
            throw new IllegalArgumentException("无效的 Token");
        }

        Integer userId = jwtUtil.getUserIdFromToken(token);
        Optional<User> userOptional = userRepository.findById(userId);

        return userOptional.orElseThrow(() -> new IllegalArgumentException("用户不存在"));
    }

    /**
     * 更新用户信息
     * 
     * @param userId 用户 ID
     * @param nickname 昵称
     * @param email 邮箱
     * @param phone 手机号
     * @param bio 个人简介
     * @return 更新后的用户信息
     */
    public User updateUser(Integer userId, String nickname, String email, String phone, String bio) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("用户不存在"));

        // 检查邮箱是否已被其他用户使用
        if (email != null && !email.isEmpty()) {
            Optional<User> existingUser = userRepository.findByEmail(email);
            if (existingUser.isPresent() && !existingUser.get().getId().equals(userId)) {
                throw new IllegalArgumentException("邮箱已被使用");
            }
            user.setEmail(email);
        }

        if (nickname != null) {
            user.setNickname(nickname);
        }

        if (phone != null) {
            user.setPhone(phone);
        }

        if (bio != null) {
            user.setBio(bio);
        }

        return userRepository.save(user);
    }

    /**
     * 修改密码
     * 
     * @param userId 用户 ID
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     */
    public void changePassword(Integer userId, String oldPassword, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("用户不存在"));

        // 验证旧密码
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new IllegalArgumentException("旧密码错误");
        }

        // 更新密码
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    /**
     * 移除用户对象中的敏感信息
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

    /**
     * 根据 ID 获取用户（公开信息）
     */
    public User getUserById(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("用户不存在"));
        return sanitizeUser(user);
    }

    /**
     * 根据用户名获取用户（公开信息）
     */
    public User getUserByUsername(String username) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        User user = userOptional.orElseThrow(() -> new IllegalArgumentException("用户不存在"));
        return sanitizeUser(user);
    }
}
