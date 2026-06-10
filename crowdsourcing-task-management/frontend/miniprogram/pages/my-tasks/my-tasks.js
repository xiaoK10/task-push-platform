const app = getApp()

Page({
  data: {
    currentTab: 'all',
    orderList: []
  },

  onLoad: function () {
    this.loadOrders()
  },

  onShow: function () {
    this.loadOrders()
  },

  switchTab: function (e) {
    const tab = e.currentTarget.dataset.tab
    this.setData({ currentTab: tab })
    this.loadOrders()
  },

  loadOrders: function () {
    const userInfo = wx.getStorageSync('userInfo')
    if (!userInfo) return

    let status = null
    if (this.data.currentTab === 'ongoing') {
      status = [1, 2, 3]
    } else if (this.data.currentTab === 'completed') {
      status = [4, 7]
    }

    wx.request({
      url: `${app.globalData.baseUrl}/api/worker/${userInfo.id}/orders`,
      method: 'GET',
      data: { status: status ? status.join(',') : '' },
      success: (res) => {
        if (res.data.code === 200) {
          this.setData({ orderList: res.data.data })
        }
      },
      fail: () => {
        wx.showToast({ title: '加载失败', icon: 'none' })
      }
    })
  },

  goToOrderDetail: function (e) {
    const orderId = e.currentTarget.dataset.orderId
    wx.navigateTo({ url: `/pages/order-detail/order-detail?id=${orderId}` })
  },

  goToSubmit: function (e) {
    const orderId = e.currentTarget.dataset.orderId
    wx.navigateTo({ url: `/pages/order-detail/order-detail?id=${orderId}&action=submit` })
  },

  goToRetry: function (e) {
    const orderId = e.currentTarget.dataset.orderId
    wx.navigateTo({ url: `/pages/order-detail/order-detail?id=${orderId}&action=retry` })
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
    return `${date.getMonth() + 1}/${date.getDate()} ${date.getHours()}:${String(date.getMinutes()).padStart(2, '0')}`
  }
})