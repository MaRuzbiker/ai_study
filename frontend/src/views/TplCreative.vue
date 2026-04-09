<template>
  <div class="tpl-creative">
    <!-- ========== 头部 ========== -->
    <div class="tpl-header">
      <!-- 姓名圆角卡片 -->
      <div class="name-card">
        <div class="name-avatar">{{ (data.personal?.name || '名')[0] }}</div>
        <div class="name-info">
          <div class="name-text">{{ data.personal?.name || '姓名' }}</div>
          <div v-if="data.personal?.targetPosition" class="job-target">{{ data.personal.targetPosition }}</div>
        </div>
      </div>
      <!-- 联系方式条 -->
      <div class="contact-strip">
        <span v-if="data.personal?.phone" class="contact-chip">{{ data.personal.phone }}</span>
        <span v-if="data.personal?.email" class="contact-chip">{{ data.personal.email }}</span>
        <span v-if="data.personal?.gender" class="contact-chip">{{ data.personal.gender }}</span>
      </div>
    </div>

    <!-- ========== 主体内容 ========== -->
    <div class="tpl-body">

      <!-- 自我评价 -->
      <div v-if="data.summary" class="section-block">
        <div class="sec-header">
          <span class="sec-icon">💡</span>
          <span class="sec-title">自我评价</span>
          <span class="sec-line"></span>
        </div>
        <div class="summary-text">{{ data.summary }}</div>
      </div>

      <!-- 教育背景 -->
      <div v-if="data.education && data.education.length" class="section-block">
        <div class="sec-header">
          <span class="sec-icon">🎓</span>
          <span class="sec-title">教育背景</span>
          <span class="sec-line"></span>
        </div>
        <div class="timeline">
          <div v-for="(edu, idx) in data.education" :key="idx" class="timeline-item">
            <div class="timeline-dot green"></div>
            <div class="timeline-content">
              <div class="item-header">
                <span class="item-title">{{ edu.school }}</span>
                <span class="item-date green-text">{{ edu.startDate }} — {{ edu.endDate || '至今' }}</span>
              </div>
              <div class="item-sub">{{ edu.degree }} · {{ edu.major }}</div>
              <div v-if="edu.gpa" class="item-tag">GPA：{{ edu.gpa }}</div>
            </div>
          </div>
        </div>
      </div>

      <!-- 工作经历 -->
      <div v-if="data.experience && data.experience.length" class="section-block">
        <div class="sec-header">
          <span class="sec-icon">💼</span>
          <span class="sec-title">工作经历</span>
          <span class="sec-line"></span>
        </div>
        <div class="timeline">
          <div v-for="(exp, idx) in data.experience" :key="idx" class="timeline-item">
            <div class="timeline-dot green"></div>
            <div class="timeline-content">
              <div class="item-header">
                <span class="item-title">{{ exp.company }}</span>
                <span class="item-date green-text">{{ exp.startDate }} — {{ exp.endDate || '至今' }}</span>
              </div>
              <div class="item-sub italic">{{ exp.position }}</div>
              <div v-if="exp.description" class="item-desc">{{ exp.description }}</div>
            </div>
          </div>
        </div>
      </div>

      <!-- 项目经历 -->
      <div v-if="data.projects && data.projects.length" class="section-block">
        <div class="sec-header">
          <span class="sec-icon">🚀</span>
          <span class="sec-title">项目经历</span>
          <span class="sec-line"></span>
        </div>
        <div class="timeline">
          <div v-for="(proj, idx) in data.projects" :key="idx" class="timeline-item">
            <div class="timeline-dot green"></div>
            <div class="timeline-content">
              <div class="item-header">
                <span class="item-title">{{ proj.name }}</span>
                <span class="item-date green-text">{{ proj.startDate }} — {{ proj.endDate || '至今' }}</span>
              </div>
              <div v-if="proj.role" class="item-sub italic">{{ proj.role }}</div>
              <div v-if="proj.description" class="item-desc">{{ proj.description }}</div>
              <div v-if="proj.achievements" class="item-achieve">{{ proj.achievements }}</div>
            </div>
          </div>
        </div>
      </div>

      <!-- 专业技能 -->
      <div v-if="data.skills && data.skills.length" class="section-block section-skills">
        <div class="sec-header">
          <span class="sec-icon">🛠️</span>
          <span class="sec-title">专业技能</span>
          <span class="sec-line"></span>
        </div>
        <div class="skills-wrap">
          <span v-for="(skill, idx) in data.skills" :key="idx" class="skill-badge">{{ skill }}</span>
        </div>
      </div>

    </div>
  </div>
</template>

<script setup lang="ts">
defineProps<{ data: Record<string, any> }>()
</script>

<style scoped>
.tpl-creative {
  width: 820px;
  min-height: 1160px;
  background: #fff;
  font-family: 'PingFang SC', 'Microsoft YaHei', sans-serif;
  font-size: 13px;
  color: #333;
  box-shadow: 0 4px 24px rgba(0,0,0,0.12);
  overflow: hidden;
}

