<template>
  <div class="template5-resume resume-content">
    <!-- Topbar -->
    <div class="resume-topbar">
      <div class="topbar-identity">
        <div v-if="!data.avatar?.hidden" class="avatar">
          <div :class="data.avatar?.shape || 'circle'">
            <img v-if="data.avatar?.src" :src="data.avatar.src" alt="avatar" />
            <span v-else class="avatar-placeholder">{{ getInitial(data.profile?.name) }}</span>
          </div>
        </div>
        <div class="identity-copy">
          <h1 class="name">{{ data.profile?.name || '姓名' }}</h1>
          <div v-if="data.profile?.positionTitle" class="position-title">{{ data.profile.positionTitle }}</div>
        </div>
      </div>

      <div class="topbar-contacts">
        <div v-if="data.profile?.mobile" class="contact-item">
          <svg width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <path d="M22 16.92v3a2 2 0 0 1-2.18 2 19.79 19.79 0 0 1-8.63-3.07A19.5 19.5 0 0 1 4.69 12.2 19.79 19.79 0 0 1 1.61 3.61 2 2 0 0 1 3.58 1h3a2 2 0 0 1 2 1.72c.127.96.361 1.903.7 2.81a2 2 0 0 1-.45 2.11L7.91 8.55A16 16 0 0 0 15 15.09l.92-.92a2 2 0 0 1 2.11-.45c.907.339 1.85.573 2.81.7A2 2 0 0 1 22 16.92z"/>
          </svg>
          <span>{{ data.profile.mobile }}</span>
        </div>
        <div v-if="data.profile?.email" class="contact-item">
          <svg width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <path d="M4 4h16c1.1 0 2 .9 2 2v12c0 1.1-.9 2-2 2H4c-1.1 0-2-.9-2-2V6c0-1.1.9-2 2-2z"/>
            <polyline points="22,6 12,13 2,6"/>
          </svg>
          <span>{{ data.profile.email }}</span>
        </div>
        <div v-if="data.profile?.github" class="contact-item link">
          <svg width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <path d="M9 19c-5 1.5-5-2.5-7-3m14 6v-3.87a3.37 3.37 0 0 0-.94-2.61c3.14-.35 6.44-1.54 6.44-7A5.44 5.44 0 0 0 20 4.77 5.07 5.07 0 0 0 19.91 1S18.73.65 16 2.48a13.38 13.38 0 0 0-7 0C6.27.65 5.09 1 5.09 1A5.07 5.07 0 0 0 5 4.77a5.44 5.44 0 0 0-1.5 3.78c0 5.42 3.3 6.61 6.44 7A3.37 3.37 0 0 0 9 18.13V22"/>
          </svg>
          <span>GitHub</span>
        </div>
        <div v-if="data.profile?.zhihu" class="contact-item link">
          <svg width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <circle cx="12" cy="12" r="10"/>
            <path d="M8 12h8"/>
            <path d="M8 8h8"/>
            <path d="M8 16h8"/>
          </svg>
          <span>知乎</span>
        </div>
      </div>
    </div>

    <!-- Column Layout -->
    <div class="resume-column-layout">
      <!-- Sidebar -->
      <aside class="resume-sidebar">
        <div class="contact-section">
          <h3 class="section-label">个人信息</h3>
          <div class="contact-list">
            <div v-if="data.profile?.workExpYear" class="contact-item">
              <svg width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <circle cx="12" cy="12" r="10"/>
                <polyline points="12 6 12 12 16 14"/>
              </svg>
              <span>{{ data.profile.workExpYear }}</span>
            </div>
            <div v-if="data.profile?.workPlace" class="contact-item">
              <svg width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <path d="M21 10c0 7-9 13-9 13s-9-6-9-13a9 9 0 0 1 18 0z"/>
                <circle cx="12" cy="10" r="3"/>
              </svg>
              <span>{{ data.profile.workPlace }}</span>
            </div>
          </div>
        </div>

        <!-- 技能 & 奖项 -->
        <div v-if="(data.skillList && data.skillList.length) || (data.awardList && data.awardList.length)" class="skills-section">
          <h3 class="section-label">专业技能</h3>
          <div class="skill-list">
            <div v-for="(skill, idx) in data.skillList" :key="idx" class="skill-item">
              <div class="skill-name">{{ skill.skill_name }}</div>
              <div v-if="skill.skill_desc" class="skill-desc">{{ skill.skill_desc }}</div>
            </div>
            <div v-for="(award, idx) in data.awardList" :key="`award-${idx}`" class="award-item">
              <div class="award-info">{{ award.award_info }}</div>
              <div v-if="award.award_time" class="award-time">{{ award.award_time }}</div>
            </div>
          </div>
        </div>

        <!-- 个人作品 -->
        <div v-if="data.workList && data.workList.length" class="portfolio-section">
          <h3 class="section-label">个人作品</h3>
          <div class="portfolio-list">
            <div v-for="(work, idx) in data.workList" :key="idx" class="work-item">
              <div class="info-name">{{ work.work_name }}</div>
              <div v-if="work.work_desc" class="sub-info">{{ work.work_desc }}</div>
            </div>
          </div>
        </div>
      </aside>

      <!-- Main -->
      <main class="resume-main">
        <!-- 自我介绍 -->
        <div v-if="hasAboutme" class="section">
          <div class="section-title" :style="{ borderBottomColor: themeColor }">自我介绍</div>
          <div class="section-body about-content">
            <p v-for="(item, idx) in aboutmeList" :key="idx">{{ item }}</p>
          </div>
        </div>

        <!-- 工作经历 -->
        <div v-if="data.workExpList && data.workExpList.length" class="section">
          <div class="section-title" :style="{ borderBottomColor: themeColor }">工作经历</div>
          <div class="section-body">
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
          <div class="section-title" :style="{ borderBottomColor: themeColor }">项目经历</div>
          <div class="section-body">
            <div v-for="(proj, idx) in data.projectList" :key="idx" class="item">
              <div class="item-header">
                <span class="project-name">{{ proj.project_name }}</span>
                <span v-if="proj.project_time" class="time">{{ proj.project_time }}</span>
                <span v-if="proj.project_role" class="role-tag" :style="{ backgroundColor: themeColor }">{{ proj.project_role }}</span>
              </div>
              <div v-if="proj.project_desc" class="item-desc">
                <span class="label">项目描述：</span>
                <span>{{ proj.project_desc }}</span>
              </div>
              <div v-if="proj.project_content" class="item-content">
                <span class="label">主要工作：</span>
                <span>{{ proj.project_content }}</span>
              </div>
            </div>
          </div>
        </div>

        <!-- 教育背景 -->
        <div v-if="data.educationList && data.educationList.length" class="section">
          <div class="section-title" :style="{ borderBottomColor: themeColor }">教育背景</div>
          <div class="section-body">
            <div v-for="(edu, idx) in data.educationList" :key="idx" class="edu-item">
              <span class="school">{{ edu.school }}</span>
              <span class="major">{{ edu.major }} {{ edu.academic_degree ? `(${edu.academic_degree})` : '' }}</span>
              <span v-if="edu.edu_time" class="time">{{ formatDateRange(edu.edu_time) }}</span>
            </div>
          </div>
        </div>
      </main>
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
.template5-resume {
  width: 794px;
  min-height: 942px;
  margin-bottom: 60px;
  background: #fff;
  color: #192131;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
  box-shadow: 0 20px 40px rgba(20, 31, 49, 0.08);
}

