<template>
  <DefaultLayout>
    <div class="page-container article-page">
      <!-- 加载 -->
      <div v-if="loading" class="loading-state">
        <n-spin size="medium" />
        <p>加载中...</p>
      </div>

      <!-- 文章不存在 -->
      <div v-else-if="!article" class="empty-state">
        <n-icon size="48"><DocumentOutline /></n-icon>
        <p>文章不存在</p>
        <n-button @click="$router.push('/blog')">返回博客</n-button>
      </div>

      <!-- 文章内容 -->
      <template v-else>
        <!-- 文章头部 -->
        <header class="article-header animate-in" @contextmenu.prevent="loggedIn && ctxMenu.show($event, [{ label: '编辑这篇文章', action: () => $router.push(`/admin/articles/${article.id}`) }])">
          <h1>{{ article.title }}</h1>
          <div class="article-meta">
            <div class="meta-left">
              <span class="category-tag">{{ article.category }}</span>
              <span class="meta-item">{{ formatDate(article.createTime) }}</span>
              <span class="meta-item">👁 {{ article.viewCount }}</span>
              <span class="meta-item">❤ {{ article.likeCount }}</span>
            </div>
            <div class="tags">
              <span class="tag" v-for="t in tagList" :key="t">{{ t }}</span>
            </div>
          </div>
        </header>

        <!-- 文章正文 + 侧边栏 -->
        <div class="article-body-layout">
          <!-- TOC 侧边栏 -->
          <aside class="toc-sidebar" v-if="headings.length > 0">
            <div class="toc-sticky">
              <h4>目录</h4>
              <nav class="toc-nav">
                <a
                  v-for="(h, i) in headings"
                  :key="i"
                  :class="['toc-item', `toc-${h.level}`, { active: activeHeading === h.id }]"
                  :href="`#${h.id}`"
                  @click.prevent="scrollToHeading(h.id)"
                >{{ h.text }}</a>
              </nav>
            </div>
          </aside>

          <!-- 正文 -->
          <main class="article-main">
            <MarkdownViewer :content="article.content" ref="markdownRef" />
          </main>
        </div>

        <!-- 评论区 -->
        <section class="comments-section">
          <h3>评论 ({{ comments.length }})</h3>
          <div v-if="comments.length === 0" class="no-comments">暂无评论，来抢沙发吧</div>
          <div v-for="c in comments" :key="c.id" class="comment-item card-style">
            <div class="comment-header">
              <strong>{{ c.author }}</strong>
              <span class="comment-time">{{ formatDate(c.createTime) }}</span>
            </div>
            <p class="comment-content">{{ c.content }}</p>
            <n-button text size="small" type="primary" @click="replyTo = c">回复</n-button>

            <!-- 回复表单 -->
            <div v-if="replyTo === c" class="reply-form">
              <n-input
                v-model:value="replyContent"
                type="textarea"
                placeholder="写下你的回复..."
                :autosize="{ minRows: 2, maxRows: 4 }"
              />
              <div class="reply-actions">
                <n-button size="small" type="primary" @click="submitReply(c)">提交</n-button>
                <n-button size="small" @click="cancelReply">取消</n-button>
              </div>
            </div>

            <!-- 子回复 -->
            <div v-if="c.replies && c.replies.length" class="replies">
              <div v-for="r in c.replies" :key="r.id" class="reply-item">
                <strong>{{ r.author }}</strong>
                <span class="comment-time">{{ formatDate(r.createTime) }}</span>
                <p>{{ r.content }}</p>
              </div>
            </div>
          </div>

          <!-- 新评论表单 -->
          <div class="new-comment card-style">
            <h4>发表评论</h4>
            <n-input v-model:value="newCommentAuthor" placeholder="昵称" class="comment-field" />
            <n-input
              v-model:value="newCommentContent"
              type="textarea"
              placeholder="写下你的想法..."
              :autosize="{ minRows: 3, maxRows: 6 }"
              class="comment-field"
            />
            <n-button type="primary" @click="submitComment" :disabled="!newCommentContent.trim()">
              提交评论
            </n-button>
          </div>
        </section>
      </template>
    </div>
  </DefaultLayout>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, nextTick, watch } from 'vue'
import { useRoute } from 'vue-router'
import { NIcon, NButton, NInput, NSpin } from 'naive-ui'
import { DocumentOutline } from '@vicons/ionicons5'
import DefaultLayout from '@/layouts/DefaultLayout.vue'
import MarkdownViewer from '@/components/MarkdownViewer/index.vue'
import { getArticleById, getComments } from '@/api'
import { useContextMenu } from '@/composables/useContextMenu'
import { useAuthStore } from '@/stores/auth'
import type { Article, Comment } from '@/types'

const ctxMenu = useContextMenu()
const auth = useAuthStore()
const loggedIn = auth.isLoggedIn()

const route = useRoute()
const markdownRef = ref<InstanceType<typeof MarkdownViewer>>()

const loading = ref(false)
const article = ref<Article | null>(null)
const comments = ref<Comment[]>([])
const replyTo = ref<Comment | null>(null)
const replyContent = ref('')
const newCommentAuthor = ref('')
const newCommentContent = ref('')
const activeHeading = ref('')

interface Heading { id: string; text: string; level: number }
const headings = ref<Heading[]>([])

const tagList = computed(() => {
  if (!article.value?.tags) return []
  return article.value.tags.split(/[,，]/).map(t => t.trim()).filter(Boolean)
})

function formatDate(d: string) {
  return new Date(d).toLocaleDateString('zh-CN')
}

