<template>
  <div class="resume-edit-layout">
    <!-- ========== 顶部导航 ========== -->
    <div class="edit-toolbar">
      <div class="toolbar-left">
        <el-button text @click="router.push('/resume')">
          <el-icon><ArrowLeft /></el-icon>返回
        </el-button>
        <el-divider direction="vertical" />
        <el-input
          v-model="form.title"
          placeholder="简历标题"
          size="small"
          style="width: 220px;"
          maxlength="60"
        />
      </div>
      <div class="toolbar-right">
        <el-select v-model="currentTemplateId" placeholder="选择模板" size="default" style="width:160px;" @change="onTemplateChange">
          <el-option v-for="t in templateOptions" :key="t.id" :label="t.label" :value="t.id" />
        </el-select>
        <el-button @click="saveAsDraft" :loading="saving">保存草稿</el-button>
        <el-button type="primary" @click="saveAndPublish" :loading="saving">保存发布</el-button>
        <el-button @click="handlePrint">导出 PDF</el-button>
      </div>
    </div>

    <!-- ========== 主体：左侧编辑 + 右侧预览 ========== -->
    <div class="edit-main">
      <!-- 左侧：表单编辑 -->
      <div class="edit-form-panel">
        <el-scrollbar height="100%">
          <div class="form-inner">

            <!-- 个人信息 -->
            <div class="section-block">
              <div class="block-title">👤 个人信息</div>
              <el-form label-width="72px" size="default">
                <el-row :gutter="10">
                  <el-col :span="12"><el-form-item label="姓名"><el-input v-model="form.personal.name" placeholder="张三" /></el-form-item></el-col>
                  <el-col :span="12"><el-form-item label="性别"><el-select v-model="form.personal.gender" placeholder="请选择" style="width:100%"><el-option label="男" value="男" /><el-option label="女" value="女" /></el-select></el-form-item></el-col>
                  <el-col :span="12"><el-form-item label="电话"><el-input v-model="form.personal.phone" placeholder="138xxxx8888" /></el-form-item></el-col>
                  <el-col :span="12"><el-form-item label="邮箱"><el-input v-model="form.personal.email" placeholder="example@qq.com" /></el-form-item></el-col>
                  <el-col :span="24"><el-form-item label="求职意向"><el-input v-model="form.personal.targetPosition" placeholder="例如：Java 后端开发工程师" /></el-form-item></el-col>
                </el-row>
              </el-form>
            </div>

            <!-- 教育背景 -->
            <div class="section-block">
              <div class="block-title">🎓 教育背景</div>
              <div v-for="(edu, idx) in form.education" :key="idx" class="sub-block">
                <el-row :gutter="10">
                  <el-col :span="12"><el-form-item label="学校" size="small"><el-input v-model="edu.school" placeholder="学校名称" /></el-form-item></el-col>
                  <el-col :span="12"><el-form-item label="学历" size="small"><el-select v-model="edu.degree" placeholder="请选择" style="width:100%"><el-option label="高中" value="高中" /><el-option label="大专" value="大专" /><el-option label="本科" value="本科" /><el-option label="硕士" value="硕士" /><el-option label="博士" value="博士" /></el-select></el-form-item></el-col>
                  <el-col :span="12"><el-form-item label="专业" size="small"><el-input v-model="edu.major" placeholder="专业名称" /></el-form-item></el-col>
                  <el-col :span="12"><el-form-item label="GPA" size="small"><el-input v-model="edu.gpa" placeholder="例如：3.8/4.0" /></el-form-item></el-col>
                  <el-col :span="12"><el-form-item label="开始" size="small"><el-date-picker v-model="edu.startDate" type="month" value-format="YYYY-MM" placeholder="开始时间" style="width:100%" /></el-form-item></el-col>
                  <el-col :span="12"><el-form-item label="结束" size="small"><el-date-picker v-model="edu.endDate" type="month" value-format="YYYY-MM" placeholder="至今留空" style="width:100%" /></el-form-item></el-col>
                </el-row>
                <el-button type="danger" plain size="small" @click="form.education.splice(idx, 1)">删除</el-button>
              </div>
              <el-button type="default" plain size="small" @click="form.education.push({ school:'', degree:'', major:'', gpa:'', startDate:'', endDate:'' })">+ 添加教育经历</el-button>
            </div>

            <!-- 工作经历 -->
            <div class="section-block">
              <div class="block-title">💼 工作经历</div>
              <div v-for="(exp, idx) in form.experience" :key="idx" class="sub-block">
                <el-row :gutter="10">
                  <el-col :span="12"><el-form-item label="公司" size="small"><el-input v-model="exp.company" placeholder="公司名称" /></el-form-item></el-col>
                  <el-col :span="12"><el-form-item label="职位" size="small"><el-input v-model="exp.position" placeholder="担任职位" /></el-form-item></el-col>
                  <el-col :span="12"><el-form-item label="开始" size="small"><el-date-picker v-model="exp.startDate" type="month" value-format="YYYY-MM" placeholder="开始时间" style="width:100%" /></el-form-item></el-col>
                  <el-col :span="12"><el-form-item label="结束" size="small"><el-date-picker v-model="exp.endDate" type="month" value-format="YYYY-MM" placeholder="至今留空" style="width:100%" /></el-form-item></el-col>
                  <el-col :span="24"><el-form-item label="工作描述" size="small"><el-input v-model="exp.description" type="textarea" :rows="3" placeholder="请描述工作职责和成绩，尽量量化成果" /></el-form-item></el-col>
                </el-row>
                <el-button type="danger" plain size="small" @click="form.experience.splice(idx, 1)">删除</el-button>
              </div>
              <el-button type="default" plain size="small" @click="form.experience.push({ company:'', position:'', startDate:'', endDate:'', description:'' })">+ 添加工作经历</el-button>
            </div>

            <!-- 项目经历 -->
            <div class="section-block">
              <div class="block-title">🚀 项目经历</div>
              <div v-for="(proj, idx) in form.projects" :key="idx" class="sub-block">
                <el-row :gutter="10">
                  <el-col :span="12"><el-form-item label="项目名" size="small"><el-input v-model="proj.name" placeholder="项目名称" /></el-form-item></el-col>
                  <el-col :span="12"><el-form-item label="担任角色" size="small"><el-input v-model="proj.role" placeholder="例如：后端开发" /></el-form-item></el-col>
                  <el-col :span="12"><el-form-item label="开始" size="small"><el-date-picker v-model="proj.startDate" type="month" value-format="YYYY-MM" placeholder="开始时间" style="width:100%" /></el-form-item></el-col>
                  <el-col :span="12"><el-form-item label="结束" size="small"><el-date-picker v-model="proj.endDate" type="month" value-format="YYYY-MM" placeholder="至今留空" style="width:100%" /></el-form-item></el-col>
                  <el-col :span="24"><el-form-item label="项目描述" size="small"><el-input v-model="proj.description" type="textarea" :rows="2" placeholder="项目背景、技术栈、个人贡献" /></el-form-item></el-col>
                  <el-col :span="24"><el-form-item label="项目成果" size="small"><el-input v-model="proj.achievements" type="textarea" :rows="2" placeholder="量化成果，例如：提升接口响应速度40%" /></el-form-item></el-col>
                </el-row>
                <el-button type="danger" plain size="small" @click="form.projects.splice(idx, 1)">删除</el-button>
              </div>
              <el-button type="default" plain size="small" @click="form.projects.push({ name:'', role:'', startDate:'', endDate:'', description:'', achievements:'' })">+ 添加项目经历</el-button>
            </div>

            <!-- 专业技能 -->
            <div class="section-block">
              <div class="block-title">🛠️ 专业技能</div>
              <div class="skill-tags">
                <el-tag v-for="(skill, idx) in form.skills" :key="idx" closable @close="form.skills.splice(idx, 1)" style="margin-right:6px;margin-bottom:6px;">{{ skill }}</el-tag>
                <el-input v-if="addingSkill" ref="skillInputRef" v-model="newSkill" size="small" style="width:120px;" @keyup.enter="confirmAddSkill" @blur="confirmAddSkill" />
                <el-button v-else size="small" @click="startAddSkill">+ 添加</el-button>
              </div>
            </div>

            <!-- 自我评价 -->
            <div class="section-block">
              <div class="block-title">💡 自我评价</div>
              <el-input v-model="form.summary" type="textarea" :rows="4" placeholder="简要描述你的优势、职业目标和性格特点" maxlength="500" show-word-limit />
            </div>

          </div>
        </el-scrollbar>
      </div>

      <!-- 右侧：实时模板预览 -->
      <div class="edit-preview-panel">
        <div class="preview-toolbar">
          <span>实时预览 · {{ currentTemplateLabel }}</span>
          <div class="zoom-ctrl">
            <el-button size="small" text @click="zoom = Math.min(1.2, +(zoom + 0.1).toFixed(1))">+</el-button>
            <span class="zoom-val">{{ Math.round(zoom * 100) }}%</span>
            <el-button size="small" text @click="zoom = Math.max(0.3, +(zoom - 0.1).toFixed(1))">−</el-button>
            <el-button size="small" text @click="zoom = 0.75" style="margin-left:4px;font-size:11px;">适应</el-button>
          </div>
        </div>
        <div class="preview-scroll" ref="previewScrollRef">
          <div class="preview-outer">
            <ResumePreview :data="previewData" :zoom="zoom" />
          </div>
        </div>
      </div>
    </div>

    <!-- 打印区：视觉隐藏但 DOM 存在，CSS 打印时显示 -->
    <div id="resume-print-area" style="position:fixed;top:0;left:-9999px;width:820px;z-index:-1;">
      <ResumePreview :data="previewData" :zoom="1" />
    </div>
  </div>
