import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    { path: '/', name: 'Home', component: () => import('@/views/Home/index.vue') },
    { path: '/blog', name: 'Blog', component: () => import('@/views/Blog/index.vue') },
    { path: '/blog/:id', name: 'Article', component: () => import('@/views/Article/index.vue') },
    { path: '/projects', name: 'Projects', component: () => import('@/views/Projects/index.vue') },
    { path: '/projects/:id', name: 'ProjectDetail', component: () => import('@/views/Projects/detail.vue') },
    { path: '/about', name: 'About', component: () => import('@/views/About/index.vue') },
    { path: '/login', name: 'Login', component: () => import('@/views/Login/index.vue') },
    { path: '/register', name: 'Register', component: () => import('@/views/Register/index.vue') },
    { path: '/profile', name: 'Profile', component: () => import('@/views/Profile/index.vue'), meta: { requiresAuth: true } },
    { path: '/ai', name: 'AI', component: () => import('@/views/AI/index.vue') },
    { path: '/admin', name: 'Admin', component: () => import('@/views/Admin/index.vue'), meta: { requiresAuth: true } },
    { path: '/admin/profile', name: 'AdminProfile', component: () => import('@/views/Admin/ProfileEdit.vue'), meta: { requiresAuth: true } },
    { path: '/admin/articles', name: 'AdminArticles', component: () => import('@/views/Admin/Articles.vue'), meta: { requiresAuth: true } },
    { path: '/admin/articles/:id', name: 'AdminArticleEdit', component: () => import('@/views/Admin/ArticleEdit.vue'), meta: { requiresAuth: true } },
    { path: '/admin/projects', name: 'AdminProjects', component: () => import('@/views/Admin/Projects.vue'), meta: { requiresAuth: true } },
    { path: '/admin/projects/:id', name: 'AdminProjectEdit', component: () => import('@/views/Admin/ProjectEdit.vue'), meta: { requiresAuth: true } },
    { path: '/admin/skills', name: 'AdminSkills', component: () => import('@/views/Admin/Skills.vue'), meta: { requiresAuth: true } },
    { path: '/admin/timeline', name: 'AdminTimeline', component: () => import('@/views/Admin/Timeline.vue'), meta: { requiresAuth: true } },
    { path: '/:pathMatch(.*)*', name: 'NotFound', component: () => import('@/views/NotFound/index.vue') }
  ],
  scrollBehavior() {
    return { top: 0 }
  }
})

router.beforeEach((to) => {
  if (to.meta.requiresAuth && !localStorage.getItem('token')) {
    return '/login'
  }
})

export default router
