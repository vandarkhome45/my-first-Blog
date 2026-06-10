<template>
  <DefaultLayout>
    <div class="page-container admin-page">
      <div class="admin-header animate-in">
        <n-button text @click="$router.push('/admin')" class="back-btn">
          <template #icon><n-icon><ArrowBackOutline /></n-icon></template>
          管理后台
        </n-button>
        <div class="header-row">
          <h1>文章管理</h1>
          <n-button type="primary" @click="$router.push('/admin/articles/new')">
            <template #icon><n-icon><AddOutline /></n-icon></template>
            新建文章
          </n-button>
        </div>
      </div>

      <div v-if="store.articles.length === 0" class="empty-state">
        <p>暂无文章，点击右上角新建</p>
      </div>

      <div v-for="a in store.articles" :key="a.id" class="admin-item card-style animate-in">
        <div class="item-main">
          <h3>{{ a.title }}</h3>
          <div class="item-meta">
            <span class="category-tag">{{ a.category }}</span>
            <span>{{ formatDate(a.createTime) }}</span>
            <span>👁 {{ a.viewCount }}</span>
          </div>
          <p class="item-summary">{{ a.summary }}</p>
        </div>
        <div class="item-actions">
          <n-button size="small" @click="$router.push(`/admin/articles/${a.id}`)">
            <template #icon><n-icon><CreateOutline /></n-icon></template>
            编辑
          </n-button>
          <n-popconfirm @positive-click="handleDelete(a.id)">
            <template #trigger>
              <n-button size="small" type="error" ghost>
                <template #icon><n-icon><TrashOutline /></n-icon></template>
                删除
              </n-button>
            </template>
            确定删除这篇文章？
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

function formatDate(d: string) {
  return new Date(d).toLocaleDateString('zh-CN')
}

function handleDelete(id: number) {
  store.removeArticle(id)
  message.success('文章已删除')
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

  h3 { font-size: 16px; margin-bottom: $gap-sm; }

  .item-meta {
    display: flex;
    gap: $gap-md;
    align-items: center;
    font-size: 12px;
    color: $text-secondary;
    margin-bottom: $gap-sm;
    flex-wrap: wrap;
  }

  .item-summary {
    font-size: 13px;
    color: $text-secondary;
    line-height: 1.6;
  }
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
