package org.fox.mooc.util;

import javax.servlet.http.HttpServletRequest;

/**
 * 处理注册时前端传递的数据：如各种类型的转换等
 * @author fox
 */
public class HttpServletRequestUtil {
    /**
     * 对象类型转换为Integer
     */
    public static int getInteger(HttpServletRequest request,String key) {
        try {
            return Integer.decode(request.getParameter(key));
        }
        catch(Exception e) {
            return -1;
        }
    }
    /**
     * 对象类型转换为long
     */
    public static long getLong(HttpServletRequest request,String key) {
        try {
            return Long.valueOf(request.getParameter(key));
        }
        catch(Exception e) {
            return -1;
        }
    }
    /**
     * 对象类型转换为Double
     */
    public static Double getDouble(HttpServletRequest request,String key) {
        try {
            return Double.valueOf(request.getParameter(key));
        }
        catch(Exception e) {
            return -1d;
        }
    }
    /**
     * 对象类型转换为boolean
     */
    public static boolean getBoolean(HttpServletRequest request,String key) {
        try {
            return Boolean.valueOf(request.getParameter(key));
        }
        catch(Exception e) {
            return false;
        }
    }
    /**
     * 对象类型转换为String,并处理字符串前后空格
     */
    public static String getString(HttpServletRequest request,String key) {
        try {
            String result = request.getParameter(key);
            if(result!=null) {
                //返回字符串的副本，忽略前部空白和尾部空白
                result = result.trim();
            }
            return result;
        }
        catch(Exception e) {
            return null;
        }
    }
}

