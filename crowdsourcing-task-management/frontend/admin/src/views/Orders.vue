<template>
  <div class="orders-container">
    <div class="search-bar">
      <el-input 
        v-model="searchKey" 
        placeholder="搜索订单号或任务标题" 
        class="search-input"
      />
      <el-select v-model="filterStatus" placeholder="订单状态">
        <el-option :value="0" label="全部" />
        <el-option :value="0" label="待接单" />
        <el-option :value="1" label="已接单" />
        <el-option :value="2" label="待提交" />
        <el-option :value="3" label="待审核" />
        <el-option :value="4" label="审核通过" />
        <el-option :value="5" label="已驳回" />
        <el-option :value="7" label="已完成" />
      </el-select>
      <el-button type="primary" @click="handleSearch">搜索</el-button>
    </div>

    <el-table :data="orderList" border>
      <el-table-column prop="id" label="订单ID" width="100" />
      <el-table-column prop="taskTitle" label="任务标题" />
      <el-table-column prop="workerName" label="执行者" width="100" />
      <el-table-column prop="commission" label="佣金" width="100">
        <template #default="scope">¥{{ scope.row.commission }}</template>
      </el-table-column>
      <el-table-column prop="status" label="订单状态">
        <template #default="scope">
          <el-tag :type="getStatusTag(scope.row.status)">
            {{ getStatusText(scope.row.status) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="acceptTime" label="接单时间" width="160" />
      <el-table-column prop="deliveryTime" label="提交时间" width="160" />
      <el-table-column label="操作" width="150">
        <template #default="scope">
          <el-button type="text" @click="viewDetail(scope.row)">查看</el-button>
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

const orderList = ref([
  { id: 'ORD20240115001', taskTitle: '微信公众号文章撰写', workerName: '李四', commission: 50, status: 3, acceptTime: '2024-01-15 10:30:00', deliveryTime: '2024-01-15 14:00:00' },
  { id: 'ORD20240116002', taskTitle: '淘宝店铺装修设计', workerName: '王五', commission: 200, status: 4, acceptTime: '2024-01-16 14:20:00', deliveryTime: '2024-01-17 10:00:00' },
  { id: 'ORD20240117003', taskTitle: 'Excel数据整理', workerName: '赵六', commission: 80, status: 1, acceptTime: '2024-01-17 09:15:00', deliveryTime: '' },
  { id: 'ORD20240118004', taskTitle: '短视频剪辑制作', workerName: '钱七', commission: 150, status: 5, acceptTime: '2024-01-18 16:45:00', deliveryTime: '2024-01-19 09:00:00' },
  { id: 'ORD20240119005', taskTitle: '产品文案撰写', workerName: '张三', commission: 100, status: 7, acceptTime: '2024-01-19 11:00:00', deliveryTime: '2024-01-19 15:00:00' }
])

total.value = orderList.value.length

const handleSearch = () => {
  page.value = 1
}

const handlePageChange = (val) => {
  page.value = val
}

const getStatusText = (status) => {
  const texts = { 0: '待接单', 1: '已接单', 2: '待提交', 3: '待审核', 4: '审核通过', 5: '已驳回', 6: '已作废', 7: '已完成' }
  return texts[status] || '未知'
}

const getStatusTag = (status) => {
  const types = { 0: 'info', 1: 'primary', 2: 'primary', 3: 'warning', 4: 'success', 5: 'danger', 6: 'danger', 7: 'info' }
  return types[status] || 'info'
}

const viewDetail = (row) => {
  console.log('查看订单:', row)
}
</script>

<style scoped>
.orders-container {
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