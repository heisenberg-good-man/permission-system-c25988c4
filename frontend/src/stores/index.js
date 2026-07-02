import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { statisticsApi } from '@/api'

export const useUserStore = defineStore('user', () => {
  const role = ref(localStorage.getItem('userRole') || 'APPLICANT')
  const name = ref(localStorage.getItem('userName') || '求职者')

  const isEmployer = computed(() => role.value === 'EMPLOYER')
  const isApplicant = computed(() => role.value === 'APPLICANT')

  function switchRole(newRole, newName) {
    role.value = newRole
    name.value = newName
    localStorage.setItem('userRole', newRole)
    localStorage.setItem('userName', newName)
  }

  return { role, name, isEmployer, isApplicant, switchRole }
})

export const useStatisticsStore = defineStore('statistics', () => {
  const stats = ref({
    activeJobCount: 0,
    todayApplicationCount: 0,
    communicatingCount: 0,
    pendingCount: 0
  })

  async function fetchStatistics() {
    try {
      const data = await statisticsApi.getStatistics()
      stats.value = data
    } catch (error) {
      console.error('获取统计数据失败', error)
    }
  }

  return { stats, fetchStatistics }
})
