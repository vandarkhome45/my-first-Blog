<template>
  <Teleport to="body">
    <div
      v-if="ctx.visible.value"
      class="ctx-overlay"
      @click="ctx.hide()"
      @contextmenu.prevent="ctx.hide()"
      @wheel="ctx.hide()"
    />
    <div
      v-if="ctx.visible.value"
      class="ctx-menu"
      :style="{ left: menuX + 'px', top: menuY + 'px' }"
    >
      <div
        v-for="(item, i) in ctx.items.value"
        :key="i"
        class="ctx-item"
        :class="{ danger: item.danger }"
        @click="handleClick(item)"
      >
        <span class="ctx-label">{{ item.label }}</span>
        <span class="ctx-shortcut" v-if="item.icon">{{ item.icon }}</span>
      </div>
    </div>
  </Teleport>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useContextMenu } from '@/composables/useContextMenu'

const ctx = useContextMenu()

const menuX = computed(() => {
  // 确保菜单不超出右边界
  const maxX = window.innerWidth - 180
  return Math.min(ctx.x.value, maxX)
})

const menuY = computed(() => {
  // 确保菜单不超出下边界（估算菜单高度约200px）
  const menuHeight = ctx.items.value.length * 40 + 16
  const maxY = window.innerHeight - menuHeight
  return Math.min(ctx.y.value, maxY)
})

function handleClick(item: { action: () => void }) {
  ctx.hide()
  item.action()
}
</script>

<style scoped lang="scss">
.ctx-overlay {
  position: fixed;
  inset: 0;
  z-index: 9998;
}

.ctx-menu {
  position: fixed;
  z-index: 9999;
  min-width: 160px;
  padding: $gap-sm;
  background: #1e222b;
  border: 1px solid rgba($accent, 0.25);
  border-radius: $radius-md;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.5), 0 0 0 1px rgba(255, 255, 255, 0.04);
  backdrop-filter: blur(16px);
  animation: ctxFadeIn 0.12s ease-out;
}

.ctx-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 7px 12px;
  border-radius: $radius-sm;
  cursor: pointer;
  font-size: 13px;
  color: $text-primary;
  transition: all 0.12s;

  &:hover {
    background: rgba($accent, 0.12);
    color: $accent;
  }

  &.danger:hover {
    background: rgba(#e74c3c, 0.12);
    color: #e74c3c;
  }
}

.ctx-label {
  flex: 1;
}

.ctx-shortcut {
  font-size: 11px;
  color: $text-secondary;
  margin-left: $gap-md;
  font-family: $font-mono;
}

@keyframes ctxFadeIn {
  from {
    opacity: 0;
    transform: scale(0.96);
  }
  to {
    opacity: 1;
    transform: scale(1);
  }
}
</style>
