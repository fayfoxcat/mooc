package org.fox.mooc.controller.course;

import org.fox.mooc.dto.CourseQueryDTO;
import org.fox.mooc.entity.AuthUser;
import org.fox.mooc.entity.course.Course;
import org.fox.mooc.entity.course.CourseComment;
import org.fox.mooc.service.course.CourseClassifyService;
import org.fox.mooc.service.course.CourseCommentService;
import org.fox.mooc.service.course.CourseService;
import org.fox.mooc.service.course.CourseSiteCarouselService;
import org.fox.mooc.util.HttpServletRequestUtil;
import org.fox.mooc.util.PageCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author by fairyfox
 * *2020/2/21-15:31
 */
@Controller
@RequestMapping(value = "/course")
class CourseController {

    @Autowired
    private CourseClassifyService courseClassifyService;

    @Autowired
    private CourseSiteCarouselService courseSiteCarouselService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private CourseCommentService courseCommentService;

    /**
     * 根据id获取当前课程
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/onecourseinfo", method = RequestMethod.GET)
    private Map<String,Object> OneCourseInfo(HttpServletRequest request){
        Map<String,Object> resultMap = new HashMap<>();
        Long id = HttpServletRequestUtil.getLong(request, "id");
        try {
            Course course = courseService.getById(id);
            resultMap.put("success",true);
            resultMap.put("course",course);
        } catch (Exception e) {
            resultMap.put("success",false);
            resultMap.put("message",e.getMessage());
        }
        return resultMap;
    }

    /**
     * 分类获取课程
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/courselist",method = RequestMethod.GET)
    private Map<String,Object> CourseList(HttpServletRequest request) {
        Map<String,Object> resultMap = new HashMap<>();
        CourseQueryDTO courseQueryDTO = new CourseQueryDTO();
        //讲师id
        String account = HttpServletRequestUtil.getString(request,"account");
        //一级分类
        String topClassify = HttpServletRequestUtil.getString(request, "topClassify");
        //二级分类
        String subClassify = HttpServletRequestUtil.getString(request, "subClassify");
        //排序方式
        String sort = HttpServletRequestUtil.getString(request,"sort");
        //课程状态
        int status = HttpServletRequestUtil.getInteger(request,"status");
        //关键词(课程名、讲师名)
        String keyword = HttpServletRequestUtil.getString(request, "keyword");
        //当前页码
        int pageIndex = HttpServletRequestUtil.getInteger(request,"pageIndex");
        //页面大小
        int pageSize = HttpServletRequestUtil.getInteger(request,"pageSize");
        try {
            courseQueryDTO.setAccount(account);
            courseQueryDTO.setClassify(topClassify);
            courseQueryDTO.setSubClassify(subClassify);
            courseQueryDTO.setSortField(sort);
            /*默认设置1*/
            courseQueryDTO.setOnsale(1);
            courseQueryDTO.setCourseName(keyword);
            courseQueryDTO.setOwnerName(keyword);
            courseQueryDTO.setRowIndex(PageCalculator.calculateRowIndex(pageIndex,pageSize));
            courseQueryDTO.setPageSize(pageSize);
            List<Course> courseList = courseService.queryList(courseQueryDTO);
            resultMap.put("success",true);
            resultMap.put("courseList",courseList);
        }  catch (Exception e) {
            resultMap.put("success",false);
            resultMap.put("message",e.getMessage());
        }
        return resultMap;
    }

    /**
     * 获取当前课程评论&答疑
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/commentlist", method = RequestMethod.GET)
    private Map<String,Object> CommentList(HttpServletRequest request){
        Map<String,Object> resultMap = new HashMap<>();
        //获取当前课程id
        Long courseId = HttpServletRequestUtil.getLong(request, "id");
        //获取评论类型(评论&答疑)
        int type = HttpServletRequestUtil.getInteger(request,"type");
        //获取当前页
        int pageIndex = HttpServletRequestUtil.getInteger(request, "pageIndex");
        //获取页面大小
        int pageSize = HttpServletRequestUtil.getInteger(request, "pageSize");
        //计算起始行
        Integer rowIndex = PageCalculator.calculateRowIndex(pageIndex, pageSize);
        CourseComment courseComment = new CourseComment();
        courseComment.setCourseId(courseId);
        courseComment.setType(type);
        try {
            //获取评论总数量
            int count = courseCommentService.getTotalItemsCount(courseComment);
            //加载评论
            List<CourseComment> courseCommentList = courseCommentService.queryPage(courseComment, rowIndex, pageSize);
            resultMap.put("success",true);
            resultMap.put("count",count);
            resultMap.put("courseCommentList",courseCommentList);
        } catch (Exception e) {
            resultMap.put("success",false);
            resultMap.put("message",e.getMessage());
        }
        return resultMap;
    }

    /**
     * 添加评论答疑
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/addcomment", method = RequestMethod.POST)
    private Map<String,Object> AddComment(@RequestBody CourseComment courseComment, HttpServletRequest request){
        Map<String,Object> resultMap = new HashMap<>();
        try {
            AuthUser authUser = (AuthUser)request.getSession().getAttribute("userinfo");
            if (authUser != null) {
                courseComment.setAccount(authUser.getAccount());
                courseCommentService.createSelectivity(courseComment,authUser);
                resultMap.put("success",true);
                resultMap.put("message","发布成功");
            } else {
                resultMap.put("success", false);
                resultMap.put("message","登录失效！");
            }
        } catch (Exception e) {
            resultMap.put("success",false);
            resultMap.put("message",e.getMessage());
        }
        return resultMap;
    }

}
