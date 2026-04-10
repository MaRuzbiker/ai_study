<template>
  <div class="tpl-minimal">
    <!-- ========== 头部：姓名 + 联系 ========== -->
    <div class="tpl-header">
      <div class="tpl-name">{{ data.personal?.name || '姓名' }}</div>
      <div class="tpl-contact">
        <span v-if="data.personal?.phone">{{ data.personal.phone }}</span>
        <span v-if="data.personal?.email" class="sep">·</span>
        <span v-if="data.personal?.email">{{ data.personal.email }}</span>
        <span v-if="data.personal?.targetPosition" class="sep">·</span>
        <span v-if="data.personal?.targetPosition">{{ data.personal.targetPosition }}</span>
      </div>
    </div>

    <!-- 分隔线 -->
    <div class="divider"></div>

    <!-- ========== 主体内容 ========== -->
    <div class="tpl-body">

      <!-- 教育背景 -->
      <div v-if="data.education && data.education.length" class="tpl-section">
        <div class="section-label">教育背景</div>
        <div v-for="(edu, idx) in data.education" :key="idx" class="edu-row">
          <div class="row-left">
            <div class="row-title">{{ edu.school }}</div>
            <div class="row-sub">{{ edu.degree }} · {{ edu.major }}</div>
          </div>
          <div class="row-right">
            <span class="row-date">{{ edu.startDate }} — {{ edu.endDate || '至今' }}</span>
            <span v-if="edu.gpa" class="row-gpa">GPA {{ edu.gpa }}</span>
          </div>
        </div>
      </div>

      <div v-if="data.education?.length" class="divider divider-light"></div>

      <!-- 校园经历 -->
      <div v-if="data.campusExperiences && data.campusExperiences.length" class="tpl-section">
        <div class="minimal-label">校园经历</div>
        <div v-for="(c, idx) in data.campusExperiences" :key="idx" class="minimal-item">
          <div class="minimal-item-row"><span class="minimal-item-title">{{ c.organization }}</span><span class="minimal-item-date">{{ c.startDate }} — {{ c.endDate || '至今' }}</span></div>
          <div class="minimal-item-sub">{{ c.role }}</div>
          <div v-if="c.description" class="minimal-item-desc">{{ c.description }}</div>
        </div>
      </div>

      <!-- 竞赛经历 -->
      <div v-if="data.competitions && data.competitions.length" class="tpl-section">
        <div class="minimal-label">竞赛经历</div>
        <div v-for="(comp, idx) in data.competitions" :key="idx" class="minimal-item">
          <div class="minimal-item-row"><span class="minimal-item-title">{{ comp.name }}</span><span class="minimal-item-date">{{ comp.date }}</span></div>
          <div v-if="comp.level" class="minimal-item-sub">{{ comp.level }} · {{ comp.rank || '' }}</div>
          <div v-if="comp.description" class="minimal-item-desc">{{ comp.description }}</div>
        </div>
      </div>

      <div v-if="data.campusExperiences?.length || data.competitions?.length" class="divider divider-light"></div>

      <!-- 工作经历 -->
      <div v-if="data.experience && data.experience.length" class="tpl-section">
        <div class="section-label">工作经历</div>
        <div v-for="(exp, idx) in data.experience" :key="idx" class="exp-row">
          <div class="row-left">
            <div class="row-title">{{ exp.company }} <span class="row-title-sep">—</span> {{ exp.position }}</div>
            <div v-if="exp.description" class="row-desc">{{ exp.description }}</div>
          </div>
          <div class="row-right">
            <span class="row-date">{{ exp.startDate }} — {{ exp.endDate || '至今' }}</span>
          </div>
        </div>
      </div>

      <div v-if="data.experience?.length" class="divider divider-light"></div>

      <!-- 项目经历 -->
      <div v-if="data.projects && data.projects.length" class="tpl-section">
        <div class="section-label">项目经历</div>
        <div v-for="(proj, idx) in data.projects" :key="idx" class="exp-row">
          <div class="row-left">
            <div class="row-title">{{ proj.name }}</div>
            <div v-if="proj.role" class="row-sub">{{ proj.role }}</div>
            <div v-if="proj.description" class="row-desc">{{ proj.description }}</div>
            <div v-if="proj.achievements" class="row-ach">{{ proj.achievements }}</div>
          </div>
          <div class="row-right">
            <span class="row-date">{{ proj.startDate }} — {{ proj.endDate || '至今' }}</span>
          </div>
        </div>
      </div>

      <div v-if="data.projects?.length" class="divider divider-light"></div>

      <!-- 荣誉奖励 -->
      <div v-if="data.awards && data.awards.length" class="tpl-section">
        <div class="minimal-label">荣誉奖励</div>
        <div class="minimal-awards">
          <div v-for="(aw, idx) in data.awards" :key="idx" class="minimal-award-row">
            <span>{{ aw.name }}</span>
            <span v-if="aw.date" class="minimal-date">{{ aw.date }}</span>
            <span v-if="aw.level" class="minimal-level">{{ aw.level }}</span>
          </div>
        </div>
      </div>

      <!-- 证书 -->
      <div v-if="data.certificates && data.certificates.length" class="tpl-section">
        <div class="minimal-label">证书资质</div>
        <div class="minimal-certs">
          <span v-for="(cert, idx) in data.certificates" :key="idx" class="minimal-cert">{{ cert.name }}</span>
        </div>
      </div>

      <div v-if="data.awards?.length || data.certificates?.length" class="divider divider-light"></div>

      <!-- 专业技能 -->
      <div v-if="data.skills && data.skills.length" class="tpl-section">
        <div class="section-label">专业技能</div>
        <div class="skill-row">
          <span v-for="(skill, idx) in data.skills" :key="idx" class="skill-item">
            {{ skill }}
            <span v-if="idx < data.skills.length - 1" class="skill-sep">/</span>
          </span>
        </div>
      </div>

      <div v-if="data.skills?.length" class="divider divider-light"></div>

      <!-- 自我评价 -->
      <div v-if="data.summary" class="tpl-section">
        <div class="section-label">关于我</div>
        <div class="summary-text">{{ data.summary }}</div>
      </div>

    </div>
  </div>
