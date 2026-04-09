<template>
  <div class="tpl-classic">
    <!-- ========== 头部 ========== -->
    <div class="tpl-header">
      <div class="tpl-name">{{ data.personal?.name || '姓名' }}</div>
      <div class="tpl-contact">
        <span v-if="data.personal?.phone">
          <svg width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" style="vertical-align:middle;margin-right:2px"><path d="M22 16.92v3a2 2 0 0 1-2.18 2 19.79 19.79 0 0 1-8.63-3.07A19.5 19.5 0 0 1 4.69 12.2 19.79 19.79 0 0 1 1.61 3.61 2 2 0 0 1 3.58 1h3a2 2 0 0 1 2 1.72c.127.96.361 1.903.7 2.81a2 2 0 0 1-.45 2.11L7.91 8.55A16 16 0 0 0 15 15.09l.92-.92a2 2 0 0 1 2.11-.45c.907.339 1.85.573 2.81.7A2 2 0 0 1 22 16.92z"/></svg>
          {{ data.personal.phone }}
        </span>
        <span v-if="data.personal?.email">
          <svg width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" style="vertical-align:middle;margin-right:2px"><path d="M4 4h16c1.1 0 2 .9 2 2v12c0 1.1-.9 2-2 2H4c-1.1 0-2-.9-2-2V6c0-1.1.9-2 2-2z"/><polyline points="22,6 12,13 2,6"/></svg>
          {{ data.personal.email }}
        </span>
        <span v-if="data.personal?.targetPosition">
          <svg width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" style="vertical-align:middle;margin-right:2px"><circle cx="12" cy="12" r="10"/><line x1="12" y1="8" x2="12" y2="16"/><line x1="8" y1="12" x2="16" y2="12"/></svg>
          {{ data.personal.targetPosition }}
        </span>
      </div>
    </div>

    <!-- ========== 主体内容 ========== -->
    <div class="tpl-body">

      <!-- 自我评价 -->
      <div v-if="data.summary" class="tpl-section">
        <div class="section-title">
          <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" style="vertical-align:middle;margin-right:4px"><circle cx="12" cy="12" r="10"/><line x1="12" y1="8" x2="12" y2="12"/><line x1="12" y1="16" x2="12.01" y2="16"/></svg>
          自我评价
        </div>
        <div class="section-content summary-content">{{ data.summary }}</div>
      </div>

      <!-- 教育背景 -->
      <div v-if="data.education && data.education.length" class="tpl-section">
        <div class="section-title">
          <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" style="vertical-align:middle;margin-right:4px"><path d="M22 10v6M2 10l10-5 10 5-10 5z"/><path d="M6 12v5c3 3 9 3 12 0v-5"/></svg>
          教育背景
        </div>
        <div class="timeline">
          <div v-for="(edu, idx) in data.education" :key="idx" class="timeline-item">
            <div class="timeline-dot"></div>
            <div class="timeline-content">
              <div class="item-header">
                <span class="item-title">{{ edu.school }}</span>
                <span class="item-date">{{ edu.startDate }} — {{ edu.endDate || '至今' }}</span>
              </div>
              <div class="item-sub">{{ edu.degree }} · {{ edu.major }}</div>
              <div v-if="edu.gpa" class="item-desc">GPA：{{ edu.gpa }}</div>
            </div>
          </div>
        </div>
      </div>

      <!-- 工作经历 -->
      <div v-if="data.experience && data.experience.length" class="tpl-section">
        <div class="section-title">
          <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" style="vertical-align:middle;margin-right:4px"><rect x="2" y="7" width="20" height="14" rx="2" ry="2"/><path d="M16 21V5a2 2 0 0 0-2-2h-4a2 2 0 0 0-2 2v16"/></svg>
          工作经历
        </div>
        <div class="timeline">
          <div v-for="(exp, idx) in data.experience" :key="idx" class="timeline-item">
            <div class="timeline-dot"></div>
            <div class="timeline-content">
              <div class="item-header">
                <span class="item-title">{{ exp.company }}</span>
                <span class="item-date">{{ exp.startDate }} — {{ exp.endDate || '至今' }}</span>
              </div>
              <div class="item-sub">{{ exp.position }}</div>
              <div v-if="exp.description" class="item-desc">{{ exp.description }}</div>
            </div>
          </div>
        </div>
      </div>

      <!-- 项目经历 -->
      <div v-if="data.projects && data.projects.length" class="tpl-section">
        <div class="section-title">
          <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" style="vertical-align:middle;margin-right:4px"><polygon points="13 2 3 14 12 14 11 22 21 10 12 10 13 2"/></svg>
          项目经历
        </div>
        <div class="timeline">
          <div v-for="(proj, idx) in data.projects" :key="idx" class="timeline-item">
            <div class="timeline-dot"></div>
            <div class="timeline-content">
              <div class="item-header">
                <span class="item-title">{{ proj.name }}</span>
                <span class="item-date">{{ proj.startDate }} — {{ proj.endDate || '至今' }}</span>
              </div>
              <div class="item-sub" v-if="proj.role">{{ proj.role }}</div>
              <div v-if="proj.description" class="item-desc">{{ proj.description }}</div>
              <div v-if="proj.achievements" class="item-achievement">
                <svg width="11" height="11" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" style="vertical-align:middle;margin-right:2px"><polyline points="20 6 9 17 4 12"/></svg>
                {{ proj.achievements }}
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 专业技能 -->
      <div v-if="data.skills && data.skills.length" class="tpl-section">
        <div class="section-title">
          <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" style="vertical-align:middle;margin-right:4px"><path d="M14.7 6.3a1 1 0 0 0 0 1.4l1.6 1.6a1 1 0 0 0 1.4 0l3.77-3.77a6 6 0 0 1-7.94 7.94l-6.91 6.91a2.12 2.12 0 0 1-3-3l6.91-6.91a6 6 0 0 1 7.94-7.94l-3.76 3.76z"/></svg>
          专业技能
        </div>
        <div class="skill-tags">
          <span v-for="(skill, idx) in data.skills" :key="idx" class="skill-tag">{{ skill }}</span>
        </div>
      </div>

    </div>
  </div>
