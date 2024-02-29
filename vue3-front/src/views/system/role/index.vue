<template>
  <div>
    <el-card class="box-card">
      <!-- 卡片顶部添加按钮 -->
      <el-button
        type="primary"
        size="default"
        icon="Plus"
        @click="addRole"
        v-has="`btn.ACLRole.add`"
      >
        添加角色
      </el-button>
      <!-- 表格组件：用于展示已有得平台数据 -->
      <el-table
        v-loading="loading"
        style="margin: 10px 0px"
        border
        :height="formHeight"
        :data="roleArr"
      >
        <el-table-column
          label="序号"
          width="80px"
          align="center"
          type="index"
        ></el-table-column>
        <!-- table-column:默认展示数据用div -->
        <el-table-column label="角色名称" width="100px" prop="roleName"></el-table-column>
        <el-table-column label="角色代码" width="160px" prop="roleCode"></el-table-column>
        <el-table-column
          label="创建时间"
          prop="gmtCreate"
          width="180px"
          show-overflow-tooltip
        ></el-table-column>
        <el-table-column label="操作">
          <template #="{ row}">
            <el-button
              type="success"
              size="small"
              icon="Coin"
              @click="showMenuConfig(row.roleId)"
            >
              配置菜单
            </el-button>
            <el-button
              type="success"
              size="small"
              icon="Promotion"
              @click="showInterfaceConfig(row.roleId)"
            >
              配置接口
            </el-button>
            <el-button
              type="primary"
              size="small"
              icon="Edit"
              @click="updateCurrentRole(row)"
            >
              更新
            </el-button>
            <el-popconfirm
              :title="`您确定要删除${row.roleName}?`"
              width="250px"
              icon="Delete"
              @confirm="deleteCurrentRole(row.roleId)"
            >
              <template #reference>
                <el-button type="danger" size="small" icon="Delete">
                  删除
                </el-button>
              </template>
            </el-popconfirm>
          </template>
        </el-table-column>
      </el-table>
      <!-- 分页器组件
                pagination
                   v-model:current-page:设置分页器当前页码
                   v-model:page-size:设置每一个展示数据条数
                   page-sizes:用于设置下拉菜单数据
                   background:设置分页器按钮的背景颜色
                   layout:可以设置分页器六个子组件布局调整
            -->
      <el-pagination
        @size-change="sizeChange"
        @current-change="getRoles"
        :pager-count="9"
        v-model:current-page="pageNo"
        v-model:page-size="limit"
        :page-sizes="[20, 50, 100]"
        :background="true"
        layout="prev, pager, next, jumper,->,sizes,total"
        :total="total"
      />
    </el-card>

    <!-- 对话框组件:在添加角色与修改已有角色的业务时候使用结构 -->
    <!-- 
            v-model: 属性用户控制对话框的显示与隐藏的 true显示 false隐藏
             title:  设置对话框左上角标题
     -->
    <el-dialog
      v-model="dialogFormVisible"
      :title="roleParams.roleId ? '修改角色' : '添加角色'"
    >
      <el-form
        style="width: 80%"
        :model="roleParams"
        :rules="rules"
        ref="formRef"
      >
        <el-form-item label="角色名称" label-width="100px" prop="roleName">
          <el-input
            placeholder="请您输入角色名称"
            v-model="roleParams.roleName"
          ></el-input>
        </el-form-item>
        <el-form-item label="角色代码" label-width="100px" prop="roleCode">
          <el-input
            placeholder="请您输入角色代码"
            v-model="roleParams.roleCode"
          ></el-input>
        </el-form-item>
      </el-form>
      <!-- 具名插槽:footer -->
      <template #footer>
        <el-button type="primary" size="default" @click="cancel">
          取消
        </el-button>
        <el-button type="primary" size="default" @click="confirm">
          确定
        </el-button>
      </template>
    </el-dialog>

    <!-- 抽屉，配置权限 -->
    <el-drawer v-model="drawer" @closed="showTree = false">
      <template #header>
        <h4>分配菜单与按钮的权限</h4>
      </template>
      <template #default>
        <!-- 树形控件 -->
        <el-tree
          v-if="showTree"
          ref="tree"
          :data="menuArr"
          show-checkbox
          node-key="acmeId"
          default-expand-all
          :default-checked-keys="selectArr"
          :props="defaultProps"
        />
      </template>
      <template #footer>
        <div style="flex: auto">
          <el-button @click="cancleDrawer">取消</el-button>
          <el-button type="primary" @click="handleRoleMenu">确定</el-button>
        </div>
      </template>
    </el-drawer>

    <el-drawer v-model="interfaceDrawer" @closed="showInterfaceTree = false">
      <template #header>
        <h4>分配接口的权限</h4>
      </template>
      <template #default>
        <!-- 树形控件 -->
        <el-tree
          v-if="showInterfaceTree"
          ref="tree"
          :data="interfaceArr"
          show-checkbox
          node-key="acpaId"
          default-expand-all
          :default-checked-keys="selectArr"
          :props="interfaceProps"
        />
      </template>
      <template #footer>
        <div style="flex: auto">
          <el-button @click="cancleInterfaceDrawer">取消</el-button>
          <el-button type="primary" @click="handleRoleInterface">确定</el-button>
        </div>
      </template>
    </el-drawer>
  </div>
