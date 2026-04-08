<template>
  <div class="today-tasks">
    <!-- ===== 今日任务列表 ===== -->
    <el-card class="main-card">
      <template #header>
        <div class="card-header">
          <span>今日任务</span>
          <span class="total-time">总学习: {{ formatTime(todayTotalSeconds) }}</span>
        </div>
      </template>

      <div class="target-setting">
        <el-form :inline="true">
          <el-form-item label="今日目标时长：">
            <el-input-number v-model="dailyTargetMinutes" :min="0" :max="1440" :step="10" @change="saveTarget" />
            <span style="margin-left: 8px;">分钟</span>
          </el-form-item>
        </el-form>
      </div>

      <div class="today-summary">
        今日目标：{{ dailyTargetMinutes }} 分钟 ｜ 已学习：{{ formatTime(todayTotalSeconds) }}
        <el-progress :percentage="progressPercent" :stroke-width="8" style="margin-top:8px;" />
      </div>

      <el-divider />

      <div class="task-list">
        <div v-for="item in dailyTodos" :key="item.id" class="task-item" :class="{ 'is-done': item.done }">
          <div class="task-left">
            <el-checkbox v-model="item.done" @change="() => toggleToday(item)">
              <div class="task-title">{{ item.text }}</div>
            </el-checkbox>
            <div class="task-meta">
              <span class="meta-item">
                <i class="el-icon-time"></i>
                目标 {{ formatTime((item.targetHours || 0) * 3600) }}
              </span>
              <span class="meta-item learned">
                <i class="el-icon-check"></i>
                已学 {{ formatTime(item.accumulatedSeconds || 0) }}
              </span>
            </div>
          </div>
          <div class="task-right">
            <el-button size="small" type="primary" @click="openTimer(item)">
              <i class="el-icon-timer"></i> 计时
            </el-button>
            <el-button size="small" type="danger" text @click="removeToday(item.id)">
              删除
            </el-button>
          </div>
        </div>
        <div v-if="dailyTodos.length === 0" class="empty-tip">
          暂无今日任务，请前往"学习任务"页面添加任务
        </div>
      </div>
    </el-card>

    <!-- ===== 时间分布饼图 ===== -->
    <el-card v-if="todayTotalSeconds > 0" class="chart-card">
      <template #header>
        <span>今日时间分布</span>
      </template>
      <div ref="pieChartRef" class="pie-chart"></div>
    </el-card>

    <!-- ===== 计时器弹窗 ===== -->
    <el-dialog v-model="showTimerDialog" :title="'计时 - ' + (timerTask?.text || '')" width="420px" :close-on-click-modal="false" :before-close="onBeforeClose">
      <div class="timer-display">
        <div class="timer-time">{{ formatTime(timerSeconds) }}</div>
        <div class="timer-task-name">{{ timerTask?.text }}</div>
        <div class="timer-accumulated">
          本次: {{ formatTime(timerSeconds) }} ｜ 总累计: {{ formatTime((timerTask?.accumulatedSeconds || 0) + timerSeconds) }}
        </div>
      </div>

      <div class="timer-controls">
        <template v-if="timerState === 'running'">
          <el-button type="warning" size="large" @click="pauseTimer" round>
            <i class="el-icon-video-pause"></i> 暂停
          </el-button>
          <el-button type="danger" size="large" @click="stopTimer" round>
            <i class="el-icon-circle-close"></i> 停止并保存
          </el-button>
        </template>
        <template v-if="timerState === 'paused'">
          <el-button type="primary" size="large" @click="resumeTimer" round>
            <i class="el-icon-video-play"></i> 继续计时
          </el-button>
          <el-button type="danger" size="large" @click="stopTimer" round>
            <i class="el-icon-circle-close"></i> 停止并保存
          </el-button>
        </template>
      </div>
    </el-dialog>
  </div>
</template>

<script lang="ts" setup>
import { ref, computed, onMounted, onBeforeUnmount, watch, nextTick } from 'vue';
import { ElMessage } from 'element-plus';
import * as echarts from 'echarts';
import { updateTask } from '@/api/task';
import { createRecord } from '@/api/record';

/* ── Constants ── */
const STORAGE_TODOS = 'dashboard_today_todos';
const STORAGE_TARGET = 'dashboard_today_target';
const STORAGE_WEEK = 'dashboard_week_info';

/* ── State ── */
const dailyTodos = ref<Array<any>>([]);
const dailyTargetMinutes = ref<number>(120);
const todayTotalSeconds = ref<number>(0);
const pieChartRef = ref<HTMLElement | null>(null);
let pieChart: echarts.ECharts | null = null;

