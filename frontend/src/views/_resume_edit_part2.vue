                <div v-if="element === 'competitions'" class="section-block draggable-block">
                  <div class="block-header"><el-icon class="drag-handle" style="cursor:grab;color:#bbb"><Rank /></el-icon><span class="block-icon">&#127942;</span><span class="block-title">竞赛经历</span></div>
                  <div v-for="(comp, idx) in form.competitions" :key="idx" class="sub-block">
                    <el-row :gutter="10">
                      <el-col :span="12"><el-form-item label="竞赛名称" size="small"><el-input v-model="comp.name" placeholder="例如：全国大学生数学建模竞赛" /></el-form-item></el-col>
                      <el-col :span="12"><el-form-item label="获奖等级" size="small"><el-select v-model="comp.level" style="width:100%"><el-option label="国家级" value="国家级" /><el-option label="省级" value="省级" /><el-option label="校级" value="校级" /><el-option label="院级" value="院级" /></el-select></el-form-item></el-col>
                      <el-col :span="12"><el-form-item label="具体名次" size="small"><el-input v-model="comp.rank" placeholder="例如：一等奖第3名" /></el-form-item></el-col>
                      <el-col :span="12"><el-form-item label="获奖时间" size="small"><el-date-picker v-model="comp.date" type="month" value-format="YYYY-MM" style="width:100%" /></el-form-item></el-col>
                      <el-col :span="24"><el-form-item label="描述" size="small"><el-input v-model="comp.description" type="textarea" :rows="2" placeholder="简述竞赛内容及本人承担的角色" /></el-form-item></el-col>
                    </el-row>
                    <el-button type="danger" plain size="small" @click="form.competitions.splice(idx, 1)">删除</el-button>
                  </div>
                  <el-button type="default" plain size="small" @click="form.competitions.push({ name:'', level:'', rank:'', date:'', description:'' })">+ 添加竞赛经历</el-button>
                </div>
                <div v-if="element === 'experience'" class="section-block draggable-block">
                  <div class="block-header"><el-icon class="drag-handle" style="cursor:grab;color:#bbb"><Rank /></el-icon><span class="block-icon">&#128188;</span><span class="block-title">工作经历</span></div>
                  <div v-for="(exp, idx) in form.experience" :key="idx" class="sub-block">
                    <el-row :gutter="10">
                      <el-col :span="12"><el-form-item label="公司" size="small"><el-input v-model="exp.company" placeholder="公司名称" /></el-form-item></el-col>
                      <el-col :span="12"><el-form-item label="职位" size="small"><el-input v-model="exp.position" placeholder="担任职位" /></el-form-item></el-col>
                      <el-col :span="12"><el-form-item label="开始" size="small"><el-date-picker v-model="exp.startDate" type="month" value-format="YYYY-MM" style="width:100%" /></el-form-item></el-col>
                      <el-col :span="12"><el-form-item label="结束" size="small"><el-date-picker v-model="exp.endDate" type="month" value-format="YYYY-MM" placeholder="留空为至今" style="width:100%" /></el-form-item></el-col>
                      <el-col :span="24"><el-form-item label="工作描述" size="small"><el-input v-model="exp.description" type="textarea" :rows="3" placeholder="描述工作职责和成绩，尽量量化成果" /></el-form-item></el-col>
                    </el-row>
                    <el-button type="danger" plain size="small" @click="form.experience.splice(idx, 1)">删除</el-button>
                  </div>
                  <el-button type="default" plain size="small" @click="form.experience.push({ company:'', position:'', startDate:'', endDate:'', description:'' })">+ 添加工作经历</el-button>
                </div>
                <div v-if="element === 'projects'" class="section-block draggable-block">
                  <div class="block-header"><el-icon class="drag-handle" style="cursor:grab;color:#bbb"><Rank /></el-icon><span class="block-icon">&#128640;</span><span class="block-title">项目经历</span></div>
                  <div v-for="(proj, idx) in form.projects" :key="idx" class="sub-block">
                    <el-row :gutter="10">
                      <el-col :span="12"><el-form-item label="项目名" size="small"><el-input v-model="proj.name" placeholder="项目名称" /></el-form-item></el-col>
                      <el-col :span="12"><el-form-item label="担任角色" size="small"><el-input v-model="proj.role" placeholder="例如：后端开发" /></el-form-item></el-col>
                      <el-col :span="12"><el-form-item label="开始" size="small"><el-date-picker v-model="proj.startDate" type="month" value-format="YYYY-MM" style="width:100%" /></el-form-item></el-col>
                      <el-col :span="12"><el-form-item label="结束" size="small"><el-date-picker v-model="proj.endDate" type="month" value-format="YYYY-MM" placeholder="留空为至今" style="width:100%" /></el-form-item></el-col>
                      <el-col :span="24"><el-form-item label="项目描述" size="small"><el-input v-model="proj.description" type="textarea" :rows="2" placeholder="项目背景、技术栈、个人贡献" /></el-form-item></el-col>
                      <el-col :span="24"><el-form-item label="项目成果" size="small"><el-input v-model="proj.achievements" type="textarea" :rows="2" placeholder="量化成果，例如：提升接口响应速度40%" /></el-form-item></el-col>
                    </el-row>
                    <el-button type="danger" plain size="small" @click="form.projects.splice(idx, 1)">删除</el-button>
                  </div>
                  <el-button type="default" plain size="small" @click="form.projects.push({ name:'', role:'', startDate:'', endDate:'', description:'', achievements:'' })">+ 添加项目经历</el-button>
                </div>
                <div v-if="element === 'awards'" class="section-block draggable-block">
                  <div class="block-header"><el-icon class="drag-handle" style="cursor:grab;color:#bbb"><Rank /></el-icon><span class="block-icon">&#127941;</span><span class="block-title">荣誉奖励</span></div>
                  <div v-for="(aw, idx) in form.awards" :key="idx" class="sub-block">
                    <el-row :gutter="10">
                      <el-col :span="12"><el-form-item label="奖励名称" size="small"><el-input v-model="aw.name" placeholder="例如：国家奖学金" /></el-form-item></el-col>
                      <el-col :span="12"><el-form-item label="奖励等级" size="small"><el-input v-model="aw.level" placeholder="例如：国家级" /></el-form-item></el-col>
                      <el-col :span="12"><el-form-item label="获得时间" size="small"><el-date-picker v-model="aw.date" type="month" value-format="YYYY-MM" style="width:100%" /></el-form-item></el-col>
                    </el-row>
                    <el-button type="danger" plain size="small" @click="form.awards.splice(idx, 1)">删除</el-button>
                  </div>
                  <el-button type="default" plain size="small" @click="form.awards.push({ name:'', level:'', date:'' })">+ 添加荣誉奖励</el-button>
                </div>
                <div v-if="element === 'certificates'" class="section-block draggable-block">
                  <div class="block-header"><el-icon class="drag-handle" style="cursor:grab;color:#bbb"><Rank /></el-icon><span class="block-icon">&#128220;</span><span class="block-title">证书资质</span></div>
                  <div v-for="(cert, idx) in form.certificates" :key="idx" class="sub-block">
                    <el-row :gutter="10">
                      <el-col :span="12"><el-form-item label="证书名称" size="small"><el-input v-model="cert.name" placeholder="例如：计算机二级" /></el-form-item></el-col>
                      <el-col :span="12"><el-form-item label="获得时间" size="small"><el-date-picker v-model="cert.date" type="month" value-format="YYYY-MM" style="width:100%" /></el-form-item></el-col>
                    </el-row>
                    <el-button type="danger" plain size="small" @click="form.certificates.splice(idx, 1)">删除</el-button>
                  </div>
                  <el-button type="default" plain size="small" @click="form.certificates.push({ name:'', date:'' })">+ 添加证书</el-button>
                </div>
                <div v-if="element === 'skills'" class="section-block draggable-block">
                  <div class="block-header"><el-icon class="drag-handle" style="cursor:grab;color:#bbb"><Rank /></el-icon><span class="block-icon">&#128736;</span><span class="block-title">专业技能</span></div>
                  <div class="skill-tags">
                    <el-tag v-for="(skill, idx) in form.skills" :key="idx" closable @close="form.skills.splice(idx, 1)" style="margin-right:6px;margin-bottom:6px;">{{ skill }}</el-tag>
                    <el-input v-if="addingSkill" ref="skillInputRef" v-model="newSkill" size="small" style="width:120px;" @keyup.enter="confirmAddSkill" @blur="confirmAddSkill" />
                    <el-button v-else size="small" @click="startAddSkill">+ 添加</el-button>
                  </div>
                </div>
              </template>
            </draggable>
          </div>
        </el-scrollbar>
      </div>
      <div class="edit-preview-panel">
        <div class="preview-toolbar">
          <span>实时预览</span>
          <div class="zoom-ctrl">
            <el-button size="small" text @click="zoom = Math.min(1.2, +(zoom + 0.1).toFixed(1))">+</el-button>
            <span class="zoom-val">{{ Math.round(zoom * 100) }}%</span>
            <el-button size="small" text @click="zoom = Math.max(0.3, +(zoom - 0.1).toFixed(1))">-</el-button>
            <el-button size="small" text @click="zoom = 0.65" style="margin-left:4px;font-size:11px;">适应</el-button>
          </div>
        </div>
        <div class="preview-scroll">
          <div class="preview-outer" ref="previewOuterRef">
            <ResumePreview :data="previewData" :zoom="zoom" />
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script lang="ts" setup>
import { ref, reactive, computed, onMounted, nextTick } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { ArrowLeft, Download, Rank } from '@element-plus/icons-vue'
import draggable from 'vuedraggable'
import html2canvas from 'html2canvas'
import { jsPDF } from 'jspdf'
import { getResume, saveResume } from '@/api/resume'
import ResumePreview from './ResumePreview.vue'

