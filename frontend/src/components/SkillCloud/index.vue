<template>
  <div class="skill-cloud">
    <span
      v-for="skill in skills"
      :key="skill.name"
      class="skill-tag"
      :style="{ fontSize: 12 + skill.level / 10 + 'px' }"
    >{{ skill.name }}</span>
  </div>
</template>

<script setup lang="ts">
import type { Skill } from '@/types'
defineProps<{ skills: Skill[] }>()
</script>

<style scoped lang="scss">
.skill-cloud {
  display: flex;
  flex-wrap: wrap;
  gap: $gap-sm $gap-md;
  justify-content: center;

  .skill-tag {
    padding: 4px 14px;
    background: $bg-card;
    border: 1px solid $border-color;
    color: $text-secondary;
    font-family: $font-mono;
    cursor: default;
    transition: all $transition;

    &:hover {
      color: $accent;
      border-color: $accent;
      transform: translateY(-2px);
    }
  }
}
</style>
