<template>
  <div class="timeline">
    <div class="line"></div>
    <div v-for="(item, i) in items" :key="i" class="timeline-item" :class="{ left: i % 2 === 0, right: i % 2 === 1 }">
      <div class="dot" :class="item.type"></div>
      <div class="content card-style">
        <span class="year">{{ item.year }}</span>
        <h4>{{ item.title }}</h4>
        <p>{{ item.description }}</p>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import type { TimelineItem } from '@/types'
defineProps<{ items: TimelineItem[] }>()
</script>

<style scoped lang="scss">
.timeline {
  position: relative;
  padding: $gap-xl 0;

  .line {
    position: absolute;
    left: 50%;
    transform: translateX(-50%);
    top: 0;
    bottom: 0;
    width: 2px;
    background: rgba($accent, 0.2);
  }

  .timeline-item {
    position: relative;
    width: 50%;
    padding: 0 $gap-xl $gap-xl;

    &.left { padding-right: $gap-2xl; }
    &.right { margin-left: 50%; padding-left: $gap-2xl; }

    .dot {
      position: absolute;
      top: 8px;
      width: 12px;
      height: 12px;
      border-radius: 50%;
      border: 2px solid $accent;
      background: $bg-primary;
      z-index: 1;

      .left & { right: -6px; }
      .right & { left: -6px; }
    }
  }

  .content {
    .year { @include glow-text; font-size: 13px; font-family: $font-mono; }
    h4 { font-size: 16px; margin: 4px 0; }
    p { color: $text-secondary; font-size: 13px; line-height: 1.6; }
  }

  @media (max-width: 768px) {
    .line { left: 20px; }
    .timeline-item {
      width: 100%;
      &.left, &.right { margin-left: 0; padding: 0 0 $gap-xl 40px; }
      .dot { left: 14px !important; }
    }
  }
}
</style>
