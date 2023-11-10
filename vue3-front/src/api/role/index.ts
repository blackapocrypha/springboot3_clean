import request from '@/utils/request'
import type { jsonResult ,pageLimit,singleId} from '@/api/common/type'
import type { roleRequest,ACLRole } from './type'
// 统一管理接口
enum API {
  SELECT = '/system/acl-role/selectByExample',
  UPDATE = '/system/acl-role/update',
  DELETE = '/system/acl-role/delete',
  INSERT = '/system/acl-role/insert',
}

// 查询用户
export const selectRole = (data: pageLimit) =>
  request.post<jsonResult>(API.SELECT, data)

// 删除用户
  export const deleteRole = (data: singleId) =>
  request.post<jsonResult>(API.DELETE, data)


// 更新用户
export const updateRole = (data: roleRequest) =>
request.post<jsonResult>(API.UPDATE, data)


// 插入用户
export const insertRole = (data: ACLRole) =>
request.post<jsonResult>(API.INSERT, data)