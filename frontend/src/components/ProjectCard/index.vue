<template>
  <div class="project-card card-style" @click="$router.push(`/projects/${project.id}`)">
    <div class="card-header">
      <span class="card-category">{{ categoryLabel }}</span>
      <n-icon v-if="project.githubUrl" size="18" @click.stop="openLink(project.githubUrl)">
        <LogoGithub />
      </n-icon>
    </div>
    <h3>{{ project.title }}</h3>
    <p class="desc">{{ project.description }}</p>
    <div class="tech-stack">
      <span class="tag" v-for="tech in project.techStack" :key="tech">{{ tech }}</span>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { NIcon } from 'naive-ui'
import { LogoGithub } from '@vicons/ionicons5'
import type { Project } from '@/types'

const props = defineProps<{ project: Project }>()

const categoryLabel = computed(() => {
  const map: Record<string, string> = { web: 'Web', embedded: '嵌入式', algorithm: '算法', ai: 'AI' }
  return map[props.project.category] || props.project.category
})

function openLink(url: string) {
  window.open(url, '_blank')
}
</script>

<style scoped lang="scss">
.project-card {
  cursor: pointer;

  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: $gap-sm;
  }

  .card-category {
    font-size: 11px;
    text-transform: uppercase;
    color: $accent;
    letter-spacing: 1px;
  }

  h3 { font-size: 18px; margin-bottom: $gap-sm; }

  .desc {
    color: $text-secondary;
    font-size: 14px;
    line-height: 1.7;
    margin-bottom: $gap-md;
  }

  .tech-stack {
    display: flex;
    gap: $gap-xs;
    flex-wrap: wrap;
  }
}
</style>
