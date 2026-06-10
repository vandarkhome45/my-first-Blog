import type { ApiResponse, PageResult, Article, Project, Comment, Profile, Skill, TimelineItem } from '@/types'
import { useDataStore } from '@/stores/data'

const delay = (ms = 200) => new Promise(resolve => setTimeout(resolve, ms))

function ok<T>(data: T): ApiResponse<T> {
  return { code: 200, msg: 'success', data }
}

function getStore() {
  return useDataStore()
}

// ============ Blog / Article ============
export async function getArticles(params?: { page?: number; size?: number; category?: string; tag?: string }): Promise<ApiResponse<Article[] | PageResult<Article>>> {
  await delay()
  const s = getStore()
  let list = [...s.articles]
  if (params?.category) list = list.filter(a => a.category === params.category)
  if (params?.tag) list = list.filter(a => a.tags.includes(params.tag!))
  if (params?.page !== undefined && params?.size) {
    const start = params.page * params.size
    return ok({ list: list.slice(start, start + params.size), total: list.length })
  }
  return ok(list)
}

export async function getArticleById(id: number): Promise<ApiResponse<Article | null>> {
  await delay()
  return ok(getStore().getArticleById(id))
}

export async function saveArticle(data: Omit<Article, 'id' | 'viewCount' | 'likeCount' | 'createTime'> & { id?: number }): Promise<ApiResponse<Article>> {
  await delay()
  const s = getStore()
  if (data.id) {
    s.updateArticle(data.id, data)
    return ok(s.getArticleById(data.id)!)
  }
  return ok(s.addArticle(data))
}

export async function deleteArticle(id: number): Promise<ApiResponse<null>> {
  await delay()
  getStore().removeArticle(id)
  return ok(null)
}

// ============ Project ============
export async function getProjects(category?: string): Promise<ApiResponse<Project[]>> {
  await delay()
  const s = getStore()
  const list = category ? s.projects.filter(p => p.category === category) : s.projects
  return ok(list)
}

export async function getProjectById(id: number): Promise<ApiResponse<Project | null>> {
  await delay()
  return ok(getStore().getProjectById(id))
}

export async function saveProject(data: Omit<Project, 'id' | 'createTime'> & { id?: number }): Promise<ApiResponse<Project>> {
  await delay()
  const s = getStore()
  if (data.id) {
    s.updateProject(data.id, data)
    return ok(s.getProjectById(data.id)!)
  }
  return ok(s.addProject(data))
}

export async function deleteProject(id: number): Promise<ApiResponse<null>> {
  await delay()
  getStore().removeProject(id)
  return ok(null)
}

// ============ Comment ============
export async function getComments(articleId: number): Promise<ApiResponse<Comment[]>> {
  await delay()
  return ok(getStore().getCommentsByArticle(articleId))
}

export async function getReplies(parentId: number): Promise<ApiResponse<Comment[]>> {
  await delay()
  return ok(getStore().getReplies(parentId))
}

export async function addCommentToArticle(comment: Omit<Comment, 'id' | 'createTime'>): Promise<ApiResponse<Comment>> {
  await delay()
  return ok(getStore().addComment(comment))
}

// ============ Profile ============
export async function getProfile(): Promise<ApiResponse<Profile>> {
  await delay()
  return ok({ ...getStore().profile })
}

export async function saveProfile(data: Partial<Profile>): Promise<ApiResponse<Profile>> {
  await delay()
  getStore().updateProfile(data)
  return ok({ ...getStore().profile })
}

// ============ Skills ============
export async function getSkills(): Promise<ApiResponse<Skill[]>> {
  await delay()
  return ok([...getStore().skills])
}

export async function saveSkill(data: { index: number; skill: Partial<Skill> }): Promise<ApiResponse<Skill[]>> {
  await delay()
  const s = getStore()
  if (data.index >= 0 && data.index < s.skills.length) {
    s.updateSkill(data.index, data.skill)
  }
  return ok([...s.skills])
}

export async function addSkill(skill: Skill): Promise<ApiResponse<Skill[]>> {
  await delay()
  getStore().addSkill(skill)
  return ok([...getStore().skills])
}

export async function removeSkill(index: number): Promise<ApiResponse<Skill[]>> {
  await delay()
  getStore().removeSkill(index)
  return ok([...getStore().skills])
}

// ============ Timeline ============
export async function getTimeline(): Promise<ApiResponse<TimelineItem[]>> {
  await delay()
  return ok([...getStore().timeline])
}

export async function saveTimelineItem(data: { index: number; item: Partial<TimelineItem> }): Promise<ApiResponse<TimelineItem[]>> {
  await delay()
  getStore().updateTimelineItem(data.index, data.item)
  return ok([...getStore().timeline])
}

export async function addTimelineItem(item: TimelineItem): Promise<ApiResponse<TimelineItem[]>> {
  await delay()
  getStore().addTimelineItem(item)
  return ok([...getStore().timeline])
}

export async function removeTimelineItem(index: number): Promise<ApiResponse<TimelineItem[]>> {
  await delay()
  getStore().removeTimelineItem(index)
  return ok([...getStore().timeline])
}

// ============ User (Mock) ============
export async function login(username: string, password: string): Promise<ApiResponse<{ token: string }>> {
  await delay(500)
  if (username && password) {
    return ok({ token: 'mock-jwt-token-' + Date.now() })
  }
  throw new Error('用户名或密码错误')
}

export async function register(data: { username: string; password: string; email: string }): Promise<ApiResponse<null>> {
  await delay(500)
  return ok(null)
}
