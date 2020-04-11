package org.fox.mooc.controller.superadmin;

import org.fox.mooc.dao.course.CourseDao;
import org.fox.mooc.dto.CourseQueryDTO;
import org.fox.mooc.dto.ImageDTO;
import org.fox.mooc.entity.AuthUser;
import org.fox.mooc.entity.UserMessage;
import org.fox.mooc.entity.UserStudyCourse;
import org.fox.mooc.entity.course.*;
import org.fox.mooc.service.auth.AuthUserService;
import org.fox.mooc.service.auth.UserMessageService;
import org.fox.mooc.service.auth.UserStudyCourseService;
import org.fox.mooc.service.course.*;
import org.fox.mooc.util.*;
import org.fox.mooc.vo.GraphVO;
import org.fox.mooc.vo.UserMessageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author by fairyfox
 * *2020/3/4-16:47
 */
@Controller
@RequestMapping(value = "/admin")
class SuperAdminController {
    @Autowired
    private AuthUserService authUserService;

    @Autowired
    private CourseDao courseDao;

    @Autowired
    private CourseService courseService;

    @Autowired
    private CourseClassifyService courseClassifyService;

    @Autowired
    private CourseSiteCarouselService courseSiteCarouselService;

    @Autowired
    private CourseSectionService courseSectionService;

    @Autowired
    private CourseCommentService courseCommentService;

    @Autowired
    private UserMessageService userMessageService;

    @Autowired
    private UserStudyCourseService userStudyCourseService;

