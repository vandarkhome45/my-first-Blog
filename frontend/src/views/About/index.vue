<template>
  <DefaultLayout>
    <div class="page-container about-page">
      <!-- 个人介绍 -->
      <section class="about-intro animate-in" @contextmenu.prevent="loggedIn && ctxMenu.show($event, [{ label: '编辑个人信息', action: () => $router.push('/admin/profile') }])">
        <div class="intro-card card-style">
          <div class="intro-avatar">
            <div class="avatar-circle">{{ profile.name[0] }}</div>
          </div>
          <div class="intro-text">
            <h1>{{ profile.name }}</h1>
            <p class="intro-title">{{ profile.title }}</p>
            <p class="intro-bio">{{ profile.bio }}</p>
            <div class="intro-info">
              <div class="info-item">
                <n-icon size="16"><SchoolOutline /></n-icon>
                <span>{{ profile.school }} · {{ profile.major }}</span>
              </div>
              <div class="info-item">
                <n-icon size="16"><MailOutline /></n-icon>
                <span>{{ profile.email }}</span>
              </div>
            </div>
          </div>
        </div>
      </section>

      <!-- 技能掌握度 -->
      <section class="about-skills animate-in" @contextmenu.prevent="loggedIn && ctxMenu.show($event, [{ label: '管理技能列表', action: () => $router.push('/admin/skills') }])">
        <h2 class="section-title">技能掌握度</h2>
        <div class="skills-bars">
          <div v-for="skill in skillList" :key="skill.name" class="skill-bar">
            <div class="skill-bar-header">
              <span class="skill-name">{{ skill.name }}</span>
              <span class="skill-level">{{ skill.level }}%</span>
            </div>
            <div class="bar-track">
              <div class="bar-fill" :style="{ width: skill.level + '%' }"></div>
            </div>
          </div>
        </div>
      </section>

      <!-- 学习路线 -->
      <section class="about-roadmap animate-in">
        <h2 class="section-title">学习路线</h2>
        <div class="roadmap">
          <div v-for="(step, i) in roadmap" :key="i" class="roadmap-step card-style">
            <div class="step-number">{{ i + 1 }}</div>
            <div class="step-content">
              <h4>{{ step.title }}</h4>
              <p>{{ step.description }}</p>
              <div class="step-techs">
                <span class="tag" v-for="t in step.techs" :key="t">{{ t }}</span>
              </div>
            </div>
            <div v-if="i < roadmap.length - 1" class="step-connector">
              <n-icon size="20"><ChevronDownOutline /></n-icon>
            </div>
          </div>
        </div>
      </section>

      <!-- 经历时间线 -->
      <section class="about-timeline animate-in" @contextmenu.prevent="loggedIn && ctxMenu.show($event, [{ label: '管理时间线', action: () => $router.push('/admin/timeline') }])">
        <h2 class="section-title" style="display:block;text-align:center">成长历程</h2>
        <Timeline :items="timelineItems" />
      </section>

      <!-- 联系方式 -->
      <section class="about-contact animate-in" @contextmenu.prevent="loggedIn && ctxMenu.show($event, [{ label: '编辑联系方式', action: () => $router.push('/admin/profile') }])">
        <h2 class="section-title" style="display:block;text-align:center">联系我</h2>
        <Contact />
      </section>
    </div>
  </DefaultLayout>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { NIcon } from 'naive-ui'
import { SchoolOutline, MailOutline, ChevronDownOutline } from '@vicons/ionicons5'
import DefaultLayout from '@/layouts/DefaultLayout.vue'
import Timeline from '@/components/Timeline/index.vue'
import Contact from '@/components/Contact/index.vue'
import { getSkills, getTimeline, getProfile } from '@/api'
import { useContextMenu } from '@/composables/useContextMenu'
import { useAuthStore } from '@/stores/auth'
import type { Skill, TimelineItem, Profile } from '@/types'

const ctxMenu = useContextMenu()
const auth = useAuthStore()
const loggedIn = auth.isLoggedIn()

const profile = ref<Profile>({
  name: 'GAO',
  avatar: '',
  title: '',
  school: '',
  major: '',
  bio: '',
  email: '',
  github: '',
  qq: '',
  wechat: ''
})
const skillList = ref<Skill[]>([])
const timelineItems = ref<TimelineItem[]>([])

