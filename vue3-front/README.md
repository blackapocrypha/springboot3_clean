# Vue 3 + TypeScript + Vite + ElementPlus

参考：https://www.bilibili.com/video/BV1Xh411V7b5

个人用二次开发框架练习。



## 1 安装

```
#安装依赖
pnpm install

#运行
pnpm run dev

#打包
pnpm run build:prod
```



更换后台需要修改 `.env.production` 文件 ,测试环境的文件 `.env.development`同理

```properties
NODE_ENV = 'production'
VITE_APP_TITLE = '正式平台'
VITE_APP_BASE_API = 'http://143.143.210.111:8090'
```

同时需要修改`/src/utils/common.ts`文件

```typescript
// 基础拼接路径
export const BASE_URL = 'http://143.143.210.111'
export const BASE_URL_PORT = 'http://143.143.210.111:8090'

//export const BASE_URL = 'http://127.0.0.1'
//export const BASE_URL_PORT = 'http://127.0.0.1:8090'
```



打包前如果不想语法检查需要在package.json中去掉

```
"scripts": {
"dev": "vite --open",
"build": "vue-tsc && vite build --mode development",
"build:prod": "vite build --mode production", #修改这里
...
},
```

