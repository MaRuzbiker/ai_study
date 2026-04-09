<template>
  <div class="resume-preview-wrap" :style="{ zoom: zoom || 1 }">
    <ResumeTemplateRenderer :templateId="data.templateId" :data="previewData" />
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import ResumeTemplateRenderer from './ResumeTemplateRenderer.vue'

const props = defineProps<{
  data: Record<string, any>
  zoom?: number
}>()

const previewData = computed(() => ({
  templateId: props.data.templateId,
  personal: props.data.personal || {},
  education: props.data.education || [],
  experience: props.data.experience || [],
  projects: props.data.projects || [],
  skills: props.data.skills || [],
  summary: props.data.summary || '',
}))
</script>

<style scoped>
.resume-preview-wrap {
  transform-origin: top left;
  display: flex;
  justify-content: center;
}
</style>
