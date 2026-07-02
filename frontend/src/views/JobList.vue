<template>
  <Layout>
    <div class="container">
      <h2 class="page-title">职位列表</h2>

      <div class="card filter-card">
        <el-form :inline="true" :model="filters" @submit.prevent>
          <el-form-item label="岗位">
            <el-select
              v-model="filters.position"
              placeholder="请选择岗位"
              clearable
              style="width: 180px"
            >
              <el-option
                v-for="pos in positions"
                :key="pos"
                :label="pos"
                :value="pos"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="城市">
            <el-select
              v-model="filters.city"
              placeholder="请选择城市"
              clearable
              style="width: 180px"
            >
              <el-option
                v-for="city in cities"
                :key="city"
                :label="city"
                :value="city"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="状态">
            <el-select
              v-model="filters.status"
              placeholder="请选择状态"
              clearable
              style="width: 180px"
            >
              <el-option label="招聘中" value="ACTIVE" />
              <el-option label="已暂停" value="PAUSED" />
              <el-option label="已关闭" value="CLOSED" />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="fetchJobs">
              <el-icon><Search /></el-icon> 搜索
            </el-button>
            <el-button @click="resetFilters">
              <el-icon><Refresh /></el-icon> 重置
            </el-button>
          </el-form-item>
        </el-form>
      </div>

      <div v-loading="loading" class="job-list">
        <div
          v-for="job in jobs"
          :key="job.id"
          class="job-card card"
          @click="goToDetail(job.id)"
        >
          <div class="job-header">
            <div class="job-title">
              <h3>{{ job.title }}</h3>
              <span :class="['status-tag', `status-${job.status.toLowerCase()}`]">
                {{ getStatusText(job.status) }}
              </span>
            </div>
            <div class="job-salary">{{ job.minSalary }}K-{{ job.maxSalary }}K</div>
          </div>
          <div class="job-meta">
            <span><el-icon><Location /></el-icon> {{ job.city }}</span>
            <span><el-icon><Briefcase /></el-icon> {{ job.position }}</span>
            <span><el-icon><OfficeBuilding /></el-icon> {{ job.department }}</span>
            <span>
              <el-icon><Clock /></el-icon>
              {{ job.experienceMin }}-{{ job.experienceMax }}年经验
            </span>
            <span><el-icon><Reading /></el-icon> {{ job.education }}</span>
          </div>
          <div class="job-skills">
            <el-tag
              v-for="skill in job.requiredSkills.slice(0, 5)"
              :key="skill"
              size="small"
              type="info"
              effect="plain"
              style="margin-right: 6px; margin-bottom: 6px"
            >
              {{ skill }}
            </el-tag>
          </div>
          <div class="job-footer">
            <div class="job-company">
              <el-icon color="#2563eb"><OfficeBuilding /></el-icon>
              <span>{{ job.employerName }}</span>
            </div>
            <div class="job-stats">
              <span><el-icon><View /></el-icon> {{ job.viewCount }}</span>
              <span><el-icon><UserFilled /></el-icon> {{ job.applyCount }}人投递</span>
            </div>
          </div>
        </div>

        <el-empty v-if="jobs.length === 0 && !loading" description="暂无符合条件的职位" />
      </div>
    </div>
  </Layout>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { jobApi } from '@/api'
import { useStatisticsStore } from '@/stores'
import Layout from '@/components/Layout.vue'

const router = useRouter()
const statisticsStore = useStatisticsStore()

const loading = ref(false)
const jobs = ref([])
const cities = ref([])
const positions = ref([])
const filters = ref({
  position: '',
  city: '',
  status: ''
})

const statusMap = {
  ACTIVE: '招聘中',
  PAUSED: '已暂停',
  CLOSED: '已关闭'
}

function getStatusText(status) {
  return statusMap[status] || status
}

async function fetchJobs() {
  loading.value = true
  try {
    const params = {}
    if (filters.value.position) params.position = filters.value.position
    if (filters.value.city) params.city = filters.value.city
    if (filters.value.status) params.status = filters.value.status
    jobs.value = await jobApi.getList(params)
    statisticsStore.fetchStatistics()
  } finally {
    loading.value = false
  }
}

async function fetchCitiesAndPositions() {
  try {
    const [citiesData, positionsData] = await Promise.all([
      jobApi.getCities(),
      jobApi.getPositions()
    ])
    cities.value = citiesData
    positions.value = positionsData
  } catch (error) {
    console.error('获取筛选选项失败', error)
  }
}

function resetFilters() {
  filters.value = {
    position: '',
    city: '',
    status: ''
  }
  fetchJobs()
}

function goToDetail(id) {
  router.push(`/jobs/${id}`)
}

onMounted(() => {
  fetchCitiesAndPositions()
  fetchJobs()
})
</script>

<style scoped>
.filter-card {
  margin-bottom: 20px;
}

.job-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.job-card {
  cursor: pointer;
  transition: all 0.3s;
}

.job-card:hover {
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.12);
  transform: translateY(-2px);
}

.job-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 12px;
}

.job-title {
  display: flex;
  align-items: center;
  gap: 12px;
}

.job-title h3 {
  font-size: 20px;
  font-weight: 600;
  color: #1f2937;
  margin: 0;
}

.job-salary {
  font-size: 20px;
  font-weight: 700;
  color: #f56c6c;
}

.job-meta {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
  margin-bottom: 16px;
  color: #6b7280;
  font-size: 14px;
}

.job-meta span {
  display: flex;
  align-items: center;
  gap: 4px;
}

.job-skills {
  margin-bottom: 16px;
}

.job-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-top: 16px;
  border-top: 1px solid #e5e7eb;
}

.job-company {
  display: flex;
  align-items: center;
  gap: 6px;
  color: #374151;
  font-weight: 500;
}

.job-stats {
  display: flex;
  gap: 20px;
  color: #9ca3af;
  font-size: 14px;
}

.job-stats span {
  display: flex;
  align-items: center;
  gap: 4px;
}
</style>
