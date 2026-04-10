<template>
  <div class="template3-resume resume-content">
    <div class="basic-info">
      <div class="profile">
        <div v-if="!data.avatar?.hidden" class="avatar-section">
          <div class="avatar" :class="data.avatar?.shape || 'circle'">
            <img v-if="data.avatar?.src" :src="data.avatar.src" alt="avatar" />
            <span v-else class="avatar-placeholder">{{ getInitial(data.profile?.name) }}</span>
          </div>
        </div>
        <div v-if="data.profile?.name" class="name">{{ data.profile.name }}</div>
        <div class="profile-list">
          <div v-if="data.profile?.mobile" class="mobile">
            <svg width="12" height="12" viewBox="0 0 24 24" fill="none" :stroke="themeColor" stroke-width="2">
              <path d="M22 16.92v3a2 2 0 0 1-2.18 2 19.79 19.79 0 0 1-8.63-3.07A19.5 19.5 0 0 1 4.69 12.2 19.79 19.79 0 0 1 1.61 3.61 2 2 0 0 1 3.58 1h3a2 2 0 0 1 2 1.72c.127.96.361 1.903.7 2.81a2 2 0 0 1-.45 2.11L7.91 8.55A16 16 0 0 0 15 15.09l.92-.92a2 2 0 0 1 2.11-.45c.907.339 1.85.573 2.81.7A2 2 0 0 1 22 16.92z"/>
            </svg>
            {{ data.profile.mobile }}
          </div>
          <div v-if="data.profile?.email" class="email">
            <svg width="12" height="12" viewBox="0 0 24 24" fill="none" :stroke="themeColor" stroke-width="2">
              <path d="M4 4h16c1.1 0 2 .9 2 2v12c0 1.1-.9 2-2 2H4c-1.1 0-2-.9-2-2V6c0-1.1.9-2 2-2z"/>
              <polyline points="22,6 12,13 2,6"/>
            </svg>
            {{ data.profile.email }}
          </div>
          <div v-if="data.profile?.github" class="github">
            <svg width="12" height="12" viewBox="0 0 24 24" fill="none" :stroke="themeColor" stroke-width="2">
              <path d="M9 19c-5 1.5-5-2.5-7-3m14 6v-3.87a3.37 3.37 0 0 0-.94-2.61c3.14-.35 6.44-1.54 6.44-7A5.44 5.44 0 0 0 20 4.77 5.07 5.07 0 0 0 19.91 1S18.73.65 16 2.48a13.38 13.38 0 0 0-7 0C6.27.65 5.09 1 5.09 1A5.07 5.07 0 0 0 5 4.77a5.44 5.44 0 0 0-1.5 3.78c0 5.42 3.3 6.61 6.44 7A3.37 3.37 0 0 0 9 18.13V22"/>
            </svg>
            {{ data.profile.github }}
          </div>
          <div v-if="data.profile?.positionTitle" class="expect-job">
            <svg width="12" height="12" viewBox="0 0 24 24" fill="none" :stroke="themeColor" stroke-width="2">
              <path d="M20.84 4.61a5.5 5.5 0 0 0-7.78 0L12 5.67l-1.06-1.06a5.5 5.5 0 0 0-7.78 7.78l1.06 1.06L12 21.23l7.78-7.78 1.06-1.06a5.5 5.5 0 0 0 0-7.78z"/>
            </svg>
            职位：{{ data.profile.positionTitle }}
          </div>
        </div>
      </div>

      <!-- 教育背景 -->
      <div v-if="data.educationList && data.educationList.length" class="section section-education">
        <div class="section-title">教育背景</div>
        <div class="section-content">
          <div v-for="(edu, idx) in data.educationList" :key="idx" class="education-item">
            <div class="section-info">
              <span class="info-name">{{ edu.school }}</span>
              <span class="info-time">{{ formatDateRange(edu.edu_time) }}</span>
            </div>
            <div class="sub-info">{{ edu.major }} {{ edu.academic_degree ? `· ${edu.academic_degree}` : '' }}</div>
          </div>
        </div>
      </div>

      <!-- 个人作品 -->
      <div v-if="data.workList && data.workList.length" class="section section-work">
        <div class="section-title">个人作品</div>
        <div class="section-content">
          <div v-for="(work, idx) in data.workList" :key="idx" class="work-item">
            <span class="work-name">{{ work.work_name }}</span>
            <span v-if="work.work_desc" class="work-desc">{{ work.work_desc }}</span>
          </div>
        </div>
      </div>

      <!-- 自我介绍 -->
      <div v-if="hasAboutme" class="section section-aboutme">
        <div class="section-title">自我介绍</div>
        <div class="section-content">
          <div v-for="(item, idx) in aboutmeList" :key="idx" class="aboutme-item">{{ item }}</div>
        </div>
      </div>

      <!-- 专业技能 -->
      <div v-if="data.skillList && data.skillList.length" class="section section-skill">
        <div class="section-title">专业技能</div>
        <div class="section-content">
          <div v-for="(skill, idx) in data.skillList" :key="idx" class="skill-item">
            <span class="skill-name">{{ skill.skill_name }}</span>
          </div>
        </div>
      </div>

      <!-- 荣誉奖项 -->
      <div v-if="data.awardList && data.awardList.length" class="section section-award">
        <div class="section-title">荣誉奖项</div>
        <div class="section-content">
          <div v-for="(award, idx) in data.awardList" :key="idx" class="award-item">
            <span class="award-name">{{ award.award_info }}</span>
            <span v-if="award.award_time" class="award-time">{{ award.award_time }}</span>
          </div>
        </div>
      </div>
    </div>

    <div class="main-info">
      <!-- 工作经历 -->
      <div v-if="data.workExpList && data.workExpList.length" class="section section-work-exp">
        <div class="section-header">
          <h1 :style="{ backgroundColor: themeColor }">工作经历</h1>
        </div>
        <div class="section-content">
          <div v-for="(work, idx) in data.workExpList" :key="idx" class="section-item">
            <div class="section-info">
              <span class="company">{{ work.company_name }}</span>
              <span v-if="work.work_time" class="info-time">{{ formatDateRange(work.work_time) }}</span>
            </div>
            <div v-if="work.department_name" class="sub-info">{{ work.department_name }}</div>
            <div v-if="work.work_desc" class="work-description">{{ work.work_desc }}</div>
          </div>
        </div>
      </div>

      <!-- 项目经历 -->
      <div v-if="data.projectList && data.projectList.length" class="section section-project">
        <div class="section-header">
          <h1 :style="{ backgroundColor: themeColor }">项目经历</h1>
        </div>
        <div class="section-content">
          <div v-for="(proj, idx) in data.projectList" :key="idx" class="section-item">
            <div class="section-info">
              <span class="project-name">{{ proj.project_name }}</span>
              <span v-if="proj.project_time" class="info-time">{{ proj.project_time }}</span>
            </div>
            <div v-if="proj.project_role" class="sub-info">{{ proj.project_role }}</div>
            <div v-if="proj.project_desc" class="project-content">{{ proj.project_desc }}</div>
            <div v-if="proj.project_content" class="project-content">{{ proj.project_content }}</div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import type { ResumeConfig, ThemeConfig } from '@/types/resume'

