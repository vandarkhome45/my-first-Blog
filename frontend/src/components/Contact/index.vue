<template>
  <div class="contact">
    <a :href="`mailto:${profile.email}`" class="contact-item">
      <n-icon size="20"><MailOutline /></n-icon>
      <span>Email</span>
    </a>
    <a :href="profile.github" target="_blank" class="contact-item">
      <n-icon size="20"><LogoGithub /></n-icon>
      <span>GitHub</span>
    </a>
    <div class="contact-item">
      <n-icon size="20"><ChatbubbleOutline /></n-icon>
      <span>QQ: {{ profile.qq }}</span>
    </div>
    <div class="contact-item">
      <n-icon size="20"><PeopleOutline /></n-icon>
      <span>微信: {{ profile.wechat }}</span>
    </div>
  </div>
</template>

<script setup lang="ts">
import { NIcon } from 'naive-ui'
import { MailOutline, LogoGithub, ChatbubbleOutline, PeopleOutline } from '@vicons/ionicons5'
import { useDataStore } from '@/stores/data'

const { profile } = useDataStore()
</script>

<style scoped lang="scss">
.contact {
  display: flex;
  gap: $gap-lg;
  justify-content: center;
  flex-wrap: wrap;

  &-item {
    display: flex;
    align-items: center;
    gap: $gap-sm;
    padding: $gap-sm $gap-md;
    color: $text-secondary;
    font-size: 14px;
    transition: color $transition;

    &:hover { color: $accent; }
  }
}
</style>
