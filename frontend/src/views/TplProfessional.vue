<template>
  <div class="tpl-professional">
    <!-- ========== 左侧边栏 ========== -->
    <div class="tpl-sidebar">
      <!-- 头像区 -->
      <div class="sidebar-avatar">
        <div class="avatar-circle">
          <span>{{ (data.personal?.name || 'N').charAt(0) }}</span>
        </div>
      </div>

      <!-- 姓名 -->
      <div class="sidebar-name">{{ data.personal?.name || '姓名' }}</div>
      <div class="sidebar-position">{{ data.personal?.targetPosition || '职位' }}</div>
      <div class="sidebar-divider"></div>

      <!-- 基本信息 -->
      <div class="sidebar-section">
        <div class="sidebar-section-title">基本信息</div>
        <div v-if="data.personal?.gender" class="sidebar-info-item">
          <span class="info-label">性别</span>
          <span class="info-value">{{ data.personal.gender }}</span>
        </div>
        <div v-if="data.personal?.phone" class="sidebar-info-item">
          <span class="info-label">电话</span>
          <span class="info-value">{{ data.personal.phone }}</span>
        </div>
        <div v-if="data.personal?.email" class="sidebar-info-item">
          <span class="info-label">邮箱</span>
          <span class="info-value email-text">{{ data.personal.email }}</span>
        </div>
      </div>

      <!-- 技能证书 -->
      <div v-if="data.skills && data.skills.length" class="sidebar-section">
        <div class="sidebar-section-title">专业技能</div>
        <div v-for="(skill, idx) in data.skills" :key="idx" class="sidebar-skill-item">
          <span class="skill-dot"></span>
          {{ skill }}
        </div>
      </div>

      <!-- 教育背景（侧边栏） -->
      <div v-if="data.education && data.education.length" class="sidebar-section">
        <div class="sidebar-section-title">教育背景</div>
        <div v-for="(edu, idx) in data.education" :key="idx" class="sidebar-edu-item">
          <div class="edu-school">{{ edu.school }}</div>
          <div class="edu-info">{{ edu.degree }} · {{ edu.major }}</div>
          <div class="edu-date">{{ edu.startDate }} — {{ edu.endDate || '至今' }}</div>
        </div>
      </div>
    </div>

    <!-- ========== 右侧主内容 ========== -->
    <div class="tpl-main">

      <!-- 自我评价 -->
      <div v-if="data.summary" class="main-section">
        <div class="main-section-title">
          <span class="title-accent"></span>
          自我评价
        </div>
        <div class="summary-text">{{ data.summary }}</div>
      </div>

      <!-- 工作经历 -->
      <div v-if="data.experience && data.experience.length" class="main-section">
        <div class="main-section-title">
          <span class="title-accent"></span>
          工作经历
        </div>
        <div v-for="(exp, idx) in data.experience" :key="idx" class="exp-card">
          <div class="exp-header">
            <div class="exp-company">{{ exp.company }}</div>
            <div class="exp-date">{{ exp.startDate }} — {{ exp.endDate || '至今' }}</div>
          </div>
          <div class="exp-position">{{ exp.position }}</div>
          <div v-if="exp.description" class="exp-desc">{{ exp.description }}</div>
        </div>
      </div>

      <!-- 项目经历 -->
      <div v-if="data.projects && data.projects.length" class="main-section">
        <div class="main-section-title">
          <span class="title-accent"></span>
          项目经历
        </div>
        <div v-for="(proj, idx) in data.projects" :key="idx" class="proj-card">
          <div class="exp-header">
            <div class="exp-company">{{ proj.name }}</div>
            <div class="exp-date">{{ proj.startDate }} — {{ proj.endDate || '至今' }}</div>
          </div>
          <div v-if="proj.role" class="exp-position">{{ proj.role }}</div>
          <div v-if="proj.description" class="exp-desc">{{ proj.description }}</div>
          <div v-if="proj.achievements" class="proj-achievement">
            <span class="ach-icon">★</span> {{ proj.achievements }}
          </div>
        </div>
      </div>

      <!-- 校园经历 -->
      <div v-if="data.campusExperiences && data.campusExperiences.length" class="main-section">
        <div class="main-sec-title">校园经历</div>
        <div v-for="(c, idx) in data.campusExperiences" :key="idx" class="main-item">
          <div class="main-item-header"><span class="main-item-title">{{ c.organization }}</span><span class="main-item-date">{{ c.startDate }} — {{ c.endDate || '至今' }}</span></div>
          <div class="main-item-sub">{{ c.role }}</div>
          <div v-if="c.description" class="main-item-desc">{{ c.description }}</div>
        </div>
      </div>

      <!-- 竞赛经历 -->
      <div v-if="data.competitions && data.competitions.length" class="main-section">
        <div class="main-sec-title">竞赛经历</div>
        <div v-for="(comp, idx) in data.competitions" :key="idx" class="main-item">
          <div class="main-item-header"><span class="main-item-title">{{ comp.name }}</span><span class="main-item-date">{{ comp.date }}</span></div>
          <div v-if="comp.level" class="main-item-sub">{{ comp.level }} · {{ comp.rank || '' }}</div>
          <div v-if="comp.description" class="main-item-desc">{{ comp.description }}</div>
        </div>
      </div>

      <!-- 荣誉奖励 -->
      <div v-if="data.awards && data.awards.length" class="main-section">
        <div class="main-sec-title">荣誉奖励</div>
        <div v-for="(aw, idx) in data.awards" :key="idx" class="main-award-item">
          <span class="main-award-name">{{ aw.name }}</span>
          <span v-if="aw.date" class="main-award-date">{{ aw.date }}</span>
          <span v-if="aw.level" class="main-award-level">{{ aw.level }}</span>
        </div>
      </div>

      <!-- 证书 -->
      <div v-if="data.certificates && data.certificates.length" class="main-section">
        <div class="main-sec-title">证书资质</div>
        <div class="main-cert-list">
          <span v-for="(cert, idx) in data.certificates" :key="idx" class="main-cert-badge">{{ cert.name }}<span v-if="cert.date" class="cert-date"> · {{ cert.date }}</span></span>
        </div>
      </div>

    </div>
  </div>
