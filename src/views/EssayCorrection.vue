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
            <span class="model-badge recommend">推荐</span>作文批改模型
            <el-icon class="el-icon--right"><ArrowDown /></el-icon>
          </el-button>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item><span class="model-badge recommend">推荐</span>作文批改模型</el-dropdown-item>
              <el-dropdown-item><span class="model-badge vip">VIP</span>高级批改模型</el-dropdown-item>
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
      <h2>作文批改</h2>
    </div>

    <div class="chat-main">
      <div class="history-sidebar" v-if="showHistory">
        <el-button size="small" class="new-chat-btn-full">
          <Plus class="plus-icon" /> 新建批改
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
              <Clock class="menu-icon" /> 批改历史
            </template>
            <el-menu-item index="5-1">三年级春游作文</el-menu-item>
            <el-menu-item index="5-2">五年级我的理想</el-menu-item>
            <el-menu-item index="5-3">初中记叙文</el-menu-item>
          </el-sub-menu>
        </el-menu>
        <div class="quick-actions">
          <div class="action-title">推荐年级</div>
          <div class="action-list">
            <div class="action-item">
              <EditPen class="action-icon" /> 小学低年级
            </div>
            <div class="action-item">
              <EditPen class="action-icon" /> 小学中年级
            </div>
            <div class="action-item">
              <EditPen class="action-icon" /> 小学高年级
            </div>
            <div class="action-item">
              <EditPen class="action-icon" /> 初中
            </div>
            <div class="action-item">
              <EditPen class="action-icon" /> 高中
            </div>
          </div>
        </div>
      </div>

      <div class="correction-content">
        <div class="correction-controls">
          <el-select v-model="grade" placeholder="选择年级">
            <el-option label="小学低年级" value="1-2" />
            <el-option label="小学中年级" value="3-4" />
            <el-option label="小学高年级" value="5-6" />
            <el-option label="初中" value="7-9" />
            <el-option label="高中" value="10-12" />
          </el-select>
          <el-select v-model="type" placeholder="作文类型">
            <el-option label="记叙文" value="narrative" />
            <el-option label="说明文" value="expository" />
            <el-option label="议论文" value="argumentative" />
            <el-option label="应用文" value="practical" />
          </el-select>
        </div>

        <el-input
          v-model="essayContent"
          type="textarea"
          :rows="10"
          placeholder="请粘贴作文内容..."
          class="essay-input"
        />

        <div class="correction-actions">
          <el-button @click="correctEssay" class="correct-btn">开始批改</el-button>
          <el-button @click="clearContent" class="clear-btn">清空内容</el-button>
        </div>

        <div class="correction-result" v-if="showResult">
          <div v-if="isCorrecting" class="loading-state">
            <el-icon class="is-loading"><Loading /></el-icon>
            <p>正在批改中...</p>
          </div>
          <div v-else class="result-content">
            <div class="score-section">
              <div class="score-label">综合评分</div>
              <div class="score-value">{{ score }}分</div>
              <div class="score-stars">★★★★☆</div>
            </div>
            <div class="feedback-section">
              <h4>批改反馈</h4>
              <div class="feedback-content">
                <p>1. 文章结构完整，开头点题，结尾呼应，过渡自然。</p>
                <p>2. 语句通顺，但部分句子存在语法错误，已标注修改。</p>
                <p>3. 用词较为平淡，建议增加修辞手法使文章更生动。</p>
                <p>4. 段落划分合理，逻辑清晰。</p>
              </div>
            </div>
            <div class="corrected-essay">
              <h4>修改建议</h4>
              <p class="corrected-text">{{ correctedEssay }}</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { Plus, Message, Picture, Edit, Lightning, Clock, ArrowDown, Loading, EditPen } from '@element-plus/icons-vue'

export default {
  name: 'EssayCorrection',
  components: {
    Plus, Message, Picture, Edit, Lightning, Clock, ArrowDown, Loading, EditPen
  },
  data() {
    return {
      showHistory: true,
      grade: '',
      type: '',
      essayContent: '',
      isCorrecting: false,
      showResult: false,
      score: 0,
      correctedEssay: ''
    }
  },
  methods: {
    correctEssay() {
      if (!this.essayContent.trim()) {
        this.$message.warning('请输入作文内容')
        return
      }

      this.isCorrecting = true
      this.showResult = true
      // 模拟批改过程
      setTimeout(() => {
        this.score = Math.floor(Math.random() * 10) + 90
        this.correctedEssay = this.essayContent.replace(/\n/g, '<br>').replace(/的/g, '<span style="color: #409eff;">的</span>')
        this.isCorrecting = false
      }, 2000)
    },
    clearContent() {
      this.essayContent = ''
      this.showResult = false
    }
  }
}
</script>

<style scoped>
@import url('../styles/shared-styles.css');

/* 页面特定样式 */
.chat-container, .top-header, .logo, .header-actions, .chat-header, .chat-main, .history-sidebar, .sidebar-menu, .menu-item, .quick-actions, .action-item {
  @extend .chat-container, .top-header, .logo, .header-actions, .chat-header, .chat-main, .history-sidebar, .sidebar-menu, .menu-item, .quick-actions, .action-item;
}

.correction-content {
  flex: 1;
  padding: 24px;
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.correction-controls {
  display: flex;
  gap: 16px;
  align-items: center;
  width: 100%;
  padding: 16px;
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
}

.essay-input {
  width: 100%;
  border-radius: 8px;
  border: 1px solid #dcdfe6;
  min-height: 200px;
  display: block;
  visibility: visible;
}

.correction-actions {
  display: flex;
  gap: 15px;
  justify-content: flex-end;
}

.correct-btn {
  background-color: #0066ff;
  color: white;
}

.clear-btn {
  background-color: white;
  color: #606266;
  border: 1px solid #dcdfe6;
}

.correction-result {
  background-color: white;
  border-radius: 8px;
  padding: 24px;
  margin-top: 10px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
}

.loading-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 15px;
  color: #606266;
  padding: 40px 0;
}

.result-content {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.score-section {
  display: flex;
  align-items: center;
  gap: 20px;
  padding-bottom: 15px;
  border-bottom: 1px solid #e5e5e5;
}

.score-label {
  font-size: 14px;
  color: #606266;
}

.score-value {
  font-size: 24px;
  font-weight: bold;
  color: #0066ff;
}

.score-stars {
  color: #ffd04b;
  font-size: 18px;
}

.feedback-section h4, .corrected-essay h4 {
  font-size: 16px;
  margin-bottom: 10px;
  color: #303133;
}

.feedback-content {
  line-height: 1.6;
  color: #606266;
}

.corrected-text {
  line-height: 1.6;
  color: #303133;
  white-space: pre-wrap;
}
</style>