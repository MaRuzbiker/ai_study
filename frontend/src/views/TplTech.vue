<template>
  <div class="tpl-tech">
    <!-- ========== 头部 ========== -->
    <div class="tpl-header">
      <div class="header-grid-bg"></div>
      <div class="header-content">
        <div class="tpl-name">{{ data.personal?.name || '姓名' }}</div>
        <div v-if="data.personal?.targetPosition" class="tpl-position">{{ data.personal.targetPosition }}</div>
        <div class="tpl-contact">
          <span v-if="data.personal?.phone">
            <svg width="11" height="11" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M22 16.92v3a2 2 0 0 1-2.18 2 19.79 19.79 0 0 1-8.63-3.07A19.5 19.5 0 0 1 4.69 12.2 19.79 19.79 0 0 1 1.61 3.61 2 2 0 0 1 3.58 1h3a2 2 0 0 1 2 1.72c.127.96.361 1.903.7 2.81a2 2 0 0 1-.45 2.11L7.91 8.55A16 16 0 0 0 15 15.09l.92-.92a2 2 0 0 1 2.11-.45c.907.339 1.85.573 2.81.7A2 2 0 0 1 22 16.92z"/></svg>
            {{ data.personal.phone }}
          </span>
          <span v-if="data.personal?.email">
            <svg width="11" height="11" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M4 4h16c1.1 0 2 .9 2 2v12c0 1.1-.9 2-2 2H4c-1.1 0-2-.9-2-2V6c0-1.1.9-2 2-2z"/><polyline points="22,6 12,13 2,6"/></svg>
            {{ data.personal.email }}
          </span>
        </div>
      </div>
      <!-- 技术栈标签（头部） -->
      <div v-if="data.skills && data.skills.length" class="header-tags">
        <span v-for="(skill, idx) in data.skills.slice(0, 8)" :key="idx" class="h-tag">{{ skill }}</span>
      </div>
    </div>

    <!-- ========== 主体内容 ========== -->
    <div class="tpl-body">

      <!-- 技术摘要（个人标签网格） -->
      <div v-if="data.skills && data.skills.length" class="tpl-section">
        <div class="section-header">
          <span class="section-label">&gt; </span>SKILLS
          <span class="section-line"></span>
        </div>
        <div class="skill-grid">
          <span v-for="(skill, idx) in data.skills" :key="idx" class="tech-tag">{{ skill }}</span>
        </div>
      </div>

      <!-- 教育背景 -->
      <div v-if="data.education && data.education.length" class="tpl-section">
        <div class="section-header">
          <span class="section-label">&gt; </span>EDUCATION
          <span class="section-line"></span>
        </div>
        <div v-for="(edu, idx) in data.education" :key="idx" class="edu-row">
          <span class="row-index">0{{ idx + 1 }}</span>
          <div class="row-content">
            <div class="row-title">{{ edu.school }}</div>
            <div class="row-meta">{{ edu.degree }} · {{ edu.major }}
              <span v-if="edu.gpa"> · GPA: {{ edu.gpa }}</span>
            </div>
            <div class="row-date">{{ edu.startDate }} — {{ edu.endDate || 'Present' }}</div>
          </div>
        </div>
      </div>

      <!-- 工作经历 -->
      <div v-if="data.experience && data.experience.length" class="tpl-section">
        <div class="section-header">
          <span class="section-label">&gt; </span>WORK EXPERIENCE
          <span class="section-line"></span>
        </div>
        <div v-for="(exp, idx) in data.experience" :key="idx" class="exp-card">
          <div class="card-index">0{{ idx + 1 }}</div>
          <div class="card-body">
            <div class="card-header-row">
              <span class="card-company">{{ exp.company }}</span>
              <span class="card-period">{{ exp.startDate }} — {{ exp.endDate || 'Present' }}</span>
            </div>
            <div class="card-position">{{ exp.position }}</div>
            <div v-if="exp.description" class="card-desc">{{ exp.description }}</div>
          </div>
        </div>
      </div>

      <!-- 项目经历 -->
      <div v-if="data.projects && data.projects.length" class="tpl-section">
        <div class="section-header">
          <span class="section-label">&gt; </span>PROJECT EXPERIENCE
          <span class="section-line"></span>
        </div>
        <div v-for="(proj, idx) in data.projects" :key="idx" class="exp-card">
          <div class="card-index">0{{ idx + 1 }}</div>
          <div class="card-body">
            <div class="card-header-row">
              <span class="card-company">{{ proj.name }}</span>
              <span class="card-period">{{ proj.startDate }} — {{ proj.endDate || 'Present' }}</span>
            </div>
            <div v-if="proj.role" class="card-position">{{ proj.role }}</div>
            <div v-if="proj.description" class="card-desc">{{ proj.description }}</div>
            <div v-if="proj.achievements" class="card-achievement">
              <span class="ach-prefix">→</span> {{ proj.achievements }}
            </div>
          </div>
        </div>
      </div>

      <!-- 自我评价 -->
      <div v-if="data.summary" class="tpl-section">
        <div class="section-header">
          <span class="section-label">&gt; </span>ABOUT ME
          <span class="section-line"></span>
        </div>
        <div class="summary-text">{{ data.summary }}</div>
      </div>

      <!-- 校园经历 -->
      <div v-if="data.campusExperiences && data.campusExperiences.length" class="tech-section">
        <div class="tech-sec-title">// 校园经历</div>
        <div v-for="(c, idx) in data.campusExperiences" :key="idx" class="tech-item">
          <div class="tech-item-header"><span class="tech-item-title">{{ c.organization }}</span><span class="tech-item-date">{{ c.startDate }} — {{ c.endDate || '至今' }}</span></div>
          <div class="tech-item-sub">{{ c.role }}</div>
          <div v-if="c.description" class="tech-item-desc">{{ c.description }}</div>
        </div>
      </div>

      <!-- 竞赛经历 -->
      <div v-if="data.competitions && data.competitions.length" class="tech-section">
        <div class="tech-sec-title">// 竞赛经历</div>
        <div v-for="(comp, idx) in data.competitions" :key="idx" class="tech-item">
          <div class="tech-item-header"><span class="tech-item-title">{{ comp.name }}</span><span class="tech-item-date">{{ comp.date }}</span></div>
          <div v-if="comp.level" class="tech-item-sub">{{ comp.level }} · {{ comp.rank || '' }}</div>
          <div v-if="comp.description" class="tech-item-desc">{{ comp.description }}</div>
        </div>
      </div>

      <!-- 荣誉奖励 -->
      <div v-if="data.awards && data.awards.length" class="tech-section">
        <div class="tech-sec-title">// 荣誉奖励</div>
        <div class="tech-awards">
          <span v-for="(aw, idx) in data.awards" :key="idx" class="tech-award-tag">
            {{ aw.name }}<span v-if="aw.level"> [{{ aw.level }}]</span>
          </span>
        </div>
      </div>

      <!-- 证书 -->
      <div v-if="data.certificates && data.certificates.length" class="tech-section">
        <div class="tech-sec-title">// 证书资质</div>
        <div class="tech-cert-list">
          <span v-for="(cert, idx) in data.certificates" :key="idx" class="tech-cert-tag">{{ cert.name }}</span>
        </div>
      </div>

    </div>
  </div>
