import { get, post, del } from './index'

// 获取模板列表
export const getResumeTemplates = () => get('/resume/templates')

// 获取单个模板
export const getResumeTemplate = (id: number) => get(`/resume/templates/${id}`)

// 保存简历
export const saveResume = (data: any) => post('/resume/save', data)

// 获取我的简历列表
export const getMyResumes = () => get('/resume/my')

// 获取简历详情
export const getResume = (id: number) => get(`/resume/${id}`)

// 删除简历
export const deleteResume = (id: number) => del(`/resume/${id}`)

// AI生成简历
export const generateResume = (data: any) => post('/resume/generate', data)
