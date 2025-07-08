<template>
  <div class="chat-container">
    <div class="top-header">
      <div class="logo">
          <span class="logo-icon">悟音</span>
          <span class="logo-text">智能万物AI系统</span>
        </div>
      <div class="header-actions">
        <el-dropdown class="model-selector">
          <el-button size="small" class="model-btn">
            <span class="model-badge recommend">推荐</span>Stable Diffusion
            <el-icon class="el-icon--right"><ArrowDown /></el-icon>
          </el-button>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item><span class="model-badge recommend">推荐</span>Stable Diffusion</el-dropdown-item>
              <el-dropdown-item><span class="model-badge vip">VIP</span>Midjourney</el-dropdown-item>
              <el-dropdown-item><span class="model-badge">普通</span>DALL-E</el-dropdown-item>
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
      <h2>AI 绘画</h2>
    </div>

    <div class="chat-main">
      <div class="history-sidebar" v-if="showHistory">
        <el-button size="small" class="new-chat-btn-full">
          <Plus class="plus-icon" /> 开始新创作
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
              <Clock class="menu-icon" /> 历史作品
            </template>
            <el-menu-item index="5-1">古风美女</el-menu-item>
            <el-menu-item index="5-2">科幻城市</el-menu-item>
            <el-menu-item index="5-3">自然风景</el-menu-item>
          </el-sub-menu>
        </el-menu>
        <div class="quick-actions">
          <div class="action-title">推荐风格</div>
          <div class="action-list">
            <div class="action-item">
              <Picture class="action-icon" /> 古风
            </div>
            <div class="action-item">
              <Picture class="action-icon" /> 二次元
            </div>
            <div class="action-item">
              <Picture class="action-icon" /> 写实
            </div>
            <div class="action-item">
              <Picture class="action-icon" /> 科幻
            </div>
            <div class="action-item">
              <Picture class="action-icon" /> 水彩
            </div>
            <div class="action-item">
              <Picture class="action-icon" /> 油画
            </div>
          </div>
        </div>
      </div>

      <div class="painting-content">
        <div class="painting-controls">
          <el-input
            v-model="prompt"
            placeholder="输入绘画描述..."
            class="painting-prompt"
          />
          <el-select v-model="style" placeholder="选择风格">
            <el-option label="古风" value="ancient" />
            <el-option label="二次元" value="anime" />
            <el-option label="写实" value="realistic" />
            <el-option label="科幻" value="sci-fi" />
            <el-option label="水彩" value="watercolor" />
            <el-option label="油画" value="oil" />
          </el-select>
          <el-button @click="generateImage" class="generate-btn">生成图片</el-button>
        </div>

        <div class="painting-result">
          <div v-if="isGenerating" class="loading-state">
            <el-icon class="is-loading"><Loading /></el-icon>
            <p>正在生成图片...</p>
          </div>
          <div v-else-if="imageUrl" class="image-container">
            <img :src="imageUrl" alt="AI生成图片" class="generated-image" />
            <div class="image-actions">
              <el-button icon="Download" size="small">下载</el-button>
              <el-button icon="RefreshLeft" size="small">重新生成</el-button>
              <el-button icon="Edit" size="small">调整</el-button>
            </div>
          </div>
          <div v-else class="empty-state">
            <Picture class="empty-icon" />
            <h3>开始AI绘画创作</h3>
            <p>输入描述词，选择风格，生成专属画作</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { Plus, Message, Picture, Edit, Lightning, Clock, ArrowDown, Loading } from '@element-plus/icons-vue'

export default {
  name: 'AIPainting',
  components: {
    Plus, Message, Picture, Edit, Lightning, Clock, ArrowDown, Loading
  },
  data() {
    return {
      showHistory: true,
      prompt: '',
      style: '',
      imageUrl: '',
      isGenerating: false
    }
  },
  methods: {
    generateImage() {
      if (!this.prompt.trim()) {
        this.$message.warning('请输入绘画描述')
        return
      }

      this.isGenerating = true
      // 模拟图片生成过程
      setTimeout(() => {
        this.imageUrl = 'https://picsum.photos/800/600?random=' + Math.random()
        this.isGenerating = false
      }, 3000)
    }
  }
}
</script>

<style scoped>
@import url('../styles/shared-styles.css');

/* 页面特定样式 */
.chat-container, .top-header, .logo, .header-actions, .chat-header, .chat-main, .history-sidebar, .sidebar-menu, .menu-item, .quick-actions, .action-item {
  /* 由于当前 CSS 不支持 @extend，这里需要手动复制对应样式或使用预处理器。
     假设这些类的样式需要复用，这里仅作占位，实际项目中需补充具体样式 */
  /* 复用 Chat.vue 中的样式 */
  /* 以下样式需根据 Chat.vue 中的实际样式进行补充 */
  /* 示例样式仅作占位，非实际复用样式 */
  display: flex;
  box-sizing: border-box;
  /* 请根据实际需求补充更多样式 */
}

.painting-content {
  flex: 1;
  padding: 24px;
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.painting-controls {
  display: flex;
  gap: 16px;
  align-items: center;
  padding: 16px;
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
}

.painting-prompt {
  flex: 1;
}

.generate-btn {
  background-color: #0066ff;
  color: white;
}

.painting-result {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  background-color: white;
  border-radius: 8px;
  padding: 24px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
}

.loading-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 15px;
  color: #606266;
}

.image-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 15px;
}

.generated-image {
  max-width: 100%;
  max-height: 60vh;
  border-radius: 8px;
}

.image-actions {
  display: flex;
  gap: 10px;
}

.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 15px;
  color: #606266;
}

.empty-icon {
  font-size: 48px;
  color: #c0c4cc;
}
</style>