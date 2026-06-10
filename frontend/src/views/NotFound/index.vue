<template>
  <DefaultLayout>
    <div class="page-container not-found-page">
      <div class="not-found-content animate-in">
        <div class="error-code">404</div>
        <h2>页面未找到</h2>
        <p>你访问的页面不存在或已被移除</p>
        <div class="error-decoration">
          <div class="deco-line"></div>
          <div class="deco-diamond"></div>
          <div class="deco-line"></div>
        </div>
        <div class="not-found-actions">
          <n-button type="primary" size="large" @click="$router.push('/')">
            <template #icon><n-icon><HomeOutline /></n-icon></template>
            回到首页
          </n-button>
          <n-button size="large" @click="$router.push('/blog')">
            <template #icon><n-icon><BookOutline /></n-icon></template>
            浏览博客
          </n-button>
        </div>
      </div>
    </div>
  </DefaultLayout>
</template>

<script setup lang="ts">
import { NButton, NIcon } from 'naive-ui'
import { HomeOutline, BookOutline } from '@vicons/ionicons5'
import DefaultLayout from '@/layouts/DefaultLayout.vue'
</script>

<style scoped lang="scss">
.not-found-page {
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: calc(100vh - 160px);
  padding-top: 80px;
}

.not-found-content {
  text-align: center;
}

.error-code {
  font-size: 120px;
  font-weight: 900;
  font-family: $font-mono;
  color: transparent;
  -webkit-text-stroke: 2px $accent;
  line-height: 1;
  margin-bottom: $gap-md;
  opacity: 0.6;
}

h2 {
  font-size: 24px;
  margin-bottom: $gap-sm;
}

p {
  color: $text-secondary;
  font-size: 14px;
  margin-bottom: $gap-xl;
}

.error-decoration {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: $gap-md;
  margin-bottom: $gap-xl;

  .deco-line {
    width: 60px;
    height: 1px;
    background: rgba($accent, 0.3);
  }

  .deco-diamond {
    width: 8px;
    height: 8px;
    border: 1px solid rgba($accent, 0.4);
    transform: rotate(45deg);
  }
}

.not-found-actions {
  display: flex;
  gap: $gap-md;
  justify-content: center;
  flex-wrap: wrap;
}
</style>
