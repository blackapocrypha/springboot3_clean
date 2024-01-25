/**
 * AclWhiteList
 */
export interface ACLWhiteList {
    /**
     * id
     */
    acwlId?: number | null;
    /**
     * 强制锁(1不允许删除 0允许)
     */
    acwlLock?: number | null;
    /**
     * 接口地址
     */
    acwlPath?: null | string;
    [property: string]: any;
}
