<template>
  <div class="tpl-classic-new resume-content">
    <!-- ========== 左侧边栏 ========== -->
    <div class="sidebar">
      <!-- 头像 -->
      <div v-if="!data.avatar?.hidden" class="avatar-section">
        <div class="avatar" :class="data.avatar?.shape || 'circle'">
          <img v-if="data.avatar?.src" :src="data.avatar.src" alt="avatar" />
          <span v-else class="avatar-placeholder">{{ getInitial(data.profile?.name) }}</span>
        </div>
      </div>

      <!-- 个人信息 -->
      <div class="profile-section">
        <h3 class="section-label" :style="{ color: themeColor }">个人信息</h3>
        <div class="profile-list">
          <div v-if="data.profile?.name" class="profile-item">
            <svg width="12" height="12" viewBox="0 0 24 24" fill="none" :stroke="themeColor" stroke-width="2">
              <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"/>
              <circle cx="12" cy="7" r="4"/>
            </svg>
            <span>{{ data.profile.name }}</span>
          </div>
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
          <div v-if="data.profile?.workExpYear" class="profile-item">
            <svg width="12" height="12" viewBox="0 0 24 24" fill="none" :stroke="themeColor" stroke-width="2">
              <circle cx="12" cy="12" r="10"/>
              <polyline points="12 6 12 12 16 14"/>
            </svg>
            <span>{{ data.profile.workExpYear }}</span>
          </div>
          <div v-if="data.profile?.workPlace" class="profile-item">
            <svg width="12" height="12" viewBox="0 0 24 24" fill="none" :stroke="themeColor" stroke-width="2">
              <path d="M21 10c0 7-9 13-9 13s-9-6-9-13a9 9 0 0 1 18 0z"/>
              <circle cx="12" cy="10" r="3"/>
            </svg>
            <span>{{ data.profile.workPlace }}</span>
          </div>
        </div>
      </div>

      <!-- 教育背景 -->
      <div v-if="data.educationList && data.educationList.length" class="sidebar-section">
        <h3 class="section-label" :style="{ color: themeColor }">教育背景</h3>
        <div class="timeline">
          <div v-for="(edu, idx) in data.educationList" :key="idx" class="timeline-item">
            <div class="timeline-dot" :style="{ borderColor: themeColor }"></div>
            <div class="timeline-content">
              <div class="item-title">{{ edu.school }}</div>
              <div class="item-sub">{{ edu.major }}</div>
              <div v-if="edu.academic_degree" class="item-desc">{{ edu.academic_degree }}</div>
              <div v-if="edu.edu_time" class="item-date">{{ formatDateRange(edu.edu_time) }}</div>
            </div>
          </div>
        </div>
      </div>

      <!-- 专业技能 -->
      <div v-if="data.skillList && data.skillList.length" class="sidebar-section">
        <h3 class="section-label" :style="{ color: themeColor }">专业技能</h3>
        <div class="skill-list">
          <div v-for="(skill, idx) in data.skillList" :key="idx" class="skill-item">
            <div class="skill-name">{{ skill.skill_name }}</div>
            <div v-if="skill.skill_desc" class="skill-desc">{{ skill.skill_desc }}</div>
          </div>
        </div>
      </div>

      <!-- 荣誉奖项 -->
      <div v-if="data.awardList && data.awardList.length" class="sidebar-section">
        <h3 class="section-label" :style="{ color: themeColor }">荣誉奖项</h3>
        <div class="award-list">
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

      <!-- 个人作品 -->
      <div v-if="data.workList && data.workList.length" class="sidebar-section">
        <h3 class="section-label" :style="{ color: themeColor }">个人作品</h3>
        <div class="work-list">
          <div v-for="(work, idx) in data.workList" :key="idx" class="work-item">
            <div class="work-name">{{ work.work_name }}</div>
            <div v-if="work.work_desc" class="work-desc">{{ work.work_desc }}</div>
          </div>
        </div>
      </div>
    </div>

    <!-- ========== 右侧主内容 ========== -->
    <div class="main">
      <!-- 自我介绍 -->
      <div v-if="hasAboutme" class="main-section">
        <div class="section-header">
          <svg width="16" height="16" viewBox="0 0 24 24" fill="none" :stroke="themeColor" stroke-width="2">
            <circle cx="12" cy="12" r="10"/>
            <line x1="12" y1="8" x2="12" y2="12"/>
            <line x1="12" y1="16" x2="12.01" y2="16"/>
          </svg>
          <h2 :style="{ background: themeColor }">自我介绍</h2>
        </div>
        <div class="section-body aboutme-content">
          <p v-for="(item, idx) in aboutmeList" :key="idx">{{ item }}</p>
        </div>
      </div>

      <!-- 工作经历 -->
      <div v-if="data.workExpList && data.workExpList.length" class="main-section">
        <div class="section-header">
          <svg width="16" height="16" viewBox="0 0 24 24" fill="none" :stroke="themeColor" stroke-width="2">
            <rect x="2" y="7" width="20" height="14" rx="2" ry="2"/>
            <path d="M16 21V5a2 2 0 0 0-2-2h-4a2 2 0 0 0-2 2v16"/>
          </svg>
          <h2 :style="{ background: themeColor }">工作经历</h2>
        </div>
        <div class="section-body">
          <div v-for="(work, idx) in data.workExpList" :key="idx" class="work-item">
            <div class="work-header">
              <span class="company">{{ work.company_name }}</span>
              <span v-if="work.work_time" class="work-time">{{ formatDateRange(work.work_time) }}</span>
            </div>
            <div v-if="work.department_name" class="department">{{ work.department_name }}</div>
            <div v-if="work.work_desc" class="work-desc">{{ work.work_desc }}</div>
          </div>
        </div>
      </div>

      <!-- 项目经历 -->
      <div v-if="data.projectList && data.projectList.length" class="main-section">
        <div class="section-header">
          <svg width="16" height="16" viewBox="0 0 24 24" fill="none" :stroke="themeColor" stroke-width="2">
            <polygon points="13 2 3 14 12 14 11 22 21 10 12 10 13 2"/>
          </svg>
          <h2 :style="{ background: themeColor }">项目经历</h2>
        </div>
        <div class="section-body">
          <div v-for="(proj, idx) in data.projectList" :key="idx" class="project-item">
            <div class="project-header">
              <span class="project-name">{{ proj.project_name }}</span>
              <span v-if="proj.project_time" class="project-time">{{ proj.project_time }}</span>
            </div>
            <div v-if="proj.project_role" class="project-role">
              <span class="role-badge" :style="{ backgroundColor: themeColor }">{{ proj.project_role }}</span>
            </div>
            <div v-if="proj.project_desc" class="project-desc">
              <span class="label">项目描述：</span>
              <span>{{ proj.project_desc }}</span>
            </div>
            <div v-if="proj.project_content" class="project-content">
              <span class="label">主要工作：</span>
              <span>{{ proj.project_content }}</span>
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
.tpl-classic-new {
  width: 794px;
  min-height: 942px;
  display: grid;
  grid-template-columns: 2fr 3fr;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.12);
}