</template>

<script setup lang="ts">
defineProps<{ data: Record<string, any> }>()
</script>

<style scoped>
.tpl-professional {
  width: 820px;
  min-height: 1160px;
  background: #fff;
  font-family: 'Microsoft YaHei', 'PingFang SC', sans-serif;
  font-size: 13px;
  color: #333;
  box-shadow: 0 4px 24px rgba(0,0,0,0.12);
  overflow: hidden;
  display: flex;
}

/* ========== 侧边栏 ========== */
.tpl-sidebar {
  width: 220px;
  flex-shrink: 0;
  background: #263238;
  padding: 0;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.sidebar-avatar {
  padding: 28px 0 16px;
}
.avatar-circle {
  width: 72px;
  height: 72px;
  background: linear-gradient(135deg, #ff6b35, #f7c94b);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 32px;
  font-weight: 700;
  color: #fff;
  letter-spacing: 1px;
  box-shadow: 0 4px 12px rgba(0,0,0,0.3);
}

.sidebar-name {
  font-size: 20px;
  font-weight: 700;
  color: #fff;
  text-align: center;
  letter-spacing: 1px;
  padding: 0 16px;
  margin-bottom: 4px;
}
.sidebar-position {
  font-size: 11.5px;
  color: #90a4ae;
  text-align: center;
  padding: 0 16px;
  margin-bottom: 12px;
}
.sidebar-divider {
  width: 80%;
  height: 1px;
  background: linear-gradient(to right, transparent, #37474f, transparent);
  margin-bottom: 16px;
}

/* 侧边栏通用区块 */
.sidebar-section {
  width: 100%;
  padding: 0 16px;
  margin-bottom: 20px;
}
.sidebar-section-title {
  font-size: 13px;
  font-weight: 700;
  color: #fff;
  border-bottom: 3px solid #ff6b35;
  padding-bottom: 5px;
  margin-bottom: 10px;
  letter-spacing: 1px;
}

/* 个人信息 */
.sidebar-info-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 7px;
  font-size: 12px;
  gap: 4px;
}
.info-label {
  color: #78909c;
  flex-shrink: 0;
}
.info-value {
  color: #cfd8dc;
  font-size: 11.5px;
  text-align: right;
  word-break: break-all;
}
.email-text {
  font-size: 10.5px;
}

/* 技能列表 */
.sidebar-skill-item {
  display: flex;
  align-items: flex-start;
  gap: 6px;
  margin-bottom: 6px;
  font-size: 12px;
  color: #cfd8dc;
  line-height: 1.5;
}
.skill-dot {
  width: 5px;
  height: 5px;
  background: #ff6b35;
  border-radius: 50%;
  flex-shrink: 0;
  margin-top: 5px;
}

/* 教育背景 */
.sidebar-edu-item {
  margin-bottom: 12px;
  padding-bottom: 12px;
  border-bottom: 1px solid #37474f;
}
.sidebar-edu-item:last-child { border-bottom: none; margin-bottom: 0; padding-bottom: 0; }
.edu-school { font-size: 12.5px; font-weight: 600; color: #eceff1; margin-bottom: 3px; }
.edu-info { font-size: 11.5px; color: #90a4ae; margin-bottom: 2px; }
.edu-date { font-size: 11px; color: #78909c; }

/* ========== 主内容区 ========== */
.tpl-main {
  flex: 1;
  padding: 32px 32px 40px;
  background: #fff;
  overflow: hidden;
}

.main-section {
  margin-bottom: 24px;
}
.main-section-title {
  font-size: 15px;
  font-weight: 700;
  color: #263238;
  margin-bottom: 14px;
  display: flex;
  align-items: center;
  gap: 8px;
  letter-spacing: 0.5px;
  border-bottom: 1px solid #eceff1;
  padding-bottom: 8px;
}
.title-accent {
  width: 4px;
  height: 18px;
  background: linear-gradient(to bottom, #ff6b35, #f7c94b);
  border-radius: 2px;
  flex-shrink: 0;
}

/* 自我评价 */
.summary-text {
  font-size: 13px;
  color: #555;
  line-height: 1.8;
  background: #fafafa;
  border-left: 3px solid #ff6b35;
  padding: 10px 14px;
  border-radius: 0 4px 4px 0;
}

/* 工作/项目卡片 */
.exp-card, .proj-card {
  background: #f9fafb;
  border: 1px solid #eceff1;
  border-radius: 8px;
  padding: 14px 16px;
  margin-bottom: 10px;
}
.exp-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 4px;
}
.exp-company {
  font-size: 14px;
  font-weight: 700;
  color: #263238;
}
.exp-date {
  font-size: 11.5px;
  color: #999;
  white-space: nowrap;
}
.exp-position {
  font-size: 12.5px;
  color: #607d8b;
  font-style: italic;
  margin-bottom: 6px;
}
.exp-desc {
  font-size: 12.5px;
  color: #555;
  line-height: 1.7;
}
.proj-achievement {
  margin-top: 6px;
  font-size: 12px;
  color: #2e7d32;
  background: #e8f5e9;
  border-radius: 4px;
  padding: 3px 10px;
  display: inline-block;
  font-weight: 500;
}
.ach-icon { margin-right: 4px; }
/* 校园/竞赛经历 */
.main-sec-title { font-size: 14px; font-weight: 700; color: #263238; margin-bottom: 12px; border-bottom: 2px solid #ff6b35; padding-bottom: 5px; letter-spacing: 0.5px; }
.main-item { margin-bottom: 12px; }
.main-item-header { display: flex; justify-content: space-between; margin-bottom: 2px; }
.main-item-title { font-size: 13.5px; font-weight: 700; color: #263238; }
.main-item-date { font-size: 11px; color: #999; white-space: nowrap; }
.main-item-sub { font-size: 12px; color: #607d8b; font-style: italic; margin-bottom: 3px; }
.main-item-desc { font-size: 12.5px; color: #555; line-height: 1.6; }
/* 荣誉奖励 */
.main-award-item {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 5px 0;
  border-bottom: 1px dashed #e0e0e0;
  font-size: 12px;
}
.main-award-item:last-child { border-bottom: none; }
.main-award-name { flex: 1; color: #333; font-size: 12.5px; }
.main-award-date { color: #999; font-size: 11px; }
.main-award-level { color: #ff6b35; font-size: 10.5px; background: #fff3e0; padding: 1px 7px; border-radius: 10px; font-weight: 500; }
/* 证书 */
.main-cert-list { display: flex; flex-wrap: wrap; gap: 6px; }
.main-cert-badge {
  background: #fff3e0;
  color: #e65100;
  border: 1px solid #ffcc80;
  border-radius: 12px;
  padding: 3px 10px;
  font-size: 11.5px;
  font-weight: 500;
}
</style>