const router = useRouter()
const route = useRoute()
const isEdit = ref(false)
const saving = ref(false)
const pdfLoading = ref(false)
const addingSkill = ref(false)
const newSkill = ref('')
const skillInputRef = ref()
const zoom = ref(0.65)
const previewOuterRef = ref()

const templateOptions = [
  { id: 1, label: '经典蓝色' },
  { id: 2, label: '专业深灰' },
  { id: 3, label: '清新绿色' },
  { id: 4, label: '技术风格' },
  { id: 5, label: '简约白色' },
]
const currentTemplateId = ref<number | null>(1)

const sectionOrder = ref(['summary', 'education', 'campusExperiences', 'competitions', 'experience', 'projects', 'awards', 'certificates', 'skills'])

const form = reactive({
  id: null as number | null,
  templateId: null as number | null,
  title: '',
  status: 'VISIBLE',
  personal: { name: '', gender: '', phone: '', email: '', targetPosition: '' },
  education: [] as any[],
  campusExperiences: [] as any[],
  competitions: [] as any[],
  experience: [] as any[],
  projects: [] as any[],
  awards: [] as any[],
  certificates: [] as any[],
  skills: [] as string[],
  summary: ''
})

const previewData = computed(() => ({
  templateId: currentTemplateId.value,
  personal: form.personal,
  education: form.education,
  campusExperiences: form.campusExperiences,
  competitions: form.competitions,
  experience: form.experience,
  projects: form.projects,
  awards: form.awards,
  certificates: form.certificates,
  skills: form.skills,
  summary: form.summary,
  sectionOrder: sectionOrder.value
}))

