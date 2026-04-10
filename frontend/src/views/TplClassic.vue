<template>
  <div class="template2-resume resume-content">
    <div class="basic-info">
      <div class="profile">
        <div class="profile-info">
          <div v-if="data.personal?.name" class="name">{{ data.personal.name }}</div>
          <div class="profile-list">
            <div v-if="data.personal?.phone" class="mobile">
              <svg width="12" height="12" viewBox="0 0 24 24" fill="none" :stroke="themeColor" stroke-width="2">
                <path d="M22 16.92v3a2 2 0 0 1-2.18 2 19.79 19.79 0 0 1-8.63-3.07A19.5 19.5 0 0 1 4.69 12.2 19.79 19.79 0 0 1 1.61 3.61 2 2 0 0 1 3.58 1h3a2 2 0 0 1 2 1.72c.127.96.361 1.903.7 2.81a2 2 0 0 1-.45 2.11L7.91 8.55A16 16 0 0 0 15 15.09l.92-.92a2 2 0 0 1 2.11-.45c.907.339 1.85.573 2.81.7A2 2 0 0 1 22 16.92z"/>
              </svg>
              {{ data.personal.phone }}
            </div>
            <div v-if="data.personal?.email" class="email">
              <svg width="12" height="12" viewBox="0 0 24 24" fill="none" :stroke="themeColor" stroke-width="2">
                <path d="M4 4h16c1.1 0 2 .9 2 2v12c0 1.1-.9 2-2 2H4c-1.1 0-2-.9-2-2V6c0-1.1.9-2 2-2z"/>
                <polyline points="22,6 12,13 2,6"/>
              </svg>
              {{ data.personal.email }}
            </div>
            <div v-if="data.personal?.github" class="github">
              <svg width="12" height="12" viewBox="0 0 24 24" fill="none" :stroke="themeColor" stroke-width="2">
                <path d="M9 19c-5 1.5-5-2.5-7-3m14 6v-3.87a3.37 3.37 0 0 0-.94-2.61c3.14-.35 6.44-1.54 6.44-7A5.44 5.44 0 0 0 20 4.77 5.07 5.07 0 0 0 19.91 1S18.73.65 16 2.48a13.38 13.38 0 0 0-7 0C6.27.65 5.09 1 5.09 1A5.07 5.07 0 0 0 5 4.77a5.44 5.44 0 0 0-1.5 3.78c0 5.42 3.3 6.61 6.44 7A3.37 3.37 0 0 0 9 18.13V22"/>
              </svg>
              {{ data.personal.github }}
            </div>
            <div v-if="data.personal?.targetPosition" class="expect-job">
              <svg width="12" height="12" viewBox="0 0 24 24" fill="none" :stroke="themeColor" stroke-width="2">
                <path d="M20.84 4.61a5.5 5.5 0 0 0-7.78 0L12 5.67l-1.06-1.06a5.5 5.5 0 0 0-7.78 7.78l1.06 1.06L12 21.23l7.78-7.78 1.06-1.06a5.5 5.5 0 0 0 0-7.78z"/>
              </svg>
              职位：{{ data.personal.targetPosition }}
            </div>
          </div>
        </div>
        <div v-if="!data.avatar?.hidden" class="avatar-section">
          <div class="avatar" :class="data.avatar?.shape || 'circle'">
            <img v-if="data.avatar?.src" :src="data.avatar.src" alt="avatar" />
            <span v-else class="avatar-placeholder">{{ getInitial(data.personal?.name) }}</span>
          </div>
        </div>
      </div>

      <!-- 教育背景 -->
      <div v-if="data.education && data.education.length" class="section section-education">
        <div class="section-title">
          <div class="title">教育背景</div>
          <div class="title-addon"></div>
        </div>
        <div class="section-content">
          <div v-for="(edu, idx) in data.education" :key="idx" class="education-item">
            <div class="section-info">
              <span class="info-name">{{ edu.school }}</span>
              <span class="info-time">{{ edu.startDate }} — {{ edu.endDate || '至今' }}</span>
            </div>
            <div class="sub-info">{{ edu.degree }} · {{ edu.major }}</div>
          </div>
        </div>
      </div>

      <!-- 自我介绍 -->
      <div v-if="data.summary" class="section section-aboutme">
        <div class="section-title">
          <div class="title">自我介绍</div>
          <div class="title-addon"></div>
        </div>
        <div class="section-content">
          <div class="aboutme-item">{{ data.summary }}</div>
        </div>
      </div>

      <!-- 专业技能 -->
      <div v-if="data.skills && data.skills.length" class="section section-skill">
        <div class="section-title">
          <div class="title">专业技能</div>
          <div class="title-addon"></div>
        </div>
        <div class="section-content">
          <div v-for="(skill, idx) in data.skills" :key="idx" class="skill-item">
            <span class="skill-name">{{ skill }}</span>
          </div>
        </div>
      </div>
    </div>

    <div class="main-info">
      <!-- 工作经历 -->
      <div v-if="data.experience && data.experience.length" class="section section-work-exp">
        <div class="section-title">
          <div class="title">工作经历</div>
          <div class="title-addon"></div>
        </div>
        <div class="section-content">
          <div v-for="(exp, idx) in data.experience" :key="idx" class="section-item">
            <div class="section-info">
              <span class="company">{{ exp.company }}</span>
              <span class="info-time">{{ exp.startDate }} — {{ exp.endDate || '至今' }}</span>
            </div>
            <div class="sub-info">{{ exp.position }}</div>
            <div v-if="exp.description" class="work-description">{{ exp.description }}</div>
          </div>
        </div>
      </div>

      <!-- 项目经历 -->
      <div v-if="data.projects && data.projects.length" class="section section-project">
        <div class="section-title">
          <div class="title">项目经历</div>
          <div class="title-addon"></div>
        </div>
        <div class="section-content">
          <div v-for="(proj, idx) in data.projects" :key="idx" class="section-item">
            <div class="section-info">
              <span class="project-name">{{ proj.name }}</span>
              <span class="info-time">{{ proj.startDate }} — {{ proj.endDate || '至今' }}</span>
            </div>
            <div v-if="proj.role" class="sub-info">{{ proj.role }}</div>
            <div v-if="proj.description" class="project-content">{{ proj.description }}</div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'

