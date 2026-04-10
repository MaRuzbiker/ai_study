<template>
  <div class="resume-preview-page">
    <!-- 顶部工具栏 -->
    <div class="preview-header">
      <el-button text @click="router.push('/resume/edit')" class="back-link">
        <el-icon><ArrowLeft /></el-icon>返回编辑
      </el-button>
      <div class="preview-actions">
        <el-select v-model="templateId" size="small" style="width: 160px" @change="handleTemplateChange">
          <el-option label="现代简洁模板" value="1" />
          <el-option label="经典模板" value="2" />
          <el-option label="简易模板" value="3" />
          <el-option label="多页模板" value="4" />
          <el-option label="商务模板" value="5" />
        </el-select>
        
        <!-- 主题设置 -->
        <el-popover
          placement="bottom-right"
          width="300"
          trigger="click"
          v-model:visible="themePanelOpen"
        >
          <template #reference>
            <el-button type="primary" size="small" :icon="Edit">主题设置</el-button>
          </template>
          <template #default>
            <div class="theme-settings-panel">
              <div class="theme-settings-preview">
                <span class="theme-settings-label">当前主色</span>
                <div class="theme-settings-preview-value">
                  <span
                    class="theme-settings-preview-chip"
                    :style="{ backgroundColor: currentData.value.theme?.color || '#2f5785' }"
                  />
                  <code>{{ currentData.value.theme?.color || '#2f5785' }}</code>
                </div>
              </div>
              <div class="theme-settings-row">
                <span class="theme-settings-label">主题色</span>
                <el-color-picker
                  v-model="currentData.value.theme.color"
                  @change="handleThemeChange"
                  class="theme-settings-picker"
                />
              </div>
              <div class="theme-settings-presets">
                <div class="theme-settings-label">预设主题</div>
                <div class="theme-settings-swatch-list">
                  <button
                    v-for="color in PRESET_THEME_COLORS"
                    :key="color"
                    type="button"
                    class="theme-settings-swatch"
                    :class="{ 'is-active': currentData.value.theme?.color === color }"
                    :style="{ backgroundColor: color }"
                    @click="handleThemeChange(color)"
                  />
                </div>
              </div>
              <el-button size="small" @click="handleThemeReset">恢复默认</el-button>
            </div>
          </template>
        </el-popover>
        
        <!-- 下载PDF -->
        <el-button type="primary" size="small" :icon="Download" @click="handleDownloadPdf">下载PDF</el-button>
      </div>
    </div>

    <!-- 预览内容区域 -->
    <div class="preview-content">
      <div class="print-resume-shell">
        <ResumeTemplateRenderer :templateId="Number(templateId)" :data="mappedData" />
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { ArrowLeft, Download, Edit } from '@element-plus/icons-vue'
import ResumeTemplateRenderer from './ResumeTemplateRenderer.vue'
import { useResumeStore } from '@/stores/resume'
import { PRESET_THEME_COLORS } from '@/types/resume'

const router = useRouter()
const store = useResumeStore()

const props = defineProps<{
  data?: Record<string, any>
}>()

const templateId = ref('1')
const themePanelOpen = ref(false)

// 使用传入的数据或store中的数据
const currentData = computed(() => props.data || store.config)

const mappedData = computed(() => ({
  ...currentData.value,
  templateId: Number(templateId.value),
  theme: currentData.value.theme || {},
}))



// 处理模板切换
const handleTemplateChange = (value: string) => {
  templateId.value = value
}

// 处理主题色变化
const handleThemeChange = (color: string) => {
  store.updateTheme(color)
}

// 恢复默认主题
const handleThemeReset = () => {
  store.updateTheme('#2f5785')
}

// 下载PDF
const handleDownloadPdf = () => {
  ElMessage.info('正在准备打印，请稍候...')
  document.body.classList.add('is-printing')
  setTimeout(() => {
    window.print()
    setTimeout(() => {
      document.body.classList.remove('is-printing')
    }, 800)
  }, 100)
}

onMounted(() => {
  // 初始化模板ID
  templateId.value = String(currentData.value.templateId || 1)
})
</script>

<style scoped>
.resume-preview-page {
  min-height: 100vh;
  background: #f5f7fa;
}

.preview-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 24px;
  background: #fff;
  border-bottom: 1px solid #e8e8e8;
  position: sticky;
  top: 0;
  z-index: 100;
}

.back-link {
  font-size: 14px;
}

.preview-actions {
  display: flex;
  gap: 12px;
  align-items: center;
}

.preview-content {
  padding: 24px;
  display: flex;
  flex-direction: column;
  align-items: center;
  background: #e2e8ef;
}

.print-resume-shell {
  width: 794px;
  flex-shrink: 0;
}

/* 主题设置面板样式 */
.theme-settings-panel {
  padding: 16px;
}

.theme-settings-preview {
  margin-bottom: 16px;
}

.theme-settings-label {
  font-size: 12px;
  color: #666;
  margin-bottom: 8px;
  display: block;
}

.theme-settings-preview-value {
  display: flex;
  align-items: center;
  gap: 8px;
}

.theme-settings-preview-chip {
  width: 24px;
  height: 24px;
  border-radius: 4px;
}

.theme-settings-row {
  margin-bottom: 16px;
}

.theme-settings-picker {
  width: 100%;
}

.theme-settings-presets {
  margin-bottom: 16px;
}

.theme-settings-swatch-list {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.theme-settings-swatch {
  width: 24px;
  height: 24px;
  border-radius: 4px;
  cursor: pointer;
  border: 2px solid transparent;
  transition: all 0.2s;
}

.theme-settings-swatch:hover {
  transform: scale(1.1);
}

.theme-settings-swatch.is-active {
  border-color: #333;
  box-shadow: 0 0 0 2px rgba(0, 0, 0, 0.2);
}

/* 打印样式 */
@media print {
  @page {
    size: A4 portrait;
    margin: 0;
  }

  .preview-header {
    display: none !important;
  }

  .preview-content {
    padding: 0 !important;
    background: #fff !important;
  }

  .print-resume-shell {
    width: 100% !important;
  }
}
</style>
