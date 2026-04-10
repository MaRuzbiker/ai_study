<template>
  <div class="resume-generate">
    <el-row :gutter="16">
      <!-- 左侧：简历上传 -->
      <el-col :span="12">
        <el-card class="upload-card">
          <template #header>
            <div class="card-header">
              <span>AI 简历识别</span>
              <div class="header-actions">
                <el-button size="small" @click="showHistory = !showHistory">
                  {{ showHistory ? '隐藏历史' : '历史记录' }}
                </el-button>
              </div>
            </div>
          </template>

          <!-- 历史记录 -->
          <div v-if="showHistory && suggestionHistory.length > 0" class="history-list">
            <div
              v-for="(item, index) in suggestionHistory"
              :key="index"
              class="history-item"
              :class="{ active: index === currentHistoryIndex }"
              @click="loadHistorySuggestion(index)"
            >
              <div class="history-time">{{ item.time }}</div>
              <div class="history-preview">{{ item.content.substring(0, 50) }}...</div>
            </div>
          </div>

          <!-- 文件上传区域 -->
          <div class="upload-section">
            <el-upload
              class="resume-uploader"
              drag
              action="#"
              :auto-upload="false"
              :on-change="handleFileChange"
              :show-file-list="false"
              accept=".pdf,.doc,.docx,.txt"
            >
              <el-icon class="el-icon--upload"><UploadFilled /></el-icon>
              <div class="el-upload__text">
                拖拽文件到此处或<em>点击上传</em>
              </div>
              <template #tip>
                <div class="el-upload__tip">
                  支持 PDF、Word、TXT 格式，文件大小不超过 10MB
                </div>
              </template>
            </el-upload>

            <div v-if="selectedFile" class="file-info">
              <el-tag size="small">{{ selectedFile.name }}</el-tag>
              <el-button type="danger" size="small" plain @click="clearFile">移除</el-button>
            </div>

            <!-- 手动填写简历信息 -->
            <div v-if="!selectedFile" class="manual-form">
              <el-form :model="resumeForm" label-width="80px" size="default">
                <el-form-item label="姓名" required>
                  <el-input v-model="resumeForm.name" placeholder="请输入姓名" maxlength="30" />
                </el-form-item>
                <el-form-item label="目标职位">
                  <el-input v-model="resumeForm.position" placeholder="例如：前端开发工程师" maxlength="50" />
                </el-form-item>
                <el-form-item label="工作年限">
                  <el-input v-model="resumeForm.workYears" placeholder="例如：3年" maxlength="20" />
                </el-form-item>
                <el-form-item label="技能清单">
                  <el-input v-model="resumeForm.skills" placeholder="用逗号分隔，如：Vue, JavaScript, TypeScript" maxlength="200" />
                </el-form-item>
                <el-form-item label="工作经历">
                  <el-input
                    v-model="resumeForm.experience"
                    type="textarea"
                    :rows="3"
                    placeholder="简要描述工作经历"
                    maxlength="500"
                  />
                </el-form-item>
              </el-form>
            </div>

            <div style="margin-top: 20px;">
              <el-button
                type="primary"
                :loading="analyzing"
                @click="handleAnalyze"
                :disabled="!canAnalyze"
                style="width: 100%;"
              >
                <span v-if="!analyzing">🔍 开始分析</span>
                <span v-else>AI 正在分析中...</span>
              </el-button>
            </div>
          </div>
        </el-card>
      </el-col>

      <!-- 右侧：学习建议 -->
      <el-col :span="12">
        <el-card class="suggestion-card">
          <template #header>
            <div class="card-header">
              <span>AI 学习建议</span>
              <div class="header-actions">
                <el-button size="small" type="primary" @click="loadSuggestion" :loading="suggestionLoading">
                  重新生成
                </el-button>
              </div>
            </div>
          </template>

          <div class="suggestion-content" :class="{ 'with-history': showHistory && suggestionHistory.length > 0 }">
            <div v-if="suggestion" class="suggestion-text">{{ suggestion }}</div>
            <el-empty v-else description="上传简历或填写信息后点击开始分析" />
          </div>

          <!-- 任务拆分计划 -->
          <el-divider v-if="planResult" />

          <div v-if="planResult" class="plan-result">
            <div class="plan-summary" v-if="planResult.summary">
              <h4>学习计划总结</h4>
              <p>{{ planResult.summary }}</p>
            </div>
            <div class="plan-tasks">
              <h4>学习任务列表</h4>
              <el-table :data="planResult.tasks" style="margin-top: 12px;">
                <el-table-column prop="title" label="任务标题" width="200" />
                <el-table-column label="任务描述" min-width="200">
                  <template #default="{ row }">
                    <el-tooltip
                      effect="dark"
                      :content="row.description"
                      placement="top"
                      :popper-style="{ maxWidth: '300px' }"
                    >
                      <div class="description-cell">
                        {{ row.description }}
                      </div>
                    </el-tooltip>
                  </template>
                </el-table-column>
                <el-table-column prop="suggestedHours" label="建议时长" width="100">
                  <template #default="{ row }">{{ row.suggestedHours }} 小时</template>
                </el-table-column>
                <el-table-column label="操作" width="120">
                  <template #default="{ row }">
                    <el-button size="small" type="primary" @click="createTaskFromPlan(row)">
                      创建任务
                    </el-button>
                  </template>
                </el-table-column>
              </el-table>
              <div style="margin-top: 12px;">
                <el-button type="success" @click="createAllTasks">
                  一键创建所有任务
                </el-button>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script lang="ts" setup>
