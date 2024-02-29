<template>
  <el-card class="search-card">
    <el-form :inline="true" class="form">
      <el-form-item label="账号">
        <el-input
          placeholder="请您输入账号"
          @keyup.enter.native="getUsers()"
          v-model="selectParams.accountIsLike"
          clearable
        ></el-input>
      </el-form-item>
      <el-form-item label="姓名">
        <el-input
          @keyup.enter.native="getUsers()"
          placeholder="请您输入姓名"
          v-model="selectParams.realNameIsLike"
          clearable
        ></el-input>
      </el-form-item>
      <el-form-item label="手机号">
        <el-input
          placeholder="请您输入手机号"
          @keyup.enter.native="getUsers()"
          v-model="selectParams.phoneIsLike"
          clearable
        ></el-input>
      </el-form-item>
      <el-form-item class="search-button">
        <el-button type="primary" size="default" @click="getUsers()">
          搜索
        </el-button>
        <el-button size="default" @click="resetSelect()">重置</el-button>
      </el-form-item>
    </el-form>
  </el-card>

  <el-card class="box-card">
    <!-- 卡片顶部添加按钮 -->
    <el-button
      type="primary"
      size="default"
      icon="Plus"
      @click="addUser"
      v-has="`AclUser.add`"
    >
      添加用户
    </el-button>

    <el-button
      type="danger"
      size="default"
      icon="DeleteFilled"
      @click="mutipleDelete"
      v-has="`AclUser.mutipleDelete`"
      :disabled="showMutipleDelete"
    >
      批量删除
    </el-button>

    <el-button
      type="success"
      size="default"
      icon="Download"
      @click="excelDialogVisible = true"
    >
      导入用户
    </el-button>

    <!-- 表格组件：用于展示已有得平台数据 -->
    <el-table
      v-loading="loading"
      style="margin: 10px 0px; width: 100%"
      border
      tooltip-effect="dark"
      :height="formHeight"
      :data="userArr"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55" />
      <el-table-column
        label="序号"
        width="80px"
        align="center"
        type="index"
        v-if="false"
      ></el-table-column>
      <!-- table-column:默认展示数据用div -->
      <el-table-column
        label="账号"
        prop="account"
        width="100px"
        show-overflow-tooltip
      ></el-table-column>
      <el-table-column
        label="姓名"
        prop="realName"
        width="80px"
        show-overflow-tooltip
      ></el-table-column>
      <el-table-column label="头像" prop="userImage" width="140px">
        <template #="{ row }">
          <img
            :src="formatImage(row.userImage)"
            style="width: 100px; height: 100px"
          />
        </template>
      </el-table-column>
      <!-- <el-table-column
        label="昵称"
        prop="nickName"
        width="80px"
        show-overflow-tooltip
      ></el-table-column> -->
      <el-table-column
        label="手机号"
        prop="phone"
        width="120px"
        show-overflow-tooltip
      ></el-table-column>
      <el-table-column label="性别" prop="gender" width="80px">
        <template #="{ row }">
          {{ formatGender(row.gender) }}
        </template>
      </el-table-column>
      <el-table-column label="角色" width="80px" show-overflow-tooltip>
        <template #="{ row }">
          {{ row.myRoles.map((obj: any) => obj.roleName).join(',') }}
        </template>
      </el-table-column>
      <el-table-column
        label="创建时间"
        prop="gmtCreate"
        width="110px"
        show-overflow-tooltip
      >
        <template #="{ row }">
          {{ row.gmtCreate.substring(0, 10) }}
        </template>
      </el-table-column>
      <el-table-column label="操作">
        <template #="{ row }">
          <el-button
            style="margin-left: 10px"
            type="success"
            size="small"
            icon="Coin"
            @click="resetRole(row)"
          >
            分配角色
          </el-button>
          <el-button
            type="info"
            size="small"
            icon="RefreshRight"
            @click="resetPassword(row)"
          >
            重置密码
          </el-button>
          <el-button
            type="primary"
            size="small"
            icon="Edit"
            @click="updateCurrentUser(row)"
          >
            更新
          </el-button>

          <el-popconfirm
            :title="`您确定要删除${row.realName}?`"
            width="250px"
            icon="Delete"
            @confirm="deleteCurrentUser(row.id)"
          >
            <template #reference>
              <el-button
                v-has="`AclUser.delete`"
                size="small"
                icon="Delete"
                type="danger"
              >
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
      @current-change="getUsers"
      :pager-count="9"
      v-model:current-page="pageNo"
      v-model:page-size="limit"
      :page-sizes="[20, 50, 100]"
      :background="true"
      layout="prev, pager, next, jumper,->,sizes,total"
      :total="total"
    />
  </el-card>

  <!-- 对话框组件:在添加用户与修改已有用户的业务时候使用结构 -->
  <el-dialog
    v-model="dialogFormVisible"
    :title="userParams.id ? '修改用户' : '添加用户'"
  >
    <el-form
      style="width: 80%"
      :model="userParams"
      :rules="rules"
      ref="formRef"
    >
      <el-form-item label="账号" label-width="100px" prop="account">
        <el-input
          placeholder="请您输入账号"
          v-model="userParams.account"
        ></el-input>
      </el-form-item>
      <el-form-item
        v-if="!userParams.id"
        label="密码"
        label-width="100px"
        prop="password"
      >
        <el-input
          placeholder="请您输入密码"
          v-model="userParams.password"
        ></el-input>
      </el-form-item>
      <el-form-item label="真实姓名" label-width="100px" prop="realName">
        <el-input
          placeholder="请您输入姓名"
          v-model="userParams.realName"
        ></el-input>
      </el-form-item>
      <el-form-item label="昵称" label-width="100px" prop="nickName">
        <el-input
          placeholder="请您输入昵称"
          v-model="userParams.nickName"
        ></el-input>
      </el-form-item>
      <el-form-item label="手机号" label-width="100px" prop="phone">
        <el-input
          placeholder="请您输入手机号"
          v-model="userParams.phone"
        ></el-input>
      </el-form-item>
      <el-form-item label="性别" label-width="100px" prop="gender">
        <el-radio-group v-model="userParams.gender">
          <el-radio :label="1">男</el-radio>
          <el-radio :label="2">女</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="头像" label-width="100px" prop="userImage">
        <!-- upload组件属性:action图片上传路径书写/api,代理服务器不发送这次post请求  -->
        <el-upload
          class="avatar-uploader"
          :action="BASE_URL_PORT + '/file/upload'"
          :show-file-list="false"
          :on-success="handleAvatarSuccess"
          :before-upload="beforeAvatarUpload"
        >
          <img
            v-if="userParams.userImage"
            :src="formatImage(userParams.userImage)"
            class="avatar"
          />
          <el-icon v-else class="avatar-uploader-icon">
            <Plus />
          </el-icon>
        </el-upload>
      </el-form-item>
    </el-form>
    <!-- 具名插槽:footer -->
    <template #footer>
      <el-button type="primary" size="default" @click="cancel">取消</el-button>
      <el-button type="primary" size="default" @click="confirm">确定</el-button>
    </template>
  </el-dialog>

  <!---excel导入-->
  <el-dialog title="导入" v-model="excelDialogVisible" width="30%">
    <p>
      <el-button type="primary">
        <a
          style="text-decoration: none; color: aliceblue"
          :href="BASE_URL + '/template/user-import.xlsx'"
        >
          下载Excel模板
        </a>
      </el-button>

      <el-upload
        style="display: inline-block; margin-left: 20px"
        class="upload-demo"
        :show-file-list="false"
        :action="BASE_URL_PORT + '/system/acl-user/excelImport'"
        multiple
        :on-success="handleExceed"
      >
        <el-button type="success">导入Excel</el-button>
      </el-upload>
    </p>
  </el-dialog>

  <!-- 重置密码-->
  <el-dialog v-model="resetPasswordFormVisible" title="重置用户密码">
    <el-form
      style="width: 80%"
      :model="userParams"
      :rules="rules"
      ref="formRef"
    >
      <el-form-item label="密码" label-width="100px" prop="password">
        <el-input
          placeholder="请您输入密码"
          v-model="userParams.password"
        ></el-input>
      </el-form-item>
    </el-form>
    <!-- 具名插槽:footer -->
    <template #footer>
      <el-button type="primary" size="default" @click="cancelReset">
        取消
      </el-button>
      <el-button type="primary" size="default" @click="confirmReset">
        确定
      </el-button>
    </template>
  </el-dialog>

  <!-- 分配角色-->
  <el-dialog v-model="resetRoleFormVisible" title="分配用户角色">
    <el-form style="width: 80%" :model="userParams" ref="formRef">
      <el-form-item label="账号" label-width="100px" prop="account">
        <el-input v-model="userParams.account" disabled></el-input>
      </el-form-item>

      <el-form-item label="角色" label-width="100px">
        <el-checkbox-group v-model="checkRoleList">
          <el-checkbox
            v-for="role in allRoles"
            :key="role.roleId"
            :label="role.roleId"
          >
            {{ role.roleName }}
          </el-checkbox>
        </el-checkbox-group>
      </el-form-item>
    </el-form>
    <!-- 具名插槽:footer -->
    <template #footer>
      <el-button type="primary" size="default" @click="cancelResetRole">
        取消
      </el-button>
      <el-button type="primary" size="default" @click="confirmResetRole">
        确定
      </el-button>
    </template>
  </el-dialog>