// Timer state
const showTimerDialog = ref(false);
const timerTask = ref<any>(null);
const timerSeconds = ref(0);
const timerState = ref<'idle' | 'running' | 'paused'>('idle');
let timerInterval: ReturnType<typeof setInterval> | null = null;

/* ── Helpers ── */
const getTodayDateStr = (): string => {
  const d = new Date();
  return `${d.getFullYear()}-${(d.getMonth() + 1).toString().padStart(2, '0')}-${d.getDate().toString().padStart(2, '0')}`;
};

// 格式化秒数为 Xh Ym Zs
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

const progressPercent = computed(() => {
  if (dailyTargetMinutes.value <= 0) return 0;
  const targetSeconds = dailyTargetMinutes.value * 60;
  return Math.min(100, Math.round((todayTotalSeconds.value / targetSeconds) * 100));
});

/* ── Week info ── */
type WeekInfo = { startDate: string; seconds: number };

const loadWeekInfo = (): WeekInfo => {
  const raw = localStorage.getItem(STORAGE_WEEK);
  let info: WeekInfo;
  try {
    info = raw ? JSON.parse(raw) : null;
  } catch { info = null; }
  if (!info) info = { startDate: getTodayDateStr(), seconds: 0 };

  const start = new Date(info.startDate);
  const today = new Date(getTodayDateStr());
  const diffDays = (today.getTime() - start.getTime()) / (1000 * 60 * 60 * 24);
  if (diffDays >= 7 || diffDays < 0) {
    info = { startDate: getTodayDateStr(), seconds: 0 };
  }
  localStorage.setItem(STORAGE_WEEK, JSON.stringify(info));
  return info;
};

const updateWeekSecondsByDelta = (deltaSeconds: number) => {
  if (!deltaSeconds) return;
  const info = loadWeekInfo();
  info.seconds = Math.max(0, (info.seconds || 0) + deltaSeconds);
  localStorage.setItem(STORAGE_WEEK, JSON.stringify(info));
};

/* ── Data ── */
const loadDailyData = () => {
  try {
    const todos = localStorage.getItem(STORAGE_TODOS);
    const target = localStorage.getItem(STORAGE_TARGET);
    if (todos) dailyTodos.value = JSON.parse(todos);
    if (target) dailyTargetMinutes.value = Number(target);

    // Recalculate total seconds
    recalcTodaySeconds();
  } catch (error) {
    console.error('load error:', error);
  }
};

const recalcTodaySeconds = () => {
  todayTotalSeconds.value = dailyTodos.value.reduce(
    (sum: number, t: any) => sum + (t.accumulatedSeconds || 0), 0
  );
};

const saveDailyData = () => {
  localStorage.setItem(STORAGE_TODOS, JSON.stringify(dailyTodos.value));
  localStorage.setItem(STORAGE_TARGET, String(dailyTargetMinutes.value));
};

const saveTarget = () => {
  localStorage.setItem(STORAGE_TARGET, String(dailyTargetMinutes.value));
};

const createStudyRecord = async (taskId: number, seconds: number) => {
  try {
    if (!taskId || !seconds) return;
    const minutes = Math.ceil(seconds / 60);
    await createRecord({ taskId, studyDate: getTodayDateStr(), durationMinutes: minutes, comment: '计时学习' });
  } catch (e) { console.error(e); }
};

/* ── Task actions ── */
const toggleToday = (item: any) => {
  saveDailyData();
};

const removeToday = (id: string) => {
  dailyTodos.value = dailyTodos.value.filter((t: any) => t.id !== id);
  recalcTodaySeconds();
  saveDailyData();
  ElMessage.success('已删除');
  renderPieChart();
};

/* ── Timer ── */
const openTimer = (item: any) => {
  timerTask.value = item;
  timerSeconds.value = 0;
  timerState.value = 'running';
  showTimerDialog.value = true;
  // Auto start timer
  const startTs = Date.now();
  if (timerInterval) clearInterval(timerInterval);
  timerInterval = setInterval(() => {
    timerSeconds.value = Math.floor((Date.now() - startTs) / 1000);
  }, 200);
};

const pauseTimer = () => {
  timerState.value = 'paused';
  if (timerInterval) { clearInterval(timerInterval); timerInterval = null; }
};

const resumeTimer = () => {
  const offsetMs = timerSeconds.value * 1000;
  const resumeTs = Date.now();
  timerState.value = 'running';
  timerInterval = setInterval(() => {
    timerSeconds.value = Math.floor((offsetMs + (Date.now() - resumeTs)) / 1000);
  }, 200);
};

