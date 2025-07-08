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
            <span class="model-badge recommend">推荐</span>创意生成模型
            <el-icon class="el-icon--right"><ArrowDown /></el-icon>
          </el-button>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item><span class="model-badge recommend">推荐</span>创意生成模型</el-dropdown-item>
              <el-dropdown-item><span class="model-badge vip">VIP</span>高级创意模型</el-dropdown-item>
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
      <h2>创意中心</h2>
    </div>

    <div class="chat-main">
      <div class="history-sidebar" v-if="showHistory">
        <el-button size="small" class="new-chat-btn-full">
          <Plus class="plus-icon" /> 新建创意
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
              <Clock class="menu-icon" /> 创意历史
            </template>
            <el-menu-item index="5-1">故事创作</el-menu-item>
            <el-menu-item index="5-2">营销文案</el-menu-item>
            <el-menu-item index="5-3">产品命名</el-menu-item>
          </el-sub-menu>
        </el-menu>
        <div class="quick-actions">
          <div class="action-title">创意工具</div>
          <div class="action-list">
            <div class="action-item">
              <PenTool class="action-icon" /> 故事创作
            </div>
            <div class="action-item">
              <Document class="action-icon" /> 营销文案
            </div>
            <div class="action-item">
              <EditPen class="action-icon" /> 产品命名
            </div>
            <div class="action-item">
              <HelpFilled class="action-icon" /> 创意点子
            </div>
            <div class="action-item">
              <Language class="action-icon" />  slogan生成
            </div>
          </div>
        </div>
      </div>

      <div class="creative-content">
        <div class="tool-selection">
          <el-card v-for="tool in creativeTools" :key="tool.id" class="tool-card" @click="selectTool(tool.id)">
            <div class="tool-icon"><component :is="tool.icon" /></div>
            <div class="tool-name">{{ tool.name }}</div>
            <div class="tool-desc">{{ tool.description }}</div>
          </el-card>
        </div>

        <div v-if="selectedTool" class="tool-content">
          <div class="tool-header">
            <h3>{{ selectedTool.name }}</h3>
            <el-button @click="resetTool" size="small">返回工具选择</el-button>
          </div>

          <el-form :model="toolForm" class="tool-form">
            <el-form-item label="主题" prop="topic">
              <el-input v-model="toolForm.topic" placeholder="输入{{ selectedTool.placeholder }}" />
            </el-form-item>
            <el-form-item label="详细要求" v-if="selectedTool.hasDetails">
              <el-input v-model="toolForm.details" type="textarea" :rows="4" placeholder="请输入详细要求..." />
            </el-form-item>
            <el-form-item label="风格/类型" v-if="selectedTool.hasStyle">
              <el-select v-model="toolForm.style" placeholder="选择风格/类型">
                <el-option v-for="style in selectedTool.styles" :key="style.value" :label="style.label" :value="style.value" />
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button @click="generateCreative" class="generate-btn">生成创意</el-button>
            </el-form-item>
          </el-form>

          <div class="creative-result" v-if="showResult">
            <div v-if="isGenerating" class="loading-state">
              <el-icon class="is-loading"><Loading /></el-icon>
              <p>正在生成创意...</p>
            </div>
            <div v-else class="result-content">
              <h4>生成结果</h4>
              <div class="result-text">{{ creativeResult }}</div>
              <div class="result-actions">
                <el-button icon="CopyDocument" size="small" @click="copyResult">复制</el-button>
                <el-button icon="RefreshLeft" size="small" @click="regenerateResult">重新生成</el-button>
                <el-button icon="Download" size="small">导出</el-button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { Plus, Message, Picture, Edit, Lightning, Clock, ArrowDown, Loading, PenTool, Document, EditPen, HelpFilled, Language, CopyDocument } from '@element-plus/icons-vue'

