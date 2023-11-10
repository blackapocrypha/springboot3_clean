//封装一个函数:获取一个结果:当前早上|上午|下午|晚上
export const getTime = () => {
  let message = ''
  //通过内置构造函数Date
  const hours = new Date().getHours()
  //情况的判断
  if (hours <= 9) {
    message = '早上'
  } else if (hours <= 12) {
    message = '上午'
  } else if (hours <= 18) {
    message = '下午'
  } else {
    message = '晚上'
  }
  return message
}

// 格式化日期
export const formatDate = (dateString: string) => {
  if(dateString ==null || dateString === ''){
    return '';
  }
  const date = new Date(dateString)
  const year = date.getFullYear()
  const month = date.getMonth() + 1
  const day = date.getDate()
  const formattedDate = `${year}-${month < 10 ? '0' + month : month}-${
    day < 10 ? '0' + day : day
  }`

  return formattedDate
}
