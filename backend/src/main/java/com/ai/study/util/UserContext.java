package com.ai.study.util;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class UserContext {

    private static final String USER_ID_HEADER = "X-User-Id";

    /**
     * 获取当前用户ID。
     * 有 X-User-Id header 时返回实际值；未登录时默认返回 1L（向后兼容）。
     */
    public static Long getUserId() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            String userIdStr = request.getHeader(USER_ID_HEADER);
            if (userIdStr != null && !userIdStr.trim().isEmpty()) {
                try {
                    long userId = Long.parseLong(userIdStr.trim());
                    if (userId > 0) return userId; // 有效的用户ID
                } catch (NumberFormatException ignored) {
                }
            }
        }
        // 默认返回 1L（向后兼容旧逻辑；前端已修复，登录用户会正确发 header）
        return 1L;
    }
}
