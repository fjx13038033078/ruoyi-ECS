<script>
import { listAllCommunityActivities, addCommunityActivity, updateCommunityActivity, deleteCommunityActivity, getCommunityActivityById } from '@/api/care/communityActivity'
import { addRegistration } from '@/api/care/registration'

export default {
  name: 'CommunityActivity',
  data() {
    return {
      loading: true,
      activityList: [],
      total: 0,
      queryParams: {
        pageNum: 1,
        pageSize: 10
      },
      dialogVisible: false,
      dialogTitle: '',
      activityForm: {
        activityId: null,
        title: '',
        description: '',
        activityTime: '',
        location: '',
        maxParticipants: 1
      },
      viewDialogVisible: false,
      viewActivityForm: {
        title: '',
        description: '',
        activityTime: '',
        location: '',
        maxParticipants: 1
      },
      pickerOptions: {
        disabledDate(time) {
          return time.getTime() < Date.now() - 8.64e7
        }
      },
      rules: {
        title: [{ required: true, message: '请输入活动名称', trigger: 'blur' }],
        description: [{ required: true, message: '请输入活动描述', trigger: 'blur' }],
        activityTime: [{ required: true, message: '请选择活动时间', trigger: 'change' }],
        location: [{ required: true, message: '请输入活动地点', trigger: 'blur' }],
        maxParticipants: [{ required: true, message: '请输入最大参与人数', trigger: 'blur' }]
      }
    }
  },
  created() {
    this.fetchActivities()
  },
  methods: {
    // 查询活动列表
    fetchActivities() {
      this.loading = true
      listAllCommunityActivities(this.queryParams).then(response => {
        this.activityList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    // 打开新增活动对话框
    handleAdd() {
      this.resetForm()
      this.dialogTitle = '新增活动'
      this.dialogVisible = true
    },
    // 编辑活动
    handleEdit(row) {
      getCommunityActivityById(row.activityId).then(response => {
        this.activityForm = { ...response.data }
        this.dialogTitle = '编辑活动'
        this.dialogVisible = true
      })
    },
    // 提交表单
    submitForm() {
      this.$refs.activityFormRef.validate(valid => {
        if (valid) {
          if (this.activityForm.activityId) {
            updateCommunityActivity(this.activityForm).then(() => {
              this.$message.success('更新成功')
              this.fetchActivities()
              this.dialogVisible = false
            })
          } else {
            addCommunityActivity(this.activityForm).then(() => {
              this.$message.success('新增成功')
              this.fetchActivities()
              this.dialogVisible = false
            })
          }
        }
      })
    },
    // 删除活动
    handleDelete(activityId) {
      this.$confirm('确定要删除该活动吗？', '提示', {
        type: 'warning'
      }).then(() => {
        deleteCommunityActivity(activityId).then(() => {
          this.$message.success('删除成功')
          this.fetchActivities()
        })
      })
    },
    // 查看活动
    handleView(row) {
      getCommunityActivityById(row.activityId).then(response => {
        this.viewActivityForm = { ...response.data }
        this.viewDialogVisible = true
      })
    },
    // 预约活动
    handleRegister(row) {
      this.$confirm('确定要预约参加该活动吗？', '预约确认', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'info'
      }).then(() => {
        const data = {
          activityId: row.activityId
        }
        return addRegistration(data)
      }).then(() => {
        this.$message.success('预约成功')
      }).catch(() => {
        // 用户取消操作，不做处理
      })
    },
    // 重置表单
    resetForm() {
      this.activityForm = {
        activityId: null,
        title: '',
        description: '',
        activityTime: '',
        location: '',
        maxParticipants: 1
      }
      if (this.$refs.activityFormRef) {
        this.$refs.activityFormRef.resetFields()
      }
    }
  }
}
</script>

<template>
  <div class="app-container">
    <el-row :gutter="20" class="mb-20" style="margin-bottom: 20px;">
      <el-col>
        <el-button type="primary" @click="handleAdd" v-hasPermi="['care:communityActivity:add']">新增活动</el-button>
      </el-col>
    </el-row>

    <el-table :data="activityList" v-loading="loading" border style="width: 100%">
      <el-table-column label="活动ID" prop="activityId" align="center"></el-table-column>
      <el-table-column label="活动名称" prop="title" align="center"></el-table-column>
      <el-table-column label="活动描述" prop="description" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.description ? scope.row.description.slice(0, 10) + '...' : '-' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="活动时间" prop="activityTime" align="center"></el-table-column>
      <el-table-column label="活动地点" prop="location" align="center"></el-table-column>
      <el-table-column label="最大参与人数" prop="maxParticipants" align="center"></el-table-column>
      <el-table-column label="操作" align="center" width="380" fixed="right">
        <template slot-scope="scope">
          <el-button size="mini" @click="handleView(scope.row)" v-hasPermi="['care:communityActivity:detail']">查看</el-button>
          <el-button type="primary" size="mini" @click="handleEdit(scope.row)" v-hasPermi="['care:communityActivity:edit']">编辑</el-button>
          <el-button type="success" size="mini" @click="handleRegister(scope.row)" v-hasPermi="['care:registration:add']">预约</el-button>
          <el-button type="danger" size="mini" @click="handleDelete(scope.row.activityId)" v-hasPermi="['care:communityActivity:delete']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页组件 -->
    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="fetchActivities"
    />

    <!-- 添加/编辑活动对话框 -->
    <el-dialog :visible.sync="dialogVisible" :title="dialogTitle" width="600px" @close="resetForm">
      <el-form :model="activityForm" label-width="120px" ref="activityFormRef" :rules="rules">
        <el-form-item label="活动名称" prop="title">
          <el-input v-model="activityForm.title"></el-input>
        </el-form-item>
        <el-form-item label="活动描述" prop="description">
          <el-input v-model="activityForm.description" type="textarea" :rows="4"></el-input>
        </el-form-item>
        <el-form-item label="活动时间" prop="activityTime">
          <el-date-picker
            v-model="activityForm.activityTime"
            type="date"
            placeholder="选择活动时间"
            value-format="yyyy-MM-dd"
            :picker-options="pickerOptions">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="活动地点" prop="location">
          <el-input v-model="activityForm.location"></el-input>
        </el-form-item>
        <el-form-item label="最大参与人数" prop="maxParticipants">
          <el-input-number v-model="activityForm.maxParticipants" :min="1"></el-input-number>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitForm">确定</el-button>
      </div>
    </el-dialog>

    <!-- 查看活动对话框 -->
    <el-dialog :visible.sync="viewDialogVisible" title="活动详情" width="600px">
      <el-form :model="viewActivityForm" label-width="120px">
        <el-form-item label="活动名称">
          <el-input v-model="viewActivityForm.title" disabled></el-input>
        </el-form-item>
        <el-form-item label="活动描述">
          <el-input v-model="viewActivityForm.description" type="textarea" :rows="4" disabled></el-input>
        </el-form-item>
        <el-form-item label="活动时间">
          <el-input v-model="viewActivityForm.activityTime" disabled></el-input>
        </el-form-item>
        <el-form-item label="活动地点">
          <el-input v-model="viewActivityForm.location" disabled></el-input>
        </el-form-item>
        <el-form-item label="最大参与人数">
          <el-input v-model="viewActivityForm.maxParticipants" disabled></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="viewDialogVisible = false">关闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<style scoped>
.mb-20 {
  margin-bottom: 20px;
}
</style>