    /**
     * 管理员登录
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    private Map<String, Object> Login(HttpServletRequest request , HttpServletResponse response){
        Map<String, Object> resultMap = new HashMap<>();
        //、判读验证码是否正确
        if (!Codeutil.checkVerifyCode(request)) {
            resultMap.put("success", true);
            resultMap.put("message","输入了错误的验证码！");
            return resultMap;
        }
        AuthUser authUser = new AuthUser();
        try {
            String account = HttpServletRequestUtil.getString(request, "account");
            String password = HttpServletRequestUtil.getString(request, "password");
            authUser.setAccount(account);
            authUser.setPassword(MD5.getMd5(password));
            AuthUser au = authUserService.getByUsernameAndPassword(authUser);
            if (au!=null && au.getAccount()!= null && au.getPassword()!=null) {
                if (au.getRole()==0){
                    //提示当前管理员未处理消息
                    UserMessage userMessage = new UserMessage();
                    userMessage.setStatus(0);
                    userMessage.setType(2);
                    Integer count = userMessageService.getTotalItemsCount(userMessage);
                    //将用户登录状态存入session中
                    request.getSession().setAttribute("userinfo", au);
                    resultMap.put("success", true);
                    resultMap.put("count", count);
                    resultMap.put("message","登录成功！");
                }else{
                    resultMap.put("success", false);
                    resultMap.put("message","非管理员用户！非法登录！");
                }
            } else {
                resultMap.put("success", false);
                resultMap.put("message","用户或密码不正确！");
            }
        } catch (Exception e) {
            resultMap.put("success", false);
            resultMap.put("message", e.getMessage());
        }
        return resultMap;
    }


    /**
     * (含模糊匹配)分页加载用户列表
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/authuserlist", method = RequestMethod.POST)
    private Map<String,Object> AuthUserList(HttpServletRequest request){
        Map<String,Object> resultMap = new HashMap<>();
        //获取当前页
        int pageIndex = HttpServletRequestUtil.getInteger(request, "pageIndex");
        String keyWord = HttpServletRequestUtil.getString(request, "keyWord");
        int role = HttpServletRequestUtil.getInteger(request, "role");
        //设置每页显示10条用户数据
        int pageSize = HttpServletRequestUtil.getInteger(request, "pageSize");
        AuthUser authUser = new AuthUser();

        Integer rowIndex = PageCalculator.calculateRowIndex(pageIndex, pageSize);
        try {
            //加载用户列表;模糊匹配
            if (role!=-1){
                authUser.setRole(role);
            }
            if (!keyWord.equals("")){
                authUser.setUsername(keyWord);
                authUser.setRealname(keyWord);
            }
            //获取用户总数量
            int count = authUserService.getTotalItemsCount(authUser);
            //获取用户列表
            List<AuthUser> authUserslist = authUserService.queryPage(authUser, rowIndex, pageSize);
            resultMap.put("success",true);
            resultMap.put("count",count);
            resultMap.put("authuserlist",authUserslist);
        } catch (Exception e) {
            resultMap.put("success",false);
            resultMap.put("message",e.getMessage());
        }
        return resultMap;
    }

    /**
     * 根据当前课程查询学习用户
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/studyuserlist", method = RequestMethod.POST)
    private Map<String,Object> StudyUserList(HttpServletRequest request){
        Map<String,Object> resultMap = new HashMap<>();
        //获取当前页
        int pageIndex = HttpServletRequestUtil.getInteger(request, "pageIndex");
        //设置每页显示10条用户数据
        int pageSize = HttpServletRequestUtil.getInteger(request, "pageSize");
        //当前课程
        long courseId = HttpServletRequestUtil.getLong(request, "courseId");
        Integer rowIndex = PageCalculator.calculateRowIndex(pageIndex, pageSize);
        try {
            //获取学习用户总数量
            UserStudyCourse studyCourse = new UserStudyCourse();
            studyCourse.setCourseId(courseId);
            int count = userStudyCourseService.getTotalItemsCount(studyCourse);
            //获取学习用户列表
            List<AuthUser> studyList = authUserService.byCourseIdQuery(courseId, rowIndex, pageSize);
            resultMap.put("success",true);
            resultMap.put("count",count);
            resultMap.put("studyList",studyList);
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
        //讲师id
        String account = HttpServletRequestUtil.getString(request,"account");
        //一级分类
        String topClassify = HttpServletRequestUtil.getString(request, "topClassify");
        //二级分类
        String subClassify = HttpServletRequestUtil.getString(request, "subClassify");
        //排序方式
        String sort = HttpServletRequestUtil.getString(request,"sort");
        //课程状态
        String status = (HttpServletRequestUtil.getString(request,"status"));
        //关键词(课程名、讲师名)
        String keyword = HttpServletRequestUtil.getString(request, "keyword");
        //当前页码
        int pageIndex = HttpServletRequestUtil.getInteger(request,"pageIndex");
        //页面大小
        int pageSize = HttpServletRequestUtil.getInteger(request,"pageSize");
        courseQueryDTO.setAccount(account);
        courseQueryDTO.setClassify(topClassify);
        courseQueryDTO.setSubClassify(subClassify);
        courseQueryDTO.setSortField(sort);
        if (status!=null){
            courseQueryDTO.setOnsale(Integer.parseInt(status));
        }
        courseQueryDTO.setCourseName(keyword);
        courseQueryDTO.setOwnerName(keyword);
        courseQueryDTO.setRowIndex(PageCalculator.calculateRowIndex(pageIndex,pageSize));
        courseQueryDTO.setPageSize(pageSize);
        try {
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
     * 获取三项数据（用户总数量、讲师总数量、课程总数量）
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/threedata", method = RequestMethod.GET)
    private Map<String,Object> ThreeData(HttpServletRequest request){
        Map<String,Object> resultMap = new HashMap<>();
        AuthUser authUser = new AuthUser();
        CourseQueryDTO course = new CourseQueryDTO();
        try {
            Integer userCount = authUserService.getTotalItemsCount(authUser);
            authUser.setRole(2);
            Integer teacherCount = authUserService.getTotalItemsCount(authUser);
            Integer courseCount = courseService.getTotalItemsCount(course);
            resultMap.put("success",true);
            resultMap.put("userCount",userCount);
            resultMap.put("teacherCount",teacherCount);
            resultMap.put("courseCount",courseCount);
        } catch (Exception e) {
            resultMap.put("success",false);
            resultMap.put("message",e.getMessage());
        }
        return resultMap;
    }

    /**
     * 获取用户数变化Graph数据（用户总数量、讲师总数量）
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/graphdata", method = RequestMethod.GET)
    private Map<String,Object> GraphData(HttpServletRequest request){
        Map<String,Object> resultMap = new HashMap<>();
        AuthUser authUser = new AuthUser();
        try {
            List<GraphVO> userGraph = authUserService.queryGraph(authUser);
            authUser.setRole(2);
            List<GraphVO> teacherGraph = authUserService.queryGraph(authUser);
            resultMap.put("success",true);
            resultMap.put("userGraph",userGraph);
            resultMap.put("teacherGraph",teacherGraph);
        } catch (Exception e) {
            resultMap.put("success",false);
            resultMap.put("message",e.getMessage());
        }
        return resultMap;
    }

    /**
     * 根据id获取对应课程最近7天学习人数graph
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/studygraph", method = RequestMethod.GET)
    private Map<String,Object> studyGraph(HttpServletRequest request){
        Map<String,Object> resultMap = new HashMap<>();
        Long courseId = HttpServletRequestUtil.getLong(request,"courseId");
        try {
            List<GraphVO> courseGraph = courseService.queryGraph(courseId);
            resultMap.put("success",true);
            resultMap.put("courseGraph",courseGraph);
        } catch (Exception e) {
            resultMap.put("success",false);
            resultMap.put("message",e.getMessage());
        }
        return resultMap;
    }

    /**
     * 更新用户信息
     * @param request
     * @return
     */
    @RequestMapping(value = "/modifyinfo", method = RequestMethod.POST)
    @ResponseBody
    private Map<String, Object> ModifyInfo(HttpServletRequest request){
        Map<String, Object> resultMap = new HashMap<>();
        Long id = HttpServletRequestUtil.getLong(request, "id");
        String account = HttpServletRequestUtil.getString(request, "account");
        String username = HttpServletRequestUtil.getString(request, "username");
        String realname = HttpServletRequestUtil.getString(request, "realname");
        Integer sex = HttpServletRequestUtil.getInteger(request, "sex");
        String education = HttpServletRequestUtil.getString(request, "education");
        String college = HttpServletRequestUtil.getString(request, "college");
        String title = HttpServletRequestUtil.getString(request, "title");
        String sign = HttpServletRequestUtil.getString(request, "sign");
        String mobile = HttpServletRequestUtil.getString(request, "mobile");
        Integer status = HttpServletRequestUtil.getInteger(request, "status");
        Integer role = HttpServletRequestUtil.getInteger(request, "role");
        try {
            AuthUser authUser = (AuthUser)request.getSession().getAttribute("userinfo");
            if (authUser != null) {
                if (authUser.getRole()==0){
                    AuthUser au = new AuthUser();
                    au.setId(id);
                    au.setAccount(account);
                    au.setUsername(username);
                    au.setRealname(realname);
                    au.setGender(sex);
                    au.setEducation(education);
                    au.setCollegeName(college);
                    au.setTitle(title);
                    au.setSign(sign);
                    au.setMobile(mobile);
                    au.setStatus(status);
                    au.setRole(role);
                    authUserService.updateSelectivity(au);
                    resultMap.put("success", true);
                    resultMap.put("message","修改信息成功！");
                }else{
                    resultMap.put("success", false);
                    resultMap.put("message","权限不足！违规操作");
                }
            } else {
                resultMap.put("success", false);
                resultMap.put("message","登录超时,凭证失效，请重新登录！");
            }
        } catch (Exception e) {
            resultMap.put("success", false);
            resultMap.put("message", e.getMessage());
        }
        return resultMap;
    }

