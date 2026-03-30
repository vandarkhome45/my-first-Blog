package com.example.blog.module.markdown.util;

import org.commonmark.Extension;
import org.commonmark.ext.gfm.tables.TablesExtension;
import org.commonmark.ext.autolink.AutolinkExtension;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * Markdown 渲染工具类
 * 模块：Markdown 编辑器支持
 * 
 * 功能：
 * - Markdown 转 HTML
 * - 支持表格扩展
 * - 支持自动链接
 * - 代码高亮预处理
 */
@Component
public class MarkdownUtil {

    /**
     * Markdown 解析器（支持扩展）
     */
    private final Parser parser;

    /**
     * HTML 渲染器
     */
    private final HtmlRenderer renderer;

    public MarkdownUtil() {
        // 配置扩展
        List<Extension> extensions = Arrays.asList(
                TablesExtension.create(),      // 表格支持
                AutolinkExtension.create()     // 自动链接
        );

        // 创建解析器
        this.parser = Parser.builder()
                .extensions(extensions)
                .build();

        // 创建渲染器
        this.renderer = HtmlRenderer.builder()
                .extensions(extensions)
                .build();
    }

    /**
     * 将 Markdown 渲染为 HTML
     * 
     * @param markdown Markdown 文本
     * @return HTML 内容
     */
    public String render(String markdown) {
        if (markdown == null || markdown.isEmpty()) {
            return "";
        }

        // 解析 Markdown
        Node document = parser.parse(markdown);
        
        // 渲染为 HTML
        String html = renderer.render(document);
        
        // 添加代码高亮类名（配合前端 highlight.js 使用）
        html = addCodeHighlightClasses(html);
        
        return html;
    }

    /**
     * 为代码块添加高亮类名
     * 例如：<code> -> <code class="language-java">
     */
    private String addCodeHighlightClasses(String html) {
        // 为 <pre><code> 添加 language- 前缀
        html = html.replaceAll("<pre><code>", "<pre><code class=\"hljs\">");
        
        // 如果有指定语言，例如 ```java，会自动被 commonmark 处理
        // 我们只需要确保有 hljs 类名即可
        
        return html;
    }

    /**
     * 提取 Markdown 文档的纯文本摘要（前 N 个字符）
     * 
     * @param markdown Markdown 文本
     * @param maxLength 最大长度
     * @return 摘要文本
     */
    public String extractSummary(String markdown, int maxLength) {
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

    /**
     * 检查 Markdown 是否包含代码块
     */
    public boolean hasCodeBlocks(String markdown) {
        return markdown != null && markdown.contains("```");
    }

    /**
     * 检查 Markdown 是否包含表格
     */
    public boolean hasTables(String markdown) {
        return markdown != null && markdown.contains("|");
    }

    /**
     * 统计 Markdown 文档中的代码块数量
     */
    public int countCodeBlocks(String markdown) {
        if (markdown == null) {
            return 0;
        }
        
        int count = 0;
        int index = 0;
        
        while ((index = markdown.indexOf("```", index)) != -1) {
            count++;
            index += 3;
        }
        
        return count / 2; // 每个代码块有两个 ```
    }
}
