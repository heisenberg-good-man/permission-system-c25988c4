<template>
  <div class="layout">
    <el-header class="header">
      <div class="header-content">
        <div class="logo">
          <el-icon :size="28" color="#2563eb"><Briefcase /></el-icon>
          <span class="logo-text">招聘平台</span>
        </div>
        <el-menu
          :default-active="activeMenu"
          mode="horizontal"
          router
          class="nav-menu"
        >
          <el-menu-item index="/dashboard">
            <el-icon><DataAnalysis /></el-icon>
            工作台
          </el-menu-item>
          <el-menu-item index="/jobs">
            <el-icon><Search /></el-icon>
            职位列表
          </el-menu-item>
          <el-menu-item v-if="userStore.isApplicant" index="/my-applications">
            <el-icon><Document /></el-icon>
            我的投递
          </el-menu-item>
          <el-menu-item v-if="userStore.isEmployer" index="/employer">
            <el-icon><Management /></el-icon>
            招聘管理
          </el-menu-item>
        </el-menu>
        <div class="user-info">
          <el-tag :type="userStore.isEmployer ? 'primary' : 'success'" effect="light">
            {{ userStore.isEmployer ? '招聘方' : '应聘方' }}: {{ userStore.name }}
          </el-tag>
          <el-dropdown @command="handleRoleSwitch">
            <el-button type="primary" size="small" style="margin-left: 12px">
              切换身份 <el-icon class="el-icon--right"><ArrowDown /></el-icon>
            </el-button>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="APPLICANT:张三">
                  <el-icon><User /></el-icon> 应聘方：张三
                </el-dropdown-item>
                <el-dropdown-item command="APPLICANT:李四">
                  <el-icon><User /></el-icon> 应聘方：李四
                </el-dropdown-item>
                <el-dropdown-item command="EMPLOYER:科技有限公司HR">
                  <el-icon><OfficeBuilding /></el-icon> 招聘方：科技有限公司
                </el-dropdown-item>
                <el-dropdown-item command="EMPLOYER:互联网科技公司HR">
                  <el-icon><OfficeBuilding /></el-icon> 招聘方：互联网科技公司
                </el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </div>
    </el-header>
    <el-main class="main">
      <slot />
    </el-main>
  </div>
</template>

<script setup>
import { computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useUserStore, useStatisticsStore } from '@/stores'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()
const statisticsStore = useStatisticsStore()

const activeMenu = computed(() => route.path)

function handleRoleSwitch(command) {
  const [role, name] = command.split(':')
  userStore.switchRole(role, name)
  statisticsStore.fetchStatistics()
  if (role === 'EMPLOYER') {
    router.push('/employer')
  } else {
    router.push('/jobs')
  }
}

onMounted(() => {
  statisticsStore.fetchStatistics()
})
</script>

<style scoped>
.layout {
  min-height: 100vh;
}

.header {
  background: #fff;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  padding: 0;
  height: 64px;
}

.header-content {
  max-width: 1400px;
  margin: 0 auto;
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 100%;
  padding: 0 24px;
}

.logo {
  display: flex;
  align-items: center;
  gap: 8px;
}

.logo-text {
  font-size: 20px;
  font-weight: 700;
  color: #1f2937;
}

.nav-menu {
  flex: 1;
  margin: 0 40px;
  border-bottom: none;
}

.user-info {
  display: flex;
  align-items: center;
}

.main {
  padding: 24px;
  background: #f5f7fa;
}
</style>
