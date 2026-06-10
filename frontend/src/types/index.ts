// ============= Blog Article =============
export interface Article {
  id: number
  title: string
  content: string
  originalContent?: string
  renderedContent?: string
  summary?: string
  category: string
  tags: string
  viewCount: number
  likeCount: number
  createTime: string
  updateTime?: string
}

// ============= Project =============
export interface Project {
  id: number
  title: string
  description: string
  cover?: string
  techStack: string[]
  githubUrl?: string
  demoUrl?: string
  category: 'web' | 'embedded' | 'algorithm' | 'ai'
  featured: boolean
  createTime: string
}

// ============= Comment =============
export interface Comment {
  id: number
  content: string
  author: string
  email?: string
  blogId?: number
  parentId?: number | null
  createTime: string
  replies?: Comment[]
}

// ============= User =============
export interface User {
  id: number
  username: string
  nickname?: string
  email?: string
  avatar?: string
  bio?: string
  role: string
}

// ============= Skill =============
export interface Skill {
  name: string
  category: 'frontend' | 'backend' | 'database' | 'embedded' | 'other'
  level: number // 0-100
}

// ============= Timeline Item =============
export interface TimelineItem {
  year: string
  title: string
  description: string
  type: 'education' | 'project' | 'competition' | 'work'
}

// ============= API Response =============
export interface ApiResponse<T> {
  code: number
  msg: string
  data: T
}

export interface PageResult<T> {
  list: T[]
  total: number
}

// ============= Profile =============
export interface Profile {
  name: string
  avatar: string
  title: string
  school: string
  major: string
  bio: string
  email: string
  github: string
  qq: string
  wechat: string
}
