<template>
  <div class="resume-generate">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>AI 简历生成器</span>
          <el-button text @click="router.back()"><el-icon><ArrowLeft /></el-icon>返回</el-button>
        </div>
      </template>

      <el-row :gutter="24">
        <!-- 左侧：基础信息表单 -->
        <el-col :span="10">
          <div class="form-panel">
            <div class="panel-title">填写基本信息</div>

            <el-form :model="basicForm" label-width="100px" size="default">
              <el-form-item label="姓名" required>
                <el-input v-model="basicForm.userName" placeholder="请输入姓名" />
              </el-form-item>
              <el-form-item label="目标岗位">
                <el-input v-model="basicForm.targetPosition" placeholder="例如：Java 开发工程师" />
              </el-form-item>
              <el-form-item label="学历">
                <el-select v-model="basicForm.education" placeholder="请选择" style="width:100%">
                  <el-option label="高中" value="高中" />
                  <el-option label="大专" value="大专" />
                  <el-option label="本科" value="本科" />
                  <el-option label="硕士" value="硕士" />
                  <el-option label="博士" value="博士" />
                </el-select>
              </el-form-item>
              <el-form-item label="工作年限">
                <el-select v-model="basicForm.workYears" placeholder="请选择" style="width:100%">
                  <el-option label="在校生/应届生" value="在校生/应届生" />
                  <el-option label="1年以下" value="1年以下" />
                  <el-option label="1-3年" value="1-3年" />
                  <el-option label="3-5年" value="3-5年" />
                  <el-option label="5-10年" value="5-10年" />
                  <el-option label="10年以上" value="10年以上" />
                </el-select>
              </el-form-item>
              <el-form-item label="性别">
                <el-radio-group v-model="basicForm.gender"><el-radio label="男" /><el-radio label="女" /></el-radio-group>
              </el-form-item>
              <el-form-item label="电话">
                <el-input v-model="basicForm.phone" placeholder="手机号码" />
              </el-form-item>
              <el-form-item label="邮箱">
                <el-input v-model="basicForm.email" placeholder="电子邮箱" />
              </el-form-item>

              <el-divider content-position="left">补充信息（可选）</el-divider>

              <el-form-item label="工作经历">
                <el-input v-model="basicForm.workExperience" type="textarea" :rows="3" placeholder="请描述工作经历，如：公司名、职位、主要职责和成绩（用换行分隔不同公司）" />
              </el-form-item>
              <el-form-item label="项目经历">
                <el-input v-model="basicForm.projects" type="textarea" :rows="3" placeholder="请描述项目经历，如：项目名、角色、技术栈、个人贡献（用换行分隔）" />
              </el-form-item>
              <el-form-item label="技能清单">
                <el-input v-model="basicForm.skills" placeholder="用逗号分隔，如：Java, Spring Boot, MySQL, Redis" />
              </el-form-item>
              <el-form-item label="自我评价">
                <el-input v-model="basicForm.selfEvaluation" type="textarea" :rows="3" placeholder="简单描述你的优势和特点" />
              </el-form-item>
              <el-form-item label="补充说明">
                <el-input v-model="basicForm.additionalInfo" type="textarea" :rows="2" placeholder="其他想让 AI 了解的信息" />
              </el-form-item>

              <el-form-item>
                <el-button type="primary" :loading="generating" @click="handleGenerate" style="width:100%;">
                  <span v-if="!generating">&#129302; 一键生成简历</span>
                  <span v-else>AI 正在生成中...</span>
                </el-button>
              </el-form-item>
            </el-form>
          </div>
        </el-col>

        <!-- 右侧：生成结果 -->
        <el-col :span="14">
          <div class="result-panel">
            <div class="panel-title">
              生成结果
              <div v-if="generated" style="float:right;">
                <el-button size="small" type="default" @click="handleRegenerate">重新生成</el-button>
                <el-button size="small" type="primary" @click="handleSave">保存到简历</el-button>
              </div>
            </div>

            <div v-if="generating" class="generating-state">
              <el-icon class="is-loading" style="font-size:48px;color:#409eff;margin-bottom:16px;"><Loading /></el-icon>
              <p style="color:#666;">AI 正在根据您的信息生成专业简历，请稍候...</p>
            </div>

            <div v-else-if="!generated && !generating" class="empty-state">
              <div class="empty-icon">&#128203;</div>
              <p style="color:#999;">填写左侧表单后，点击「一键生成简历」</p>
            </div>

            <div v-else class="generated-result">
              <div v-if="isJsonResult" class="result-json">
                <pre>{{ formattedJson }}</pre>
              </div>
              <div v-else class="result-markdown" v-html="renderedMarkdown"></div>
            </div>
          </div>
        </el-col>
      </el-row>
    </el-card>
  </div>
