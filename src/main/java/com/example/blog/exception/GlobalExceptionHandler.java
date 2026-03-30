package com.example.blog.exception;

import com.example.blog.common.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 自定义异常
    @ExceptionHandler(BlogException.class)
    public Result<?> handleBlogException(BlogException e) {
        return Result.error(e.getMessage());
    }

    // 兜底异常
    @ExceptionHandler(Exception.class)
    public Result<?> handleException(Exception e) {
        e.printStackTrace();
        return Result.error("系统错误");
    }
}