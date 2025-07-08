<template>
  <div class="chat-container">
    <div class="background-decoration"></div>
    <div class="floating-elements"></div>
    <div class="top-header">
      <div class="logo">
          <span class="logo-icon">悟音</span>
          <span class="logo-text">智能万物AI系统</span>
        </div>
      <div class="header-actions">
        <el-dropdown class="model-selector">
          <el-button size="small" class="model-btn">
            <span class="model-badge">普通</span>DeepSeek-R1
            <el-icon class="el-icon--right"><ArrowDown /></el-icon>
          </el-button>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item><span class="model-badge">普通</span>DeepSeek-R1</el-dropdown-item>
              <el-dropdown-item><span class="model-badge vip">VIP</span>GPT-4.0</el-dropdown-item>
              <el-dropdown-item><span class="model-badge recommend">推荐</span>GPT-3.5</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
        <el-button size="small" class="vip-btn">
          <span class="vip-icon">💎</span> 开通会员
        </el-button>
        <el-avatar :size="36" class="user-avatar-mini" />
        <el-button size="small" class="login-btn">登录/注册</el-button>
        <el-button size="small" icon="Settings" class="nav-btn" />
      </div>
    </div>
    
    <div class="chat-header">
      <h2>AI 聊天助手</h2>
      
    </div>

    <div class="chat-main">
      <div class="history-sidebar" v-if="showHistory">
        <el-button size="small" class="new-chat-btn-full">
          <Plus class="plus-icon" /> 开始新对话
        </el-button>
        <el-menu class="sidebar-menu" router>
          <el-menu-item index="/chat" class="menu-item">
            <Message class="menu-icon" /> AI聊天
          </el-menu-item>
          <el-menu-item index="/ai-painting" class="menu-item">
            <Picture class="menu-icon" /> AI绘画
          </el-menu-item>
          <el-menu-item index="/essay-correction" class="menu-item">
            <Edit class="menu-icon" /> 作文批改
          </el-menu-item>
          <el-menu-item index="/creative-center" class="menu-item">
            <Lightning class="menu-icon" /> 创意中心
          </el-menu-item>
          <el-sub-menu index="5">
            <template #title>
              <Clock class="menu-icon" /> 历史对话
            </template>
            <el-menu-item index="5-1">如何学习Vue</el-menu-item>
            <el-menu-item index="5-2">AI助手介绍</el-menu-item>
            <el-menu-item index="5-3">最新技术趋势</el-menu-item>
          </el-sub-menu>
        </el-menu>
        <div class="quick-actions">
          <div class="action-title">推荐场景</div>
          <div class="action-list">
            <div class="action-item">
      <Document class="action-icon" /> 春天散文
    </div>
    <div class="action-item">
      <EditPen class="action-icon" /> 小学作文
    </div>
    <div class="action-item">
      <Calculator class="action-icon" /> 数学应用
    </div>
    <div class="action-item">
      <Language class="action-icon" /> 英语短文
    </div>
    <div class="action-item">
      <Picture class="action-icon" /> 古风绘画
    </div>
    <div class="action-item">
      <Lightning class="action-icon" /> 学习效率
    </div>
          </div>
        </div>
        <el-scrollbar>
          <el-menu class="history-menu">
            <el-menu-item
              v-for="(item, index) in historyChats"
              :key="index"
              @click="loadHistory(index)"
            >
              {{ item.title }}
            </el-menu-item>
          </el-menu>
        </el-scrollbar>
      </div>

      <div class="chat-content">
        <div v-if="messages.length === 0" class="empty-chat">
          
          <h3>开始与AI助手对话</h3>
          <p class="empty-subtitle">尝试以下示例或输入您的问题</p>
          <div class="sample-prompts">
            <el-tag class="sample-tag" @click="sendSample('写一篇春天的散文300字')">写一篇春天的散文300字</el-tag>
            <el-tag class="sample-tag" @click="sendSample('写一篇三年级的小学作文300字')">写一篇三年级的小学作文300字</el-tag>
            <el-tag class="sample-tag" @click="sendSample('转出出三年级五年级的数学应用题')">转出出三年级五年级的数学应用题</el-tag>
            <el-tag class="sample-tag" @click="sendSample('写一篇自我介绍英语短文')">写一篇自我介绍英语短文</el-tag>
            <el-tag class="sample-tag" @click="sendSample('请帮我画一个古风美女，穿汉服，高画质')">请帮我画一个古风美女，穿汉服，高画质</el-tag>
            <el-tag class="sample-tag" @click="sendSample('如何提高学习效率？5个实用方法')">如何提高学习效率？5个实用方法</el-tag>
          </div>
        </div>

        <el-scrollbar v-else class="message-container">
          <div
            v-for="(message, index) in messages"
            :key="index"
            class="message"
            :class="[message.role]"
          >
            <div class="message-content">
              <el-avatar :size="40" :src="getAvatar(message.role)" />
              <div class="message-text">
                <div class="message-header">
                  <span class="message-role">{{ message.role === 'user' ? '你' : 'AI助手' }}</span>
                  <span class="message-time">{{ new Date().toLocaleTimeString() }}</span>
                </div>
                <div class="message-bubble">
                  {{ message.content }}
                </div>
              </div>
            </div>
          </div>

          <div v-if="isLoading" class="loading-indicator">
            <el-icon class="is-loading">
              <Loading />
            </el-icon>
            AI正在思考...
          </div>
        </el-scrollbar>

        <div class="input-area">
          <input
            v-model="inputMessage"
            placeholder="输入消息..."
            @keyup.enter="sendMessage"
            :disabled="isLoading"
            style="width: 80%; padding: 8px; border: 1px solid #dcdfe6; border-radius: 4px;"
          >
          <button
            @click="sendMessage"
            :disabled="!inputMessage || isLoading"
            style="margin-left: 10px; padding: 8px 16px; background-color: #0066ff; color: white; border: none; border-radius: 4px; cursor: pointer;"
          >
            发送
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ElInput, ElButton } from 'element-plus'
import { Loading, Plus, Message, HelpFilled, PenTool, Settings, Document, EditPen, Calculator, Language, Picture, Lightning } from '@element-plus/icons-vue'

