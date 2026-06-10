const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true,
  devServer: {
    host: '0.0.0.0',
    port: 8081,
    hot: false,
    liveReload: false,
    client: {
      overlay: false,
      progress: false,
      logging: 'none',
      webSocketTransport: 'sockjs'
    },
    webSocketServer: false,
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true
      }
    }
  }
})