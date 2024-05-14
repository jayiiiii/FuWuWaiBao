<template>
  <el-card style="width:450px">
    <el-form label-width="70px">

      <el-form-item label="姓名">
        <el-input v-model="form.name" autocomplete="off"></el-input>
      </el-form-item>

      <el-form-item label="邮箱">
        <el-input v-model="form.email" autocomplete="off" readonly=""></el-input>
      </el-form-item>

      <el-form-item label="职位">
        <el-input v-model="form.position" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="工作店铺">
        <el-input v-model="form.store" autocomplete="off"></el-input>
      </el-form-item>
      <div style="text-align: center">
        <el-button type="primary" @click="edit">确认修改</el-button>
      </div>
    </el-form>
  </el-card>

</template>

<script>
import MiddleUtil from "@/utils/MiddleUtil";

export default {
  name: "Person",

  data() {
    return {
      form: {},
      staff: localStorage.getItem("staff") ? JSON.parse(localStorage.getItem("staff")) : {}
    }
  },
  created() {
    this.request.get("/staff/email/" + this.staff.email).then(res => {
      if (res.code === '200') {
        this.form = res.data
        MiddleUtil.$emit("click", this.form.name);
      }
    })
  },
  methods: {
    edit() {//保存用户新信息
      this.request.post("/staff", this.form).then(res => {
        if (res.data) {
          this.$message.success("修改成功")//提示
          this.load();
        } else {
          this.$message.error("修改失败")
        }

      })

    },

  },

}
</script>

<style scoped>

</style>