@media (max-width: 794px) {
  .template5-resume {
    width: 100%;
    min-height: auto;
  }
}

@media print {
  @page {
    size: A4;
  }

  .template5-resume {
    width: 100%;
    min-height: auto;
    margin-bottom: 0;
    box-shadow: none;
  }

  .template5-resume .resume-topbar {
    flex-direction: row;
    padding: 24px 30px 16px;
  }

  .template5-resume .topbar-contacts {
    min-width: 184px;
  }

  .template5-resume .resume-column-layout {
    grid-template-columns: 214px minmax(0, 1fr);
    padding: 18px 30px 24px;
  }

  .template5-resume .section,
  .template5-resume .item,
  .template5-resume .contact-section,
  .template5-resume .skills-section,
  .template5-resume .portfolio-section {
    break-inside: auto;
    page-break-inside: auto;
  }
}

.template5-resume .resume-topbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 20px;
  padding: 24px 30px 16px;
  border-bottom: 1px solid #d7dfeb;
  background: linear-gradient(180deg, rgba(31, 60, 136, 0.04) 0%, rgba(255, 255, 255, 0) 100%);
}

@media (max-width: 794px) {
  .template5-resume .resume-topbar {
    flex-direction: column;
    padding: 28px 24px 22px;
  }
}

.template5-resume .topbar-identity {
  display: flex;
  align-items: center;
  gap: 18px;
  min-width: 0;
  flex: 1;
}

