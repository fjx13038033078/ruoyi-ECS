<script>
import { listAllRegistrations, deleteRegistration, updateRegistration } from '@/api/care/registration'

export default {
  name: 'RegistrationManagement',
  data() {
    return {
      // 遮罩层
      loading: false,
      // 预约列表数据
      registrationList: [],
      // 总条数
      total: 0,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询预约列表 */
    getList() {
      this.loading = true
      listAllRegistrations(this.queryParams).then(response => {
        this.registrationList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    /** 取消预约 */
    handleCancel(row) {
      this.$modal.confirm('确认取消该活动预约吗？').then(() => {
        const data = {
          registrationId: row.registrationId,
          status: 0 // 已取消
        }
        return updateRegistration(data)
      }).then(() => {
        this.$modal.msgSuccess('取消预约成功')
        this.getList()
      }).catch(() => {})
    },
    /** 删除预约记录 */
    handleDelete(registrationId) {
      this.$modal.confirm('确认删除该预约记录吗？该操作不可恢复！').then(() => {
        return deleteRegistration(registrationId)
      }).then(() => {
        this.$modal.msgSuccess('删除成功')
        this.getList()
      }).catch(() => {})
    },
    /** 状态文字 */
    statusText(status) {
      return status === 1 ? '已预约' : '已取消'
    },
    /** 状态标签类型 */
    statusType(status) {
      return status === 1 ? 'success' : 'info'
    }
  }
}
</script>

<template>
  <div class="app-container">
    <el-table :data="registrationList" v-loading="loading" border style="width: 100%">
      <el-table-column label="报名ID" prop="registrationId" align="center" width="200"></el-table-column>
      <el-table-column label="活动名称" prop="activityName" align="center" ></el-table-column>
      <el-table-column label="报名用户" prop="userName" align="center" ></el-table-column>
      <el-table-column label="报名时间" prop="registerTime" align="center" ></el-table-column>
      <el-table-column label="状态" prop="status" align="center" >
        <template slot-scope="scope">
          <el-tag :type="statusType(scope.row.status)">
            {{ statusText(scope.row.status) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="200" fixed="right">
        <template slot-scope="scope">
          <el-button 
            size="mini" 
            type="warning" 
            @click="handleCancel(scope.row)" 
            v-if="scope.row.status === 1"
            v-hasPermi="['care:registration:edit']"
          >取消预约</el-button>
          <el-button 
            size="mini" 
            type="danger" 
            @click="handleDelete(scope.row.registrationId)" 
            v-hasPermi="['care:registration:delete']"
          >删除记录</el-button>
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
  </div>
</template>

<style scoped lang="scss">
.app-container {
  padding: 20px;
}
</style>
