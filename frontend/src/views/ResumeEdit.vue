<template>
  <div class="resume-edit-layout">
    <!-- 顶部工具栏 -->
    <div class="edit-toolbar">
      <div class="toolbar-left">
        <el-button text @click="router.push('/resume')">
          <el-icon><ArrowLeft /></el-icon>返回
        </el-button>
        <el-divider direction="vertical" />
        <el-input
          v-model="store.config.title"
          placeholder="简历标题"
          size="small"
          style="width:220px"
          maxlength="60"
        />
      </div>
      <div class="toolbar-right">
        <el-select v-model="store.config.templateId" size="default" style="width:160px">
          <el-option v-for="t in templateOptions" :key="t.id" :label="t.label" :value="t.id" />
        </el-select>
        <el-button @click="saveAsDraft" :loading="store.saving">保存草稿</el-button>
        <el-button type="primary" @click="saveAndPublish" :loading="store.saving">保存发布</el-button>
        <el-button type="success" @click="handleDownloadPdf">
          <el-icon><Download /></el-icon>下载PDF
        </el-button>
      </div>
    </div>

    <!-- 主内容区：左侧模块列表 + 中间表单 + 右侧预览 -->
    <div class="edit-main">
      <!-- 左侧：模块列表 -->
      <div class="edit-sidebar">
        <ModuleList
          v-model="store.selectedModule"
          :config="store.config"
        />
      </div>

      <!-- 中间：编辑表单 -->
      <div class="edit-form-area">
        <ModuleForm
          :selected-module="store.selectedModule"
          :config="store.config"
          @update="handleConfigUpdate"
        />
      </div>

      <!-- 右侧：实时预览 -->
      <div class="edit-preview-area">
        <div class="preview-header">
          <span class="preview-title">实时预览</span>
          <div class="zoom-ctrl">
            <el-button size="small" text @click="zoomOut" :disabled="zoom <= 0.4">-</el-button>
            <span class="zoom-val">{{ Math.round(zoom * 100) }}%</span>
            <el-button size="small" text @click="zoomIn" :disabled="zoom >= 1.2">+</el-button>
            <el-button size="small" text @click="zoom = 1" style="margin-left:4px;font-size:11px">100%</el-button>
          </div>
        </div>
        <div class="preview-panel">
          <div class="preview-scroll-area">
            <div class="resume-paper" ref="paperRef" :style="{ zoom: zoom }">
              <ResumeTemplateRenderer
                :templateId="store.config.templateId || 1"
                :data="previewDataForTemplate"
              />
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { ArrowLeft, Download } from '@element-plus/icons-vue'
import { useResumeStore } from '@/stores/resume'
import { PRESET_THEME_COLORS, DEFAULT_THEME } from '@/types/resume'
import type { ResumeConfig } from '@/types/resume'
import ModuleList from './resume/ModuleList.vue'
import ModuleForm from './resume/ModuleForm.vue'
import ResumeTemplateRenderer from './ResumeTemplateRenderer.vue'

const router = useRouter()
const route = useRoute()
const store = useResumeStore()
const zoom = ref(0.65)
const paperRef = ref()

const zoomIn = () => { zoom.value = Math.min(1.2, +(zoom.value + 0.1).toFixed(2) - 0) }
const zoomOut = () => { zoom.value = Math.max(0.4, +(zoom.value - 0.1).toFixed(2) - 0) }

const templateOptions = [
  { id: 1, label: '现代简洁模板' },
  { id: 2, label: '经典模板' },
  { id: 3, label: '简易模板' },
  { id: 4, label: '多页模板' },
  { id: 5, label: '商务模板' },
]

// 直接传递 store.config，不需要复杂映射
const previewDataForTemplate = computed(() => store.config)

// 处理 ModuleForm 发出的更新
function handleConfigUpdate(partial: Partial<ResumeConfig>) {
  store.updateConfig(partial)
}

// 保存草稿
async function saveAsDraft() {
  store.config.status = 'DRAFT'
  const ok = await store.saveResumeConfig()
  if (ok) ElMessage.success('草稿保存成功')
  else ElMessage.error('保存失败')
}

// 保存发布
async function saveAndPublish() {
  store.config.status = 'VISIBLE'
  const ok = await store.saveResumeConfig()
  if (ok) ElMessage.success('简历已发布')
  else ElMessage.error('发布失败')
}

