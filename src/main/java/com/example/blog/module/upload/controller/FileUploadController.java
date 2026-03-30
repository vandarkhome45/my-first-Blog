package com.example.blog.module.upload.controller;

import com.example.blog.common.Result;
import com.example.blog.module.upload.service.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 文件上传控制器
 * 模块：文件上传模块
 * 
 * API 接口：
 * - POST /upload/image - 上传图片
 * - POST /upload/document - 上传文档
 * - POST /upload/file - 上传任意文件
 * - POST /upload/images - 批量上传图片
 * - DELETE /upload/{filename} - 删除文件
 * - GET /uploads/** - 访问上传的文件
 */
@RestController
@RequestMapping("/upload")
public class FileUploadController {

    @Autowired
    private FileUploadService fileUploadService;

    /**
     * 上传图片文件
     * POST /upload/image
     * 
     * @param file 上传的图片文件
     * @return 文件访问 URL
     */
    @PostMapping("/image")
    public Result<Map<String, String>> uploadImage(
            @RequestParam("file") MultipartFile file) {
        
        try {
            String url = fileUploadService.uploadImage(file);
            
            Map<String, String> response = new HashMap<>();
            response.put("url", url);
            response.put("filename", file.getOriginalFilename());
            response.put("size", String.valueOf(file.getSize()));
            
            return Result.success(response);
        } catch (IOException e) {
            return Result.error("上传失败：" + e.getMessage());
        } catch (IllegalArgumentException e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 上传文档文件
     * POST /upload/document
     * 
     * @param file 上传的文档文件
     * @return 文件访问 URL
     */
    @PostMapping("/document")
    public Result<Map<String, String>> uploadDocument(
            @RequestParam("file") MultipartFile file) {
        
        try {
            String url = fileUploadService.uploadDocument(file);
            
            Map<String, String> response = new HashMap<>();
            response.put("url", url);
            response.put("filename", file.getOriginalFilename());
            response.put("size", String.valueOf(file.getSize()));
            
            return Result.success(response);
        } catch (IOException e) {
            return Result.error("上传失败：" + e.getMessage());
        } catch (IllegalArgumentException e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 上传任意文件
     * POST /upload/file
     * 
     * @param file 上传的文件
     * @return 文件访问 URL
     */
    @PostMapping("/file")
    public Result<Map<String, String>> uploadFile(
            @RequestParam("file") MultipartFile file) {
        
        try {
            String url = fileUploadService.uploadFile(file);
            
            Map<String, String> response = new HashMap<>();
            response.put("url", url);
            response.put("filename", file.getOriginalFilename());
            response.put("size", String.valueOf(file.getSize()));
            response.put("type", getFileType(file.getOriginalFilename()));
            
            return Result.success(response);
        } catch (IOException e) {
            return Result.error("上传失败：" + e.getMessage());
        } catch (IllegalArgumentException e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 批量上传图片
     * POST /upload/images
     * 
     * @param files 上传的图片文件数组
     * @return 文件访问 URL 列表
     */
    @PostMapping("/images")
    public Result<List<String>> uploadImages(
            @RequestParam("files") MultipartFile[] files) {
        
        try {
            List<String> urls = fileUploadService.uploadImages(files);
            return Result.success(urls);
        } catch (IOException e) {
            return Result.error("上传失败：" + e.getMessage());
        } catch (IllegalArgumentException e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 删除文件
     * DELETE /upload/{filename}
     * 
     * @param filename 文件名（包含路径，如 images/xxx.jpg）
     * @return 删除结果
     */
    @DeleteMapping("/{filename:.+}")
    public Result<String> deleteFile(@PathVariable String filename) {
        String filePath = "/uploads/" + filename;
        
        boolean deleted = fileUploadService.deleteFile(filePath);
        
        if (deleted) {
            return Result.success("删除成功");
        } else {
            return Result.error("文件不存在或删除失败");
        }
    }

    /**
     * 获取文件类型
     */
    private String getFileType(String filename) {
        if (filename == null) {
            return "unknown";
        }
        
        String ext = filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
        
        if (List.of("jpg", "jpeg", "png", "gif", "webp", "bmp").contains(ext)) {
            return "image";
        } else if (List.of("pdf", "doc", "docx", "xls", "xlsx", "ppt", "pptx", "txt", "md").contains(ext)) {
            return "document";
        } else {
            return "other";
        }
    }
}
