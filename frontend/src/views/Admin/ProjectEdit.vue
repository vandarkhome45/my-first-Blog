<template>
  <DefaultLayout>
    <div class="page-container admin-page">
      <div class="admin-header animate-in">
        <n-button text @click="$router.push('/admin/projects')" class="back-btn">
          <template #icon><n-icon><ArrowBackOutline /></n-icon></template>
          项目管理
        </n-button>
        <h1>{{ isNew ? '新建项目' : '编辑项目' }}</h1>
      </div>

      <div class="admin-form card-style animate-in">
        <n-form ref="formRef" :model="form" label-placement="top">
          <n-grid :cols="2" :x-gap="24" responsive="screen">
            <n-grid-item span="2">
              <n-form-item label="项目名称" path="title" :rule="{ required: true, message: '请输入项目名称' }">
                <n-input v-model:value="form.title" size="large" />
              </n-form-item>
            </n-grid-item>
            <n-grid-item span="2">
              <n-form-item label="项目描述" path="description" :rule="{ required: true, message: '请输入项目描述' }">
                <n-input v-model:value="form.description" type="textarea" :autosize="{ minRows: 3, maxRows: 6 }" />
              </n-form-item>
            </n-grid-item>
            <n-grid-item span="2 m:1">
              <n-form-item label="分类" path="category">
                <n-select v-model:value="form.category" :options="categoryOptions" />
              </n-form-item>
            </n-grid-item>
            <n-grid-item span="2 m:1">
              <n-form-item label="技术栈（逗号分隔）" path="techStackStr">
                <n-input v-model:value="techStackStr" placeholder="Java, Spring Boot, Vue 3" />
              </n-form-item>
            </n-grid-item>
            <n-grid-item span="2 m:1">
              <n-form-item label="GitHub 地址" path="githubUrl">
                <n-input v-model:value="form.githubUrl" placeholder="https://github.com/..." />
              </n-form-item>
            </n-grid-item>
            <n-grid-item span="2 m:1">
              <n-form-item label="演示地址" path="demoUrl">
                <n-input v-model:value="form.demoUrl" placeholder="https://..." />
              </n-form-item>
            </n-grid-item>
            <n-grid-item span="2">
              <n-form-item label="是否精选" path="featured">
                <n-switch v-model:value="form.featured" />
              </n-form-item>
            </n-grid-item>
          </n-grid>
        </n-form>
        <div class="form-actions">
          <n-button type="primary" size="large" :loading="saving" @click="save">
            {{ isNew ? '创建项目' : '保存修改' }}
          </n-button>
          <n-button size="large" @click="$router.push('/admin/projects')">取消</n-button>
        </div>
      </div>
    </div>
  </DefaultLayout>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { NButton, NIcon, NForm, NFormItem, NInput, NSelect, NSwitch, NGrid, NGridItem, useMessage } from 'naive-ui'
import { ArrowBackOutline } from '@vicons/ionicons5'
import DefaultLayout from '@/layouts/DefaultLayout.vue'
import { useDataStore } from '@/stores/data'

const route = useRoute()
const router = useRouter()
const message = useMessage()
const store = useDataStore()
const formRef = ref()
const saving = ref(false)
const techStackStr = ref('')

const isNew = computed(() => !route.params.id || route.params.id === 'new')

const categoryOptions = [
  { label: 'Web', value: 'web' as const },
  { label: '嵌入式', value: 'embedded' as const },
  { label: '算法', value: 'algorithm' as const },
  { label: 'AI', value: 'ai' as const }
]

const form = reactive<{
  title: string
  description: string
  category: 'web' | 'embedded' | 'algorithm' | 'ai'
  githubUrl: string
  demoUrl: string
  featured: boolean
  techStack: string[]
}>({
  title: '',
  description: '',
  category: 'web',
  githubUrl: '',
  demoUrl: '',
  featured: false,
  techStack: []
})

watch(techStackStr, (val) => {
  form.techStack = val.split(/[,，]/).map(t => t.trim()).filter(Boolean)
})

onMounted(() => {
  if (!isNew.value) {
    const id = Number(route.params.id)
    const project = store.getProjectById(id)
    if (project) {
      form.title = project.title
      form.description = project.description
      form.category = project.category
      form.githubUrl = project.githubUrl || ''
      form.demoUrl = project.demoUrl || ''
      form.featured = project.featured
      form.techStack = [...project.techStack]
      techStackStr.value = project.techStack.join(', ')
    }
  }
})

async function save() {
  const valid = await formRef.value?.validate().catch(() => false)
  if (!valid) return

  saving.value = true
  await new Promise(r => setTimeout(r, 300))

  const data = {
    title: form.title,
    description: form.description,
    category: form.category,
    techStack: form.techStack,
    githubUrl: form.githubUrl,
    demoUrl: form.demoUrl,
    featured: form.featured
  }

  if (isNew.value) {
    store.addProject(data)
    message.success('项目已创建')
  } else {
    store.updateProject(Number(route.params.id), data)
    message.success('项目已更新')
  }
  saving.value = false
  router.push('/admin/projects')
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
  h1 { font-size: 24px; font-weight: 700; }
}

.admin-form {
  padding: $gap-xl;
}

.form-actions {
  display: flex;
  gap: $gap-md;
  margin-top: $gap-lg;
}
</style>