.template5-resume .topbar-identity > .avatar {
  width: 78px;
  min-width: 78px;
  height: 78px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.template5-resume .avatar > div {
  width: 78px;
  height: 78px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 34px;
  font-weight: 700;
  color: #1f3c88;
  background: #f0f4fc;
}

.template5-resume .avatar > div.circle {
  border-radius: 50%;
}

.template5-resume .avatar > div.square {
  border-radius: 8px;
}

.template5-resume .avatar img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.template5-resume .identity-copy {
  min-width: 0;
}

.template5-resume .name {
  margin: 0;
  font-size: 27px;
  font-weight: 700;
  letter-spacing: 0.05em;
  color: #172033;
}

.template5-resume .position-title {
  margin-top: 4px;
  font-size: 14px;
  font-weight: 600;
  color: #274277;
}

.template5-resume .topbar-contacts {
  min-width: 184px;
  display: grid;
  gap: 4px;
}

.template5-resume .topbar-contacts .contact-item {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 11px;
  color: #4d5d74;
  line-height: 1.5;
}

.template5-resume .topbar-contacts .contact-item svg {
  color: #1f3c88;
  font-size: 12px;
}

.template5-resume .topbar-contacts .contact-item.link {
  cursor: pointer;
}

.template5-resume .topbar-contacts .contact-item.link:hover {
  color: #1f3c88;
}

.template5-resume .resume-column-layout {
  display: grid;
  grid-template-columns: 214px minmax(0, 1fr);
  gap: 18px;
  align-items: start;
  padding: 18px 30px 24px;
}

@media (max-width: 794px) {
  .template5-resume .resume-column-layout {
    grid-template-columns: 1fr;
    padding: 24px;
  }
}

.template5-resume .resume-sidebar {
  display: block;
}

.template5-resume .resume-main {
  display: block;
}

.template5-resume .resume-sidebar > * + * {
  margin-top: 10px;
}

.template5-resume .resume-main > * + * {
  margin-top: 14px;
}

.template5-resume .contact-section,
.template5-resume .skills-section,
.template5-resume .portfolio-section {
  padding: 10px 10px 8px;
  border: 1px solid #e3e8f1;
  border-radius: 10px;
  background: #fafbfd;
}

.template5-resume .section-label {
  margin: 0 0 8px;
  padding-bottom: 5px;
  border-bottom: 1px solid #e3e8f1;
  font-size: 11px;
  font-weight: 700;
  color: #536279;
  letter-spacing: 0.06em;
  text-transform: uppercase;
}

.template5-resume .contact-list {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.template5-resume .contact-list .contact-item {
  display: flex;
  align-items: center;
  font-size: 11px;
  color: #4f5d73;
  line-height: 1.5;
}

.template5-resume .contact-list .contact-item svg {
  margin-right: 8px;
  color: #1f3c88;
  font-size: 12px;
}

.template5-resume .skills-section .skill-list {
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.template5-resume .skills-section .skill-item {
  margin-bottom: 6px;
}

.template5-resume .skills-section .skill-item:last-child {
  margin-bottom: 0;
}

.template5-resume .skills-section .skill-name {
  display: block;
  margin-bottom: 1px;
  font-size: 11px;
  font-weight: 700;
  color: #213149;
}

.template5-resume .skills-section .skill-desc {
  font-size: 10px;
  color: #617086;
  line-height: 1.42;
}

.template5-resume .skills-section .award-item {
  margin-bottom: 4px;
  font-size: 10px;
  color: #55647b;
}

.template5-resume .skills-section .award-item .award-info {
  font-weight: 600;
  color: #243044;
}

.template5-resume .skills-section .award-item .award-time {
  margin-left: 6px;
  font-size: 11px;
  color: #7a8798;
}

.template5-resume .portfolio-list {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.template5-resume .portfolio-list > div {
  padding-bottom: 8px;
  border-bottom: 1px dashed #d8e0ed;
}

.template5-resume .portfolio-list > div:last-child {
  padding-bottom: 0;
  border-bottom: none;
}

.template5-resume .portfolio-list .info-name {
  color: #213149;
  font-size: 11px;
  font-weight: 600;
}

.template5-resume .portfolio-list .sub-info {
  display: inline-block;
  margin-left: 8px;
  color: #637189;
  font-size: 10px;
}

.template5-resume .section {
  margin-bottom: 0;
  break-inside: avoid;
  page-break-inside: avoid;
}

.template5-resume .section-title {
  margin: 0 0 8px;
  padding-bottom: 6px;
  border-bottom: 1px solid;
  font-size: 13px;
  font-weight: 700;
  letter-spacing: 0.08em;
  text-transform: uppercase;
  color: #172033;
}

.template5-resume .section-body {
  padding-left: 0;
}

.template5-resume .item {
  margin-bottom: 12px;
  padding: 0 0 12px 12px;
  border-left: 2px solid #d6deeb;
  break-inside: avoid;
  page-break-inside: avoid;
}

.template5-resume .item:last-child {
  margin-bottom: 0;
  padding-bottom: 0;
}

.template5-resume .item-header {
  display: flex;
  align-items: flex-start;
  flex-wrap: wrap;
  gap: 4px 8px;
  margin-bottom: 6px;
}

.template5-resume .company,
.template5-resume .project-name {
  font-size: 14px;
  font-weight: 700;
  color: #172033;
}

.template5-resume .department {
  font-size: 11px;
  color: #59677c;
}

.template5-resume .time {
  margin-left: auto;
  padding: 2px 7px;
  border-radius: 4px;
  border: 1px solid #dde5f0;
  background: #f9fbfe;
  font-size: 10px;
  color: #6e7c90;
}

.template5-resume .role-tag {
  margin-left: 0;
  border: none;
  border-radius: 999px;
  padding-inline: 8px;
  font-size: 10px;
  color: #fff;
}

.template5-resume .item-desc,
.template5-resume .item-content {
  color: #49576b;
  font-size: 11px;
  line-height: 1.52;
  white-space: pre-wrap;
}

.template5-resume .item-desc {
  margin-bottom: 4px;
}

.template5-resume .label {
  display: inline;
  margin-right: 6px;
  color: #748093;
  font-size: 10px;
  font-weight: 600;
  letter-spacing: 0.03em;
  text-transform: uppercase;
}

.template5-resume .edu-item {
  display: flex;
  align-items: center;
  flex-wrap: wrap;
  gap: 6px;
  margin-bottom: 10px;
  padding-left: 18px;
  border-left: 2px solid #dce3ef;
}

.template5-resume .edu-item:last-child {
  margin-bottom: 0;
}

.template5-resume .edu-item .school {
  font-size: 13px;
  font-weight: 700;
  color: #1c2638;
}

.template5-resume .edu-item .major {
  font-size: 11px;
  color: #5b677a;
}

.template5-resume .edu-item .time {
  margin-left: auto;
  font-size: 10px;
  color: #6e7c90;
}

.template5-resume .about-content {
  padding-left: 0;
}

.template5-resume .about-content p {
  margin: 0 0 6px;
  color: #455164;
  font-size: 11px;
  line-height: 1.54;
  white-space: pre-wrap;
}

.template5-resume .about-content p:last-child {
  margin-bottom: 0;
}
</style>
