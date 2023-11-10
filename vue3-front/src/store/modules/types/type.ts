import type { RouteRecordRaw } from 'vue-router'

//定义小仓库数据state类型
export interface UserState {
  token: string | null
  menuRoutes: RouteRecordRaw[]
  account: string
  avatar: string
  realName: string
  id: number | null
  buttons: string[]
}


