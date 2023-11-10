//@ts-ignore
import SvgIcon from '@/components/SvgIcon/index.vue'
import type { App, Component } from 'vue'
// 引入全部的图标组件
import * as ElementPlusIconsVue from '@element-plus/icons-vue'


// 全部对象
const gloablComponent = { SvgIcon }
const components: { [name: string]: Component } = gloablComponent
export default {
  install(app: App) {
    Object.keys(components).forEach((key: string) => {
      app.component(key, components[key])
    })
    // 注入全部的element-plus图标
    for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
      app.component(key, component)
    }
  },
}