</template>

<script setup lang="ts">
defineProps<{ data: Record<string, any> }>()
</script>

<style scoped>
.tpl-minimal {
  width: 820px;
  min-height: 1160px;
  background: #fff;
  font-family: Georgia, 'Times New Roman', 'Microsoft YaHei', serif;
  font-size: 14px;
  color: #222;
  box-shadow: 0 4px 24px rgba(0,0,0,0.10);
  overflow: hidden;
}

/* ---- 头部 ---- */
.tpl-header {
  padding: 52px 64px 24px;
}
.tpl-name {
  font-size: 36px;
  font-weight: 700;
  color: #111;
  letter-spacing: 3px;
  margin-bottom: 10px;
  font-family: Georgia, serif;
  line-height: 1.1;
}
.tpl-contact {
  font-size: 12.5px;
  color: #888;
  letter-spacing: 0.3px;
}
.tpl-contact .sep {
  margin: 0 6px;
  color: #ccc;
}

/* ---- 分隔线 ---- */
.divider {
  height: 1px;
  background: #e0e0e0;
  margin: 0 64px;
}
.divider-light {
  margin: 20px 64px;
  background: #f0f0f0;
}

/* ---- 主体 ---- */
.tpl-body {
  padding: 24px 64px 48px;
}

/* ---- 通用区块 ---- */
.tpl-section { margin-bottom: 0; }
.section-label {
  font-size: 10px;
  font-weight: 600;
  color: #999;
  text-transform: uppercase;
  letter-spacing: 2.5px;
  font-family: 'Helvetica Neue', Arial, sans-serif;
  margin-bottom: 14px;
  margin-top: 24px;
}

/* ---- 教育行 ---- */
.edu-row {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 14px;
}
.row-left { flex: 1; }
.row-title {
  font-size: 15px;
  font-weight: 600;
  color: #111;
  margin-bottom: 3px;
  letter-spacing: 0.3px;
}
.row-title-sep { font-weight: 400; color: #999; }
.row-sub { font-size: 12.5px; color: #666; margin-bottom: 3px; }
.row-desc { font-size: 13px; color: #444; line-height: 1.75; margin-top: 5px; }
.row-right {
  flex-shrink: 0;
  text-align: right;
  margin-left: 24px;
  padding-top: 2px;
}
.row-date { font-size: 11.5px; color: #aaa; display: block; margin-bottom: 3px; white-space: nowrap; }
.row-gpa { font-size: 11px; color: #bbb; display: block; font-style: italic; }

/* ---- 经验行 ---- */
.exp-row {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 16px;
}
.exp-row .row-title {
  font-size: 15px;
  font-weight: 600;
  color: #111;
  margin-bottom: 3px;
}
.exp-row .row-sub {
  font-size: 12.5px;
  color: #666;
  margin-bottom: 3px;
}
.exp-row .row-desc {
  font-size: 13px;
  color: #444;
  line-height: 1.75;
  margin-top: 4px;
}
.exp-row .row-right {
  margin-left: 24px;
  padding-top: 2px;
}
.row-ach {
  font-size: 12px;
  color: #666;
  margin-top: 5px;
  font-style: italic;
  border-left: 2px solid #ddd;
  padding-left: 10px;
}

/* ---- 技能 ---- */
.skill-row {
  font-size: 13px;
  color: #444;
  line-height: 1.8;
  letter-spacing: 0.3px;
}
.skill-item { font-style: italic; }
.skill-sep {
  color: #bbb;
  margin: 0 8px;
  font-style: normal;
}

/* ---- 自我评价 ---- */
.summary-text {
  font-size: 13.5px;
  color: #555;
  line-height: 1.9;
  font-style: italic;
  border-left: 2px solid #ccc;
  padding-left: 16px;
}

/* 简约风格新模块 */
.minimal-section { margin-bottom: 20px; }
.minimal-label {
  font-size: 11px;
  color: #aaa;
  font-weight: 700;
  text-transform: uppercase;
  letter-spacing: 2px;
  margin-bottom: 10px;
}
.minimal-item { margin-bottom: 14px; }
.minimal-item-row { display: flex; justify-content: space-between; margin-bottom: 2px; }
.minimal-item-title { font-size: 14px; font-weight: 600; color: #111; }
.minimal-item-date { font-size: 12px; color: #aaa; }
.minimal-item-sub { font-size: 12.5px; color: #666; margin-bottom: 3px; }
.minimal-item-desc { font-size: 12.5px; color: #444; line-height: 1.7; }
.minimal-awards { border-left: 2px solid #e0e0e0; padding-left: 14px; }
.minimal-award-row { display: flex; gap: 10px; align-items: baseline; font-size: 13px; color: #333; margin-bottom: 6px; }
.minimal-date { font-size: 11px; color: #aaa; }
.minimal-level { font-size: 11px; color: #888; background: #f0f0f0; padding: 1px 7px; border-radius: 3px; }
.minimal-certs { display: flex; flex-wrap: wrap; gap: 8px; }
.minimal-cert { font-size: 12.5px; color: #555; border: 1px solid #ddd; padding: 3px 10px; border-radius: 3px; }
</style>
