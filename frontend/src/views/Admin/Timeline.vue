<template>
  <DefaultLayout>
    <div class="page-container admin-page">
      <div class="admin-header animate-in">
        <n-button text @click="$router.push('/admin')" class="back-btn">
          <template #icon><n-icon><ArrowBackOutline /></n-icon></template>
          管理后台
        </n-button>
        <div class="header-row">
          <h1>时间线管理</h1>
          <n-button type="primary" @click="showAdd = true">
            <template #icon><n-icon><AddOutline /></n-icon></template>
            添加经历
          </n-button>
        </div>
      </div>

      <!-- 添加表单 -->
      <div v-if="showAdd" class="add-form card-style animate-in">
        <n-grid :cols="4" :x-gap="16" responsive="screen">
          <n-grid-item span="4 m:1">
            <n-input v-model:value="newItem.year" placeholder="年份 (例如: 2026)" />
          </n-grid-item>
          <n-grid-item span="4 m:1">
            <n-input v-model:value="newItem.title" placeholder="标题" />
          </n-grid-item>
          <n-grid-item span="4 m:1">
            <n-select v-model:value="newItem.type" :options="typeOptions" placeholder="类型" />
          </n-grid-item>
          <n-grid-item span="4 m:1">
            <n-input v-model:value="newItem.description" placeholder="描述" />
          </n-grid-item>
        </n-grid>
        <div class="add-actions">
          <n-button type="primary" size="small" @click="addItem">添加</n-button>
          <n-button size="small" @click="showAdd = false">取消</n-button>
        </div>
      </div>

      <!-- 时间线列表 -->
      <div v-for="(item, i) in store.timeline" :key="i" class="timeline-row card-style animate-in">
        <div class="timeline-info">
          <span class="tl-year">{{ item.year }}</span>
          <span class="tl-type-tag">{{ typeLabel(item.type) }}</span>
          <h4>{{ item.title }}</h4>
          <p>{{ item.description }}</p>
        </div>
        <div class="tl-actions">
          <n-popconfirm @positive-click="store.removeTimelineItem(i)">
            <template #trigger>
              <n-button size="tiny" type="error" ghost>
                <template #icon><n-icon><TrashOutline /></n-icon></template>
              </n-button>
            </template>
            确定删除？
          </n-popconfirm>
        </div>
      </div>

      <div v-if="store.timeline.length === 0" class="empty-state">
        <p>暂无时间线条目</p>
      </div>
    </div>
  </DefaultLayout>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { NButton, NIcon, NInput, NSelect, NGrid, NGridItem, NPopconfirm, useMessage } from 'naive-ui'
import { ArrowBackOutline, AddOutline, TrashOutline } from '@vicons/ionicons5'
import DefaultLayout from '@/layouts/DefaultLayout.vue'
import { useDataStore } from '@/stores/data'
import type { TimelineItem } from '@/types'

const store = useDataStore()
const message = useMessage()
const showAdd = ref(false)

const newItem = reactive<Omit<TimelineItem, 'id'>>({ year: '', title: '', description: '', type: 'education' })

const typeOptions = [
  { label: '教育', value: 'education' as const },
  { label: '项目', value: 'project' as const },
  { label: '比赛', value: 'competition' as const },
  { label: '工作', value: 'work' as const }
]

function typeLabel(t: string) {
  const map: Record<string, string> = { education: '教育', project: '项目', competition: '比赛', work: '工作' }
  return map[t] || t
}

function addItem() {
  if (!newItem.year.trim() || !newItem.title.trim()) return
  store.addTimelineItem({ ...newItem })
  newItem.year = ''
  newItem.title = ''
  newItem.description = ''
  newItem.type = 'education'
  showAdd.value = false
  message.success('已添加')
}
</script>

<style scoped lang="scss">
.admin-page {
  max-width: 800px;
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

.add-form {
  margin-bottom: $gap-lg;
  padding: $gap-lg;
}

.add-actions {
  display: flex;
  gap: $gap-sm;
  margin-top: $gap-md;
}

.timeline-row {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  gap: $gap-md;
  margin-bottom: $gap-sm;
  padding: $gap-md $gap-lg;
}

.timeline-info {
  flex: 1;
  min-width: 0;

  .tl-year {
    font-family: $font-mono;
    font-size: 13px;
    color: $accent;
    margin-right: $gap-sm;
  }

  .tl-type-tag {
    font-size: 10px;
    background: rgba($accent, 0.12);
    color: $accent;
    padding: 0 6px;
    border-radius: $radius-sm;
    margin-left: $gap-sm;
  }

  h4 { font-size: 14px; margin: 4px 0; }
  p { font-size: 12px; color: $text-secondary; }
}

.empty-state {
  text-align: center;
  padding: $gap-3xl;
  color: $text-secondary;
}
</style>
