<template>
  <DefaultLayout>
    <div class="page-container admin-page">
      <div class="admin-header animate-in">
        <n-button text @click="$router.push('/admin')" class="back-btn">
          <template #icon><n-icon><ArrowBackOutline /></n-icon></template>
          管理后台
        </n-button>
        <h1>编辑个人信息</h1>
      </div>

      <div class="admin-form card-style animate-in">
        <n-form ref="formRef" :model="form" label-placement="top">
          <n-grid :cols="2" :x-gap="24" responsive="screen">
            <n-grid-item span="2 m:1">
              <n-form-item label="姓名" path="name">
                <n-input v-model:value="form.name" />
              </n-form-item>
            </n-grid-item>
            <n-grid-item span="2 m:1">
              <n-form-item label="职位/头衔" path="title">
                <n-input v-model:value="form.title" placeholder="例如：全栈开发工程师" />
              </n-form-item>
            </n-grid-item>
            <n-grid-item span="2 m:1">
              <n-form-item label="学校" path="school">
                <n-input v-model:value="form.school" />
              </n-form-item>
            </n-grid-item>
            <n-grid-item span="2 m:1">
              <n-form-item label="专业" path="major">
                <n-input v-model:value="form.major" />
              </n-form-item>
            </n-grid-item>
            <n-grid-item span="2">
              <n-form-item label="个人简介" path="bio">
                <n-input v-model:value="form.bio" type="textarea" :autosize="{ minRows: 3, maxRows: 6 }" />
              </n-form-item>
            </n-grid-item>
            <n-grid-item span="2 m:1">
              <n-form-item label="邮箱" path="email">
                <n-input v-model:value="form.email" />
              </n-form-item>
            </n-grid-item>
            <n-grid-item span="2 m:1">
              <n-form-item label="GitHub" path="github">
                <n-input v-model:value="form.github" placeholder="https://github.com/..." />
              </n-form-item>
            </n-grid-item>
            <n-grid-item span="2 m:1">
              <n-form-item label="QQ" path="qq">
                <n-input v-model:value="form.qq" />
              </n-form-item>
            </n-grid-item>
            <n-grid-item span="2 m:1">
              <n-form-item label="微信" path="wechat">
                <n-input v-model:value="form.wechat" />
              </n-form-item>
            </n-grid-item>
          </n-grid>
        </n-form>
        <div class="form-actions">
          <n-button type="primary" size="large" :loading="saving" @click="save">保存修改</n-button>
          <n-button size="large" @click="reset">恢复原始</n-button>
        </div>
      </div>
      <p v-if="savedMsg" class="save-hint">{{ savedMsg }}</p>
    </div>
  </DefaultLayout>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { NButton, NIcon, NForm, NFormItem, NInput, NGrid, NGridItem, useMessage } from 'naive-ui'
import { ArrowBackOutline } from '@vicons/ionicons5'
import DefaultLayout from '@/layouts/DefaultLayout.vue'
import { useDataStore } from '@/stores/data'

const message = useMessage()
const store = useDataStore()
const formRef = ref()
const saving = ref(false)
const savedMsg = ref('')

const form = reactive({ ...store.profile })
const original = { ...store.profile }

async function save() {
  saving.value = true
  await new Promise(r => setTimeout(r, 300))
  Object.assign(store.profile, form)
  Object.assign(original, form)
  saving.value = false
  savedMsg.value = '保存成功！'
  message.success('个人信息已更新')
  setTimeout(() => savedMsg.value = '', 2000)
}

function reset() {
  Object.assign(form, original)
  savedMsg.value = ''
}
</script>

<style scoped lang="scss">
.admin-page {
  max-width: 800px;
  margin: 0 auto;
}

.admin-header {
  margin-bottom: $gap-xl;
  .back-btn { margin-bottom: $gap-sm; }
  h1 { font-size: 24px; font-weight: 700; }
}

.admin-form {
  padding: $gap-xl;
}

.form-actions {
  display: flex;
  gap: $gap-md;
  margin-top: $gap-lg;
}

.save-hint {
  text-align: center;
  color: $accent;
  margin-top: $gap-md;
  font-size: 13px;
}
</style>