    /**
     * 删除用户
     * @param request
     * @return
     */
    @RequestMapping(value = "/deleteinfo", method = RequestMethod.POST)
    @ResponseBody
    private Map<String, Object> DeleteInfo(HttpServletRequest request){
        Map<String, Object> resultMap = new HashMap<>();
        Long id = HttpServletRequestUtil.getLong(request, "id");
        try {
            AuthUser authUser = (AuthUser)request.getSession().getAttribute("userinfo");
            if (authUser != null) {
                if (authUser.getRole()==0){
                    AuthUser au = new AuthUser();
                    au.setId(id);
                    authUserService.delete(au);
                    if (authUser.getId().equals(id)){
                        request.getSession().invalidate();
                        resultMap.put("success", true);
                        resultMap.put("message","删除成功！当前登录账号已销户，请重新登录");
                    }else{
                        resultMap.put("success", true);
                        resultMap.put("message","删除成功！");
                    }
                }else{
                    resultMap.put("success", false);
                    resultMap.put("message","权限不足！违规操作");
                }
            } else {
                resultMap.put("success", false);
                resultMap.put("message","登录超时,凭证失效，请重新登录！");
            }
        } catch (Exception e) {
            resultMap.put("success", false);
            resultMap.put("message", e.getMessage());
        }
        return resultMap;
    }

