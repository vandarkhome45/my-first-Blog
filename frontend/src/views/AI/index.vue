<template>
  <DefaultLayout>
    <div class="page-container ai-page">
      <div class="ai-card card-style animate-in">
        <div class="ai-icon">
          <n-icon size="64"><HardwareChipOutline /></n-icon>
        </div>
        <h2>AI 功能</h2>
        <p class="ai-desc">
          AI 功能正在开发中，敬请期待。未来将支持智能文章摘要、代码解释、
          学习路径推荐等功能。
        </p>
        <div class="ai-features">
          <div class="feature-chip">
            <n-icon size="18"><SparklesOutline /></n-icon>
            <span>智能摘要</span>
          </div>
          <div class="feature-chip">
            <n-icon size="18"><CodeSlashOutline /></n-icon>
            <span>代码解释</span>
          </div>
          <div class="feature-chip">
            <n-icon size="18"><NavigateOutline /></n-icon>
            <span>学习推荐</span>
          </div>
        </div>
        <div class="ai-progress">
          <div class="progress-dot"></div>
          <div class="progress-line"></div>
          <div class="progress-dot active"></div>
          <div class="progress-line dim"></div>
          <div class="progress-dot"></div>
        </div>
        <p class="ai-status">开发进度：规划中</p>
      </div>
    </div>
  </DefaultLayout>
</template>

<script setup lang="ts">
import { NIcon } from 'naive-ui'
import { HardwareChipOutline, SparklesOutline, CodeSlashOutline, NavigateOutline } from '@vicons/ionicons5'
import DefaultLayout from '@/layouts/DefaultLayout.vue'
</script>

<style scoped lang="scss">
.ai-page {
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: calc(100vh - 160px);
  padding-top: 80px;
}

.ai-card {
  text-align: center;
  max-width: 500px;
  width: 100%;
  padding: $gap-2xl;
}

.ai-icon {
  color: $accent;
  margin-bottom: $gap-lg;
  opacity: 0.8;
}

h2 {
  font-size: 24px;
  margin-bottom: $gap-md;
}

.ai-desc {
  color: $text-secondary;
  font-size: 14px;
  line-height: 1.8;
  margin-bottom: $gap-xl;
}

.ai-features {
  display: flex;
  justify-content: center;
  gap: $gap-md;
  flex-wrap: wrap;
  margin-bottom: $gap-xl;
}

.feature-chip {
  display: flex;
  align-items: center;
  gap: $gap-sm;
  padding: $gap-sm $gap-md;
  background: rgba($accent, 0.08);
  border: 1px solid rgba($accent, 0.15);
  border-radius: $radius-lg;
  color: $text-secondary;
  font-size: 13px;
}

.ai-progress {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 0;
  margin-bottom: $gap-md;
}

.progress-dot {
  width: 12px;
  height: 12px;
  border-radius: 50%;
  border: 2px solid $border-color;
  background: $bg-primary;

  &.active {
    border-color: $accent;
    background: $accent;
    box-shadow: 0 0 10px rgba($accent, 0.4);
  }
}

.progress-line {
  width: 60px;
  height: 2px;
  background: $accent;

  &.dim {
    background: $border-color;
  }
}

.ai-status {
  font-size: 12px;
  color: $text-secondary;
  font-family: $font-mono;
}
</style>
