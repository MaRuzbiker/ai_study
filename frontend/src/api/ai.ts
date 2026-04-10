// src/api/ai.ts
import { get, post } from './index';
import type { ApiResponse, AiTaskPlanRequest, AiTaskPlanResponse, AiModel } from '@/types/api';

// 获取最近 7 天学习建议
export const getWeeklySuggestion = (model?: string): Promise<ApiResponse<string>> => {
  return get<string>('/ai/suggestion/weekly', { model });
};

// 根据目标生成任务计划
export const generateTaskPlan = (data: AiTaskPlanRequest): Promise<ApiResponse<AiTaskPlanResponse>> => {
  return post<AiTaskPlanResponse>('/ai/tasks/plan', data);
};

// 获取可用的 AI 模型列表
export const getAvailableModels = (): Promise<ApiResponse<AiModel[]>> => {
  return get<AiModel[]>('/ai/models');
};

// 分析简历生成学习建议
export const analyzeResume = (data: any): Promise<ApiResponse<string>> => {
  return post<string>('/ai/resume/analyze', data);
};