    /**
     * 新增课程分类
     * @param request
     * @return
     */
    @RequestMapping(value = "/addclassify", method = RequestMethod.POST)
    @ResponseBody
    private Map<String, Object> AddClassify(HttpServletRequest request){
        Map<String, Object> resultMap = new HashMap<>();
        String classifyName = HttpServletRequestUtil.getString(request, "classifyName");
        String classifyCode = HttpServletRequestUtil.getString(request,"classifyCode");
        String parentCode = HttpServletRequestUtil.getString(request,"parentCode");
        try {
            AuthUser authUser = (AuthUser)request.getSession().getAttribute("userinfo");
            if (authUser != null) {
                if (authUser.getRole()==0){
                    CourseClassify classify = new CourseClassify();
                    classify.setParentCode(parentCode);
                    classify.setName(classifyName);
                    classify.setCode(classifyCode);
                    classify.setCreateUser(authUser.getAccount());
                    courseClassifyService.createSelectivity(classify);
                    resultMap.put("success", true);
                    resultMap.put("message","新增成功！");
                }else{
                    resultMap.put("success", false);
                    resultMap.put("message","权限不足！违规操作");
                }
            } else {
                resultMap.put("success", false);
                resultMap.put("message","登录超时,凭证失效，请重新登录！");
            }
        } catch (Exception e) {
            resultMap.put("success", false);
            resultMap.put("message", e.getMessage());
        }
        return resultMap;
    }

    /**
     * 修改课程分类(避免复杂的业务逻辑、和错误，仅提课程分类名的修改)
     * @param request
     * @return
     */
    @RequestMapping(value = "/modifyclassify", method = RequestMethod.POST)
    @ResponseBody
    private Map<String, Object> ModifyClassify(HttpServletRequest request){
        Map<String, Object> resultMap = new HashMap<>();
        Long id = HttpServletRequestUtil.getLong(request,"id");
        String classifyName = HttpServletRequestUtil.getString(request, "classifyName");
        try {
            AuthUser authUser = (AuthUser)request.getSession().getAttribute("userinfo");
            if (authUser != null) {
                if (authUser.getRole()==0){
                    CourseClassify classify = new CourseClassify();
                    classify.setId(id);
                    classify.setName(classifyName);
                    courseClassifyService.updateSelectivity(classify);
                    resultMap.put("success", true);
                    resultMap.put("message","修改成功！");
                }else{
                    resultMap.put("success", false);
                    resultMap.put("message","权限不足！违规操作");
                }
            } else {
                resultMap.put("success", false);
                resultMap.put("message","登录超时,凭证失效，请重新登录！");
            }
        } catch (Exception e) {
            resultMap.put("success", false);
            resultMap.put("message", e.getMessage());
        }
        return resultMap;
    }

    /**
     * 根据id删除课程分类
     * @param request
     * @return
     */
    @RequestMapping(value = "/deleteclassify", method = RequestMethod.POST)
    @ResponseBody
    private Map<String, Object> DeleteClassify(HttpServletRequest request){
        Map<String, Object> resultMap = new HashMap<>();
        Long id = HttpServletRequestUtil.getLong(request,"id");
        try {
            AuthUser authUser = (AuthUser)request.getSession().getAttribute("userinfo");
            if (authUser != null) {
                //判断当前操作用户权限
                if (authUser.getRole()==0){
                    CourseClassify classify = new CourseClassify();
                    classify.setId(id);
                    courseClassifyService.delete(classify);
                    resultMap.put("success", true);
                    resultMap.put("message","删除成功！");
                }else{
                    resultMap.put("success", false);
                    resultMap.put("message","权限不足！违规操作");
                }
            } else {
                resultMap.put("success", false);
                resultMap.put("message","登录超时,凭证失效，请重新登录！");
            }
        } catch (Exception e) {
            resultMap.put("success", false);
            resultMap.put("message", e.getMessage());
        }
        return resultMap;
    }