</template>

<script setup lang="ts">
// @ts-ignore
import { ElMessage, ElMessageBox, UploadProps } from 'element-plus'
import { formatGender, formatImage } from '@/utils/common'
//引入组合式API函数ref
import { ref, onMounted, reactive, nextTick } from 'vue'
import type { jsonResult, singleId } from '@/api/common/type'
import { REQ_SUCCESS } from '@/utils/resultStatus'
import { BASE_URL, BASE_URL_PORT } from '@/utils/common'
//当前页码
let pageNo = ref<number>(1)
//每一页展示多少条数据
let limit = ref<number>(20)
//存储已有数据总数
let total = ref<number>(0)
//表单高度
let formHeight = window.innerHeight * 0.52
// ********** 以上为通用部分 **********

import type { ACLUser } from '@/api/user/type'
import type { ACLRole } from '@/api/role/type'
import {
  selectUser,
  updateUser,
  deleteUser,
  insertUser,
  deleteUserBatch,
} from '@/api/user/index'
import { selectRole } from '@/api/role/index'
import { insertUserRole } from '@/api/userRole/index'
import { UserRole } from '@/api/userRole/type'

// 多选
let multipleSelection = ref()
//存储已有的数据
// @ts-ignore
let userArr = ref<ACLUser>([])
//控制对话框显示与隐藏
let dialogFormVisible = ref<boolean>(false)
let excelDialogVisible = ref<boolean>(false)
let resetPasswordFormVisible = ref<boolean>(false)
let resetRoleFormVisible = ref<boolean>(false)
//加载中动画
const loading = ref(true)
// 是否展示批量删除
const showMutipleDelete = ref(true)
// 查询数据
let selectParams = reactive<any>({
  orderItem: 'gmt_create',
  orderType: 'desc',
  accountIsLike: '',
  realNameIsLike: '',
  nickNameIsLike: '',
  phoneIsLike: '',
})
// 勾选角色
const checkRoleList = ref([])
//定义收集新增用户数据
//@ts-ignore
let userParams = reactive<ACLUser>({
  id: null,
  realName: '',
  password: '',
  account: '',
  phone: '',
  gender: 0,
  nickName: '',
  userImage: '',
})
// 全部的角色信息
let allRoles = ref<Array<ACLRole>>([])
//获取el-form组件实例
let formRef = ref()

