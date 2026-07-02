<template>
  <Layout>
    <div class="container">
      <h2 class="page-title">我的投递</h2>

      <div class="card" v-loading="loading">
        <div class="filter-bar">
          <el-radio-group v-model="statusFilter" @change="filterApplications">
            <el-radio-button value="ALL">全部</el-radio-button>
            <el-radio-button value="PENDING">待查看</el-radio-button>
            <el-radio-button value="COMMUNICATING">沟通中</el-radio-button>
            <el-radio-button value="UNSUITABLE">不合适</el-radio-button>
          </el-radio-group>
        </div>

        <el-table :data="filteredApplications" style="width: 100%">
          <el-table-column prop="jobTitle" label="应聘职位" width="200" />
          <el-table-column label="状态" width="120">
            <template #default="{ row }">
              <span :class="['status-tag', `status-${row.status.toLowerCase()}`]">
                {{ getStatusText(row.status) }}
              </span>
            </template>
          </el-table-column>
          <el-table-column label="工作年限" width="100">
            <template #default="{ row }">{{ row.workYears }}年</template>
          </el-table-column>
          <el-table-column label="技能标签">
            <template #default="{ row }">
              <el-tag
                v-for="skill in row.skills.slice(0, 3)"
                :key="skill"
                size="small"
                type="info"
                style="margin-right: 4px"
              >
                {{ skill }}
              </el-tag>
              <span v-if="row.skills.length > 3" style="color: #9ca3af; font-size: 12px">
                等{{ row.skills.length }}项
              </span>
            </template>
          </el-table-column>
          <el-table-column label="投递时间" width="180">
            <template #default="{ row }">{{ formatDate(row.appliedAt) }}</template>
          </el-table-column>
          <el-table-column label="操作" width="120">
            <template #default="{ row }">
              <el-button type="primary" size="small" @click="viewDetail(row)">查看详情</el-button>
            </template>
          </el-table-column>
        </el-table>

        <el-empty v-if="filteredApplications.length === 0 && !loading" description="暂无投递记录" />
      </div>

      <el-dialog
        v-model="showDetailDialog"
        title="投递详情"
        width="800px"
      >
        <div v-if="currentApplication">
          <el-descriptions :column="2" border>
            <el-descriptions-item label="应聘职位">{{ currentApplication.jobTitle }}</el-descriptions-item>
            <el-descriptions-item label="投递状态">
              <span :class="['status-tag', `status-${currentApplication.status.toLowerCase()}`]">
                {{ getStatusText(currentApplication.status) }}
              </span>
            </el-descriptions-item>
            <el-descriptions-item label="工作年限">{{ currentApplication.workYears }}年</el-descriptions-item>
            <el-descriptions-item label="投递时间">{{ formatDate(currentApplication.appliedAt) }}</el-descriptions-item>
            <el-descriptions-item label="手机号">{{ currentApplication.applicantPhone }}</el-descriptions-item>
            <el-descriptions-item label="邮箱">{{ currentApplication.applicantEmail }}</el-descriptions-item>
            <el-descriptions-item label="技能标签" :span="2">
              <el-tag
                v-for="skill in currentApplication.skills"
                :key="skill"
                size="small"
                type="info"
                style="margin-right: 6px"
              >
                {{ skill }}
              </el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="简历摘要" :span="2">
              <div style="white-space: pre-wrap; line-height: 1.6">{{ currentApplication.resumeSummary }}</div>
            </el-descriptions-item>
          </el-descriptions>

          <MessagePanel
            v-if="currentApplication.id"
            :application-id="currentApplication.id"
            style="margin-top: 24px"
          />
        </div>
      </el-dialog>
    </div>
  </Layout>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import dayjs from 'dayjs'
import { applicationApi } from '@/api'
import { useUserStore } from '@/stores'
import Layout from '@/components/Layout.vue'
import MessagePanel from '@/components/MessagePanel.vue'

const userStore = useUserStore()

const loading = ref(false)
const allApplications = ref([])
const statusFilter = ref('ALL')
const showDetailDialog = ref(false)
const currentApplication = ref(null)

const statusMap = {
  PENDING: '待查看',
  COMMUNICATING: '沟通中',
  UNSUITABLE: '不合适',
  ACCEPTED: '已录用'
}

const filteredApplications = computed(() => {
  if (statusFilter.value === 'ALL') {
    return allApplications.value
  }
  return allApplications.value.filter(app => app.status === statusFilter.value)
})

function getStatusText(status) {
  return statusMap[status] || status
}

function formatDate(date) {
  return dayjs(date).format('YYYY-MM-DD HH:mm')
}

function filterApplications() {
}

async function fetchMyApplications() {
  loading.value = true
  try {
    allApplications.value = await applicationApi.getByApplicant(userStore.name)
  } catch (error) {
    console.error('获取投递记录失败', error)
    ElMessage.error('获取投递记录失败')
  } finally {
    loading.value = false
  }
}

function viewDetail(app) {
  currentApplication.value = app
  showDetailDialog.value = true
}

onMounted(() => {
  fetchMyApplications()
})
</script>

<style scoped>
.filter-bar {
  margin-bottom: 16px;
}
</style>
