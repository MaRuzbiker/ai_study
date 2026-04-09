<template>
  <div class="deskmate">
    <el-card class="search-card">
      <template #header>
        <div class="card-header">
          <span>同桌学习</span>
        </div>
      </template>
      <div class="search-area">
        <el-input
          v-model="searchNickname"
          placeholder="输入对方昵称，查询今日学习情况"
          size="large"
          clearable
          @keyup.enter="search"
          style="max-width: 400px;"
        >
          <template #prefix>
            <i class="el-icon-user"></i>
          </template>
        </el-input>
        <el-button type="primary" size="large" :loading="loading" @click="search">
          查询
        </el-button>
      </div>
      <div v-if="searched" class="result-tip">
        <span v-if="notFound" class="not-found">未找到该昵称的用户</span>
        <span v-else class="found">
          <i class="el-icon-user"></i> {{ deskMateInfo?.nickname }} 的今日学习情况
        </span>
      </div>
    </el-card>

    <!-- 同桌信息 -->
    <el-card v-if="!notFound && deskMateInfo" class="info-card">
      <div class="desk-header">
        <div class="desk-avatar">
          <i class="el-icon-user-solid"></i>
        </div>
        <div class="desk-info">
          <div class="desk-name">{{ deskMateInfo.nickname }}</div>
          <div class="desk-sub">同桌学习 · 今日数据</div>
        </div>
      </div>

      <div class="stats-row">
        <div class="stat-item">
          <div class="stat-value">{{ formatTime(deskMateInfo.totalSeconds) }}</div>
          <div class="stat-label">今日学习时长</div>
        </div>
        <div class="stat-item">
          <div class="stat-value">{{ deskMateInfo.recordCount }}</div>
          <div class="stat-label">今日学习记录</div>
        </div>
      </div>
    </el-card>

    <!-- 详细统计表 -->
    <el-card v-if="!notFound && deskMateInfo && hasChartData" class="stats-table-card">
      <template #header>
        <span>各任务学习明细</span>
      </template>
      <el-table :data="statsTableData" stripe style="width: 100%;">
        <el-table-column prop="name" label="任务名称" min-width="140" />
        <el-table-column prop="time" label="学习时长" width="120" align="center">
          <template #default="{ row }">
            <span style="font-weight: 600; color: #409EFF;">{{ row.time }}</span>
          </template>
        </el-table-column>
        <el-table-column label="占比" width="200" align="center">
          <template #default="{ row }">
            <div style="display: flex; align-items: center; gap: 8px;">
              <el-progress
                :percentage="row.percent"
                :stroke-width="10"
                :format="(p: number) => p + '%'"
                style="width: 120px;"
              />
            </div>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 饼状图 -->
    <el-card v-if="!notFound && deskMateInfo && hasChartData" class="chart-card">
      <template #header>
        <span>时间分布图</span>
      </template>
      <div ref="pieChartRef" class="pie-chart"></div>
    </el-card>

    <!-- 无数据提示 -->
    <el-card v-if="!notFound && deskMateInfo && !hasChartData" class="empty-card">
      <div class="empty-tip">
        <i class="el-icon-warning-outline"></i>
        <p>{{ deskMateInfo.nickname }} 今日暂无学习记录</p>
      </div>
    </el-card>
  </div>
</template>

<script lang="ts" setup>
import { ref, computed, onBeforeUnmount, nextTick } from 'vue';
import { ElMessage } from 'element-plus';
import * as echarts from 'echarts';
import { get } from '@/api/index';

interface DeskMateInfo {
  userId: number;
  nickname: string;
  totalSeconds: number;
  totalMinutes: number;
  taskSeconds: Record<string, number>;
  recordCount: number;
}

const searchNickname = ref('');
const loading = ref(false);
const searched = ref(false);
const notFound = ref(false);
const deskMateInfo = ref<DeskMateInfo | null>(null);
const pieChartRef = ref<HTMLElement | null>(null);
let pieChart: echarts.ECharts | null = null;

const hasChartData = computed(() => {
  if (!deskMateInfo.value?.taskSeconds) return false;
  return Object.keys(deskMateInfo.value.taskSeconds).length > 0;
});

const statsTableData = computed(() => {
  if (!deskMateInfo.value?.taskSeconds) return [];
  const total = deskMateInfo.value.totalSeconds || 1;
  return Object.entries(deskMateInfo.value.taskSeconds)
    .filter(([, v]) => v > 0)
    .sort((a, b) => b[1] - a[1])
    .map(([name, seconds]) => ({
      name,
      time: formatTime(seconds),
      percent: Math.round((seconds / total) * 100)
    }));
});