</template>

<style>
/* 打印样式：隐藏所有 UI，只显示简历打印区 */
@media print {
  body > * { display: none !important; }
  #resume-print-area {
    display: block !important;
    position: static !important;
    top: auto !important;
    left: auto !important;
    width: 820px !important;
    z-index: auto !important;
  }
  #resume-print-area * { visibility: visible !important; }
}
</style>

<script lang="ts" setup>
import { ref, reactive, computed, onMounted, nextTick } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { ArrowLeft } from '@element-plus/icons-vue'
import { getResume, saveResume, getResumeTemplates } from '@/api/resume'
import ResumePreview from './ResumePreview.vue'

const router = useRouter()
const route = useRoute()

const isEdit = ref(false)
const saving = ref(false)
const addingSkill = ref(false)
const newSkill = ref('')
const skillInputRef = ref()
const zoom = ref(0.75)
const templates = ref<any[]>([])
const currentTemplateId = ref<number | null>(null)
const previewScrollRef = ref()

// 内置模板选项（优先级高于 API）
const templateOptions = [
  { id: 1, label: '经典蓝色' },
  { id: 2, label: '专业深灰' },
  { id: 3, label: '清新绿色' },
  { id: 4, label: '技术风格' },
  { id: 5, label: '简约白色' },
]

