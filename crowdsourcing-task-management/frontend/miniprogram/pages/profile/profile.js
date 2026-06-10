const app = getApp()

Page({
  data: {
    userInfo: null,
    balance: null
  },

  onLoad: function () {
    this.loadUserInfo()
  },

  onShow: function () {
    this.loadUserInfo()
  },

  loadUserInfo: function () {
    const userInfo = wx.getStorageSync('userInfo')
    if (userInfo) {
      this.setData({ userInfo })
      this.loadBalance(userInfo.id)
    }
  },

  loadBalance: function (userId) {
    wx.request({
      url: `${app.globalData.baseUrl}/api/worker/${userId}/balance`,
      method: 'GET',
      success: (res) => {
        if (res.data.code === 200) {
          this.setData({ balance: res.data.data })
        }
      }
    })
  },

  goToAuth: function () {
    wx.showModal({
      title: '实名认证',
      content: '请输入实名信息',
      editable: true,
      placeholderText: '真实姓名',
      success: (res) => {
        if (res.confirm && res.content) {
          const userInfo = wx.getStorageSync('userInfo')
          wx.request({
            url: `${app.globalData.baseUrl}/api/auth/${userInfo.id}/auth`,
            method: 'POST',
            data: { realName: res.content, idCard: '110101199001011234' },
            success: (result) => {
              if (result.data.code === 200) {
                wx.showToast({ title: '提交成功', icon: 'success' })
                userInfo.authStatus = 1
                wx.setStorageSync('userInfo', userInfo)
                this.setData({ userInfo })
              } else {
                wx.showToast({ title: result.data.message, icon: 'none' })
              }
            }
          })
        }
      }
    })
  },

  switchIdentity: function () {
    const userInfo = this.data.userInfo
    const newIdentity = userInfo.identityType === 1 ? 2 : 1
    
    wx.request({
      url: `${app.globalData.baseUrl}/api/auth/${userInfo.id}/identity`,
      method: 'PUT',
      data: { identityType: newIdentity },
      success: (res) => {
        if (res.data.code === 200) {
          userInfo.identityType = newIdentity
          wx.setStorageSync('userInfo', userInfo)
          this.setData({ userInfo })
          wx.showToast({ title: `已切换为${this.getIdentityText(newIdentity)}`, icon: 'none' })
        }
      }
    })
  },

  goToRecharge: function () {
    wx.showModal({
      title: '充值',
      content: '请输入充值金额',
      editable: true,
      placeholderText: '100',
      success: (res) => {
        if (res.confirm && res.content) {
          const userInfo = wx.getStorageSync('userInfo')
          wx.request({
            url: `${app.globalData.baseUrl}/api/employer/${userInfo.id}/balance/recharge`,
            method: 'POST',
            data: { amount: parseFloat(res.content) },
            success: (result) => {
              if (result.data.code === 200) {
                wx.showToast({ title: '充值成功', icon: 'success' })
                this.loadBalance(userInfo.id)
              }
            }
          })
        }
      }
    })
  },

  goToWithdraw: function () {
    wx.showModal({
      title: '提现',
      content: '请输入提现金额',
      editable: true,
      placeholderText: '10',
      success: (res) => {
        if (res.confirm && res.content) {
          const userInfo = wx.getStorageSync('userInfo')
          wx.request({
            url: `${app.globalData.baseUrl}/api/worker/${userInfo.id}/withdraw`,
            method: 'POST',
            data: { amount: parseFloat(res.content), withdrawPassword: '123456' },
            success: (result) => {
              if (result.data.code === 200) {
                wx.showToast({ title: '提现申请成功', icon: 'success' })
                this.loadBalance(userInfo.id)
              } else {
                wx.showToast({ title: result.data.message, icon: 'none' })
              }
            }
          })
        }
      }
    })
  },

  goToCreateTask: function () {
    wx.navigateTo({ url: '/pages/task-create/task-create' })
  },

  goToSettings: function () {
    wx.showToast({ title: '设置页面开发中', icon: 'none' })
  },

  goToHelp: function () {
    wx.showToast({ title: '帮助中心开发中', icon: 'none' })
  },

  handleLogout: function () {
    wx.showModal({
      title: '确认退出',
      content: '确定要退出登录吗？',
      success: (res) => {
        if (res.confirm) {
          wx.removeStorageSync('userInfo')
          app.globalData.userInfo = null
          wx.showToast({ title: '已退出', icon: 'none' })
        }
      }
    })
  },

  getIdentityText: function (identityType) {
    return identityType === 1 ? '执行者' : '雇主'
  }
})