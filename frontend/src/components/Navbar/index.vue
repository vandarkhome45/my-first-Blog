<template>
  <nav class="navbar" :class="{ scrolled: isScrolled }">
    <div class="navbar-inner">
      <router-link to="/" class="logo">G<span class="accent">A</span>O</router-link>
      <div class="nav-links">
        <router-link v-for="item in navItems" :key="item.path" :to="item.path" :class="{ active: $route.path === item.path }">
          {{ item.label }}
        </router-link>
      </div>
      <div class="nav-actions">
        <router-link v-if="auth.isLoggedIn()" to="/admin" class="admin-link" :class="{ active: $route.path.startsWith('/admin') }">
          管理
        </router-link>
        <n-button text @click="$router.push('/login')" v-if="!auth.isLoggedIn()">登录</n-button>
        <n-button text @click="$router.push('/profile')" v-else>个人中心</n-button>
      </div>
    </div>
  </nav>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted } from 'vue'
import { NButton } from 'naive-ui'
import { useAuthStore } from '@/stores/auth'

const auth = useAuthStore()
const isScrolled = ref(false)

const navItems = [
  { label: '首页', path: '/' },
  { label: '博客', path: '/blog' },
  { label: '项目', path: '/projects' },
  { label: '关于', path: '/about' },
  { label: 'AI', path: '/ai' }
]

function onScroll() {
  isScrolled.value = window.scrollY > 50
}

onMounted(() => window.addEventListener('scroll', onScroll))
onUnmounted(() => window.removeEventListener('scroll', onScroll))
</script>

<style scoped lang="scss">
.navbar {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 1000;
  height: 64px;
  transition: all $transition;
  background: transparent;

  &.scrolled {
    @include glass;
    border-bottom: 1px solid $border-color;
  }

  &-inner {
    max-width: 1200px;
    margin: 0 auto;
    padding: 0 $gap-lg;
    height: 100%;
    display: flex;
    align-items: center;
    justify-content: space-between;
  }
}

.logo {
  font-size: 24px;
  font-weight: 800;
  color: $text-primary;
  letter-spacing: 2px;

  .accent { color: $accent; }
}

.nav-links {
  display: flex;
  gap: $gap-xl;

  a {
    font-size: 14px;
    color: $text-secondary;
    padding: 4px 0;
    position: relative;

    &::after {
      content: '';
      position: absolute;
      bottom: -2px;
      left: 0;
      width: 0;
      height: 2px;
      background: $accent;
      transition: width $transition;
    }

    &:hover, &.active {
      color: $text-primary;
      &::after { width: 100%; }
    }
  }
}

.nav-actions {
  display: flex;
  align-items: center;
  gap: $gap-sm;
}

.admin-link {
  font-size: 13px;
  color: $text-secondary;
  padding: 4px 10px;
  border: 1px solid $border-color;
  border-radius: $radius-sm;
  transition: all $transition;

  &:hover, &.active {
    color: $accent;
    border-color: $accent;
  }
}
</style>
