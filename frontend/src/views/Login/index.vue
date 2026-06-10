<template>
  <DefaultLayout>
    <div class="page-container auth-page">
      <div class="auth-card card-style animate-in">
        <h2>登录</h2>
        <p class="auth-subtitle">登录后可以管理文章和评论</p>

        <n-form ref="formRef" :model="form" :rules="rules" class="auth-form">
          <n-form-item path="username" label="用户名">
            <n-input v-model:value="form.username" placeholder="请输入用户名" size="large" />
          </n-form-item>
          <n-form-item path="password" label="密码">
            <n-input
              v-model:value="form.password"
              type="password"
              placeholder="请输入密码"
              show-password-on="click"
              size="large"
              @keyup.enter="handleLogin"
            />
          </n-form-item>
        </n-form>

        <n-button
          type="primary"
          block
          size="large"
          :loading="submitting"
          @click="handleLogin"
        >
          登录
        </n-button>

        <p class="auth-switch">
          还没有账号？
          <router-link to="/register">立即注册</router-link>
        </p>
      </div>
    </div>
  </DefaultLayout>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { NForm, NFormItem, NInput, NButton, useMessage } from 'naive-ui'
import DefaultLayout from '@/layouts/DefaultLayout.vue'
import { useAuthStore } from '@/stores/auth'
import { login } from '@/api'

const router = useRouter()
const message = useMessage()
const auth = useAuthStore()
const formRef = ref()
const submitting = ref(false)

const form = reactive({
  username: '',
  password: ''
})

const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}

async function handleLogin() {
  const valid = await formRef.value?.validate().catch(() => false)
  if (!valid) return
  submitting.value = true
  try {
    const res = await login(form.username, form.password)
    auth.setAuth(res.data.token, { username: form.username })
    message.success('登录成功')
    router.push('/')
  } catch {
    message.error('用户名或密码错误')
  } finally {
    submitting.value = false
  }
}
</script>

<style scoped lang="scss">
.auth-page {
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: calc(100vh - 160px);
  padding-top: 80px;
}

.auth-card {
  width: 100%;
  max-width: 420px;
  padding: $gap-2xl;

  h2 {
    font-size: 24px;
    margin-bottom: $gap-xs;
    text-align: center;
  }

  .auth-subtitle {
    color: $text-secondary;
    font-size: 14px;
    text-align: center;
    margin-bottom: $gap-xl;
  }
}

.auth-form {
  margin-bottom: $gap-lg;
}

.auth-switch {
  text-align: center;
  margin-top: $gap-lg;
  font-size: 13px;
  color: $text-secondary;

  a {
    color: $accent;
  }
}
</style>
