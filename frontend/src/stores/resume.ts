import { defineStore } from 'pinia'
import { ref } from 'vue'
import type { ResumeConfig } from '@/types/resume'
import { getResume, saveResume, getMyResumes } from '@/api/resume'

export const useResumeStore = defineStore('resume', () => {
  // 初始化默认数据结构，避免 undefined 错误
  const config = ref<ResumeConfig>({
    profile: { name: '' },
    aboutme: { aboutme_desc: '' },
    educationList: [],
    workExpList: [],
    projectList: [],
    skillList: [],
    awardList: [],
    workList: [],
    templateId: 1,
    sectionOrder: ['summary', 'education', 'experience', 'projects', 'awards', 'certificates', 'skills'],
  })
  const resumes = ref<ResumeConfig[]>([])
  const selectedModule = ref<string>('profile')
  const loading = ref(false)
  const saving = ref(false)

  // 获取简历列表
  const fetchResumes = async () => {
    loading.value = true
    try {
      const res = await getMyResumes()
      if (res.code === 0) {
        resumes.value = res.data || []
      }
    } finally {
      loading.value = false
    }
  }

  // 加载单个简历
  const loadResume = async (id: number) => {
    loading.value = true
    try {
      const res = await getResume(id)
      if (res.code === 0 && res.data) {
        config.value = typeof res.data.content === 'string'
          ? JSON.parse(res.data.content)
          : res.data.content
        config.value.id = res.data.id
        config.value.templateId = res.data.templateId
        config.value.title = res.data.title
        config.value.status = res.data.status
      }
    } finally {
      loading.value = false
    }
  }

  // 保存简历
  const saveResumeConfig = async () => {
    saving.value = true
    try {
      // 确保 title 有默认值
      const title = config.value.title?.trim() || '未命名简历'
      const res = await saveResume({
        id: config.value.id,
        templateId: config.value.templateId,
        title: title,
        status: config.value.status || 'DRAFT',
        content: JSON.stringify(config.value)
      })
      if (res.code === 0) {
        config.value.id = res.data?.id ?? config.value.id
        return true
      }
      return false
    } finally {
      saving.value = false
    }
  }

  // 更新部分配置
  const updateConfig = (partial: Partial<ResumeConfig>) => {
    config.value = { ...config.value, ...partial }
  }

  // 更新主题色
  const updateTheme = (color: string) => {
    config.value.theme = {
      color,
      tagColor: color
    }
  }

  // 选择模块
  const selectModule = (moduleKey: string) => {
    selectedModule.value = moduleKey
  }

  return {
    config,
    resumes,
    selectedModule,
    loading,
    saving,
    fetchResumes,
    loadResume,
    saveResumeConfig,
    updateConfig,
    updateTheme,
    selectModule
  }
})
