<template>
  <div class="users-container">
    <div class="search-bar">
      <el-input 
        v-model="searchKey" 
        placeholder="搜索用户名或手机号" 
        class="search-input"
        @keyup.enter="handleSearch"
      />
      <el-select v-model="filterIdentity" placeholder="身份类型">
        <el-option :value="0" label="全部" />
        <el-option :value="1" label="雇主" />
        <el-option :value="2" label="执行者" />
      </el-select>
      <el-select v-model="filterAuth" placeholder="认证状态">
        <el-option :value="0" label="全部" />
        <el-option :value="1" label="未认证" />
        <el-option :value="2" label="审核中" />
        <el-option :value="3" label="已认证" />
        <el-option :value="4" label="已驳回" />
      </el-select>
      <el-button type="primary" @click="handleSearch">搜索</el-button>
    </div>

    <el-table :data="userList" border>
      <el-table-column prop="id" label="用户ID" width="80" />
      <el-table-column prop="nickname" label="用户名" />
      <el-table-column prop="phone" label="手机号" />
      <el-table-column prop="identityType" label="身份类型">
        <template #default="scope">
          {{ scope.row.identityType === 1 ? '雇主' : '执行者' }}
        </template>
      </el-table-column>
      <el-table-column prop="authStatus" label="认证状态">
        <template #default="scope">
          <el-tag :type="getStatusTag(scope.row.authStatus)">
            {{ getAuthStatusText(scope.row.authStatus) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="creditScore" label="信用分" width="80" />
      <el-table-column prop="createdAt" label="注册时间" width="160" />
      <el-table-column label="操作" width="150">
        <template #default="scope">
          <el-button type="text" @click="viewDetail(scope.row)">查看</el-button>
          <el-button type="text" @click="toggleBan(scope.row)">
            {{ scope.row.banned ? '解封' : '封禁' }}
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination 
      :total="total" 
      :current-page="page" 
      :page-size="pageSize"
      @current-change="handlePageChange"
    />
  </div>
</template>

<script setup>
import { ref } from 'vue'

const searchKey = ref('')
const filterIdentity = ref(0)
const filterAuth = ref(0)
const page = ref(1)
const pageSize = ref(10)
const total = ref(0)

const userList = ref([
  { id: 1, nickname: '张三', phone: '138****1234', identityType: 1, authStatus: 3, creditScore: 100, createdAt: '2024-01-15 10:30:00', banned: false },
  { id: 2, nickname: '李四', phone: '139****5678', identityType: 2, authStatus: 3, creditScore: 95, createdAt: '2024-01-16 14:20:00', banned: false },
  { id: 3, nickname: '王五', phone: '137****9012', identityType: 2, authStatus: 1, creditScore: 80, createdAt: '2024-01-17 09:15:00', banned: false },
  { id: 4, nickname: '赵六', phone: '136****3456', identityType: 1, authStatus: 2, creditScore: 90, createdAt: '2024-01-18 16:45:00', banned: false },
  { id: 5, nickname: '钱七', phone: '135****7890', identityType: 2, authStatus: 4, creditScore: 70, createdAt: '2024-01-19 11:00:00', banned: true }
])

total.value = userList.value.length

const handleSearch = () => {
  page.value = 1
}

const handlePageChange = (val) => {
  page.value = val
}

const getAuthStatusText = (status) => {
  const texts = { 1: '未认证', 2: '审核中', 3: '已认证', 4: '已驳回' }
  return texts[status] || '未知'
}

const getStatusTag = (status) => {
  const types = { 1: 'info', 2: 'warning', 3: 'success', 4: 'danger' }
  return types[status] || 'info'
}

const viewDetail = (row) => {
  console.log('查看用户:', row)
}

const toggleBan = (row) => {
  row.banned = !row.banned
}
</script>

<style scoped>
.users-container {
  padding: 24px;
}

.search-bar {
  display: flex;
  gap: 16px;
  margin-bottom: 20px;
}

.search-input {
  width: 300px;
}
</style>