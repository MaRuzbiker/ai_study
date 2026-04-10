<template>
  <div class="module-form">
    <el-scrollbar height="100%">
      <div class="form-content">

        <!-- 个人信息 -->
        <template v-if="selectedModule === 'profile'">
          <div class="form-section-title">个人信息</div>
          <el-form label-width="80px" label-position="left" size="default">
            <el-form-item label="头像">
              <div class="avatar-upload">
                <div class="avatar-preview" :class="avatarShape">
                  <img v-if="props.config.avatar?.src" :src="props.config.avatar.src" alt="avatar" />
                  <span v-else class="avatar-placeholder">{{ getInitial(props.config.profile?.name) }}</span>
                </div>
                <input
                  type="file"
                  accept="image/*"
                  class="avatar-input"
                  @change="handleAvatarUpload"
                />
                <el-button type="primary" plain size="small" @click="triggerAvatarUpload">
                  上传头像
                </el-button>
              </div>
            </el-form-item>
            
            <el-form-item label="头像形状">
              <el-radio-group v-model="avatarShape">
                <el-radio label="circle">圆形</el-radio>
                <el-radio label="square">方形</el-radio>
              </el-radio-group>
            </el-form-item>
            
            <el-form-item label="显示头像">
              <el-switch v-model="avatarHidden" />
            </el-form-item>
            
            <el-form-item label="姓名" required>
              <el-input v-model="config.profile!.name" placeholder="张三" maxlength="30" />
            </el-form-item>
            <el-form-item label="手机号">
              <el-input v-model="config.profile!.mobile" placeholder="138xxxx8888" maxlength="20" />
            </el-form-item>
            <el-form-item label="邮箱">
              <el-input v-model="config.profile!.email" placeholder="example@qq.com" maxlength="50" />
            </el-form-item>
            <el-form-item label="GitHub">
              <el-input v-model="config.profile!.github" placeholder="github.com/username" maxlength="100" />
            </el-form-item>
            <el-form-item label="知乎">
              <el-input v-model="config.profile!.zhihu" placeholder="zhihu.com/people/xxx" maxlength="100" />
            </el-form-item>
            <el-form-item label="工作年限">
              <el-input v-model="config.profile!.workExpYear" placeholder="例如：3年" maxlength="20" />
            </el-form-item>
            <el-form-item label="所在城市">
              <el-input v-model="config.profile!.workPlace" placeholder="例如：北京" maxlength="30" />
            </el-form-item>
            <el-form-item label="职位名称">
              <el-input v-model="config.profile!.positionTitle" placeholder="例如：前端开发工程师" maxlength="50" />
            </el-form-item>
          </el-form>
        </template>

        <!-- 教育背景 -->
        <template v-else-if="selectedModule === 'educationList'">
          <div class="form-section-title">教育背景</div>
          <div v-for="(item, idx) in config.educationList!" :key="idx" class="sub-block">
            <div class="sub-block-header">
              <span>条目 {{ idx + 1 }}</span>
              <el-button type="danger" plain size="small" @click="removeItem('educationList', idx)">删除</el-button>
            </div>
            <el-form label-width="72px" label-position="left" size="small">
              <el-form-item label="学校">
                <el-input v-model="item.school" placeholder="学校名称" maxlength="100" />
              </el-form-item>
              <el-form-item label="专业">
                <el-input v-model="item.major" placeholder="专业名称" maxlength="100" />
              </el-form-item>
              <el-form-item label="学历">
                <el-select v-model="item.academic_degree" placeholder="请选择" style="width:100%">
                  <el-option label="高中" value="高中" />
                  <el-option label="大专" value="大专" />
                  <el-option label="本科" value="本科" />
                  <el-option label="硕士" value="硕士" />
                  <el-option label="博士" value="博士" />
                </el-select>
              </el-form-item>
              <el-form-item label="时间范围">
                <el-date-picker
                  v-model="item.edu_time"
                  type="daterange"
                  range-separator="至"
                  start-placeholder="开始时间"
                  end-placeholder="结束时间"
                  value-format="YYYY-MM-DD"
                  style="width:100%"
                />
              </el-form-item>
            </el-form>
          </div>
          <el-button type="default" plain class="add-btn" @click="addItem('educationList', { school: '', major: '', academic_degree: '', edu_time: null })">
            + 添加教育经历
          </el-button>
        </template>

        <!-- 工作经历 -->
        <template v-else-if="selectedModule === 'workExpList'">
          <div class="form-section-title">工作经历</div>
          <div v-for="(item, idx) in config.workExpList!" :key="idx" class="sub-block">
            <div class="sub-block-header">
              <span>条目 {{ idx + 1 }}</span>
              <el-button type="danger" plain size="small" @click="removeItem('workExpList', idx)">删除</el-button>
            </div>
            <el-form label-width="72px" label-position="left" size="small">
              <el-form-item label="公司名称">
                <el-input v-model="item.company_name" placeholder="公司名称" maxlength="100" />
              </el-form-item>
              <el-form-item label="部门">
                <el-input v-model="item.department_name" placeholder="部门/团队名称" maxlength="100" />
              </el-form-item>
              <el-form-item label="时间范围">
                <el-date-picker
                  v-model="item.work_time"
                  type="daterange"
                  range-separator="至"
                  start-placeholder="开始时间"
                  end-placeholder="结束时间"
                  value-format="YYYY-MM-DD"
                  style="width:100%"
                />
              </el-form-item>
              <el-form-item label="工作描述">
                <el-input
                  v-model="item.work_desc"
                  type="textarea"
                  :rows="3"
                  placeholder="描述工作职责、技术栈和取得的成果"
                  maxlength="1000"
                  show-word-limit
                />
              </el-form-item>
            </el-form>
          </div>
          <el-button type="default" plain class="add-btn" @click="addItem('workExpList', { company_name: '', department_name: '', work_desc: '', work_time: null })">
            + 添加工作经历
          </el-button>
        </template>

        <!-- 项目经历 -->
        <template v-else-if="selectedModule === 'projectList'">
          <div class="form-section-title">项目经历</div>
          <div v-for="(item, idx) in config.projectList!" :key="idx" class="sub-block">
            <div class="sub-block-header">
              <span>条目 {{ idx + 1 }}</span>
              <el-button type="danger" plain size="small" @click="removeItem('projectList', idx)">删除</el-button>
            </div>
            <el-form label-width="72px" label-position="left" size="small">
              <el-form-item label="项目名称">
                <el-input v-model="item.project_name" placeholder="项目名称" maxlength="100" />
              </el-form-item>
              <el-form-item label="担任角色">
                <el-input v-model="item.project_role" placeholder="例如：后端开发" maxlength="50" />
              </el-form-item>
              <el-form-item label="项目时间">
                <el-date-picker
                  v-model="item.project_time"
                  type="monthrange"
                  range-separator="至"
                  start-placeholder="开始月份"
                  end-placeholder="结束月份"
                  value-format="YYYY-MM"
                  style="width:100%"
                />
              </el-form-item>
              <el-form-item label="项目简介">
                <el-input
                  v-model="item.project_desc"
                  type="textarea"
                  :rows="2"
                  placeholder="一句话介绍项目背景"
                  maxlength="300"
                />
              </el-form-item>
              <el-form-item label="工作内容">
                <el-input
                  v-model="item.project_content"
                  type="textarea"
                  :rows="4"
                  placeholder="详细描述个人职责、技术栈、遇到的挑战和成果"
                  maxlength="2000"
                  show-word-limit
                />
              </el-form-item>
            </el-form>
          </div>
          <el-button type="default" plain class="add-btn" @click="addItem('projectList', { project_name: '', project_role: '', project_desc: '', project_content: '', project_time: null })">
            + 添加项目经历
          </el-button>
        </template>

        <!-- 专业技能 -->
        <template v-else-if="selectedModule === 'skillList'">
          <div class="form-section-title">专业技能</div>
          <div v-for="(item, idx) in config.skillList!" :key="idx" class="sub-block">
            <div class="sub-block-header">
              <span>技能 {{ idx + 1 }}</span>
              <el-button type="danger" plain size="small" @click="removeItem('skillList', idx)">删除</el-button>
            </div>
            <el-form label-width="72px" label-position="left" size="small">
              <el-form-item label="技能名称">
                <el-input v-model="item.skill_name" placeholder="例如：Vue.js" maxlength="50" />
              </el-form-item>
              <el-form-item label="掌握程度">
                <el-slider v-model="item.skill_level!" :min="1" :max="5" :step="1" show-stops :marks="skillMarks" />
              </el-form-item>
              <el-form-item label="补充说明">
                <el-input v-model="item.skill_desc" placeholder="可选，补充说明熟悉程度或使用经验" maxlength="200" />
              </el-form-item>
            </el-form>
          </div>
          <el-button type="default" plain class="add-btn" @click="addItem('skillList', { skill_name: '', skill_level: 3, skill_desc: '' })">
            + 添加技能
          </el-button>
        </template>

        <!-- 荣誉奖项 -->
        <template v-else-if="selectedModule === 'awardList'">
          <div class="form-section-title">荣誉奖项</div>
          <div v-for="(item, idx) in config.awardList!" :key="idx" class="sub-block">
            <div class="sub-block-header">
              <span>奖项 {{ idx + 1 }}</span>
              <el-button type="danger" plain size="small" @click="removeItem('awardList', idx)">删除</el-button>
            </div>
            <el-form label-width="72px" label-position="left" size="small">
              <el-form-item label="奖项名称">
                <el-input v-model="item.award_info" placeholder="例如：国家奖学金" maxlength="200" />
              </el-form-item>
              <el-form-item label="获得时间">
                <el-date-picker
                  v-model="item.award_time"
                  type="month"
                  value-format="YYYY-MM"
                  placeholder="选择时间"
                  style="width:100%"
                />
              </el-form-item>
            </el-form>
          </div>
          <el-button type="default" plain class="add-btn" @click="addItem('awardList', { award_info: '', award_time: '' })">
            + 添加奖项
          </el-button>
        </template>

        <!-- 个人作品 -->
        <template v-else-if="selectedModule === 'workList'">
          <div class="form-section-title">个人作品</div>
          <div v-for="(item, idx) in config.workList!" :key="idx" class="sub-block">
            <div class="sub-block-header">
              <span>作品 {{ idx + 1 }}</span>
              <el-button type="danger" plain size="small" @click="removeItem('workList', idx)">删除</el-button>
            </div>
            <el-form label-width="72px" label-position="left" size="small">
              <el-form-item label="作品名称">
                <el-input v-model="item.work_name" placeholder="作品名称" maxlength="100" />
              </el-form-item>
              <el-form-item label="作品描述">
                <el-input
                  v-model="item.work_desc"
                  type="textarea"
                  :rows="2"
                  placeholder="简要描述作品的功能和技术栈"
                  maxlength="300"
                />
              </el-form-item>
              <el-form-item label="访问链接">
                <el-input v-model="item.visit_link" placeholder="https://..." maxlength="300" />
              </el-form-item>
            </el-form>
          </div>
          <el-button type="default" plain class="add-btn" @click="addItem('workList', { work_name: '', work_desc: '', visit_link: '' })">
            + 添加作品
          </el-button>
        </template>

        <!-- 自我介绍 -->
        <template v-else-if="selectedModule === 'aboutme'">
          <div class="form-section-title">自我介绍</div>
          <el-form label-position="top" size="default">
            <el-form-item label="自我描述">
              <el-input
                v-model="config.aboutme!.aboutme_desc"
                type="textarea"
                :rows="6"
                placeholder="简要描述你的优势、职业目标和性格特点"
                maxlength="1000"
                show-word-limit
              />
            </el-form-item>
          </el-form>
        </template>

        <!-- 主题配色 -->
        <template v-else-if="selectedModule === 'theme'">
          <div class="form-section-title">主题配色</div>
          <div class="theme-section">
            <div class="theme-label">选择主题色</div>
            <div class="color-swatches">
              <div
                v-for="color in PRESET_THEME_COLORS"
                :key="color"
                class="color-swatch"
                :class="{ active: config.theme?.color === color }"
                :style="{ backgroundColor: color }"
                @click="applyTheme(color)"
              />
            </div>
            <div class="theme-label" style="margin-top:16px;">自定义颜色</div>
            <el-color-picker
              :model-value="config.theme?.color"
              @change="applyTheme"
              size="default"
            />
          </div>
        </template>

      </div>
    </el-scrollbar>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import type { ResumeConfig } from '@/types/resume'
