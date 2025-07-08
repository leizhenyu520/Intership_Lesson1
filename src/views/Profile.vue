<template>
  <div class="profile-container">
    <el-card class="profile-card">
      <div class="avatar-container">
        <el-avatar :size="100" :src="user.avatar" />
      </div>
      <h2>{{ user.username }}</h2>
      <el-descriptions :column="1" border>
        <el-descriptions-item label="邮箱">{{ user.email }}</el-descriptions-item>
        <el-descriptions-item label="注册时间">{{ user.registerTime }}</el-descriptions-item>
      </el-descriptions>
      <div class="action-buttons">
        <el-button type="primary" @click="goToChat">开始聊天</el-button>
      </div>
    </el-card>
  </div>
</template>

<script>
export default {
  name: 'Profile',
  data() {
    return {
      user: {
        username: '测试用户',
        email: 'test@example.com',
        avatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png',
        registerTime: '2023-01-01'
      }
    }
  },
  methods: {
    goToChat() {
      this.$router.push('/chat')
    }
  }
}
</script>

<style scoped>
.profile-container {
  display: flex;
  justify-content: center;
  padding: 20px;
}

.profile-card {
  width: 500px;
  padding: 20px;
}

.avatar-container {
  display: flex;
  justify-content: center;
  margin-bottom: 20px;
}

h2 {
  text-align: center;
  margin-bottom: 20px;
}

.action-buttons {
  margin-top: 20px;
  display: flex;
  justify-content: center;
}
</style>