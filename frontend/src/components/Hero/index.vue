<template>
  <section class="hero">
    <div class="hero-content">
      <div class="avatar-ring">
        <div class="avatar-placeholder">{{ profile.name[0] }}</div>
      </div>
      <h1 class="hero-name">{{ profile.name }}</h1>
      <p class="hero-title">{{ profile.title }}</p>
      <p class="hero-bio">{{ profile.bio }}</p>
      <div class="hero-actions">
        <n-button type="primary" size="large" @click="$router.push('/projects')">
          <template #icon><n-icon><CodeOutline /></n-icon></template>
          查看项目
        </n-button>
        <n-button size="large" @click="$router.push('/blog')" class="btn-secondary">
          <template #icon><n-icon><BookOutline /></n-icon></template>
          进入博客
        </n-button>
      </div>
    </div>
    <div class="hero-geometry">
      <div class="geo-line l1"></div>
      <div class="geo-line l2"></div>
      <div class="geo-line l3"></div>
      <div class="geo-diamond"></div>
    </div>
  </section>
</template>

<script setup lang="ts">
import { NButton, NIcon } from 'naive-ui'
import { CodeOutline, BookOutline } from '@vicons/ionicons5'
import { useDataStore } from '@/stores/data'

const { profile } = useDataStore()
</script>

<style scoped lang="scss">
.hero {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
  overflow: hidden;
  padding: 120px $gap-lg $gap-3xl;

  &-content {
    text-align: center;
    z-index: 2;
    animation: fadeInUp 0.8s $transition both;
  }

  &-name {
    font-size: 48px;
    font-weight: 800;
    letter-spacing: 4px;
    margin: $gap-lg 0 $gap-sm;
  }

  &-title {
    @include glow-text;
    font-size: 18px;
    font-family: $font-mono;
    margin-bottom: $gap-md;
  }

  &-bio {
    color: $text-secondary;
    font-size: 15px;
    max-width: 500px;
    margin: 0 auto $gap-xl;
    line-height: 1.8;
  }

  &-actions {
    display: flex;
    gap: $gap-md;
    justify-content: center;
    flex-wrap: wrap;

    .btn-secondary {
      border-color: rgba($accent, 0.3) !important;
      color: $accent !important;
    }
  }
}

.avatar-ring {
  width: 120px;
  height: 120px;
  margin: 0 auto;
  border-radius: 50%;
  border: 2px solid $accent;
  box-shadow: 0 0 30px rgba($accent, 0.2);
  display: flex;
  align-items: center;
  justify-content: center;

  .avatar-placeholder {
    width: 100px;
    height: 100px;
    border-radius: 50%;
    background: $bg-card;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 40px;
    font-weight: 700;
    color: $accent;
  }
}

// 几何装饰
.hero-geometry {
  position: absolute;
  inset: 0;
  pointer-events: none;
  z-index: 1;

  .geo-line {
    position: absolute;
    background: rgba($accent, 0.08);
  }
  .l1 { top: 20%; left: 10%; width: 200px; height: 1px; transform: rotate(-30deg); }
  .l2 { top: 60%; right: 15%; width: 300px; height: 1px; transform: rotate(15deg); }
  .l3 { bottom: 30%; left: 20%; width: 150px; height: 1px; transform: rotate(45deg); }

  .geo-diamond {
    position: absolute;
    top: 25%;
    right: 20%;
    width: 60px;
    height: 60px;
    border: 1px solid rgba($accent, 0.15);
    transform: rotate(45deg);
  }
}
</style>
