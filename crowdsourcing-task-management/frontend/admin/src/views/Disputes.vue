<template>
  <div class="disputes-container">
    <div class="search-bar">
      <el-input 
        v-model="searchKey" 
        placeholder="搜索订单号或用户ID" 
        class="search-input"
      />
      <el-select v-model="filterStatus" placeholder="处理状态">
        <el-option :value="0" label="全部" />
        <el-option :value="1" label="待处理" />
        <el-option :value="2" label="处理中" />
        <el-option :value="3" label="已结案" />
      </el-select>
      <el-button type="primary" @click="handleSearch">搜索</el-button>
    </div>

    <el-table :data="disputeList" border>
      <el-table-column prop="id" label="申诉ID" width="100" />
      <el-table-column prop="orderId" label="订单号" width="140" />
      <el-table-column prop="workerId" label="申诉人ID" width="100" />
      <el-table-column prop="type" label="申诉类型">
        <template #default="scope">
          {{ scope.row.type === 1 ? '雇主恶意驳回' : '无故扣佣金' }}
        </template>
      </el-table-column>
      <el-table-column prop="content" label="申诉内容" />
      <el-table-column prop="status" label="处理状态">
        <template #default="scope">
          <el-tag :type="getStatusTag(scope.row.status)">
            {{ getStatusText(scope.row.status) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createdAt" label="申诉时间" width="160" />
      <el-table-column label="操作" width="150">
        <template #default="scope">
          <el-button type="text" @click="viewDetail(scope.row)">查看</el-button>
          <el-button 
            type="text" 
            v-if="scope.row.status !== 3" 
            @click="handleDispute(scope.row)"
          >处理</el-button>
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
const filterStatus = ref(0)
const page = ref(1)
const pageSize = ref(10)
const total = ref(0)

const disputeList = ref([
  { id: 'DSP20240115001', orderId: 'ORD20240118004', workerId: 1004, type: 1, content: '雇主无理由驳回我的任务成果，我已经按照要求完成了所有工作...', status: 1, createdAt: '2024-01-15 10:30:00' },
  { id: 'DSP20240115002', orderId: 'ORD20240116002', workerId: 1002, type: 2, content: '任务完成后雇主没有按时发放佣金，已经超过48小时...', status: 2, createdAt: '2024-01-14 14:20:00' },
  { id: 'DSP20240115003', orderId: 'ORD20240115001', workerId: 1001, type: 1, content: '雇主恶意刁难，多次要求修改但不给出明确标准...', status: 3, createdAt: '2024-01-13 09:15:00' },
  { id: 'DSP20240115004', orderId: 'ORD20240117003', workerId: 1003, type: 2, content: '任务审核通过后佣金一直未到账...', status: 1, createdAt: '2024-01-12 16:45:00' }
])

total.value = disputeList.value.length

const handleSearch = () => {
  page.value = 1
}

const handlePageChange = (val) => {
  page.value = val
}

const getStatusText = (status) => {
  const texts = { 1: '待处理', 2: '处理中', 3: '已结案' }
  return texts[status] || '未知'
}

const getStatusTag = (status) => {
  const types = { 1: 'danger', 2: 'warning', 3: 'success' }
  return types[status] || 'info'
}

const viewDetail = (row) => {
  console.log('查看申诉:', row)
}

const handleDispute = (row) => {
  row.status = 2
}
</script>

<style scoped>
.disputes-container {
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