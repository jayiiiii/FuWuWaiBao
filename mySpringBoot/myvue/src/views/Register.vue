<template>

  <div class="wrapper">
    <div
        style="margin: 10px auto; background-color: #fff; width: 350px; height: 570px; padding: 20px; border-radius: 10px">
      <div style="margin:10px; text-align: center; font-size: 24px"><b>注册</b></div>
      <el-form :model="user" :rules="rules" ref="userForm">
        <el-form-item prop="username"  >
          <el-input placeholder="请输入用户名" size="medium" style="margin: 5px 0" prefix-icon="el-icon-user" v-model="user.username"></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input placeholder="请输入密码" size="medium" style="margin: 5px 0" prefix-icon="el-icon-lock" show-password
                    v-model="user.password"></el-input>
        </el-form-item>
        <el-form-item prop="confirmPassword">
          <el-input placeholder="确认密码" size="medium" style="margin:5px 0" prefix-icon="el-icon-lock" show-password
                    v-model="user.confirmPassword"></el-input>
        </el-form-item>
        <el-form-item prop="nickname">
          <el-input placeholder="请输入昵称" size="medium" style="margin: 5px 0" prefix-icon="el-icon-user"
                    v-model="user.nickname"></el-input>
        </el-form-item>
                 <el-form-item  >
                  <el-input placeholder="请输入邮箱" size="medium" style="margin: 5px 0" prefix-icon="el-icon-message"
                            v-model="user.email"></el-input>
                </el-form-item>
                <el-form-item  >
                  <el-input placeholder="请输入电话" size="medium" style="margin: 5px 0" prefix-icon="el-icon-phone"
                            v-model="user.phone"></el-input>
                </el-form-item>
                <el-form-item  >
                  <el-input placeholder="请输入地址" size="medium" style="margin: 5px 0" prefix-icon="el-icon-location"
                            v-model="user.address"></el-input>
                </el-form-item>
        <el-form-item style="margin: 10px 0; text-align: right">
          <el-button type="primary" size="small" autocomplete="off" @click=" register()">确定</el-button>
          <el-button type="warning" size="small" autocomplete="off" @click=" $router.push('/login')">取消</el-button>
        </el-form-item>
      </el-form>

    </div>

  </div>
</template>

<script>



export default {
  name: "Register",
  data() {
    return {
      user: {},
      rules: {
        username: [
          {required: true, message: '请输入用户名', trigger: 'blur'},
          {min: 1, max: 10, message: '长度在1 到 10个字符', trigger: 'blur'}
        ],
        password: [
          {required: true, message: '请输入密码', trigger: 'blur'},
          {min: 1, max: 20, message: '长度在 1 到 20 个字符', trigger: 'blur'}
        ],
        confirmPassword:[
          {required: true, message: '请确认密码', trigger: 'blur'},
          {min: 1, max: 20, message: '长度在 1 到 20 个字符', trigger: 'blur'}

        ],
        nickname:[
          {required: true, message: '请输入昵称', trigger: 'blur'},
          {min: 1, max: 20, message: '长度在 1 到 20 个字符', trigger: 'blur'}

        ],
      }
    }
  },

  methods: {
    register() {
      this.$refs['userForm'].validate((valid) => {
        if (valid) {  // 表单校验合法
          if(this.user.password!==this.user.confirmPassword){
            this.$message.error("两次密码不一致！")
            return false
          }
          this.request.post("/user/register", this.user).then(res => {
            if (res.code === '200') {
              localStorage.setItem("user",JSON.stringify(res.data))//获取信息到用户浏览器
              this.$router.push("/login")
              this.$message.success("注册成功")
            } else {
              this.$message.error(res.msg)
            }
          })
        }
      });


    },


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