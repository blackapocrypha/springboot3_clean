<template>
  <div>
    <el-card class="box-card">
      <!-- 卡片顶部添加按钮 -->
      <el-button
        type="primary"
        size="default"
        icon="Plus"
        @click="addData"
      >
        添加
      </el-button>
      <!-- 表格组件：用于展示已有得平台数据 -->
      <el-table
        v-loading="loading"
        style="margin: 10px 0px"
        border
        :height="formHeight"
        :data="dataArr"
      >
        <el-table-column
          label="序号"
          width="80px"
          align="center"
          type="index"
        ></el-table-column>
        <!-- table-column:默认展示数据用div -->
        <el-table-column label="接口地址"  prop="acwlPath"></el-table-column>
       
        <el-table-column label="操作">
          <template #="{ row}">    
            <el-button
              type="primary"
              size="small"
              icon="Edit"
              @click="updateCurrentData(row)"
            >
              更新
            </el-button>
            <el-popconfirm
              :title="`您确定要删除?`"
              width="250px"
              icon="Delete"
              
              @confirm="deleteCurrentData(row.acwlId)"
            >
              <template #reference>
                <el-button type="danger" size="small" :disabled="row.acwlLock" icon="Delete">
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

    <!-- 对话框组件:在添加与修改已有的业务时候使用结构 -->
    <!-- 
            v-model: 属性用户控制对话框的显示与隐藏的 true显示 false隐藏
             title:  设置对话框左上角标题
     -->
    <el-dialog
      v-model="dialogFormVisible"
      :title="singneDataParams.roleId ? '修改' : '添加'"
    >
      <el-form
        style="width: 80%"
        :model="singneDataParams"
        :rules="rules"
        ref="formRef"
      >
        <el-form-item label="接口地址" label-width="100px" prop="acwlPath">
          <el-input
            placeholder="请您输入接口地址"
            v-model="singneDataParams.acwlPath"
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

 
  </div>
</template>

<script setup lang="ts">
//@ts-ignore
import { ElMessage } from 'element-plus'
//引入组合式API函数ref
import { ref, onMounted, reactive, nextTick } from 'vue'
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
let formHeight = window.innerHeight * 0.61
import type { jsonResult, singleId } from '@/api/common/type'
/******  以上是通用导入部分  ******/


import {
  selectWhite,
  updateWhite,
  deleteWhite,
  insertWhite,
} from '@/api/white/index'
import type { ACLWhiteList } from '@/api/white/type'
//存储已有的数据
// @ts-ignore
let dataArr = ref<ACLWhiteList>([])
//控制对话框显示与隐藏
let dialogFormVisible = ref<boolean>(false)
//加载中动画
const loading = ref(true)
//定义收集新增数据
let singneDataParams = reactive<ACLWhiteList>({
  acwlId: null,
  acwlPath: ''
})
//获取el-form组件实例
let formRef = ref()



// 查询
const getData = async (pager = 1) => {
  loading.value = true
  pageNo.value = pager
  let pages: any = {
    page: pageNo.value,
    limit: limit.value,
  }
  let result: jsonResult | any = await selectWhite(pages)
  if (result.status === REQ_SUCCESS) {
    total.value = result.totalsize
    dataArr.value = result.data
  }
  loading.value = false
}

//当下拉菜单发生变化的时候触发次方法
//这个自定义事件,分页器组件会将下拉菜单选中数据返回
const sizeChange = () => {
  //当前每一页的数据量发生变化的时候，当前页码归1
  getData()
}



// 更新
const updateCurrentData = (row: ACLWhiteList) => {
  //清空校验规则错误提示信息
  nextTick(() => {
    formRef.value.clearValidate('acwlPath')
  })
  //对话框显示
  dialogFormVisible.value = true

  singneDataParams.acwlId = row.acwlId
  //@ts-ignore
  singneDataParams.acwlPath = row.acwlPath
  // Object.assign(singneDataParams, row);
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

  let result: jsonResult | any = singneDataParams.acwlId
    ? await updateWhite(singneDataParams)
    : await insertWhite(singneDataParams)
  //添加|修改已有
  if (result.status === REQ_SUCCESS) {
    //关闭对话框
    dialogFormVisible.value = false
    //弹出提示信息
    ElMessage({
      type: 'success',
      message: singneDataParams.acwlId ? '修改成功' : '添加成功',
    })
    //再次发请求获取已有全部的数据
    if (singneDataParams.acwlId) {
      pageNo.value = 1
    }
    getData()
  } else {
    //添加失败
    ElMessage({
      type: 'error',
      message: singneDataParams.roleId
        ? `修改失败,${result.message}`
        : '添加失败,${result.message}',
    })
    //关闭对话框
    dialogFormVisible.value = false
  }
}

//添加按钮的回调
const addData = () => {
  //对话框显示
  dialogFormVisible.value = true
  //清空收集数据
  singneDataParams.acwlPath = ''
  //第一种写法:ts的问号语法
  // formRef.value?.clearValidate('tmName');
  // formRef.value?.clearValidate('logoUrl');
  nextTick(() => {
    formRef.value.clearValidate('acwlPath')
  })
}

//气泡确认框确定按钮的回调
const deleteCurrentData = async (id: number) => {
  let data: singleId = {
    id: id,
  }
  let result: jsonResult | any = await deleteWhite(data)
  if (result.status === REQ_SUCCESS) {
    //删除成功提示信息
    ElMessage({
      type: 'success',
      message: '删除成功',
    })
  
    getData()
  } else {
    ElMessage({
      type: 'error',
      message: `删除失败,${result.message}`,
    })
  }
}





//组件挂载完毕钩子---发一次请求,获取第一页、一页三个已有数据
onMounted(() => {
  getData()
})

//表单校验规则对象
const rules = {
  acwlPath: [{ required: true, trigger: 'blur', message: '接口地址不能为空' }]
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