//获取已有用户
const getUsers = async (pager = 1) => {
  loading.value = true
  //当前页码
  pageNo.value = pager

  let pages: any = {
    page: pageNo.value,
    limit: limit.value,
  }
  Object.assign(pages, selectParams)

  let result: jsonResult | any = await selectUser(pages)
  if (result.status === REQ_SUCCESS) {
    //存储已有用户总个数
    total.value = result.totalsize
    userArr.value = result.data
  }
  loading.value = false
}

//获取全部的角色信息
const getAllRoles = async () => {
  let pages: any = {
    page: 1,
    limit: 0,
  }
  let result: jsonResult | any = await selectRole(pages)
  if (result.status === REQ_SUCCESS) {
    allRoles.value = result.data
  }
}

//添加用户按钮的回调
const addUser = () => {
  //对话框显示
  dialogFormVisible.value = true
  //清空收集数据
  userParams.id = null
  userParams.account = ''
  userParams.password = ''
  userParams.phone = ''
  userParams.gender = 0
  userParams.nickName = ''
  userParams.userImage = ''
  userParams.realName = ''

  //第一种写法:ts的问号语法
  // formRef.value?.clearValidate('tmName');
  // formRef.value?.clearValidate('logoUrl');
  nextTick(() => {
    formRef.value.clearValidate('id')
    formRef.value.clearValidate('account')
    formRef.value.clearValidate('password')
    formRef.value.clearValidate('phone')
    formRef.value.clearValidate('realName')
    formRef.value.clearValidate('gender')
    formRef.value.clearValidate('nickName')
    formRef.value.clearValidate('userImage')
  })
}

