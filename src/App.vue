<template>
  <div id="app">
    <el-container>
      <el-header v-if="$route.meta.requiresAuth">
        <el-menu
          mode="horizontal"
          :default-active="activeIndex"
          @select="handleSelect"
        >
          <el-menu-item index="profile">个人中心</el-menu-item>
          <el-menu-item index="chat">AI聊天</el-menu-item>
          <el-menu-item index="logout" style="float: right">退出登录</el-menu-item>
        </el-menu>
      </el-header>
      <el-main>
        <router-view />
      </el-main>
    </el-container>
  </div>
</template>

<script>
export default {
  name: 'App',
  computed: {
    activeIndex() {
      return this.$route.name.toLowerCase()
    }
  },
  methods: {
    handleSelect(key) {
      if (key === 'logout') {
        // 退出登录逻辑
        this.$router.push('/login')
      } else {
        this.$router.push({ name: key })
      }
    }
  }
}
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}

.el-header {
  padding: 0;
}
</style>