export default {
  name: 'Chat',
  components: { Loading, ElInput, ElButton },
  data() {
    return {
      showHistory: true,
      inputMessage: '',
      messages: [],
      isLoading: false,
      historyChats: [
        { title: '如何学习Vue', messages: [] },
        { title: 'AI助手介绍', messages: [] },
        { title: '最新技术趋势', messages: [] }
      ]
    }
  },
  methods: {
    getAvatar(role) {
      return role === 'user'
        ? 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'
        : 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png'
    },
    sendMessage() {
      if (!this.inputMessage.trim()) return

      const userMessage = {
        role: 'user',
        content: this.inputMessage
      }

      this.messages.push(userMessage)
      this.inputMessage = ''

      this.isLoading = true
      // 模拟API调用
      setTimeout(() => {
        const aiMessage = {
          role: 'assistant',
          content: '这是AI助手的模拟回复。在实际应用中，这里会调用DeepSeekR1的API获取真实回复。'
        }
        this.messages.push(aiMessage)
        this.isLoading = false
      }, 1000)
    },
    sendSampleMessage() {
      this.inputMessage = '你好，AI助手！'
      this.sendMessage()
    },
    loadHistory(index) {
      // 模拟加载历史对话
      this.messages = this.historyChats[index].messages
    }
  }
}
</script>

<style scoped>
.chat-container {
  height: 100vh;
  display: flex;
  flex-direction: column;
  background-color: white;
  overflow: hidden;
  position: relative;
}

.top-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 30px;
  background-color: white;
  position: relative;
  z-index: 10;
}

.header-actions {
  display: flex;
  align-items: center;
  gap: 12px;
}

.model-badge {
  display: inline-block;
  padding: 2px 6px;
  border-radius: 10px;
  font-size: 12px;
  margin-right: 5px;
  color: white;
  background-color: #409eff;
}

.model-badge.vip {
  background-color: #ff7d00;
}

.model-badge.recommend {
  background-color: #67c23a;
}

.vip-icon {
  margin-right: 5px;
}

.logo {
  display: flex;
  align-items: center;
  font-weight: 600;
}

.logo-icon {
  font-size: 20px;
  color: #409eff;
  margin-right: 5px;
  font-weight: bold;
}

.logo-text {
  font-size: 16px;
  color: #303133;
}

.nav-actions {
  display: flex;
  align-items: center;
  gap: 10px;
}

.nav-btn {
  background: transparent;
  color: #606266;
}

.input-area {
  position: relative;
  margin: 2px auto 500px;
  max-width: 800px;
  width: 90%;
  padding: 15px;
  background-color: white;
  border: 1px solid #e5e5e5;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
  z-index: 1;
}

.model-btn {
  background-color: #f0f7ff;
  color: #409eff;
  border: 1px solid #e6f0ff;
}

.el-dropdown-menu {
  border-radius: 10px;
  box-shadow: 0 4px 15px rgba(137, 210, 253, 0.15);
  border: 1px solid #e6f0ff;
  padding: 8px 0;
}

.el-dropdown-menu__item {
  padding: 10px 16px;
  transition: all 0.3s;
}