const formatTime = (totalSeconds: number): string => {
  if (!totalSeconds || totalSeconds < 0) return '0s';
  const h = Math.floor(totalSeconds / 3600);
  const m = Math.floor((totalSeconds % 3600) / 60);
  const s = totalSeconds % 60;
  const parts: string[] = [];
  if (h > 0) parts.push(`${h}h`);
  if (m > 0) parts.push(`${m}m`);
  if (s > 0 || parts.length === 0) parts.push(`${s}s`);
  return parts.join(' ');
};

const search = async () => {
  if (!searchNickname.value.trim()) {
    ElMessage.warning('请输入昵称');
    return;
  }

  loading.value = true;
  searched.value = false;
  notFound.value = false;
  deskMateInfo.value = null;

  try {
    const res = await get<any>('/deskmate/search', { nickname: searchNickname.value.trim() });

    if (res.code !== 0) {
      notFound.value = true;
      searched.value = true;
      return;
    }

    deskMateInfo.value = res.data;
    searched.value = true;
    notFound.value = false;

    nextTick(() => {
      renderPieChart();
    });
  } catch {
    notFound.value = true;
    searched.value = true;
  } finally {
    loading.value = false;
  }
};

const renderPieChart = () => {
  if (!pieChartRef.value || !deskMateInfo.value?.taskSeconds) return;

  if (!pieChart) {
    pieChart = echarts.init(pieChartRef.value);
  }

  const entries = Object.entries(deskMateInfo.value.taskSeconds).filter(([, v]) => v > 0);
  if (entries.length === 0) {
    pieChart.clear();
    return;
  }

  const colors = ['#409EFF', '#67C23A', '#E6A23C', '#F56C6C', '#909399', '#b37beb', '#36cfc9', '#ff85c0'];
  const data = entries.map(([name, value], i) => ({
    name,
    value,
    itemStyle: { color: colors[i % colors.length] }
  }));

  pieChart.setOption({
    tooltip: {
      trigger: 'item',
      formatter: (p: any) => `${p.name}<br/>${formatTime(p.value)} (${p.percent}%)`
    },
    legend: {
      bottom: 0,
      type: 'scroll'
    },
    series: [{
      type: 'pie',
      radius: ['35%', '65%'],
      center: ['50%', '45%'],
      avoidLabelOverlap: true,
      itemStyle: { borderRadius: 6, borderColor: '#fff', borderWidth: 2 },
      label: {
        show: true,
        formatter: '{b}\n{d}%'
      },
      emphasis: {
        label: { show: true, fontSize: 14, fontWeight: 'bold' }
      },
      data
    }]
  });
};

const handleResize = () => {
  if (pieChart) pieChart.resize();
};

onBeforeUnmount(() => {
  if (pieChart) {
    pieChart.dispose();
    pieChart = null;
  }
});
</script>

<style scoped>
.deskmate {
  display: flex;
  flex-direction: column;
  gap: 16px;
  max-width: 800px;
}

.search-card { border-radius: 12px; }

.card-header {
  font-weight: bold;
  font-size: 16px;
}

.search-area {
  display: flex;
  gap: 12px;
  align-items: center;
  flex-wrap: wrap;
}

.result-tip {
  margin-top: 12px;
  font-size: 14px;
}

.result-tip .not-found {
  color: #F56C6C;
}

.result-tip .found {
  color: #67C23A;
  display: flex;
  align-items: center;
  gap: 6px;
}

.info-card, .chart-card, .empty-card { border-radius: 12px; }

.desk-header {
  display: flex;
  align-items: center;
  gap: 16px;
  margin-bottom: 20px;
}

.desk-avatar {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  background: linear-gradient(135deg, #409EFF, #67C23A);
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 28px;
}

.desk-name {
  font-size: 22px;
  font-weight: bold;
  color: #303133;
}

.desk-sub {
  font-size: 13px;
  color: #909399;
  margin-top: 4px;
}

.stats-row {
  display: flex;
  gap: 32px;
  padding: 16px;
  background: #f5f7fa;
  border-radius: 10px;
}

.stat-item { text-align: center; }

.stat-value {
  font-size: 28px;
  font-weight: bold;
  color: #409EFF;
}

.stat-label {
  font-size: 13px;
  color: #909399;
  margin-top: 4px;
}

.pie-chart {
  width: 100%;
  height: 320px;
}

.stats-table-card { border-radius: 12px; }

.empty-card { border-radius: 12px; }

.empty-tip {
  text-align: center;
  padding: 32px 0;
  color: #909399;
}

.empty-tip .el-icon-warning-outline {
  font-size: 48px;
  margin-bottom: 12px;
  display: block;
}

.empty-tip p {
  margin: 0;
  font-size: 15px;
}
</style>