</template>

<script setup lang="ts">
//@ts-ignore
import { ElMessage } from 'element-plus'
//引入组合式API函数ref
import { ref, onMounted, reactive, nextTick } from 'vue'
import type { ACLRole, rolePage, roleRequest } from '@/api/role/type'
import type { jsonResult, singleId } from '@/api/common/type'
import { REQ_SUCCESS } from '@/utils/resultStatus'
//引入用户相关的小仓库
import useUserStore from '@/store/modules/user'
//@ts-ignore
let userStore = useUserStore()
//当前页码
let pageNo = ref<number>(1)
//每一页展示多少条数据
let limit = ref<number>(20)
//存储已有数据总数
let total = ref<number>(0)
//表单高度
let formHeight = window.innerHeight * 0.7
// ********** 以上为通用部分 **********

import type { AclMenu } from '@/api/menu/type'
import type { ACLPath } from '@/api/interface/type'
import {
  selectRole,
  updateRole,
  deleteRole,
  insertRole,
} from '@/api/role/index'
import {
  selectMenu,
  selectMenuByRoleId,
  updateRoleMenu

} from '@/api/menu/index'
import { selectInterface, selectPathByRoleId,  updatePathByRole } from '@/api/interface/index'


//存储已有的数据
// @ts-ignore
let roleArr = ref<ACLRole>([])
//控制对话框显示与隐藏
let dialogFormVisible = ref<boolean>(false)
//控制抽屉组件显示与隐藏
let drawer = ref<boolean>(false)
let interfaceDrawer = ref<boolean>(false)
let showTree = ref<boolean>(false)
let showInterfaceTree = ref<boolean>(false)
// 当前选中的角色id
let currentRoleId = ref<number | null>(null)
//加载中动画
const loading = ref(true)
//定义收集新增角色数据
let roleParams = reactive<roleRequest>({
  roleId: null,
  roleName: '',
  roleCode: '',
})
// 当前选中的菜单id
let currentMenuIdArr: any = []
// 当前选中的接口Id
let currentInterfaceIdArr: any = []
//@ts-ignore  菜单数组
let menuArr = ref<AclMenu>([])
//@ts-ignore  接口数组
let interfaceArr = ref<ACLPath>([])
//准备一个数组:数组用于存储勾选的节点的ID
let selectArr = ref<number[]>([])
//获取el-form组件实例
let formRef = ref()
//获取tree组件实例
let tree = ref<any>()


//获取已有角色的接口封装为一个函数:在任何情况下向获取数据,调用次函数即可
const getRoles = async (pager = 1) => {
  loading.value = true
  //当前页码
  pageNo.value = pager
  let pages: rolePage = {
    page: pageNo.value,
    limit: limit.value,
    orderItem: 'gmt_create',
    orderType: 'desc',
  }
  let result: jsonResult | any = await selectRole(pages)
  if (result.status === REQ_SUCCESS) {
    //存储已有角色总个数
    total.value = result.totalsize
    roleArr.value = result.data
  }
  loading.value = false
}