import { ref, reactive, computed } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { UploadFilled } from '@element-plus/icons-vue'
import { analyzeResume, generateTaskPlan } from '@/api/ai'
import { createTask } from '@/api/task'
import type { AiTaskPlanResponse, AiPlannedTask } from '@/types/api'

const router = useRouter()

// 本地存储 key
const STORAGE_SUGGESTION_HISTORY = 'resume_suggestion_history'
const STORAGE_CURRENT_SUGGESTION = 'resume_current_suggestion'

// 状态管理
const selectedFile = ref<File | null>(null)
const analyzing = ref(false)
const suggestionLoading = ref(false)
const suggestion = ref('')
const planResult = ref<AiTaskPlanResponse | null>(null)
const showHistory = ref(false)
const currentHistoryIndex = ref(-1)

// 手动填写表单
const resumeForm = reactive({
  name: '',
  position: '',
  workYears: '',
  skills: '',
  experience: ''
})

// 历史记录
interface SuggestionHistoryItem {
  time: string;
  content: string;
}

const suggestionHistory = ref<SuggestionHistoryItem[]>([])

// 计算属性
const canAnalyze = computed(() => {
  return selectedFile.value || (resumeForm.name.trim() && resumeForm.position.trim())
})

// 文件上传处理
const handleFileChange = (file: any) => {
  selectedFile.value = file.raw
}

const clearFile = () => {
  selectedFile.value = null
}

// 保存历史记录
const saveSuggestionHistory = () => {
  const history: SuggestionHistoryItem[] = [
    {
      time: new Date().toLocaleString(),
      content: suggestion.value
    },
    ...suggestionHistory.value
  ]
  suggestionHistory.value = history.slice(0, 20)
  localStorage.setItem(STORAGE_SUGGESTION_HISTORY, JSON.stringify(suggestionHistory.value))
  localStorage.setItem(STORAGE_CURRENT_SUGGESTION, suggestion.value)
  currentHistoryIndex.value = 0
}

// 加载历史记录
const loadHistorySuggestion = (index: number) => {
  if (index < 0 || index >= suggestionHistory.value.length) return
  currentHistoryIndex.value = index
  suggestion.value = suggestionHistory.value[index].content
  localStorage.setItem(STORAGE_CURRENT_SUGGESTION, suggestion.value)
}