/* ---- 头部 ---- */
.tpl-header {
  background: linear-gradient(135deg, #2e7d32 0%, #43a047 50%, #66bb6a 100%);
  padding: 36px 48px 24px;
  position: relative;
}
.name-card {
  display: flex;
  align-items: center;
  gap: 18px;
  margin-bottom: 16px;
}
.name-avatar {
  width: 64px;
  height: 64px;
  border-radius: 50%;
  background: rgba(255,255,255,0.2);
  border: 3px solid rgba(255,255,255,0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 28px;
  color: #fff;
  font-weight: 700;
  flex-shrink: 0;
}
.name-info { flex: 1; }
.name-text {
  font-size: 30px;
  font-weight: 700;
  color: #fff;
  letter-spacing: 3px;
  font-family: 'Georgia', serif;
  text-shadow: 0 2px 6px rgba(0,0,0,0.15);
}
.job-target {
  font-size: 13px;
  color: rgba(255,255,255,0.85);
  margin-top: 4px;
  padding: 3px 12px;
  background: rgba(255,255,255,0.15);
  border-radius: 20px;
  display: inline-block;
  letter-spacing: 0.5px;
}
.contact-strip {
  display: flex;
  gap: 12px;
  flex-wrap: wrap;
}
.contact-chip {
  font-size: 12px;
  color: rgba(255,255,255,0.9);
  padding: 3px 12px;
  background: rgba(255,255,255,0.12);
  border-radius: 20px;
  border: 1px solid rgba(255,255,255,0.2);
}

/* ---- 主体 ---- */
.tpl-body { padding: 0 48px 32px; }

/* ---- 区块 ---- */
.section-block { margin-top: 24px; }
.section-skills { margin-top: 24px; }

.sec-header {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 14px;
}
.sec-icon { font-size: 15px; }
.sec-title {
  font-size: 15px;
  font-weight: 700;
  color: #2e7d32;
  white-space: nowrap;
  letter-spacing: 1px;
}
.sec-line {
  flex: 1;
  height: 1.5px;
  background: linear-gradient(to right, #a5d6a7, transparent);
  margin-left: 4px;
}

/* 自我评价 */
.summary-text {
  font-size: 13px;
  color: #555;
  line-height: 1.9;
  background: #f1f8e9;
  border-left: 4px solid #66bb6a;
  padding: 14px 16px;
  border-radius: 0 8px 8px 0;
}

/* 时间线 */
.timeline { padding-left: 8px; }
.timeline-item {
  display: flex;
  gap: 14px;
  margin-bottom: 16px;
  position: relative;
}
.timeline-item:last-child { margin-bottom: 0; }
.timeline-dot {
  width: 12px;
  height: 12px;
  border-radius: 50%;
  margin-top: 5px;
  flex-shrink: 0;
  position: relative;
  z-index: 1;
}
.timeline-dot.green {
  background: #43a047;
  border: 2px solid #a5d6a7;
  box-shadow: 0 0 0 3px rgba(67,160,71,0.15);
}
.timeline-item::before {
  content: '';
  position: absolute;
  left: 5px;
  top: 18px;
  bottom: -16px;
  width: 2px;
  background: #c8e6c9;
}
.timeline-item:last-child::before { display: none; }

.timeline-content { flex: 1; padding-bottom: 4px; }
.item-header {
  display: flex;
  justify-content: space-between;
  align-items: baseline;
  margin-bottom: 2px;
}
.item-title {
  font-size: 14px;
  font-weight: 700;
  color: #1b5e20;
}
.item-date { font-size: 11.5px; color: #999; white-space: nowrap; }
.green-text { color: #43a047 !important; }
.item-sub {
  font-size: 12.5px;
  color: #555;
  margin-bottom: 3px;
}
.item-sub.italic { font-style: italic; color: #388e3c; }
.item-desc {
  font-size: 12.5px;
  color: #555;
  line-height: 1.7;
  background: #f9f9f9;
  padding: 6px 10px;
  border-radius: 0 6px 6px 0;
  border-left: 3px solid #a5d6a7;
  margin-top: 4px;
}
.item-tag {
  font-size: 11.5px;
  color: #2e7d32;
  background: #e8f5e9;
  padding: 2px 8px;
  border-radius: 4px;
  display: inline-block;
  margin-top: 3px;
  font-weight: 500;
}
.item-achieve {
  font-size: 12px;
  color: #1b5e20;
  background: #e8f5e9;
  border: 1px solid #a5d6a7;
  padding: 4px 10px;
  border-radius: 4px;
  margin-top: 5px;
  font-weight: 500;
  display: inline-block;
}

/* 技能标签 */
.skills-wrap { display: flex; flex-wrap: wrap; gap: 10px; }
.skill-badge {
  background: linear-gradient(135deg, #e8f5e9, #c8e6c9);
  color: #2e7d32;
  border: 1px solid #a5d6a7;
  border-radius: 20px;
  padding: 5px 16px;
  font-size: 13px;
  font-weight: 500;
  letter-spacing: 0.3px;
  box-shadow: 0 1px 3px rgba(46,125,50,0.1);
}
</style>
