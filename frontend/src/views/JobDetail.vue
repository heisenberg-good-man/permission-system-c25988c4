<template>
  <Layout>
    <div class="container" v-loading="loading">
      <el-button @click="goBack" style="margin-bottom: 20px">
        <el-icon><ArrowLeft /></el-icon> 返回列表
      </el-button>

      <div v-if="job" class="job-detail">
        <div class="card job-header-card">
          <div class="job-header">
            <div>
              <div class="job-title-row">
                <h1>{{ job.title }}</h1>
                <span :class="['status-tag', `status-${job.status.toLowerCase()}`]">
                  {{ getStatusText(job.status) }}
                </span>
              </div>
              <div class="job-salary">{{ job.minSalary }}K-{{ job.maxSalary }}K · 14薪</div>
              <div class="job-meta">
                <span><el-icon><Location /></el-icon> {{ job.city }}</span>
                <span><el-icon><Briefcase /></el-icon> {{ job.position }}</span>
                <span>
                  <el-icon><Clock /></el-icon>
                  {{ job.experienceMin }}-{{ job.experienceMax }}年经验
                </span>
                <span><el-icon><Reading /></el-icon> {{ job.education }}</span>
              </div>
            </div>
            <el-button
              v-if="userStore.isApplicant && job.status === 'ACTIVE'"
              type="primary"
              size="large"
              @click="showApplyDialog = true"
            >
              <el-icon><Send /></el-icon> 立即投递
            </el-button>
          </div>
          <div class="job-skills">
            <span class="skills-label">技能标签：</span>
            <el-tag
              v-for="skill in job.requiredSkills"
              :key="skill"
              type="primary"
              effect="plain"
              style="margin-right: 8px; margin-bottom: 4px"
            >
              {{ skill }}
            </el-tag>
          </div>
          <div class="job-company-info">
            <el-icon color="#2563eb" :size="20"><OfficeBuilding /></el-icon>
            <span class="company-name">{{ job.employerName }}</span>
            <span class="company-dept">· {{ job.department }}</span>
          </div>
          <div class="job-stats-row">
            <span><el-icon><View /></el-icon> 浏览 {{ job.viewCount }}</span>
            <span><el-icon><UserFilled /></el-icon> {{ job.applyCount }}人投递</span>
            <span><el-icon><Time /></el-icon> 发布于 {{ formatDate(job.createdAt) }}</span>
          </div>
        </div>

        <el-row :gutter="20">
          <el-col :span="16">
            <div class="card">
              <h3 class="section-title">
                <el-icon><Document /></el-icon> 职位描述
              </h3>
              <div class="content-text" style="white-space: pre-wrap">{{ job.description }}</div>
            </div>

            <div class="card">
              <h3 class="section-title">
                <el-icon><List /></el-icon> 岗位职责
              </h3>
              <div class="content-text" style="white-space: pre-wrap">{{ job.responsibilities }}</div>
            </div>

            <div v-if="userStore.isEmployer" class="card">
              <h3 class="section-title">
                <el-icon><Management /></el-icon> 收到的投递
              </h3>
              <div v-if="jobApplications.length > 0">
                <el-table :data="jobApplications" style="width: 100%">
                  <el-table-column prop="applicantName" label="姓名" width="100" />
                  <el-table-column prop="workYears" label="工作年限" width="100">
                    <template #default="{ row }">{{ row.workYears }}年</template>
                  </el-table-column>
                  <el-table-column label="状态" width="120">
                    <template #default="{ row }">
                      <span :class="['status-tag', `status-${row.status.toLowerCase()}`]">
                        {{ getAppStatusText(row.status) }}
                      </span>
                    </template>
                  </el-table-column>
                  <el-table-column prop="appliedAt" label="投递时间" width="180">
                    <template #default="{ row }">{{ formatDate(row.appliedAt) }}</template>
                  </el-table-column>
                  <el-table-column label="操作" width="150">
                    <template #default="{ row }">
                      <el-button type="primary" size="small" @click="viewApplication(row)">
                        查看详情
                      </el-button>
                    </template>
                  </el-table-column>
                </el-table>
              </div>
              <el-empty v-else description="暂无投递记录" />
            </div>
          </el-col>

          <el-col :span="8">
            <div class="card">
              <h3 class="section-title">
                <el-icon><Trophy /></el-icon> 你可能感兴趣
              </h3>
              <div class="recommend-item" v-for="i in 3" :key="i">
                <div class="recommend-title">高级前端开发工程师</div>
                <div class="recommend-salary">20K-35K</div>
                <div class="recommend-meta">北京 · 3-5年 · 本科</div>
              </div>
            </div>
          </el-col>
        </el-row>
      </div>

      <el-dialog
        v-model="showApplyDialog"
        title="投递简历"
        width="600px"
        :close-on-click-modal="false"
      >
        <el-form
          ref="applyFormRef"
          :model="applyForm"
          :rules="applyRules"
          label-width="100px"
        >
          <el-form-item label="姓名" prop="applicantName">
            <el-input v-model="applyForm.applicantName" placeholder="请输入姓名" />
          </el-form-item>
          <el-form-item label="手机号" prop="applicantPhone">
            <el-input v-model="applyForm.applicantPhone" placeholder="请输入手机号" />
          </el-form-item>
          <el-form-item label="邮箱" prop="applicantEmail">
            <el-input v-model="applyForm.applicantEmail" placeholder="请输入邮箱" />
          </el-form-item>
          <el-form-item label="工作年限" prop="workYears">
            <el-select v-model="applyForm.workYears" placeholder="请选择工作年限" style="width: 100%">
              <el-option :label="i + '年'" :value="i" v-for="i in 15" :key="i" />
            </el-select>
          </el-form-item>
          <el-form-item label="技能标签" prop="skills">
            <el-select
              v-model="applyForm.skills"
              multiple
              filterable
              allow-create
              placeholder="请输入或选择技能标签"
              style="width: 100%"
            >
              <el-option
                v-for="skill in job?.requiredSkills || []"
                :key="skill"
                :label="skill"
                :value="skill"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="简历摘要" prop="resumeSummary">
            <el-input
              v-model="applyForm.resumeSummary"
              type="textarea"
              :rows="4"
              placeholder="请简要介绍您的工作经历、项目经验和核心优势..."
            />
          </el-form-item>
        </el-form>
        <template #footer>
          <el-button @click="showApplyDialog = false">取消</el-button>
          <el-button type="primary" :loading="submitting" @click="submitApplication">
            确认投递
          </el-button>
        </template>
      </el-dialog>

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
              @click="updateStatus"
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
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import dayjs from 'dayjs'
import { jobApi, applicationApi } from '@/api'
import { useUserStore, useStatisticsStore } from '@/stores'
import Layout from '@/components/Layout.vue'
import MessagePanel from '@/components/MessagePanel.vue'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()
const statisticsStore = useStatisticsStore()

