<template>
  <div class="tpl-modern resume-content">
    <div class="basic-info">
      <!-- 头像 -->
      <div v-if="!data.avatar?.hidden" class="avatar-section">
        <div class="avatar" :class="data.avatar?.shape || 'circle'">
          <img v-if="data.avatar?.src" :src="data.avatar.src" alt="avatar" />
          <span v-else class="avatar-placeholder">{{ getInitial(data.profile?.name) }}</span>
        </div>
      </div>

      <!-- 个人信息 -->
      <div class="profile">
        <div class="profile-name">{{ data.profile?.name || '姓名' }}</div>
        <div class="profile-list">
          <div v-if="data.profile?.mobile" class="profile-item">
            <svg width="12" height="12" viewBox="0 0 24 24" fill="none" :stroke="themeColor" stroke-width="2">
              <path d="M22 16.92v3a2 2 0 0 1-2.18 2 19.79 19.79 0 0 1-8.63-3.07A19.5 19.5 0 0 1 4.69 12.2 19.79 19.79 0 0 1 1.61 3.61 2 2 0 0 1 3.58 1h3a2 2 0 0 1 2 1.72c.127.96.361 1.903.7 2.81a2 2 0 0 1-.45 2.11L7.91 8.55A16 16 0 0 0 15 15.09l.92-.92a2 2 0 0 1 2.11-.45c.907.339 1.85.573 2.81.7A2 2 0 0 1 22 16.92z"/>
            </svg>
            <span>{{ data.profile.mobile }}</span>
          </div>
          <div v-if="data.profile?.email" class="profile-item">
            <svg width="12" height="12" viewBox="0 0 24 24" fill="none" :stroke="themeColor" stroke-width="2">
              <path d="M4 4h16c1.1 0 2 .9 2 2v12c0 1.1-.9 2-2 2H4c-1.1 0-2-.9-2-2V6c0-1.1.9-2 2-2z"/>
              <polyline points="22,6 12,13 2,6"/>
            </svg>
            <span>{{ data.profile.email }}</span>
          </div>
          <div v-if="data.profile?.github" class="profile-item link" @click="openLink(data.profile.github)">
            <svg width="12" height="12" viewBox="0 0 24 24" fill="none" :stroke="themeColor" stroke-width="2">
              <path d="M9 19c-5 1.5-5-2.5-7-3m14 6v-3.87a3.37 3.37 0 0 0-.94-2.61c3.14-.35 6.44-1.54 6.44-7A5.44 5.44 0 0 0 20 4.77 5.07 5.07 0 0 0 19.91 1S18.73.65 16 2.48a13.38 13.38 0 0 0-7 0C6.27.65 5.09 1 5.09 1A5.07 5.07 0 0 0 5 4.77a5.44 5.44 0 0 0-1.5 3.78c0 5.42 3.3 6.61 6.44 7A3.37 3.37 0 0 0 9 18.13V22"/>
            </svg>
            <span>{{ data.profile.github }}</span>
          </div>
          <div v-if="data.profile?.zhihu" class="profile-item link" @click="openLink(data.profile.zhihu)">
            <svg width="12" height="12" viewBox="0 0 24 24" fill="none" :stroke="themeColor" stroke-width="2">
              <circle cx="12" cy="12" r="10"/>
              <line x1="12" y1="8" x2="12" y2="12"/>
              <line x1="12" y1="16" x2="12.01" y2="16"/>
            </svg>
            <span>{{ data.profile.zhihu }}</span>
          </div>
          <div v-if="data.profile?.workExpYear" class="profile-item">
            <svg width="12" height="12" viewBox="0 0 24 24" fill="none" :stroke="themeColor" stroke-width="2">
              <circle cx="12" cy="12" r="10"/>
              <polyline points="12 6 12 12 16 14"/>
            </svg>
            <span>工作经验：{{ data.profile.workExpYear }}</span>
          </div>
          <div v-if="data.profile?.workPlace" class="profile-item">
            <svg width="12" height="12" viewBox="0 0 24 24" fill="none" :stroke="themeColor" stroke-width="2">
              <path d="M21 10c0 7-9 13-9 13s-9-6-9-13a9 9 0 0 1 18 0z"/>
              <circle cx="12" cy="10" r="3"/>
            </svg>
            <span>期望工作地：{{ data.profile.workPlace }}</span>
          </div>
          <div v-if="data.profile?.positionTitle" class="profile-item">
            <svg width="12" height="12" viewBox="0 0 24 24" fill="none" :stroke="themeColor" stroke-width="2">
              <path d="M20.84 4.61a5.5 5.5 0 0 0-7.78 0L12 5.67l-1.06-1.06a5.5 5.5 0 0 0-7.78 7.78l1.06 1.06L12 21.23l7.78-7.78 1.06-1.06a5.5 5.5 0 0 0 0-7.78z"/>
            </svg>
            <span>职位：{{ data.profile.positionTitle }}</span>
          </div>
        </div>
      </div>

      <!-- 自我介绍 -->
      <div v-if="hasAboutme" class="section section-aboutme">
        <div class="section-title" :style="{ color: themeColor }">自我介绍</div>
        <div class="section-content">
          <p v-for="(item, idx) in aboutmeList" :key="idx">{{ item }}</p>
        </div>
      </div>

      <!-- 教育背景 -->
      <div v-if="data.educationList && data.educationList.length" class="section section-education">
        <div class="section-title" :style="{ color: themeColor }">教育背景</div>
        <div class="section-content">
          <div v-for="(edu, idx) in data.educationList" :key="idx" class="education-item">
            <div class="item-title">{{ edu.school }}</div>
            <div class="item-sub">{{ edu.major }}</div>
            <div v-if="edu.academic_degree" class="item-desc">{{ edu.academic_degree }}</div>
            <div v-if="edu.edu_time" class="item-date">{{ formatDateRange(edu.edu_time) }}</div>
          </div>
        </div>
      </div>

      <!-- 个人作品 -->
      <div v-if="data.workList && data.workList.length" class="section section-work">
        <div class="section-title" :style="{ color: themeColor }">个人作品</div>
        <div class="section-content">
          <div v-for="(work, idx) in data.workList" :key="idx">
            <span class="info-name">{{ work.work_name }}</span>
            <span v-if="work.work_desc" class="sub-info">{{ work.work_desc }}</span>
          </div>
        </div>
      </div>

      <!-- 专业技能 -->
      <div v-if="data.skillList && data.skillList.length" class="section section-skill">
        <div class="section-title" :style="{ color: themeColor }">专业技能</div>
        <div class="section-content">
          <div v-for="(skill, idx) in data.skillList" :key="idx" class="skill-item">
            <div class="skill-name">{{ skill.skill_name }}</div>
            <div v-if="skill.skill_desc" class="skill-desc">{{ skill.skill_desc }}</div>
          </div>
        </div>
      </div>

      <!-- 荣誉奖项 -->
      <div v-if="data.awardList && data.awardList.length" class="section section-award">
        <div class="section-title" :style="{ color: themeColor }">荣誉奖项</div>
        <div class="section-content">
          <div v-for="(award, idx) in data.awardList" :key="idx" class="award-item">
            <svg width="10" height="10" viewBox="0 0 24 24" fill="none" :stroke="themeColor" stroke-width="2">
              <circle cx="12" cy="8" r="7"/>
              <polyline points="8.21 13.89 7 23 12 20 17 23 15.79 13.88"/>
            </svg>
            <span>{{ award.award_info }}</span>
            <span v-if="award.award_time" class="award-date">{{ award.award_time }}</span>
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
          <div v-for="(work, idx) in data.workExpList" :key="idx" class="work-item">
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
          <div v-for="(proj, idx) in data.projectList" :key="idx" class="project-item">
            <div class="section-info">
              <span class="project-name">{{ proj.project_name }}</span>
              <span v-if="proj.project_time" class="info-time">{{ proj.project_time }}</span>
            </div>
            <div v-if="proj.project_role" class="sub-info">{{ proj.project_role }}</div>
            <div v-if="proj.project_desc" class="project-desc">{{ proj.project_desc }}</div>
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

