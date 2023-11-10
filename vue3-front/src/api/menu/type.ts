import type { pageLimit } from '@/api/common/type'




export interface menuPage extends pageLimit {
    orderItem:string,
    orderType:string
}



/**
 * AclMenuDTO
 */
export interface ACLMenuDTO {
    /**
     * 代码
     */
    acmeCode?: null | string;
    /**
     * id
     */
    acmeId?: number | null;
    /**
     * 菜单名称
     */
    acmeName?: null | string;
    /**
     * 父级，0为顶级
     */
    acmePid?: number | null;
    /**
     * 是否可选Y是N否
     */
    acmeSelect?: null | string;
    /**
     * 排序
     */
    acmeSort?: number | null;
    /**
     * 状态
     */
    acmeStatus?: number | null;
    /**
     * 类型 1菜单 2按钮
     */
    acmeType?: number | null;
    /**
     * 子节点
     */
    children?: ChildElement[] | null;
    /**
     * 创建时间
     */
    gmtCreate?: any[] | boolean | number | number | { [key: string]: any } | null | string;
    /**
     * 更新时间
     */
    gmtModified?: any[] | boolean | number | number | { [key: string]: any } | null | string;

    /**
     * 多个菜单id用逗号隔开
     */
    menuIds?: string | null;
    /**
     * 层级
     */
    level?: number | null;
}

/**
 * AclMenuDTO
 */
export interface AclMenu{
    /**
     * 代码
     */
    acmeCode?: null | string;
    /**
     * id
     */
    acmeId?: number | null;
    /**
     * 菜单名称
     */
    acmeName?: null | string;
    /**
     * 父级，0为顶级
     */
    acmePid?: number | null;
    /**
     * 是否可选Y是N否
     */
    acmeSelect?: null | string;
    /**
     * 排序
     */
    acmeSort?: number | null;
    /**
     * 状态
     */
    acmeStatus?: number | null;
    /**
     * 类型 1菜单 2按钮
     */
    acmeType?: number | null;
    /**
     * 层级
     */
    level?: number | null;
    /**
     * 子节点
     */
    children?: ChildElement[] | null;
    /**
     * 创建时间
     */
    gmtCreate?: any[] | boolean | number | number | { [key: string]: any } | null | string;
    /**
     * 更新时间
     */
    gmtModified?: any[] | boolean | number | number | { [key: string]: any } | null | string;
}

export type ChildElement = AclMenu[]