//当下拉菜单发生变化的时候触发次方法
//这个自定义事件,分页器组件会将下拉菜单选中数据返回
const sizeChange = () => {
  //当前每一页的数据量发生变化的时候，当前页码归1
  getRoles()
}


//树形控件的测试数据
const defaultProps = {
  children: 'children',
  label: 'acmeName',
}
const interfaceProps = {
  children: 'children',
  label: 'acpaName',
}

// 更新
const updateCurrentRole = (row: ACLRole) => {
  //清空校验规则错误提示信息
  nextTick(() => {
    formRef.value.clearValidate('roleCode')
    formRef.value.clearValidate('roleName')
  })
  //对话框显示
  dialogFormVisible.value = true

  roleParams.roleId = row.roleId
  //@ts-ignore
  roleParams.roleName = row.roleName
  //@ts-ignore
  roleParams.roleCode = row.roleCode
  // Object.assign(roleParams, row);
}

//对话框底部取消按钮
const cancel = () => {
  //对话框隐藏
  dialogFormVisible.value = false
}

//对话框底部确认按钮
const confirm = async () => {
  //在你发请求之前,要对于整个表单进行校验
  //调用这个方法进行全部表单相校验,如果校验全部通过，在执行后面的语法
  await formRef.value.validate()

  let result: jsonResult | any = roleParams.roleId
    ? await updateRole(roleParams)
    : await insertRole(roleParams)
  //添加|修改已有角色
  if (result.status === REQ_SUCCESS) {
    //关闭对话框
    dialogFormVisible.value = false
    //弹出提示信息
    ElMessage({
      type: 'success',
      message: roleParams.roleId ? '修改成功' : '添加成功',
    })
    //再次发请求获取已有全部的角色数据
    if (roleParams.roleId) {
      pageNo.value = 1
    }
    getRoles()
  } else {
    //添加角色失败
    ElMessage({
      type: 'error',
      message: roleParams.roleId
        ? `修改失败,${result.message}`
        : '添加失败,${result.message}',
    })
    //关闭对话框
    dialogFormVisible.value = false
  }
}

//添加角色按钮的回调
const addRole = () => {
  //对话框显示
  dialogFormVisible.value = true
  //清空收集数据
  roleParams.roleId = null
  roleParams.roleName = ''
  roleParams.roleCode = ''
  //第一种写法:ts的问号语法
  // formRef.value?.clearValidate('tmName');
  // formRef.value?.clearValidate('logoUrl');
  nextTick(() => {
    formRef.value.clearValidate('roleId')
    formRef.value.clearValidate('roleName')
    formRef.value.clearValidate('roleCode')
  })
}

//气泡确认框确定按钮的回调
const deleteCurrentRole = async (id: number) => {
  let data: singleId = {
    id: id,
  }
  let result: jsonResult | any = await deleteRole(data)
  if (result.status === REQ_SUCCESS) {
    //删除成功提示信息
    ElMessage({
      type: 'success',
      message: '删除成功',
    })
    //再次获取已有的角色数据
    getRoles()
  } else {
    ElMessage({
      type: 'error',
      message: `删除失败,${result.message}`,
    })
  }
}

// 查询菜单数据
const getMenus = async () => {
  let result: jsonResult | any = await selectMenu(null)
  if (result.status === REQ_SUCCESS) {
    // @ts-ignore
    menuArr.value.push(result.data)
  }
}

// 查询接口数据
const getPath = async () => {
  let result: jsonResult | any = await selectInterface(null)
  if (result.status === REQ_SUCCESS) {
    // @ts-ignore
    interfaceArr.value.push(result.data)
  }
}

// 取消展示菜单抽屉
const cancleDrawer = () => {
  drawer.value = false
  showTree.value = false
  currentRoleId.value = null
  selectArr.value.splice(0, selectArr.value.length)
  currentMenuIdArr.splice(0, currentMenuIdArr.length)
}

