<template>
  <div>

    <div style="margin: 10px 0">
      <el-input prefix-icon="el-icon-user-solid" style="width :200px; margin-right: 5px" placeholder="请输入名称"
                v-model="name"></el-input>
      <el-input prefix-icon="el-icon-edit-outline" style="width :200px; margin-right: 5px" placeholder="请输入描述"
                v-model="description"></el-input>
      <el-button style="margin-left: 10px" type="primary" @click="load">搜索</el-button>
      <el-button style="margin-left: 10px" type="info" @click="reReset">重置</el-button>
    </div>
    <div style="margin:  10px">
      <el-button type="success" @click="addUser">新增<i class="el-icon-circle-plus-outline "
                                                        style="padding-left: 3px"></i></el-button>
      <el-popconfirm
          confirm-button-text='确认'
          cancel-button-text='取消'
          icon="el-icon-delete"
          icon-color="red"
          title="确认删除？"
          @confirm="delBatch"
      >
        <el-button type="danger" slot="reference" style="margin-left: 5px">批量删除<i class="el-icon-remove-outline "
                                                                                      style="padding-left: 3px"></i>
        </el-button>
      </el-popconfirm>
      <el-upload action="http://localhost:9090/role/import" :show-file-list="false" accept="xlsx"
                 :on-success="handleExcelImportSuccess" style="display: inline-block">
        <el-button type="primary" style="margin-left: 5px">导入 <i class="el-icon-bottom"></i></el-button>
      </el-upload>
      <el-button type="primary" @click="exp" style="margin-left: 5px">导出 <i class="el-icon-top"></i></el-button>

    </div>
    <el-table :data="tableData" border stripe header-cell-class-name="headerClass"
              @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="40"></el-table-column>
      <el-table-column prop="id" align="center" label="ID" width="80"></el-table-column>
      <el-table-column prop="name" align="center" label="名称" width="150"></el-table-column>
      <el-table-column prop="description" align="center" label="描述" width="200"></el-table-column>
     <el-table-column label="操作" align="center">
        <template slot-scope="scope">
          <el-popconfirm
              confirm-button-text='确认'
              cancel-button-text='取消'
              icon="el-icon-delete"
              icon-color="red"
              title="确认删除该角色？"
              @confirm="del(scope.row.id)"
          >
            <el-button icon="el-icon-delete" type="danger" slot="reference" circle style="margin-right: 3px">
              删除
            </el-button>

          </el-popconfirm>
          <el-button icon="el-icon-edit" type="warning" @click="editUser(scope.row)" circle>编辑</el-button>
        </template>
      </el-table-column>
    </el-table>
    <div style="padding: 10px 0">
      <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="pageNum"
          :page-sizes="[2, 5, 10, 20]"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total">//绑定数据（pageNum,Size前端传，total后台数据）
      </el-pagination>
    </div>

    <el-dialog title="角色信息" :visible.sync="dialogFormVisible">
      <el-form label-width="60px">
        <el-form-item label="名称">
          <el-input v-model="form.name" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="form.description" autocomplete="off"></el-input>
        </el-form-item>

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </div>
    </el-dialog>
    <el-dialog title="修改角色信息" :visible.sync="dialogFormVisibleEdit" @close="load()">
      <el-form label-width="100px">
        <el-form-item label="名称">
          <el-input v-model="form.name" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="form.description" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisibleEdit = false;load()">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>


export default {
  name: 'Role',

  data() {

    return {

      tableData: [],
      total: 0,
      pageNum: 1,//默认
      pageSize: 5,
     name: '',//这里一定要记得返回v-model中绑定的值！
      description: '',//这里一定要记得返回v-model中绑定的值！
      dialogFormVisible: false,
      dialogFormVisibleEdit: false,
      form: {},
      multipleSelection: []
    }
  },
  created() {
    //请求分页查询数据
    this.load();
  },
  methods: {

    load() {

      this.request.get("/role/page", {

        params: {
          pageNum: this.pageNum,//请求数据
          pageSize: this.pageSize,
          name: this.name,
          description: this.description,
        }
      }).then((res) => {
        console.log(res.data)
        this.tableData = res.data.records
        this.total = res.data.total

      })
    },
    reReset() {//搜索重置
      this.name = "";
      this.description = "";
      this.load();
    },
    handleSizeChange(pageSize) {//获取数据
      console.log(`每页 ${pageSize} 条`);
      this.pageSize = pageSize
      this.load()//请求数据并更新
    },
    handleCurrentChange(pageNum) {
      console.log(`第${pageNum} 页 `);
      this.pageNum = pageNum;
      this.load()
    },
    addUser() {//点击新增用户按钮
      this.dialogFormVisible = true
      this.form = {}
    },
    save() {//保存新增用户
      this.request.post("/user/", this.form).then(res => {
        this.load()
        if (res.code==='200') {
          this.$message.success("保存成功")//提示
          this.dialogFormVisible = false
          this.dialogFormVisibleEdit = false

        } else {
          this.$message.error("保存失败")
          this.dialogFormVisible = false
          this.dialogFormVisibleEdit = false
        }
      })

    },
    editUser(rom) {//编辑用户信息
      this.form = rom//赋予数据到弹窗格
      this.dialogFormVisibleEdit = true
    },
    del(id) {
      this.request.delete("/role/" + id).then(res => {
        if (res.code==='200') {
          this.$message.success("删除成功")//提示
          this.load()
        } else {
          this.$message.success("删除失败")
          this.dialogFormVisible = false
        }
      })
    },
    handleSelectionChange(val) {//多选框
      console.log(val)
      this.multipleSelection = val
    },
    delBatch() {
      let ids = this.multipleSelection.map(v => v.id)//扁平化处理，将一个对象数组变为纯id数组
      this.request.post("/role/del/batch", ids).then(res => {
        this.load()
        if (res.code==='200') {
          this.$message.success("批量删除成功")//提示

        } else {
          this.$message.error("批量删除失败")
        }
      })
    },
    exp() {
      window.open("http://localhost:9090/role/export")
    },
    handleExcelImportSuccess() {
      this.$message.success("导入成功")
      this.load()
    }
  }

}

</script>

<style>
.headerClass {
  background: rgba(214, 215, 217, 0.99) !important;
}
</style>
