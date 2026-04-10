<template>
  <div class="module-list">
    <div class="module-list-header">编辑模块</div>
    <el-scrollbar>
      <div class="module-items">
        <div
          v-for="item in modules"
          :key="item.key"
          class="module-item"
          :class="{ active: modelValue === item.key }"
          @click="$emit('update:modelValue', item.key)"
        >
          <span class="module-icon">{{ item.icon }}</span>
          <span class="module-label">{{ item.label }}</span>
          <el-badge
            v-if="item.badge && item.badge > 0"
            :value="item.badge"
            :max="99"
            class="module-badge"
          />
        </div>
      </div>
    </el-scrollbar>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import type { ResumeConfig } from '@/types/resume'

const props = defineProps<{
  modelValue: string
  config: ResumeConfig
}>()

defineEmits<{
  (e: 'update:modelValue', key: string): void
}>()

const modules = computed(() => [
  {
    key: 'profile',
    icon: '\uD83D\uDC64',
    label: '个人信息',
    badge: 0
  },
  {
    key: 'educationList',
    icon: '\uD83C\uDF93',
    label: '教育背景',
    badge: props.config.educationList?.length || 0
  },
  {
    key: 'workExpList',
    icon: '\uD83D\uDCBC',
    label: '工作经历',
    badge: props.config.workExpList?.length || 0
  },
  {
    key: 'projectList',
    icon: '\uD83D\uDE80',
    label: '项目经历',
    badge: props.config.projectList?.length || 0
  },
  {
    key: 'skillList',
    icon: '\uD83D\uDD27',
    label: '专业技能',
    badge: props.config.skillList?.length || 0
  },
  {
    key: 'awardList',
    icon: '\uD83C\uDFC6',
    label: '荣誉奖项',
    badge: props.config.awardList?.length || 0
  },
  {
    key: 'workList',
    icon: '\uD83D\uDCC4',
    label: '个人作品',
    badge: props.config.workList?.length || 0
  },
  {
    key: 'aboutme',
    icon: '\uD83D\uDCA1',
    label: '自我介绍',
    badge: 0
  },
  {
    key: 'theme',
    icon: '\uD83C\uDFA8',
    label: '主题配色',
    badge: 0
  }
])
</script>

<style scoped>
.module-list {
  display: flex;
  flex-direction: column;
  height: 100%;
  background: #fff;
  border-right: 1px solid #e8e8e8;
}

.module-list-header {
  padding: 12px 16px 8px;
  font-size: 12px;
  font-weight: 600;
  color: #888;
  text-transform: uppercase;
  letter-spacing: 0.5px;
  border-bottom: 1px solid #f0f0f0;
  flex-shrink: 0;
}

.module-items {
  padding: 8px;
}

.module-item {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 9px 12px;
  border-radius: 6px;
  cursor: pointer;
  transition: background 0.15s, color 0.15s;
  color: #555;
  font-size: 13.5px;
  position: relative;
  user-select: none;
}

.module-item:hover {
  background: #f5f7ff;
  color: #409eff;
}

.module-item.active {
  background: #ecf5ff;
  color: #409eff;
  font-weight: 600;
}

.module-item.active .module-icon {
  transform: scale(1.15);
}

.module-icon {
  font-size: 15px;
  flex-shrink: 0;
  transition: transform 0.15s;
}

.module-label {
  flex: 1;
}

.module-badge {
  flex-shrink: 0;
}
</style>
