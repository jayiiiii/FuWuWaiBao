<template>

  <div class="wrapper">
    <div
        style="margin: 200px auto; background-color: #fff; width: 350px; height: 300px; padding: 20px; border-radius: 10px">
      <div style="margin: 20px 0; text-align: center; font-size: 24px"><b>登 录</b></div>
      <el-form :model="staff" :rules="rules" ref="staffForm">
        <el-form-item prop="email">
          <el-input size="medium" style="margin: 10px 0" prefix-icon="el-icon-user" v-model="staff.email"></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input size="medium" style="margin: 10px 0" prefix-icon="el-icon-lock" show-password
                    v-model="staff.password"></el-input>
        </el-form-item>
        <el-form-item style="margin: 10px 0; text-align: right">
          <Vcode :show="isShow" @success="success" @close="close"/>

          <el-button type="primary" size="small" autocomplete="off" @click=" submit()">登录</el-button>
          <el-button type="warning" size="small" autocomplete="off" @click="$router.push('/register')">注册</el-button>
        </el-form-item>
      </el-form>

    </div>

  </div>
</template>

<script>

import Vcode from "vue-puzzle-vcode";


export default {
  name: "Login",
  data() {
    return {
      isShow: false,
      staff: {
        email: '',
        password: ''

      },
      rules: {
        email: [
          {required: true, message: '请输入登录名', trigger: 'blur'},
          {min: 1, max: 20, message: '长度在1 到 10个字符', trigger: 'blur'}
        ],
        password: [
          {required: true, message: '请输入密码', trigger: 'blur'},
          {min: 1, max: 20, message: '长度在 1 到 20 个字符', trigger: 'blur'}
        ],
      }
    }
  },
  components: {
    Vcode
  },
  methods: {
    submit() {
      this.isShow = true;
       },
    login() {
      this.$refs['staffForm'].validate((valid) => {
        if (valid) {  // 表单校验合法

          this.request.post("/staff/login", this.staff).then(res => {
            if (res.code === '200') {
              localStorage.setItem("staff", JSON.stringify(res.data))//获取信息到用户浏览器
              this.$router.push("/home")
              this.$message.success("登陆成功")

            } else {
              this.$message.error(res.msg)
            }
          })
        }
      });


    },
    success(msg) {
      this.isShow = false; // 通过验证后，需要手动隐藏模态框
      this.login()
    },
    // 用户点击遮罩层，应该关闭模态框
    close() {
      this.isShow = false;
    }

  }
}
</script>

<style>
.wrapper {
  height: 100vh; /*视图窗口高度*/
  background-image: linear-gradient(to bottom right, #f2e6ce, #b9dec9);
  overflow: hidden;
}
</style>