    /**
     * 新增&修改轮播图
     * @param request
     * @return
     */
    @RequestMapping(value = "/carousel", method = RequestMethod.POST)
    @ResponseBody
    private Map<String, Object> Carousel(HttpServletRequest request){
        Map<String, Object> resultMap = new HashMap<>();
        ImageDTO imageDTO = null;
        Long id = HttpServletRequestUtil.getLong(request,"id");
        String carouselName = HttpServletRequestUtil.getString(request, "carouselName");
        String carouselUrl = HttpServletRequestUtil.getString(request,"carouselUrl");
        Integer weight = HttpServletRequestUtil.getInteger(request,"weight");
        Integer status = HttpServletRequestUtil.getInteger(request,"status");
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        //判断文件流是否存在
        try {
            AuthUser authUser = (AuthUser)request.getSession().getAttribute("userinfo");
            if (authUser != null) {
                if (authUser.getRole()==0){
                    if(multipartResolver.isMultipart(request)) {
                        imageDTO = ImageUtil.Image(request, imageDTO);
                    }else {
                        resultMap.put("success",false);
                        resultMap.put("message","轮播图不能为空!");
                        return resultMap;
                    }
                    CourseSiteCarousel carousel = new CourseSiteCarousel();
                    carousel.setName(carouselName);
                    carousel.setUrl(carouselUrl);
                    carousel.setWeight(weight);
                    carousel.setEnable(status);
                    carousel.setCreateUser(authUser.getAccount());
                    if(id!=-1L){
                        carousel.setId(id);
                        courseSiteCarouselService.updateSelectivity(carousel,imageDTO);
                        resultMap.put("success", true);
                        resultMap.put("message","修改成功！");
                    }else{
                        courseSiteCarouselService.createSelectivity(carousel,imageDTO);
                        resultMap.put("success", true);
                        resultMap.put("message","新增成功！");
                    }
                }else{
                    resultMap.put("success", false);
                    resultMap.put("message","权限不足！违规操作");
                }
            } else {
                resultMap.put("success", false);
                resultMap.put("message","登录超时,凭证失效，请重新登录！");
            }
        } catch (Exception e) {
            resultMap.put("success", false);
            resultMap.put("message", e.getMessage());
        }
        return resultMap;
    }

    /**
     * 根据id删除轮播图
     * @param request
     * @return
     */
    @RequestMapping(value = "/deletecarousel", method = RequestMethod.POST)
    @ResponseBody
    private Map<String, Object> DeleteCarousel(HttpServletRequest request){
        Map<String, Object> resultMap = new HashMap<>();
        Long id = HttpServletRequestUtil.getLong(request,"id");
        try {
            AuthUser authUser = (AuthUser)request.getSession().getAttribute("userinfo");
            if (authUser != null) {
                //判断当前操作用户权限
                if (authUser.getRole()==0){
                    CourseSiteCarousel carousel = new CourseSiteCarousel();
                    carousel.setId(id);
                    courseSiteCarouselService.delete(carousel);
                    resultMap.put("success", true);
                    resultMap.put("message","删除成功！");
                }else{
                    resultMap.put("success", false);
                    resultMap.put("message","权限不足！违规操作");
                }
            } else {
                resultMap.put("success", false);
                resultMap.put("message","登录超时,凭证失效，请重新登录！");
            }
        } catch (Exception e) {
            resultMap.put("success", false);
            resultMap.put("message", e.getMessage());
        }
        return resultMap;
    }

    /**
     * 创建课程
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/modifycourse", method = RequestMethod.POST)
    private Map<String,Object> CreateCourse(HttpServletRequest request){
        Map<String,Object> resultMap = new HashMap<>();
        ImageDTO imageDTO = null;
        Course course = new Course();
        Long id = HttpServletRequestUtil.getLong(request,"id");
        String courseName = HttpServletRequestUtil.getString(request, "courseName");
        String owner = HttpServletRequestUtil.getString(request, "ownerName");
        String topClassify = HttpServletRequestUtil.getString(request, "classify");
        String topClassifyName = HttpServletRequestUtil.getString(request, "classifyName");
        String subClassify = HttpServletRequestUtil.getString(request, "subClassify");
        String subClassifyName = HttpServletRequestUtil.getString(request, "subClassifyName");
        Integer weight = HttpServletRequestUtil.getInteger(request, "weight");
        String time = HttpServletRequestUtil.getString(request, "time");
        String brief = HttpServletRequestUtil.getString(request, "brief");
        course.setId(id);
        course.setCourseName(courseName);
        course.setOwnerName(owner);
        course.setClassify(topClassify);
        course.setClassifyName(topClassifyName);
        course.setSubClassify(subClassify);
        course.setSubClassifyName(subClassifyName);
        course.setWeight(weight);
        course.setCourseTime(time);
        course.setBrief(brief);
        //判断文件流是否存在
        try {
            AuthUser authUser = (AuthUser)request.getSession().getAttribute("userinfo");
            if (authUser != null) {
                if (authUser.getRole()==0){
                    imageDTO = ImageUtil.Image(request, imageDTO);
                    courseService.updateSelectivity(course,imageDTO);
                    resultMap.put("success", true);
                    resultMap.put("message","修改成功！");

                }else{
                    resultMap.put("success", false);
                    resultMap.put("message","权限不足！违规操作");
                }
            } else {
                resultMap.put("success", false);
                resultMap.put("message","登录超时,凭证失效，请重新登录！");
            }
        } catch (Exception e) {
            resultMap.put("success", false);
            resultMap.put("message", e.getMessage());
        }
        return resultMap;
    }

    /**
     * 课程删除
     * @param request
     * @return
     */
    @RequestMapping(value = "/deletecourse", method = RequestMethod.POST)
    @ResponseBody
    private Map<String, Object> DeleteCourse(HttpServletRequest request){
        Map<String, Object> resultMap = new HashMap<>();
        Long id = HttpServletRequestUtil.getLong(request,"id");
        try {
            AuthUser authUser = (AuthUser)request.getSession().getAttribute("userinfo");
            if (authUser != null) {
                //判断当前操作用户权限
                if (authUser.getRole()==0){
                    Course course = new Course();
                    course.setId(id);
                    courseService.delete(course);
                    resultMap.put("success", true);
                    resultMap.put("message","删除成功！");
                }else{
                    resultMap.put("success", false);
                    resultMap.put("message","权限不足！违规操作");
                }
            } else {
                resultMap.put("success", false);
                resultMap.put("message","登录超时,凭证失效，请重新登录！");
            }
        } catch (Exception e) {
            resultMap.put("success", false);
            resultMap.put("message", e.getMessage());
        }
        return resultMap;
    }

