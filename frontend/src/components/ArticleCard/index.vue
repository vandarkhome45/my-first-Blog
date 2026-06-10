<template>
  <div class="article-card card-style" @click="$router.push(`/blog/${article.id}`)">
    <h3>{{ article.title }}</h3>
    <p class="summary">{{ article.summary || article.content?.slice(0, 120) || '' }}</p>
    <div class="meta">
      <div class="tags">
        <span class="tag" v-for="t in tagList" :key="t">{{ t }}</span>
      </div>
      <div class="stats">
        <span>{{ formatDate(article.createTime) }}</span>
        <span>👁 {{ article.viewCount }}</span>
        <span>❤ {{ article.likeCount }}</span>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import type { Article } from '@/types'

const props = defineProps<{ article: Article }>()

const tagList = computed(() => {
  if (!props.article.tags) return []
  return props.article.tags.split(/[,，]/).map(t => t.trim()).filter(Boolean)
})

function formatDate(d: string) {
  return new Date(d).toLocaleDateString('zh-CN')
}
</script>

<style scoped lang="scss">
.article-card {
  cursor: pointer;

  h3 { font-size: 18px; margin-bottom: $gap-sm; }

  .summary {
    color: $text-secondary;
    font-size: 14px;
    line-height: 1.7;
    margin-bottom: $gap-md;
  }

  .meta {
    display: flex;
    justify-content: space-between;
    align-items: center;
    flex-wrap: wrap;
    gap: $gap-sm;
  }

  .tags { display: flex; gap: $gap-xs; flex-wrap: wrap; }

  .stats {
    display: flex;
    gap: $gap-md;
    color: $text-secondary;
    font-size: 12px;
  }
}
</style>
