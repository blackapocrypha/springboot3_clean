//对外暴露配置路由
export const constantRoute = [
  {
    path: '/login',
    // @ts-ignore
    component: () => import('@/views/login/index.vue'),
    name: 'login', // 命名路由
    meta: {
      hidden: true,
    },
  },
  {
    path: '/',
    // @ts-ignore
    component: () => import('@/layout/index.vue'),
    name: 'layout', // 命名路由,
    redirect:'/home',
    children: [
      {
        path: '/home',
        name: 'home',
        // @ts-ignore
        component: () => import('@/views/home/index.vue'),
        meta: {
          hidden: false,
          icon: 'Grid',
          title: '首页',
        },
      },
    ],
  },
  {
    path: '/screen',
    // @ts-ignore
    component: () => import('@/views/screen/index.vue'),
    name: 'screen', // 命名路由,
    meta: {
      hidden: false,
      icon: 'User',
      title: '大屏界面',
    },
  },
  {
    path: '/404',
    // @ts-ignore
    component: () => import('@/views/404/index.vue'),
    name: '404', // 命名路由
    meta: {
      hidden: true,
    },
  },
]

// 异步路由
export const asyncRoute = [
  {
    path: '/system',
    name: 'System', // 命名路由,
    meta: {
      hidden: false,
      icon: 'Setting',
      title: '系统管理',
    },
        // @ts-ignore
    component: () => import('@/layout/index.vue'),
    children: [
      {
        path: '/system/user',
        name: 'AclUser',
        // @ts-ignore
        component: () => import('@/views/system/user/index.vue'),
        meta: {
          hidden: false,
          icon: 'User',
          title: '用户管理',
        },
      },
      {
        path: '/system/role',
        name: 'AclRole',
        // @ts-ignore
        component: () => import('@/views/system/role/index.vue'),
        meta: {
          hidden: false,
          icon: 'Coin',
          title: '角色管理',
        },
      },
      {
        path: '/system/interface',
        name: 'AclInterface',
        // @ts-ignore
        component: () => import('@/views/system/interface/index.vue'),
        meta: {
          hidden: false,
          icon: 'Promotion',
          title: '接口管理',
        },
      },
      {
        path: '/system/menu',
        name: 'AclMenu',
        // @ts-ignore
        component: () => import('@/views/system/menu/index.vue'),
        meta: {
          hidden: false,
          icon: 'Fold',
          title: '菜单管理',
        },
      },
      {
        path: '/system/logs',
        name: 'Logs',
        // @ts-ignore
        component: () => import('@/views/system/logs/index.vue'),
        meta: {
          hidden: false,
          icon: 'Tickets',
          title: '日志管理',
        },
      },
      {
        path: '/system/white',
        name: 'White',
        // @ts-ignore
        component: () => import('@/views/system/white/index.vue'),
        meta: {
          hidden: false,
          icon: 'SetUp',
          title: '白名单管理',
        },
      }
    ],
  },
]

// 任意路由
export const anyRoute = 
  {
    path: '/:pathMatch(.*)*', //任意路由
    redirect: '/404',
    name: 'any',
    meta: {
      hidden: true,
    },
  }