.el-dropdown-menu__item:hover {
  background-color: #f0f7ff;
  color: #409eff;
}

.el-dropdown-menu__item.selected {
  background-color: #e6f0ff;
  color: #409eff;
  font-weight: 500;
}

.vip-btn {
  background-color: #ff7d00;
  color: white;
}

.user-avatar-mini {
  border: 1px solid #e6f0ff;
}

.login-btn {
  background: transparent;
  color: #606266;
}

.welcome-section {
  padding: 15px 20px;
  background-color: white;
  display: flex;
  justify-content: space-between;
  align-items: center;
  position: relative;
}

.welcome-content h2 {
  margin: 0;
  font-size: 24px;
  color: #333;
  background: linear-gradient(90deg, #409eff, #67c23a);
  -webkit-background-clip: text;
  background-clip: text;
  color: transparent;
  position: relative;
  z-index: 2;
}

.ai-mascot {
  position: relative;
  z-index: 2;
}

.mascot-image {
  width: 100px;
  height: 100px;
  transform: translateY(5px);
  animation: float 3s ease-in-out infinite;
}

@keyframes float {
  0% { transform: translateY(5px); }
  50% { transform: translateY(-10px); }
  100% { transform: translateY(5px); }
}

.user-avatar {
  border: 2px solid #e6f0ff;
  box-shadow: 0 2px 8px rgba(137, 210, 253, 0.3);
}

.chat-header {
  padding: 12px 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  background-color: white;
  border-bottom: 1px solid #f0f0f0;
}

.chat-main {
  height: calc(100vh - 180px);
  overflow: hidden;
}

.chat-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.history-sidebar {
  width: 280px;
  flex-shrink: 0;
  background-color: white;
  border-right: 1px solid rgba(153, 187, 255, 0.3);
  display: flex;
  flex-direction: column;
  box-shadow: 2px 0 15px rgba(137, 210, 253, 0.15);
  z-index: 2;
  overflow: hidden;
}

.chat-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.sidebar-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px;
  border-bottom: 1px solid rgba(153, 187, 255, 0.3);
}

.sidebar-header h3 {
  margin: 0;
  font-size: 16px;
}

.new-chat-btn {
  background-color: #e6f0ff;
  color: #409eff;
  padding: 5px 10px;
}

.new-chat-btn-full {
  width: 90%;
  margin: 15px auto;
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 5px;
  background-color: #409eff;
  color: white;
}

.sidebar-menu {
  border-right: none;
  margin-top: 10px;
  background-color: transparent;
}

.sidebar-menu .el-menu-item {
  height: 42px;
  line-height: 42px;
  border-radius: 0 18px 18px 0;
  margin-bottom: 4px;
  transition: all 0.2s;
  position: relative;
  overflow: hidden;
}

.sidebar-menu .el-menu-item:hover {
  background-color: #f0f7ff;
  padding-left: 28px;
}

.sidebar-menu .el-menu-item.is-active {
  background-color: #e6f0ff;
  color: #409eff;
  font-weight: 500;
}

.sidebar-menu .el-menu-item.is-active::before {
  content: '';
  position: absolute;
  left: 0;
  top: 0;
  height: 100%;
  width: 3px;
  background-color: #409eff;
}

.sidebar-menu .el-menu-item:hover {
  background-color: #f0f7ff;
}

.sidebar-menu .el-menu-item.is-active {
  background-color: #e6f0ff;
  color: #409eff;
}

.sidebar-menu .el-submenu .el-submenu__title {
  height: 45px;
  line-height: 45px;
  border-radius: 0 20px 20px 0;
}

.sidebar-menu .el-submenu .el-submenu__title:hover {
  background-color: #f0f7ff;
}

.menu-item {
  padding-left: 25px;
}

.menu-icon {
  margin-right: 8px;
  color: #409eff;
}

.quick-actions {
  padding: 0;
  margin-top: auto;
  margin-bottom: 20px;
}

.action-title {
  font-size: 14px;
  color: #606266;
  margin: 20px 0 10px 25px;
  font-weight: 500;
}

.action-card {
  border-radius: 12px;
  border: 1px solid #e6f0ff;
  box-shadow: 0 4px 15px rgba(137, 210, 253, 0.1);
  transition: all 0.3s;
  overflow: hidden;
}

.action-card .el-card__body {
  padding: 0;
}

.quick-actions .action-item {
  padding: 0 25px;
  height: 42px;
  line-height: 42px;
  cursor: pointer;
  border-radius: 0 18px 18px 0;
  transition: all 0.2s;
  display: flex;
  align-items: center;
  margin-bottom: 4px;
  font-size: 14px;
  padding-left: 25px;
}

