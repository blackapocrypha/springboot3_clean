import type { pageLimit } from '@/api/common/type'

export interface logsPage extends pageLimit {
    /**
     * 账号
     */
    slogUserAccountIsLike:string,

    /**
     * 端源
     */
    slogClientIsLike:string
}


/**
 * SysHistoryLogs
 */
export interface SysHistoryLogs {
    /**
     * 创建时间
     */
    gmtCreate: null | string;
    /**
     * 更新时间
     */
    gmtModified: null | string;
    /**
     * 端源
     */
    slogClient: null | string;
    /**
     * id
     */
    slogId: number | null;
    /**
     * 登录用户id
     */
    slogLoginUserId: number | null;
    /**
     * 请求载荷
     */
    slogRequestData: null | string;
    /**
     * 请求者ip
     */
    slogRequestIp: null | string;
    /**
     * 请求方法
     */
    slogRequestMethod: null | string;
    /**
     * 请求地址
     */
    slogRequestUrl: null | string;
    /**
     * 响应数据
     */
    slogResponseData: null | string;
    /**
     * 账号
     */
    slogUserAccount: null | string;
}
