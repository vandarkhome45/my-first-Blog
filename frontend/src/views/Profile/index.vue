<template>
  <DefaultLayout>
    <div class="page-container profile-page">
      <div class="profile-card card-style animate-in">
        <div class="profile-header">
          <div class="avatar-large">
            <span>{{ auth.user?.username?.[0]?.toUpperCase() || 'U' }}</span>
          </div>
          <div class="profile-info">
            <h2>{{ auth.user?.username || '用户' }}</h2>
            <p class="profile-role">普通用户</p>
          </div>
        </div>

        <n-divider />

        <div class="profile-stats">
          <div class="stat-item">
            <span class="stat-value">0</span>
            <span class="stat-label">文章</span>
          </div>
          <div class="stat-item">
            <span class="stat-value">0</span>
            <span class="stat-label">评论</span>
          </div>
          <div class="stat-item">
            <span class="stat-value">0</span>
            <span class="stat-label">项目</span>
          </div>
        </div>

        <n-divider />

        <div class="profile-actions">
          <n-button type="error" block @click="handleLogout">
            <template #icon><n-icon><LogOutOutline /></n-icon></template>
            退出登录
          </n-button>
        </div>
      </div>
    </div>
  </DefaultLayout>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router'
import { NButton, NIcon, NDivider, useMessage } from 'naive-ui'
import { LogOutOutline } from '@vicons/ionicons5'
import DefaultLayout from '@/layouts/DefaultLayout.vue'
import { useAuthStore } from '@/stores/auth'

const router = useRouter()
const message = useMessage()
const auth = useAuthStore()

function handleLogout() {
  auth.logout()
  message.success('已退出登录')
  router.push('/')
}
</script>

<style scoped lang="scss">
.profile-page {
  display: flex;
  align-items: flex-start;
  justify-content: center;
  padding-top: 100px;
  min-height: calc(100vh - 160px);
}

.profile-card {
  width: 100%;
  max-width: 500px;
  padding: $gap-xl;
}

.profile-header {
  display: flex;
  align-items: center;
  gap: $gap-lg;
}

.avatar-large {
  width: 80px;
  height: 80px;
  border-radius: 50%;
  background: $bg-card;
  border: 2px solid $accent;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 32px;
  font-weight: 700;
  color: $accent;
}

.profile-info {
  h2 {
    font-size: 22px;
    margin-bottom: $gap-xs;
  }

  .profile-role {
    color: $text-secondary;
    font-size: 13px;
    font-family: $font-mono;
  }
}

.profile-stats {
  display: flex;
  justify-content: space-around;
  text-align: center;
}

.stat-item {
  display: flex;
  flex-direction: column;
  gap: $gap-xs;

  .stat-value {
    font-size: 28px;
    font-weight: 700;
    color: $accent;
    font-family: $font-mono;
  }

  .stat-label {
    font-size: 12px;
    color: $text-secondary;
  }
}
</style>
