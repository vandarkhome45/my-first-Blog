<template>
  <DefaultLayout>
    <div class="page-container">
      <h1 class="page-title animate-in">博客</h1>

      <!-- 搜索与筛选栏 -->
      <div class="blog-toolbar animate-in">
        <n-input
          v-model:value="searchKey"
          placeholder="搜索文章标题..."
          clearable
          round
          class="search-input"
        >
          <template #prefix>
            <n-icon><SearchOutline /></n-icon>
          </template>
        </n-input>
        <div class="category-filters">
          <n-button
            v-for="cat in categories"
            :key="cat"
            :type="activeCategory === cat ? 'primary' : 'default'"
            size="small"
            round
            @click="activeCategory = cat"
          >{{ cat === '' ? '全部' : cat }}</n-button>
        </div>
      </div>

      <!-- 加载状态 -->
      <div v-if="loading" class="loading-state">
        <n-spin size="medium" />
        <p>加载中...</p>
      </div>

      <!-- 空状态 -->
      <div v-else-if="filteredArticles.length === 0" class="empty-state">
        <n-icon size="48"><DocumentOutline /></n-icon>
        <p>暂无匹配文章</p>
      </div>

      <!-- 文章列表 -->
      <div v-else class="article-grid animate-in">
        <div
          v-for="a in pagedArticles"
          :key="a.id"
          @contextmenu.prevent="loggedIn && ctxMenu.show($event, [
            { label: '编辑文章', action: () => $router.push(`/admin/articles/${a.id}`) },
            { label: '删除文章', action: () => { dataStore.removeArticle(a.id); articles.value = articles.value.filter(x => x.id !== a.id); message.success('已删除') }, danger: true }
          ])"
        >
          <ArticleCard :article="a" />
        </div>
      </div>

      <!-- 分页 -->
      <div v-if="totalPages > 1" class="pagination">
        <n-pagination
          v-model:page="currentPage"
          :page-count="totalPages"
          :page-slot="7"
        />
      </div>
    </div>
  </DefaultLayout>
</template>

<script setup lang="ts">
import { ref, computed, watch, onMounted } from 'vue'
import { NInput, NIcon, NButton, NSpin, NPagination, useMessage } from 'naive-ui'
import { SearchOutline, DocumentOutline } from '@vicons/ionicons5'
import DefaultLayout from '@/layouts/DefaultLayout.vue'
import ArticleCard from '@/components/ArticleCard/index.vue'
import { getArticles } from '@/api'
import { useContextMenu } from '@/composables/useContextMenu'
import { useAuthStore } from '@/stores/auth'
import { useDataStore } from '@/stores/data'
import type { Article } from '@/types'

const ctxMenu = useContextMenu()
const auth = useAuthStore()
const dataStore = useDataStore()
const message = useMessage()
const loggedIn = auth.isLoggedIn()

const loading = ref(false)
const articles = ref<Article[]>([])
const searchKey = ref('')
const activeCategory = ref('')
const currentPage = ref(1)
const pageSize = 9

const categories = ['', 'Java', '前端', '嵌入式', '数据库', '算法', '工具']

const filteredArticles = computed(() => {
  let list = articles.value
  if (activeCategory.value) {
    list = list.filter(a => a.category === activeCategory.value)
  }
  if (searchKey.value.trim()) {
    const kw = searchKey.value.trim().toLowerCase()
    list = list.filter(a =>
      a.title.toLowerCase().includes(kw) ||
      (a.summary && a.summary.toLowerCase().includes(kw)) ||
      (a.tags && a.tags.toLowerCase().includes(kw))
    )
  }
  return list
})

const totalPages = computed(() => Math.max(1, Math.ceil(filteredArticles.value.length / pageSize)))

const pagedArticles = computed(() => {
  const start = (currentPage.value - 1) * pageSize
  return filteredArticles.value.slice(start, start + pageSize)
})

watch([searchKey, activeCategory], () => {
  currentPage.value = 1
})

onMounted(async () => {
  loading.value = true
  try {
    const res = await getArticles()
    articles.value = Array.isArray(res.data) ? res.data : (res.data as any).list || []
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

.blog-toolbar {
  display: flex;
  flex-direction: column;
  gap: $gap-md;
  margin-bottom: $gap-xl;

  .search-input {
    max-width: 420px;
  }

  .category-filters {
    display: flex;
    gap: $gap-sm;
    flex-wrap: wrap;
  }
}

.article-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(340px, 1fr));
  gap: $gap-lg;
  margin-bottom: $gap-xl;
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

.pagination {
  display: flex;
  justify-content: center;
  margin-top: $gap-xl;
}
</style>
