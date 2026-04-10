<template>
  <el-popover
    :visible="visible"
    placement="bottom"
    :width="280"
    trigger="click"
    @update:visible="$emit('update:visible', $event)"
  >
    <template #reference>
      <el-button :icon="Brush" circle />
    </template>
    
    <div class="theme-picker">
      <!-- 当前颜色预览 -->
      <div class="theme-current">
        <span class="theme-label">当前主题色</span>
        <div class="theme-preview">
          <span class="theme-chip" :style="{ backgroundColor: modelValue }" />
          <code class="theme-code">{{ modelValue }}</code>
        </div>
      </div>
      
      <!-- 颜色选择器 -->
      <div class="theme-row">
        <span class="theme-label">自定义颜色</span>
        <el-color-picker
          :model-value="modelValue"
          @update:model-value="$emit('update:modelValue', $event)"
          :predefine="PRESET_COLORS"
          size="small"
        />
      </div>
      
      <!-- 预设颜色 -->
      <div class="theme-presets">
        <span class="theme-label">预设主题</span>
        <div class="theme-swatches">
          <button
            v-for="color in PRESET_COLORS"
            :key="color"
            type="button"
            class="theme-swatch"
            :class="{ 'is-active': modelValue === color }"
            :style="{ backgroundColor: color }"
            @click="$emit('update:modelValue', color)"
          />
        </div>
      </div>
      
      <!-- 重置按钮 -->
      <el-button size="small" :disabled="modelValue === DEFAULT_COLOR" @click="$emit('update:modelValue', DEFAULT_COLOR)">
        恢复默认
      </el-button>
    </div>
  </el-popover>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { Brush } from '@element-plus/icons-vue'

const props = defineProps<{
  modelValue: string
  visible?: boolean
}>()

defineEmits<{
  'update:modelValue': [value: string]
  'update:visible': [value: boolean]
}>()

const PRESET_COLORS = [
  '#2f5785', // 经典蓝
  '#1f7a8c', // 青色
  '#198754', // 绿色
  '#c86b1f', // 橙色
  '#8f3d5e', // 玫红
  '#4f46e5', // 靛蓝
  '#374151', // 深灰
  '#b91c1c', // 红色
]

const DEFAULT_COLOR = '#2f5785'
</script>

<style scoped>
.theme-picker {
  display: flex;
  flex-direction: column;
  gap: 14px;
  padding: 8px 4px;
}

.theme-current {
  padding: 12px 14px;
  border-radius: 8px;
  background: #f5f7fa;
  border: 1px solid #e4e7ed;
}

.theme-preview {
  margin-top: 8px;
  display: flex;
  align-items: center;
  gap: 10px;
}

.theme-chip {
  width: 18px;
  height: 18px;
  border-radius: 4px;
  border: 1px solid #dcdfe6;
  flex-shrink: 0;
}

.theme-code {
  font-size: 12px;
  color: #909399;
  background: #fff;
  padding: 4px 8px;
  border-radius: 4px;
  border: 1px solid #e4e7ed;
}

.theme-row {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 16px;
}

.theme-label {
  font-size: 14px;
  color: #303133;
  font-weight: 500;
}

.theme-presets {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.theme-swatches {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 8px;
}

.theme-swatch {
  position: relative;
  width: 100%;
  height: 32px;
  border-radius: 6px;
  border: 1px solid #dcdfe6;
  cursor: pointer;
  transition: transform 0.15s ease, box-shadow 0.15s ease;
}

.theme-swatch:hover {
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.theme-swatch.is-active {
  border-color: #409eff;
  box-shadow: 0 0 0 2px rgba(64, 158, 255, 0.2);
}

.theme-swatch.is-active::after {
  content: '';
  position: absolute;
  top: 50%;
  left: 50%;
  width: 8px;
  height: 8px;
  border-radius: 2px;
  transform: translate(-50%, -50%);
  background: #fff;
  box-shadow: 0 0 0 1px #409eff;
}
</style>
