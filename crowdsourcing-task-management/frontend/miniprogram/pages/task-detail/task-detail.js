const app = getApp()

Page({
  data: {
    task: null,
    steps: [],
    isFavorited: false
  },

  onLoad: function (options) {
    const taskId = options.id
    this.loadTaskDetail(taskId)
  },

  loadTaskDetail: function (taskId) {
    wx.request({
      url: `${app.globalData.baseUrl}/api/worker/tasks/${taskId}`,
      method: 'GET',
      success: (res) => {
        if (res.data.code === 200) {
          const task = res.data.data
          this.setData({ 
            task: task,
            steps: task.steps ? task.steps.split('\n').filter(s => s.trim()) : []
          })
        }
      },
      fail: () => {
        wx.showToast({ title: '加载失败', icon: 'none' })
      }
    })
  },

  handleFavorite: function () {
    this.setData({ isFavorited: !this.data.isFavorited })
    wx.showToast({ 
      title: this.data.isFavorited ? '收藏成功' : '取消收藏', 
      icon: 'none' 
    })
  },

  handleAccept: function () {
    const userInfo = wx.getStorageSync('userInfo')
    if (!userInfo) {
      wx.showToast({ title: '请先登录', icon: 'none' })
      return
    }

    if (userInfo.authStatus !== 2) {
      wx.showModal({
        title: '提示',
        content: '需要完成实名认证才能接单',
        confirmText: '去认证',
        success: (res) => {
          if (res.confirm) {
            wx.navigateTo({ url: '/pages/profile/profile' })
          }
        }
      })
      return
    }

    wx.request({
      url: `${app.globalData.baseUrl}/api/worker/${userInfo.id}/tasks/${this.data.task.id}/accept`,
      method: 'POST',
      success: (res) => {
        if (res.data.code === 200) {
          wx.showToast({ title: '接单成功', icon: 'success' })
          setTimeout(() => {
            wx.navigateTo({ url: '/pages/my-tasks/my-tasks' })
          }, 1500)
        } else {
          wx.showToast({ title: res.data.message, icon: 'none' })
        }
      },
      fail: () => {
        wx.showToast({ title: '接单失败', icon: 'none' })
      }
    })
  },

  getCategoryName: function (category) {
    const names = {
      1: '新媒体',
      2: '电商运营',
      3: '办公文职',
      4: '创意设计',
      5: '其他'
    }
    return names[category] || '其他'
  },

  formatDeadline: function (deadline) {
    if (!deadline) return ''
    const date = new Date(deadline)
    return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')} ${String(date.getHours()).padStart(2, '0')}:${String(date.getMinutes()).padStart(2, '0')}`
  }
})