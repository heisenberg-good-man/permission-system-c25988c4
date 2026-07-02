<template>
  <Layout>
    <div class="container">
      <h2 class="page-title">招聘管理</h2>

      <el-tabs v-model="activeTab">
        <el-tab-pane label="发布新职位" name="publish">
          <div class="card">
            <el-form
              ref="jobFormRef"
              :model="jobForm"
              :rules="jobRules"
              label-width="120px"
              style="max-width: 800px"
            >
              <el-row :gutter="20">
                <el-col :span="12">
                  <el-form-item label="职位名称" prop="title">
                    <el-input v-model="jobForm.title" placeholder="如：高级Java开发工程师" />
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="所属部门" prop="department">
                    <el-input v-model="jobForm.department" placeholder="如：技术部" />
                  </el-form-item>
                </el-col>
              </el-row>

              <el-row :gutter="20">
                <el-col :span="12">
                  <el-form-item label="岗位类型" prop="position">
                    <el-select v-model="jobForm.position" placeholder="请选择或输入岗位类型" style="width: 100%">
                      <el-option label="后端开发" value="后端开发" />
                      <el-option label="前端开发" value="前端开发" />
                      <el-option label="数据分析" value="数据分析" />
                      <el-option label="产品设计" value="产品设计" />
                      <el-option label="项目管理" value="项目管理" />
                    </el-select>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="工作城市" prop="city">
                    <el-select v-model="jobForm.city" placeholder="请选择工作城市" style="width: 100%">
                      <el-option label="北京" value="北京" />
                      <el-option label="上海" value="上海" />
                      <el-option label="深圳" value="深圳" />
                      <el-option label="杭州" value="杭州" />
                      <el-option label="广州" value="广州" />
                    </el-select>
                  </el-form-item>
                </el-col>
              </el-row>

              <el-row :gutter="20">
                <el-col :span="12">
                  <el-form-item label="薪资范围" required>
                    <el-input-group>
                      <el-input v-model.number="jobForm.minSalary" placeholder="最低" />
                      <el-input-group-text>K -</el-input-group-text>
                      <el-input v-model.number="jobForm.maxSalary" placeholder="最高" />
                      <el-input-group-text>K</el-input-group-text>
                    </el-input-group>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="工作年限" required>
                    <el-input-group>
                      <el-input v-model.number="jobForm.experienceMin" placeholder="最低" />
                      <el-input-group-text>年 -</el-input-group-text>
                      <el-input v-model.number="jobForm.experienceMax" placeholder="最高" />
                      <el-input-group-text>年</el-input-group-text>
                    </el-input-group>
                  </el-form-item>
                </el-col>
              </el-row>

              <el-row :gutter="20">
                <el-col :span="12">
                  <el-form-item label="学历要求" prop="education">
                    <el-select v-model="jobForm.education" placeholder="请选择学历要求" style="width: 100%">
                      <el-option label="大专" value="大专" />
                      <el-option label="本科" value="本科" />
                      <el-option label="硕士" value="硕士" />
                      <el-option label="博士" value="博士" />
                    </el-select>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="招聘方名称" prop="employerName">
                    <el-input v-model="jobForm.employerName" :disabled="true" />
                  </el-form-item>
                </el-col>
              </el-row>

              <el-form-item label="技能标签" prop="requiredSkills">
                <el-select
                  v-model="jobForm.requiredSkills"
                  multiple
                  filterable
                  allow-create
                  placeholder="请输入或选择技能标签"
                  style="width: 100%"
                >
                  <el-option label="Java" value="Java" />
                  <el-option label="Spring Boot" value="Spring Boot" />
                  <el-option label="Vue" value="Vue" />
                  <el-option label="React" value="React" />
                  <el-option label="Python" value="Python" />
                  <el-option label="MySQL" value="MySQL" />
                  <el-option label="Redis" value="Redis" />
                  <el-option label="TypeScript" value="TypeScript" />
                </el-select>
              </el-form-item>

              <el-form-item label="职位描述" prop="description">
                <el-input
                  v-model="jobForm.description"
                  type="textarea"
                  :rows="3"
                  placeholder="请描述职位的整体情况..."
                />
              </el-form-item>

              <el-form-item label="岗位职责" prop="responsibilities">
                <el-input
                  v-model="jobForm.responsibilities"
                  type="textarea"
                  :rows="4"
                  placeholder="请描述具体的岗位职责..."
                />
              </el-form-item>

              <el-form-item>
                <el-button type="primary" size="large" :loading="submitting" @click="publishJob">
                  <el-icon><Promotion /></el-icon> 发布职位
                </el-button>
                <el-button size="large" @click="resetForm">重置</el-button>
              </el-form-item>
            </el-form>
          </div>
        </el-tab-pane>

        <el-tab-pane label="已发布职位" name="jobs">
          <div class="card" v-loading="loadingJobs">
            <el-table :data="myJobs" style="width: 100%">
              <el-table-column prop="title" label="职位名称" width="200" />
              <el-table-column prop="city" label="城市" width="100" />
              <el-table-column label="薪资" width="140">
                <template #default="{ row }">{{ row.minSalary }}K-{{ row.maxSalary }}K</template>
              </el-table-column>
              <el-table-column label="状态" width="100">
                <template #default="{ row }">
                  <span :class="['status-tag', `status-${row.status.toLowerCase()}`]">
                    {{ getStatusText(row.status) }}
                  </span>
                </template>
              </el-table-column>
              <el-table-column prop="viewCount" label="浏览量" width="80" />
              <el-table-column prop="applyCount" label="投递数" width="80" />
              <el-table-column label="发布时间" width="180">
                <template #default="{ row }">{{ formatDate(row.createdAt) }}</template>
              </el-table-column>
              <el-table-column label="操作" width="180">
                <template #default="{ row }">
                  <el-button type="primary" size="small" @click="viewJobDetail(row.id)">查看</el-button>
                  <el-dropdown @command="(status) => updateJobStatus(row.id, status)">
                    <el-button type="info" size="small">
                      状态 <el-icon class="el-icon--right"><ArrowDown /></el-icon>
                    </el-button>
                    <template #dropdown>
                      <el-dropdown-menu>
                        <el-dropdown-item command="ACTIVE">招聘中</el-dropdown-item>
                        <el-dropdown-item command="PAUSED">已暂停</el-dropdown-item>
                        <el-dropdown-item command="CLOSED">已关闭</el-dropdown-item>
                      </el-dropdown-menu>
                    </template>
                  </el-dropdown>
                </template>
              </el-table-column>
            </el-table>
            <el-empty v-if="myJobs.length === 0 && !loadingJobs" description="暂无发布的职位" />
          </div>
        </el-tab-pane>

        <el-tab-pane label="收到的投递" name="applications">
          <div class="card" v-loading="loadingApplications">
            <div class="filter-bar">
              <el-radio-group v-model="statusFilter" @change="filterApplications">
                <el-radio-button value="ALL">全部</el-radio-button>
                <el-radio-button value="PENDING">待查看</el-radio-button>
                <el-radio-button value="COMMUNICATING">沟通中</el-radio-button>
                <el-radio-button value="UNSUITABLE">不合适</el-radio-button>
              </el-radio-group>
            </div>
            <el-table :data="filteredApplications" style="width: 100%">
              <el-table-column prop="jobTitle" label="应聘职位" width="180" />
              <el-table-column prop="applicantName" label="姓名" width="100" />
              <el-table-column label="工作年限" width="100">
                <template #default="{ row }">{{ row.workYears }}年</template>
              </el-table-column>
              <el-table-column label="状态" width="120">
                <template #default="{ row }">
                  <span :class="['status-tag', `status-${row.status.toLowerCase()}`]">
                    {{ getAppStatusText(row.status) }}
                  </span>
                </template>
              </el-table-column>
              <el-table-column label="投递时间" width="180">
                <template #default="{ row }">{{ formatDate(row.appliedAt) }}</template>
              </el-table-column>
              <el-table-column label="操作" width="150">
                <template #default="{ row }">
                  <el-button type="primary" size="small" @click="viewApplication(row)">查看详情</el-button>
                </template>
              </el-table-column>
            </el-table>
            <el-empty v-if="filteredApplications.length === 0 && !loadingApplications" description="暂无投递记录" />
          </div>
        </el-tab-pane>
      </el-tabs>

      <el-dialog
        v-model="showAppDetailDialog"
        title="投递详情"
        width="800px"
      >
        <div v-if="currentApplication">
          <el-descriptions :column="2" border>
            <el-descriptions-item label="姓名">{{ currentApplication.applicantName }}</el-descriptions-item>
            <el-descriptions-item label="工作年限">{{ currentApplication.workYears }}年</el-descriptions-item>
            <el-descriptions-item label="手机号">{{ currentApplication.applicantPhone }}</el-descriptions-item>
            <el-descriptions-item label="邮箱">{{ currentApplication.applicantEmail }}</el-descriptions-item>
            <el-descriptions-item label="投递状态">
              <span :class="['status-tag', `status-${currentApplication.status.toLowerCase()}`]">
                {{ getAppStatusText(currentApplication.status) }}
              </span>
            </el-descriptions-item>
            <el-descriptions-item label="投递时间">{{ formatDate(currentApplication.appliedAt) }}</el-descriptions-item>
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

          <div style="margin-top: 20px">
            <h4 style="margin-bottom: 12px">修改状态</h4>
            <el-radio-group v-model="newStatus" style="margin-bottom: 12px">
              <el-radio-button value="PENDING">待查看</el-radio-button>
              <el-radio-button value="COMMUNICATING">沟通中</el-radio-button>
              <el-radio-button value="UNSUITABLE">不合适</el-radio-button>
              <el-radio-button value="ACCEPTED">已录用</el-radio-button>
            </el-radio-group>
            <el-button
              type="primary"
              size="small"
              :disabled="newStatus === currentApplication.status"
              @click="updateApplicationStatus"
            >
              更新状态
            </el-button>
          </div>

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
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import dayjs from 'dayjs'
import { jobApi, applicationApi } from '@/api'
import { useUserStore, useStatisticsStore } from '@/stores'
import Layout from '@/components/Layout.vue'
import MessagePanel from '@/components/MessagePanel.vue'

