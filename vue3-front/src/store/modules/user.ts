//创建用户相关的小仓库
import { defineStore } from 'pinia'
//引入接口
import { reqLogin, reqUserInfo, reqLogout } from '@/api/user'
import type {
  loginForm,
  loginResult,
  logOutToken,
} from '@/api/user/type'
// 引入接口状态识别
import { REQ_SUCCESS } from '@/utils/resultStatus'
import type { UserState } from './types/type'

//引入操作本地存储的工具方法
import {
  SET_TOKEN,
  GET_TOKEN,
  REMOVE_TOKEN,
  SET_TOKEN_USER,
} from '@/utils/token'
//引入路由(常量路由)
import { constantRoute, asyncRoute, anyRoute } from '@/router/routes'
//引入深拷贝方法
//@ts-ignore
import cloneDeep from 'lodash/cloneDeep'
import router from '@/router'
//用于过滤当前用户需要展示的异步路由
function filterAsyncRoute(asyncRoute: any, routes: any) {
  return asyncRoute.filter((item: any) => {
    if (routes.includes(item.name)) {
      if (item.children && item.children.length > 0) {
        item.children = filterAsyncRoute(item.children, routes)
      }
      return true
    }
  })
}

//创建用户小仓库
const useUserStore = defineStore('User', {
  //小仓库存储数据地方
  state: (): UserState => {
    return {
      token: GET_TOKEN(), //用户唯一标识token
      menuRoutes: constantRoute, //仓库存储生成菜单需要数组(路由)
      account: '',
      avatar: '',
      realName: '',
      id: null,
      //存储当前用户是否包含某一个按钮
      buttons: [],
    }
  },
  //异步|逻辑的地方
  actions: {
    //用户登录的方法
    async userLogin(data: loginForm) {
      //登录请求
      const result: loginResult | any = await reqLogin(data)
      if (result.status == REQ_SUCCESS) {
        //pinia仓库存储一下token
        //由于pinia|vuex存储数据其实利用js对象
        this.token = result.data as string
        //本地存储持久化存储一份
        SET_TOKEN(result.data as string)
        //能保证当前async函数返回一个成功的promise
        this.userInfo()
        return 'ok'
      } else {
        return Promise.reject(new Error(result.message))
      }
    },
    //获取用户信息方法
    async userInfo() {
      //获取用户信息进行存储仓库当中[用户头像、名字]
      const result: loginResult = await reqUserInfo()
      //如果获取用户信息成功，存储一下用户信息
      if (result.status == REQ_SUCCESS) {
        this.account = result.data.userAccount
        this.avatar = result.data.img
        this.buttons = result.data.buttons
        this.realName = result.data.userRealname
        this.id = result.data.id
        result.data.userSalt = ''
        SET_TOKEN_USER(result.data)

        //计算当前用户需要展示的异步路由
        const userAsyncRoute = filterAsyncRoute(
          cloneDeep(asyncRoute),
          result.data.routes,
        )

        //菜单需要的数据整理完毕
        this.menuRoutes = [...constantRoute, ...userAsyncRoute, anyRoute]
        //目前路由器管理的只有常量路由:用户计算完毕异步路由、任意路由动态追加
        ;[...userAsyncRoute, anyRoute].forEach((route: any) => {
          router.addRoute(route)
        })
        return 'ok'
      } else {
        return Promise.reject(new Error(result.message))
      }
    },
    //退出登录
    async userLogout() {
      //退出登录请求
      let token: any = GET_TOKEN()
      if (!token) {
        return Promise.reject(new Error('用户未登录'))
      }
      let data: logOutToken = { token: token }
      const result: any = await reqLogout(data)
      this.token = ''
      this.account = ''
      this.avatar = ''
      this.id = null
      REMOVE_TOKEN()
      if (result.status == REQ_SUCCESS) {
        return 'ok'
      } else {
        return Promise.reject(new Error(result.message))
      }
    },
    // 清除token
    clearnToken() {
      this.token = ''
    },
  },
  getters: {},
})
//对外暴露获取小仓库方法
export default useUserStore
