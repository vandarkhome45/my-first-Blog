<template>
  <DefaultLayout>
    <div class="page-container">
      <!-- 加载 -->
      <div v-if="loading" class="loading-state">
        <n-spin size="medium" />
        <p>加载中...</p>
      </div>

      <!-- 不存在 -->
      <div v-else-if="!project" class="empty-state">
        <n-icon size="48"><FolderOpenOutline /></n-icon>
        <p>项目不存在</p>
        <n-button @click="$router.push('/projects')">返回项目列表</n-button>
      </div>

      <!-- 项目详情 -->
      <template v-else>
        <div class="project-header animate-in" @contextmenu.prevent="loggedIn && ctxMenu.show($event, [{ label: '编辑这个项目', action: () => $router.push(`/admin/projects/${project.id}`) }])">
          <n-button text @click="$router.push('/projects')" class="back-btn">
            <template #icon><n-icon><ArrowBackOutline /></n-icon></template>
            返回
          </n-button>
          <h1>{{ project.title }}</h1>
          <div class="project-meta">
            <span class="category-badge">{{ categoryLabel }}</span>
            <span class="meta-time">{{ formatDate(project.createTime) }}</span>
          </div>
        </div>

        <div class="project-body animate-in">
          <p class="project-desc">{{ project.description }}</p>

          <div class="tech-section">
            <h3>技术栈</h3>
            <div class="tech-list">
              <span class="tag" v-for="tech in project.techStack" :key="tech">{{ tech }}</span>
            </div>
          </div>

          <div class="link-section">
            <n-button
              v-if="project.githubUrl"
              type="primary"
              @click="openLink(project.githubUrl)"
            >
              <template #icon><n-icon><LogoGithub /></n-icon></template>
              GitHub
            </n-button>
            <n-button
              v-if="project.demoUrl"
              @click="openLink(project.demoUrl)"
              class="demo-btn"
            >
              <template #icon><n-icon><OpenOutline /></n-icon></template>
              在线演示
            </n-button>
          </div>
        </div>
      </template>
    </div>
  </DefaultLayout>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { NButton, NIcon, NSpin } from 'naive-ui'
import { ArrowBackOutline, LogoGithub, OpenOutline, FolderOpenOutline } from '@vicons/ionicons5'
import DefaultLayout from '@/layouts/DefaultLayout.vue'
import { getProjectById } from '@/api'
import { useContextMenu } from '@/composables/useContextMenu'
import { useAuthStore } from '@/stores/auth'
import type { Project } from '@/types'

const ctxMenu = useContextMenu()
const auth = useAuthStore()
const loggedIn = auth.isLoggedIn()

const route = useRoute()
const loading = ref(false)
const project = ref<Project | null>(null)

const categoryLabel = computed(() => {
  if (!project.value) return ''
  const map: Record<string, string> = { web: 'Web', embedded: '嵌入式', algorithm: '算法', ai: 'AI' }
  return map[project.value.category] || project.value.category
})

function formatDate(d: string) {
  return new Date(d).toLocaleDateString('zh-CN')
}

function openLink(url: string) {
  window.open(url, '_blank')
}

onMounted(async () => {
  loading.value = true
  try {
    const id = Number(route.params.id)
    const res = await getProjectById(id)
    project.value = res.data
  } finally {
    loading.value = false
  }
})
</script>

<style scoped lang="scss">
.project-header {
  margin-bottom: $gap-xl;
  padding-bottom: $gap-lg;
  border-bottom: 1px solid $border-color;

  .back-btn {
    margin-bottom: $gap-md;
    font-size: 13px;
  }

  h1 {
    font-size: 28px;
    font-weight: 700;
    margin-bottom: $gap-md;
  }
}

.project-meta {
  display: flex;
  align-items: center;
  gap: $gap-md;
}

.category-badge {
  background: rgba($accent, 0.15);
  color: $accent;
  padding: 2px 12px;
  border-radius: $radius-sm;
  font-size: 12px;
  font-family: $font-mono;
}

.meta-time {
  color: $text-secondary;
  font-size: 13px;
}

.project-body {
  max-width: 800px;

  .project-desc {
    font-size: 16px;
    line-height: 1.9;
    color: $text-secondary;
    margin-bottom: $gap-xl;
  }
}

.tech-section {
  margin-bottom: $gap-xl;

  h3 {
    font-size: 16px;
    margin-bottom: $gap-md;
  }
}

.tech-list {
  display: flex;
  gap: $gap-xs;
  flex-wrap: wrap;
}

.link-section {
  display: flex;
  gap: $gap-md;
  flex-wrap: wrap;

  .demo-btn {
    border-color: rgba($accent, 0.3);
    color: $accent;
  }
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