const startAddSkill = () => { addingSkill.value = true; nextTick(() => skillInputRef.value?.focus()) }
const confirmAddSkill = () => {
  if (newSkill.value.trim()) form.skills.push(newSkill.value.trim())
  newSkill.value = ''; addingSkill.value = false
}

const getContent = () => JSON.stringify({
  personal: form.personal,
  education: form.education,
  campusExperiences: form.campusExperiences,
  competitions: form.competitions,
  experience: form.experience,
  projects: form.projects,
  awards: form.awards,
  certificates: form.certificates,
  skills: form.skills,
  summary: form.summary,
  sectionOrder: sectionOrder.value
})

const doSave = async (status: string) => {
  form.status = status
  if (!form.title.trim()) { ElMessage.warning('请填写简历标题'); return }
  if (!form.personal.name.trim()) { ElMessage.warning('请填写姓名'); return }
  saving.value = true
  try {
    const res = await saveResume({ id: form.id, templateId: currentTemplateId.value, title: form.title, status: form.status, content: getContent() })
    if (res.code === 0) { form.id = res.data?.id ?? form.id; isEdit.value = true; ElMessage.success(status === 'DRAFT' ? '草稿保存成功' : '简历保存成功') }
    else { ElMessage.error(res.message || '保存失败') }
  } catch (e: any) { ElMessage.error(e.message || '保存失败') }
  finally { saving.value = false }
}
const saveAsDraft = () => doSave('DRAFT')
const saveAndPublish = () => doSave('VISIBLE')

