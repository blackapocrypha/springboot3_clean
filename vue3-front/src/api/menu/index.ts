import request from '@/utils/request'
import type { jsonResult, singleId } from '@/api/common/type'
import type { ACLMenuDTO,AclMenu} from './type'

enum API {
  SELECT = '/system/acl-menu/selectByExample',
  INSERT = '/system/acl-menu/insert',
  UPDATE_MENU = '/system/acl-menu/update',
  DELETE_MENU = '/system/acl-menu/delete',
  SELECT_BY_USERTID = '/system/acl-menu-role/selectCurrentUserMenu',
  SELECT_BY_ROLEID = '/system/acl-menu-role/selectCurrentRoleMenu',
  UPDATE = '/system/acl-menu-role/update',
}

// 查询
export const selectMenu = (data:any) =>
  request.post<jsonResult>(API.SELECT,data)

// 插入
export const addMenu = (data:AclMenu) =>
request.post<jsonResult>(API.INSERT,data)

// 删除
export const deleteMenu = (data:singleId) =>
request.post<jsonResult>(API.DELETE_MENU,data)

// 更新菜单
export const updateCurrentMenu = (data:AclMenu) =>
request.post<jsonResult>(API.UPDATE_MENU,data)

// 根据用户id查询菜单
  export const selectMenuByUserId = (data:singleId) =>
  request.post<jsonResult>(API.SELECT_BY_USERTID,data)

// 根据角色id查询菜单
export const selectMenuByRoleId = (data:singleId) =>
request.post<jsonResult>(API.SELECT_BY_ROLEID,data)



// 根据角色id更新菜单
export const updateRoleMenu = (data:ACLMenuDTO) =>
request.post<jsonResult>(API.UPDATE,data)