<template>
  <el-table
    :data="pathArr"
    style="width: 100%; margin-bottom: 20px"
    row-key="acpaId"
    node-key="acpaId"
    :tree-props="{ children: 'children' }"
    border
  >
    <el-table-column label="名称" prop="acpaName">
      <template #default="scope">
        <el-icon v-if="scope.row.acpaType == 0"><Menu /></el-icon>
        <el-icon v-if="scope.row.acpaType == 1"><Promotion /></el-icon>
        {{ scope.row.acpaName }}
      </template>
    </el-table-column>
    <el-table-column label="权限值" prop="acpaPath"></el-table-column>
    <el-table-column label="类型" prop="acpaType" width="70">
      <template #default="scope">
        <el-tag v-if="scope.row.acpaType == 0" class="ml-2" type="success">
          菜单
        </el-tag>
        <el-tag v-if="scope.row.acpaType == 1" class="ml-2" type="info">
          接口
        </el-tag>
      </template>
    </el-table-column>
    <el-table-column label="修改时间" prop="gmtModified"></el-table-column>
    <el-table-column label="操作">
      <!-- row:即为已有的菜单对象|按钮的对象的数据 -->
      <template #="{ row }">
        <el-button @click="clickAddMenu(row)" size="small">
          {{ '添加' }}
        </el-button>
        <el-button type="primary" @click="updateMenu(row)" size="small">
          编辑
        </el-button>
        <el-popconfirm
          :title="`你确定要删除${row.acpaName}?`"
          width="260px"
          @confirm="removeInterface(row.acpaId)"
        >
          <template #reference>
            <el-button
              type="danger" 
              size="small"
              :disabled="row.acpaPid == -1 ? true : false"
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
    :title="interfaceData.acpaId ? '更新菜单' : '添加菜单'"
  >
    <!-- 表单组件:收集新增与已有的菜单的数据 -->
    <el-form>
      <el-form-item label="名称">
        <el-input
          placeholder="请你输入名称"
          v-model="interfaceData.acpaName"
        ></el-input>
      </el-form-item>
      <el-form-item label="路径">
        <el-input
          placeholder="请你输入路径"
          v-model="interfaceData.acpaPath"
        ></el-input>
      </el-form-item>
      <el-form-item label="分类">
        <el-radio-group v-model="interfaceData.acpaType" class="ml-4">
          <el-radio :label="0" size="large">菜单</el-radio>
          <el-radio :label="1" size="large">接口</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="排序">
        <el-input-number v-model="interfaceData.acpaSort" :min="1" :max="10" />
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
// 通用引入部分
//@ts-ignore
import { ElMessage } from 'element-plus'
import { ref, onMounted, reactive } from 'vue'
import { REQ_SUCCESS } from '@/utils/resultStatus'
import type { jsonResult, singleId } from '@/api/common/type'

// 类型
import type { ACLPath } from '@/api/interface/type'
import { selectInterface,addInterface,updateInterface,deleteInterface } from '@/api/interface/index'

// 变量部分
//@ts-ignore
let pathArr = ref<ACLPath>([])
// 对话框
let dialogVisible = ref<boolean>(false)
// 接口信息
//@ts-ignore
let interfaceData = reactive<ACLPath>({
  acpaId: null,
  acpaPath: '',
  acpaName: '',
  acpaPid: null,
  acpaSort: 1,
  acpaType: 1
})

// 查询接口数据
const getInterface = async () => {
  //@ts-ignore
  pathArr.value.splice(0, pathArr.value.length)
  let result: jsonResult | any = await selectInterface(null)
  if (result.status === REQ_SUCCESS) {
    //@ts-ignore
    pathArr.value.push(result.data)
  }
}

// 添加菜单获按钮
const clickAddMenu = (menu: ACLPath) => {
  cleanInterfaceData()
  dialogVisible.value = true
  interfaceData.acpaPid = menu.acpaId
}

// 编辑菜单按钮
const updateMenu = (menu: ACLPath) => {
  cleanInterfaceData()
  dialogVisible.value = true
  interfaceData.acpaId = menu.acpaId
  interfaceData.acpaName = menu.acpaName
  interfaceData.acpaPath = menu.acpaPath
  interfaceData.acpaPid = menu.acpaPid
  interfaceData.acpaSort = menu.acpaSort
  interfaceData.acpaType = menu.acpaType
}


// 删除节点
const removeInterface = async (id: number) => {

let data:singleId = {
  id: id
}
let result: jsonResult | any = await deleteInterface(data)
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
getInterface()
}



// 添加/更新
const confrim = async () => {
  let result: jsonResult | any = interfaceData.acpaId
    ? await updateInterface(interfaceData)
    : await addInterface(interfaceData)
  if (result.status === REQ_SUCCESS) {
    //弹出提示信息
    ElMessage({
      type: 'success',
      message: interfaceData.acpaId ? '修改成功' : '添加成功',
    })
  } else {
    //添加角色失败
    ElMessage({
      type: 'error',
      message: interfaceData.acpaId
        ? `修改失败,${result.message}`
        : `添加失败,${result.message}`,
    })
  }
  dialogVisible.value = false
  cleanInterfaceData()
  getInterface()
}

const cleanInterfaceData = ()=>{
    interfaceData.acpaId = null
    interfaceData.acpaPath = ''
    interfaceData.acpaName = ''
    interfaceData.acpaPid = null
    interfaceData.acpaSort = 1
    interfaceData.acpaType = 1
    interfaceData.acpaId = null
}
//组件挂载完毕钩子---发一次请求,获取第一页、一页三个已有角色数据
onMounted(() => {
  getInterface()
})
</script>

<style scoped></style>
