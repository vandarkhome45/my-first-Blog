import { defineStore } from 'pinia'
import { reactive } from 'vue'
import type { Article, Project, Skill, TimelineItem, Profile, Comment } from '@/types'

export const useDataStore = defineStore('data', () => {
  // localStorage 工具函数
  function loadFromStorage<T>(key: string, fallback: T): T {
    try {
      const saved = localStorage.getItem(key)
      if (saved) return JSON.parse(saved)
    } catch {}
    return fallback
  }

  function saveToStorage(key: string, data: any) {
    try {
      localStorage.setItem(key, JSON.stringify(data))
    } catch {}
  }

  const defaultProfile: Profile = {
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

  const profile = reactive<Profile>(loadFromStorage('blog_profile', defaultProfile))

  const defaultSkills: Skill[] = [
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

  const skills = reactive<Skill[]>(loadFromStorage('blog_skills', defaultSkills))

  const defaultArticles: Article[] = [
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

  const articles = reactive<Article[]>(loadFromStorage('blog_articles', defaultArticles))

  const defaultProjects: Project[] = [
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

  const projects = reactive<Project[]>(loadFromStorage('blog_projects', defaultProjects))

  const defaultTimeline: TimelineItem[] = [
    { year: '2026', title: '个人博客系统开发', description: '全栈开发个人博客与作品集网站', type: 'project' },
    { year: '2026', title: '蓝桥杯省赛一等奖', description: '嵌入式设计与开发组', type: 'competition' },
    { year: '2025', title: '智能家居项目', description: '基于 RT-Thread 的物联网中控系统', type: 'project' },
    { year: '2025', title: '全国大学生电子设计竞赛', description: '省级二等奖', type: 'competition' },
    { year: '2024', title: '开始学习嵌入式开发', description: '从 STM32 到 RTOS，逐步深入', type: 'education' },
    { year: '2024', title: '数据结构与算法学习', description: '系统学习 + LeetCode 刷题 200+', type: 'education' },
    { year: '2023', title: '开始学习 Java 后端开发', description: 'Spring Boot + MySQL 技术栈入门', type: 'education' },
    { year: '2023', title: '大学入学', description: '计算机科学与技术专业', type: 'education' }
  ]

  const timeline = reactive<TimelineItem[]>(loadFromStorage('blog_timeline', defaultTimeline))

  const defaultComments: Comment[] = [
    { id: 1, content: '写得太好了，收获很多！', author: '技术爱好者', blogId: 1, parentId: null, createTime: '2026-05-22T10:00:00' },
    { id: 2, content: '同意，Spring Boot 3.5 确实很强大', author: 'Java开发者', blogId: 1, parentId: null, createTime: '2026-05-22T11:00:00' },
    { id: 3, content: '请问对 GraalVM 的支持具体有哪些改进？', author: '后端小白', blogId: 1, parentId: 1, createTime: '2026-05-22T12:00:00' }
  ]

  const comments = reactive<Comment[]>(loadFromStorage('blog_comments', defaultComments))

  let nextId = { article: 7, project: 5, comment: 4, timeline: 9 }

  // ================= Profile =================
  function updateProfile(data: Partial<Profile>) {
    Object.assign(profile, data)
    saveToStorage('blog_profile', { ...profile })
  }

  // ================= Skills =================
  function updateSkill(index: number, data: Partial<Skill>) {
    if (index >= 0 && index < skills.length) {
      Object.assign(skills[index], data)
      saveToStorage('blog_skills', [...skills])
    }
  }

  function addSkill(skill: Skill) {
    skills.push(skill)
    saveToStorage('blog_skills', [...skills])
  }

  function removeSkill(index: number) {
    if (index >= 0 && index < skills.length) {
      skills.splice(index, 1)
      saveToStorage('blog_skills', [...skills])
    }
  }

  function reorderSkills(from: number, to: number) {
    if (from >= 0 && from < skills.length && to >= 0 && to < skills.length) {
      const item = skills.splice(from, 1)[0]
      skills.splice(to, 0, item)
      saveToStorage('blog_skills', [...skills])
    }
  }

  // ================= Articles =================
  function addArticle(article: Omit<Article, 'id' | 'viewCount' | 'likeCount' | 'createTime'>) {
    const a: Article = {
      ...article,
      id: nextId.article++,
      viewCount: 0,
      likeCount: 0,
      createTime: new Date().toISOString()
    }
    articles.unshift(a)
    saveToStorage('blog_articles', [...articles])
    return a
  }

  function updateArticle(id: number, data: Partial<Article>) {
    const idx = articles.findIndex(a => a.id === id)
    if (idx !== -1) {
      Object.assign(articles[idx], data)
      saveToStorage('blog_articles', [...articles])
    }
  }

  function removeArticle(id: number) {
    const idx = articles.findIndex(a => a.id === id)
    if (idx !== -1) {
      articles.splice(idx, 1)
      saveToStorage('blog_articles', [...articles])
    }
  }

  function getArticleById(id: number): Article | null {
    return articles.find(a => a.id === id) || null
  }

  // ================= Projects =================
  function addProject(project: Omit<Project, 'id' | 'createTime'>) {
    const p: Project = {
      ...project,
      id: nextId.project++,
      createTime: new Date().toISOString().split('T')[0]
    }
    projects.unshift(p)
    saveToStorage('blog_projects', [...projects])
    return p
  }

  function updateProject(id: number, data: Partial<Project>) {
    const idx = projects.findIndex(p => p.id === id)
    if (idx !== -1) {
      Object.assign(projects[idx], data)
      saveToStorage('blog_projects', [...projects])
    }
  }

  function removeProject(id: number) {
    const idx = projects.findIndex(p => p.id === id)
    if (idx !== -1) {
      projects.splice(idx, 1)
      saveToStorage('blog_projects', [...projects])
    }
  }

  function getProjectById(id: number): Project | null {
    return projects.find(p => p.id === id) || null
  }

  // ================= Timeline =================
  function updateTimelineItem(index: number, data: Partial<TimelineItem>) {
    if (index >= 0 && index < timeline.length) {
      Object.assign(timeline[index], data)
      saveToStorage('blog_timeline', [...timeline])
    }
  }

  function addTimelineItem(item: TimelineItem) {
    timeline.push(item)
    saveToStorage('blog_timeline', [...timeline])
  }

  function removeTimelineItem(index: number) {
    if (index >= 0 && index < timeline.length) {
      timeline.splice(index, 1)
      saveToStorage('blog_timeline', [...timeline])
    }
  }

  // ================= Comments =================
  function addComment(comment: Omit<Comment, 'id' | 'createTime'>) {
    const c: Comment = {
      ...comment,
      id: nextId.comment++,
      createTime: new Date().toISOString()
    }
    comments.push(c)
    saveToStorage('blog_comments', [...comments])
    return c
  }

  function getCommentsByArticle(articleId: number): Comment[] {
    return comments.filter(c => c.blogId === articleId && !c.parentId)
  }

  function getReplies(parentId: number): Comment[] {
    return comments.filter(c => c.parentId === parentId)
  }

  return {
    profile,
    skills,
    articles,
    projects,
    timeline,
    comments,
    updateProfile,
    updateSkill, addSkill, removeSkill, reorderSkills,
    addArticle, updateArticle, removeArticle, getArticleById,
    addProject, updateProject, removeProject, getProjectById,
    updateTimelineItem, addTimelineItem, removeTimelineItem,
    addComment, getCommentsByArticle, getReplies
  }
})
