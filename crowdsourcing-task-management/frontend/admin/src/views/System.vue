<template>
  <div class="system-container">
    <div class="system-section">
      <h3 class="section-title">平台配置</h3>
      <el-form label-width="150px" class="config-form">
        <el-form-item label="平台名称">
          <el-input v-model="config.platformName" />
        </el-form-item>
        <el-form-item label="提现手续费比例">
          <el-input v-model="config.withdrawFeeRate" placeholder="例如：0.01表示1%" />
        </el-form-item>
        <el-form-item label="最低提现金额">
          <el-input v-model="config.minWithdrawAmount" />
        </el-form-item>
        <el-form-item label="最高提现金额">
          <el-input v-model="config.maxWithdrawAmount" />
        </el-form-item>
        <el-form-item label="自动审核时长(小时)">
          <el-input v-model="config.autoAuditHours" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="saveConfig">保存配置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <div class="system-section">
      <h3 class="section-title">系统日志</h3>
      <el-table :data="logList" border>
        <el-table-column prop="id" label="ID" width="60" />
        <el-table-column prop="type" label="日志类型">
          <template #default="scope">
            <el-tag :type="getLogTypeTag(scope.row.type)">
              {{ getLogTypeText(scope.row.type) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="content" label="日志内容" />
        <el-table-column prop="operator" label="操作人" width="100" />
        <el-table-column prop="createdAt" label="操作时间" width="160" />
      </el-table>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { ElMessage } from 'element-plus'

const config = ref({
  platformName: '安企付众包',
  withdrawFeeRate: '0.005',
  minWithdrawAmount: '10',
  maxWithdrawAmount: '5000',
  autoAuditHours: '24'
})

const logList = ref([
  { id: 1, type: 1, content: '管理员张三登录系统', operator: 'system', createdAt: '2024-01-15 10:30:00' },
  { id: 2, type: 2, content: '更新平台配置：提现手续费比例改为0.005', operator: '张三', createdAt: '2024-01-15 11:00:00' },
  { id: 3, type: 3, content: '审核任务ID:12345通过', operator: '李四', createdAt: '2024-01-15 14:30:00' },
  { id: 4, type: 1, content: '管理员李四登录系统', operator: 'system', createdAt: '2024-01-15 09:00:00' },
  { id: 5, type: 4, content: '处理申诉ID:DSP20240115001完成', operator: '张三', createdAt: '2024-01-15 16:00:00' }
])

const saveConfig = () => {
  ElMessage.success('配置保存成功')
}

const getLogTypeText = (type) => {
  const texts = { 1: '系统', 2: '配置', 3: '审核', 4: '仲裁' }
  return texts[type] || '未知'
}

const getLogTypeTag = (type) => {
  const types = { 1: 'info', 2: 'primary', 3: 'success', 4: 'warning' }
  return types[type] || 'info'
}
</script>

<style scoped>
.system-container {
  padding: 24px;
}

.system-section {
  background: #fff;
  border-radius: 12px;
  padding: 24px;
  margin-bottom: 24px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
}

.section-title {
  font-size: 16px;
  font-weight: 600;
  color: #333;
  margin-bottom: 20px;
}

.config-form {
  width: 500px;
}
</style>