<template>
  <div class="template4-resume resume-content">
    <div class="resume-header" :style="{ borderBottomColor: themeColor }">
      <div class="resume-header-top">
        <div class="resume-header-main">
          <div v-if="!data.avatar?.hidden" class="avatar">
            <img v-if="data.avatar?.src" :src="data.avatar.src" alt="avatar" />
            <span v-else class="avatar-placeholder">{{ getInitial(data.profile?.name) }}</span>
          </div>
          <div class="identity-block">
            <h1 class="name" :style="{ color: themeColor }">{{ data.profile?.name || '姓名' }}</h1>
            <div v-if="data.profile?.positionTitle" class="position-title">{{ data.profile.positionTitle }}</div>
          </div>
        </div>

        <div class="contact-panel">
          <span v-if="data.profile?.mobile" class="contact-item">
            <svg width="12" height="12" viewBox="0 0 24 24" fill="none" :stroke="themeColor" stroke-width="2">
              <path d="M22 16.92v3a2 2 0 0 1-2.18 2 19.79 19.79 0 0 1-8.63-3.07A19.5 19.5 0 0 1 4.69 12.2 19.79 19.79 0 0 1 1.61 3.61 2 2 0 0 1 3.58 1h3a2 2 0 0 1 2 1.72c.127.96.361 1.903.7 2.81a2 2 0 0 1-.45 2.11L7.91 8.55A16 16 0 0 0 15 15.09l.92-.92a2 2 0 0 1 2.11-.45c.907.339 1.85.573 2.81.7A2 2 0 0 1 22 16.92z"/>
            </svg>
            {{ data.profile.mobile }}
          </span>
          <span v-if="data.profile?.email" class="contact-item">
            <svg width="12" height="12" viewBox="0 0 24 24" fill="none" :stroke="themeColor" stroke-width="2">
              <path d="M4 4h16c1.1 0 2 .9 2 2v12c0 1.1-.9 2-2 2H4c-1.1 0-2-.9-2-2V6c0-1.1.9-2 2-2z"/>
              <polyline points="22,6 12,13 2,6"/>
            </svg>
            {{ data.profile.email }}
          </span>
          <span v-if="data.profile?.github" class="contact-item link">
            <svg width="12" height="12" viewBox="0 0 24 24" fill="none" :stroke="themeColor" stroke-width="2">
              <path d="M9 19c-5 1.5-5-2.5-7-3m14 6v-3.87a3.37 3.37 0 0 0-.94-2.61c3.14-.35 6.44-1.54 6.44-7A5.44 5.44 0 0 0 20 4.77 5.07 5.07 0 0 0 19.91 1S18.73.65 16 2.48a13.38 13.38 0 0 0-7 0C6.27.65 5.09 1 5.09 1A5.07 5.07 0 0 0 5 4.77a5.44 5.44 0 0 0-1.5 3.78c0 5.42 3.3 6.61 6.44 7A3.37 3.37 0 0 0 9 18.13V22"/>
            </svg>
            GitHub
          </span>
          <span v-if="data.profile?.zhihu" class="contact-item link">
            <svg width="12" height="12" viewBox="0 0 24 24" fill="none" :stroke="themeColor" stroke-width="2">
              <circle cx="12" cy="12" r="10"/>
              <path d="M12 2a15.3 15.3 0 0 1 4 10 15.3 15.3 0 0 1-4 10 15.3 15.3 0 0 1-4-10 15.3 15.3 0 0 1 4-10z"/>
              <path d="M12 2a9 9 0 0 1 0 18 9 9 0 0 1 0-18z"/>
            </svg>
            知乎
          </span>
        </div>
      </div>
    </div>

    <div class="resume-flow">
      <!-- 自我介绍 -->
      <div v-if="hasAboutme" class="section">
        <div class="section-title">自我介绍</div>
        <div class="section-content about-content">
          <p v-for="(item, idx) in aboutmeList" :key="idx">{{ item }}</p>
        </div>
      </div>

      <!-- 工作经历 -->
      <div v-if="data.workExpList && data.workExpList.length" class="section">
        <div class="section-title">工作经历</div>
        <div class="section-content">
          <div v-for="(work, idx) in data.workExpList" :key="idx" class="item">
            <div class="item-header">
              <span class="company">{{ work.company_name }}</span>
              <span v-if="work.department_name" class="department">{{ work.department_name }}</span>
              <span v-if="work.work_time" class="time">{{ formatDateRange(work.work_time) }}</span>
            </div>
            <div v-if="work.work_desc" class="item-desc">{{ work.work_desc }}</div>
          </div>
        </div>
      </div>

      <!-- 项目经历 -->
      <div v-if="data.projectList && data.projectList.length" class="section">
        <div class="section-title">项目经历</div>
        <div class="section-content">
          <div v-for="(proj, idx) in data.projectList" :key="idx" class="item">
            <div class="item-header">
              <span class="project-name">{{ proj.project_name }}</span>
              <span v-if="proj.project_role" class="role-tag" :style="{ background: themeColor, color: '#fff' }">{{ proj.project_role }}</span>
              <span v-if="proj.project_time" class="time">{{ proj.project_time }}</span>
            </div>
            <div v-if="proj.project_desc" class="item-desc">{{ proj.project_desc }}</div>
            <div v-if="proj.project_content" class="item-content">{{ proj.project_content }}</div>
          </div>
        </div>
      </div>

      <!-- 教育背景 -->
      <div v-if="data.educationList && data.educationList.length" class="section">
        <div class="section-title">教育背景</div>
        <div class="section-content">
          <div v-for="(edu, idx) in data.educationList" :key="idx" class="edu-item">
            <span class="school">{{ edu.school }}</span>
            <span class="major">{{ edu.major }} {{ edu.academic_degree ? `· ${edu.academic_degree}` : '' }}</span>
            <span v-if="edu.edu_time" class="time">{{ formatDateRange(edu.edu_time) }}</span>
          </div>
        </div>
      </div>

      <!-- 专业技能 & 荣誉奖项 & 个人作品 -->
      <div v-if="(data.skillList && data.skillList.length) || (data.awardList && data.awardList.length) || (data.workList && data.workList.length)" class="resume-utility-grid">
        <div v-if="data.skillList && data.skillList.length || data.awardList && data.awardList.length" class="utility-section">
          <div class="section-title">专业技能</div>
          <div class="section-content">
            <div v-if="data.skillList && data.skillList.length">
              <div v-for="(skill, idx) in data.skillList" :key="idx" class="skill-item">
                <span class="skill-name">{{ skill.skill_name }}</span>
                <span v-if="skill.skill_desc" class="skill-desc">{{ skill.skill_desc }}</span>
              </div>
            </div>
            <div v-if="data.awardList && data.awardList.length">
              <div v-for="(award, idx) in data.awardList" :key="idx" class="award-item">
                <span class="award-info">{{ award.award_info }}</span>
                <span v-if="award.award_time" class="award-time">{{ award.award_time }}</span>
              </div>
            </div>
          </div>
        </div>

        <div v-if="data.workList && data.workList.length" class="utility-section">
          <div class="section-title">个人作品</div>
          <div class="section-content">
            <div class="portfolio-list">
              <div v-for="(work, idx) in data.workList" :key="idx" class="work-item">
                <span class="info-name">{{ work.work_name }}</span>
                <span v-if="work.work_desc" class="sub-info">{{ work.work_desc }}</span>
              </div>
            </div>
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
</script>