</template>

<script setup lang="ts">
defineProps<{ data: Record<string, any> }>()
</script>

<style scoped>
.tpl-tech {
  width: 820px;
  min-height: 1160px;
  background: #fafafa;
  font-family: 'Consolas', 'Microsoft YaHei', monospace;
  font-size: 13px;
  color: #333;
  box-shadow: 0 4px 24px rgba(0,0,0,0.12);
  overflow: hidden;
}

/* ---- 头部 ---- */
.tpl-header {
  background: linear-gradient(135deg, #0d47a1 0%, #1565c0 40%, #1976d2 100%);
  padding: 32px 48px 24px;
  position: relative;
  overflow: hidden;
}
.header-grid-bg {
  position: absolute;
  inset: 0;
  background-image:
    linear-gradient(rgba(255,255,255,0.04) 1px, transparent 1px),
    linear-gradient(90deg, rgba(255,255,255,0.04) 1px, transparent 1px);
  background-size: 30px 30px;
  pointer-events: none;
}
.header-content { position: relative; }
.tpl-name {
  font-size: 30px;
  font-weight: 700;
  color: #fff;
  letter-spacing: 4px;
  text-transform: uppercase;
  font-family: 'Consolas', monospace;
  text-shadow: 0 2px 8px rgba(0,0,0,0.3);
  margin-bottom: 6px;
}
.tpl-position {
  font-size: 13px;
  color: rgba(255,255,255,0.75);
  letter-spacing: 1px;
  margin-bottom: 8px;
  font-family: 'Consolas', monospace;
}
.tpl-contact {
  display: flex;
  gap: 20px;
  font-size: 11.5px;
  color: rgba(255,255,255,0.8);
  font-family: 'Consolas', monospace;
}
.tpl-contact span { display: flex; align-items: center; gap: 4px; }

/* 头部技术栈 */
.header-tags {
  position: relative;
  margin-top: 16px;
  display: flex;
  flex-wrap: wrap;
  gap: 6px;
}
.h-tag {
  background: rgba(255,255,255,0.15);
  border: 1px solid rgba(255,255,255,0.3);
  color: rgba(255,255,255,0.92);
  border-radius: 4px;
  padding: 3px 10px;
  font-size: 11.5px;
  font-family: 'Consolas', monospace;
}

/* ---- 主体 ---- */
.tpl-body {
  padding: 24px 48px 40px;
}

/* ---- 通用区块 ---- */
.tpl-section { margin-bottom: 28px; }
.section-header {
  font-size: 13px;
  font-weight: 700;
  color: #1976d2;
  font-family: 'Consolas', monospace;
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 14px;
  border-bottom: 1px solid #e3eaf3;
  padding-bottom: 8px;
}
.section-label { color: #0d47a1; }
.section-line { flex: 1; height: 1px; background: #e3eaf3; }

/* ---- 技能网格 ---- */
.skill-grid {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}
.tech-tag {
  background: linear-gradient(135deg, #e3f0ff, #bbd6f7);
  color: #0d47a1;
  border: 1px solid #90caf9;
  border-radius: 4px;
  padding: 4px 12px;
  font-size: 12px;
  font-family: 'Consolas', monospace;
  font-weight: 500;
}

/* ---- 教育行 ---- */
.edu-row {
  display: flex;
  gap: 16px;
  margin-bottom: 12px;
  align-items: flex-start;
}
.row-index {
  font-size: 22px;
  font-weight: 700;
  color: #bbdefb;
  font-family: 'Consolas', monospace;
  flex-shrink: 0;
  line-height: 1;
  min-width: 28px;
}
.row-title { font-size: 14px; font-weight: 700; color: #0d47a1; font-family: 'Consolas', monospace; }
.row-meta { font-size: 12px; color: #555; margin: 2px 0; font-family: 'Consolas', monospace; }
.row-date { font-size: 11px; color: #90a4ae; font-family: 'Consolas', monospace; }

/* ---- 经验卡片 ---- */
.exp-card {
  display: flex;
  gap: 16px;
  background: #fff;
  border: 1px solid #e3eaf3;
  border-radius: 6px;
  padding: 14px 16px;
  margin-bottom: 10px;
}
.card-index {
  font-size: 22px;
  font-weight: 700;
  color: #e3f0ff;
  font-family: 'Consolas', monospace;
  flex-shrink: 0;
  line-height: 1;
  min-width: 28px;
}
.card-body { flex: 1; }
.card-header-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 3px;
}
.card-company { font-size: 14px; font-weight: 700; color: #0d47a1; font-family: 'Consolas', monospace; }
.card-period { font-size: 11px; color: #999; font-family: 'Consolas', monospace; white-space: nowrap; }
.card-position { font-size: 12px; color: #1976d2; font-style: italic; margin-bottom: 5px; font-family: 'Consolas', monospace; }
.card-desc { font-size: 12.5px; color: #444; line-height: 1.7; font-family: 'Consolas', monospace; }
.card-achievement {
  margin-top: 5px;
  font-size: 12px;
  color: #1565c0;
  background: #e3f0ff;
  border-radius: 4px;
  padding: 3px 10px;
  font-family: 'Consolas', monospace;
  display: inline-block;
}
.ach-prefix { margin-right: 6px; }

/* ---- 自我评价 ---- */
.summary-text {
  font-size: 12.5px;
  color: #555;
  line-height: 1.85;
  background: #f0f4f8;
  border-left: 3px solid #1976d2;
  padding: 10px 16px;
  border-radius: 0 4px 4px 0;
  font-family: 'Consolas', monospace;
}

/* 科技风格新模块 */
.tech-section { margin-bottom: 18px; }
.tech-sec-title {
  font-size: 13px;
  color: #1976d2;
  font-family: 'Courier New', Courier, monospace;
  margin-bottom: 10px;
  padding-bottom: 4px;
  border-bottom: 1px solid #e3f2fd;
}
.tech-item { margin-bottom: 12px; }
.tech-item-header { display: flex; justify-content: space-between; margin-bottom: 2px; }
.tech-item-title { font-size: 13.5px; font-weight: 700; color: #0d47a1; }
.tech-item-date { font-size: 11px; color: #78909c; }
.tech-item-sub { font-size: 12px; color: #1565c0; font-style: italic; margin-bottom: 3px; }
.tech-item-desc { font-size: 12.5px; color: #546e7a; line-height: 1.6; }
.tech-awards { display: flex; flex-wrap: wrap; gap: 8px; }
.tech-award-tag {
  background: #e3f2fd;
  color: #1565c0;
  border: 1px solid #90caf9;
  border-radius: 4px;
  padding: 4px 12px;
  font-size: 12px;
  font-weight: 600;
  font-family: 'Courier New', monospace;
}
.tech-cert-list { display: flex; flex-wrap: wrap; gap: 8px; }
.tech-cert-tag {
  background: rgba(255,255,255,0.1);
  border: 1px solid rgba(255,255,255,0.2);
  color: #90caf9;
  border-radius: 4px;
  padding: 4px 12px;
  font-size: 12px;
  font-family: 'Courier New', monospace;
}
</style>
