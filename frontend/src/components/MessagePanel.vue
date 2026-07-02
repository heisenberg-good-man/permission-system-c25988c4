<template>
  <div class="message-panel">
    <h4 style="margin-bottom: 16px; display: flex; align-items: center; gap: 8px">
      <el-icon><ChatDotRound /></el-icon> 消息沟通
    </h4>

    <div class="message-list" ref="messageListRef">
      <el-empty v-if="messages.length === 0" description="暂无消息，开始沟通吧" :image-size="80" />
      <div
        v-for="msg in messages"
        :key="msg.id"
        :class="['message-item', msg.senderRole === currentRole ? 'message-self' : 'message-other']"
      >
        <div :class="['message-avatar', msg.senderRole === 'EMPLOYER' ? 'avatar-employer' : 'avatar-applicant']">
          {{ msg.senderName.charAt(0) }}
        </div>
        <div class="message-content">
          <div class="message-meta">
            <span class="sender-name">{{ msg.senderName }}</span>
            <el-tag size="small" :type="msg.senderRole === 'EMPLOYER' ? 'primary' : 'success'">
              {{ msg.senderRole === 'EMPLOYER' ? '招聘方' : '应聘方' }}
            </el-tag>
            <span class="message-time">{{ formatTime(msg.sentAt) }}</span>
          </div>
          <div class="message-bubble">
            {{ msg.content }}
          </div>
        </div>
      </div>
    </div>

    <div class="message-input">
      <el-input
        v-model="newMessage"
        type="textarea"
        :rows="2"
        placeholder="输入消息..."
        resize="none"
        @keydown.enter.ctrl="sendMessage"
      />
      <div class="input-actions">
        <span class="tip">按 Ctrl+Enter 发送</span>
        <el-button type="primary" :disabled="!newMessage.trim()" :loading="sending" @click="sendMessage">
          发送
        </el-button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch, nextTick } from 'vue'
import dayjs from 'dayjs'
import { messageApi } from '@/api'
import { useUserStore, useStatisticsStore } from '@/stores'

const props = defineProps({
  applicationId: {
    type: Number,
    required: true
  }
})

const userStore = useUserStore()
const statisticsStore = useStatisticsStore()

const messageListRef = ref(null)
const messages = ref([])
const newMessage = ref('')
const sending = ref(false)
const currentRole = ref(userStore.role)

function formatTime(time) {
  return dayjs(time).format('MM-DD HH:mm')
}

async function fetchMessages() {
  try {
    messages.value = await messageApi.getList(props.applicationId)
    nextTick(() => {
      scrollToBottom()
    })
  } catch (error) {
    console.error('获取消息失败', error)
  }
}

async function sendMessage() {
  if (!newMessage.value.trim()) return

  sending.value = true
  try {
    await messageApi.send(props.applicationId, {
      senderName: userStore.name,
      senderRole: userStore.role,
      content: newMessage.value.trim()
    })
    newMessage.value = ''
    await fetchMessages()
    statisticsStore.fetchStatistics()
  } finally {
    sending.value = false
  }
}

function scrollToBottom() {
  if (messageListRef.value) {
    messageListRef.value.scrollTop = messageListRef.value.scrollHeight
  }
}

watch(() => props.applicationId, () => {
  currentRole.value = userStore.role
  fetchMessages()
})

onMounted(() => {
  currentRole.value = userStore.role
  fetchMessages()
})
</script>

<style scoped>
.message-panel {
  border: 1px solid #e5e7eb;
  border-radius: 8px;
  padding: 16px;
  background: #fafafa;
}

.message-list {
  max-height: 400px;
  overflow-y: auto;
  margin-bottom: 16px;
  padding-right: 8px;
}

.message-list::-webkit-scrollbar {
  width: 6px;
}

.message-list::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 3px;
}

.message-list::-webkit-scrollbar-thumb {
  background: #c1c1c1;
  border-radius: 3px;
}

.message-item {
  display: flex;
  margin-bottom: 20px;
  gap: 12px;
}

.message-self {
  flex-direction: row-reverse;
}

.message-avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  font-weight: 600;
  flex-shrink: 0;
}

.avatar-employer {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.avatar-applicant {
  background: linear-gradient(135deg, #11998e 0%, #38ef7d 100%);
}

.message-content {
  max-width: 70%;
}

.message-self .message-content {
  text-align: right;
}

.message-meta {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 6px;
  font-size: 12px;
  color: #9ca3af;
}

.message-self .message-meta {
  justify-content: flex-end;
}

.sender-name {
  font-weight: 500;
  color: #6b7280;
}

.message-bubble {
  display: inline-block;
  padding: 12px 16px;
  border-radius: 12px;
  background: #fff;
  border: 1px solid #e5e7eb;
  text-align: left;
  line-height: 1.6;
  word-break: break-word;
}

.message-self .message-bubble {
  background: #2563eb;
  color: #fff;
  border-color: #2563eb;
}

.message-input {
  background: #fff;
  border: 1px solid #e5e7eb;
  border-radius: 8px;
  padding: 12px;
}

.input-actions {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 8px;
}

.tip {
  font-size: 12px;
  color: #9ca3af;
}
</style>
