import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    redirect: '/dashboard'
  },
  {
    path: '/dashboard',
    name: 'Dashboard',
    component: () => import('@/views/Dashboard.vue')
  },
  {
    path: '/jobs',
    name: 'JobList',
    component: () => import('@/views/JobList.vue')
  },
  {
    path: '/jobs/:id',
    name: 'JobDetail',
    component: () => import('@/views/JobDetail.vue')
  },
  {
    path: '/employer',
    name: 'Employer',
    component: () => import('@/views/Employer.vue')
  },
  {
    path: '/my-applications',
    name: 'MyApplications',
    component: () => import('@/views/MyApplications.vue')
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
