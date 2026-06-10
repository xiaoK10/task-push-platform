<template>
  <div class="dashboard">
    <div class="stats-cards">
      <div class="stat-card">
        <div class="stat-icon user-icon">
          <el-icon><Users /></el-icon>
        </div>
        <div class="stat-info">
          <div class="stat-value">{{ stats.totalUsers }}</div>
          <div class="stat-label">总用户数</div>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-icon task-icon">
          <el-icon><ClipboardList /></el-icon>
        </div>
        <div class="stat-info">
          <div class="stat-value">{{ stats.totalTasks }}</div>
          <div class="stat-label">总任务数</div>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-icon order-icon">
          <el-icon><ShoppingCart /></el-icon>
        </div>
        <div class="stat-info">
          <div class="stat-value">{{ stats.totalOrders }}</div>
          <div class="stat-label">总订单数</div>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-icon fund-icon">
          <el-icon><Wallet /></el-icon>
        </div>
        <div class="stat-info">
          <div class="stat-value">¥{{ stats.totalFunds }}</div>
          <div class="stat-label">总交易额</div>
        </div>
      </div>
    </div>

    <div class="charts-row">
      <div class="chart-card">
        <h3>任务分类统计</h3>
        <div ref="categoryChart" class="chart"></div>
      </div>
      <div class="chart-card">
        <h3>订单状态分布</h3>
        <div ref="statusChart" class="chart"></div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import { Users, ClipboardList, ShoppingCart, Wallet } from '@element-plus/icons-vue'
import * as echarts from 'echarts'

const stats = ref({
  totalUsers: 0,
  totalTasks: 0,
  totalOrders: 0,
  totalFunds: 0
})

const categoryChart = ref(null)
const statusChart = ref(null)
let chart1 = null
let chart2 = null

const loadStats = async () => {
  stats.value = {
    totalUsers: 12580,
    totalTasks: 3890,
    totalOrders: 8654,
    totalFunds: 1285600
  }
}

const initCharts = () => {
  if (categoryChart.value) {
    chart1 = echarts.init(categoryChart.value)
    chart1.setOption({
      title: { show: false },
      tooltip: { trigger: 'item' },
      legend: { orient: 'vertical', right: 10 },
      series: [{
        type: 'pie',
        radius: ['40%', '70%'],
        avoidLabelOverlap: false,
        itemStyle: { borderRadius: 10, borderColor: '#fff', borderWidth: 2 },
        label: { show: false },
        emphasis: { label: { show: true, fontSize: 16, fontWeight: 'bold' } },
        data: [
          { value: 35, name: '新媒体' },
          { value: 25, name: '电商运营' },
          { value: 20, name: '办公文职' },
          { value: 15, name: '创意设计' },
          { value: 5, name: '其他' }
        ]
      }]
    })
  }

  if (statusChart.value) {
    chart2 = echarts.init(statusChart.value)
    chart2.setOption({
      xAxis: { type: 'category', data: ['待接单', '已接单', '待审核', '已完成', '已驳回'] },
      yAxis: { type: 'value' },
      series: [{
        data: [120, 340, 280, 560, 45],
        type: 'bar',
        barWidth: '50%',
        itemStyle: { borderRadius: [6, 6, 0, 0] }
      }]
    })
  }
}

const handleResize = () => {
  chart1?.resize()
  chart2?.resize()
}

onMounted(() => {
  loadStats()
  initCharts()
  window.addEventListener('resize', handleResize)
})

onUnmounted(() => {
  window.removeEventListener('resize', handleResize)
  chart1?.dispose()
  chart2?.dispose()
})
</script>

<style scoped>
.dashboard {
  padding: 24px;
}

.stats-cards {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
  margin-bottom: 24px;
}

.stat-card {
  background: #fff;
  border-radius: 12px;
  padding: 24px;
  display: flex;
  align-items: center;
  gap: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
}

.stat-icon {
  width: 64px;
  height: 64px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 28px;
}

.user-icon { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: #fff; }
.task-icon { background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%); color: #fff; }
.order-icon { background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%); color: #fff; }
.fund-icon { background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%); color: #fff; }

.stat-value {
  font-size: 28px;
  font-weight: 600;
  color: #333;
}

.stat-label {
  font-size: 14px;
  color: #999;
}

.charts-row {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 20px;
}

.chart-card {
  background: #fff;
  border-radius: 12px;
  padding: 24px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
}

.chart-card h3 {
  margin-bottom: 20px;
  font-size: 16px;
  font-weight: 600;
  color: #333;
}

.chart {
  height: 300px;
}
</style>