</template>

<script setup lang="ts">
defineProps<{ data: Record<string, any> }>()
</script>

<style scoped>
.tpl-classic {
  width: 820px;
  min-height: 1160px;
  background: #fff;
  font-family: 'Microsoft YaHei', 'PingFang SC', sans-serif;
  font-size: 14px;
  color: #333;
  box-shadow: 0 4px 24px rgba(0,0,0,0.12);
  overflow: hidden;
}

/* ---- 头部 ---- */
.tpl-header {
  background: linear-gradient(135deg, #1a73e8 0%, #4285f4 50%, #5e97f6 100%);
  padding: 36px 48px 28px;
  text-align: center;
  position: relative;
  overflow: hidden;
}
.tpl-header::before {
  content: '';
  position: absolute;
  top: -40px;
  right: -40px;
  width: 200px;
  height: 200px;
  background: rgba(255,255,255,0.06);
  border-radius: 50%;
}
.tpl-header::after {
  content: '';
  position: absolute;
  bottom: -60px;
  left: 50%;
  transform: translateX(-50%);
  width: 300px;
  height: 120px;
  background: rgba(255,255,255,0.04);
  border-radius: 50%;
}
.tpl-name {
  font-size: 34px;
  font-weight: 700;
  color: #fff;
  letter-spacing: 4px;
  margin-bottom: 12px;
  text-shadow: 0 2px 8px rgba(0,0,0,0.15);
}
.tpl-contact {
  display: flex;
  justify-content: center;
  gap: 24px;
  font-size: 12.5px;
  color: rgba(255,255,255,0.92);
  flex-wrap: wrap;
}
.tpl-contact span {
  display: flex;
  align-items: center;
  gap: 4px;
}

/* ---- 主体 ---- */
.tpl-body {
  padding: 28px 48px 40px;
}

/* ---- 通用区块标题 ---- */
.section-title {
  font-size: 16px;
  font-weight: 700;
  color: #1a73e8;
  border-bottom: 2px solid #1a73e8;
  padding-bottom: 6px;
  margin-bottom: 14px;
  margin-top: 0;
  display: flex;
  align-items: center;
  letter-spacing: 0.5px;
}
.tpl-section {
  margin-bottom: 24px;
}

/* ---- 时间线 ---- */
.timeline {
  position: relative;
  padding-left: 16px;
}
.timeline::before {
  content: '';
  position: absolute;
  left: 0;
  top: 6px;
  bottom: 6px;
  width: 3px;
  background: linear-gradient(to bottom, #1a73e8, #4285f4);
  border-radius: 2px;
}
.timeline-item {
  position: relative;
  padding-left: 20px;
  margin-bottom: 16px;
}
.timeline-item:last-child { margin-bottom: 0; }
.timeline-dot {
  position: absolute;
  left: -8px;
  top: 5px;
  width: 10px;
  height: 10px;
  background: #fff;
  border: 2.5px solid #1a73e8;
  border-radius: 50%;
  z-index: 1;
}
.item-header {
  display: flex;
  justify-content: space-between;
  align-items: baseline;
  margin-bottom: 3px;
}
.item-title {
  font-size: 14px;
  font-weight: 700;
  color: #1a1a1a;
}
.item-date {
  font-size: 11.5px;
  color: #999;
  white-space: nowrap;
  margin-left: 12px;
}
.item-sub {
  font-size: 12.5px;
  color: #666;
  margin-bottom: 3px;
}
.item-desc {
  font-size: 12.5px;
  color: #555;
  line-height: 1.65;
  margin-top: 3px;
}
.item-achievement {
  font-size: 12px;
  color: #2e7d32;
  margin-top: 4px;
  background: #e8f5e9;
  border-radius: 4px;
  padding: 3px 8px;
  display: inline-block;
  font-weight: 500;
}

/* ---- 技能标签 ---- */
.skill-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}
.skill-tag {
  background: linear-gradient(135deg, #e8f0fe, #d2e3fc);
  color: #1a56db;
  border: 1px solid #c2d9f8;
  border-radius: 16px;
  padding: 4px 14px;
  font-size: 12.5px;
  font-weight: 500;
  transition: all 0.2s;
}

/* ---- 自我评价 ---- */
.summary-content {
  font-size: 13.5px;
  color: #555;
  line-height: 1.8;
  background: #f8f9fb;
  border-left: 3px solid #1a73e8;
  padding: 10px 14px;
  border-radius: 0 6px 6px 0;
}
</style>