const roadmap = [
  {
    title: 'C 语言基础',
    description: '掌握指针、内存管理、数据结构等核心概念，为系统编程打下坚实基础',
    techs: ['C', '指针', '内存管理']
  },
  {
    title: '数据结构与算法',
    description: '系统学习常用数据结构和经典算法，辅以 LeetCode 刷题训练',
    techs: ['数据结构', '算法', 'LeetCode']
  },
  {
    title: 'Java 后端开发',
    description: '学习 Spring Boot 框架，掌握 Web 开发、数据库操作、接口设计',
    techs: ['Java', 'Spring Boot', 'MySQL']
  },
  {
    title: 'Linux 系统编程',
    description: '深入 Linux 操作系统，学习系统调用、进程线程、网络编程',
    techs: ['Linux', 'Shell', '系统编程']
  },
  {
    title: '嵌入式开发',
    description: '从 STM32 裸机开发到 RTOS 实时操作系统，逐步深入嵌入式领域',
    techs: ['STM32', 'RT-Thread', 'FreeRTOS']
  },
  {
    title: '全栈项目实战',
    description: '独立完成个人博客项目，整合前后端技术栈，积累完整项目经验',
    techs: ['Vue 3', 'TypeScript', 'Spring Boot']
  }
]

onMounted(async () => {
  const [profileRes, skillsRes, timelineRes] = await Promise.all([
    getProfile(),
    getSkills(),
    getTimeline()
  ])
  profile.value = profileRes.data
  skillList.value = skillsRes.data
  timelineItems.value = timelineRes.data
})
</script>

<style scoped lang="scss">
.about-page {
  max-width: 900px;
  margin: 0 auto;
}

// Intro
.about-intro {
  margin-bottom: $gap-3xl;
}

.intro-card {
  display: flex;
  gap: $gap-xl;
  align-items: flex-start;
  padding: $gap-xl;
}

.intro-avatar {
  flex-shrink: 0;

  .avatar-circle {
    width: 100px;
    height: 100px;
    border-radius: 50%;
    background: $bg-card;
    border: 2px solid $accent;
    box-shadow: 0 0 30px rgba($accent, 0.2);
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 36px;
    font-weight: 700;
    color: $accent;
  }
}

.intro-text {
  flex: 1;

  h1 {
    font-size: 28px;
    margin-bottom: $gap-xs;
  }

  .intro-title {
    @include glow-text;
    font-size: 14px;
    font-family: $font-mono;
    margin-bottom: $gap-md;
  }

  .intro-bio {
    color: $text-secondary;
    font-size: 14px;
    line-height: 1.8;
    margin-bottom: $gap-lg;
  }
}

.intro-info {
  display: flex;
  flex-wrap: wrap;
  gap: $gap-md;

  .info-item {
    display: flex;
    align-items: center;
    gap: $gap-sm;
    font-size: 13px;
    color: $text-secondary;
  }
}

// Skills bars
.about-skills {
  margin-bottom: $gap-3xl;
}

.skills-bars {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(380px, 1fr));
  gap: $gap-md $gap-xl;
}

.skill-bar-header {
  display: flex;
  justify-content: space-between;
  margin-bottom: $gap-xs;
  font-size: 13px;

  .skill-name { font-family: $font-mono; color: $text-primary; }
  .skill-level { color: $accent; }
}

.bar-track {
  height: 6px;
  background: rgba(255, 255, 255, 0.05);
  border-radius: 3px;
  overflow: hidden;
}

.bar-fill {
  height: 100%;
  background: linear-gradient(90deg, $accent, $accent-hover);
  border-radius: 3px;
  transition: width 1s $transition;
}

// Roadmap
.about-roadmap {
  margin-bottom: $gap-3xl;
}

.roadmap {
  display: flex;
  flex-direction: column;
  gap: $gap-md;
}

.roadmap-step {
  display: flex;
  flex-direction: column;
  gap: $gap-md;
  position: relative;

  .step-number {
    width: 32px;
    height: 32px;
    border-radius: 50%;
    background: $accent;
    color: #fff;
    font-size: 14px;
    font-weight: 700;
    display: flex;
    align-items: center;
    justify-content: center;
    flex-shrink: 0;
  }

  .step-content {
    h4 { font-size: 16px; margin-bottom: $gap-xs; }
    p { font-size: 13px; color: $text-secondary; line-height: 1.7; margin-bottom: $gap-sm; }
  }

  .step-techs {
    display: flex;
    gap: $gap-xs;
    flex-wrap: wrap;
  }

  .step-connector {
    display: flex;
    justify-content: center;
    color: $accent;
    opacity: 0.4;
    position: absolute;
    bottom: -24px;
    left: 50%;
    transform: translateX(-50%);
  }
}

// Timeline & Contact sections
.about-timeline {
  margin-bottom: $gap-3xl;
}

.about-contact {
  margin-bottom: $gap-xl;
}

@media (max-width: 600px) {
  .intro-card {
    flex-direction: column;
    align-items: center;
    text-align: center;
  }

  .intro-info {
    justify-content: center;
  }

  .skills-bars {
    grid-template-columns: 1fr;
  }
}
</style>