const currentTemplateLabel = computed(() => {
  return templateOptions.find(t => t.id === currentTemplateId.value)?.label || '经典蓝色'
})

const form = reactive({
  id: null as number | null,
  templateId: null as number | null,
  title: '',
  status: 'VISIBLE',
  personal: { name: '', gender: '', phone: '', email: '', targetPosition: '' },
  education: [] as any[],
  experience: [] as any[],
  projects: [] as any[],
  skills: [] as string[],
  summary: ''
})

// 传给预览组件的完整数据（含 templateId）
const previewData = computed(() => ({
  templateId: currentTemplateId.value,
  personal: form.personal,
  education: form.education,
  experience: form.experience,
  projects: form.projects,
  skills: form.skills,
  summary: form.summary
}))

const startAddSkill = () => { addingSkill.value = true; nextTick(() => skillInputRef.value?.focus()) }
const confirmAddSkill = () => {
  if (newSkill.value.trim()) form.skills.push(newSkill.value.trim())
  newSkill.value = ''; addingSkill.value = false
}

const getContent = () => JSON.stringify({
  personal: form.personal,
  education: form.education,
  experience: form.experience,
  projects: form.projects,
  skills: form.skills,
  summary: form.summary
})

const doSave = async (status: string) => {
  form.status = status
  if (!form.title.trim()) { ElMessage.warning('请填写简历标题'); return }
  if (!form.personal.name.trim()) { ElMessage.warning('请填写姓名'); return }
  saving.value = true
  try {
    const payload = {
      id: form.id,
      templateId: currentTemplateId.value,
      title: form.title,
      status: form.status,
      content: getContent()
    }
    const res = await saveResume(payload)
    if (res.code === 0) {
      form.id = res.data?.id ?? form.id
      isEdit.value = true
      ElMessage.success(status === 'DRAFT' ? '草稿保存成功' : '简历保存成功')
    } else { ElMessage.error(res.message || '保存失败') }
  } catch (e: any) { ElMessage.error(e.message || '保存失败') }
  finally { saving.value = false }
}

