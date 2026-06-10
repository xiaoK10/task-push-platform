const app = getApp()

Page({
  data: {
    taskList: [],
    currentCategory: null,
    searchKeyword: '',
    loading: false,
    page: 1,
    hasMore: true
  },

  onLoad: function () {
    this.loadTasks()
  },

  onPullDownRefresh: function () {
    this.data.page = 1
    this.data.hasMore = true
    this.loadTasks(true)
  },

  onReachBottom: function () {
    if (this.data.hasMore && !this.data.loading) {
      this.data.page++
      this.loadTasks()
    }
  },

  loadTasks: function (refresh = false) {
    if (this.data.loading) return
    
    this.setData({ loading: true })
    
    wx.request({
      url: `${app.globalData.baseUrl}/api/worker/tasks`,
      method: 'GET',
      data: {
        category: this.data.currentCategory || '',
        keyword: this.data.searchKeyword,
        page: this.data.page,
        size: 10
      },
      success: (res) => {
        if (res.data.code === 200) {
          const newTasks = res.data.data
          if (refresh) {
            this.setData({ taskList: newTasks })
          } else {
            this.setData({ taskList: [...this.data.taskList, ...newTasks] })
          }
          this.setData({ hasMore: newTasks.length >= 10 })
        }
      },
      fail: () => {
        wx.showToast({ title: '加载失败', icon: 'none' })
      },
      complete: () => {
        this.setData({ loading: false })
        wx.stopPullDownRefresh()
      }
    })
  },

  onSearchInput: function (e) {
    this.data.searchKeyword = e.detail.value
  },

  handleSearch: function () {
    this.data.page = 1
    this.data.hasMore = true
    this.loadTasks(true)
  },

  selectCategory: function (e) {
    const category = e.currentTarget.dataset.category
    this.setData({ 
      currentCategory: category ? parseInt(category) : null,
      page: 1,
      hasMore: true
    })
    this.loadTasks(true)
  },

  goToTaskDetail: function (e) {
    const taskId = e.currentTarget.dataset.taskId
    wx.navigateTo({
      url: `/pages/task-detail/task-detail?id=${taskId}`
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

  formatTime: function (time) {
    if (!time) return ''
    const date = new Date(time)
    return `${date.getMonth() + 1}/${date.getDate()}`
  }
})