    /**
     * 课程上下架
     * @param request
     * @return
     */
    @RequestMapping(value = "/coursestatus", method = RequestMethod.POST)
    @ResponseBody
    private Map<String, Object> CourseStatus(HttpServletRequest request){
        Map<String, Object> resultMap = new HashMap<>();
        Long id = HttpServletRequestUtil.getLong(request,"id");
        boolean status = HttpServletRequestUtil.getBoolean(request,"status");
        try {
            AuthUser authUser = (AuthUser)request.getSession().getAttribute("userinfo");
            if (authUser != null) {
                //判断当前操作用户权限
                if (authUser.getRole()==0){
                    Course course = new Course();
                    course.setId(id);
                    if (status){
                        course.setOnsale(1);
                        courseService.updateStatus(course);
                        resultMap.put("success", true);
                        resultMap.put("message","操作成功，课程已上架！");
                    }else{
                        course.setOnsale(0);
                        courseService.updateStatus(course);
                        resultMap.put("success", true);
                        resultMap.put("message","操作成功，课程已下架！");
                    }
                }else{
                    resultMap.put("success", false);
                    resultMap.put("message","权限不足！违规操作");
                }
            } else {
                resultMap.put("success", false);
                resultMap.put("message","登录超时,凭证失效，请重新登录！");
            }
        } catch (Exception e) {
            resultMap.put("success", false);
            resultMap.put("message", e.getMessage());
        }
        return resultMap;
    }

    /**
     * 修改课程章（章名称）、节（节名称、url、时常）
     * @param request
     * @return
     */
    @RequestMapping(value = "/modifysection", method = RequestMethod.POST)
    @ResponseBody
    private Map<String, Object> ModifySection(HttpServletRequest request){
        Map<String, Object> resultMap = new HashMap<>();
        CourseSection courseSection = new CourseSection();
        Long id = HttpServletRequestUtil.getLong(request,"id");
        String sectionName = HttpServletRequestUtil.getString(request,"name");
        String url = HttpServletRequestUtil.getString(request,"url");
        String time = HttpServletRequestUtil.getString(request,"time");
        try {
            AuthUser authUser = (AuthUser)request.getSession().getAttribute("userinfo");
            if (authUser != null) {
                if (authUser.getRole()==0){
                    courseSection.setId(id);
                    courseSection.setName(sectionName);
                    courseSection.setVideoUrl(url);
                    courseSection.setTime(time);
                    courseSectionService.updateSelectivity(courseSection);
                    resultMap.put("success", true);
                    resultMap.put("message","修改成功！");
                }else{
                    resultMap.put("success", false);
                    resultMap.put("message","权限不足！违规操作");
                }
            } else {
                resultMap.put("success", false);
                resultMap.put("message","登录超时,凭证失效，请重新登录！");
            }
        } catch (Exception e) {
            resultMap.put("success", false);
            resultMap.put("message", e.getMessage());
        }
        return resultMap;
    }

