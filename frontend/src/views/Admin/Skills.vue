<template>
  <DefaultLayout>
    <div class="page-container admin-page">
      <div class="admin-header animate-in">
        <n-button text @click="$router.push('/admin')" class="back-btn">
          <template #icon><n-icon><ArrowBackOutline /></n-icon></template>
          管理后台
        </n-button>
        <div class="header-row">
          <h1>技能管理</h1>
          <n-button type="primary" @click="showAdd = true">
            <template #icon><n-icon><AddOutline /></n-icon></template>
            添加技能
          </n-button>
        </div>
      </div>

      <!-- 添加表单 -->
      <div v-if="showAdd" class="add-form card-style animate-in">
        <n-grid :cols="3" :x-gap="16" responsive="screen">
          <n-grid-item span="3 m:1">
            <n-input v-model:value="newSkill.name" placeholder="技能名称" />
          </n-grid-item>
          <n-grid-item span="3 m:1">
            <n-select v-model:value="newSkill.category" :options="categoryOptions" placeholder="分类" />
          </n-grid-item>
          <n-grid-item span="3 m:1">
            <n-input-number v-model:value="newSkill.level" :min="0" :max="100" placeholder="掌握度" />
          </n-grid-item>
        </n-grid>
        <div class="add-actions">
          <n-button type="primary" size="small" @click="addSkill">添加</n-button>
          <n-button size="small" @click="showAdd = false">取消</n-button>
        </div>
      </div>

      <!-- 技能列表 -->
      <div class="skills-list">
        <div v-for="(skill, i) in store.skills" :key="i" class="skill-row card-style animate-in">
          <div class="skill-info">
            <span class="skill-name">{{ skill.name }}</span>
            <span class="skill-cat-tag">{{ categoryLabel(skill.category) }}</span>
          </div>
          <div class="skill-bar-row">
            <n-slider v-model:value="skill.level" :min="0" :max="100" :step="5" class="skill-slider" />
            <span class="skill-val">{{ skill.level }}%</span>
          </div>
          <n-button size="tiny" type="error" ghost @click="store.removeSkill(i)">
            <template #icon><n-icon><CloseOutline /></n-icon></template>
          </n-button>
        </div>
      </div>

      <div v-if="store.skills.length === 0" class="empty-state">
        <p>暂无技能</p>
      </div>
    </div>
  </DefaultLayout>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { NButton, NIcon, NInput, NSelect, NInputNumber, NSlider, NGrid, NGridItem, useMessage } from 'naive-ui'
import { ArrowBackOutline, AddOutline, CloseOutline } from '@vicons/ionicons5'
import DefaultLayout from '@/layouts/DefaultLayout.vue'
import { useDataStore } from '@/stores/data'

const store = useDataStore()
const message = useMessage()
const showAdd = ref(false)

const newSkill = reactive({ name: '', category: 'other' as const, level: 50 })

const categoryOptions = [
  { label: '前端', value: 'frontend' as const },
  { label: '后端', value: 'backend' as const },
  { label: '数据库', value: 'database' as const },
  { label: '嵌入式', value: 'embedded' as const },
  { label: '其他', value: 'other' as const }
]

function categoryLabel(c: string) {
  const map: Record<string, string> = { frontend: '前端', backend: '后端', database: '数据库', embedded: '嵌入式', other: '其他' }
  return map[c] || c
}

function addSkill() {
  if (!newSkill.name.trim()) return
  store.addSkill({ name: newSkill.name.trim(), category: newSkill.category, level: newSkill.level })
  newSkill.name = ''
  newSkill.level = 50
  showAdd.value = false
  message.success('技能已添加')
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

.skills-list {
  display: flex;
  flex-direction: column;
  gap: $gap-sm;
}

.skill-row {
  display: flex;
  align-items: center;
  gap: $gap-md;
  padding: $gap-sm $gap-md;
}

.skill-info {
  display: flex;
  align-items: center;
  gap: $gap-sm;
  width: 180px;
  flex-shrink: 0;
}

.skill-name {
  font-size: 14px;
  font-family: $font-mono;
}

.skill-cat-tag {
  font-size: 11px;
  color: $text-secondary;
  background: rgba($accent, 0.1);
  padding: 0 6px;
  border-radius: $radius-sm;
}

.skill-bar-row {
  flex: 1;
  display: flex;
  align-items: center;
  gap: $gap-md;
  min-width: 0;
}

.skill-slider {
  flex: 1;
}

.skill-val {
  font-size: 13px;
  color: $accent;
  font-family: $font-mono;
  width: 36px;
  text-align: right;
}

.empty-state {
  text-align: center;
  padding: $gap-3xl;
  color: $text-secondary;
}
</style>
