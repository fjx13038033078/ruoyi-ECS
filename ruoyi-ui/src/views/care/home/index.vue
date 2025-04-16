<script setup>

</script>

<template>
  <div class="app-container">
    <el-row :gutter="20" class="mb-20" style="margin-bottom: 20px;">
      <el-col>
        <el-button type="primary" @click="handleAdd" v-hasPermi="['care:home:add']">新增家庭关系</el-button>
      </el-col>
    </el-row>

    <el-table v-loading="loading" :data="homeRelationList" border style="width: 100%">
      <el-table-column label="关系ID" prop="homeId" align="center" />
      <el-table-column label="老年人姓名" prop="userName" align="center" :show-overflow-tooltip="true" />
      <el-table-column label="家属姓名" prop="dependentsName" align="center" :show-overflow-tooltip="true" />
      <el-table-column label="关系" prop="relation" align="center">
        <template slot-scope="scope">
          <span v-if="scope.row.relation === 1">配偶</span>
          <span v-else-if="scope.row.relation === 2">子女</span>
          <span v-else-if="scope.row.relation === 3">兄弟姐妹</span>
          <span v-else-if="scope.row.relation === 4">其他</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="240" fixed="right">
        <template slot-scope="scope">
          <el-button type="primary" size="mini" @click="handleUpdate(scope.row)" v-hasPermi="['care:home:edit']">编辑</el-button>
          <el-button type="danger" size="mini" @click="handleDelete(scope.row)" v-hasPermi="['care:home:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    
    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改家庭关系对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px" @close="cancel">
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-form-item label="老年人" prop="userId">
          <el-select v-model="form.userId" placeholder="请选择老年人" filterable @change="handleElderlyChange">
            <el-option
              v-for="item in elderlyOptions"
              :key="item.userId"
              :label="item.nickName"
              :value="item.userId"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="家属" prop="dependentsId">
          <el-select v-model="form.dependentsId" placeholder="请选择家属" filterable @change="handleDependentsChange">
            <el-option
              v-for="item in vipOptions"
              :key="item.userId"
              :label="item.nickName"
              :value="item.userId"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="关系" prop="relation">
          <el-select v-model="form.relation" placeholder="请选择关系类型">
            <el-option :value="1" label="配偶"></el-option>
            <el-option :value="2" label="子女"></el-option>
            <el-option :value="3" label="兄弟姐妹"></el-option>
            <el-option :value="4" label="其他"></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="cancel">取 消</el-button>
        <el-button type="primary" @click="submitForm">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listAllHomeRelations, getHomeRelationById, addHomeRelation, updateHomeRelation, deleteHomeRelation } from "@/api/care/homeRelation";
import { listElderly, listVip } from "@/api/system/user";

export default {
  name: "HomeRelation",
  data() {
    return {
      // 遮罩层
      loading: false,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 家庭关系表格数据
      homeRelationList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 老年人用户选项
      elderlyOptions: [],
      // 家属用户选项
      vipOptions: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        userId: [
          { required: true, message: "请选择老年人", trigger: "change" }
        ],
        dependentsId: [
          { required: true, message: "请选择家属", trigger: "change" }
        ],
        relation: [
          { required: true, message: "请选择关系类型", trigger: "change" }
        ]
      }
    };
  },
  created() {
    this.getList();
    this.getElderlyOptions();
    this.getVipOptions();
  },
  methods: {
    /** 查询家庭关系列表 */
    getList() {
      this.loading = true;
      listAllHomeRelations(this.queryParams).then(response => {
        this.homeRelationList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    /** 获取老年人选项 */
    getElderlyOptions() {
      listElderly().then(response => {
        this.elderlyOptions = response.rows;
      });
    },
    /** 获取家属选项 */
    getVipOptions() {
      listVip().then(response => {
        this.vipOptions = response.rows;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        homeId: null,
        userId: null,
        userName: null,
        dependentsId: null,
        dependentsName: null,
        relation: undefined
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.homeId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加家庭关系";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const homeId = row.homeId || this.ids
      getHomeRelationById(homeId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改家庭关系";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.homeId != null) {
            updateHomeRelation(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addHomeRelation(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const homeIds = row.homeId || this.ids;
      this.$modal.confirm('是否确认删除家庭关系编号为"' + homeIds + '"的数据项？').then(function() {
        return deleteHomeRelation(homeIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    // 老年人选择变更
    handleElderlyChange(value) {
      // 更新表单中的userName
      const selectedElderly = this.elderlyOptions.find(item => item.userId === value);
      if (selectedElderly) {
        this.form.userName = selectedElderly.nickName;
      }
    },
    // 家属选择变更
    handleDependentsChange(value) {
      // 更新表单中的dependentsName
      const selectedVip = this.vipOptions.find(item => item.userId === value);
      if (selectedVip) {
        this.form.dependentsName = selectedVip.nickName;
      }
    }
  }
};
</script>

<style scoped lang="scss">
.mb-20 {
  margin-bottom: 20px;
}

.el-select {
  width: 100%;
}
</style>
