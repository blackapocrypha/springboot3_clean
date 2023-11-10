import request from '@/utils/request'
import type { UserRole } from './type'
import { jsonResult } from '../common/type'



// 统一管理接口
enum API {
    INSERT = '/system/acl-user-role/insert'
}


// 插入
export const insertUserRole = (data: UserRole) =>
request.post<jsonResult>(API.INSERT, data)

