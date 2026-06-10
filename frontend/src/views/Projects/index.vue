<template>
  <DefaultLayout>
    <div class="page-container">
      <h1 class="page-title animate-in">项目</h1>

      <!-- 分类筛选 -->
      <div class="filter-bar animate-in">
        <n-button
          v-for="cat in categories"
          :key="cat.value"
          :type="activeCategory === cat.value ? 'primary' : 'default'"
          size="small"
          round
          @click="activeCategory = cat.value"
        >{{ cat.label }}</n-button>
      </div>

      <!-- 加载 -->
      <div v-if="loading" class="loading-state">
        <n-spin size="medium" />
        <p>加载中...</p>
      </div>

      <!-- 空状态 -->
      <div v-else-if="filteredProjects.length === 0" class="empty-state">
        <n-icon size="48"><FolderOpenOutline /></n-icon>
        <p>暂无项目</p>
      </div>

      <!-- 项目网格 -->
      <div v-else class="project-grid animate-in">
        <div
          v-for="p in filteredProjects"
          :key="p.id"
          @contextmenu.prevent="loggedIn && ctxMenu.show($event, [
            { label: '编辑项目', action: () => $router.push(`/admin/projects/${p.id}`) },
            { label: '删除项目', action: () => { dataStore.removeProject(p.id); projects.value = projects.value.filter(x => x.id !== p.id); message.success('已删除') }, danger: true }
          ])"
        >
          <ProjectCard :project="p" />
        </div>
      </div>
    </div>
  </DefaultLayout>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { NButton, NIcon, NSpin, useMessage } from 'naive-ui'
import { FolderOpenOutline } from '@vicons/ionicons5'
import DefaultLayout from '@/layouts/DefaultLayout.vue'
import ProjectCard from '@/components/ProjectCard/index.vue'
import { getProjects } from '@/api'
import { useContextMenu } from '@/composables/useContextMenu'
import { useAuthStore } from '@/stores/auth'
import { useDataStore } from '@/stores/data'
import type { Project } from '@/types'

const ctxMenu = useContextMenu()
const auth = useAuthStore()
const dataStore = useDataStore()
const message = useMessage()
const loggedIn = auth.isLoggedIn()

const loading = ref(false)
const projects = ref<Project[]>([])
const activeCategory = ref('')

const categories = [
  { label: '全部', value: '' },
  { label: 'Web', value: 'web' },
  { label: '嵌入式', value: 'embedded' },
  { label: '算法', value: 'algorithm' },
  { label: 'AI', value: 'ai' }
]

const filteredProjects = computed(() => {
  if (!activeCategory.value) return projects.value
  return projects.value.filter(p => p.category === activeCategory.value)
})

onMounted(async () => {
  loading.value = true
  try {
    const res = await getProjects()
    projects.value = res.data
  } finally {
    loading.value = false
  }
})
</script>

<style scoped lang="scss">
.page-title {
  font-size: 32px;
  font-weight: 700;
  margin-bottom: $gap-xl;
}

.filter-bar {
  display: flex;
  gap: $gap-sm;
  flex-wrap: wrap;
  margin-bottom: $gap-xl;
}

.project-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(340px, 1fr));
  gap: $gap-lg;
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
