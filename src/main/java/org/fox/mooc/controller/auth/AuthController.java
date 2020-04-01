package org.fox.mooc.controller.auth;

import org.fox.mooc.dao.auth.AuthUserDao;
import org.fox.mooc.entity.AuthUser;
import org.fox.mooc.entity.course.Course;
import org.fox.mooc.service.course.CourseService;
import org.fox.mooc.service.auth.AuthUserService;
import org.fox.mooc.util.HttpServletRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @Author by fairyfox
 * *2020/2/24-12:19
 */
@Controller
@RequestMapping(value = "/auth")
public class AuthController {
    @Autowired
    private AuthUserDao authUserDao ;

    @Autowired
    private AuthUserService authUserService;

    @Autowired
    private CourseService courseService;

    /**
     * 根据课程id获取当前讲师信息
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/teacherinfo", method = RequestMethod.GET)
    public Map<String, Object> TeacherInfo(HttpServletRequest request){
        Map<String,Object> resultMap = new HashMap<>();
        Long id = HttpServletRequestUtil.getLong(request, "id");
        try {
            Course course = courseService.getById(id);
            AuthUser authUser = authUserDao.getByAccount(course.getAccount());
            resultMap.put("success", true);
            resultMap.put("authuser", authUser);
        } catch (Exception e) {
            resultMap.put("success", false);
            resultMap.put("message", e.getMessage());
        }
        return resultMap;
    }
    /**
     * 根据权重显示首页的五位讲师
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/teacherlist", method = RequestMethod.GET)
    public Map<String, Object> TeacherList(HttpServletRequest request){
        Map<String,Object> resultMap = new HashMap<>();
        try {
            List<AuthUser> authUser = authUserService.queryRecomd();
            resultMap.put("success", true);
            resultMap.put("authuser", authUser);
        } catch (Exception e) {
            resultMap.put("success", false);
            resultMap.put("message", e.getMessage());
        }
        return resultMap;
    }
}
