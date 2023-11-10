<template>
  <el-table
    :data="menuArr"
    style="width: 100%; margin-bottom: 20px"
    row-key="acmeId"
    node-key="acmeId"
    :tree-props="{ children: 'children' }"
    border
  >
    <el-table-column label="名称" prop="acmeName">
      <template #default="scope">
          <el-icon v-if="scope.row.acmeType==1"><Menu /></el-icon>
          <el-icon v-if="scope.row.acmeType==2"><Open /></el-icon>
         {{ scope.row.acmeName }}
      </template>
    </el-table-column>
    <el-table-column label="权限值" prop="acmeCode"></el-table-column>
    <el-table-column label="类型" prop="acmeType" width="70">
      <template #default="scope">
        <el-tag v-if="scope.row.acmeType==1" class="ml-2" type="success">菜单</el-tag>
        <el-tag v-if="scope.row.acmeType==2" class="ml-2" type="info">按钮</el-tag>
      </template>
    </el-table-column>
    <el-table-column label="修改时间" prop="gmtModified"></el-table-column>
    <el-table-column label="操作">
      <!-- row:即为已有的菜单对象|按钮的对象的数据 -->
      <template #="{ row }">
        <el-button
          @click="clickAddMenu(row)"
          size="small"
          :disabled="row.level >= 5 ? true : false"
        >
          {{ '添加' }}
        </el-button>
        <el-button
          type="primary"
          @click="updateMenu(row)"
          size="small"
          :disabled="row.acmePid == 0 ? true : false"
        >
          编辑
        </el-button>
        <el-popconfirm
          :title="`你确定要删除${row.acmeName}?`"
          width="260px"
          @confirm="removeMenu(row.acmeId)"
        >
          <template #reference>
            <el-button
              type="danger"
              size="small"
              :disabled="row.acmePid == 0 ? true : false"
            >
              删除
            </el-button>
          </template>
        </el-popconfirm>
      </template>
    </el-table-column>
  </el-table>

  <el-dialog
    v-model="dialogVisible"
    :title="menuData.acmeId ? '更新菜单' : '添加菜单'"
  >
    <!-- 表单组件:收集新增与已有的菜单的数据 -->
    <el-form>
      <el-form-item label="名称">
        <el-input
          placeholder="请你输入菜单名称"
          v-model="menuData.acmeName"
        ></el-input>
      </el-form-item>
      <el-form-item label="权限">
        <el-input
          placeholder="请你输入权限数值"
          v-model="menuData.acmeCode"
        ></el-input>
      </el-form-item>
      <el-form-item label="分类">
        <el-radio-group v-model="menuData.acmeType" class="ml-4">
          <el-radio :label="1" size="large">菜单</el-radio>
          <el-radio :label="2" size="large">按钮</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="排序">
        <el-input-number v-model="menuData.acmeSort" :min="1" :max="10" />
      </el-form-item>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="confrim">确定</el-button>
      </span>
    </template>
  </el-dialog>
</template>

<script setup lang="ts">
// 引入部分
//@ts-ignore
import { ElMessage } from 'element-plus'
import { ref, onMounted, reactive } from 'vue'
import type { AclMenu } from '@/api/menu/type'
import { REQ_SUCCESS } from '@/utils/resultStatus'
import type { jsonResult, singleId } from '@/api/common/type'
import {
  selectMenu,
  addMenu,
  updateCurrentMenu,
  deleteMenu,
} from '@/api/menu/index'

// 变量部分
let menuArr = ref<Array<AclMenu>>([])
// 对话框
let dialogVisible = ref<boolean>(false)

// 菜单信息
//@ts-ignore
let menuData = reactive<AclMenu>({
  acmeCode: '',
  acmeId: null,
  acmeName: '',
  acmePid: 0,
  acmeSort: 1,
  acmeType: 1,
  level: null
})

// 查询菜单数据
const getMenus = async () => {
  //@ts-ignore
  menuArr.value.splice(0, menuArr.value.length)
  //@ts-ignore
  let result: jsonResult | any = await selectMenu()
  if (result.status === REQ_SUCCESS) {
    //@ts-ignore
    menuArr.value.push(result.data)
  }
}

// 添加菜单获按钮
const clickAddMenu = (menu: AclMenu) => {
  cleanMenuData()
  dialogVisible.value = true
  menuData.acmePid = menu.acmeId
  //@ts-ignore
  menuData.level = menu.level + 1
}

// 编辑菜单按钮
const updateMenu = (menu: AclMenu) => {
  cleanMenuData()
  dialogVisible.value = true
  Object.assign(menuData, menu)
  menuData.level = null
  menuData.gmtCreate = null
  menuData.gmtModified = null
}

// 添加/更新
const confrim = async () => {
  let result: jsonResult | any = menuData.acmeId
    ? await updateCurrentMenu(menuData)
    : await addMenu(menuData)
  if (result.status === REQ_SUCCESS) {
    //弹出提示信息
    ElMessage({
      type: 'success',
      message: menuData.acmeId ? '修改成功' : '添加成功',
    })
  } else {
    //添加角色失败
    ElMessage({
      type: 'error',
      message: menuData.acmeId
        ? `修改失败,${result.message}`
        : `添加失败,${result.message}`,
    })
  }
  dialogVisible.value = false
  cleanMenuData()
  getMenus()
}

// 删除节点
const removeMenu = async (id: number) => {

  let data:singleId = {
    id: id
  }
  let result: jsonResult | any = await deleteMenu(data)
  if (result.status === REQ_SUCCESS) {
    //弹出提示信息
    ElMessage({
      type: 'success',
      message: '删除成功',
    })
  } else {
    //添加角色失败
    ElMessage({
      type: 'error',
      message: '删除失败',
    })
  }
  getMenus()
}

//组件挂载完毕钩子---发一次请求,获取第一页、一页三个已有角色数据
onMounted(() => {
  getMenus()
})

// 清空菜单数据
const cleanMenuData = () => {
  menuData.acmeCode = ''
  menuData.acmeId = null
  menuData.acmeName = ''
  menuData.acmePid = 0
  menuData.acmeSort = 1
  menuData.acmeType = 1
  menuData.level = null
}
</script>

<style scoped></style>
