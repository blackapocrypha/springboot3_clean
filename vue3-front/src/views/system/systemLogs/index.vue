<template>
  <el-card class="search-card">
    <el-form :inline="true" class="form">
      <el-form-item label="账号">
        <el-input
          placeholder="请您输入账号"
          @keyup.enter.native="getLogs()"
          v-model="selectParams.slogUserAccountIsLike"
          clearable
        ></el-input>
      </el-form-item>

      <el-form-item class="search-button">
        <el-button type="primary" size="default" @click="getLogs()">
          搜索
        </el-button>
        <el-button size="default" @click="resetSelect()">重置</el-button>
      </el-form-item>
    </el-form>
  </el-card>

  <el-card class="box-card">
    <!-- 表格组件：用于展示已有得平台数据 -->
    <el-table
      v-loading="loading"
      style="margin: 10px 0px; width: 100%"
      border
      tooltip-effect="dark"
      :height="formHeight"
      :data="logsArr"
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
        prop="slogUserAccount"
        width="100px"
        show-overflow-tooltip
      ></el-table-column>
      <el-table-column
        label="请求地址"
        prop="slogRequestUrl"
        width="100px"
        show-overflow-tooltip
      ></el-table-column>
      <el-table-column
        label="端源"
        prop="slogClient"
        width="70px"
        show-overflow-tooltip
      ></el-table-column>
      <el-table-column
        label="IP"
        prop="slogRequestIp"
        width="80px"
        show-overflow-tooltip
      ></el-table-column>
      <el-table-column
        label="请求方法名称"
        prop="slogRequestMethod"
        width="120px"
        show-overflow-tooltip
      ></el-table-column>
      <el-table-column
        label="响应数据"
        prop="slogResponseData"
        show-overflow-tooltip
      ></el-table-column>

      <el-table-column label="创建时间" prop="gmtCreate" show-overflow-tooltip>
        <template #="{ row }">
          {{ row.gmtCreate }}
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
      @current-change="getLogs"
      :pager-count="9"
      v-model:current-page="pageNo"
      v-model:page-size="limit"
      :page-sizes="[20, 50, 100]"
      :background="true"
      layout="prev, pager, next, jumper,->,sizes,total"
      :total="total"
    />
  </el-card>
</template>

<script setup lang="ts">
// @ts-ignore
import { ElMessage } from 'element-plus'
//引入组合式API函数ref
import { ref, onMounted, reactive } from 'vue'
import type { jsonResult } from '@/api/common/type'
import { REQ_SUCCESS } from '@/utils/resultStatus'
//当前页码
let pageNo = ref<number>(1)
//每一页展示多少条数据
let limit = ref<number>(20)
//存储已有数据总数
let total = ref<number>(0)
//表单高度
let formHeight = window.innerHeight * 0.52
// ***** 以上为通用部分 ***** //

import type { SysHistoryLogs } from '@/api/systemLogs/type'
import { selectLogs } from '@/api/systemLogs/index'
//存储已有的数据
// @ts-ignore
let logsArr = ref<SysHistoryLogs>([])
//加载中动画
const loading = ref(true)

// 查询数据
let selectParams = reactive<any>({
  orderItem: 'gmt_create',
  orderType: 'desc',
  slogUserAccountIsLike: '',
  slogClientIsLike: '',
})

//获取日志数据
const getLogs = async (pager = 1) => {
  loading.value = true
  //当前页码
  pageNo.value = pager

  let pages: any = {
    page: pageNo.value,
    limit: limit.value,
  }
  Object.assign(pages, selectParams)

  let result: jsonResult | any = await selectLogs(pages)
  if (result.status === REQ_SUCCESS) {
    //存储总个数
    total.value = result.totalsize
    logsArr.value = result.data
  } else {
    total.value = result.totalsize
    logsArr.value = result.data
  }
  loading.value = false
}

// 重置搜索
const resetSelect = () => {
  selectParams.slogUserAccountIsLike = ''
  selectParams.slogClientIsLike = ''
}

//组件挂载完毕钩子
onMounted(() => {
  getLogs()
})

//当下拉菜单发生变化的时候触发次方法
//这个自定义事件,分页器组件会将下拉菜单选中数据返回
const sizeChange = () => {
  //当前每一页的数据量发生变化的时候，当前页码归1
  getLogs()
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