const getInitial = (name?: string) => {
  if (!name) return '?'
  return name.charAt(0).toUpperCase()
}

const formatDateRange = (dateRange: [string | null, string | null]) => {
  if (!dateRange || !dateRange[0]) return ''
  const start = dateRange[0]
  const end = dateRange[1] || '至今'
  return `${start} — ${end}`
}

const openLink = (url: string) => {
  if (url && window) {
    window.open(url, '_blank')
  }
}
</script>

<style scoped>
.tpl-modern {
  width: 794px;
  height: 1123px;
  display: grid;
  grid-template-columns: 2fr 3fr;
  grid-template-rows: 1fr;
  box-shadow: 0px 2px 4px 1px rgba(0, 0, 0, 0.15);
}

@media (max-width: 794px) {
  .tpl-modern {
    width: 100%;
    grid-template-columns: 1fr;
  }
}

@media print {
  @page {
    size: A4;
  }

  .tpl-modern {
    width: 100%;
    display: grid;
    grid-template-columns: 2fr 3fr;
    box-shadow: none;
  }
}

.tpl-modern .basic-info {
  padding: 24px 18px 24px 24px;
}

.tpl-modern .main-info {
  height: 100%;
  padding: 33px 24px 32px 20px;
  background: #f2f2f2;
}