// 分析简历
const handleAnalyze = async () => {
  analyzing.value = true
  try {
    let data: any
    
    if (selectedFile.value) {
      // 文件上传分析
      const formData = new FormData()
      formData.append('resume_file', selectedFile.value)
      data = { file: selectedFile.value }
    } else {
      // 手动填写分析
      data = {
        name: resumeForm.name,
        position: resumeForm.position,
        workYears: resumeForm.workYears,
        skills: resumeForm.skills,
        experience: resumeForm.experience
      }
    }

    const res = await analyzeResume(data)
    if (res.code === 0) {
      suggestion.value = res.data
      saveSuggestionHistory()
      ElMessage.success('简历分析成功')
      
      // 自动生成学习计划
      await generateStudyPlan()
    } else {
      ElMessage.error(res.message || '分析失败')
    }
  } catch (e: any) {
    ElMessage.error(e.message || '分析失败')
  } finally {
    analyzing.value = false
  }
}

// 重新生成建议
const loadSuggestion = async () => {
  suggestionLoading.value = true
  try {
    const data = {
      name: resumeForm.name,
      position: resumeForm.position,
      workYears: resumeForm.workYears,
      skills: resumeForm.skills,
      experience: resumeForm.experience,
      resumeContent: suggestion.value
    }
    
    const res = await analyzeResume(data)
    if (res.code === 0) {
      suggestion.value = res.data
      saveSuggestionHistory()
      ElMessage.success('学习建议生成成功')
      
      // 重新生成学习计划
      await generateStudyPlan()
    } else {
      ElMessage.error(res.message || '生成失败')
    }
  } catch (e: any) {
    ElMessage.error(e.message || '生成失败')
  } finally {
    suggestionLoading.value = false
  }
}

// 生成学习计划
const generateStudyPlan = async () => {
  try {
    const res = await generateTaskPlan({
      goalDescription: `根据简历分析结果制定学习计划：${suggestion.value.substring(0, 100)}...`,
      taskCount: 5,
      preference: `目标职位：${resumeForm.position || '未知'}，技能：${resumeForm.skills || '未知'}`,
      model: 'qwen-turbo'
    })
    
    if (res.code === 0) {
      planResult.value = res.data
      ElMessage.success('学习计划生成成功')
    }
  } catch (error) {
    console.error('生成学习计划失败:', error)
  }
}

// 创建单个任务
const createTaskFromPlan = async (plannedTask: AiPlannedTask) => {
  try {
    const category = extractCategory(plannedTask.title, plannedTask.description)
    const res = await createTask({
      title: plannedTask.title,
      description: plannedTask.description,
      targetHours: plannedTask.suggestedHours,
      category
    })
    if (res.code === 0) {
      ElMessage.success('任务创建成功')
    } else {
      ElMessage.error(res.message || '创建任务失败')
    }
  } catch (error: any) {
    console.error('创建任务失败:', error)
    ElMessage.error('创建任务失败')
  }
}

// 一键创建所有任务
const createAllTasks = async () => {
  if (!planResult.value || !planResult.value.tasks || planResult.value.tasks.length === 0) {
    ElMessage.warning('没有可创建的任务')
    return
  }

  try {
    await ElMessageBox.confirm(
      `确定要创建 ${planResult.value.tasks.length} 个学习任务吗？`,
      '提示',
      { type: 'warning' }
    )

    let successCount = 0
    for (const plannedTask of planResult.value.tasks) {
      try {
        const category = extractCategory(plannedTask.title, plannedTask.description)
        await createTask({
          title: plannedTask.title,
          description: plannedTask.description,
          targetHours: plannedTask.suggestedHours,
          category
        })
        successCount++
      } catch (error) {
        console.error('创建任务失败:', error)
      }
    }

    ElMessage.success(`成功创建 ${successCount} 个任务`)
  } catch (error: any) {
    if (error !== 'cancel') {
      console.error('批量创建任务失败:', error)
    }
  }
}