.quick-actions .action-item:hover {
  background-color: #f0f7ff;
  color: #409eff;
  padding-left: 28px;
}

.quick-actions .action-item .action-icon {
  margin-right: 8px;
  color: #409eff;
  font-size: 16px;
}

.quick-actions .action-title {
  font-size: 14px;
  color: #606266;
  margin: 20px 0 10px 25px;
  font-weight: 500;
}

.quick-actions .action-item:last-child {
  border-bottom: none;
}

.quick-actions .action-card:hover {
  transform: translateY(-3px);
  box-shadow: 0 10px 20px rgba(137, 210, 253, 0.15);
}

.history-menu {
  border-right: none;
}

.chat-content {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.empty-chat {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  padding: 20px;
}

.empty-illustration {
  display: none;
}

.ai-illustration {
  width: 120px;
  height: 120px;
  opacity: 0.9;
}

.empty-chat h3 {
  margin: 0 0 10px 0;
  color: #1f2329;
  font-size: 18px;
  font-weight: 500;
}

.empty-chat .ai-illustration {
  width: 150px;
  height: 150px;
  margin-bottom: 25px;
}

.empty-subtitle {
  color: #909399;
  margin-bottom: 30px;
  font-size: 15px;
}

.empty-subtitle {
  color: #909399;
  margin-bottom: 20px;
}

.sample-prompts {
  display: flex;
  flex-wrap: wrap;
  gap: 15px;
  justify-content: center;
  max-width: 800px;
  margin-top: 30px;
  padding: 0 20px;
  margin-bottom: 20px;
}

.sample-tag {
  background-color: transparent;
  color: #409eff;
  border: none;
  cursor: pointer;
  transition: all 0.2s;
  padding: 8px 16px;
  border-radius: 20px;
  font-size: 14px;
  box-shadow: none;
}

.sample-tag:hover {
  background-color: #f0f7ff;
  color: #409eff;
  transform: translateY(0);
  box-shadow: none;
}

.message-container {
  flex: 1;
  padding: 20px;
  padding-bottom: 80px;
  overflow-y: auto;
}

.message {
  margin-bottom: 15px;
}

.message-content {
  display: flex;
}

.message.assistant .message-content {
  flex-direction: row;
}

.message.user .message-content {
  flex-direction: row-reverse;
}

.message-text {
  margin: 0 10px;
  max-width: 70%;
}

.message-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 5px;
}

.message-role {
  font-size: 12px;
  color: #909399;
}

.message-time {
  font-size: 12px;
  color: #c0c4cc;
}

.message-bubble {
  padding: 10px 16px;
  border-radius: 16px 16px 16px 4px;
  background-color: white;
  line-height: 1.6;
  font-size: 14px;
  border: 1px solid #dcdfe6;
}

.message-bubble:hover {
  box-shadow: 0 6px 16px rgba(137, 210, 253, 0.2);
}

.message.user .message-bubble {
  border-radius: 18px 18px 4px 18px;
}

.message.user .message-bubble {
  background-color: #0066ff;
  color: white;
}

.input-area {
  padding: 15px 20px;
  border-top: 1px solid rgba(153, 187, 255, 0.3);
  background-color: white;
  box-shadow: 0 -2px 10px rgba(0, 0, 0, 0.03);
}

.input-area .el-input {
  border-radius: 20px 0 0 20px;
  border-right: none;
  box-shadow: none;
  transition: none;
  border: 1px solid #dcdfe6;
}

.input-area .el-input:focus-within {
  border-color: #409eff;
  box-shadow: 0 0 0 3px rgba(64, 158, 255, 0.1);
}

.input-area .el-button {
  border-radius: 0 20px 20px 0;
  padding: 0 20px;
  background-color: #409eff;
  border: none;
  transition: none;
  box-shadow: none;
}

.input-area .el-button:hover {
  background-color: #3390e9;
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(64, 158, 255, 0.25);
}

.el-input:focus-within {
  border-color: #409eff;
  box-shadow: 0 0 0 3px rgba(64, 158, 255, 0.2);
}

.el-button {
  border-radius: 20px;
  padding: 8px 20px;
  background-color: #409eff;
  border: none;
  transition: all 0.3s;
}

.el-button:hover {
  background-color: #3088e6;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(64, 158, 255, 0.2);
}

.el-input {
  border-radius: 25px;
  border: 1px solid #e6f0ff;
  box-shadow: 0 2px 5px rgba(137, 210, 253, 0.1);
}

.el-button {
  border-radius: 20px;
  padding: 8px 20px;
  background-color: #409eff;
  border: none;
}

.loading-indicator {
  display: flex;
  align-items: center;
  justify-content: center;
  color: #909399;
  padding: 10px;
}

</style>