    /**
     * 删除章节(若删除章，其节也会被删除)
     * @param request
     * @return
     */
    @RequestMapping(value = "/deletesection", method = RequestMethod.POST)
    @ResponseBody
    private Map<String, Object> DeleteSection(HttpServletRequest request){
        Map<String, Object> resultMap = new HashMap<>();
        CourseSection courseSection = new CourseSection();
        Long id = HttpServletRequestUtil.getLong(request,"id");
        try {
            AuthUser authUser = (AuthUser)request.getSession().getAttribute("userinfo");
            if (authUser != null) {
                //判断当前操作用户权限
                if (authUser.getRole()==0){
                    courseSection.setId(id);
                    courseSectionService.delete(courseSection);
                    resultMap.put("success", true);
                    resultMap.put("message","删除成功！");
                }else{
                    resultMap.put("success", false);
                    resultMap.put("message","权限不足！违规操作");
                }
            } else {
                resultMap.put("success", false);
                resultMap.put("message","登录超时,凭证失效，请重新登录！");
            }
        } catch (Exception e) {
            resultMap.put("success", false);
            resultMap.put("message", e.getMessage());
        }
        return resultMap;
    }

    /**
     * 删除评论、答疑（批量删除：关联内容、实现违规评论删除）
     * @param request
     * @return
     */
    @RequestMapping(value = "/deletecomment", method = RequestMethod.POST)
    @ResponseBody
    private Map<String, Object> DeleteComment(HttpServletRequest request){
        Map<String, Object> resultMap = new HashMap<>();
        CourseComment courseComment = new CourseComment();
        Long id = HttpServletRequestUtil.getLong(request,"id");
        try {
            AuthUser authUser = (AuthUser)request.getSession().getAttribute("userinfo");
            if (authUser != null) {
                //判断当前操作用户权限
                if (authUser.getRole()==0){
                    courseComment.setId(id);
                    courseCommentService.delete(courseComment);
                    resultMap.put("success", true);
                    resultMap.put("message","删除成功！");
                }else{
                    resultMap.put("success", false);
                    resultMap.put("message","权限不足！违规操作");
                }
            } else {
                resultMap.put("success", false);
                resultMap.put("message","登录超时,凭证失效，请重新登录！");
            }
        } catch (Exception e) {
            resultMap.put("success", false);
            resultMap.put("message", e.getMessage());
        }
        return resultMap;
    }

