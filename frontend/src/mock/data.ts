import type { Article, Project, Skill, TimelineItem, Profile } from '@/types'

export const profile: Profile = {
  name: 'GAO',
  avatar: '',
  title: '全栈开发工程师 / 嵌入式爱好者',
  school: 'XX 大学',
  major: '计算机科学与技术',
  bio: '热爱技术，专注于 Web 全栈开发与嵌入式系统。追求简洁高效的代码，相信技术可以改变世界。正在不断学习中，目标是成为一名优秀的全栈工程师。',
  email: 'gao@example.com',
  github: 'https://github.com/gao',
  qq: '123456789',
  wechat: 'gao_wechat'
}

export const skills: Skill[] = [
  { name: 'C', category: 'embedded', level: 85 },
  { name: 'C++', category: 'embedded', level: 75 },
  { name: 'Java', category: 'backend', level: 80 },
  { name: 'Vue 3', category: 'frontend', level: 85 },
  { name: 'TypeScript', category: 'frontend', level: 75 },
  { name: 'Spring Boot', category: 'backend', level: 80 },
  { name: 'MySQL', category: 'database', level: 75 },
  { name: 'Linux', category: 'other', level: 70 },
  { name: 'Git', category: 'other', level: 80 },
  { name: '数据结构', category: 'other', level: 75 },
  { name: '算法', category: 'other', level: 70 },
  { name: 'RTOS', category: 'embedded', level: 65 }
]

export const articles: Article[] = [
  {
    id: 1,
    title: 'Spring Boot 3.5 新特性详解',
    content: 'Spring Boot 3.5 带来了许多令人兴奋的新特性...\n\n## 自动配置优化\n\n新的自动配置机制大大简化了开发流程...\n\n```java\n@SpringBootApplication\npublic class DemoApplication {\n    public static void main(String[] args) {\n        SpringApplication.run(DemoApplication.class, args);\n    }\n}\n```\n\n## 原生镜像支持\n\nSpring Boot 3.5 对 GraalVM 的支持更加完善...',
    summary: '深入探索 Spring Boot 3.5 的核心新特性与最佳实践',
    category: 'Java',
    tags: 'Spring Boot, Java, 后端开发',
    viewCount: 1280,
    likeCount: 42,
    createTime: '2026-05-20T10:00:00'
  },
  {
    id: 2,
    title: 'Vue 3 Composition API 实战指南',
    content: '',
    summary: '从实际项目出发，掌握 Vue 3 Composition API 的核心用法',
    category: '前端',
    tags: 'Vue3, TypeScript, 前端开发',
    viewCount: 960,
    likeCount: 35,
    createTime: '2026-05-15T14:30:00'
  },
  {
    id: 3,
    title: '嵌入式 Linux 驱动开发入门',
    content: '',
    summary: '从零开始学习 Linux 内核驱动开发的基础知识',
    category: '嵌入式',
    tags: 'Linux, 嵌入式, 驱动开发',
    viewCount: 750,
    likeCount: 28,
    createTime: '2026-05-10T09:00:00'
  },
  {
    id: 4,
    title: 'MySQL 索引优化实战',
    content: '',
    summary: '深入理解 B+Tree 索引原理与 SQL 性能调优技巧',
    category: '数据库',
    tags: 'MySQL, 数据库, 性能优化',
    viewCount: 2100,
    likeCount: 67,
    createTime: '2026-05-05T16:00:00'
  },
  {
    id: 5,
    title: '数据结构与算法学习路线',
    content: '',
    summary: '系统梳理数据结构与算法的学习路径和刷题方法',
    category: '算法',
    tags: '算法, 数据结构, LeetCode',
    viewCount: 1560,
    likeCount: 53,
    createTime: '2026-04-28T11:00:00'
  },
  {
    id: 6,
    title: 'Git 工作流最佳实践',
    content: '',
    summary: '团队协作中 Git 分支策略与 Commit 规范总结',
    category: '工具',
    tags: 'Git, DevOps, 团队协作',
    viewCount: 890,
    likeCount: 31,
    createTime: '2026-04-20T08:30:00'
  }
]

export const projects: Project[] = [
  {
    id: 1,
    title: '个人博客系统',
    description: '基于 Spring Boot + Vue 3 的全栈博客系统，采用 DDD 模块化架构，支持 Markdown 编辑、评论回复、全文搜索等功能。',
    techStack: ['Java', 'Spring Boot', 'Vue 3', 'MySQL', 'JWT'],
    githubUrl: 'https://github.com/gao/blog',
    category: 'web',
    featured: true,
    createTime: '2026-05-01'
  },
  {
    id: 2,
    title: '智能家居中控系统',
    description: '基于 STM32 + RT-Thread 的物联网中控设备，支持温湿度监测、远程控制、语音交互等功能。',
    techStack: ['C', 'RT-Thread', 'STM32', 'MQTT', 'ESP32'],
    githubUrl: 'https://github.com/gao/smart-home',
    category: 'embedded',
    featured: true,
    createTime: '2026-04-15'
  },
  {
    id: 3,
    title: 'LeetCode 刷题笔记',
    description: '系统整理 200+ 道算法题解，包含详细思路分析和多种语言实现。',
    techStack: ['Java', 'C++', '算法', '数据结构'],
    githubUrl: 'https://github.com/gao/leetcode',
    category: 'algorithm',
    featured: false,
    createTime: '2026-03-20'
  },
  {
    id: 4,
    title: 'AI 图像识别应用',
    description: '基于 TensorFlow Lite 的移动端图像分类应用，支持实时识别与离线推理。',
    techStack: ['Python', 'TensorFlow', 'Android', 'AI'],
    githubUrl: 'https://github.com/gao/ai-vision',
    category: 'ai',
    featured: true,
    createTime: '2026-02-10'
  }
]

export const timeline: TimelineItem[] = [
  { year: '2026', title: '个人博客系统开发', description: '全栈开发个人博客与作品集网站', type: 'project' },
  { year: '2026', title: '蓝桥杯省赛一等奖', description: '嵌入式设计与开发组', type: 'competition' },
  { year: '2025', title: '智能家居项目', description: '基于 RT-Thread 的物联网中控系统', type: 'project' },
  { year: '2025', title: '全国大学生电子设计竞赛', description: '省级二等奖', type: 'competition' },
  { year: '2024', title: '开始学习嵌入式开发', description: '从 STM32 到 RTOS，逐步深入', type: 'education' },
  { year: '2024', title: '数据结构与算法学习', description: '系统学习 + LeetCode 刷题 200+', type: 'education' },
  { year: '2023', title: '开始学习 Java 后端开发', description: 'Spring Boot + MySQL 技术栈入门', type: 'education' },
  { year: '2023', title: '大学入学', description: '计算机科学与技术专业', type: 'education' }
]

export const comments: Comment[] = [
  { id: 1, content: '写得太好了，收获很多！', author: '技术爱好者', blogId: 1, parentId: null, createTime: '2026-05-22T10:00:00' },
  { id: 2, content: '同意，Spring Boot 3.5 确实很强大', author: 'Java开发者', blogId: 1, parentId: null, createTime: '2026-05-22T11:00:00' },
  { id: 3, content: '请问对 GraalVM 的支持具体有哪些改进？', author: '后端小白', blogId: 1, parentId: 1, createTime: '2026-05-22T12:00:00' }
]