// 提取分类
const extractCategory = (title: string, description: string): string => {
  const text = (title + ' ' + description).toLowerCase()
  const categories = ['编程', '前端', '后端', '数据库', '算法', '设计', '产品', '运营']
  for (const cat of categories) {
    if (text.includes(cat.toLowerCase())) {
      return cat
    }
  }
  return '学习'
}

// 加载本地历史
const loadLocalHistory = () => {
  const historyRaw = localStorage.getItem(STORAGE_SUGGESTION_HISTORY)
  if (historyRaw) {
    try {
      suggestionHistory.value = JSON.parse(historyRaw)
    } catch {
      suggestionHistory.value = []
    }
  }
  
  const currentSuggestion = localStorage.getItem(STORAGE_CURRENT_SUGGESTION)
  if (currentSuggestion) {
    suggestion.value = currentSuggestion
  }
}

// 组件挂载时加载历史
loadLocalHistory()
</script>

<style scoped>
.resume-generate {
  padding: 0;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header-actions {
  display: flex;
  gap: 8px;
}

.upload-card,
.suggestion-card {
  height: 100%;
}

.upload-section {
  padding: 20px 0;
}

.resume-uploader {
  border: 2px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}

.resume-uploader:hover {
  border-color: #409eff;
}

.file-info {
  margin-top: 12px;
  display: flex;
  align-items: center;
  gap: 8px;
}

.manual-form {
  margin-top: 20px;
  padding-top: 20px;
  border-top: 1px solid #e0e0e0;
}

.suggestion-content {
  min-height: 260px;
  max-height: 420px;
  overflow-y: auto;
  padding: 16px 18px;
  border-radius: 14px;
  background: radial-gradient(circle at top left, rgba(129, 140, 248, 0.25), rgba(56, 189, 248, 0.08)),
    linear-gradient(135deg, rgba(76, 111, 255, 0.12), rgba(145, 101, 255, 0.2));
  box-shadow:
    0 12px 30px rgba(15, 23, 42, 0.15),
    inset 0 0 0 1px rgba(255, 255, 255, 0.4);
  backdrop-filter: blur(10px);
  color: #111827;
  border: 1px solid rgba(129, 140, 248, 0.35);
  position: relative;
}

.suggestion-content::before {
  content: '';
  position: absolute;
  inset: 0;
  border-radius: inherit;
  background: radial-gradient(circle at top right, rgba(244, 244, 245, 0.3), transparent 60%);
  pointer-events: none;
  opacity: 0.9;
}

.suggestion-content.with-history {
  margin-top: 8px;
}

.suggestion-text {
  white-space: pre-wrap;
  line-height: 1.8;
  font-size: 14px;
  color: #111827;
  text-shadow: 0 1px 1px rgba(255, 255, 255, 0.6);
}

.history-list {
  max-height: 140px;
  overflow-y: auto;
  margin-bottom: 8px;
  padding: 8px;
  border-radius: 8px;
  background: rgba(15, 23, 42, 0.7);
  color: #e5e7eb;
}

.history-item {
  padding: 6px 8px;
  border-radius: 6px;
  cursor: pointer;
  transition: background-color 0.2s ease;
}

.history-item + .history-item {
  margin-top: 4px;
}

.history-item:hover {
  background: rgba(148, 163, 184, 0.3);
}

.history-item.active {
  background: rgba(59, 130, 246, 0.5);
}

.history-time {
  font-size: 11px;
  opacity: 0.75;
}

.history-preview {
  font-size: 12px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.plan-result {
  margin-top: 16px;
}

.plan-summary {
  margin-bottom: 16px;
  padding: 12px;
  background-color: #f5f7fa;
  border-radius: 4px;
}

.plan-summary h4 {
  margin: 0 0 8px 0;
  color: #303133;
}

.plan-summary p {
  margin: 0;
  color: #606266;
  line-height: 1.6;
}

.plan-tasks h4 {
  margin: 0 0 12px 0;
  color: #303133;
}

.description-cell {
  max-width: 200px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
</style>