// 取消展示接口抽屉
const cancleInterfaceDrawer = ()=>{
  interfaceDrawer.value = false
  showInterfaceTree.value = false
  currentRoleId.value = null
  selectArr.value.splice(0, selectArr.value.length)
  currentInterfaceIdArr.splice(0, currentInterfaceIdArr.length)
}

// 展示菜单
const showMenuConfig = async (id: number) => {
  drawer.value = true
  showTree.value = true
  currentRoleId.value = id
  let data = {
    id: id,
  }
  currentMenuIdArr.splice(0, currentMenuIdArr.length)
  selectArr.value.splice(0, selectArr.value.length)
  let result: jsonResult | any = await selectMenuByRoleId(data)
  if (result.status === REQ_SUCCESS) {
    selectArr.value = result.data.map((m: any) => m.acmeId)
    currentMenuIdArr = result.data.map((m: any) => m.acmeId)
  } else {
    selectArr.value.splice(0, selectArr.value.length)
    currentMenuIdArr.splice(0, currentMenuIdArr.length)
  }
}

// 展示接口配置菜单
const showInterfaceConfig = async (id: number) => {
  interfaceDrawer.value = true
  showInterfaceTree.value = true
  currentRoleId.value = id
  let data = {
    id: id,
  }
  currentInterfaceIdArr.splice(0, currentInterfaceIdArr.length)
  selectArr.value.splice(0, selectArr.value.length)
  let result: jsonResult | any = await selectPathByRoleId(data)
  if (result.status === REQ_SUCCESS) {
    selectArr.value = result.data.map((m: any) => m.acpaId)
    currentInterfaceIdArr = result.data.map((m: any) => m.acpaId)
  } else {
    selectArr.value.splice(0, selectArr.value.length)
    currentInterfaceIdArr.splice(0, currentInterfaceIdArr.length)
  }
}

// 更新角色菜单
const handleRoleMenu = async () => {
  let data = {
    menuIds: tree.value
      .getCheckedKeys()
      .map((m: any) => m)
      .join(','),
      acmrRoleId: currentRoleId.value,
  }
  let result: jsonResult | any = await updateRoleMenu(data)
  if (result.status === REQ_SUCCESS) {
    cancleDrawer()
    //弹出提示信息
    ElMessage({
      type: 'success',
      message: '修改成功',
    })
  } else {
    cancleDrawer()
    //添加角色失败
    ElMessage({
      type: 'error',
      message: `修改失败,${result.message}`,
    })
  }
}


// 更新角色接口
const handleRoleInterface = async () => {
  let data = {
    pathIds: tree.value
      .getCheckedKeys()
      .map((m: any) => m)
      .join(','),
    acprRoleId: currentRoleId.value,
  }
  let result: jsonResult | any = await updatePathByRole(data)
  if (result.status === REQ_SUCCESS) {
    cancleInterfaceDrawer()
    //弹出提示信息
    ElMessage({
      type: 'success',
      message: '修改成功',
    })
  } else {
    cancleInterfaceDrawer()
    //添加角色失败
    ElMessage({
      type: 'error',
      message: `修改失败,${result.message}`,
    })
  }
}

//组件挂载完毕钩子---发一次请求,获取第一页、一页三个已有角色数据
onMounted(() => {
  getRoles()
  getMenus()
  getPath()
})

//表单校验规则对象
const rules = {
  roleName: [{ required: true, trigger: 'blur', message: '角色名不能为空' }],
  roleCode: [{ required: true, message: '角色代码不能为空' }],
}
</script>

<style scoped>
.avatar-uploader .avatar {
  width: 178px;
  height: 178px;
  display: block;
}

.fade-enter-from {
  opacity: 0;
  transform: scale(0);
}

.fade-enter-active {
  transition: all 0.1s;
}

.fade-enter-to {
  opacity: 1;
  transform: scale(1);
}
</style>
<style>
.avatar-uploader .el-upload {
  border: 1px dashed var(--el-border-color);
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: var(--el-transition-duration-fast);
}

.avatar-uploader .el-upload:hover {
  border-color: var(--el-color-primary);
}

.el-icon.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  text-align: center;
}
</style>
