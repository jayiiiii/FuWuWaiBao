import Vue from 'vue'
import VueRouter from 'vue-router'
import store from "@/store";


Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    component: () => import('../views/Manage.vue'),
    redirect: "/home",
    children: [
      { path: 'home', name: '首页', component: () => import('../views/HomeView.vue')},
      { path: 'user', name: '用户管理', component: () => import('../views/User.vue')},
      { path: 'role', name: '角色管理', component: () => import('../views/Role.vue')},
      { path: 'person', name: '个人信息', component: () => import('../views/Person.vue')},
      { path: 'file', name: '文件 ', component: () => import('../views/Files.vue')},
      { path: 'arrange', name: '排班表 ', component: () => import('../views/Arrange.vue')},
      { path: 'staff', name: '员工管理 ', component: () => import('../views/Staff.vue')},

    ]
  },
  {
    path: '/about',
    name: 'AboutView',
    component: () => import('../views/AboutView.vue')
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/Login.vue')
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('../views/Register.vue')
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

// 路由守卫
router.beforeEach((to, from, next) => {
  localStorage.setItem("currentPathName", to.name)  // 设置当前路由名称，为了在Header组件中去使用
  store.commit("setPath")  // 触发路由更新
  next()  // 放行路由
})

export default router