package com.example.blog.module.upload.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 文件上传服务层
 * 模块：文件上传模块
 * 
 * 功能：
 * - 图片上传（支持 jpg、png、gif、webp）
 * - 文件上传（支持常见文档格式）
 * - 文件类型验证
 * - 文件大小限制
 * - 自动生成唯一文件名
 */
@Service
public class FileUploadService {

    /**
     * 文件上传根目录
     * 可在 application.properties 中配置
     */
    @Value("${upload.path:./uploads}")
    private String uploadPath;

    /**
     * 允许的图片文件扩展名
     */
    private static final List<String> IMAGE_EXTENSIONS = List.of(
        "jpg", "jpeg", "png", "gif", "webp", "bmp"
    );

    /**
     * 允许的文档文件扩展名
     */
    private static final List<String> DOCUMENT_EXTENSIONS = List.of(
        "pdf", "doc", "docx", "xls", "xlsx", "ppt", "pptx", "txt", "md"
    );

    /**
     * 最大文件大小（默认 10MB）
     */
    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024;

    /**
     * 初始化上传目录
     */
    @PostConstruct
    public void init() {
        try {
            Path path = Paths.get(uploadPath);
            if (!Files.exists(path)) {
                Files.createDirectories(path);
                System.out.println("✅ 创建文件上传目录：" + uploadPath);
            }
            
            // 创建图片子目录
            createSubDirectory("images");
            // 创建文档子目录
            createSubDirectory("documents");
            // 创建其他文件子目录
            createSubDirectory("others");
            
        } catch (IOException e) {
            System.err.println("❌ 创建上传目录失败：" + e.getMessage());
        }
    }

    /**
     * 创建子目录
     */
    private void createSubDirectory(String subdir) {
        Path path = Paths.get(uploadPath, subdir);
        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                System.err.println("创建子目录失败：" + subdir);
            }
        }
    }

    /**
     * 上传图片文件
     * 
     * @param file 上传的文件
     * @return 文件访问 URL
     * @throws IOException 上传失败时抛出
     */
    public String uploadImage(MultipartFile file) throws IOException {
        // 验证文件是否为空
        if (file.isEmpty()) {
            throw new IllegalArgumentException("上传文件不能为空");
        }

        // 验证文件类型
        String originalFilename = file.getOriginalFilename();
        String extension = getFileExtension(originalFilename);
        if (!IMAGE_EXTENSIONS.contains(extension.toLowerCase())) {
            throw new IllegalArgumentException("不支持的图片格式，仅支持：" + IMAGE_EXTENSIONS);
        }

        // 验证文件大小
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException("文件大小超过限制（10MB）");
        }

        // 生成唯一文件名
        String filename = generateUniqueFilename(originalFilename);
        
        // 保存到 images 子目录
        Path savePath = Paths.get(uploadPath, "images", filename);
        Files.copy(file.getInputStream(), savePath, StandardCopyOption.REPLACE_EXISTING);

        // 返回访问 URL
        return "/uploads/images/" + filename;
    }

    /**
     * 上传文档文件
     * 
     * @param file 上传的文件
     * @return 文件访问 URL
     * @throws IOException 上传失败时抛出
     */
    public String uploadDocument(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("上传文件不能为空");
        }

        String originalFilename = file.getOriginalFilename();
        String extension = getFileExtension(originalFilename);
        
        if (!DOCUMENT_EXTENSIONS.contains(extension.toLowerCase())) {
            throw new IllegalArgumentException("不支持的文档格式，仅支持：" + DOCUMENT_EXTENSIONS);
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException("文件大小超过限制（10MB）");
        }

        String filename = generateUniqueFilename(originalFilename);
        Path savePath = Paths.get(uploadPath, "documents", filename);
        Files.copy(file.getInputStream(), savePath, StandardCopyOption.REPLACE_EXISTING);

        return "/uploads/documents/" + filename;
    }

    /**
     * 上传任意文件（不限制类型）
     * 
     * @param file 上传的文件
     * @return 文件访问 URL
     * @throws IOException 上传失败时抛出
     */
    public String uploadFile(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("上传文件不能为空");
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException("文件大小超过限制（10MB）");
        }

        String originalFilename = file.getOriginalFilename();
        String filename = generateUniqueFilename(originalFilename);
        
        // 根据扩展名决定保存位置
        String extension = getFileExtension(originalFilename);
        String subDir;
        String urlPrefix;
        
        if (IMAGE_EXTENSIONS.contains(extension.toLowerCase())) {
            subDir = "images";
            urlPrefix = "/uploads/images/";
        } else if (DOCUMENT_EXTENSIONS.contains(extension.toLowerCase())) {
            subDir = "documents";
            urlPrefix = "/uploads/documents/";
        } else {
            subDir = "others";
            urlPrefix = "/uploads/others/";
        }

        Path savePath = Paths.get(uploadPath, subDir, filename);
        Files.copy(file.getInputStream(), savePath, StandardCopyOption.REPLACE_EXISTING);

        return urlPrefix + filename;
    }

    /**
     * 批量上传图片
     * 
     * @param files 上传的文件数组
     * @return 文件访问 URL 列表
     */
    public List<String> uploadImages(MultipartFile[] files) throws IOException {
        List<String> urls = new ArrayList<>();
        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                urls.add(uploadImage(file));
            }
        }
        return urls;
    }

    /**
     * 获取文件扩展名
     */
    private String getFileExtension(String filename) {
        if (filename == null || filename.lastIndexOf(".") == -1) {
            return "";
        }
        return filename.substring(filename.lastIndexOf(".") + 1);
    }

    /**
     * 生成唯一文件名
     * 格式：UUID_原文件名.扩展名
     */
    private String generateUniqueFilename(String originalFilename) {
        String extension = getFileExtension(originalFilename);
        String nameWithoutExt = originalFilename;
        
        if (originalFilename != null && originalFilename.lastIndexOf(".") != -1) {
            nameWithoutExt = originalFilename.substring(0, originalFilename.lastIndexOf("."));
        }
        
        // 使用 UUID 保证文件名唯一性
        return UUID.randomUUID().toString().replace("-", "") + "_" + 
               sanitizeFilename(nameWithoutExt) + "." + extension;
    }

    /**
     * 清理文件名，移除不安全字符
     */
    private String sanitizeFilename(String filename) {
        // 移除特殊字符，只保留字母、数字、中文、下划线、中横线
        return filename.replaceAll("[^\\w\\u4e00-\\u9fa5-]", "_");
    }

    /**
     * 删除文件
     * 
     * @param filePath 文件路径（相对路径，如 /uploads/images/xxx.jpg）
     * @return 是否删除成功
     */
    public boolean deleteFile(String filePath) {
        try {
            // 转换为绝对路径
            String absolutePath = uploadPath + filePath.replace("/uploads", "");
            Path path = Paths.get(absolutePath);
            
            if (Files.exists(path)) {
                Files.delete(path);
                return true;
            }
            return false;
        } catch (IOException e) {
            System.err.println("删除文件失败：" + e.getMessage());
            return false;
        }
    }

    /**
     * 检查文件是否存在
     */
    public boolean fileExists(String filePath) {
        String absolutePath = uploadPath + filePath.replace("/uploads", "");
        return Files.exists(Paths.get(absolutePath));
    }
}
