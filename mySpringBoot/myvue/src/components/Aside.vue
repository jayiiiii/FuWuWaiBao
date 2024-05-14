<template>
  <el-menu :default-openeds="['1', '3']" style="min-height: 100%; overflow-x: hidden"
           background-color="rgb(48, 65, 86)"
           text-color="#fff"
           active-text-color="#ffd04b"
           :collapse-transition="false"
           router
  >
    <div style="height: 60px; line-height: 60px; text-align: center">
      <img src="../assets/logo.png" alt="" style="width: 20px; position: relative; top: 5px; right: 5px">
      <b style="color: white" v-show="logoTextShow">后台管理系统</b>
    </div>
    <el-menu-item index="/home">
      <template slot="title">
        <i class="el-icon-house"></i>
        <span slot="title" v-show="logoTextShow">主页</span>
      </template>
    </el-menu-item>
    <el-submenu index="2">
      <template slot="title">
        <i class="el-icon-menu" ></i>
        <span slot="title" v-show="logoTextShow">系统管理</span>
      </template>
      <el-menu-item index="/user">
        <i class="el-icon-s-custom"></i>
        <span slot="title" >用户管理</span>
      </el-menu-item>
      <el-menu-item index="/role">
        <i class="el-icon-s-custom"></i>
        <span slot="title" >角色管理</span>
      </el-menu-item>
      <el-menu-item index="/file">
        <i class="el-icon-folder"></i>
        <span slot="title" >文件管理</span>
      </el-menu-item>
      <el-menu-item index="/arrange">
        <i class="el-icon-time"></i>
        <span slot="title" >排班表</span>
      </el-menu-item>
      <el-menu-item index="/staff" v-show="managerShow">
        <i class="el-icon-time"></i>
        <span slot="title" >员工管理</span>
      </el-menu-item>
    </el-submenu>
  </el-menu>
</template>

<script>


export default {
  name: "Aside",
  data(){
    return {
      form: {},
      staff:localStorage.getItem("staff")?JSON.parse(localStorage.getItem("staff")):{},
      managerShow:false
    }
  },
  props: {
    collapse: Boolean,
    logoTextShow: Boolean,
  },

  methods:{

  },
 created(){
   this.request.get("/staff/email/" + this.staff.email).then(res => {
     if (res.code === '200') {
       this.form = res.data
       if( this.form.position==='总经理')this.managerShow=true
     }
   })
  }


}
</script>

<style scoped>

</style>