export default {
  name: 'CreativeCenter',
  components: {
    Plus, Message, Picture, Edit, Lightning, Clock, ArrowDown, Loading, PenTool, Document, EditPen, HelpFilled, Language, CopyDocument
  },
  data() {
    return {
      showHistory: true,
      creativeTools: [
        {
          id: 'story',
          name: '故事创作',
          description: '生成原创故事、小说情节和角色设定',
          icon: PenTool,
          placeholder: '故事主题或开头',
          hasDetails: true,
          hasStyle: true,
          styles: [
            { label: '童话', value: 'fairy' },
            { label: '科幻', value: 'sci-fi' },
            { label: '冒险', value: 'adventure' },
            { label: '悬疑', value: 'mystery' }
          ]
        },
        {
          id: 'marketing',
          name: '营销文案',
          description: '生成产品描述、广告文案和宣传语',
          icon: Document,
          placeholder: '产品名称或特点',
          hasDetails: true,
          hasStyle: true,
          styles: [
            { label: '活泼', value: 'lively' },
            { label: '专业', value: 'professional' },
            { label: '温馨', value: 'warm' },
            { label: '幽默', value: 'humorous' }
          ]
        },
        {
          id: 'naming',
          name: '产品命名',
          description: '为产品、品牌或项目生成创意名称',
          icon: EditPen,
          placeholder: '产品类型或特点',
          hasDetails: true,
          hasStyle: false
        },
        {
          id: 'ideas',
          name: '创意点子',
          description: '为各种场景提供创新想法和解决方案',
          icon: HelpFilled,
          placeholder: '需要创意的场景',
          hasDetails: true,
          hasStyle: false
        },
        {
          id: 'slogan',
          name: 'Slogan生成',
          description: '创作简洁有力的品牌口号或标语',
          icon: Language,
          placeholder: '品牌或产品核心价值',
          hasDetails: false,
          hasStyle: true,
          styles: [
            { label: '简洁有力', value: 'concise' },
            { label: '押韵易记', value: 'rhyming' },
            { label: '情感共鸣', value: 'emotional' },
            { label: '专业权威', value: 'authoritative' }
          ]
        }
      ],
      selectedTool: null,
      toolForm: {
        topic: '',
        details: '',
        style: ''
      },
      isGenerating: false,
      showResult: false,
      creativeResult: ''
    }
  },
  methods: {
    selectTool(toolId) {
      this.selectedTool = this.creativeTools.find(tool => tool.id === toolId)
      this.toolForm = {
        topic: '',
        details: '',
        style: ''
      }
      this.showResult = false
    },
    resetTool() {
      this.selectedTool = null
      this.showResult = false
    },
    generateCreative() {
      if (!this.toolForm.topic.trim()) {
        this.$message.warning('请输入主题内容')
        return
      }

      this.isGenerating = true
      this.showResult = true
      // 模拟创意生成过程
      setTimeout(() => {
        this.creativeResult = `基于您的需求生成的创意内容：\n\n主题：${this.toolForm.topic}\n${this.toolForm.details ? '详细要求：' + this.toolForm.details + '\n' : ''}${this.selectedTool.hasStyle && this.toolForm.style ? '风格：' + this.toolForm.style + '\n' : ''}\n\n这是AI生成的创意结果示例。在实际应用中，这里会根据您的具体需求生成更丰富、更有创意的内容。`
        this.isGenerating = false
      }, 2000)
    },
    copyResult() {
      navigator.clipboard.writeText(this.creativeResult)
      this.$message.success('结果已复制到剪贴板')
    },
    regenerateResult() {
      this.generateCreative()
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

.creative-content {
  flex: 1;
  padding: 24px;
  overflow-y: auto;
}

.tool-selection {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(220px, 1fr));
  gap: 24px;
  margin-top: 24px;
}

.tool-card {
  height: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 25px 15px;
  cursor: pointer;
  transition: all 0.3s ease;
  border-radius: 8px;
  border: 1px solid #e4e7ed;
  background-color: white;
}

.tool-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 10px 20px rgba(0, 0, 0, 0.05);
}

.tool-icon {
  font-size: 36px;
  color: #0066ff;
  margin-bottom: 15px;
}

.tool-name {
  font-weight: 600;
  margin-bottom: 10px;
  color: #303133;
}

.tool-desc {
  font-size: 12px;
  color: #606266;
  text-align: center;
  line-height: 1.5;
}

.tool-content {
  margin-top: 20px;
}

.tool-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding-bottom: 10px;
  border-bottom: 1px solid #e5e5e5;
}

.tool-form {
  max-width: 800px;
}

.generate-btn {
  background-color: #0066ff;
  color: white;
}

.creative-result {
  margin-top: 20px;
  border: 1px solid #dcdfe6;
  border-radius: 8px;
  padding: 20px;
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
  gap: 15px;
}

.result-text {
  line-height: 1.6;
  color: #303133;
  white-space: pre-wrap;
  padding: 15px;
  background-color: #f5f7fa;
  border-radius: 4px;
}

.result-actions {
  display: flex;
  gap: 10px;
  justify-content: flex-end;
}
</style>