const props = defineProps<{
  data: ResumeConfig
  theme?: ThemeConfig
}>()

const themeColor = computed(() => props.theme?.color || '#2f5785')

const aboutmeList = computed(() => {
  return props.data.aboutme?.aboutme_desc?.split('\n').filter(Boolean) || []
})

const hasAboutme = computed(() => {
  return aboutmeList.value.length > 0 && aboutmeList.value.join('').trim().length > 0
})

const formatDateRange = (dateRange: [string | null, string | null]) => {
  if (!dateRange || !dateRange[0]) return ''
  const start = dateRange[0]
  const end = dateRange[1] || '至今'
  return `${start} — ${end}`
}

function getInitial(name?: string): string {
  if (!name) return '姓'
  return name.charAt(0)
}
</script>

<style scoped>
.template3-resume {
  width: 794px;
  min-height: 942px;
  margin-bottom: 60px;
  box-shadow: 0px 2px 4px 1px rgba(0, 0, 0, 0.15);
}

@media (max-width: 794px) {
  .template3-resume {
    width: 100%;
  }
}

@media print {
  @page {
    size: A4;
  }

  .template3-resume {
    width: 100%;
    box-shadow: none;
  }
}

.template3-resume {
  .basic-info {
    padding: 24px 18px 24px 24px;
  }

  .main-info {
    padding: 24px 24px 32px 32px;
    background: #fff;
  }

  .section-title {
    font-size: 16px;
    letter-spacing: 0;
    font-weight: 600;
    color: rgba(0, 0, 0, 0.85);
    margin-bottom: 12px;
  }

  .basic-info .section {
    padding-top: 28px;
    border-radius: 0;
  }

  section {
    &:not(:first-of-type) {
      margin-top: 24px;
    }

    .section-info {
      font-size: 18px;
      line-height: 24px;
      color: rgba(0, 0, 0, 0.85);
      margin-bottom: 8px;

      display: flex;
      justify-content: space-between;
      align-items: center;
    }
  }

  .basic-info section .section-info {
    .info-name {
      max-width: 198px;
      padding-right: 4px;
    }
  }

  .basic-info .avatar-section {
    display: flex;
    justify-content: center;
    margin-bottom: 12px;
  }

  .basic-info .avatar {
    width: 84px;
    height: 84px;
    border-radius: 50%;
    background: #f2f2f2;
    display: flex;
    align-items: center;
    justify-content: center;
    overflow: hidden;
    border: 2px solid #e8e8e8;
  }

  .basic-info .avatar.circle {
    border-radius: 50%;
  }

  .basic-info .avatar.square {
    border-radius: 4px;
  }

  .basic-info .avatar img {
    width: 100%;
    height: 100%;
    object-fit: cover;
  }

  .basic-info .avatar-placeholder {
    font-size: 24px;
    color: #999;
  }

  .basic-info .name {
    margin: 0 0 8px 4px;
    font-size: 24px;
    line-height: 36px;
    font-weight: 600;
    color: rgba(0, 0, 0, 0.85);
  }

  .basic-info .profile {
    margin-bottom: 16px;

    .profile-list {
      display: flex;
      flex-wrap: wrap;
      margin-left: 4px;

      > div {
        display: flex;
        align-items: center;
        flex: 220px 1;
        margin-right: 4px;
        margin-bottom: 4px;
      }

      svg {
        margin-right: 6px;
      }
    }
  }

  .basic-info .section-skill {
    .skill-item {
      display: flex;
      justify-content: space-between;
      margin-bottom: 4px;
    }
  }

  .basic-info section .sub-info {
    color: rgba(0, 0, 0, 0.45);
    font-size: 12px;
    margin-top: 2px;
  }

  .basic-info .education-item {
    &:not(:first-of-type) {
      margin-top: 8px;
    }
  }

  .basic-info .section-work {
    > div {
      line-height: 24px;
    }

    .work-desc {
      font-size: 12px;
      margin-left: 8px;
      color: rgba(0, 0, 0, 0.45);
    }
  }

  .main-info section {
    margin-bottom: 16px;

    .section-header {
      display: flex;
      margin-bottom: 10px;

      h1 {
        position: relative;
        font-size: 16px;
        font-weight: bold;
        color: #fff;
        width: max-content;
        line-height: 24px;
        padding: 0 100px 0 10px;
        border-top-left-radius: 3px;
        border-bottom-left-radius: 3px;
        margin-left: -12px;
        margin: 0;
      }
      h1:before {
        content: '';
        display: block;
        width: 18px;
        height: 18px;
        background: #fff;
        position: absolute;
        right: -9px;
        top: 4px;
        transform: rotate(45deg);
      }
    }

    .section-detail {
      letter-spacing: 1.2px;

      &:not(:first-of-type) {
        margin-top: 4px;
      }
    }
  }

  .main-info .section-project,
  .main-info .section-work-exp {
    .section-item:not(:first-of-type) {
      margin-top: 18px;
    }

    .section-info .sub-info,
    .section-info .info-time {
      color: rgba(0, 0, 0, 0.45);
      font-size: 12px;
      margin-left: 8px;
      font-weight: 300;
    }

    .work-description,
    .project-content {
      white-space: pre-wrap;
      font-size: 12px;
      line-height: 1.6;
      color: rgba(0, 0, 0, 0.65);
      margin-top: 4px;
    }
  }
}
</style>
