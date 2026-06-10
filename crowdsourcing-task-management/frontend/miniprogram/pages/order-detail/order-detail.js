const app = getApp()

Page({
  data: {
    order: null,
    showSubmit: false,
    deliveryContent: ''
  },

  onLoad: function (options) {
    const orderId = options.id
    this.loadOrderDetail(orderId)

    if (options.action === 'submit' || options.action === 'retry') {
      this.setData({ showSubmit: true })
    }
  },

  loadOrderDetail: function (orderId) {
    wx.request({
      url: `${app.globalData.baseUrl}/api/worker/orders/${orderId}`,
      method: 'GET',
      success: (res) => {
        if (res.data.code === 200) {
          this.setData({ order: res.data.data })
        }
      },
      fail: () => {
        wx.showToast({ title: '加载失败', icon: 'none' })
      }
    })
  },

  onContentInput: function (e) {
    this.data.deliveryContent = e.detail.value
  },

  handleSubmit: function () {
    if (!this.data.deliveryContent.trim()) {
      wx.showToast({ title: '请输入任务完成内容', icon: 'none' })
      return
    }

    const userInfo = wx.getStorageSync('userInfo')
    const isRetry = this.data.order.status === 5

    const url = isRetry 
      ? `${app.globalData.baseUrl}/api/worker/${userInfo.id}/orders/${this.data.order.id}/retry`
      : `${app.globalData.baseUrl}/api/worker/${userInfo.id}/orders/${this.data.order.id}/submit`

    wx.request({
      url: url,
      method: 'POST',
      data: this.data.deliveryContent,
      success: (res) => {
        if (res.data.code === 200) {
          wx.showToast({ 
            title: isRetry ? '重新提交成功' : '提交成功', 
            icon: 'success' 
          })
          setTimeout(() => {
            wx.navigateBack()
          }, 1500)
        } else {
          wx.showToast({ title: res.data.message, icon: 'none' })
        }
      },
      fail: () => {
        wx.showToast({ title: '提交失败', icon: 'none' })
      }
    })
  },

  goToDispute: function () {
    wx.navigateTo({ url: `/pages/dispute/dispute?orderId=${this.data.order.id}` })
  },

  getStatusText: function (status) {
    const texts = {
      0: '待接单',
      1: '已接单',
      2: '待提交',
      3: '待审核',
      4: '审核通过',
      5: '已驳回',
      6: '已作废',
      7: '已完成'
    }
    return texts[status] || '未知'
  },

  formatTime: function (time) {
    if (!time) return ''
    const date = new Date(time)
    return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')} ${String(date.getHours()).padStart(2, '0')}:${String(date.getMinutes()).padStart(2, '0')}`
  }
})