const handleDownloadPdf = async () => {
  const el = previewOuterRef.value
  if (!el) { ElMessage.error('预览区域未找到'); return }
  pdfLoading.value = true
  try {
    const canvas = await html2canvas(el, { scale: 2, useCORS: true, backgroundColor: '#ffffff', width: el.offsetWidth, height: el.offsetHeight })
    const imgData = canvas.toDataURL('image/jpeg', 0.95)
    const pdf = new jsPDF({ orientation: 'portrait', unit: 'mm', format: 'a4' })
    const pdfW = pdf.internal.pageSize.getWidth()
    const pdfH = (canvas.height * pdfW) / canvas.width
    pdf.addImage(imgData, 'JPEG', 0, 0, pdfW, pdfH)
    pdf.save((form.title || '我的简历') + '.pdf')
    ElMessage.success('PDF 下载成功')
  } catch (e: any) { ElMessage.error('PDF 生成失败：' + (e.message || '请重试')) }
  finally { pdfLoading.value = false }
}

const loadData = async () => {
  const qid = route.query.templateId
  if (qid) currentTemplateId.value = Number(qid)
  const id = route.params.id
  if (id) {
    isEdit.value = true
    try {
      const res = await getResume(Number(id))
      if (res.code === 0 && res.data) {
        const r = res.data
        form.id = r.id; form.templateId = r.templateId; form.title = r.title || ''; form.status = r.status || 'VISIBLE'
        currentTemplateId.value = r.templateId || 1
        if (r.content) {
          try {
            const c = typeof r.content === 'string' ? JSON.parse(r.content) : r.content
            Object.assign(form.personal, c.personal || {})
            form.education = c.education || []
            form.campusExperiences = c.campusExperiences || []
            form.competitions = c.competitions || []
            form.experience = c.experience || []
            form.projects = c.projects || []
            form.awards = c.awards || []
            form.certificates = c.certificates || []
            form.skills = c.skills || []
            form.summary = c.summary || ''
            if (c.sectionOrder && Array.isArray(c.sectionOrder)) sectionOrder.value = c.sectionOrder
          } catch {}
        }
      }
    } catch (e: any) { ElMessage.error(e.message || '加载简历失败') }
  }
}
onMounted(() => loadData())
</script>
<style scoped>
.resume-edit-layout { display: flex; flex-direction: column; height: calc(100vh - 60px); background: #f0f2f5; overflow: hidden; }
.edit-toolbar { display: flex; align-items: center; justify-content: space-between; padding: 8px 16px; background: #fff; border-bottom: 1px solid #e8e8e8; flex-shrink: 0; }
.toolbar-left, .toolbar-right { display: flex; align-items: center; gap: 8px; }
.edit-main { display: flex; flex: 1; overflow: hidden; }
.edit-form-panel { width: 440px; flex-shrink: 0; background: #fff; border-right: 1px solid #e8e8e8; overflow: hidden; }
.form-inner { padding: 14px; }
.edit-preview-panel { flex: 1; display: flex; flex-direction: column; background: #e2e8ef; overflow: hidden; }
.preview-toolbar { display: flex; align-items: center; justify-content: space-between; padding: 6px 16px; background: rgba(255,255,255,0.95); border-bottom: 1px solid #e0e0e0; font-size: 12.5px; color: #555; flex-shrink: 0; }
.zoom-ctrl { display: flex; align-items: center; gap: 2px; }
.zoom-val { font-size: 12px; min-width: 36px; text-align: center; color: #888; }
.preview-scroll { flex: 1; overflow: auto; padding: 20px; display: flex; justify-content: center; }
.preview-outer { transition: width 0.2s; }
.section-block { background: #fafbfc; border: 1px solid #f0f0f0; border-radius: 8px; padding: 12px; margin-bottom: 10px; }
.fixed-section { border-color: #e0e0e0; }
.draggable-block { border-left: 3px solid transparent; transition: border-color 0.2s; }
.draggable-block:hover { border-left-color: #409eff; }
.block-header { display: flex; align-items: center; gap: 8px; margin-bottom: 10px; cursor: default; }
.block-icon { font-size: 15px; }
.block-title { font-size: 13px; font-weight: 600; color: #333; }
.draggable-hint { font-size: 11px; color: #aaa; display: flex; align-items: center; gap: 4px; margin-bottom: 8px; padding: 0 4px; }
.sub-block { background: #fff; border: 1px solid #eee; border-radius: 6px; padding: 10px; margin-bottom: 8px; }
.skill-tags { display: flex; flex-wrap: wrap; align-items: center; gap: 4px; }
:deep(.drag-ghost) { opacity: 0.5; background: #e8f4ff; border-radius: 8px; }
:deep(.drag-handle:hover) { color: #409eff !important; }
</style>