import { PRESET_THEME_COLORS } from '@/types/resume'

const props = defineProps<{
  selectedModule: string
  config: ResumeConfig
}>()

const emit = defineEmits<{
  (e: 'update', partial: Partial<ResumeConfig>): void
}>()

const skillMarks = {
  1: '入门',
  2: '了解',
  3: '熟悉',
  4: '精通',
  5: '专家'
}

const getInitial = (name?: string) => {
  if (!name) return '?'
  return name.charAt(0).toUpperCase()
}

// 处理avatar的默认值和双向绑定
const avatarShape = computed({
  get: () => props.config.avatar?.shape || 'circle',
  set: (value) => {
    emit('update', {
      avatar: {
        ...props.config.avatar,
        shape: value
      }
    })
  }
})

const avatarHidden = computed({
  get: () => props.config.avatar?.hidden || false,
  set: (value) => {
    emit('update', {
      avatar: {
        ...props.config.avatar,
        hidden: value
      }
    })
  }
})

function triggerAvatarUpload() {
  const input = document.querySelector('.avatar-input') as HTMLInputElement
  if (input) input.click()
}

function handleAvatarUpload(event: Event) {
  const input = event.target as HTMLInputElement
  if (input.files && input.files[0]) {
    const file = input.files[0]
    const reader = new FileReader()
    reader.onload = (e) => {
      emit('update', {
        avatar: {
          ...props.config.avatar,
          src: e.target?.result as string
        }
      })
    }
    reader.readAsDataURL(file)
  }
}

