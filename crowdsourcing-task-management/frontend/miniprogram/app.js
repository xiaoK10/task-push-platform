App({
  onLaunch: function () {
    this.login()
  },

  onShow: function () {
    console.log('App Show')
  },

  onHide: function () {
    console.log('App Hide')
  },

  login: function() {
    wx.login({
      success: (res) => {
        if (res.code) {
          wx.request({
            url: 'https://api.aieppay.com/api/auth/login',
            method: 'POST',
            data: { code: res.code },
            success: (result) => {
              if (result.data.code === 200) {
                this.globalData.userInfo = result.data.data
                wx.setStorageSync('userInfo', result.data.data)
              }
            },
            fail: () => {
              console.log('登录失败')
            }
          })
        }
      }
    })
  },

  globalData: {
    userInfo: null,
    baseUrl: 'https://api.aieppay.com'
  }
})