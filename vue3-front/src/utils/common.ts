// 基础拼接路径
export const BASE_URL = 'http://127.0.0.1'
export const BASE_URL_PORT = 'http://127.0.0.1:8090'


//export const BASE_URL = 'http://127.0.0.1'
//export const BASE_URL_PORT = 'http://127.0.0.1:8090'

// 格式化性别
export const formatGender = (genderNumber: number) => {
  if (genderNumber == 1) {
    return '男'
  }

  if (genderNumber == 2) {
    return '女'
  }
  return ''
}

// 格式化图片
export const formatImage = (url: string) => {
  if (url && url.substring(0, 1) === '/' ) {
    return `${BASE_URL}${url}`
  }

  return `${BASE_URL}/${url}`
}
