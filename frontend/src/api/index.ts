import axios, { type InternalAxiosRequestConfig, type AxiosResponse } from 'axios';
import store from '@/store';
import type { ApiResponse } from '@/types/api';

const instance = axios.create({
  baseURL: '/api',
  timeout: 120000
});

instance.interceptors.request.use((config: InternalAxiosRequestConfig) => {
  // 移除对 baseURL 的依赖，直接检查是否以 /api 开头
  const url = (config as any).url || '';
  if (url.startsWith('/')) {
    const token = store.state.user.token;
    const userId = store.state.user.userId;
    if (config.headers) {
      if (token) {
        config.headers.Authorization = `Bearer ${token}`;
      }
      // userId > 0 才发 X-User-Id（避免发 0）
      if (userId > 0) {
        config.headers['X-User-Id'] = String(userId);
      }
    }
  }
  return config;
});

instance.interceptors.response.use(
  (response: AxiosResponse) => response,
  (error: any) => {
    if (error.config?.url?.startsWith('/') && error.response?.status === 401) {
      localStorage.clear();
      if (window.location.pathname !== '/login') {
        window.location.href = '/login';
      }
    }
    return Promise.reject(error);
  }
);

export function get<T>(url: string, params?: Record<string, unknown>): Promise<ApiResponse<T>> {
  return instance.get<ApiResponse<T>>(url, { params }).then((res: AxiosResponse<ApiResponse<T>>) => res.data);
}

export function post<T>(url: string, data?: unknown, config?: { params?: Record<string, unknown> }): Promise<ApiResponse<T>> {
  return instance.post<ApiResponse<T>>(url, data, config).then((res: AxiosResponse<ApiResponse<T>>) => res.data);
}

export function put<T>(url: string, data?: unknown): Promise<ApiResponse<T>> {
  return instance.put<ApiResponse<T>>(url, data).then((res: AxiosResponse<ApiResponse<T>>) => res.data);
}

export function del<T>(url: string): Promise<ApiResponse<T>> {
  return instance.delete<ApiResponse<T>>(url).then((res: AxiosResponse<ApiResponse<T>>) => res.data);
}
