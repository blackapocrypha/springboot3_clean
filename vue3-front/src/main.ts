import { createApp } from "vue"
//@ts-ignore
import App from "@/App.vue"
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import gloablComponent from './components/index';
//@ts-ignore
import zhCn from 'element-plus/dist/locale/zh-cn.mjs'
import 'virtual:svg-icons-register'
// 引入模板全局样式
import '@/styles/index.scss'
// 引入路由
import router from './router'
import pinia from './store'
// 引入路由鉴权
import './permission'
// 引入自定义指令
import { isHasButton } from './directive/has'

const app = createApp(App);
isHasButton(app)
app.use(ElementPlus, {
    locale: zhCn
})
app.use(router)
app.use(pinia)
// 自定义插件，注册整个项目定义的全局组件
app.use(gloablComponent);
app.mount("#app")