const router = useRouter()
const userStore = useUserStore()
const statisticsStore = useStatisticsStore()

const activeTab = ref('publish')
const loadingJobs = ref(false)
const loadingApplications = ref(false)
const submitting = ref(false)
const myJobs = ref([])
const allApplications = ref([])
const statusFilter = ref('ALL')
const showAppDetailDialog = ref(false)
const currentApplication = ref(null)
const newStatus = ref('')

const jobFormRef = ref(null)
const jobForm = ref({
  title: '',
  department: '',
  position: '',
  city: '',
  minSalary: null,
  maxSalary: null,
  experienceMin: null,
  experienceMax: null,
  education: '',
  requiredSkills: [],
  description: '',
  responsibilities: '',
  employerName: userStore.name
})

const jobRules = {
  title: [{ required: true, message: '请输入职位名称', trigger: 'blur' }],
  department: [{ required: true, message: '请输入所属部门', trigger: 'blur' }],
  position: [{ required: true, message: '请选择岗位类型', trigger: 'change' }],
  city: [{ required: true, message: '请选择工作城市', trigger: 'change' }],
  education: [{ required: true, message: '请选择学历要求', trigger: 'change' }],
  employerName: [{ required: true, message: '请输入招聘方名称', trigger: 'blur' }],
  requiredSkills: [{ required: true, message: '请至少选择一个技能标签', trigger: 'change' }],
  description: [{ required: true, message: '请输入职位描述', trigger: 'blur' }],
  responsibilities: [{ required: true, message: '请输入岗位职责', trigger: 'blur' }]
}

