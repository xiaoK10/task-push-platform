<template>
  <div class="layout-container">
    <aside class="sidebar">
      <div class="sidebar-header">
        <span class="sidebar-title">安企付众包</span>
      </div>
      <el-menu :default-active="activeMenu" class="sidebar-menu">
        <el-menu-item index="Dashboard">
          <el-icon><component :is="icons.Dashboard" /></el-icon>
          <span>数据概览</span>
        </el-menu-item>
        <el-menu-item index="Users">
          <el-icon><component :is="icons.Users" /></el-icon>
          <span>用户管理</span>
        </el-menu-item>
        <el-menu-item index="Tasks">
          <el-icon><component :is="icons.Tasks" /></el-icon>
          <span>任务管理</span>
        </el-menu-item>
        <el-menu-item index="Orders">
          <el-icon><component :is="icons.Orders" /></el-icon>
          <span>订单管理</span>
        </el-menu-item>
        <el-menu-item index="Funds">
          <el-icon><component :is="icons.Funds" /></el-icon>
          <span>资金管理</span>
        </el-menu-item>
        <el-menu-item index="Disputes">
          <el-icon><component :is="icons.Disputes" /></el-icon>
          <span>纠纷仲裁</span>
        </el-menu-item>
        <el-menu-item index="System">
          <el-icon><component :is="icons.System" /></el-icon>
          <span>系统设置</span>
        </el-menu-item>
      </el-menu>
    </aside>
    <main class="main-content">
      <header class="header">
        <div class="header-left">
          <span class="page-title">{{ pageTitle }}</span>
        </div>
        <div class="header-right">
          <el-button type="text" @click="handleLogout">退出登录</el-button>
        </div>
      </header>
      <router-view />
    </main>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { 
  LayoutDashboard as Dashboard, 
  Users, 
  ClipboardList as Tasks, 
  ShoppingCart as Orders,
  Wallet as Funds,
  FileWarning as Disputes,
  Settings as System
} from '@element-plus/icons-vue'

const router = useRouter()

const icons = { Dashboard, Users, Tasks, Orders, Funds, Disputes, System }

const activeMenu = computed(() => {
  return router.currentRoute.value.name || 'Dashboard'
})

const pageTitle = computed(() => {
  const titles = {
    Dashboard: '数据概览',
    Users: '用户管理',
    Tasks: '任务管理',
    Orders: '订单管理',
    Funds: '资金管理',
    Disputes: '纠纷仲裁',
    System: '系统设置'
  }
  return titles[activeMenu.value] || ''
})

const handleLogout = () => {
  localStorage.removeItem('token')
  localStorage.removeItem('adminInfo')
  ElMessage.success('退出成功')
  router.push('/login')
}
</script>

<style scoped>
.layout-container {
  display: flex;
  min-height: 100vh;
  background-color: #f5f7fa;
}

.sidebar {
  width: 200px;
  background: linear-gradient(180deg, #1f2937 0%, #111827 100%);
  color: #fff;
}

.sidebar-header {
  padding: 20px;
  border-bottom: 1px solid #374151;
}

.sidebar-title {
  font-size: 18px;
  font-weight: 600;
}

.sidebar-menu {
  border-right: none;
}

.main-content {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 24px;
  background: #fff;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
}

.page-title {
  font-size: 20px;
  font-weight: 600;
  color: #333;
}
</style>