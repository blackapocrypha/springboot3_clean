import axios from 'axios'
// @ts-ignore
import Qs from 'qs'
//@ts-ignore
import { ElMessage } from 'element-plus'
//token获取
import { GET_TOKEN } from '@/utils/token'
//创建axios实例
let request = axios.create({
  baseURL: import.meta.env.VITE_APP_BASE_API,
  timeout: 5000,
  headers: {
    'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8',
  },
  transformRequest: [
    function (data) {
      // 对 data 进行任意转换处理
      return Qs.stringify(data)
    },
  ],
})
//请求拦截器
request.interceptors.request.use((config) => {
  let token:any = GET_TOKEN()
  if (token) {
    config.headers['Authorization'] = token
  }
  return config
})
//响应拦截器
request.interceptors.response.use(
  (response) => {
    let res = response.data

    return res
  },
  (error) => {
    //处理网络错误
    let msg = ''
    let status = error.response.status
    switch (status) {
      case 401:
        msg = 'token过期'
        break
      case 403:
        msg = '无权访问'
        break
      case 404:
        msg = '请求地址错误'
        break
      case 500:
        msg = '服务器出现问题'
        break
      default:
        msg = '无网络'
    }
    ElMessage({
      type: 'error',
      message: msg,
    })
    return Promise.reject(error)
  },
)
export default request
