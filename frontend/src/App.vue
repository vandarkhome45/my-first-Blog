<template>
  <n-config-provider :theme-overrides="themeOverrides">
    <n-message-provider>
      <router-view />
      <ContextMenu />
    </n-message-provider>
  </n-config-provider>
</template>

<script setup lang="ts">
import type { GlobalThemeOverrides } from 'naive-ui'
import { NConfigProvider, NMessageProvider } from 'naive-ui'
import ContextMenu from '@/components/ContextMenu/index.vue'

const themeOverrides: GlobalThemeOverrides = {
  common: {
    primaryColor: '#FF7A00',
    primaryColorHover: '#FF9540',
    bodyColor: '#0F1115',
    cardColor: '#171A21',
    textColorBase: '#FFFFFF',
    textColor2: '#A0A6B0',
    borderColor: 'rgba(255,255,255,0.08)',
    fontSize: '14px'
  }
}
</script>
