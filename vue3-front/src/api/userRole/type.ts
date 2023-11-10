



/**
 * UserRole
 */
export interface UserRole {
    /**
     * id
     */
    acurId?: number | null;
    /**
     * 角色id
     */
    acurRoleId: number | null;
    /**
     * 用户Id
     */
    acurUserId: number | null;
    /**
     * 多角色，逗号隔开
     */
    allRoles?: null | string;
    /**
     * 创建时间
     */
    gmtCreate: null | string;
    /**
     * 更新时间
     */
    gmtModified?: null | string;

}




  