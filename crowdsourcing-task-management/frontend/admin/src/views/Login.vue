<template>
  <div class="login-container">
    <div class="login-box">
      <div class="logo">
        <span class="logo-text">安企付众包管理后台</span>
      </div>
      <el-form ref="loginForm" :model="form" label-width="80px">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="form.username" placeholder="请输入用户名" />
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input type="password" v-model="form.password" placeholder="请输入密码" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleLogin" class="login-btn">登录</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { ElMessage } from 'element-plus'
import axios from 'axios'

const form = ref({
  username: '',
  password: ''
})

const handleLogin = async () => {
  try {
    const res = await axios.post('/api/admin/login', form.value)
    if (res.data.code === 200) {
      localStorage.setItem('token', res.data.data.token)
      localStorage.setItem('adminInfo', JSON.stringify(res.data.data))
      ElMessage.success('登录成功')
      window.location.href = '/'
    } else {
      ElMessage.error(res.data.message)
    }
  } catch (error) {
    ElMessage.error('登录失败')
  }
}
</script>

<style scoped>
.login-container {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.login-box {
  width: 400px;
  padding: 40px;
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
}

.logo {
  text-align: center;
  margin-bottom: 30px;
}

.logo-text {
  font-size: 24px;
  font-weight: 600;
  color: #333;
}

.login-btn {
  width: 100%;
}
</style>