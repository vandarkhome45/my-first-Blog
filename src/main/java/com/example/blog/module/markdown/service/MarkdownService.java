package com.example.blog.module.markdown.service;

import com.example.blog.module.blog.entity.BlogEntity;
import com.example.blog.module.markdown.util.MarkdownUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Markdown 服务层
 * 模块：Markdown 编辑器支持
 * 
 * 功能：
 * - Markdown 渲染
 * - 内容预览
 * - 格式转换
 */
@Service
public class MarkdownService {

    @Autowired
    private MarkdownUtil markdownUtil;

    /**
     * 渲染 Markdown 为 HTML
     * 
     * @param markdown Markdown 文本
     * @return HTML 内容
     */
    public String renderMarkdown(String markdown) {
        return markdownUtil.render(markdown);
    }

    /**
     * 处理博客内容（自动渲染 Markdown）
     * 
     * @param blog 博客实体
     * @return 处理后的博客（包含渲染后的 HTML）
     */
    public BlogEntity processBlogContent(BlogEntity blog) {
        if (blog == null) {
            return null;
        }

        // 如果有原始 Markdown 内容，渲染为 HTML
        if (blog.getOriginalContent() != null && !blog.getOriginalContent().isEmpty()) {
            String renderedHtml = markdownUtil.render(blog.getOriginalContent());
            blog.setRenderedContent(renderedHtml);
            
            // content 字段默认使用渲染后的 HTML
            if (blog.getContent() == null || blog.getContent().isEmpty()) {
                blog.setContent(renderedHtml);
            }
        } else if (blog.getContent() != null && !blog.getContent().isEmpty()) {
            // 如果没有原始内容，假设 content 就是 Markdown
            String renderedHtml = markdownUtil.render(blog.getContent());
            blog.setRenderedContent(renderedHtml);
            blog.setOriginalContent(blog.getContent());
        }

        return blog;
    }

    /**
     * 获取博客摘要（纯文本）
     * 
     * @param blog 博客实体
     * @param maxLength 最大长度
     * @return 摘要文本
     */
    public String getBlogSummary(BlogEntity blog, int maxLength) {
        if (blog == null) {
            return "";
        }

        String content = blog.getOriginalContent();
        if (content == null || content.isEmpty()) {
            content = blog.getContent();
        }

        return markdownUtil.extractSummary(content, maxLength);
    }

    /**
     * 保存博客（自动处理 Markdown）
     * 
     * @param blog 博客实体
     * @return 保存后的博客
     */
    public BlogEntity saveBlog(BlogEntity blog) {
        // 处理内容
        processBlogContent(blog);
        
        // 这里应该调用 BlogRepository 保存，但为了避免循环依赖
        // 实际使用时应该在 Controller 中调用此方法后再保存
        return blog;
    }

    /**
     * 检查博客是否包含代码块
     */
    public boolean hasCodeBlocks(BlogEntity blog) {
        if (blog == null) {
            return false;
        }

        String content = blog.getOriginalContent();
        if (content == null) {
            content = blog.getContent();
        }

        return markdownUtil.hasCodeBlocks(content);
    }

    /**
     * 统计博客中的代码块数量
     */
    public int countCodeBlocks(BlogEntity blog) {
        if (blog == null) {
            return 0;
        }

        String content = blog.getOriginalContent();
        if (content == null) {
            content = blog.getContent();
        }

        return markdownUtil.countCodeBlocks(content);
    }

    /**
     * 快速预览 Markdown（渲染前 500 个字符）
     * 
     * @param markdown Markdown 文本
     * @return 预览 HTML
     */
    public String previewMarkdown(String markdown) {
        if (markdown == null || markdown.isEmpty()) {
            return "";
        }

        // 截取前 500 个字符
        String preview = markdown.length() > 500 ? 
                        markdown.substring(0, 500) + "..." : markdown;
        
        return markdownUtil.render(preview);
    }
}