const loading = ref(false)
const submitting = ref(false)
const job = ref(null)
const jobApplications = ref([])
const showApplyDialog = ref(false)
const showAppDetailDialog = ref(false)
const currentApplication = ref(null)
const newStatus = ref('')

const applyFormRef = ref(null)
const applyForm = ref({
  applicantName: '',
  applicantPhone: '',
  applicantEmail: '',
  resumeSummary: '',
  workYears: null,
  skills: []
})

const applyRules = {
  applicantName: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
  applicantPhone: [{ required: true, message: '请输入手机号', trigger: 'blur' }],
  applicantEmail: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '邮箱格式不正确', trigger: 'blur' }
  ],
  resumeSummary: [{ required: true, message: '请输入简历摘要', trigger: 'blur' }],
  workYears: [{ required: true, message: '请选择工作年限', trigger: 'change' }],
  skills: [{ required: true, message: '请至少选择一个技能标签', trigger: 'change' }]
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

function getStatusText(status) {
  return statusMap[status] || status
}

function getAppStatusText(status) {
  return appStatusMap[status] || status
}

function formatDate(date) {
  return dayjs(date).format('YYYY-MM-DD HH:mm')
}

async function fetchJobDetail() {
  loading.value = true
  try {
    const jobId = route.params.id
    job.value = await jobApi.getDetail(jobId)
    if (userStore.isEmployer) {
      jobApplications.value = await applicationApi.getByJob(jobId)
    }
  } finally {
    loading.value = false
  }
}

async function submitApplication() {
  if (!applyFormRef.value) return
  await applyFormRef.value.validate(async (valid) => {
    if (valid) {
      submitting.value = true
      try {
        await applicationApi.create(job.value.id, applyForm.value)
        ElMessage.success('投递成功！')
        showApplyDialog.value = false
        fetchJobDetail()
        statisticsStore.fetchStatistics()
        applyFormRef.value.resetFields()
      } finally {
        submitting.value = false
      }
    }
  })
}

function viewApplication(app) {
  currentApplication.value = app
  newStatus.value = app.status
  showAppDetailDialog.value = true
}

async function updateStatus() {
  try {
    await applicationApi.updateStatus(currentApplication.value.id, { status: newStatus.value })
    ElMessage.success('状态更新成功！')
    currentApplication.value.status = newStatus.value
    fetchJobDetail()
    statisticsStore.fetchStatistics()
  } catch (error) {
    console.error('更新状态失败', error)
  }
}

function goBack() {
  router.push('/jobs')
}

onMounted(() => {
  fetchJobDetail()
})
</script>

<style scoped>
.job-header-card {
  margin-bottom: 20px;
}

.job-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 16px;
}

.job-title-row {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 8px;
}

.job-title-row h1 {
  font-size: 28px;
  font-weight: 700;
  color: #1f2937;
  margin: 0;
}

.job-salary {
  font-size: 24px;
  font-weight: 700;
  color: #f56c6c;
  margin-bottom: 12px;
}

.job-meta {
  display: flex;
  gap: 24px;
  color: #6b7280;
  font-size: 15px;
}

.job-meta span {
  display: flex;
  align-items: center;
  gap: 6px;
}

.job-skills {
  margin: 16px 0;
  padding: 16px 0;
  border-top: 1px solid #e5e7eb;
  border-bottom: 1px solid #e5e7eb;
}

.skills-label {
  font-weight: 600;
  color: #374151;
  margin-right: 12px;
}

.job-company-info {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 12px;
}

.company-name {
  font-weight: 600;
  color: #2563eb;
  font-size: 16px;
}

.company-dept {
  color: #6b7280;
}

.job-stats-row {
  display: flex;
  gap: 24px;
  color: #9ca3af;
  font-size: 14px;
}

.job-stats-row span {
  display: flex;
  align-items: center;
  gap: 6px;
}

.section-title {
  font-size: 18px;
  font-weight: 600;
  color: #1f2937;
  margin-bottom: 16px;
  display: flex;
  align-items: center;
  gap: 8px;
}

.content-text {
  color: #4b5563;
  line-height: 1.8;
}

.recommend-item {
  padding: 12px 0;
  border-bottom: 1px solid #f3f4f6;
  cursor: pointer;
}

.recommend-item:last-child {
  border-bottom: none;
}

.recommend-title {
  font-weight: 500;
  color: #1f2937;
  margin-bottom: 4px;
}

.recommend-salary {
  color: #f56c6c;
  font-weight: 600;
  margin-bottom: 4px;
}

.recommend-meta {
  font-size: 13px;
  color: #9ca3af;
}
</style>