<style scoped>
.template4-resume {
  width: 794px;
  min-height: 942px;
  margin-bottom: 60px;
  background: #fff;
  color: #18212f;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
  box-shadow: 0 22px 48px rgba(22, 33, 47, 0.08);
}

@media (max-width: 794px) {
  .template4-resume {
    width: 100%;
  }
}

@media print {
  @page {
    size: A4;
  }

  .template4-resume {
    width: 100%;
    min-height: auto;
    margin-bottom: 0;
    box-shadow: none;
  }

  .template4-resume .resume-header .resume-header-top {
    flex-direction: row;
  }

  .template4-resume .resume-header .contact-panel {
    min-width: 220px;
    width: auto;
  }

  .template4-resume .resume-flow {
    padding: 18px 34px 24px;
  }

  .template4-resume .resume-utility-grid {
    grid-template-columns: repeat(2, minmax(0, 1fr));
  }

  .template4-resume .section,
  .template4-resume .item {
    break-inside: auto;
    page-break-inside: auto;
  }
}

.template4-resume .resume-header {
  padding: 26px 34px 16px;
  border-bottom: 1px solid;
  background: linear-gradient(180deg, rgba(47, 87, 133, 0.04) 0%, rgba(255, 255, 255, 0) 100%);

  .resume-header-top {
    display: flex;
    justify-content: space-between;
    align-items: flex-start;
    gap: 18px;

    @media (max-width: 794px) {
      flex-direction: column;
    }
  }

  .resume-header-main {
    display: flex;
    align-items: center;
    gap: 18px;
    min-width: 0;
    flex: 1;
  }

  .avatar {
    flex-shrink: 0;
    width: 84px;
    min-width: 84px;
    height: 84px;
    display: flex;
    align-items: center;
    justify-content: center;
    border-radius: 50%;
    background: #f2f2f2;
    overflow: hidden;
    border: 2px solid #e8e8e8;

    img {
      width: 100%;
      height: 100%;
      object-fit: cover;
    }
  }

  .avatar-placeholder {
    font-size: 38px;
    color: #999;
  }

  .identity-block {
    min-width: 0;
    flex: 1;
  }

  .name {
    margin: 0;
    font-size: 28px;
    font-weight: 700;
    line-height: 1.08;
    letter-spacing: 0.02em;
  }

  .position-title {
    margin-top: 4px;
    font-size: 15px;
    font-weight: 600;
    color: #2c3850;
  }

  .contact-panel {
    min-width: 220px;
    display: flex;
    flex-direction: column;
    gap: 6px;

    @media (max-width: 794px) {
      min-width: 0;
      width: 100%;
    }
  }

  .contact-item {
    display: flex;
    align-items: center;
    padding: 4px 8px;
    border-radius: 8px;
    background: #fafbfd;
    border: 1px solid #edf1f7;
    font-size: 11px;
    color: #546173;

    svg {
      margin-right: 5px;
      font-size: 12px;
    }

    &.link {
      cursor: pointer;

      &:hover {
        color: #2f3a4f;
      }
    }
  }
}

