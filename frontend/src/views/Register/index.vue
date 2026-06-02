<template>
  <DefaultLayout>
    <div class="page-container auth-page">
      <div class="auth-card card-style animate-in">
        <h2>注册</h2>
        <p class="auth-subtitle">创建账号，享受完整功能</p>

        <n-form ref="formRef" :model="form" :rules="rules" class="auth-form">
          <n-form-item path="username" label="用户名">
            <n-input v-model:value="form.username" placeholder="请输入用户名" size="large" />
          </n-form-item>
          <n-form-item path="email" label="邮箱">
            <n-input v-model:value="form.email" placeholder="请输入邮箱" size="large" />
          </n-form-item>
          <n-form-item path="password" label="密码">
            <n-input
              v-model:value="form.password"
              type="password"
              placeholder="请设置密码（至少6位）"
              show-password-on="click"
              size="large"
            />
          </n-form-item>
          <n-form-item path="confirmPassword" label="确认密码">
            <n-input
              v-model:value="form.confirmPassword"
              type="password"
              placeholder="请再次输入密码"
              show-password-on="click"
              size="large"
              @keyup.enter="handleRegister"
            />
          </n-form-item>
        </n-form>

        <n-button
          type="primary"
          block
          size="large"
          :loading="submitting"
          @click="handleRegister"
        >
          注册
        </n-button>

        <p class="auth-switch">
          已有账号？
          <router-link to="/login">立即登录</router-link>
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
import { register } from '@/api'

const router = useRouter()
const message = useMessage()
const auth = useAuthStore()
const formRef = ref()
const submitting = ref(false)

const form = reactive({
  username: '',
  email: '',
  password: '',
  confirmPassword: ''
})

const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请设置密码', trigger: 'blur' },
    { min: 6, message: '密码至少需要6位', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请确认密码', trigger: 'blur' },
    {
      validator: (_: any, value: string) => value === form.password,
      message: '两次密码输入不一致',
      trigger: 'blur'
    }
  ]
}

async function handleRegister() {
  const valid = await formRef.value?.validate().catch(() => false)
  if (!valid) return
  submitting.value = true
  try {
    await register({ username: form.username, password: form.password, email: form.email })
    message.success('注册成功，请登录')
    router.push('/login')
  } catch {
    message.error('注册失败，请稍后重试')
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