@media (max-width: 794px) {
  .tpl-classic-new {
    width: 100%;
    grid-template-columns: 1fr;
  }
}

@media print {
  @page {
    size: A4;
  }
  .tpl-classic-new {
    width: 100%;
    box-shadow: none;
  }
}

/* Sidebar */
.tpl-classic-new .sidebar {
  padding: 24px 18px;
  background: rgba(47, 87, 133, 0.08);
}

.tpl-classic-new .avatar-section {
  margin-bottom: 20px;
  text-align: center;
}

.tpl-classic-new .avatar {
  width: 84px;
  height: 84px;
  margin: 0 auto;
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
  background: linear-gradient(135deg, #e8f0fe 0%, #d2e3fc 100%);
  font-size: 36px;
  font-weight: 700;
  color: #2f5785;
}

.tpl-classic-new .avatar.circle {
  border-radius: 50%;
}

.tpl-classic-new .avatar.square {
  border-radius: 8px;
}

.tpl-classic-new .avatar img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.tpl-classic-new .profile-section {
  margin-bottom: 20px;
}

.tpl-classic-new .section-label {
  font-size: 16px;
  font-weight: 700;
  margin-bottom: 12px;
  padding-bottom: 6px;
  border-bottom: 2px solid currentColor;
}

.tpl-classic-new .profile-list {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.tpl-classic-new .profile-item {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 13px;
  color: #333;
}

.tpl-classic-new .sidebar-section {
  margin-bottom: 20px;
}

/* Timeline */
.tpl-classic-new .timeline {
  position: relative;
  padding-left: 16px;
}

.tpl-classic-new .timeline::before {
  content: '';
  position: absolute;
  left: 0;
  top: 6px;
  bottom: 6px;
  width: 2px;
  background: linear-gradient(to bottom, currentColor, currentColor);
  opacity: 0.3;
}

.tpl-classic-new .timeline-item {
  position: relative;
  padding-left: 12px;
  margin-bottom: 12px;
}

.tpl-classic-new .timeline-dot {
  position: absolute;
  left: -20px;
  top: 4px;
  width: 8px;
  height: 8px;
  background: #fff;
  border: 2px solid;
  border-radius: 50%;
  z-index: 1;
}

.tpl-classic-new .item-title {
  font-size: 13px;
  font-weight: 700;
  color: #1a1a1a;
  margin-bottom: 2px;
}

.tpl-classic-new .item-sub {
  font-size: 12px;
  color: #666;
  margin-bottom: 2px;
}

.tpl-classic-new .item-desc {
  font-size: 11px;
  color: #999;
  margin-bottom: 2px;
}

.tpl-classic-new .item-date {
  font-size: 11px;
  color: #999;
}

/* Skills */
.tpl-classic-new .skill-list {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.tpl-classic-new .skill-item {
  padding-bottom: 8px;
  border-bottom: 1px dashed rgba(0, 0, 0, 0.1);
}

.tpl-classic-new .skill-name {
  font-size: 13px;
  font-weight: 600;
  color: #333;
  margin-bottom: 2px;
}

.tpl-classic-new .skill-desc {
  font-size: 12px;
  color: #666;
}

/* Awards */
.tpl-classic-new .award-list {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.tpl-classic-new .award-item {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 12px;
  color: #333;
}

.tpl-classic-new .award-date {
  font-size: 11px;
  color: #999;
  margin-left: auto;
}

/* Works */
.tpl-classic-new .work-list {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.tpl-classic-new .work-item {
  font-size: 12px;
}

.tpl-classic-new .work-name {
  font-weight: 600;
  color: #333;
  margin-bottom: 2px;
}

.tpl-classic-new .work-desc {
  font-size: 11px;
  color: #666;
}

/* Main */
.tpl-classic-new .main {
  padding: 33px 24px;
  background: #f2f2f2;
}

.tpl-classic-new .main-section {
  margin-bottom: 24px;
}

.tpl-classic-new .main-section:last-child {
  margin-bottom: 0;
}

.tpl-classic-new .section-header {
  display: flex;
  align-items: center;
  margin-bottom: 12px;
}

.tpl-classic-new .section-header svg {
  margin-right: 8px;
}

.tpl-classic-new .section-header h2 {
  position: relative;
  font-size: 16px;
  font-weight: bold;
  color: #fff;
  width: max-content;
  line-height: 26px;
  padding: 0 60px 0 10px;
  border-top-left-radius: 3px;
  border-bottom-left-radius: 3px;
  margin: 0;
}

.tpl-classic-new .section-header h2::before {
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

.tpl-classic-new .section-body {
  font-size: 13px;
  line-height: 1.6;
}

.tpl-classic-new .aboutme-content p {
  margin: 6px 0;
  color: #333;
}

/* Work Item */
.tpl-classic-new .work-item {
  margin-bottom: 16px;
  padding-bottom: 16px;
  border-bottom: 1px solid #e0e0e0;
}

.tpl-classic-new .work-item:last-child {
  margin-bottom: 0;
  padding-bottom: 0;
  border-bottom: none;
}

.tpl-classic-new .work-header {
  display: flex;
  justify-content: space-between;
  align-items: baseline;
  margin-bottom: 4px;
}

.tpl-classic-new .company {
  font-size: 15px;
  font-weight: 700;
  color: #1a1a1a;
}

.tpl-classic-new .work-time {
  font-size: 12px;
  color: #999;
}

.tpl-classic-new .department {
  font-size: 13px;
  color: #666;
  margin-bottom: 4px;
}

.tpl-classic-new .work-desc {
  font-size: 12px;
  color: #555;
  white-space: pre-wrap;
  line-height: 1.6;
}

/* Project Item */
.tpl-classic-new .project-item {
  margin-bottom: 16px;
  padding-bottom: 16px;
  border-bottom: 1px solid #e0e0e0;
}

.tpl-classic-new .project-item:last-child {
  margin-bottom: 0;
  padding-bottom: 0;
  border-bottom: none;
}

.tpl-classic-new .project-header {
  display: flex;
  justify-content: space-between;
  align-items: baseline;
  margin-bottom: 6px;
}

.tpl-classic-new .project-name {
  font-size: 15px;
  font-weight: 700;
  color: #1a1a1a;
}

.tpl-classic-new .project-time {
  font-size: 12px;
  color: #999;
}

.tpl-classic-new .project-role {
  margin-bottom: 6px;
}

.tpl-classic-new .role-badge {
  display: inline-block;
  padding: 2px 12px;
  border-radius: 12px;
  font-size: 11px;
  color: #fff;
  font-weight: 500;
}

.tpl-classic-new .project-desc,
.tpl-classic-new .project-content {
  font-size: 12px;
  color: #555;
  margin-bottom: 4px;
  line-height: 1.6;
}

.tpl-classic-new .label {
  color: #666;
  font-weight: 500;
}

.tpl-classic-new .project-content {
  white-space: pre-wrap;
}
</style>