</template>

<script lang="ts" setup>
import { ref, computed, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { ArrowLeft, Loading } from '@element-plus/icons-vue'
import { generateResume, saveResume } from '@/api/resume'

const router = useRouter()

const basicForm = reactive({
  userName: '', gender: '', phone: '', email: '',
  education: '', workYears: '', targetPosition: '',
  workExperience: '', projects: '', skills: '',
  selfEvaluation: '', additionalInfo: ''
})

const generated = ref(false)
const generating = ref(false)
const generatedContent = ref('')
const isJsonResult = ref(false)

const formattedJson = computed(() => {
  try { return JSON.stringify(JSON.parse(generatedContent.value), null, 2) } catch { return generatedContent.value }
})

const renderedMarkdown = computed(() => {
  return generatedContent.value
    .replace(/^### (.+)$/gm, '<h3>$1</h3>')
    .replace(/^## (.+)$/gm, '<h2>$1</h2>')
    .replace(/^# (.+)$/gm, '<h1>$1</h1>')
    .replace(/\*\*(.+?)\*\*/g, '<strong>$1</strong>')
    .replace(/\n\n/g, '</p><p>')
    .replace(/\n/g, '<br/>')
})

const handleGenerate = async () => {
  if (!basicForm.userName.trim()) { ElMessage.warning('请填写姓名'); return }
  generating.value = true; generated.value = false
  try {
    const res = await generateResume(basicForm)
    if (res.code === 0) {
      generatedContent.value = res.data || ''
      isJsonResult.value = generatedContent.value.trim().startsWith('{')
      generated.value = true
    } else { ElMessage.error(res.message || '生成失败') }
  } catch (e: any) { ElMessage.error(e.message || '生成失败') }
  finally { generating.value = false }
}

const handleRegenerate = () => handleGenerate()

const handleSave = async () => {
  if (!generated.value) { ElMessage.warning('请先生成简历'); return }
  try {
    const payload = {
      title: basicForm.userName + ' - ' + (basicForm.targetPosition || '简历'),
      status: 'VISIBLE',
      content: isJsonResult.value ? generatedContent.value : JSON.stringify({ raw: generatedContent.value, personal: { name: basicForm.userName, gender: basicForm.gender, phone: basicForm.phone, email: basicForm.email, targetPosition: basicForm.targetPosition }, summary: basicForm.selfEvaluation })
    }
    const res = await saveResume(payload)
    if (res.code === 0) {
      ElMessage.success('简历已保存！')
      router.push('/resume')
    } else { ElMessage.error(res.message || '保存失败') }
  } catch (e: any) { ElMessage.error(e.message || '保存失败') }
}
</script>

<style scoped>
.card-header { display: flex; justify-content: space-between; align-items: center; }
.form-panel, .result-panel { padding: 8px; }
.panel-title { font-size: 15px; font-weight: 600; margin-bottom: 16px; color: #333; }
.generating-state, .empty-state { text-align: center; padding: 80px 0; }
.empty-icon { font-size: 64px; margin-bottom: 16px; }
.generated-result { max-height: 600px; overflow-y: auto; border: 1px solid #eee; border-radius: 8px; padding: 20px; background: #fafafa; }
.result-json pre { white-space: pre-wrap; word-break: break-all; font-size: 13px; color: #333; }
.result-markdown { font-size: 14px; line-height: 1.8; color: #333; }
.result-markdown :deep(h1), .result-markdown :deep(h2), .result-markdown :deep(h3) { margin-top: 16px; margin-bottom: 8px; }
</style>
