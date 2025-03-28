<template>
  <div>
    <div ref="echartsText" style="height: 100px; display: flex; justify-content: center; align-items: center;">
      <!-- 这里 ECharts 动画文本会被渲染 -->
    </div>
    <div>
      <el-carousel :interval="4000" type="card" height="300px">
        <el-carousel-item>
          <a href="https://tv.cctv.com/yxg/tbjm/scdh/index.shtml" target="_blank">
            <img src="../assets/images/01.jpg" alt="Image 1" style="width: 100%;">
          </a>
        </el-carousel-item>
        <el-carousel-item>
          <a href="https://tv.cctv.com/yxg/tbjm/scdh/index.shtml" target="_blank">
            <img src="../assets/images/02.jpg" alt="Image 2" style="width: 100%;">
          </a>
        </el-carousel-item>
        <el-carousel-item>
          <a href="https://tv.cctv.com/yxg/tbjm/scdh/index.shtml" target="_blank">
            <img src="../assets/images/03.jpg" alt="Image 3" style="width: 100%;">
          </a>
        </el-carousel-item>
      </el-carousel>
      <!-- 通知公告 -->
      <el-row style="margin-top: 20px;">
        <el-col :span="12">
          <el-card style="margin-right: 20px; height: 420px;">
            <h3 slot="header">通知公告</h3>
            <el-table v-loading="loading" :data="noticeList">
              <el-table-column label="序号" align="center" prop="noticeId" width="100"/>
              <el-table-column
                label="公告标题"
                align="center"
                prop="noticeTitle"
                :show-overflow-tooltip="true"
              >
                <template slot-scope="scope">
                  <span @click="showNoticeContent(scope.row)">{{ scope.row.noticeTitle }}</span>
                </template>
              </el-table-column>
              <el-table-column label="公告类型" align="center" prop="noticeType" width="100">
                <template slot-scope="scope">
                  <dict-tag :options="dict.type.sys_notice_type" :value="scope.row.noticeType"/>
                </template>
              </el-table-column>
              <el-table-column label="创建时间" align="center" prop="createTime" width="100">
                <template slot-scope="scope">
                  <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d}') }}</span>
                </template>
              </el-table-column>
            </el-table>
          </el-card>
        </el-col>
        <el-col :span="12">
          <el-card style="margin-right: 20px; height: 420px;">
            <h3 slot="header">热门活动报名排行榜</h3>
              <div id="hotActivitiesChart" style="height: 300px;"></div> <!-- echarts 柱状图 -->
          </el-card>
        </el-col>
      </el-row>
      <!-- 弹出的公告内容卡片 -->
      <el-dialog :title="selectedNotice.title" :visible.sync="showNoticeDialog" width="780px" append-to-body>
        <div slot="title" style="text-align: center;">{{ selectedNotice.title }}</div>
        <div v-html="selectedNotice.content" class="notice-content"></div>
      </el-dialog>
    </div>
  </div>
</template>
<script>
import {listNotice, getNotice} from "@/api/system/notice";
import * as echarts from 'echarts'
import {getTop5ActivitiesByRegistrations} from "@/api/care/registration";


export default {
  name: "Notice",
  dicts: ['sys_notice_status', 'sys_notice_type'],
  data() {
    return {
      // 遮罩层
      loading: true,
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
      // 公告表格数据
      noticeList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      hotActivitiesData: [], // 存储热门活动数据
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        noticeTitle: undefined,
        createBy: undefined,
        status: undefined
      },
      selectedNotice: {
        title: '',
        content: ''
      },
      showNoticeDialog: false,
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        noticeTitle: [
          {required: true, message: "公告标题不能为空", trigger: "blur"}
        ],
        noticeType: [
          {required: true, message: "公告类型不能为空", trigger: "change"}
        ]
      }
    };
  },
  created() {
    this.getList();
  },
  mounted() {
    this.initHotActivitiesChart(); // 初始化热门活动图表
    this.initEchartsText(); // 初始化 ECharts 动画文本
  },
  methods: {
    /** 查询公告列表 */
    getList() {
      this.loading = true;
      listNotice(this.queryParams).then(response => {
        this.noticeList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    showNoticeContent(row) {
      this.loading = true;
      getNotice(row.noticeId).then((response) => {
        this.selectedNotice.title = response.data.noticeTitle;
        this.selectedNotice.content = response.data.noticeContent;
        this.showNoticeDialog = true;
        this.loading = false;
      });
    },
    // 获取热门活动数据并更新到图表中
    fetchHotActivitiesData() {
      getTop5ActivitiesByRegistrations().then(response => {
        this.hotActivitiesData = response.data;
        this.updateHotActivitiesChart();
      })
    },
    // 初始化热门活动图表
    initHotActivitiesChart() {
      this.hotActivitiesChart = echarts.init(document.getElementById("hotActivitiesChart"));
      this.fetchHotActivitiesData(); // 获取数据并更新图表
    },
    updateHotActivitiesChart() {
      // 准备数据
      const activityNames = [];
      const registrationCounts = [];
      
      // 解析返回的数据 - 修改为适应新的数据格式
      this.hotActivitiesData.forEach(item => {
        // 数据格式是 {"活动名称": 数量}
        const activityName = Object.keys(item)[0]; // 获取第一个键作为活动名称
        const count = item[activityName];          // 获取对应的值作为报名人数
        
        activityNames.push(activityName);
        registrationCounts.push(count);
      });
      
      const option = {
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow'
          }
        },
        xAxis: {
          type: 'category',
          data: activityNames,
          axisLabel: {
            interval: 0,
            rotate: 30,
            textStyle: {
              fontSize: 12
            }
          }
        },
        yAxis: {
          type: 'value',
          name: '报名人数'
        },
        series: [{
          data: registrationCounts,
          type: 'bar',
          barWidth: '40%',
          itemStyle: {
            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
              { offset: 0, color: '#83bff6' },
              { offset: 0.5, color: '#188df0' },
              { offset: 1, color: '#188df0' }
            ])
          }
        }]
      };
      
      this.hotActivitiesChart.setOption(option);
    },
    // 初始化 ECharts 动画文本
    initEchartsText() {
      const chartDom = this.$refs.echartsText;
      const myChart = echarts.init(chartDom);
      const option = {
        graphic: {
          elements: [
            {
              type: 'text',
              left: 'center',
              top: 'center',
              style: {
                text: '珠海智慧养老系统',
                fontSize: 80,
                fontWeight: 'bold',
                lineDash: [0, 200],
                lineDashOffset: 0,
                fill: 'transparent',
                stroke: '#000',
                lineWidth: 1
              },
              keyframeAnimation: {
                duration: 3000,
                loop: true,
                keyframes: [
                  {
                    percent: 0.7,
                    style: {
                      fill: 'transparent',
                      lineDashOffset: 200,
                      lineDash: [200, 0]
                    }
                  },
                  {
                    percent: 0.8,
                    style: {
                      fill: 'transparent'
                    }
                  },
                  {
                    percent: 1,
                    style: {
                      fill: 'black'
                    }
                  }
                ]
              }
            }
          ]
        }
      };
      myChart.setOption(option);
    }
  }
};
</script>

<style scoped lang="scss">
.notice-content::v-deep img {
  max-width: 100%;
  height: auto;
  display: block;
  margin: 0 auto;
}

</style>
