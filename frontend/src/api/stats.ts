// src/api/stats.ts
import { get } from './index';
import type { ApiResponse } from '@/types/api';

export interface TodayStats {
  totalSeconds: number;
  totalMinutes: number;
  taskCount: number;
  doneTaskCount: number;
  undoneTaskCount: number;
}

export const getTodayStats = (): Promise<ApiResponse<TodayStats>> => {
  return get<TodayStats>('/stats/today');
};
