<template>
  <div class="app-container">
    <el-row :gutter="20" class="mb-20" style="margin-bottom: 20px;">
      <el-col>
        <el-button type="primary" @click="handleAdd" v-hasPermi="['care:healthRecord:add']">新增健康记录</el-button>
      </el-col>
    </el-row>

    <el-table :data="healthRecordList" v-loading="loading" border style="width: 100%">
      <el-table-column label="记录ID" prop="recordId" align="center"></el-table-column>
      <el-table-column label="老年人姓名" prop="userName" align="center"></el-table-column>
      <el-table-column label="记录日期" prop="recordDate" align="center"></el-table-column>
      <el-table-column label="血压" prop="bloodPressure" align="center"></el-table-column>
      <el-table-column label="血糖(mmol/L)" prop="bloodSugar" align="center"></el-table-column>
      <el-table-column label="体温(℃)" prop="temperature" align="center"></el-table-column>
      <el-table-column label="备注" prop="remarks" align="center">
        <template #default="scope">
          <span>{{ scope.row.remarks ? scope.row.remarks.slice(0, 20) + '...' : '-' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="300" fixed="right">
        <template #default="scope">
          <el-button size="mini" @click="handleView(scope.row)" v-hasPermi="['care:healthRecord:detail']">查看</el-button>
          <el-button type="primary" size="mini" @click="handleEdit(scope.row)" v-hasPermi="['care:healthRecord:edit']">编辑</el-button>
          <el-button type="danger" size="mini" @click="handleDelete(scope.row.recordId)" v-hasPermi="['care:healthRecord:delete']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页组件 -->
    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="fetchHealthRecords"
    />

    <!-- 添加/编辑健康记录对话框 -->
    <el-dialog :visible.sync="dialogVisible" :title="dialogTitle" width="600px" @close="resetForm">
      <el-form :model="healthRecordForm" label-width="120px" ref="healthRecordFormRef" :rules="rules">
        <el-form-item label="老年人姓名" prop="elderlyId" v-if="isAdmin">
          <el-select v-model="healthRecordForm.elderlyId" placeholder="请选择老年人" @change="handleElderlyChange">
            <el-option
              v-for="item in elderlyOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="记录日期" prop="recordDate">
          <el-date-picker
            v-model="healthRecordForm.recordDate"
            type="date"
            placeholder="选择日期"
            value-format="yyyy-MM-dd">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="血压" prop="bloodPressure">
          <el-input v-model="healthRecordForm.bloodPressure" placeholder="如：120/80"></el-input>
        </el-form-item>
        <el-form-item label="血糖" prop="bloodSugar">
          <el-input-number v-model="healthRecordForm.bloodSugar" :precision="2" :step="0.1" :min="0"></el-input-number>
        </el-form-item>
        <el-form-item label="体温" prop="temperature">
          <el-input-number v-model="healthRecordForm.temperature" :precision="1" :step="0.1" :min="35" :max="42"></el-input-number>
        </el-form-item>
        <el-form-item label="备注" prop="remarks">
          <el-input v-model="healthRecordForm.remarks" type="textarea" :rows="3"></el-input>
        </el-form-item>
        <el-form-item label="健康附件">
          <FileUpload v-model="healthRecordForm.fileName"></FileUpload>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitForm">确定</el-button>
      </div>
    </el-dialog>

    <!-- 查看健康记录对话框 -->
    <el-dialog :visible.sync="viewDialogVisible" title="健康记录详情" width="600px">
      <el-form :model="viewHealthRecordForm" label-width="120px">
        <el-form-item label="老年人姓名">
          <el-input v-model="viewHealthRecordForm.userName" disabled></el-input>
        </el-form-item>
        <el-form-item label="记录日期">
          <el-input v-model="viewHealthRecordForm.recordDate" disabled></el-input>
        </el-form-item>
        <el-form-item label="血压">
          <el-input v-model="viewHealthRecordForm.bloodPressure" disabled></el-input>
        </el-form-item>
        <el-form-item label="血糖">
          <el-input v-model="viewHealthRecordForm.bloodSugar" disabled></el-input>
        </el-form-item>
        <el-form-item label="体温">
          <el-input v-model="viewHealthRecordForm.temperature" disabled></el-input>
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="viewHealthRecordForm.remarks" type="textarea" :rows="3" disabled></el-input>
        </el-form-item>
        <el-form-item label="健康附件">
          <FileUpload v-model="viewHealthRecordForm.fileName" :is-show-tip="false" :disabled="true"></FileUpload>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="viewDialogVisible = false">关闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listAllHealthRecords, addHealthRecord, updateHealthRecord, deleteHealthRecord, getHealthRecordById } from '@/api/care/healthRecord'
import { listElderly } from '@/api/system/user'
import {getInfo} from "@/api/login";

export default {
  data() {
    return {
      loading: true,
      healthRecordList: [],
      total: 0,
      elderlyOptions: [], // 老年人选项列表
      queryParams: {
        pageNum: 1,
        pageSize: 10
      },
      dialogVisible: false,
      dialogTitle: '',
      healthRecordForm: {
        recordId: null,
        elderlyId: null,
        userName: '',
        recordDate: '',
        bloodPressure: '',
        bloodSugar: null,
        temperature: null,
        remarks: '',
        fileName: ''
      },
      viewDialogVisible: false,
      viewHealthRecordForm: {
        userName: '',
        recordDate: '',
        bloodPressure: '',
        bloodSugar: null,
        temperature: null,
        remarks: '',
        fileName: ''
      },
      isAdmin: false,
      rules: {
        elderlyId: [{ required: true, message: '请选择老年人', trigger: 'change' }],
        recordDate: [{ required: true, message: '请选择记录日期', trigger: 'change' }],
        bloodPressure: [{ required: true, message: '请输入血压', trigger: 'blur' }],
        bloodSugar: [{ required: true, message: '请输入血糖', trigger: 'blur' }],
        temperature: [{ required: true, message: '请输入体温', trigger: 'blur' }]
      }
    }
  },
  created() {
    this.fetchHealthRecords()
    this.getElderlyOptions()
  },
  methods: {
    getCurrentInfo(){
      getInfo().then(response => {
        this.isAdmin = response.roles.includes('admin')
      })
    },
    // 获取老年人选项列表
    getElderlyOptions() {
      listElderly().then(response => {
        console.log(response)
        this.elderlyOptions = response.rows.map(item => ({
          value: item.userId,
          label: item.nickName
        }))
      })
    },
    // 处理老年人选择变化
    handleElderlyChange(value) {
      const selectedElderly = this.elderlyOptions.find(item => item.value === value)
      if (selectedElderly) {
        this.healthRecordForm.userName = selectedElderly.label
      }
    },
    // 查询健康记录列表
    fetchHealthRecords() {
      this.loading = true
      listAllHealthRecords(this.queryParams).then(response => {
        this.healthRecordList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    // 打开新增健康记录对话框
    handleAdd() {
      this.getCurrentInfo();
      this.resetForm()
      this.dialogTitle = '新增健康记录'
      this.dialogVisible = true
    },
    // 编辑健康记录
    handleEdit(row) {
      this.getCurrentInfo();
      getHealthRecordById(row.recordId).then(response => {
        this.healthRecordForm = { ...response.data }
        this.dialogTitle = '编辑健康记录'
        this.dialogVisible = true
      })
    },
    // 提交表单
    submitForm() {
      this.$refs.healthRecordFormRef.validate(valid => {
        if (valid) {
          if (this.healthRecordForm.recordId) {
            updateHealthRecord(this.healthRecordForm).then(() => {
              this.$message.success('更新成功')
              this.fetchHealthRecords()
              this.dialogVisible = false
            })
          } else {
            addHealthRecord(this.healthRecordForm).then(() => {
              this.$message.success('新增成功')
              this.fetchHealthRecords()
              this.dialogVisible = false
            })
          }
        }
      })
    },
    // 删除健康记录
    handleDelete(recordId) {
      this.$confirm('确定要删除该健康记录吗？', '提示', {
        type: 'warning'
      }).then(() => {
        deleteHealthRecord(recordId).then(() => {
          this.$message.success('删除成功')
          this.fetchHealthRecords()
        })
      })
    },
    // 查看健康记录
    handleView(row) {
      getHealthRecordById(row.recordId).then(response => {
        this.viewHealthRecordForm = { ...response.data }
        this.viewDialogVisible = true
      })
    },
    // 重置表单
    resetForm() {
      this.healthRecordForm = {
        recordId: null,
        elderlyId: null,
        userName: '',
        recordDate: '',
        bloodPressure: '',
        bloodSugar: null,
        temperature: null,
        remarks: '',
        fileName: ''
      }
      this.$refs.healthRecordFormRef?.resetFields()
    }
  }
}
</script>

<style scoped>
.mb-20 {
  margin-bottom: 20px;
}
</style>
