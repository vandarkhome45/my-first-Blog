<template>
  <DefaultLayout>
    <!-- Hero 首屏 -->
    <div @contextmenu.prevent="loggedIn && ctxMenu.show($event, [{ label: '编辑个人信息', action: () => $router.push('/admin/profile') }])">
      <Hero />
    </div>

    <!-- 技能标签云 -->
    <section class="section home-skills">
      <div class="container" @contextmenu.prevent="loggedIn && ctxMenu.show($event, [{ label: '管理技能列表', action: () => $router.push('/admin/skills') }])">
        <h2 class="section-title">技术栈</h2>
        <SkillCloud :skills="skillList" />
      </div>
    </section>

    <!-- 精选项目 -->
    <section class="section home-projects">
      <div class="container">
        <div class="section-header">
          <h2 class="section-title">精选项目</h2>
          <n-button text @click="$router.push('/projects')">查看全部 →</n-button>
        </div>
        <div class="project-grid">
          <div
            v-for="p in featuredProjects"
            :key="p.id"
            @contextmenu.prevent="loggedIn && ctxMenu.show($event, [
              { label: '编辑项目', action: () => $router.push(`/admin/projects/${p.id}`) },
              { label: '删除项目', action: () => handleDeleteProject(p.id), danger: true }
            ])"
          >
            <ProjectCard :project="p" />
          </div>
        </div>
      </div>
    </section>

    <!-- 最新文章 -->
    <section class="section home-articles">
      <div class="container">
        <div class="section-header">
          <h2 class="section-title">最新文章</h2>
          <n-button text @click="$router.push('/blog')">查看全部 →</n-button>
        </div>
        <div class="article-grid">
          <div
            v-for="a in latestArticles"
            :key="a.id"
            @contextmenu.prevent="loggedIn && ctxMenu.show($event, [
              { label: '编辑文章', action: () => $router.push(`/admin/articles/${a.id}`) },
              { label: '删除文章', action: () => handleDeleteArticle(a.id), danger: true }
            ])"
          >
            <ArticleCard :article="a" />
          </div>
        </div>
      </div>
    </section>

    <!-- 时间线 -->
    <section class="section home-timeline">
      <div class="container" @contextmenu.prevent="loggedIn && ctxMenu.show($event, [{ label: '管理时间线', action: () => $router.push('/admin/timeline') }])">
        <h2 class="section-title" style="display:block;text-align:center">学习经历</h2>
        <Timeline :items="timelineItems" />
      </div>
    </section>

    <!-- 联系方式 -->
    <section class="section home-contact">
      <div class="container" @contextmenu.prevent="loggedIn && ctxMenu.show($event, [{ label: '编辑联系方式', action: () => $router.push('/admin/profile') }])">
        <h2 class="section-title" style="display:block;text-align:center">联系我</h2>
        <Contact />
      </div>
    </section>
  </DefaultLayout>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { NButton, useMessage } from 'naive-ui'
import DefaultLayout from '@/layouts/DefaultLayout.vue'
import Hero from '@/components/Hero/index.vue'
import SkillCloud from '@/components/SkillCloud/index.vue'
import ProjectCard from '@/components/ProjectCard/index.vue'
import ArticleCard from '@/components/ArticleCard/index.vue'
import Timeline from '@/components/Timeline/index.vue'
import Contact from '@/components/Contact/index.vue'
import { getSkills, getProjects, getArticles, getTimeline } from '@/api'
import { useContextMenu } from '@/composables/useContextMenu'
import { useAuthStore } from '@/stores/auth'
import { useDataStore } from '@/stores/data'
import type { Skill, Project, Article, TimelineItem } from '@/types'

const ctxMenu = useContextMenu()
const auth = useAuthStore()
const dataStore = useDataStore()
const message = useMessage()
const loggedIn = auth.isLoggedIn()

const skillList = ref<Skill[]>([])
const featuredProjects = ref<Project[]>([])
const latestArticles = ref<Article[]>([])
const timelineItems = ref<TimelineItem[]>([])

function handleDeleteProject(id: number) {
  dataStore.removeProject(id)
  featuredProjects.value = featuredProjects.value.filter(p => p.id !== id)
  message.success('项目已删除')
}

function handleDeleteArticle(id: number) {
  dataStore.removeArticle(id)
  latestArticles.value = latestArticles.value.filter(a => a.id !== id)
  message.success('文章已删除')
}

onMounted(async () => {
  const [skillsRes, projectsRes, articlesRes, timelineRes] = await Promise.all([
    getSkills(),
    getProjects(),
    getArticles({ page: 0, size: 6 }),
    getTimeline()
  ])
  skillList.value = skillsRes.data
  featuredProjects.value = (projectsRes.data as Project[]).filter(p => p.featured)
  latestArticles.value = (articlesRes.data as any).list || articlesRes.data
  timelineItems.value = timelineRes.data
})
</script>

<style scoped lang="scss">
.container {
  @include container;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: $gap-xl;
  flex-wrap: wrap;
  gap: $gap-md;
}

.project-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(320px, 1fr));
  gap: $gap-lg;
}

.article-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(340px, 1fr));
  gap: $gap-lg;
}

.home-skills {
  padding-top: $gap-xl;
}

.home-contact {
  padding-bottom: $gap-3xl;
}
</style>