.template4-resume .resume-content-grid {
  display: none;
}

.template4-resume .resume-flow {
  display: block;
  padding: 18px 34px 24px;

  @media (max-width: 794px) {
    padding: 24px;
  }
}

.template4-resume .resume-flow > * + * {
  margin-top: 14px;
}

.template4-resume .section {
  margin-bottom: 0;
  break-inside: avoid;
  page-break-inside: avoid;
}

.template4-resume .section-title {
  margin: 0 0 8px;
  padding-bottom: 6px;
  border-bottom: 1px solid #e8edf5;
  font-size: 13px;
  font-weight: 700;
  letter-spacing: 0.06em;
  text-transform: uppercase;
}

.template4-resume .section-content {
  font-size: 12px;
  line-height: 1.56;
  color: #394457;
}

.template4-resume .item {
  margin-bottom: 12px;
  padding: 0 0 12px 12px;
  border-left: 2px solid #e5eaf2;
  break-inside: avoid;
  page-break-inside: avoid;

  &:last-child {
    margin-bottom: 0;
    padding-bottom: 0;
  }
}

.template4-resume .item-header {
  display: flex;
  align-items: flex-start;
  flex-wrap: wrap;
  gap: 4px 8px;
  margin-bottom: 6px;

  .company,
  .project-name {
    font-size: 14px;
    font-weight: 700;
    color: #1b2432;
  }

  .department {
    font-size: 12px;
    color: #5f6d82;
  }

  .time {
    margin-left: auto;
    padding: 2px 7px;
    border-radius: 999px;
    background: #f5f7fb;
    border: 1px solid #e7edf6;
    font-size: 10px;
    color: #6f7d90;
  }

  .role-tag {
    margin-left: 0;
    border: none;
    border-radius: 999px;
    padding-inline: 8px;
    font-size: 11px;
  }
}

.template4-resume .item-desc,
.template4-resume .item-content {
  color: #4b5768;
  white-space: pre-wrap;
  font-size: 12px;
  line-height: 1.56;
}

.template4-resume .item-desc {
  margin-bottom: 4px;
}

.template4-resume .edu-item {
  display: flex;
  align-items: center;
  flex-wrap: wrap;
  gap: 8px;

  .school {
    font-size: 14px;
    font-weight: 700;
  }

  .major {
    font-size: 12px;
    color: #5b6678;
  }

  .time {
    margin-left: auto;
    font-size: 10px;
    color: #6f7d90;
  }
}

.template4-resume .skill-item {
  margin-bottom: 8px;
  padding-bottom: 8px;
  border-bottom: 1px dashed #e2e8f0;

  &:last-child {
    margin-bottom: 0;
    padding-bottom: 0;
    border-bottom: none;
  }

  .skill-name {
    display: block;
    margin-bottom: 1px;
    font-weight: 700;
    color: #243044;
  }

  .skill-desc {
    color: #536073;
    font-size: 12px;
    line-height: 1.5;
  }
}

.template4-resume .award-item {
  margin-bottom: 6px;

  &:last-child {
    margin-bottom: 0;
  }

  .award-info {
    font-weight: 600;
  }

  .award-time {
    margin-left: 8px;
    font-size: 10px;
    color: #6f7d90;
  }
}

.template4-resume .portfolio-list {
  display: flex;
  flex-direction: column;
  gap: 8px;

  > div {
    padding-bottom: 8px;
    border-bottom: 1px dashed #e1e7f0;

    &:last-child {
      padding-bottom: 0;
      border-bottom: none;
    }
  }

  .info-name {
    color: #243044;
  }

  .sub-info {
    display: inline-block;
    margin-left: 8px;
    color: #637189;
  }
}

.template4-resume .resume-utility-grid {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 14px;
  align-items: start;

  @media (max-width: 794px) {
    grid-template-columns: 1fr;
  }
}

.template4-resume .utility-section {
  padding: 10px 10px 8px;
  border: 1px solid #e9eef6;
  border-radius: 12px;
  background: linear-gradient(180deg, #fbfcfe 0%, #f7f9fc 100%);

  .section-title {
    margin-bottom: 8px;
    font-size: 12px;
    letter-spacing: 0.08em;
  }

  .section-content {
    font-size: 12px;
    line-height: 1.5;
  }
}

.template4-resume .about-content {
  p {
    margin: 0 0 6px;
    color: #455163;
    font-size: 12px;
    line-height: 1.58;
    white-space: pre-wrap;

    &:last-child {
      margin-bottom: 0;
    }
  }
}
</style>
