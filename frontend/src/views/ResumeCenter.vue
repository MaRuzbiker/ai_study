<template>
  <div class="resume-center">
    <!-- 统计卡片 -->
    <el-row :gutter="16" style="margin-bottom: 20px;">
      <el-col :span="8">
        <el-card class="stat-card">
          <el-statistic title="我的简历数" :value="stats.resumeCount">
            <template #suffix><span style="font-size:14px;color:#999;"> 份</span></template>
          </el-statistic>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card class="stat-card">
          <el-statistic title="已使用模板" :value="stats.templateCount">
            <template #suffix><span style="font-size:14px;color:#999;"> 种</span></template>
          </el-statistic>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card class="stat-card">
          <el-statistic title="草稿简历" :value="stats.draftCount">
            <template #suffix><span style="font-size:14px;color:#999;"> 份</span></template>
          </el-statistic>
        </el-card>
      </el-col>
    </el-row>

    <!-- 功能入口 -->
    <el-card style="margin-bottom:20px;">
      <div class="entry-section">
        <div class="entry-title">创建新简历</div>
        <el-row :gutter="20">
          <el-col :span="12">
            <div class="entry-card" @click="router.push('/resume/edit')">
              <div class="entry-icon">&#9998;</div>
              <div class="entry-name">手动填写</div>
              <div class="entry-desc">从空白表单开始编辑</div>
            </div>
          </el-col>
          <el-col :span="12">
            <div class="entry-card ai-entry" @click="router.push('/resume/generate')">
              <div class="entry-icon">&#129302;</div>
              <div class="entry-name">AI 识别简历</div>
              <div class="entry-desc">上传简历生成学习建议</div>
            </div>
          </el-col>
        </el-row>
      </div>
    </el-card>

    <!-- 我的简历列表 -->
    <el-card>
      <template #header>
        <div class="card-header">
          <span>我的简历</span>
          <el-button type="primary" size="small" @click="router.push('/resume/edit')">
            <el-icon style="margin-right:4px;"><Plus /></el-icon>新建简历
          </el-button>
        </div>
      </template>

      <div v-if="loading" style="text-align:center;padding:40px;">
        <el-icon class="is-loading" style="font-size:32px;"><Loading /></el-icon>
      </div>

      <div v-else-if="resumes.length === 0" class="empty-state">
        <div class="empty-icon">&#128196;</div>
        <div class="empty-text">还没有简历</div>
        <div class="empty-sub">点击上方按钮创建你的第一份简历</div>
        <div class="quick-entry">
          <el-button type="primary" @click="router.push('/resume/edit')">手动填写</el-button>
          <el-button type="success" @click="router.push('/resume/generate')">AI 识别</el-button>
        </div>
      </div>

      <el-row v-else :gutter="16">
        <el-col v-for="item in resumes" :key="item.id" :span="8" style="margin-bottom:16px;">
          <el-card class="resume-card" shadow="hover">
            <div class="resume-card-header">
              <el-tag v-if="item.status === 'DRAFT'" type="warning" size="small">草稿</el-tag>
              <el-tag v-else type="success" size="small">已完成</el-tag>
              <span class="resume-date">{{ formatDate(item.updateTime) }}</span>
            </div>
            <div class="resume-title">{{ item.title }}</div>
            <div class="resume-actions">
              <el-button type="primary" link size="small" @click="previewResume(item)">
                <el-icon><View /></el-icon>预览
              </el-button>
              <el-button type="primary" link size="small" @click="router.push('/resume/edit/' + item.id)">
                <el-icon><Edit /></el-icon>编辑
              </el-button>
              <el-button type="danger" link size="small" @click="handleDelete(item.id)">
                <el-icon><Delete /></el-icon>删除
              </el-button>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </el-card>

    <!-- 预览对话框 -->
    <el-dialog v-model="previewVisible" title="简历预览" width="800px" destroy-on-close>
      <div v-if="previewData" class="resume-preview">
        <ResumePreview :data="JSON.parse(previewData.content)" />
      </div>
      <template #footer>
        <el-button @click="previewVisible = false">关闭</el-button>
        <el-button type="primary" @click="doPrint">导出 PDF</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script lang="ts" setup>
import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { View, Edit, Delete, Plus, Loading } from '@element-plus/icons-vue'
import { getMyResumes, deleteResume } from '@/api/resume'
import ResumePreview from './ResumePreview.vue'

const router = useRouter()
const resumes = ref<any[]>([])
const loading = ref(true)
const previewVisible = ref(false)
const previewData = ref<any>(null)

const stats = computed(() => {
  const draft = resumes.value.filter((r: any) => r.status === 'DRAFT').length
  const templates = new Set(resumes.value.map((r: any) => r.templateId).filter(Boolean)).size
  return { resumeCount: resumes.value.length, templateCount: templates, draftCount: draft }
})

const formatDate = (dateStr: string) => dateStr ? dateStr.substring(0, 10) : ''

// 预览功能已通过ResumePreview组件实现

const previewResume = (item: any) => { previewData.value = item; previewVisible.value = true }
const doPrint = () => window.print()

const handleDelete = async (id: number) => {
  try {
    await ElMessageBox.confirm('确定要删除这份简历吗？', '提示', { type: 'warning' })
    await deleteResume(id)
    ElMessage.success('删除成功')
    loadData()
  } catch (e: any) { if (e !== 'cancel') ElMessage.error(e.message || '删除失败') }
}

const loadData = async () => {
  loading.value = true
  try {
    const res = await getMyResumes()
    if (res.code === 0) resumes.value = res.data || []
  } catch (e: any) { ElMessage.error(e.message || '加载简历失败') }
  finally { loading.value = false }
}

onMounted(() => loadData())
</script>

<style scoped>
.stat-card { text-align: center; }
.card-header { display: flex; justify-content: space-between; align-items: center; }
.entry-section { padding: 8px 0; }
.entry-title { font-size: 16px; font-weight: 600; margin-bottom: 16px; color: #333; }
.entry-card { border: 2px dashed #e0e0e0; border-radius: 12px; padding: 24px 16px; text-align: center; cursor: pointer; transition: all 0.3s; }
.entry-card:hover { border-color: #409eff; background: #f0f7ff; }
.ai-entry:hover { border-color: #67c23a; background: #f0fdf4; }
.entry-icon { font-size: 36px; margin-bottom: 8px; }
.entry-name { font-size: 16px; font-weight: 600; margin-bottom: 4px; }
.entry-desc { font-size: 13px; color: #888; }
.resume-card { height: 140px; display: flex; flex-direction: column; justify-content: space-between; }
.resume-card-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 8px; }
.resume-date { font-size: 12px; color: #999; }
.resume-title { font-size: 15px; font-weight: 600; color: #333; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
.resume-actions { display: flex; gap: 4px; }
.empty-state { text-align: center; padding: 60px 0; }
.empty-icon { font-size: 64px; margin-bottom: 16px; }
.empty-text { font-size: 18px; color: #666; margin-bottom: 8px; }
.empty-sub { font-size: 14px; color: #999; }
</style>
