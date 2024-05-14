<template>
  <div>
    <el-row :gutter="10" style="margin-bottom: 40px"><!--gutter:为组间容器设置间隔-->
      <el-col :span="24">
        <el-card style="color: #92cbdc">
          <div><i style="padding-right: 5px" class="el-icon-user"></i>员工总数：</div>
          <div style="text-align: center;padding: 10px 0;font-weight: bolder">{{ this.total }}</div>
          <div style="text-align: center;font-weight:bold"></div>
        </el-card>
      </el-col>
    </el-row>
    <el-row><!--流式布局，一个页面为24-->
      <el-col :span="12">
        <el-card id="main" style="width: 500px;height: 500px"></el-card>
      </el-col>
      <el-col :span="12">
        <el-card id="pie" style="width: 500px;height: 500px"></el-card>
      </el-col>
    </el-row>
  </div>

</template>

<script>
import * as echarts from 'echarts'

export default {
  name: "HomeView",
  data() {
    return {
      total: '',
      tableData: [],
    }
  },
  methods: {
    load() {

      this.request.get("/staff/page", {
        params: {
          pageNum: 1,//请求数据
          pageSize: 2,
          name: this.name,
          id: this.id,
        }
      }).then(res => {
        this.tableData = res.data.records
        this.total = res.data.total
      })
    },
  },
  mounted() {//页面元素渲染之后触发
    // this.load()
    var option = {


      title: {
        text: '各季度会员数量统计',
        subtext: '趋势图',
        left: 'center'

      },
      tooltip: {
        trigger: 'axis',/*鼠标悬浮显示数据*/
      },

      xAxis: [
        {
          type: 'category',
          data: ["第一季度", "第二季度", "第三季度", "第四季度"],/*后台数据传入，实现动态图表*/
        }

      ],
      yAxis: [
        {
          type: 'value',
          name:'zhu'
        }
      ],
      series: [
        {
          name: "a1",
          data: [],
          type: 'bar',

        },
        {
          name: "a2",
          data: [],
          type: 'bar',

        },
        {
          name: "a1",
          data: [],
          type: 'line',

        },
        {
          name: "a2",
          data: [],
          type: 'line',

        },
      ]
    };

    var chartDom = document.getElementById('main');
    var myChart = echarts.init(chartDom);

    this.request.get("/echarts/members").then(res => {
      // option.xAxis.data=res.data.x
      option.series[0].data = res.data
      option.series[1].data = [5, 6, 7, 8]
      option.series[2].data = res.data
      option.series[3].data = [5, 6, 7, 8]
      //数据准备完毕之后再set
      myChart.setOption(option);

    })

    //饼图

    var pieOption = {
      title: {
        text: '季度会员',
        subtext: ' 比例图',
        left: 'center'
      },
      tooltip: {
        trigger: 'item',
        formatter: " {a}<br/>{b}:<br/>{c}"
      },
      legend: {
        orient: 'vertical',
        left: 'left'
      },
      series: [
        {
          name: "bb",
          type: 'pie',
          radius: ['10%', '50%'],/*第一个参数是控制里面小圈的大小，第二个参数是控制外面大圈的大小；*/
          center: ['75%', '60%'],/*左右位置，上下位置*/
          label: {
            normal: {
              show: true,
              position: 'inner',
              textStyle: {
                color: "#fff"
              },
              formatter: " {c}\n{d}%"//模板变量有 {a}、{b}、{c}、{d}，分别表示系列名，数据名，数据值，百分比。注意不同地方换行符使用规则

            },
               },
          data: [],
          emphasis: {
            itemStyle: {
              shadowBlur: 10,
              shadowOffsetX: 0,
              shadowColor: 'rgba(255, 0, 0, 0.5)'
            }
          }
        },

        {
          type: 'pie',
          radius: '50%',
          center: ['25%', '50%'],
          label: {
            normal: {
              show: true,
              position: 'inner',
              textStyle: {
                color: "#fff"
              } ,
              formatter: "{d}%"//模板变量有 {a}、{b}、{c}、{d}，分别表示系列名，数据名，数据值，百分比。

            },

          },
          name: "aa",
          data: [],
        }
      ]
    };
    var pieDom = document.getElementById('pie');
    var pieChart = echarts.init(pieDom);

    this.request.get("echarts/members").then(res => {
      pieOption.series[0].data = [
        {name: "第一季度", value: res.data[0]},
        {name: "第二季度", value: res.data[1]},
        {name: "第三季度", value: res.data[2]},
        {name: "第四季度", value: res.data[3]},
      ]
      pieOption.series[1].data = [
        {name: "第一季度", value: 10},
        {name: "第二季度", value: 20},
        {name: "第三季度", value: 30},
        {name: "第四季度", value: 40},
      ]
      pieChart.setOption(pieOption)
    })
  }
}
</script>

<style scoped>

</style>