function extractHeadings() {
  if (!markdownRef.value) return
  nextTick(() => {
    const el = (markdownRef.value as any)?.$el
    if (!el) return
    const hs = el.querySelectorAll('h1, h2, h3')
    headings.value = Array.from(hs).map((h: any) => ({
      id: h.id || h.textContent.replace(/\s+/g, '-'),
      text: h.textContent,
      level: parseInt(h.tagName[1])
    }))
  })
}

function scrollToHeading(id: string) {
  activeHeading.value = id
  const el = document.getElementById(id)
  if (el) {
    el.scrollIntoView({ behavior: 'smooth', block: 'start' })
  }
}

function cancelReply() {
  replyTo.value = null
  replyContent.value = ''
}

function submitReply(parent: Comment) {
  if (!replyContent.value.trim()) return
  const newReply: Comment = {
    id: Date.now(),
    content: replyContent.value,
    author: '匿名用户',
    blogId: article.value?.id,
    parentId: parent.id,
    createTime: new Date().toISOString()
  }
  if (!parent.replies) parent.replies = []
  parent.replies.push(newReply)
  cancelReply()
}

function submitComment() {
  if (!newCommentContent.value.trim()) return
  const newComment: Comment = {
    id: Date.now(),
    content: newCommentContent.value,
    author: newCommentAuthor.value.trim() || '匿名用户',
    blogId: article.value?.id,
    parentId: null,
    createTime: new Date().toISOString()
  }
  comments.value.unshift(newComment)
  newCommentAuthor.value = ''
  newCommentContent.value = ''
}

onMounted(async () => {
  loading.value = true
  try {
    const id = Number(route.params.id)
    const [articleRes, commentsRes] = await Promise.all([
      getArticleById(id),
      getComments(id)
    ])
    article.value = articleRes.data
    comments.value = commentsRes.data
    await nextTick()
    extractHeadings()
  } finally {
    loading.value = false
  }
})
</script>

<style scoped lang="scss">
.article-page {
  max-width: 1200px;
  margin: 0 auto;
}

.article-header {
  margin-bottom: $gap-xl;
  padding-bottom: $gap-lg;
  border-bottom: 1px solid $border-color;

  h1 {
    font-size: 32px;
    font-weight: 700;
    margin-bottom: $gap-md;
    line-height: 1.3;
  }
}

.article-meta {
  display: flex;
  flex-wrap: wrap;
  justify-content: space-between;
  align-items: center;
  gap: $gap-md;
}

.meta-left {
  display: flex;
  align-items: center;
  gap: $gap-md;
  flex-wrap: wrap;
}

.category-tag {
  background: rgba($accent, 0.15);
  color: $accent;
  padding: 2px 12px;
  border-radius: $radius-sm;
  font-size: 12px;
  font-family: $font-mono;
}

.meta-item {
  color: $text-secondary;
  font-size: 13px;
}

.tags {
  display: flex;
  gap: $gap-xs;
  flex-wrap: wrap;
}

// 正文 + 侧边栏布局
.article-body-layout {
  display: flex;
  gap: $gap-2xl;
  align-items: flex-start;
}

.toc-sidebar {
  width: 220px;
  flex-shrink: 0;
  display: none;

  @media (min-width: 900px) {
    display: block;
  }
}

.toc-sticky {
  position: sticky;
  top: 80px;

  h4 {
    font-size: 14px;
    color: $text-secondary;
    margin-bottom: $gap-sm;
    text-transform: uppercase;
    letter-spacing: 1px;
  }
}

.toc-nav {
  display: flex;
  flex-direction: column;
  gap: 4px;
  border-left: 1px solid $border-color;
  padding-left: $gap-sm;
}

.toc-item {
  font-size: 13px;
  color: $text-secondary;
  padding: 2px 0;
  transition: color $transition;
  display: block;

  &.toc-1 { padding-left: 0; font-weight: 600; }
  &.toc-2 { padding-left: $gap-sm; }
  &.toc-3 { padding-left: $gap-md; font-size: 12px; }

  &.active, &:hover {
    color: $accent;
  }
}

.article-main {
  flex: 1;
  min-width: 0;
}

// Comments
.comments-section {
  margin-top: $gap-3xl;
  padding-top: $gap-xl;
  border-top: 1px solid $border-color;

  h3 { font-size: 20px; margin-bottom: $gap-lg; }

  .no-comments {
    color: $text-secondary;
    text-align: center;
    padding: $gap-xl 0;
  }
}

.comment-item {
  margin-bottom: $gap-md;
  padding: $gap-md $gap-lg;

  .comment-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: $gap-sm;

    strong { font-size: 14px; }
    .comment-time { font-size: 12px; color: $text-secondary; }
  }

  .comment-content {
    font-size: 14px;
    line-height: 1.7;
    color: $text-secondary;
  }
}

.reply-form {
  margin-top: $gap-md;
  display: flex;
  flex-direction: column;
  gap: $gap-sm;

  .reply-actions {
    display: flex;
    gap: $gap-sm;
    justify-content: flex-end;
  }
}

.replies {
  margin-top: $gap-md;
  padding-left: $gap-lg;
  border-left: 2px solid $border-color;

  .reply-item {
    margin-bottom: $gap-sm;
    strong { font-size: 13px; }
    p { font-size: 13px; color: $text-secondary; margin-top: 2px; }
  }
}

.new-comment {
  margin-top: $gap-xl;

  h4 { margin-bottom: $gap-md; }
  .comment-field { margin-bottom: $gap-sm; }
}

.loading-state, .empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: $gap-md;
  padding: $gap-3xl 0;
  color: $text-secondary;
}
</style>
