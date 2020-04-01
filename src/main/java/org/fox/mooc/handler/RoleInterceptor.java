package org.fox.mooc.handler;

import org.fox.mooc.entity.AuthUser;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @Author by fairyfox
 * *2020/3/31-14:31
 */
@Component
public class RoleInterceptor  implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) throws Exception {
        //获取session
        AuthUser authUser = (AuthUser) request.getSession().getAttribute("userinfo");
        //判断session里是不是有登录信息
        if (authUser!=null){
            //请求路径
            String urlPath = request.getServletPath();
            if (urlPath.contains("/admin")){
                if(authUser.getRole()==0) {
                    return true;
                }else{
                    response.sendRedirect("/mooc/login.html");
                    return false;
                }
            }else if (urlPath.contains("/coursemanage")){
                if(authUser.getRole()==0||authUser.getRole()==2) {
                    return true;
                }else{
                    response.sendRedirect("/mooc/teachinfo.html");
                    return false;
                }
            }else return urlPath.contains("/personinfo");
        }else{
            response.sendRedirect("/mooc/index.html");
            return false;
        }
    }

    @Override
    public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
            throws Exception {}

    @Override
    public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
            throws Exception {}
}