function applyTheme(color: string) {
  emit('update', {
    theme: { color, tagColor: color }
  })
}

function addItem(key: keyof ResumeConfig, item: unknown) {
  const list = [...((props.config[key] as unknown[]) || [])]
  emit('update', { [key]: [...list, item] } as unknown as Partial<ResumeConfig>)
}

function removeItem(key: keyof ResumeConfig, idx: number) {
  const list = [...((props.config[key] as unknown[]) || [])]
  list.splice(idx, 1)
  emit('update', { [key]: list } as unknown as Partial<ResumeConfig>)
}
</script>

<style scoped>
.module-form {
  height: 100%;
  background: #fff;
}

.form-content {
  padding: 16px;
}

.form-section-title {
  font-size: 14px;
  font-weight: 600;
  color: #333;
  margin-bottom: 14px;
  padding-bottom: 8px;
  border-bottom: 2px solid #409eff;
  display: inline-block;
}

.sub-block {
  background: #fafbfc;
  border: 1px solid #eee;
  border-radius: 8px;
  padding: 12px;
  margin-bottom: 10px;
}

.sub-block-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 10px;
  font-size: 12px;
  color: #888;
  font-weight: 500;
}

.add-btn {
  width: 100%;
  border-style: dashed;
  color: #409eff;
}

.theme-section {
  padding: 4px 0;
}

.theme-label {
  font-size: 12px;
  color: #666;
  margin-bottom: 8px;
}

.color-swatches {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}

.color-swatch {
  width: 32px;
  height: 32px;
  border-radius: 6px;
  cursor: pointer;
  border: 2px solid transparent;
  transition: transform 0.15s, border-color 0.15s;
}

.color-swatch:hover {
  transform: scale(1.12);
}

.color-swatch.active {
  border-color: #333;
  box-shadow: 0 0 0 2px rgba(0, 0, 0, 0.2);
}

/* 头像上传样式 */
.avatar-upload {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.avatar-preview {
  width: 80px;
  height: 80px;
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
  background: linear-gradient(135deg, #e8f0fe 0%, #d2e3fc 100%);
  font-size: 32px;
  font-weight: 700;
  color: #2f5785;
  border: 2px dashed #409eff;
}

.avatar-preview.circle {
  border-radius: 50%;
}

.avatar-preview.square {
  border-radius: 8px;
}

.avatar-preview img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.avatar-input {
  display: none;
}

.avatar-placeholder {
  color: #2f5785;
}</style>