const props = defineProps<{ 
  data: Record<string, any>
  theme?: string
}>()

const themeColor = computed(() => props.theme || '#1a73e8')

function getInitial(name?: string): string {
  if (!name) return '姓'
  return name.charAt(0)
}
</script>

<style scoped>
.template2-resume {
  width: 794px;
  min-height: 942px;
  margin-bottom: 60px;
  box-shadow: 0px 2px 4px 1px rgba(0, 0, 0, 0.15);
}

@media (max-width: 794px) {
  .template2-resume {
    width: 100%;
  }
}

@media print {
  @page {
    size: A4;
  }

  .template2-resume {
    width: 100%;
    box-shadow: none;
  }
}

.template2-resume {
  .basic-info {
    padding: 18px 24px 12px 24px;
  }

  .main-info {
    padding: 0 24px 24px;
    background: #fff;
  }

  .section-title {
    font-size: 16px;
    line-height: 18px;
    margin-bottom: 8px;
    letter-spacing: 0;
    position: relative;

    display: flex;
    .title {
      width: auto;
    }
    .title-addon {
      flex: 1;
      position: relative;
    }

    .title-addon::after {
      content: ' ';
      position: absolute;
      right: 0;
      top: 50%;
      left: 16px;
      height: 1px;
      transform: translateY(-1px);
      background-color: currentColor;
      opacity: 0.54;
    }
  }

  .basic-info .section {
    border-radius: 0;
  }

  .section {
    &:not(:first-of-type) {
      margin-top: 12px;
    }

    .section-info {
      font-size: 14px;
      line-height: 16px;
      color: rgba(0, 0, 0, 0.85);
      margin-bottom: 4px;

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
    margin-top: 12px;
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
    display: flex;
    justify-content: space-between;
    align-items: center;
    flex-wrap: wrap-reverse;

    margin-bottom: 12px;

    > div:first-of-type {
      flex: 1;
    }
    > div:nth-of-type(2) {
      width: 150px;
    }

    .profile-list {
      display: flex;
      flex-wrap: wrap;
      margin-left: 4px;

      > div {
        flex: 220px 1;
        display: flex;
        align-items: center;
        margin-right: 12px;
        margin-bottom: 4px;
      }

      svg {
        margin-right: 8px;
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

  .main-info section {
    margin-bottom: 16px;

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
      margin-top: 10px;
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