const saveAsDraft = () => doSave('DRAFT')
const saveAndPublish = () => doSave('VISIBLE')

const handlePrint = () => {
  // 先隐藏 UI，只显示简历打印区，再调用打印
  const editLayout = document.querySelector('.resume-edit-layout') as HTMLElement
  if (!editLayout) return
  editLayout.style.display = 'none'
  document.body.style.background = '#fff'
  setTimeout(() => {
    window.print()
    editLayout.style.display = ''
    document.body.style.background = ''
  }, 100)
}

const onTemplateChange = (id: number) => {
  currentTemplateId.value = id
}

const loadData = async () => {
  // 尝试加载后端模板列表（兼容旧数据）
  try {
    const res = await getResumeTemplates()
    if (res.code === 0 && (res.data || []).length > 0) {
      templates.value = res.data || []
    }
  } catch {}

  // 默认选中模板 1（经典蓝色）
  if (!currentTemplateId.value) {
    currentTemplateId.value = 1
  }

  // 从 URL query 读取 templateId
  const queryTemplateId = route.query.templateId
  if (queryTemplateId) {
    currentTemplateId.value = Number(queryTemplateId)
  }

  // 加载简历数据
  const id = route.params.id
  if (id) {
    isEdit.value = true
    try {
      const res = await getResume(Number(id))
      if (res.code === 0 && res.data) {
        const r = res.data
        form.id = r.id
        form.templateId = r.templateId
        form.title = r.title || ''
        form.status = r.status || 'VISIBLE'
        currentTemplateId.value = r.templateId || templates.value[0]?.id || 1
        if (r.content) {
          try {
            const c = typeof r.content === 'string' ? JSON.parse(r.content) : r.content
            Object.assign(form.personal, c.personal || {})
            form.education = c.education || []
            form.experience = c.experience || []
            form.projects = c.projects || []
            form.skills = c.skills || []
            form.summary = c.summary || ''
          } catch {}
        }
      }
    } catch (e: any) { ElMessage.error(e.message || '加载简历失败') }
  }
}

onMounted(() => loadData())
</script>

<style scoped>
.resume-edit-layout {
  display: flex;
  flex-direction: column;
  height: calc(100vh - 60px);
  background: #f0f2f5;
  overflow: hidden;
}

/* 工具栏 */
.edit-toolbar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 8px 16px;
  background: #fff;
  border-bottom: 1px solid #e8e8e8;
  flex-shrink: 0;
}
.toolbar-left { display: flex; align-items: center; gap: 8px; }
.toolbar-right { display: flex; align-items: center; gap: 8px; }

/* 主体 */
.edit-main {
  display: flex;
  flex: 1;
  overflow: hidden;
}

/* 左侧表单 */
.edit-form-panel {
  width: 420px;
  flex-shrink: 0;
  background: #fff;
  border-right: 1px solid #e8e8e8;
  overflow: hidden;
}
.form-inner { padding: 14px; }

/* 右侧预览 */
.edit-preview-panel {
  flex: 1;
  display: flex;
  flex-direction: column;
  background: #e2e8ef;
  overflow: hidden;
}
.preview-toolbar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 6px 16px;
  background: rgba(255,255,255,0.95);
  border-bottom: 1px solid #e0e0e0;
  font-size: 12.5px;
  color: #555;
  flex-shrink: 0;
}
.zoom-ctrl { display: flex; align-items: center; gap: 2px; }
.zoom-val { font-size: 12px; min-width: 36px; text-align: center; color: #888; }
.preview-scroll {
  flex: 1;
  overflow: auto;
  padding: 20px;
  display: flex;
  justify-content: center;
  align-items: flex-start;
}
.preview-outer {
  transition: width 0.2s;
}

/* 表单区块 */
.section-block {
  background: #fafbfc;
  border: 1px solid #f0f0f0;
  border-radius: 8px;
  padding: 12px;
  margin-bottom: 10px;
}
.block-title {
  font-size: 13px;
  font-weight: 600;
  color: #333;
  margin-bottom: 10px;
}
.sub-block {
  background: #fff;
  border: 1px solid #eee;
  border-radius: 6px;
  padding: 10px;
  margin-bottom: 8px;
}
.skill-tags { display: flex; flex-wrap: wrap; align-items: center; gap: 4px; }
</style>
