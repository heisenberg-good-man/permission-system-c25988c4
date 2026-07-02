import axios from 'axios'
import { ElMessage } from 'element-plus'

const request = axios.create({
  baseURL: '/api',
  timeout: 10000
})

request.interceptors.response.use(
  (response) => {
    const res = response.data
    if (res.code === 200) {
      return res.data
    } else {
      ElMessage.error(res.message || '请求失败')
      return Promise.reject(new Error(res.message || '请求失败'))
    }
  },
  (error) => {
    ElMessage.error(error.message || '网络错误')
    return Promise.reject(error)
  }
)

export const jobApi = {
  getList: (params) => request.get('/jobs', { params }),
  getDetail: (id) => request.get(`/jobs/${id}`),
  create: (data) => request.post('/jobs', data),
  updateStatus: (id, status) => request.put(`/jobs/${id}/status`, null, { params: { status } }),
  getCities: () => request.get('/jobs/cities'),
  getPositions: () => request.get('/jobs/positions')
}

export const applicationApi = {
  getByJob: (jobId) => request.get(`/applications/job/${jobId}`),
  getByApplicant: (applicantName) => request.get(`/applications/applicant/${applicantName}`),
  getDetail: (id) => request.get(`/applications/${id}`),
  create: (jobId, data) => request.post(`/applications/job/${jobId}`, data),
  updateStatus: (id, data) => request.put(`/applications/${id}/status`, data)
}

export const messageApi = {
  getList: (applicationId) => request.get(`/messages/application/${applicationId}`),
  send: (applicationId, data) => request.post(`/messages/application/${applicationId}`, data)
}

export const statisticsApi = {
  getStatistics: () => request.get('/statistics')
}

export default request
