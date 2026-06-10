<template>
  <div class="tasks-container">
    <div class="search-bar">
      <el-input 
        v-model="searchKey" 
        placeholder="搜索任务标题" 
        class="search-input"
      />
      <el-select v-model="filterCategory" placeholder="任务分类">
        <el-option :value="0" label="全部" />
        <el-option :value="1" label="新媒体" />
        <el-option :value="2" label="电商运营" />
        <el-option :value="3" label="办公文职" />
        <el-option :value="4" label="创意设计" />
        <el-option :value="5" label="其他" />
      </el-select>
      <el-select v-model="filterStatus" placeholder="任务状态">
        <el-option :value="0" label="全部" />
        <el-option :value="1" label="待审核" />
        <el-option :value="2" label="审核通过" />
        <el-option :value="3" label="进行中" />
        <el-option :value="4" label="已完成" />
        <el-option :value="5" label="已取消" />
      </el-select>
      <el-button type="primary" @click="handleSearch">搜索</el-button>
    </div>

    <el-table :data="taskList" border>
      <el-table-column prop="id" label="任务ID" width="80" />
      <el-table-column prop="title" label="任务标题" />
      <el-table-column prop="category" label="任务分类">
        <template #default="scope">
          {{ getCategoryText(scope.row.category) }}
        </template>
      </el-table-column>
      <el-table-column prop="commission" label="单任务佣金" width="120">
        <template #default="scope">¥{{ scope.row.commission }}</template>
      </el-table-column>
      <el-table-column prop="maxWorkers" label="可接人数" width="80" />
      <el-table-column prop="status" label="任务状态">
        <template #default="scope">
          <el-tag :type="getStatusTag(scope.row.status)">
            {{ getStatusText(scope.row.status) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createdAt" label="发布时间" width="160" />
      <el-table-column label="操作" width="180">
        <template #default="scope">
          <el-button type="text" @click="viewDetail(scope.row)">查看</el-button>
          <el-button 
            type="text" 
            v-if="scope.row.status === 1" 
            @click="auditTask(scope.row)"
          >审核</el-button>
          <el-button type="text" @click="disableTask(scope.row)">关闭</el-button>
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
const filterCategory = ref(0)
const filterStatus = ref(0)
const page = ref(1)
const pageSize = ref(10)
const total = ref(0)

const taskList = ref([
  { id: 1, title: '微信公众号文章撰写', category: 1, commission: 50, maxWorkers: 3, status: 2, createdAt: '2024-01-15 10:30:00' },
  { id: 2, title: '淘宝店铺装修设计', category: 4, commission: 200, maxWorkers: 1, status: 3, createdAt: '2024-01-16 14:20:00' },
  { id: 3, title: 'Excel数据整理', category: 3, commission: 80, maxWorkers: 5, status: 1, createdAt: '2024-01-17 09:15:00' },
  { id: 4, title: '短视频剪辑制作', category: 1, commission: 150, maxWorkers: 2, status: 2, createdAt: '2024-01-18 16:45:00' },
  { id: 5, title: '产品文案撰写', category: 2, commission: 100, maxWorkers: 4, status: 4, createdAt: '2024-01-19 11:00:00' }
])

total.value = taskList.value.length

const handleSearch = () => {
  page.value = 1
}

const handlePageChange = (val) => {
  page.value = val
}

const getCategoryText = (category) => {
  const texts = { 1: '新媒体', 2: '电商运营', 3: '办公文职', 4: '创意设计', 5: '其他' }
  return texts[category] || '未知'
}

const getStatusText = (status) => {
  const texts = { 1: '待审核', 2: '审核通过', 3: '进行中', 4: '已完成', 5: '已取消' }
  return texts[status] || '未知'
}

const getStatusTag = (status) => {
  const types = { 1: 'warning', 2: 'success', 3: 'primary', 4: 'info', 5: 'danger' }
  return types[status] || 'info'
}

const viewDetail = (row) => {
  console.log('查看任务:', row)
}

const auditTask = (row) => {
  row.status = 2
}

const disableTask = (row) => {
  row.status = 5
}
</script>

<style scoped>
.tasks-container {
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