const stopTimer = () => {
  if (timerInterval) { clearInterval(timerInterval); timerInterval = null; }
  timerState.value = 'idle';

  // Save accumulated seconds to task
  const addedSeconds = timerSeconds.value;
  if (addedSeconds > 0 && timerTask.value) {
    const task = dailyTodos.value.find((t: any) => t.id === timerTask.value.id);
    if (task) {
      task.accumulatedSeconds = (task.accumulatedSeconds || 0) + addedSeconds;
      recalcTodaySeconds();
      saveDailyData();
      updateWeekSecondsByDelta(addedSeconds);
      createStudyRecord(task.taskId, addedSeconds);
      renderPieChart();
      ElMessage.success(`已记录 ${formatTime(addedSeconds)} 学习时间`);
    }
  }

  timerSeconds.value = 0;
  showTimerDialog.value = false;
};

const onBeforeClose = (done: () => void) => {
  if (timerSeconds.value > 0) {
    // 有计时数据时，自动执行停止并保存
    stopTimer();
  }
  done();
};

const onTimerClosed = () => {
  if (timerInterval) { clearInterval(timerInterval); timerInterval = null; }
  timerState.value = 'idle';
  timerSeconds.value = 0;
};

/* ── Pie Chart ── */
const renderPieChart = () => {
  if (!pieChartRef.value) return;
  if (!pieChart) {
    pieChart = echarts.init(pieChartRef.value);
  }

  const tasksWithTime = dailyTodos.value.filter((t: any) => (t.accumulatedSeconds || 0) > 0);
  if (tasksWithTime.length === 0) {
    pieChart.clear();
    return;
  }

  const colors = ['#409EFF', '#67C23A', '#E6A23C', '#F56C6C', '#909399', '#b37beb', '#36cfc9', '#ff85c0'];
  const data = tasksWithTime.map((t: any, i: number) => ({
    name: t.text.length > 12 ? t.text.substring(0, 12) + '...' : t.text,
    value: t.accumulatedSeconds || 0, // 直接用秒
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

/* ── Lifecycle ── */
onMounted(() => {
  loadDailyData();
  nextTick(() => { renderPieChart(); });
  window.addEventListener('resize', handleResize);
});

onBeforeUnmount(() => {
  if (timerInterval) clearInterval(timerInterval);
  if (pieChart) { pieChart.dispose(); pieChart = null; }
  window.removeEventListener('resize', handleResize);
});

const handleResize = () => {
  if (pieChart) pieChart.resize();
};

watch(showTimerDialog, (val) => {
  if (!val) nextTick(() => setTimeout(() => renderPieChart(), 300));
});
</script>

<style scoped>
.today-tasks {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.main-card, .chart-card {
  border-radius: 12px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-weight: bold;
}

.total-time {
  font-size: 13px;
  color: #409EFF;
  font-weight: normal;
}

.target-setting { margin-bottom: 12px; }

.today-summary {
  font-size: 14px;
  color: #606266;
  padding: 12px;
  background: #f5f7fa;
  border-radius: 8px;
}

.task-list { display: flex; flex-direction: column; gap: 10px; }

.task-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 16px;
  border: 1px solid #ebeef5;
  border-radius: 10px;
  transition: all 0.2s;
}

.task-item:hover {
  border-color: #409EFF;
  background: #f0f9ff;
}

.task-item.is-done {
  background: #f0f9eb;
  border-color: #e1f3d8;
}

.task-left { flex: 1; min-width: 0; }

.task-title {
  font-size: 14px;
  color: #303133;
  margin-left: 8px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.task-meta {
  display: flex;
  gap: 14px;
  margin-top: 4px;
  margin-left: 24px;
  font-size: 12px;
  color: #909399;
}

.meta-item { display: flex; align-items: center; gap: 3px; }
.meta-item.learned { color: #67C23A; font-weight: 500; }

.task-right {
  display: flex;
  gap: 6px;
  align-items: center;
  flex-shrink: 0;
}

.empty-tip {
  text-align: center;
  color: #909399;
  padding: 40px 0;
}

/* Timer Dialog */
.timer-display { text-align: center; padding: 20px 0; }

.timer-time {
  font-size: 56px;
  font-weight: bold;
  font-variant-numeric: tabular-nums;
  color: #303133;
  letter-spacing: 2px;
}

.timer-task-name {
  font-size: 16px;
  color: #606266;
  margin-top: 8px;
}

.timer-accumulated {
  font-size: 13px;
  color: #909399;
  margin-top: 6px;
}

.timer-controls {
  display: flex;
  justify-content: center;
  gap: 16px;
  margin-top: 24px;
}

.timer-controls .el-button {
  min-width: 130px;
}

/* Pie Chart */
.pie-chart {
  width: 100%;
  height: 320px;
}
</style>
