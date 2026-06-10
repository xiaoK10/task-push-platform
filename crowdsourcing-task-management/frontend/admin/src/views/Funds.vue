<template>
  <div class="funds-container">
    <div class="fund-summary">
      <div class="summary-item">
        <div class="summary-label">平台总资金</div>
        <div class="summary-value">¥{{ formatMoney(totalFunds) }}</div>
      </div>
      <div class="summary-item">
        <div class="summary-label">用户可提现余额</div>
        <div class="summary-value">¥{{ formatMoney(availableFunds) }}</div>
      </div>
      <div class="summary-item">
        <div class="summary-label">冻结资金</div>
        <div class="summary-value">¥{{ formatMoney(frozenFunds) }}</div>
      </div>
      <div class="summary-item">
        <div class="summary-label">今日交易笔数</div>
        <div class="summary-value">{{ todayTransactions }}</div>
      </div>
    </div>

    <div class="search-bar">
      <el-input 
        v-model="searchKey" 
        placeholder="搜索用户ID或订单号" 
        class="search-input"
      />
      <el-select v-model="filterType" placeholder="交易类型">
        <el-option :value="0" label="全部" />
        <el-option :value="1" label="充值" />
        <el-option :value="2" label="提现" />
        <el-option :value="3" label="冻结" />
        <el-option :value="4" label="解冻" />
        <el-option :value="5" label="发放" />
      </el-select>
      <el-button type="primary" @click="handleSearch">搜索</el-button>
    </div>

    <el-table :data="fundList" border>
      <el-table-column prop="id" label="流水ID" width="120" />
      <el-table-column prop="userId" label="用户ID" width="100" />
      <el-table-column prop="orderId" label="订单号" width="140" />
      <el-table-column prop="type" label="交易类型">
        <template #default="scope">
          <el-tag :type="getTypeTag(scope.row.type)">
            {{ getTypeText(scope.row.type) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="amount" label="金额" width="120">
        <template #default="scope">
          <span :class="scope.row.amount > 0 ? 'text-green' : 'text-red'">
            {{ scope.row.amount > 0 ? '+' : '' }}¥{{ scope.row.amount }}
          </span>
        </template>
      </el-table-column>
      <el-table-column prop="balance" label="余额" width="120">
        <template #default="scope">¥{{ scope.row.balance }}</template>
      </el-table-column>
      <el-table-column prop="createdAt" label="交易时间" width="160" />
      <el-table-column prop="remark" label="备注" />
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
const filterType = ref(0)
const page = ref(1)
const pageSize = ref(10)
const total = ref(0)

const totalFunds = ref(5286500)
const availableFunds = ref(3856200)
const frozenFunds = ref(1430300)
const todayTransactions = ref(156)

const fundList = ref([
  { id: 'FD20240115001', userId: 1001, orderId: 'ORD20240115001', type: 1, amount: 1000, balance: 5200, createdAt: '2024-01-15 10:30:00', remark: '用户充值' },
  { id: 'FD20240115002', userId: 1002, orderId: 'ORD20240116002', type: 3, amount: -200, balance: 3800, createdAt: '2024-01-15 11:00:00', remark: '任务佣金冻结' },
  { id: 'FD20240115003', userId: 1003, orderId: '', type: 2, amount: -500, balance: 1500, createdAt: '2024-01-15 14:30:00', remark: '提现成功' },
  { id: 'FD20240115004', userId: 1004, orderId: 'ORD20240118004', type: 5, amount: 150, balance: 2650, createdAt: '2024-01-15 16:00:00', remark: '佣金发放' },
  { id: 'FD20240115005', userId: 1005, orderId: '', type: 1, amount: 5000, balance: 8200, createdAt: '2024-01-15 17:30:00', remark: '用户充值' }
])

total.value = fundList.value.length

const formatMoney = (value) => {
  return value.toLocaleString()
}

const handleSearch = () => {
  page.value = 1
}

const handlePageChange = (val) => {
  page.value = val
}

const getTypeText = (type) => {
  const texts = { 1: '充值', 2: '提现', 3: '冻结', 4: '解冻', 5: '发放' }
  return texts[type] || '未知'
}

const getTypeTag = (type) => {
  const types = { 1: 'success', 2: 'danger', 3: 'warning', 4: 'info', 5: 'success' }
  return types[type] || 'info'
}
</script>

<style scoped>
.funds-container {
  padding: 24px;
}

.fund-summary {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
  margin-bottom: 24px;
}

.summary-item {
  background: #fff;
  border-radius: 12px;
  padding: 20px;
  text-align: center;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
}

.summary-label {
  font-size: 14px;
  color: #999;
  margin-bottom: 8px;
}

.summary-value {
  font-size: 24px;
  font-weight: 600;
  color: #333;
}

.search-bar {
  display: flex;
  gap: 16px;
  margin-bottom: 20px;
}

.search-input {
  width: 300px;
}

.text-green {
  color: #67c23a;
}

.text-red {
  color: #f56c6c;
}
</style>