    /**
     * 查询课程列表
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/message", method = RequestMethod.POST)
    private Map<String,Object> Message(HttpServletRequest request){
        Map<String,Object> resultMap = new HashMap<>();
        //当前页
        Integer pageIndex = HttpServletRequestUtil.getInteger(request, "pageindex");
        //最大页
        Integer pageSize = HttpServletRequestUtil.getInteger(request, "pageSize");
        //排序方式
        String sort = HttpServletRequestUtil.getString(request, "sort");
        //关键词
        String keyword = HttpServletRequestUtil.getString(request, "keyword");
        CourseQueryDTO courseQueryDTO = new CourseQueryDTO();
        courseQueryDTO.setRowIndex(pageIndex);
        courseQueryDTO.setPageSize(pageSize);
        courseQueryDTO.setSortField(sort);
        courseQueryDTO.setCourseName(keyword);
        try {
            List<Course> courseList = courseDao.queryList(courseQueryDTO);
            int count = courseDao.getTotalItemsCount(courseQueryDTO);
            resultMap.put("success",true);
            resultMap.put("count",count);
            resultMap.put("courselist",courseList);
        } catch (Exception e) {
            resultMap.put("success",false);
            resultMap.put("message",e.getMessage());
        }
        return resultMap;
    }

    /**
     * 查询提交申请消息
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/applymessage", method = RequestMethod.POST)
    public Map<String, Object> ApplyMessage(HttpServletRequest request){
        Map<String,Object> resultMap = new HashMap<>();
        int pageIndex = HttpServletRequestUtil.getInteger(request,"pageIndex");
        //页面大小
        int pageSize = HttpServletRequestUtil.getInteger(request,"pageSize");
        //对应数据库行数
        int rowIndex = PageCalculator.calculateRowIndex(pageIndex,pageSize);
        UserMessage userMessage = new UserMessage();
        try {
            AuthUser authUser = (AuthUser)request.getSession().getAttribute("userinfo");
            if (authUser != null) {
                userMessage.setType(2);
                Integer count = userMessageService.getTotalItemsCount(userMessage);
                List<UserMessageVO> messageList = userMessageService.queryApplyPage(userMessage,rowIndex,pageSize);
                resultMap.put("success", true);
                resultMap.put("count",count);
                resultMap.put("messageList", messageList);
            } else {
                resultMap.put("success", false);
                resultMap.put("message", "登录失效!");
            }
        } catch (Exception e) {
            resultMap.put("success", false);
            resultMap.put("message", e.getMessage());
        }
        return resultMap;
    }

    /**
     * 查询待处理的提交课程，通知消息
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/publishmessage", method = RequestMethod.POST)
    public Map<String, Object> PublishMessage(HttpServletRequest request){
        Map<String,Object> resultMap = new HashMap<>();
        int pageIndex = HttpServletRequestUtil.getInteger(request,"pageIndex");
        //页面大小
        int pageSize = HttpServletRequestUtil.getInteger(request,"pageSize");
        //对应数据库行数
        int rowIndex = PageCalculator.calculateRowIndex(pageIndex,pageSize);
        UserMessage userMessage = new UserMessage();
        try {
            AuthUser authUser = (AuthUser)request.getSession().getAttribute("userinfo");
            if (authUser != null) {
                if (authUser.getRole()==0) {
                    userMessage.setType(3);
                    Integer count = userMessageService.getTotalItemsCount(userMessage);
                    List<UserMessageVO> messageList = userMessageService.queryApplyPage(userMessage, rowIndex, pageSize);
                    resultMap.put("success", true);
                    resultMap.put("count", count);
                    resultMap.put("messageList", messageList);
                }else {
                    resultMap.put("success", false);
                    resultMap.put("message","权限不足！违规操作");
                }
            } else {
                resultMap.put("success", false);
                resultMap.put("message", "登录失效!");
            }
        } catch (Exception e) {
            resultMap.put("success", false);
            resultMap.put("message", e.getMessage());
        }
        return resultMap;
    }

    /**
     * 处理申请消息
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/handleapply", method = RequestMethod.POST)
    public Map<String, Object> HandleApply(HttpServletRequest request){
        Map<String,Object> resultMap = new HashMap<>();
        boolean result = HttpServletRequestUtil.getBoolean(request, "result");
        Long receiptUserId = HttpServletRequestUtil.getLong(request, "userId");
        try {
            AuthUser authUser = (AuthUser)request.getSession().getAttribute("userinfo");
            if (authUser != null) {
                if (authUser.getRole()==0){
                    AuthUser user = new AuthUser();
                    user.setId(receiptUserId);
                    user.setRole(-1);//避免空指针
                    if(result){//同意该申请
                        user.setRole(2);
                    }
                    authUserService.updateRole(user,authUser);
                    resultMap.put("success", true);
                    resultMap.put("message","操作成功！");
                }else{
                    resultMap.put("success", false);
                    resultMap.put("message","权限不足！违规操作");
                }
            } else {
                resultMap.put("success", false);
                resultMap.put("message","登录超时,凭证失效，请重新登录！");
            }
        } catch (Exception e) {
            resultMap.put("success", false);
            resultMap.put("message", e.getMessage());
        }
        return resultMap;
    }

    /**
     * 根据用户id返回用户信息
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/userinfo", method = RequestMethod.POST)
    public Map<String, Object> UserInfo(HttpServletRequest request){
        Map<String,Object> resultMap = new HashMap<>();
        Long userId = HttpServletRequestUtil.getLong(request, "userId");
        try {
            AuthUser authUser = (AuthUser)request.getSession().getAttribute("userinfo");
            if (authUser != null) {
                if (authUser.getRole()==0){
                    AuthUser user = authUserService.getById(userId);
                    //清除密码
                    user.setPassword("");
                    resultMap.put("success", true);
                    resultMap.put("user",user);
                }else{
                    resultMap.put("success", false);
                    resultMap.put("message","权限不足！违规操作");
                }
            } else {
                resultMap.put("success", false);
                resultMap.put("message","登录超时,凭证失效，请重新登录！");
            }
        } catch (Exception e) {
            resultMap.put("success", false);
            resultMap.put("message", e.getMessage());
        }
        return resultMap;
    }
}