//删除用户
const deleteCurrentUser = async (id: number) => {
  let data: singleId = {
    id: id,
  }
  let result: jsonResult | any = await deleteUser(data)
  if (result.status === REQ_SUCCESS) {
    //删除成功提示信息
    ElMessage({
      type: 'success',
      message: '删除成功',
    })
    //再次获取已有的用户数据
    if (formRef.value == undefined) {
      getUsers(pageNo.value)
    } else {
      getUsers(formRef.value.length > 1 ? pageNo.value : pageNo.value - 1)
    }
  } else {
    ElMessage({
      type: 'error',
      message: result.message,
    })
  }
}

//更新用户
const updateCurrentUser = (row: ACLUser) => {
  //清空校验规则错误提示信息
  nextTick(() => {
    formRef.value.clearValidate('account')
    formRef.value.clearValidate('password')
    formRef.value.clearValidate('phone')
    formRef.value.clearValidate('gender')
    formRef.value.clearValidate('nickName')
    formRef.value.clearValidate('realName')
    formRef.value.clearValidate('userImage')
  })
  //对话框显示
  dialogFormVisible.value = true

  userParams.id = row.id
  userParams.account = row.account
  userParams.password = row.password
  userParams.realName = row.realName
  userParams.phone = row.phone
  userParams.gender = row.gender
  userParams.nickName = row.nickName
  userParams.userImage = row.userImage
  // Object.assign(roleParams, row);
}

//重置密码
const resetPassword = (row: ACLUser) => {
  //清空校验规则错误提示信息
  nextTick(() => {
    formRef.value.clearValidate('account')
    formRef.value.clearValidate('password')
    formRef.value.clearValidate('phone')
    formRef.value.clearValidate('gender')
    formRef.value.clearValidate('nickName')
    formRef.value.clearValidate('realName')
    formRef.value.clearValidate('userImage')
  })
  //对话框显示
  resetPasswordFormVisible.value = true

  userParams.id = row.id
  userParams.account = null
  userParams.password = null
  userParams.realName = null
  userParams.phone = null
  userParams.gender = null
  userParams.nickName = null
  userParams.userImage = null
}

