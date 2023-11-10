<template>
  <div class="login_container">
    <el-row>
      <el-col :span="12" :xs="0"></el-col>
      <el-col :span="12" :xs="24">
        <el-form
          class="login_form"
          :model="loginForm"
          :rules="rules"
          ref="loginForms"
        >
          <h1>Hello</h1>
          <h2>欢迎登录</h2>
          <el-form-item prop="account">
            <el-input
              :prefix-icon="User"
              v-model="loginForm.account"
            ></el-input>
          </el-form-item>
          <el-form-item prop="password">
            <el-input
              type="password"
              :prefix-icon="Lock"
              v-model="loginForm.password"
              show-password
            ></el-input>
          </el-form-item>
          <el-form-item prop="code">
            <el-input
              v-model="loginForm.code"
              auto-complete="off"
              placeholder="验证码"
              style="width: 67%"
            >
              <svg-icon
                slot="prefix"
                icon-class="validCode"
                class="el-input__icon input-icon"
              />
            </el-input>
            <div class="login-code">
              <img :src="codeUrl" @click="getCodeImg" class="login-code-img" />
            </div>
          </el-form-item>
          <el-form-item>
            <el-button
              :loading="loading"
              class="login_btn"
              type="primary"
              size="default"
              @click="login"
            >
              登录
            </el-button>
          </el-form-item>
        </el-form>
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
import { User, Lock } from '@element-plus/icons-vue'
import { reactive, ref, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import type { jsonResult } from '@/api/common/type'
import { REQ_SUCCESS } from '@/utils/resultStatus'
//@ts-ignore
import { ElNotification } from 'element-plus'
//引入用户相关的小仓库
import useUserStore from '@/store/modules/user'
let useStore = useUserStore()
//引入获取当前时间的函数
import { getTime } from '@/utils/time'
//引入获取验证码接口
import { getCode } from '@/api/user/index'
//获取el-form组件
let loginForms = ref()
//获取路由器
let $router = useRouter()
//路由对象
let $route = useRoute()
//收集账号与密码的数据
let loginForm = reactive({ account: '', password: '', code: '', uuid: '' })
let loading = ref(false)

let codeUrl = ref()

// 登录方法
//登录按钮回调
const login = async () => {
  //保证全部表单相校验通过再发请求
  await loginForms.value.validate()
  //加载效果:开始加载
  loading.value = true
  //点击登录按钮以后干什么?
  //通知仓库发登录请求
  //请求成功->首页展示数据的地方
  //请求失败->弹出登录失败信息
  try {
    //保证登录成功
    await useStore.userLogin(loginForm)
    //编程式导航跳转到展示数据首页
    //判断登录的时候,路由路径当中是否有query参数，如果有就往query参数跳转，没有跳转到首页
    let redirect: any = $route.query.redirect
    $router.push({ path: redirect || '/' })
    //登录成功提示信息
    ElNotification({
      type: 'success',
      message: '欢迎回来',
      title: `HI,${getTime()}好`,
    })
    //登录成功加载效果也消失
    loading.value = false
  } catch (error) {
    //登录失败加载效果消息
    loading.value = false
    getCodeImg()

    //登录失败的提示信息
    ElNotification({
      type: 'error',
      message: (error as Error).message,
    })
  }
}

//自定义校验规则函数
//@ts-ignore
const validatorUserName = (rule: any, value: any, callback: any) => {
  //rule:即为校验规则对象
  //value:即为表单元素文本内容
  //函数:如果符合条件callBack放行通过即为
  //如果不符合条件callBack方法,注入错误提示信息
  if (value.length >= 5) {
    callback()
  } else {
    callback(new Error('账号长度至少五位'))
  }
}

// 获取验证码
const getCodeImg = async () => {
  let result: jsonResult | any = await getCode()
  if (result.status === REQ_SUCCESS) {
    codeUrl.value = result.data.img
    loginForm.uuid = result.message
  }
}

//@ts-ignore
const validatorPassword = (rule: any, value: any, callback: any) => {
  if (value.length >= 6) {
    callback()
  } else {
    callback(new Error('密码长度至少六位'))
  }
}

//定义表单校验需要配置对象
const rules = {
  //规则对象属性:
  //required,代表这个字段务必要校验的
  //min:文本长度至少多少位
  //max:文本长度最多多少位
  //message:错误的提示信息
  //trigger:触发校验表单的时机 change->文本发生变化触发校验,blur:失去焦点的时候触发校验规则
  account: [
    // { required: true, min: 6, max: 10, message: '账号长度至少六位', trigger: 'change' }
    { trigger: 'change', validator: validatorUserName },
  ],
  password: [
    // { required: true, min: 6, max: 15, message: '密码长度至少6位', trigger: 'change' }
    { trigger: 'change', validator: validatorPassword },
  ],
}

//组件挂载完毕钩子---发一次请求,获取第一页、一页三个已有用户数据
onMounted(() => {
  getCodeImg()
})
</script>

<style scoped lang="scss">
.login_container {
  width: 100%;
  height: 100vh;
  background: url('@/assets/imgs/banner.jpg') no-repeat;
  background-size: cover;
  .login_form {
    position: relative;
    width: 60%;
    top: 25vh;
    border: 1px solid transparent;
    border-radius: 11px;
    padding-right: 30px;
    /* 为其整体设置接近透明的效果*/
    background-color: #fff;
    // background-color: rgba(255, 255, 255, 0.4);
    /* 设置box-shadow使其有立体感 */
    box-shadow: 2px 2px 0 0 rgba(0, 0, 0, 0.2);

    left: 195px;
    padding: 40px;

    h1 {
      color: rgb(22, 22, 21);
      font-size: 40px;
    }
    h2 {
      color: rgb(83, 81, 76);
      font-size: 20px;
      margin: 20px 0px;
    }

    .el-input {
      height: 36px;
      line-height: 36px;
    }
    .login_btn {
      width: 100%;
      height: 36px;
    }
  }
  .login-code {
    padding-left: 4%;
    width: 33%;
    height: 36px;

    float: right;
    img {
      height: 36px;
      cursor: pointer;
      vertical-align: middle;
    }
  }
}
</style>
