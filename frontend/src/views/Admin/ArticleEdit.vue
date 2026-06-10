<template>
  <DefaultLayout>
    <div class="page-container admin-page">
      <div class="admin-header animate-in">
        <n-button text @click="$router.push('/admin/articles')" class="back-btn">
          <template #icon><n-icon><ArrowBackOutline /></n-icon></template>
          文章管理
        </n-button>
        <h1>{{ isNew ? '新建文章' : '编辑文章' }}</h1>
      </div>

      <div class="editor-layout">
        <!-- 表单区 -->
        <div class="editor-form card-style animate-in">
          <n-form ref="formRef" :model="form" label-placement="top">
            <n-form-item label="标题" path="title" :rule="{ required: true, message: '请输入标题' }">
              <n-input v-model:value="form.title" size="large" placeholder="文章标题" />
            </n-form-item>
            <n-grid :cols="2" :x-gap="16" responsive="screen">
              <n-grid-item span="2 m:1">
                <n-form-item label="分类" path="category">
                  <n-select v-model:value="form.category" :options="categoryOptions" placeholder="选择分类" />
                </n-form-item>
              </n-grid-item>
              <n-grid-item span="2 m:1">
                <n-form-item label="标签" path="tags">
                  <n-input v-model:value="form.tags" placeholder="逗号分隔，例如：Vue, TypeScript" />
                </n-form-item>
              </n-grid-item>
            </n-grid>
            <n-form-item label="摘要" path="summary">
              <n-input v-model:value="form.summary" type="textarea" :autosize="{ minRows: 2, maxRows: 4 }" placeholder="简短描述文章内容" />
            </n-form-item>
            <n-form-item label="正文 (Markdown)" path="content">
              <n-input
                v-model:value="form.content"
                type="textarea"
                :autosize="{ minRows: 12, maxRows: 24 }"
                placeholder="使用 Markdown 语法编写..."
                class="content-editor"
              />
            </n-form-item>
          </n-form>
        </div>

        <!-- 预览区 -->
        <div class="editor-preview card-style animate-in">
          <div class="preview-header">
            <h3>预览</h3>
            <span class="preview-hint">实时渲染效果</span>
          </div>
          <div class="preview-content">
            <MarkdownViewer v-if="form.content" :content="previewContent" />
            <div v-else class="preview-empty">在左侧输入 Markdown 内容即可预览</div>
          </div>
        </div>
      </div>

      <div class="editor-actions animate-in">
        <n-button type="primary" size="large" :loading="saving" @click="save">
          {{ isNew ? '发布文章' : '保存修改' }}
        </n-button>
        <n-button size="large" @click="$router.push('/admin/articles')">取消</n-button>
      </div>
    </div>
  </DefaultLayout>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { NButton, NIcon, NForm, NFormItem, NInput, NSelect, NGrid, NGridItem, useMessage } from 'naive-ui'
import { ArrowBackOutline } from '@vicons/ionicons5'
import DefaultLayout from '@/layouts/DefaultLayout.vue'
import MarkdownViewer from '@/components/MarkdownViewer/index.vue'
import { useDataStore } from '@/stores/data'

const route = useRoute()
const router = useRouter()
const message = useMessage()
const store = useDataStore()
const formRef = ref()
const saving = ref(false)

const isNew = computed(() => !route.params.id || route.params.id === 'new')

const categoryOptions = [
  { label: 'Java', value: 'Java' },
  { label: '前端', value: '前端' },
  { label: '嵌入式', value: '嵌入式' },
  { label: '数据库', value: '数据库' },
  { label: '算法', value: '算法' },
  { label: '工具', value: '工具' }
]

const form = reactive({
  title: '',
  category: 'Java',
  tags: '',
  summary: '',
  content: ''
})

const previewContent = computed(() => {
  const parts: string[] = []
  if (form.title) parts.push(`# ${form.title}`)
  if (form.content) parts.push(form.content)
  return parts.join('\n\n')
})

onMounted(() => {
  if (!isNew.value) {
    const id = Number(route.params.id)
    const article = store.getArticleById(id)
    if (article) {
      form.title = article.title
      form.category = article.category
      form.tags = article.tags
      form.summary = article.summary || ''
      form.content = article.content
    }
  }
})

async function save() {
  const valid = await formRef.value?.validate().catch(() => false)
  if (!valid) return

  saving.value = true
  await new Promise(r => setTimeout(r, 300))

  if (isNew.value) {
    store.addArticle({
      title: form.title,
      category: form.category,
      tags: form.tags,
      summary: form.summary,
      content: form.content
    })
    message.success('文章已发布')
  } else {
    store.updateArticle(Number(route.params.id), {
      title: form.title,
      category: form.category,
      tags: form.tags,
      summary: form.summary,
      content: form.content
    })
    message.success('文章已更新')
  }
  saving.value = false
  router.push('/admin/articles')
}
</script>

<style scoped lang="scss">
.admin-page {
  max-width: 1200px;
  margin: 0 auto;
}

.admin-header {
  margin-bottom: $gap-xl;
  .back-btn { margin-bottom: $gap-sm; }
  h1 { font-size: 24px; font-weight: 700; }
}

.editor-layout {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: $gap-lg;
  margin-bottom: $gap-xl;

  @media (max-width: 800px) {
    grid-template-columns: 1fr;
  }
}

.editor-form {
  padding: $gap-lg;
  min-width: 0;
}

.content-editor {
  font-family: $font-mono;
  font-size: 14px;
}

.editor-preview {
  padding: $gap-lg;

  .preview-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: $gap-md;
    padding-bottom: $gap-sm;
    border-bottom: 1px solid $border-color;

    h3 { font-size: 14px; color: $text-secondary; }
    .preview-hint { font-size: 11px; color: $accent; font-family: $font-mono; }
  }
}

.preview-empty {
  color: $text-secondary;
  font-size: 14px;
  text-align: center;
  padding: $gap-3xl 0;
}

.editor-actions {
  display: flex;
  gap: $gap-md;
  justify-content: flex-end;
}
</style>