// 处理excel导入
const handleExceed: UploadProps['onSuccess'] = (
  response: any,
  //@ts-ignore
  uploadFile: any,
) => {
  if (response.status === REQ_SUCCESS) {
    excelDialogVisible.value = false
    ElMessage({
      type: 'success',
      message: '导入成功',
    })
  } else {
    excelDialogVisible.value = false
    ElMessageBox.alert(
      '<a href="' + BASE_URL + response.data + '">导入失败的Excel文件</a>',
      '导入错误，详情请下载对应的excel文件',
      {
        dangerouslyUseHTMLString: true,
      },
    )
  }
}

// 分配角色
const resetRole = (row: ACLUser) => {
  // 清空数组
  checkRoleList.value.splice(0, checkRoleList.value.length)
  //对话框显示
  resetRoleFormVisible.value = true

  userParams.id = row.id
  userParams.account = row.account
  userParams.password = null
  userParams.realName = null
  userParams.phone = null
  userParams.gender = null
  userParams.nickName = null
  userParams.userImage = null

  checkRoleList.value.splice(0, checkRoleList.value.length)
  //@ts-ignore
  row.myRoles.forEach(function (r: any) {
    //@ts-ignore
    checkRoleList.value.push(r.roleId)
  })
}

//对话框底部取消按钮
const cancel = () => {
  //对话框隐藏
  dialogFormVisible.value = false
}
const cancelReset = () => {
  resetPasswordFormVisible.value = false
}
const cancelResetRole = () => {
  resetRoleFormVisible.value = false
}
// 多选
const handleSelectionChange = (val: ACLUser[]) => {
  let ids = val.map((obj) => obj.id).join(',')
  multipleSelection.value = ids
  if (multipleSelection.value.length > 0) {
    showMutipleDelete.value = false
  } else {
    showMutipleDelete.value = true
  }
}

// 批量删除
const mutipleDelete = async () => {
  let data = {
    ids: multipleSelection.value,
  }
  let result: jsonResult | any = await deleteUserBatch(data)
  if (result.status === REQ_SUCCESS) {
    //删除成功提示信息
    ElMessage({
      type: 'success',
      message: '删除成功',
    })
    //再次获取已有的用户数据
    getUsers()
    multipleSelection.value = ''
  } else {
    ElMessage({
      type: 'error',
      message: '删除失败',
    })
  }
}

//对话框底部确认按钮
const confirm = async () => {
  //在你发请求之前,要对于整个表单进行校验
  //调用这个方法进行全部表单相校验,如果校验全部通过，在执行后面的语法
  await formRef.value.validate()

  let result: jsonResult | any = userParams.id
    ? await updateUser(userParams)
    : await insertUser(userParams)
  //添加|修改已有用户
  if (result.status === REQ_SUCCESS) {
    //关闭对话框
    dialogFormVisible.value = false
    //弹出提示信息
    ElMessage({
      type: 'success',
      message: userParams.id ? '修改成功' : '添加成功',
    })
    //再次发请求获取已有全部的用户数据
    if (userParams.id) {
      pageNo.value = 1
      window.location.reload()
    }

    getUsers()
  } else {
    //添加角色失败
    ElMessage({
      type: 'error',
      message: userParams.id
        ? `修改失败,${result.message}`
        : `添加失败,${result.message}`,
    })
    //关闭对话框
    dialogFormVisible.value = false
  }
}
const confirmReset = async () => {
  //在你发请求之前,要对于整个表单进行校验
  //调用这个方法进行全部表单相校验,如果校验全部通过，在执行后面的语法
  await formRef.value.validate()

  let result: jsonResult | any = await updateUser(userParams)

  //添加|修改已有用户
  if (result.status === REQ_SUCCESS) {
    //关闭对话框
    resetPasswordFormVisible.value = false
    //弹出提示信息
    ElMessage({
      type: 'success',
      message: '修改成功',
    })
    //再次发请求获取已有全部的用户数据
    pageNo.value = 1
    getUsers()
  } else {
    //操作失败
    ElMessage({
      type: 'error',
      message: `修改失败,${result.message}`,
    })
    //关闭对话框
    resetPasswordFormVisible.value = false
  }
}
const confirmResetRole = async () => {
  if (checkRoleList.value.length <= 0) {
    //操作失败
    ElMessage({
      type: 'error',
      message: '请至少选择一个角色',
    })
    return
  }

  let ids = checkRoleList.value.map((obj) => obj).join(',')
  //@ts-ignore
  let data: UserRole = {
    acurUserId: userParams.id,
    allRoles: ids,
  }
  let result: jsonResult | any = await insertUserRole(data)

  //添加|修改已有用户
  if (result.status === REQ_SUCCESS) {
    //关闭对话框
    resetRoleFormVisible.value = false
    //弹出提示信息
    ElMessage({
      type: 'success',
      message: '修改成功',
    })
    //再次发请求获取已有全部的用户数据
    pageNo.value = 1
    getUsers()
  } else {
    //操作失败
    ElMessage({
      type: 'error',
      message: `修改失败,${result.message}`,
    })
    //关闭对话框
    resetRoleFormVisible.value = false
  }
}

