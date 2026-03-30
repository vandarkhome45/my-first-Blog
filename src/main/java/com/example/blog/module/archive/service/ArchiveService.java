package com.example.blog.module.archive.service;

import com.example.blog.module.blog.entity.BlogEntity;
import com.example.blog.module.blog.repository.BlogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 归档服务层
 * 模块：归档和时间线模块
 * 
 * 功能：
 * - 按年月归档文章
 * - 生成时间线数据
 * - 最新文章列表
 * - 随机文章推荐
 */
@Service
public class ArchiveService {

    @Autowired
    private BlogRepository blogRepository;

    /**
     * 获取所有归档数据（按年月分组）
     * 
     * @return Map<年月，文章列表>
     * 例如：{"2024-01": [文章 1, 文章 2], "2024-02": [文章 3]}
     */
    public Map<String, List<BlogEntity>> getAllArchives() {
        List<BlogEntity> allBlogs = blogRepository.findAll();
        
        // 按创建时间排序（最新的在前）
        allBlogs.sort((a, b) -> {
            if (a.getCreateTime() == null) return 1;
            if (b.getCreateTime() == null) return -1;
            return b.getCreateTime().compareTo(a.getCreateTime());
        });

        // 按年月分组
        Map<String, List<BlogEntity>> archives = new LinkedHashMap<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");

        for (BlogEntity blog : allBlogs) {
            if (blog.getCreateTime() != null) {
                String monthKey = dateFormat.format(blog.getCreateTime());
                archives.computeIfAbsent(monthKey, k -> new ArrayList<>()).add(blog);
            }
        }

        return archives;
    }

    /**
     * 获取指定年月的归档文章
     * 
     * @param year 年份
     * @param month 月份
     * @return 该月的文章列表
     */
    public List<BlogEntity> getArchiveByMonth(Integer year, Integer month) {
        List<BlogEntity> allBlogs = blogRepository.findAll();
        List<BlogEntity> result = new ArrayList<>();

        for (BlogEntity blog : allBlogs) {
            if (blog.getCreateTime() != null) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(blog.getCreateTime());
                
                if (calendar.get(Calendar.YEAR) == year && 
                    calendar.get(Calendar.MONTH) == month - 1) {
                    result.add(blog);
                }
            }
        }

        // 按创建时间倒序
        result.sort((a, b) -> {
            if (a.getCreateTime() == null) return 1;
            if (b.getCreateTime() == null) return -1;
            return b.getCreateTime().compareTo(a.getCreateTime());
        });

        return result;
    }

    /**
     * 获取时间线数据（用于前端展示）
     * 
     * @return 时间线数据列表
     */
    public List<Map<String, Object>> getTimeline() {
        Map<String, List<BlogEntity>> archives = getAllArchives();
        List<Map<String, Object>> timeline = new ArrayList<>();

        for (Map.Entry<String, List<BlogEntity>> entry : archives.entrySet()) {
            Map<String, Object> item = new HashMap<>();
            item.put("month", entry.getKey());
            item.put("count", entry.getValue().size());
            item.put("articles", entry.getValue());
            timeline.add(item);
        }

        return timeline;
    }

    /**
     * 获取最新文章（按创建时间倒序）
     * 
     * @param limit 返回数量限制
     * @return 最新文章列表
     */
    public List<BlogEntity> getLatestArticles(int limit) {
        List<BlogEntity> allBlogs = blogRepository.findAll();
        
        // 按创建时间倒序
        allBlogs.sort((a, b) -> {
            if (a.getCreateTime() == null) return 1;
            if (b.getCreateTime() == null) return -1;
            return b.getCreateTime().compareTo(a.getCreateTime());
        });

        // 返回前 N 篇
        if (allBlogs.size() <= limit) {
            return allBlogs;
        }
        return allBlogs.subList(0, limit);
    }

    /**
     * 获取随机文章推荐
     * 
     * @param limit 返回数量限制
     * @return 随机文章列表
     */
    public List<BlogEntity> getRandomArticles(int limit) {
        List<BlogEntity> allBlogs = blogRepository.findAll();
        
        if (allBlogs.isEmpty() || allBlogs.size() <= limit) {
            return allBlogs;
        }

        // 打乱顺序并取前 N 篇
        Collections.shuffle(allBlogs);
        return allBlogs.subList(0, limit);
    }

    /**
     * 获取归档统计信息
     * 
     * @return 统计信息
     */
    public Map<String, Object> getArchiveStats() {
        List<BlogEntity> allBlogs = blogRepository.findAll();
        Map<String, Object> stats = new HashMap<>();

        // 总文章数
        stats.put("totalArticles", allBlogs.size());

        // 归档月份数
        Set<String> months = new HashSet<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
        for (BlogEntity blog : allBlogs) {
            if (blog.getCreateTime() != null) {
                months.add(dateFormat.format(blog.getCreateTime()));
            }
        }
        stats.put("totalMonths", months.size());

        // 最早的文章时间
        if (!allBlogs.isEmpty()) {
            Optional<Date> minDate = allBlogs.stream()
                    .map(BlogEntity::getCreateTime)
                    .filter(Objects::nonNull)
                    .min(Date::compareTo);
            minDate.ifPresent(date -> stats.put("firstArticleDate", date));

            // 最新的文章时间
            Optional<Date> maxDate = allBlogs.stream()
                    .map(BlogEntity::getCreateTime)
                    .filter(Objects::nonNull)
                    .max(Date::compareTo);
            maxDate.ifPresent(date -> stats.put("latestArticleDate", date));
        }

        return stats;
    }
}
