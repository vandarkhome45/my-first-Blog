<template>
  <div class="markdown-viewer">
    <div class="article-content" v-html="renderedHtml"></div>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import MarkdownIt from 'markdown-it'
import hljs from 'highlight.js'

const props = defineProps<{ content: string }>()

const md = new MarkdownIt({
  html: true,
  linkify: true,
  typographer: true,
  highlight(str: string, lang: string) {
    if (lang && hljs.getLanguage(lang)) {
      try {
        return `<pre class="hljs"><code>${hljs.highlight(str, { language: lang, ignoreIllegals: true }).value}</code></pre>`
      } catch {}
    }
    return `<pre class="hljs"><code>${md.utils.escapeHtml(str)}</code></pre>`
  }
})

const renderedHtml = computed(() => md.render(props.content || ''))
</script>

<style lang="scss">
// highlight.js 暗色主题
@import 'highlight.js/scss/atom-one-dark.scss';

.article-content {
  font-size: 16px;
  line-height: 1.9;
  color: $text-primary;

  h1 { font-size: 28px; margin: $gap-xl 0 $gap-md; border-bottom: 1px solid $border-color; padding-bottom: $gap-sm; }
  h2 { font-size: 22px; margin: $gap-lg 0 $gap-md; }
  h3 { font-size: 18px; margin: $gap-md 0 $gap-sm; }

  p { margin-bottom: $gap-md; }

  pre.hljs {
    background: rgba(0,0,0,0.4);
    border: 1px solid $border-color;
    border-radius: $radius-md;
    padding: $gap-md;
    overflow-x: auto;
    margin: $gap-md 0;
    font-family: $font-mono;
    font-size: 14px;
  }

  code:not(.hljs code) {
    background: rgba($accent, 0.1);
    color: $accent;
    padding: 2px 6px;
    border-radius: 3px;
    font-size: 14px;
    font-family: $font-mono;
  }

  blockquote {
    border-left: 3px solid $accent;
    padding-left: $gap-md;
    margin: $gap-md 0;
    color: $text-secondary;
    font-style: italic;
  }

  table {
    width: 100%;
    border-collapse: collapse;
    margin: $gap-md 0;
    th, td { border: 1px solid $border-color; padding: 8px 12px; text-align: left; }
    th { background: rgba($accent, 0.05); }
  }

  img { max-width: 100%; border-radius: $radius-md; margin: $gap-md 0; }

  ul, ol { padding-left: 24px; margin-bottom: $gap-md; }
  li { margin-bottom: $gap-xs; }

  a { color: $accent; }
}
</style>