const statusMap = {
  ACTIVE: '招聘中',
  PAUSED: '已暂停',
  CLOSED: '已关闭'
}

const appStatusMap = {
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

function getAppStatusText(status) {
  return appStatusMap[status] || status
}

function formatDate(date) {
  return dayjs(date).format('YYYY-MM-DD HH:mm')
}

async function publishJob() {
  if (!jobFormRef.value) return
  await jobFormRef.value.validate(async (valid) => {
    if (valid) {
      submitting.value = true
      try {
        await jobApi.create(jobForm.value)
        ElMessage.success('职位发布成功！')
        resetForm()
        fetchMyJobs()
        statisticsStore.fetchStatistics()
      } finally {
        submitting.value = false
      }
    }
  })
}

function resetForm() {
  if (jobFormRef.value) {
    jobFormRef.value.resetFields()
  }
  jobForm.value.employerName = userStore.name
}

async function fetchMyJobs() {
  loadingJobs.value = true
  try {
    myJobs.value = await jobApi.getList({})
  } finally {
    loadingJobs.value = false
  }
}

async function fetchAllApplications() {
  loadingApplications.value = true
  try {
    const apps = []
    for (const job of myJobs.value) {
      const jobApps = await applicationApi.getByJob(job.id)
      apps.push(...jobApps)
    }
    allApplications.value = apps.sort((a, b) => new Date(b.appliedAt) - new Date(a.appliedAt))
  } finally {
    loadingApplications.value = false
  }
}

function filterApplications() {
}

async function updateJobStatus(jobId, status) {
  try {
    await jobApi.updateStatus(jobId, status)
    ElMessage.success('状态更新成功！')
    fetchMyJobs()
    statisticsStore.fetchStatistics()
  } catch (error) {
    console.error('更新状态失败', error)
  }
}

function viewJobDetail(jobId) {
  router.push(`/jobs/${jobId}`)
}

function viewApplication(app) {
  currentApplication.value = app
  newStatus.value = app.status
  showAppDetailDialog.value = true
}

async function updateApplicationStatus() {
  try {
    await applicationApi.updateStatus(currentApplication.value.id, { status: newStatus.value })
    ElMessage.success('状态更新成功！')
    currentApplication.value.status = newStatus.value
    fetchAllApplications()
    statisticsStore.fetchStatistics()
  } catch (error) {
    console.error('更新状态失败', error)
  }
}

onMounted(async () => {
  await fetchMyJobs()
  fetchAllApplications()
})
</script>

<style scoped>
.filter-bar {
  margin-bottom: 16px;
}
</style>
