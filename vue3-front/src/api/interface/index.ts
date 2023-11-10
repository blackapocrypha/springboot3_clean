import request from '@/utils/request'
import type { jsonResult, singleId } from '@/api/common/type'
import type { ACLPath,ACLPathDTO } from './type'

enum API {
    SELECT_INTERFACE = '/system/acl-path/selectByExample',
    INSERT_INTERFACE = '/system/acl-path/insert',
    UPDATE_INTERFACE = '/system/acl-path/update',
    DELETE_INTERFACE = '/system/acl-path/delete',
    SELECT_ROLE_INTERFACE = '/system/acl-path-role/selectPathByRoleId',
    UPDATE_ROLE_INTERFACE = '/system/acl-path-role/updatePathByRole',
}

// 查询
export const selectInterface = (data:any | null) =>
request.post<jsonResult>(API.SELECT_INTERFACE,data)

// 根据角色id查询接口信息
export const selectPathByRoleId = (data:singleId | null) =>
request.post<jsonResult>(API.SELECT_ROLE_INTERFACE,data)

// 添加
export const addInterface = (data:ACLPath ) =>
request.post<jsonResult>(API.INSERT_INTERFACE,data)


// 更新
export const updateInterface = (data:ACLPath ) =>
request.post<jsonResult>(API.UPDATE_INTERFACE,data)

// 根据角色更新接口
export const updatePathByRole = (data:ACLPathDTO ) =>
request.post<jsonResult>(API.UPDATE_ROLE_INTERFACE,data)


// 删除
export const deleteInterface = (data:singleId ) =>
request.post<jsonResult>(API.DELETE_INTERFACE,data)