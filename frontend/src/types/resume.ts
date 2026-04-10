/** 简历完整配置 */
export interface ResumeConfig {
  id?: number
  userId?: number
  templateId?: number
  title?: string
  status?: 'DRAFT' | 'VISIBLE'

  /** 头像配置 */
  avatar?: {
    src?: string
    shape?: 'circle' | 'square'
    size?: 'small' | 'default' | 'large'
    hidden?: boolean
  }

  /** 个人信息 */
  profile?: {
    name: string
    mobile?: string
    email?: string
    github?: string
    zhihu?: string
    workExpYear?: string
    workPlace?: string
    positionTitle?: string
  }

  /** 教育背景 */
  educationList?: Array<{
    edu_time?: [string | null, string | null]
    school?: string
    major?: string
    academic_degree?: string
  }>

  /** 工作经历 */
  workExpList?: Array<{
    work_time?: [string | null, string | null]
    company_name?: string
    department_name?: string
    work_desc?: string
  }>

  /** 项目经历 */
  projectList?: Array<{
    project_time?: string
    project_name?: string
    project_role?: string
    project_desc?: string
    project_content?: string
  }>

  /** 专业技能 */
  skillList?: Array<{
    skill_name?: string
    skill_level?: number
    skill_desc?: string
  }>

  /** 荣誉奖项 */
  awardList?: Array<{
    award_time?: string
    award_info?: string
  }>

  /** 个人作品 */
  workList?: Array<{
    work_name?: string
    work_desc?: string
    visit_link?: string
  }>

  /** 自我介绍 */
  aboutme?: {
    aboutme_desc?: string
  }

  /** 主题配置 */
  theme?: ThemeConfig

  /** 自定义模块名称 */
  titleNameMap?: Record<string, string>

  /** 板块显示顺序 */
  sectionOrder?: string[]
}

export interface ThemeConfig {
  color?: string
  tagColor?: string
}

/** 预设主题色 */
export const PRESET_THEME_COLORS = [
  '#2f5785', // 经典蓝
  '#1f7a8c', // 青色
  '#198754', // 绿色
  '#c86b1f', // 橙色
  '#8f3d5e', // 玫红
  '#4f46e5', // 靛蓝
  '#374151', // 深灰
  '#b91c1c', // 红色
]

export const DEFAULT_THEME: ThemeConfig = {
  color: '#2f5785',
  tagColor: '#2f5785',
}
