import request from '@/utils/request'
import type { jsonResult } from '@/api/common/type'


enum API {
    SELECT = '/system/sys-history-logs/selectByExample',
}

// 查询
export const selectLogs = (data:any) =>
request.post<jsonResult>(API.SELECT,data)