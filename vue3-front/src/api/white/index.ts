import request from '@/utils/request'
import type { ACLWhiteList } from './type'
import { jsonResult } from '../common/type'


// 统一管理接口
enum API {
    SELECT = '/system/acl-white-list/selectByExample',
    INSERT = '/system/acl-white-list/insert',
    UPDATE = '/system/acl-white-list/update',
    DELETE = '/system/acl-white-list/delete',
}

// 查询
export const selectWhite = (data: any) =>
request.post<jsonResult>(API.SELECT, data)

// 更新
export const updateWhite = (data: ACLWhiteList) =>
request.post<jsonResult>(API.UPDATE, data)

// 插入
export const insertWhite = (data: ACLWhiteList) =>
request.post<jsonResult>(API.INSERT, data)

// 删除
export const deleteWhite = (data: any) =>
request.post<jsonResult>(API.DELETE, data)