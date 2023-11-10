import router from '@/router'
import nprogress from 'nprogress'
// 引入进度条样式
import 'nprogress/nprogress.css'
// 禁止加载动画出现
nprogress.configure({ showSpinner: false })

import setting from './setting'
//引入用户相关的小仓库
import pinia from './store'
import useUserStore from '@/store/modules/user'
let userStore = useUserStore(pinia)
// 全局守卫
// 全局前置守卫
//@ts-ignore
router.beforeEach(async (to: any, from: any, next: any) => {
  // to将要访问路由
  // from来源路由
  // next放行函数
  document.title = `${setting.title} - ${to.meta.title}`
  nprogress.start()
  // 获取token，判断用户是登录了还是未登录
  let token = userStore.token
  //获取用户名字
  const username = userStore.account
  if (token) {
    // 登录后禁止重新访问登录界面
    if (to.path === '/login') {
      next({ path: '/home' })
    } else {
      if (username) {
        await userStore.userInfo()
        next()
      } else {
        try {
          await userStore.userInfo()
          // 异步路由
          next({ ...to })
        } catch (error) {
          await userStore.userLogout()
          next({ path: '/login', query: { redirect: to.path } })
        }
      }
    }
  } else {
    // 未登录访问Login允许,否则全部跳转login
    if (to.path == '/login' || to.path == '/screen') {
      next()
    } else {
      next({ path: '/login', query: { redirect: to.path } })
    }
  }
})

// 全局后置守卫
//@ts-ignore
router.afterEach((to: any, from: any) => {
  nprogress.done()
})
