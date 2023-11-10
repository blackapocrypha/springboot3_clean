import type { pageLimit } from '@/api/common/type'

export interface roleRequest {
    /**
     * 代码
     */
    roleCode?: string;
    /**
     * 角色id
     */
    roleId: number | null;
    /**
     * 角色名
     */
    roleName?: string;
    /**
     * 备注
     */
    roleRemark?: string;
}


/**
 * AclRole
 */
export interface ACLRole {
    /**
     * 创建时间
     */
    gmtCreate?: any[] | boolean | number | number | { [key: string]: any } | null | string;
    /**
     * 更新时间
     */
    gmtModified?: any[] | boolean | number | number | { [key: string]: any } | null | string;
    /**
     * 逻辑删除 0否 1是
     */
    isDeleted?: number | null;
    /**
     * 代码
     */
    roleCode?: null | string;
    /**
     * id
     */
    roleId: number | null;
    /**
     * 角色名
     */
    roleName?: null | string;
    /**
     * 备注
     */
    roleRemark?: null | string;
}


/**
 * 角色部分的page
 */
export interface rolePage extends pageLimit {
    orderItem:string,
    orderType:string
}