// 重置搜索
const resetSelect = () => {
  selectParams.realNameIsLike = ''
  ;(selectParams.accountIsLike = ''),
    (selectParams.realNameIsLike = ''),
    (selectParams.nickNameIsLike = ''),
    (selectParams.phoneIsLike = '')
}

//上传图片组件->上传图片之前触发的钩子函数
const beforeAvatarUpload: UploadProps['beforeUpload'] = (rawFile: any) => {
  //钩子是在图片上传成功之前触发,上传文件之前可以约束文件类型与大小
  //要求:上传文件格式png|jpg|gif 4M
  if (
    rawFile.type == 'image/png' ||
    rawFile.type == 'image/jpeg' ||
    rawFile.type == 'image/gif'
  ) {
    if (rawFile.size / 1024 / 1024 < 4) {
      return true
    } else {
      ElMessage({
        type: 'error',
        message: '上传文件大小小于4M',
      })
      return false
    }
  } else {
    ElMessage({
      type: 'error',
      message: '上传文件格式务必PNG|JPG|GIF',
    })
    return false
  }
}

//图片上传成功钩子
const handleAvatarSuccess: UploadProps['onSuccess'] = (
  response: any,
  //@ts-ignore
  uploadFile: any,
) => {
  //response:即为当前这次上传图片post请求服务器返回的数据
  //收集上传图片的地址,添加一个新的品牌的时候带给服务器
  userParams.userImage = response.path
  //图片上传成功,清除掉对应图片校验结果
  formRef.value.clearValidate('userImage')
}

//组件挂载完毕钩子---发一次请求,获取第一页、一页三个已有用户数据
onMounted(() => {
  getAllRoles()
  getUsers()
})

//当下拉菜单发生变化的时候触发次方法
//这个自定义事件,分页器组件会将下拉菜单选中数据返回
const sizeChange = () => {
  //当前每一页的数据量发生变化的时候，当前页码归1
  getUsers()
}

//表单校验规则对象
const rules = {
  account: [
    { required: true, trigger: 'blur', message: '账号不能为空' },
    { min: 4, max: 22, message: '长度在4-21个字符之间', trigger: 'blur' },
  ],
  password: [
    { required: true, trigger: 'blur', message: '密码不能为空' },
    { min: 6, max: 22, message: '长度在6-21个字符之间', trigger: 'blur' },
  ],
  realName: [{ required: true, trigger: 'blur', message: '姓名不能为空' }],
  phone: [{ required: true, trigger: 'blur', message: '手机号不能为空' }],
}
</script>

<style scoped>
.search-card {
  margin-bottom: 10px;
}
.avatar-uploader .avatar {
  width: 178px;
  height: 178px;
  display: block;
}

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

.form {
  display: flex;
  align-items: center;
  position: relative;
  height: 50px;

  .el-form-item {
    float: left;
    margin-top: 20px;
  }

  .search-button {
    position: absolute;
    right: 0;
  }
}
</style>
