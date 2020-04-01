package org.fox.mooc.util;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author by fairyfox
 * *2020/4/1-15:14
 * 用户登录ip处理
 */
public class IpUtil {

    public static String getIP(HttpServletRequest request) {
        try {
            //获取用户登录ip;
            String loginIp = request.getHeader("X-Forwarded-For");
            if (loginIp == null || loginIp.length() == 0 || "unknown".equalsIgnoreCase(loginIp)) {
                loginIp = request.getHeader("X-Real-IP");
            }
            if (loginIp == null || loginIp.length() == 0 || "unknown".equalsIgnoreCase(loginIp)) {
                loginIp = request.getHeader("Proxy-Client-IP");
            }
            if (loginIp == null || loginIp.length() == 0 || "unknown".equalsIgnoreCase(loginIp)) {
                loginIp = request.getHeader("WL-Proxy-Client-IP");
            }
            if (loginIp == null || loginIp.length() == 0 || "unknown".equalsIgnoreCase(loginIp)) {
                loginIp = request.getHeader("HTTP_CLIENT_IP");
            }
            if (loginIp == null || loginIp.length() == 0 || "unknown".equalsIgnoreCase(loginIp)) {
                loginIp = request.getRemoteAddr();
            }
            return loginIp;
        }
        catch(Exception e) {
            return null;
        }
    }
}
