import request from '@/utils/request'
import type { loginForm, loginResult,logOutToken ,requestModel,ACLUser,deleteIdList} from './type'
import { singleId } from '../common/type'
// 统一管理接口
enum API {
  LOGIN_URL = '/acl/login',
  USER_INFO_URL = '/system/acl-user/getUserInfo',
  LOGOUT = '/acl/logOut',
  SELECT = '/system/acl-user/selectByExample',
  UPDATE = '/system/acl-user/update',
  DELETE = '/system/acl-user/delete',
  DELETE_BATCH = '/system/acl-user/deleteBatch',
  INSERT = '/system/acl-user/insert',
  CODE = '/common/captchaImage'
}

// 登录
export const reqLogin = (data: loginForm) =>
  request.post<loginResult>(API.LOGIN_URL, data)

// 获取用户信息
export const reqUserInfo = () => request.post<any,loginResult>(API.USER_INFO_URL)

// 登出
export const reqLogout = (data: logOutToken) =>
  request.post<any,loginResult>(API.LOGOUT,data)

// 查询
export const selectUser = (data: requestModel) =>
request.post<loginResult>(API.SELECT, data)

// 更新
export const updateUser = (data: ACLUser) =>
request.post<loginResult>(API.UPDATE, data)

// 插入
export const insertUser = (data: ACLUser) =>
request.post<loginResult>(API.INSERT, data)

// 删除
export const deleteUser = (data: singleId) =>
request.post<loginResult>(API.DELETE, data)

// 批量删除
export const deleteUserBatch = (data: deleteIdList) =>
request.post<loginResult>(API.DELETE_BATCH, data)


// 验证码
export const getCode = () => 
request.post<loginResult>(API.CODE)