.tpl-modern section {
  &:not(:first-of-type) {
    margin-top: 24px;
  }

  .section-title {
    margin-bottom: 12px;
    font-size: 24px;
    line-height: 32px;
    letter-spacing: 0;
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

.tpl-modern .basic-info section .section-info {
  .info-name {
    max-width: 198px;
    padding-right: 4px;
  }
}

.tpl-modern .basic-info .avatar {
  margin: 12px auto;
  display: block;
  width: 84px;
  height: 84px;
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
  background: linear-gradient(135deg, #e8f0fe 0%, #d2e3fc 100%);
  font-size: 36px;
  font-weight: 700;
  color: #2f5785;
}

.tpl-modern .basic-info .avatar.circle {
  border-radius: 50%;
}

.tpl-modern .basic-info .avatar.square {
  border-radius: 8px;
}

.tpl-modern .basic-info .avatar img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.tpl-modern .basic-info .profile-name {
  margin: 8px auto 24px;
  font-size: 24px;
  text-align: center;
}

.tpl-modern .basic-info .profile .profile-list {
  margin-bottom: 24px;

  > div {
    display: flex;
    align-items: center;
  }
  > div:not(:last-of-type) {
    margin-bottom: 4px;
  }

  svg {
    margin-right: 8px;
  }
}

.tpl-modern .basic-info .section-skill {
  .section-info {
    margin-top: 12px;
  }

  .skill-rate {
    font-size: 14px;
    display: flex;

    .ant-rate-star {
      margin-right: 4px;
    }
  }

  .skill-detail-item {
    margin-top: 4px;
  }
}

.tpl-modern .basic-info section .sub-info {
  color: rgba(0, 0, 0, 0.45);
}

.tpl-modern .basic-info .section-award {
  .sub-info {
    font-size: 14px;
    margin-left: 8px;
  }
}

.tpl-modern .basic-info .education-item {
  &:not(:first-of-type) {
    margin-top: 8px;
  }
}

.tpl-modern .basic-info .section-work {
  > div {
    line-height: 24px;
  }

  .sub-info {
    font-size: 12px;
    margin-left: 8px;
  }
}

.tpl-modern .main-info section {
  margin-bottom: 16px;

  .section-header {
    display: flex;
    margin-bottom: 10px;

    h1 {
      position: relative;
      font-size: 18px;
      font-weight: bold;
      color: #fff;
      width: max-content;
      line-height: 26px;
      padding: 0 100px 0 10px;
      border-top-left-radius: 3px;
      border-bottom-left-radius: 3px;
      margin: 0;
    }
    h1:before {
      content: '';
      display: block;
      width: 18.4px;
      height: 18.4px;
      background: #f2f2f2;
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

.tpl-modern .main-info .section-project,
.tpl-modern .main-info .section-work-exp {
  .section-info .ant-tag {
    line-height: 20px;
    height: 20px;
    border: none;
  }

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
  }
}

.tpl-modern .section-aboutme > div {
  margin: 6px 0;
}

.tpl-modern .skill-item {
  margin-bottom: 8px;
  padding-bottom: 8px;
  border-bottom: 1px dashed rgba(0, 0, 0, 0.1);
}

.tpl-modern .skill-name {
  font-size: 13px;
  font-weight: 600;
  color: #333;
  margin-bottom: 2px;
}

.tpl-modern .skill-desc {
  font-size: 12px;
  color: #666;
}

.tpl-modern .award-item {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 12px;
  color: #333;
}

.tpl-modern .award-date {
  font-size: 11px;
  color: #999;
  margin-left: auto;
}

.tpl-modern .education-item {
  margin-bottom: 8px;
}

.tpl-modern .item-title {
  font-size: 13px;
  font-weight: 700;
  color: #1a1a1a;
  margin-bottom: 2px;
}

.tpl-modern .item-sub {
  font-size: 12px;
  color: #666;
  margin-bottom: 2px;
}

.tpl-modern .item-desc {
  font-size: 11px;
  color: #999;
  margin-bottom: 2px;
}

.tpl-modern .item-date {
  font-size: 11px;
  color: #999;
}

.tpl-modern .work-item,
.tpl-modern .project-item {
  margin-bottom: 18px;
}

.tpl-modern .company,
.tpl-modern .project-name {
  font-size: 15px;
  font-weight: 700;
  color: #1a1a1a;
}

.tpl-modern .work-description,
.tpl-modern .project-desc,
.tpl-modern .project-content {
  font-size: 12px;
  color: #555;
  white-space: pre-wrap;
  line-height: 1.6;
}
</style>
