


/**
 * AclPath
 */
export interface ACLPath {
    /**
     * id
     */
    acpaId: number | null;
    /**
     * 路径名
     */
    acpaName: null | string;
    /**
     * 路径
     */
    acpaPath?: null | string;
    /**
     * 上级id
     */
    acpaPid: number | null;
    /**
     * 备注
     */
    acpaRemark: null | string;
    /**
     * 排序
     */
    acpaSort: number | null;
    /**
     * 类型 0分类 1路径
     */
    acpaType: number | null;
    /**
     * 下级
     */
    children?: Array<any[] | boolean | number | number | { [key: string]: any } | null | string> | null;
    /**
     * 创建时间
     */
    gmtCreate: any[] | boolean | number | number | { [key: string]: any } | null | string;
    /**
     * 更新时间
     */
    gmtModified: any[] | boolean | number | number | { [key: string]: any } | null | string;
    /**
     * 逻辑删除
     */
    isDeleted: number | null;
}


/**
 * ACLPathDTO
 */
export interface ACLPathDTO {
    /**
     * id
     */
    acpaId?: number | null;
    /**
     * 路径名
     */
    acpaName?: null | string;
    /**
     * 路径
     */
    acpaPath?: null | string;
    /**
     * 上级id
     */
    acpaPid?: number | null;
    /**
     * 备注
     */
    acpaRemark?: null | string;
    /**
     * 排序
     */
    acpaSort?: number | null;
    /**
     * 类型 0分类 1路径
     */
    acpaType?: number | null;


    roleIds?: string | null;

    pathIds?: string | null;
}