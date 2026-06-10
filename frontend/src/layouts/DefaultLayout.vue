<template>
  <div class="default-layout">
    <Background />
    <Navbar />
    <div class="content-layer">
      <slot />
    </div>
    <Footer />
  </div>
</template>

<script setup lang="ts">
import Navbar from '@/components/Navbar/index.vue'
import Footer from '@/components/Footer/index.vue'
import Background from '@/components/Background/index.vue'
</script>

<style scoped>
.default-layout { min-height: 100vh; }
</style>
