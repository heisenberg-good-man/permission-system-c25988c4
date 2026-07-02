<template>
  <Layout>
    <div class="container">
      <h2 class="page-title">工作台</h2>

      <el-row :gutter="20">
        <el-col :span="6">
          <div class="stat-card job-card">
            <div class="stat-icon">
              <el-icon :size="32"><Briefcase /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ statisticsStore.stats.activeJobCount }}</div>
              <div class="stat-label">在招职位数</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card today-card">
            <div class="stat-icon">
              <el-icon :size="32"><Document /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ statisticsStore.stats.todayApplicationCount }}</div>
              <div class="stat-label">今日投递数</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card communicating-card">
            <div class="stat-icon">
              <el-icon :size="32"><ChatDotRound /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ statisticsStore.stats.communicatingCount }}</div>
              <div class="stat-label">沟通中数量</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card pending-card">
            <div class="stat-icon">
              <el-icon :size="32"><Clock /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ statisticsStore.stats.pendingCount }}</div>
              <div class="stat-label">待处理简历</div>
            </div>
          </div>
        </el-col>
      </el-row>

      <el-row :gutter="20" style="margin-top: 24px">
        <el-col :span="12">
          <div class="card">
            <div class="card-header">
              <h3>快捷操作</h3>
            </div>
            <div class="quick-actions">
              <el-button v-if="userStore.isEmployer" type="primary" size="large" @click="goToPublish">
                <el-icon><Plus /></el-icon> 发布新职位
              </el-button>
              <el-button v-if="userStore.isApplicant" type="primary" size="large" @click="goToJobs">
                <el-icon><Search /></el-icon> 浏览职位
              </el-button>
              <el-button v-if="userStore.isEmployer" type="success" size="large" @click="goToEmployer">
                <el-icon><List /></el-icon> 查看投递
              </el-button>
              <el-button v-if="userStore.isApplicant" type="success" size="large" @click="goToMyApplications">
                <el-icon><Document /></el-icon> 我的投递
              </el-button>
            </div>
          </div>
        </el-col>
        <el-col :span="12">
          <div class="card">
            <div class="card-header">
              <h3>平台说明</h3>
            </div>
            <div class="tips">
              <el-timeline>
                <el-timeline-item timestamp="第一步" placement="top">
                  <el-card>
                    <h4>切换身份</h4>
                    <p>点击右上角"切换身份"按钮，可以在招聘方和应聘方之间切换体验不同功能</p>
                  </el-card>
                </el-timeline-item>
                <el-timeline-item timestamp="第二步" placement="top">
                  <el-card>
                    <h4>浏览职位 / 发布职位</h4>
                    <p>应聘方可以浏览和筛选职位，招聘方可以发布新职位</p>
                  </el-card>
                </el-timeline-item>
                <el-timeline-item timestamp="第三步" placement="top">
                  <el-card>
                    <h4>投递简历 / 查看投递</h4>
                    <p>应聘方投递简历后可查看状态，招聘方可以收到投递并处理</p>
                  </el-card>
                </el-timeline-item>
                <el-timeline-item timestamp="第四步" placement="top">
                  <el-card>
                    <h4>在线沟通</h4>
                    <p>双方可以在投递详情中进行实时消息沟通</p>
                  </el-card>
                </el-timeline-item>
              </el-timeline>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>
  </Layout>
</template>

<script setup>
import { onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore, useStatisticsStore } from '@/stores'
import Layout from '@/components/Layout.vue'

const router = useRouter()
const userStore = useUserStore()
const statisticsStore = useStatisticsStore()

function goToJobs() {
  router.push('/jobs')
}

function goToPublish() {
  router.push('/employer')
}

function goToEmployer() {
  router.push('/employer')
}

function goToMyApplications() {
  router.push('/my-applications')
}

onMounted(() => {
  statisticsStore.fetchStatistics()
})
</script>

<style scoped>
.stat-card {
  background: #fff;
  border-radius: 12px;
  padding: 24px;
  display: flex;
  align-items: center;
  gap: 16px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
  transition: transform 0.3s, box-shadow 0.3s;
}

.stat-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.12);
}

.stat-icon {
  width: 64px;
  height: 64px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
}

.job-card .stat-icon {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.today-card .stat-icon {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
}

.communicating-card .stat-icon {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
}

.pending-card .stat-icon {
  background: linear-gradient(135deg, #fa709a 0%, #fee140 100%);
}

.stat-value {
  font-size: 32px;
  font-weight: 700;
  color: #1f2937;
  line-height: 1.2;
}

.stat-label {
  font-size: 14px;
  color: #6b7280;
  margin-top: 4px;
}

.card-header {
  margin-bottom: 20px;
  padding-bottom: 12px;
  border-bottom: 1px solid #e5e7eb;
}

.card-header h3 {
  font-size: 18px;
  font-weight: 600;
  color: #1f2937;
}

.quick-actions {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
}

.tips h4 {
  margin: 0 0 8px 0;
  font-size: 16px;
  color: #1f2937;
}

.tips p {
  margin: 0;
  color: #6b7280;
  font-size: 14px;
}
</style>
