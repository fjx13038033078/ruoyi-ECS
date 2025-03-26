<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!-- 异常体温统计卡片 -->
      <el-col :span="8">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>今日异常体温统计</span>
          </div>
          <div class="count-container">
            <div class="count-number">{{ highTemperatureCount }}</div>
            <div class="count-label">位老年人体温异常(>37.3°C)</div>
          </div>
        </el-card>
      </el-col>

      <!-- 异常血糖统计卡片 -->
      <el-col :span="8">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>今日异常血糖统计</span>
          </div>
          <div class="count-container">
            <div class="count-number">{{ highBloodSugarCount }}</div>
            <div class="count-label">位老年人血糖异常(>7.8mmol/L)</div>
          </div>
        </el-card>
      </el-col>

      <!-- 低血糖统计卡片 -->
      <el-col :span="8">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>今日低血糖统计</span>
          </div>
          <div class="count-container">
            <div class="count-number">{{ lowBloodSugarCount }}</div>
            <div class="count-label">位老年人血糖偏低(<3.9mmol/L)</div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { countHighTemperatureToday, countHighBloodSugarToday, countLowBloodSugarToday } from '@/api/care/healthRecord'

export default {
  name: 'HealthRecordStatistics',
  data() {
    return {
      highTemperatureCount: 0,
      highBloodSugarCount: 0,
      lowBloodSugarCount: 0
    }
  },
  mounted() {
    this.initData()
  },
  methods: {
    async initData() {
      try {
        // 获取异常体温数据
        const temperatureRes = await countHighTemperatureToday()
        this.highTemperatureCount = temperatureRes.data || 0
        console.log('异常体温人数:', temperatureRes.data)
      } catch (error) {
        console.error('获取异常体温数据失败:', error)
        this.$message.error('获取异常体温数据失败')
      }

      try {
        // 获取异常血糖数据
        const bloodSugarRes = await countHighBloodSugarToday()
        this.highBloodSugarCount = bloodSugarRes.data || 0
        console.log('异常血糖人数:', bloodSugarRes.data)
      } catch (error) {
        console.error('获取异常血糖数据失败:', error)
        this.$message.error('获取异常血糖数据失败')
      }

      try {
        // 获取低血糖数据
        const lowBloodSugarRes = await countLowBloodSugarToday()
        this.lowBloodSugarCount = lowBloodSugarRes.data || 0
        console.log('低血糖人数:', lowBloodSugarRes.data)
      } catch (error) {
        console.error('获取低血糖数据失败:', error)
        this.$message.error('获取低血糖数据失败')
      }
    }
  }
}
</script>

<style scoped lang="scss">
.app-container {
  padding: 20px;
}

.count-container {
  text-align: center;
  padding: 20px;

  .count-number {
    font-size: 36px;
    color: #409EFF;
    font-weight: bold;
    margin-bottom: 10px;
  }

  .count-label {
    font-size: 16px;
    color: #606266;
  }
}

.box-card {
  margin-bottom: 20px;

  .clearfix {
    &:before,
    &:after {
      display: table;
      content: "";
    }
    &:after {
      clear: both;
    }
  }
}
</style>
