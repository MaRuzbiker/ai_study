import { get, post, put, del } from '@/api/index';
import type { ApiResponse } from '@/types/api';

// 今日任务数据（用于备份和恢复）
export interface TodayTaskDTO {
  id?: number;
  taskId: number;
  title: string;
  todayTargetMinutes: number;
  accumulatedSeconds: number;
  done: boolean;
}

// 添加任务到今日任务
export const addTodayTask = (task: TodayTaskDTO): Promise<ApiResponse<TodayTaskDTO>> => {
  return post('/today-tasks', task);
};

// 更新今日任务
export const updateTodayTask = (id: number, task: TodayTaskDTO): Promise<ApiResponse<TodayTaskDTO>> => {
  return put(`/today-tasks/${id}`, task);
};

// 批量更新今日任务
export const batchUpdateTodayTasks = (tasks: TodayTaskDTO[]): Promise<ApiResponse<void>> => {
  return put('/today-tasks/batch', tasks);
};

// 删除今日任务
export const deleteTodayTask = (id: number): Promise<ApiResponse<void>> => {
  return del(`/today-tasks/${id}`);
};

// 同步今日任务到后端
export const syncTodayTasks = (tasks: TodayTaskDTO[]): Promise<ApiResponse<void>> => {
  return post('/today-tasks/sync', tasks);
};

// 从后端获取今日任务
export const getTodayTasksFromBackend = (date?: string): Promise<ApiResponse<TodayTaskDTO[]>> => {
  return get('/today-tasks', date ? { date } : undefined);
};