// 下载 PDF — 使用CSS媒体查询方式
function handleDownloadPdf() {
  ElMessage.info('正在准备打印，请稍候...')
  
  // 创建临时打印样式
  const printStyle = document.createElement('style')
  printStyle.innerHTML = `
    @media print {
      body * {
        visibility: hidden !important;
      }
      .resume-paper,
      .resume-paper * {
        visibility: visible !important;
      }
      .resume-paper {
        position: absolute !important;
        left: 0 !important;
        top: 0 !important;
        width: 100% !important;
        height: 100% !important;
        zoom: 1 !important;
        box-shadow: none !important;
        margin: 0 !important;
      }
      @page {
        size: A4 portrait;
        margin: 0;
      }
      body {
        background: #fff !important;
      }
    }
  `
  document.head.appendChild(printStyle)
  
  // 执行打印
  setTimeout(() => {
    window.print()
    setTimeout(() => {
      document.head.removeChild(printStyle)
    }, 800)
  }, 100)
}

// 初始化：设置默认数据，避免 undefined 报错
function initDefaults() {
  if (!store.config.profile) {
    store.config.profile = { name: '' }
  }
  if (!store.config.avatar) {
    store.config.avatar = { hidden: false, shape: 'circle' }
  }
  if (!store.config.aboutme) {
    store.config.aboutme = { aboutme_desc: '' }
  }
  if (!store.config.theme) {
    store.config.theme = { ...DEFAULT_THEME }
  }
  if (!store.config.sectionOrder) {
    store.config.sectionOrder = ['summary', 'education', 'experience', 'projects', 'awards', 'certificates', 'skills']
  }
  if (!store.config.templateId) {
    store.config.templateId = 1
  }
  if (!store.config.educationList) {
    store.config.educationList = []
  }
  if (!store.config.workExpList) {
    store.config.workExpList = []
  }
  if (!store.config.projectList) {
    store.config.projectList = []
  }
  if (!store.config.skillList) {
    store.config.skillList = []
  }
  if (!store.config.awardList) {
    store.config.awardList = []
  }
  if (!store.config.workList) {
    store.config.workList = []
  }
}

// 加载已有简历
async function loadData() {
  const qid = route.query.templateId
  if (qid) store.config.templateId = Number(qid)

  const id = route.params.id
  if (id) {
    await store.loadResume(Number(id))
  }
  initDefaults()
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
/* ========== 整体布局 ========== */
.resume-edit-layout {
  display: flex;
  flex-direction: column;
  height: 100vh;
  background: #f5f7fa;
  overflow: hidden;
}

/* 顶部工具栏 */
.edit-toolbar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 8px 16px;
  background: #fff;
  border-bottom: 1px solid #e8e8e8;
  flex-shrink: 0;
}

.toolbar-left,
.toolbar-right {
  display: flex;
  align-items: center;
  gap: 8px;
}

/* 主内容区：三列布局 */
.edit-main {
  display: flex;
  flex: 1;
  overflow: hidden;
}

/* 左侧边栏：模块列表 */
.edit-sidebar {
  width: 200px;
  flex-shrink: 0;
  overflow: hidden;
  background: #fff;
  border-right: 1px solid #e8e8e8;
}

/* 中间区域：编辑表单 */
.edit-form-area {
  flex: 1;
  max-width: 400px;
  overflow: hidden;
  background: #fff;
  border-right: 1px solid #e8e8e8;
}

/* 右侧区域：实时预览 */
.edit-preview-area {
  flex: 2;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

/* 预览头部 */
.preview-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 8px 16px;
  border-bottom: 1px solid #f0f0f0;
  flex-shrink: 0;
  background: #fff;
}

.preview-title {
  font-size: 14px;
  font-weight: 600;
  color: #333;
}

.zoom-ctrl {
  display: flex;
  align-items: center;
  gap: 2px;
}

.zoom-val {
  font-size: 12px;
  min-width: 36px;
  text-align: center;
  color: #888;
}

/* 预览面板 */
.preview-panel {
  flex: 1;
  display: flex;
  flex-direction: column;
  overflow: hidden;
  background: #e2e8ef;
}

.preview-scroll-area {
  flex: 1;
  overflow: auto;
  padding: 24px;
  display: flex;
  flex-direction: column;
  align-items: center;
}

/* 简历纸张：固定 A4 宽高，zoom 控制缩放 */
.resume-paper {
  /* A4: 794px × 1123px (96dpi) */
  width: 794px;
  min-height: 1123px;
  flex-shrink: 0;
  background: #fff;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.15);
  transition: zoom 0.1s ease;
  zoom: 0.65; /* 由 JS 控制 */
}


</style>
