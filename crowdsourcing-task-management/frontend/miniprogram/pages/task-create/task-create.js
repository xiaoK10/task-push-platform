const app = getApp()

Page({
  data: {
    formData: {
      title: '',
      category: null,
      description: '',
      steps: '',
      acceptanceCriteria: '',
      deadline: '',
      commission: '',
      maxWorkers: ''
    }
  },

  onTitleInput: function (e) {
    this.data.formData.title = e.detail.value
  },

  selectCategory: function (e) {
    this.data.formData.category = parseInt(e.currentTarget.dataset.category)
    this.setData({ formData: this.data.formData })
  },

  onDescInput: function (e) {
    this.data.formData.description = e.detail.value
  },

  onStepsInput: function (e) {
    this.data.formData.steps = e.detail.value
  },

  onCriteriaInput: function (e) {
    this.data.formData.acceptanceCriteria = e.detail.value
  },

  onDeadlineChange: function (e) {
    this.data.formData.deadline = e.detail.value + ' 23:59:59'
  },

  onCommissionInput: function (e) {
    this.data.formData.commission = e.detail.value
  },

  onMaxWorkersInput: function (e) {
    this.data.formData.maxWorkers = e.detail.value
  },

  handleSubmit: function () {
    const data = this.data.formData
    
    if (!data.title) {
      wx.showToast({ title: '请输入任务标题', icon: 'none' })
      return
    }
    if (!data.category) {
      wx.showToast({ title: '请选择任务分类', icon: 'none' })
      return
    }
    if (!data.commission) {
      wx.showToast({ title: '请输入佣金', icon: 'none' })
      return
    }
    if (!data.maxWorkers) {
      wx.showToast({ title: '请输入可接人数', icon: 'none' })
      return
    }

    const userInfo = wx.getStorageSync('userInfo')
    
    const requestData = {
      title: data.title,
      category: data.category,
      description: data.description,
      steps: data.steps,
      acceptanceCriteria: data.acceptanceCriteria,
      deadline: data.deadline,
      commission: parseFloat(data.commission),
      maxWorkers: parseInt(data.maxWorkers),
      totalBudget: parseFloat(data.commission) * parseInt(data.maxWorkers),
      deliveryType: 1,
      autoAuditHours: 24
    }

    wx.request({
      url: `${app.globalData.baseUrl}/api/employer/${userInfo.id}/tasks`,
      method: 'POST',
      data: requestData,
      success: (res) => {
        if (res.data.code === 200) {
          wx.showToast({ title: '任务创建成功', icon: 'success' })
          setTimeout(() => {
            wx.navigateBack()
          }, 1500)
        } else {
          wx.showToast({ title: res.data.message, icon: 'none' })
        }
      },
      fail: () => {
        wx.showToast({ title: '创建失败', icon: 'none' })
      }
    })
  }
})