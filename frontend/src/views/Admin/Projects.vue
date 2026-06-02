<template>
  <DefaultLayout>
    <div class="page-container admin-page">
      <div class="admin-header animate-in">
        <n-button text @click="$router.push('/admin')" class="back-btn">
          <template #icon><n-icon><ArrowBackOutline /></n-icon></template>
          管理后台
        </n-button>
        <div class="header-row">
          <h1>项目管理</h1>
          <n-button type="primary" @click="$router.push('/admin/projects/new')">
            <template #icon><n-icon><AddOutline /></n-icon></template>
            新建项目
          </n-button>
        </div>
      </div>

      <div v-if="store.projects.length === 0" class="empty-state">
        <p>暂无项目，点击右上角新建</p>
      </div>

      <div v-for="p in store.projects" :key="p.id" class="admin-item card-style animate-in">
        <div class="item-main">
          <div class="item-top">
            <h3>{{ p.title }}</h3>
            <span class="category-tag">{{ categoryLabel(p.category) }}</span>
            <span v-if="p.featured" class="featured-badge">精选</span>
          </div>
          <p class="item-desc">{{ p.description }}</p>
          <div class="tech-list">
            <span class="tag" v-for="t in p.techStack" :key="t">{{ t }}</span>
          </div>
        </div>
        <div class="item-actions">
          <n-button size="small" @click="$router.push(`/admin/projects/${p.id}`)">
            <template #icon><n-icon><CreateOutline /></n-icon></template>
            编辑
          </n-button>
          <n-popconfirm @positive-click="handleDelete(p.id)">
            <template #trigger>
              <n-button size="small" type="error" ghost>
                <template #icon><n-icon><TrashOutline /></n-icon></template>
                删除
              </n-button>
            </template>
            确定删除这个项目？
          </n-popconfirm>
        </div>
      </div>
    </div>
  </DefaultLayout>
</template>

<script setup lang="ts">
import { NButton, NIcon, NPopconfirm, useMessage } from 'naive-ui'
import { ArrowBackOutline, AddOutline, CreateOutline, TrashOutline } from '@vicons/ionicons5'
import DefaultLayout from '@/layouts/DefaultLayout.vue'
import { useDataStore } from '@/stores/data'

const store = useDataStore()
const message = useMessage()

function categoryLabel(c: string) {
  const map: Record<string, string> = { web: 'Web', embedded: '嵌入式', algorithm: '算法', ai: 'AI' }
  return map[c] || c
}

function handleDelete(id: number) {
  store.removeProject(id)
  message.success('项目已删除')
}
</script>

<style scoped lang="scss">
.admin-page {
  max-width: 900px;
  margin: 0 auto;
}

.admin-header {
  margin-bottom: $gap-xl;
  .back-btn { margin-bottom: $gap-sm; }
}

.header-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-wrap: wrap;
  gap: $gap-md;
  h1 { font-size: 24px; font-weight: 700; }
}

.admin-item {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  gap: $gap-lg;
  margin-bottom: $gap-md;
  padding: $gap-md $gap-lg;
}

.item-main {
  flex: 1;
  min-width: 0;

  .item-top {
    display: flex;
    align-items: center;
    gap: $gap-sm;
    flex-wrap: wrap;
    margin-bottom: $gap-sm;

    h3 { font-size: 16px; }
  }

  .item-desc {
    font-size: 13px;
    color: $text-secondary;
    line-height: 1.6;
    margin-bottom: $gap-sm;
  }
}

.featured-badge {
  background: rgba($accent, 0.2);
  color: $accent;
  padding: 0 6px;
  border-radius: $radius-sm;
  font-size: 10px;
  text-transform: uppercase;
}

.tech-list {
  display: flex;
  gap: $gap-xs;
  flex-wrap: wrap;
}

.category-tag {
  background: rgba($accent, 0.15);
  color: $accent;
  padding: 1px 8px;
  border-radius: $radius-sm;
  font-size: 11px;
}

.item-actions {
  display: flex;
  gap: $gap-sm;
  flex-shrink: 0;
}

.empty-state {
  text-align: center;
  padding: $gap-3xl;
  color: $text-secondary;
}
</style>
