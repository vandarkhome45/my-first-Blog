<template>
  <footer class="footer">
    <div class="footer-inner">
      <div class="footer-brand">
        <span class="logo">G<span class="accent">A</span>O</span>
        <p>全栈开发 · 嵌入式 · 持续学习</p>
      </div>
      <div class="footer-links">
        <a :href="`mailto:${profile.email}`">Email</a>
        <a :href="profile.github" target="_blank">GitHub</a>
        <router-link to="/blog">博客</router-link>
        <router-link to="/projects">项目</router-link>
      </div>
    </div>
    <div class="footer-bottom">
      <span>© 2026 GAO. Built with Vue 3 + Spring Boot.</span>
    </div>
  </footer>
</template>

<script setup lang="ts">
import { useDataStore } from '@/stores/data'

const { profile } = useDataStore()
</script>

<style scoped lang="scss">
.footer {
  border-top: 1px solid $border-color;
  padding: $gap-2xl 0 $gap-lg;
  margin-top: $gap-3xl;

  &-inner {
    @include container;
    display: flex;
    justify-content: space-between;
    align-items: flex-start;
    flex-wrap: wrap;
    gap: $gap-xl;
  }

  &-brand {
    .logo {
      font-size: 20px;
      font-weight: 700;
      .accent { color: $accent; }
    }
    p { color: $text-secondary; font-size: 13px; margin-top: $gap-xs; }
  }

  &-links {
    display: flex;
    gap: $gap-lg;
    a { font-size: 14px; color: $text-secondary; }
  }

  &-bottom {
    @include container;
    margin-top: $gap-xl;
    padding-top: $gap-md;
    border-top: 1px solid $border-color;
    text-align: center;
    font-size: 12px;
    color: $text-secondary;
  }
}
</style>
