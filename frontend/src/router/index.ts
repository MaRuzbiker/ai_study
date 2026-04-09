// src/router/index.ts
import { createRouter, createWebHistory, type RouteRecordRaw } from 'vue-router'
import store from '@/store'

const Login = () => import('@/views/Login.vue')
const Register = () => import('@/views/Register.vue')
const Layout = () => import('@/views/Layout.vue')
const Dashboard = () => import('@/views/Dashboard.vue')
const TodayTasks = () => import('@/views/TodayTasks.vue')
const TaskList = () => import('@/views/TaskList.vue')
const RecordList = () => import('@/views/RecordList.vue')
const AiAssistant = () => import('@/views/AiAssistant.vue')
const Achievements = () => import('@/views/Achievements.vue')
const WordMemorize = () => import('@/views/WordMemorize.vue')
const WrongBook = () => import('@/views/WrongBook.vue')
const DeskMate = () => import('@/views/DeskMate.vue')
const ResumeCenter = () => import('@/views/ResumeCenter.vue')
const ResumeTemplate = () => import('@/views/ResumeTemplate.vue')
const ResumeEdit = () => import('@/views/ResumeEdit.vue')
const ResumeGenerate = () => import('@/views/ResumeGenerate.vue')

const routes: RouteRecordRaw[] = [
  {
    path: '/login',
    name: 'Login',
    component: Login,
    meta: { requiresAuth: false }
  },
  {
    path: '/register',
    name: 'Register',
    component: Register,
    meta: { requiresAuth: false }
  },
  {
    path: '/',
    component: Layout,
    meta: { requiresAuth: true },
    children: [
      {
        path: '',
        name: 'Dashboard',
        component: Dashboard,
        meta: { requiresAuth: true }
      },
      {
        path: 'today',
        name: 'TodayTasks',
        component: TodayTasks,
        meta: { requiresAuth: true }
      },
      {
        path: 'tasks',
        name: 'TaskList',
        component: TaskList,
        meta: { requiresAuth: true }
      },
      {
        path: 'records',
        name: 'RecordList',
        component: RecordList,
        meta: { requiresAuth: true }
      },
      {
        path: 'ai',
        name: 'AiAssistant',
        component: AiAssistant,
        meta: { requiresAuth: true }
      },
      {
        path: 'achievements',
        name: 'Achievements',
        component: Achievements,
        meta: { requiresAuth: true }
      },
      {
        path: 'words',
        name: 'WordMemorize',
        component: WordMemorize,
        meta: { requiresAuth: true }
      },
      {
        path: 'wrong-book',
        name: 'WrongBook',
        component: WrongBook,
        meta: { requiresAuth: true }
      },
      {
        path: 'deskmate',
        name: 'DeskMate',
        component: DeskMate,
        meta: { requiresAuth: true }
      },
      {
        path: 'resume',
        name: 'ResumeCenter',
        component: ResumeCenter,
        meta: { requiresAuth: true }
      },
      {
        path: 'resume/templates',
        name: 'ResumeTemplate',
        component: ResumeTemplate,
        meta: { requiresAuth: true }
      },
      {
        path: 'resume/edit/:id?',
        name: 'ResumeEdit',
        component: ResumeEdit,
        meta: { requiresAuth: true }
      },
      {
        path: 'resume/generate',
        name: 'ResumeGenerate',
        component: ResumeGenerate,
        meta: { requiresAuth: true }
      }
    ]
  },
  {
    path: '/:pathMatch(.*)*',
    redirect: '/'
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')

  if (to.meta.requiresAuth && !token) {
    // 需要登录但未登录，跳转到登录页
    next('/login')
  } else if ((to.path === '/login' || to.path === '/register') && token) {
    // 已登录用户访问登录/注册页，跳转到首页
    next('/')
  } else {
    next()
  }
})

export default router