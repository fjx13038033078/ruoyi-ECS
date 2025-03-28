<script>
import {
  listAllServiceRequests,
  getServiceRequestById,
  addServiceRequest,
  updateServiceRequest,
  deleteServiceRequest,
  updateStatus,
  updateServiceDetails
} from '@/api/care/serviceRequest'
import { listElderly } from '@/api/system/user'

export default {
  name: 'ServiceRequest',
  data() {
    return {
      // 遮罩层
      loading: false,
      // 总条数
      total: 0,
      // 服务需求列表
      requestList: [],
      // 弹出层标题
      dialogTitle: '',
      // 是否显示弹出层
      dialogVisible: false,
      // 是否显示查看弹出层
      viewDialogVisible: false,
      // 是否显示审核弹出层
      auditDialogVisible: false,
      // 是否为管理员
      isAdmin: false,
      // 老年人选项
      elderlyOptions: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10
      },
      // 表单参数
      requestForm: {
        requestId: null,
        elderlyId: null,
        userName: '',
        serviceType: null,
        requestTime: null,
        serviceTime: null,
        servicePersonnel: '',
        details: '',
        status: 0
      },
      // 审核表单参数
      auditForm: {
        requestId: null,
        serviceTime: null,
        servicePersonnel: '',
        details: '',
        status: 1
      },
      // 查看表单参数
      viewRequestForm: {
        requestId: null,
        elderlyId: null,
        userName: '',
        serviceType: null,
        serviceTypeName: '',
        requestTime: null,
        serviceTime: null,
        servicePersonnel: '',
        details: '',
        status: null
      },
      // 表单校验规则
      rules: {
        serviceType: [
          { required: true, message: '请选择服务类型', trigger: 'change' }
        ]
      },
      // 审核表单校验规则
      auditRules: {
        serviceTime: [
          { required: true, message: '请选择服务时间', trigger: 'change' }
        ],
        servicePersonnel: [
          { required: true, message: '请输入服务人员', trigger: 'blur' }
        ]
      },
      // 日期选择器配置
      pickerOptions: {
        disabledDate(time) {
          return time.getTime() < Date.now() - 8.64e7 // 禁用今天之前的日期
        }
      }
    }
  },
  created() {
    this.getList()
    this.getElderlyOptions()
    // 判断是否为管理员
    this.isAdmin = this.$store.getters.roles.includes('admin')
  },
  methods: {
    /** 查询服务需求列表 */
    getList() {
      this.loading = true
      listAllServiceRequests(this.queryParams).then(response => {
        this.requestList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    /** 获取老年人选项 */
    getElderlyOptions() {
      listElderly().then(response => {
        this.elderlyOptions = response.rows.map(item => ({
          value: item.userId,
          label: item.nickName
        }))
      })
    },
    /** 老年人选择改变 */
    handleElderlyChange(value) {
      const selected = this.elderlyOptions.find(item => item.value === value)
      if (selected) {
        this.requestForm.userName = selected.label
      }
    },
    /** 获取服务类型名称 */
    getServiceTypeName(type) {
      const typeMap = {
        1: '家政服务',
        2: '医疗护理',
        3: '日常陪伴'
      }
      return typeMap[type] || '-'
    },
    /** 获取状态类型 */
    getStatusType(status) {
      const typeMap = {
        0: 'info',
        1: 'warning',
        2: 'success',
        3: 'danger'
      }
      return typeMap[status] || 'info'
    },
    /** 获取状态名称 */
    getStatusName(status) {
      const nameMap = {
        0: '待审核',
        1: '已批准',
        2: '已完成',
        3: '已取消'
      }
      return nameMap[status] || '-'
    },
    /** 取消按钮 */
    cancel() {
      this.dialogVisible = false
      this.resetForm()
    },
    /** 表单重置 */
    resetForm() {
      this.requestForm = {
        requestId: null,
        elderlyId: null,
        userName: '',
        serviceType: null,
        requestTime: null,
        serviceTime: null,
        servicePersonnel: '',
        details: '',
        status: 0
      }
      this.$refs.requestFormRef && this.$refs.requestFormRef.resetFields()
    },
    /** 审核表单重置 */
    resetAuditForm() {
      this.auditForm = {
        requestId: null,
        serviceTime: null,
        servicePersonnel: '',
        details: '',
        status: 1
      }
      this.$refs.auditFormRef && this.$refs.auditFormRef.resetFields()
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.resetForm()
      this.dialogTitle = '新增服务需求'
      this.dialogVisible = true
    },
    /** 修改按钮操作 */
    handleEdit(row) {
      this.resetForm()
      const requestId = row.requestId
      getServiceRequestById(requestId).then(response => {
        this.requestForm = response.data
        // 确保只保留普通用户可以编辑的信息
        this.requestForm.serviceTime = null
        this.requestForm.servicePersonnel = ''
        this.requestForm.details = ''
        this.dialogTitle = '修改服务需求'
        this.dialogVisible = true
      })
    },
    /** 审核按钮操作 */
    handleAudit(row) {
      this.resetAuditForm()
      this.auditForm.requestId = row.requestId
      // 如果已经审核过，加载之前的信息
      if (row.status === 1 || row.status === 2) {
        getServiceRequestById(row.requestId).then(response => {
          const data = response.data
          this.auditForm.serviceTime = data.serviceTime
          this.auditForm.servicePersonnel = data.servicePersonnel
          this.auditForm.details = data.details
        })
      }
      this.auditDialogVisible = true
    },
    /** 完成服务 */
    handleComplete(row) {
      this.$modal.confirm('是否确认该服务已完成？').then(() => {
        const data = {
          requestId: row.requestId,
          status: 2  // 设置状态为已完成
        }
        return updateStatus(data)
      }).then(() => {
        this.$modal.msgSuccess('服务已完成')
        this.getList()
      }).catch(() => {})
    },
    /** 查看按钮操作 */
    handleView(row) {
      getServiceRequestById(row.requestId).then(response => {
        this.viewRequestForm = response.data
        this.viewRequestForm.serviceTypeName = this.getServiceTypeName(response.data.serviceType)
        this.viewDialogVisible = true
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs.requestFormRef.validate(valid => {
        if (valid) {
          // 确保状态为待审核
          this.requestForm.status = 0
          if (this.requestForm.requestId) {
            updateServiceRequest(this.requestForm).then(response => {
              this.$modal.msgSuccess('修改成功')
              this.dialogVisible = false
              this.getList()
            })
          } else {
            addServiceRequest(this.requestForm).then(response => {
              this.$modal.msgSuccess('新增成功')
              this.dialogVisible = false
              this.getList()
            })
          }
        }
      })
    },
    /** 提交审核 */
    submitAudit() {
      this.$refs.auditFormRef.validate(valid => {
        if (valid) {
          // 确保状态为已批准
          this.auditForm.status = 1
          updateServiceDetails(this.auditForm).then(response => {
            this.$modal.msgSuccess('审核通过成功')
            this.auditDialogVisible = false
            this.getList()
          })
        }
      })
    },
    /** 删除按钮操作 */
    handleDelete(requestId) {
      this.$modal.confirm('是否确认删除服务需求编号为"' + requestId + '"的数据项？').then(() => {
        return deleteServiceRequest(requestId)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess('删除成功')
      }).catch(() => {})
    }
  }
}
</script>

<template>
  <div class="app-container">
    <el-row :gutter="20" class="mb-20" style="margin-bottom: 20px;">
      <el-col>
        <el-button type="primary" @click="handleAdd" v-hasPermi="['care:serviceRequest:add']">新增服务需求</el-button>
      </el-col>
    </el-row>

    <el-table :data="requestList" v-loading="loading" border style="width: 100%">
      <el-table-column label="需求ID" prop="requestId" align="center"></el-table-column>
      <el-table-column label="老年人姓名" prop="userName" align="center"></el-table-column>
      <el-table-column label="服务类型" prop="serviceType" align="center">
        <template slot-scope="scope">
          {{ getServiceTypeName(scope.row.serviceType) }}
        </template>
      </el-table-column>
      <el-table-column label="预约时间" prop="requestTime" align="center"></el-table-column>
      <el-table-column label="服务时间" prop="serviceTime" align="center"></el-table-column>
      <el-table-column label="服务人员" prop="servicePersonnel" align="center"></el-table-column>
      <el-table-column label="状态" prop="status" align="center">
        <template slot-scope="scope">
          <el-tag :type="getStatusType(scope.row.status)">
            {{ getStatusName(scope.row.status) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="340" fixed="right">
        <template slot-scope="scope">
          <el-button size="mini" @click="handleView(scope.row)" v-hasPermi="['care:serviceRequest:detail']">查看</el-button>
          <el-button type="primary" size="mini" @click="handleEdit(scope.row)" v-hasPermi="['care:serviceRequest:edit']" v-if="scope.row.status === 0">编辑</el-button>
          <el-button type="warning" size="mini" @click="handleAudit(scope.row)" v-hasPermi="['care:serviceRequest:audit']" v-if="scope.row.status === 0">审核</el-button>
          <el-button type="success" size="mini" @click="handleComplete(scope.row)" v-hasPermi="['care:serviceRequest:finish']" v-if="scope.row.status === 1">服务完成</el-button>
          <el-button type="danger" size="mini" @click="handleDelete(scope.row.requestId)" v-hasPermi="['care:serviceRequest:delete']" v-if="scope.row.status === 0">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页组件 -->
    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加/编辑服务需求对话框 -->
    <el-dialog :visible.sync="dialogVisible" :title="dialogTitle" width="600px" @close="resetForm">
      <el-form :model="requestForm" label-width="120px" ref="requestFormRef" :rules="rules">
        <!-- 服务类型 (所有用户都需要填写) -->
        <el-form-item label="服务类型" prop="serviceType">
          <el-select v-model="requestForm.serviceType" placeholder="请选择服务类型">
            <el-option label="家政服务" :value="1"></el-option>
            <el-option label="医疗护理" :value="2"></el-option>
            <el-option label="日常陪伴" :value="3"></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitForm">确定</el-button>
      </div>
    </el-dialog>

    <!-- 审核服务需求对话框 -->
    <el-dialog :visible.sync="auditDialogVisible" title="审核服务需求" width="600px" @close="resetAuditForm">
      <el-form :model="auditForm" label-width="120px" ref="auditFormRef" :rules="auditRules">
        <el-form-item label="服务时间" prop="serviceTime">
          <el-date-picker
            v-model="auditForm.serviceTime"
            type="datetime"
            placeholder="选择服务时间"
            value-format="yyyy-MM-dd HH:mm:ss"
            :picker-options="pickerOptions">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="服务人员" prop="servicePersonnel">
          <el-input v-model="auditForm.servicePersonnel" placeholder="请输入服务人员姓名"></el-input>
        </el-form-item>
        <el-form-item label="服务详情" prop="details">
          <el-input v-model="auditForm.details" type="textarea" :rows="4" placeholder="(选填)请输入服务详情"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="auditDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitAudit">确定</el-button>
      </div>
    </el-dialog>

    <!-- 查看服务需求对话框 -->
    <el-dialog :visible.sync="viewDialogVisible" title="服务需求详情" width="600px">
      <el-form :model="viewRequestForm" label-width="120px">
        <el-form-item label="老年人姓名">
          <el-input v-model="viewRequestForm.userName" disabled></el-input>
        </el-form-item>
        <el-form-item label="服务类型">
          <el-input v-model="viewRequestForm.serviceTypeName" disabled></el-input>
        </el-form-item>
        <el-form-item label="预约时间">
          <el-input v-model="viewRequestForm.requestTime" disabled></el-input>
        </el-form-item>
        <el-form-item label="服务时间" v-if="viewRequestForm.serviceTime">
          <el-input v-model="viewRequestForm.serviceTime" disabled></el-input>
        </el-form-item>
        <el-form-item label="服务人员" v-if="viewRequestForm.servicePersonnel">
          <el-input v-model="viewRequestForm.servicePersonnel" disabled></el-input>
        </el-form-item>
        <el-form-item label="服务详情" v-if="viewRequestForm.details">
          <el-input v-model="viewRequestForm.details" type="textarea" :rows="4" disabled></el-input>
        </el-form-item>
        <el-form-item label="状态">
          <el-tag :type="getStatusType(viewRequestForm.status)">
            {{ getStatusName(viewRequestForm.status) }}
          </el-tag>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="viewDialogVisible = false">关闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<style scoped lang="scss">
.mb-20 {
  margin-bottom: 20px;
}
</style>
