const app = getApp()

Page({
  data: {
    disputeType: null,
    content: '',
    images: [],
    orderId: null
  },

  onLoad: function (options) {
    this.data.orderId = options.orderId
  },

  selectType: function (e) {
    this.setData({ disputeType: parseInt(e.currentTarget.dataset.type) })
  },

  onContentInput: function (e) {
    this.data.content = e.detail.value
  },

  chooseImage: function () {
    wx.chooseImage({
      count: 9,
      success: (res) => {
        this.data.images = [...this.data.images, ...res.tempFilePaths]
      }
    })
  },

  handleSubmit: function () {
    if (!this.data.disputeType) {
      wx.showToast({ title: '请选择申诉类型', icon: 'none' })
      return
    }
    if (!this.data.content.trim()) {
      wx.showToast({ title: '请填写申诉内容', icon: 'none' })
      return
    }

    const userInfo = wx.getStorageSync('userInfo')

    wx.request({
      url: `${app.globalData.baseUrl}/api/worker/${userInfo.id}/disputes`,
      method: 'POST',
      data: {
        orderId: this.data.orderId,
        type: this.data.disputeType,
        content: this.data.content,
        evidence: JSON.stringify(this.data.images)
      },
      success: (res) => {
        if (res.data.code === 200) {
          wx.showToast({ title: '申诉提交成功', icon: 'success' })
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
  }
})