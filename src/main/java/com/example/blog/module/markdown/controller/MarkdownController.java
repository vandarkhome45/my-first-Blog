package com.example.blog.module.markdown.controller;

import com.example.blog.common.Result;
import com.example.blog.module.markdown.service.MarkdownService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Markdown 编辑器控制器
 * 模块：Markdown 编辑器支持
 * 
 * API 接口：
 * - POST /markdown/render - 渲染 Markdown 为 HTML
 * - POST /markdown/preview - 快速预览
 * - POST /markdown/summary - 获取摘要
 * - GET /markdown/info - 分析 Markdown 文档信息
 */
@RestController
@RequestMapping("/markdown")
public class MarkdownController {

    @Autowired
    private MarkdownService markdownService;

    /**
     * 渲染 Markdown 为 HTML
     * POST /markdown/render
     * Body: {"markdown": "# Hello\n\nContent..."}
     * 
     * @param params 参数（包含 markdown 字段）
     * @return HTML 内容
     */
    @PostMapping("/render")
    public Result<Map<String, String>> renderMarkdown(@RequestBody Map<String, String> params) {
        String markdown = params.get("markdown");
        
        if (markdown == null || markdown.isEmpty()) {
            return Result.error("请提供 Markdown 内容");
        }

        try {
            String html = markdownService.renderMarkdown(markdown);
            
            Map<String, String> response = new HashMap<>();
            response.put("html", html);
            response.put("original", markdown);
            
            return Result.success(response);
        } catch (Exception e) {
            return Result.error("渲染失败：" + e.getMessage());
        }
    }

    /**
     * 快速预览 Markdown
     * POST /markdown/preview
     * Body: {"markdown": "# Hello\n\nContent..."}
     * 
     * @param params 参数
     * @return 预览 HTML
     */
    @PostMapping("/preview")
    public Result<Map<String, String>> previewMarkdown(@RequestBody Map<String, String> params) {
        String markdown = params.get("markdown");
        
        if (markdown == null || markdown.isEmpty()) {
            return Result.error("请提供 Markdown 内容");
        }

        try {
            String html = markdownService.previewMarkdown(markdown);
            
            Map<String, String> response = new HashMap<>();
            response.put("html", html);
            response.put("length", String.valueOf(markdown.length()));
            
            return Result.success(response);
        } catch (Exception e) {
            return Result.error("预览失败：" + e.getMessage());
        }
    }

    /**
     * 获取 Markdown 摘要
     * POST /markdown/summary
     * Body: {"markdown": "...", "length": 200}
     * 
     * @param params 参数
     * @return 摘要文本
     */
    @PostMapping("/summary")
    public Result<Map<String, String>> getSummary(@RequestBody Map<String, String> params) {
        String markdown = params.get("markdown");
        String lengthStr = params.get("length");
        
        int maxLength = 200; // 默认 200 字
        if (lengthStr != null && !lengthStr.isEmpty()) {
            try {
                maxLength = Integer.parseInt(lengthStr);
            } catch (NumberFormatException e) {
                // 使用默认值
            }
        }
        
        if (markdown == null || markdown.isEmpty()) {
            return Result.error("请提供 Markdown 内容");
        }

        try {
            // 使用本地方法提取摘要
            String summary = extractPlainText(markdown, maxLength);
            
            Map<String, String> response = new HashMap<>();
            response.put("summary", summary);
            response.put("length", String.valueOf(summary.length()));
            
            return Result.success(response);
        } catch (Exception e) {
            return Result.error("提取摘要失败：" + e.getMessage());
        }
    }

    /**
     * 分析 Markdown 文档信息
     * POST /markdown/info
     * Body: {"markdown": "..."}
     * 
     * @param params 参数
     * @return 文档信息
     */
    @PostMapping("/info")
    public Result<Map<String, Object>> analyzeMarkdown(@RequestBody Map<String, String> params) {
        String markdown = params.get("markdown");
        
        if (markdown == null || markdown.isEmpty()) {
            return Result.error("请提供 Markdown 内容");
        }

        try {
            Map<String, Object> info = new HashMap<>();
            
            // 基本信息
            info.put("totalLength", markdown.length());
            info.put("lineCount", markdown.split("\n").length);
            
            // 特性检测
            info.put("hasCodeBlocks", markdown.contains("```"));
            info.put("hasTables", markdown.contains("|"));
            info.put("hasImages", markdown.contains("![") && markdown.contains("]("));
            info.put("hasLinks", markdown.matches(".*\\[[^\\]]+\\]\\([^)]+\\).*"));
            
            // 统计
            if (info.get("hasCodeBlocks").equals(true)) {
                info.put("codeBlockCount", countOccurrences(markdown, "```") / 2);
            }
            
            // 标题数量
            info.put("headingCount", countOccurrences(markdown, "\n#"));
            
            return Result.success(info);
        } catch (Exception e) {
            return Result.error("分析失败：" + e.getMessage());
        }
    }

    /**
     * 辅助方法：统计子串出现次数
     */
    private int countOccurrences(String str, String sub) {
        if (str.isEmpty() || sub.isEmpty()) {
            return 0;
        }
        
        int count = 0;
        int index = 0;
        
        while ((index = str.indexOf(sub, index)) != -1) {
            count++;
            index += sub.length();
        }
        
        return count;
    }

    /**
     * 辅助方法：提取纯文本摘要
     */
    private String extractPlainText(String markdown, int maxLength) {
        if (markdown == null || markdown.isEmpty()) {
            return "";
        }

        // 移除 Markdown 语法
        String plainText = markdown
                .replaceAll("#+", "")                    // 标题
                .replaceAll("\\*{1,3}([^*]+)\\*{1,3}", "$1")  // 斜体/粗体
                .replaceAll("`([^`]+)`", "$1")          // 行内代码
                .replaceAll("!\\[([^\\]]*)\\]\\([^)]+\\)", "$1") // 图片
                .replaceAll("\\[([^\\]]*)\\]\\([^)]+\\)", "$1")  // 链接
                .replaceAll("^\\s*>\\s*", "")           // 引用
                .replaceAll("^\\s*[-*+]\\s*", "")       // 列表
                .replaceAll("^\\s*\\d+\\.\\s*", "")     // 有序列表
                .replaceAll("\\n{2,}", "\n")            // 多余空行
                .trim();

        // 截取摘要
        if (plainText.length() > maxLength) {
            return plainText.substring(0, maxLength) + "...";
        }

        return plainText;
    }
}
