import type { jsonResult, pageLimit } from '../common/type'
//登录接口的ts对象
export interface loginForm {
  account: string
  password: string
  uuid: null | string
  code: null | string
}

// 登录接口返回的数据类型
export interface loginResult extends jsonResult {}

// 返回的用户信息
export interface jwtUser {
  id: number
  userAccount: string
  userRealname: string
  userSalt: string
  img: string
  userGender: number
  auth: string[]
  routes: string[]
  buttons: string[]
}

// 登出接口
export interface logOutToken {
  token: string
}

// 批量删除
export interface deleteIdList {
  ids: string
}

// 查询请求参数
export interface requestModel extends userPage {
  /**
   * 账号
   */
  accountIsLike?: string
  /**
   * id
   */
  'param.id'?: number
  /**
   * 手机号
   */
  phoneIsLike?: string
  /**
   * 姓名
   */
  realNameIsLike?: string
}

/**
 * AclUser
 */
export interface ACLUser {
  id: number | null
  /**
   * 账号
   */
  account: null | string
  /**
   * 性别 0无 1男 2女
   */
  gender: number | null

  /**
   * 昵称
   */
  nickName: null | string
  /**
   * 密码
   */
  password: null | string
  /**
   * 手机号
   */
  phone: null | string
  /**
   * 姓名
   */
  realName: null | string
  /**
   * 盐
   */
  salt: null | string
  /**
   * 头像
   */
  userImage: null | string

   /**
     * 创建时间
     */
   gmtCreate: any[] | boolean | number | number | { [key: string]: any } | null | string;
   /**
    * 更新时间
    */
   gmtModified?: any[] | boolean | number | number | { [key: string]: any } | null | string;
}


/**
 * 用户部分的page
 */
export interface userPage extends pageLimit {
  orderItem: string
  orderType: string
}
