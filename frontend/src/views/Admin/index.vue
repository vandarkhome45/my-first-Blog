<template>
  <DefaultLayout>
    <div class="page-container admin-page">
      <h1 class="page-title animate-in">管理后台</h1>
      <p class="page-desc animate-in">在这里管理你的个人博客与作品集</p>

      <div class="admin-grid animate-in">
        <router-link to="/admin/profile" class="admin-tile card-style">
          <div class="tile-icon">
            <n-icon size="28"><PersonCircleOutline /></n-icon>
          </div>
          <h3>个人信息</h3>
          <p>编辑姓名、简介、联系方式等基本资料</p>
        </router-link>

        <router-link to="/admin/articles" class="admin-tile card-style">
          <div class="tile-icon">
            <n-icon size="28"><DocumentTextOutline /></n-icon>
          </div>
          <h3>文章管理</h3>
          <p>新建、编辑、删除博客文章</p>
          <span class="tile-count">{{ store.articles.length }} 篇</span>
        </router-link>

        <router-link to="/admin/projects" class="admin-tile card-style">
          <div class="tile-icon">
            <n-icon size="28"><CodeSlashOutline /></n-icon>
          </div>
          <h3>项目管理</h3>
          <p>新建、编辑、删除项目展示</p>
          <span class="tile-count">{{ store.projects.length }} 个</span>
        </router-link>

        <router-link to="/admin/skills" class="admin-tile card-style">
          <div class="tile-icon">
            <n-icon size="28"><FlashOutline /></n-icon>
          </div>
          <h3>技能管理</h3>
          <p>管理技能列表与掌握程度</p>
          <span class="tile-count">{{ store.skills.length }} 项</span>
        </router-link>

        <router-link to="/admin/timeline" class="admin-tile card-style">
          <div class="tile-icon">
            <n-icon size="28"><TimeOutline /></n-icon>
          </div>
          <h3>时间线管理</h3>
          <p>添加、删除个人经历与里程碑</p>
          <span class="tile-count">{{ store.timeline.length }} 条</span>
        </router-link>
      </div>
    </div>
  </DefaultLayout>
</template>

<script setup lang="ts">
import { NIcon } from 'naive-ui'
import { PersonCircleOutline, DocumentTextOutline, CodeSlashOutline, FlashOutline, TimeOutline } from '@vicons/ionicons5'
import DefaultLayout from '@/layouts/DefaultLayout.vue'
import { useDataStore } from '@/stores/data'

const store = useDataStore()
</script>

<style scoped lang="scss">
.admin-page {
  max-width: 900px;
  margin: 0 auto;
}

.page-title {
  font-size: 28px;
  font-weight: 700;
  margin-bottom: $gap-xs;
}

.page-desc {
  color: $text-secondary;
  font-size: 14px;
  margin-bottom: $gap-xl;
}

.admin-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(220px, 1fr));
  gap: $gap-lg;
}

.admin-tile {
  position: relative;
  display: block;
  padding: $gap-lg;
  text-decoration: none;
  color: inherit;

  .tile-icon {
    color: $accent;
    margin-bottom: $gap-md;
  }

  h3 {
    font-size: 16px;
    margin-bottom: $gap-sm;
    color: $text-primary;
  }

  p {
    font-size: 12px;
    color: $text-secondary;
    line-height: 1.5;
  }

  .tile-count {
    position: absolute;
    top: $gap-md;
    right: $gap-md;
    font-size: 11px;
    font-family: $font-mono;
    color: $accent;
    background: rgba($accent, 0.1);
    padding: 2px 8px;
    border-radius: $radius-sm;
  }
}
</style>
