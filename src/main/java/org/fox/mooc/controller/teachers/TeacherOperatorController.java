package org.fox.mooc.controller.teachers;

import org.apache.commons.collections4.CollectionUtils;
import org.fox.mooc.dto.CourseQueryDTO;
import org.fox.mooc.dto.ImageDTO;
import org.fox.mooc.entity.AuthUser;
import org.fox.mooc.entity.course.Course;
import org.fox.mooc.entity.course.CourseSection;
import org.fox.mooc.service.course.CourseSectionService;
import org.fox.mooc.service.course.CourseService;
import org.fox.mooc.service.auth.AuthUserService;
import org.fox.mooc.util.Codeutil;
import org.fox.mooc.util.HttpServletRequestUtil;
import org.fox.mooc.util.ImageUtil;
import org.fox.mooc.util.PageCalculator;
import org.fox.mooc.vo.CourseSectionVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.regex.Pattern;

/**
 * @Author by fairyfox
 * *2020/3/2-19:28
 */
@Controller
@RequestMapping(value = "/teacher")
class TeacherOperatorController {

    @Autowired
    private AuthUserService authUserService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private CourseSectionService courseSectionService;

    /**
     * 用户提交申请
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/applyinfo", method = RequestMethod.POST)
    private Map<String,Object> ApplyInfo(HttpServletRequest request){
        Map<String,Object> resultMap = new HashMap<>();
        ImageDTO imageDTO = null;
        AuthUser authUser = (AuthUser)request.getSession().getAttribute("userinfo");
        String realName = HttpServletRequestUtil.getString(request, "realname");
        String education = HttpServletRequestUtil.getString(request, "education");
        String college = HttpServletRequestUtil.getString(request, "college");
        String title = HttpServletRequestUtil.getString(request, "title");
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        //判读验证码是否正确
        if (!Codeutil.checkVerifyCode(request)) {
            resultMap.put("success",false);
            resultMap.put("message","输入了错误的验证码!");
            return resultMap;
        }
        //判断文件流是否存在
        try {
            if (authUser != null) {
                if (authUser.getRole()!=2){
                    if(multipartResolver.isMultipart(request)) {
                        imageDTO = ImageUtil.Image(request, imageDTO);
                    }else {
                        resultMap.put("success",false);
                        resultMap.put("message","资格凭证不能为空");
                    }
                    authUser.setRealname(realName);
                    authUser.setEducation(education);
                    authUser.setCollegeName(college);
                    authUser.setTitle(title);
                    authUserService.applyupdate(authUser,imageDTO);
                    resultMap.put("success",true);
                    resultMap.put("message","申请已提交，将在2-3工作日完成审核，注意个人中心消息通知");
                }else{
                    resultMap.put("success", false);
                    resultMap.put("message","您已是讲师，可直接在课程发布中管理发布课程，无需重复提交！");
                }
            }else{
                resultMap.put("success", false);
                resultMap.put("message","登录失效！");
            }
        }catch(Exception e) {
            resultMap.put("success",false);
            resultMap.put("message",e.getMessage());
        }
        return resultMap;
    }

    /**
     * 创建课程
     */
    @ResponseBody
    @RequestMapping(value = "/createcourse", method = RequestMethod.POST)
    private Map<String,Object> CreateCourse(HttpServletRequest request){
        Map<String,Object> resultMap = new HashMap<>();
        ImageDTO imageDTO = null;
        Course course = new Course();
        String courseName = HttpServletRequestUtil.getString(request, "courseName");
        String topClassify = HttpServletRequestUtil.getString(request, "topClassify");
        String topClassifyName = HttpServletRequestUtil.getString(request, "topClassifyName");
        String subClassify = HttpServletRequestUtil.getString(request, "subClassify");
        String subClassifyName = HttpServletRequestUtil.getString(request, "subClassifyName");
        String owner = HttpServletRequestUtil.getString(request, "owner");
        String time = HttpServletRequestUtil.getString(request, "time");
        String brief = HttpServletRequestUtil.getString(request, "brief");
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        try{
            AuthUser authUser = (AuthUser)request.getSession().getAttribute("userinfo");
            if (authUser != null) {
                if (authUser.getRole()==2||authUser.getRole()==0){
                    if(multipartResolver.isMultipart(request)) {
                        imageDTO = ImageUtil.Image(request, imageDTO);
                    }else {
                        resultMap.put("success",false);
                        resultMap.put("message","轮播图不能为空!");
                        return resultMap;
                    }
                    course.setAccount(authUser.getAccount());
                    course.setCourseName(courseName);
                    course.setClassify(topClassify);
                    course.setClassifyName(topClassifyName);
                    course.setSubClassify(subClassify);
                    course.setSubClassifyName(subClassifyName);
                    course.setOwnerName(owner);
                    course.setCourseTime(time);
                    course.setOnsale(1);
                    course.setBrief(brief);
                    course.setCreateUser(authUser.getRealname());
                    course.setUpdateUser(authUser.getRealname());
                    courseService.createSelectivity(authUser,course,imageDTO);
                    resultMap.put("success",true);
                    resultMap.put("courseid",course.getId());
                    resultMap.put("message","创建课程成功！请及时添加章节");
                }else{
                    resultMap.put("success", false);
                    resultMap.put("message","权限不足！违规操作");
                }

            }else{
                resultMap.put("success", false);
                resultMap.put("message","登录失效！");
            }
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
    @RequestMapping(value = "/courselist",method = RequestMethod.POST)
    private Map<String,Object> CourseList(HttpServletRequest request) {
        Map<String,Object> resultMap = new HashMap<>();
        CourseQueryDTO courseQueryDTO = new CourseQueryDTO();
        //当前页码
        int pageIndex = HttpServletRequestUtil.getInteger(request,"pageIndex");
        //页面大小
        int pageSize = HttpServletRequestUtil.getInteger(request,"pageSize");
        AuthUser authUser = (AuthUser)request.getSession().getAttribute("userinfo");
        try {
            if (authUser!=null){
                courseQueryDTO.setAccount(authUser.getAccount());
            }
            courseQueryDTO.setRowIndex(PageCalculator.calculateRowIndex(pageIndex,pageSize));
            courseQueryDTO.setPageSize(pageSize);
            Integer count = courseService.getTotalItemsCount(courseQueryDTO);
            List<Course> courseList = courseService.queryList(courseQueryDTO);
            resultMap.put("success",true);
            resultMap.put("count",count);
            resultMap.put("courseList",courseList);
        }  catch (Exception e) {
            resultMap.put("success",false);
            resultMap.put("message",e.getMessage());
        }
        return resultMap;
    }

    /**
     * 添加章节信息
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/chaptersection", method = RequestMethod.POST)
    private Map<String,Object> ChapterSection(@RequestBody List<CourseSectionVO> courseSections,HttpServletRequest request){
        Map<String,Object> resultMap = new HashMap<>();
        try {
            AuthUser authUser = (AuthUser)request.getSession().getAttribute("userinfo");
            if (authUser != null) {
                if (authUser.getRole()==2||authUser.getRole()==0){
                    if (CollectionUtils.isNotEmpty(courseSections)) {
                        //先获取最大的排序id
                        Integer maxSort = courseSectionService.getMaxSort(courseSections.get(0).getCourseId());

                        for (int i = 0; i < courseSections.size(); i++) {
                            CourseSectionVO tmpVO = courseSections.get(i);
                            if (null == maxSort) {
                                maxSort = 0;
                            }
                            maxSort += (i + 1);
                            CourseSection courseSection = new CourseSection();
                            courseSection.setCourseId(tmpVO.getCourseId());
                            courseSection.setName(tmpVO.getName());
                            courseSection.setParentId(0L);//章的parentId固定为0
                            courseSection.setSort(maxSort);
                            /*管理员未审核，默认未上架状态*/
                            courseSection.setOnsale(0);
                            courseSection.setCreateTime(new Date());
                            courseSection.setUpdateTime(new Date());
                            courseSection.setCreateUser(authUser.getUsername());
                            courseSection.setUpdateUser(authUser.getUsername());
                            courseSection.setDel(0);
                            //创建章
                            courseSectionService.createSelectivity(courseSection);

                            List<CourseSection> subCourseSections = tmpVO.getSections();
                            if (CollectionUtils.isNotEmpty(subCourseSections)) {
                                String totalTime = "00:00";
                                for (int j = 0; j < subCourseSections.size(); j++) {
                                    CourseSection courseSectionTmp = subCourseSections.get(j);
                                    courseSectionTmp.setCourseId(courseSection.getCourseId());
                                    courseSectionTmp.setParentId(courseSection.getId());
                                    courseSectionTmp.setSort(j + 1);

                                    courseSectionTmp.setCreateTime(new Date());
                                    courseSectionTmp.setCreateUser(authUser.getUsername());
                                    courseSectionTmp.setUpdateTime(new Date());
                                    courseSectionTmp.setUpdateUser(authUser.getUsername());
                                    courseSectionTmp.setOnsale(1);
                                    courseSectionTmp.setDel(0);

                                    Pattern p = Pattern.compile("^([0-5][0-9]):([0-5][0-9])$");
                                    if (!p.matcher(courseSectionTmp.getTime()).find()) {//正则表达式匹配不成功
                                        courseSectionTmp.setTime("00:00");
                                    }
                                    if (null == courseSectionTmp.getVideoUrl()) {
                                        courseSectionTmp.setVideoUrl("");
                                    }
                                    //计算节的总时间
                                    totalTime = appendCourseSectionTime(totalTime, courseSectionTmp.getTime());
                                }
                                //批量创建 节
                                courseSectionService.createList(subCourseSections);

                                //更新时间
                                courseSection.setTime(totalTime);
                                courseSectionService.updateSelectivity(courseSection);
                            }
                        }
                        resultMap.put("success", true);
                        resultMap.put("message", "添加章节成功！ 新增" + courseSections.size() + "记录");
                    } else {
                        resultMap.put("success", false);
                        resultMap.put("message", "添加章节失败！请稍后重试");
                    }
                }else {
                    resultMap.put("success", false);
                    resultMap.put("message","权限不足！违规操作");
                }
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

    /**
     * 计算时间
     * @param time1
     * @param time2
     * @return
     */
    private String appendCourseSectionTime(String time1, String time2){
        String[] time1Arr = time1.split(":");
        String[] time2Arr = time2.split(":");
        Integer second1 = Integer.parseInt(time1Arr[0]) * 60 + Integer.parseInt(time1Arr[1]);
        Integer second2 = Integer.parseInt(time2Arr[0]) * 60 + Integer.parseInt(time2Arr[1]);
        Integer secondTotal = second1 + second2;
        Integer minute = secondTotal/60;
        String minuteStr = minute + "";
        if(minute < 10){
            minuteStr = "0"+minute;
        }
        Integer secode = secondTotal%60;
        String secodeStr = secode + "";
        if(secode < 10){
            secodeStr = "0"+secode;
        }
        return minuteStr + ":" + secodeStr;
    }
}
