package org.fox.mooc.controller.auth;

import org.apache.commons.collections4.CollectionUtils;
import org.fox.mooc.dto.ImageDTO;
import org.fox.mooc.dto.UserCourseSectionDTO;
import org.fox.mooc.entity.*;
import org.fox.mooc.service.course.CourseService;
import org.fox.mooc.service.auth.*;
import org.fox.mooc.util.*;
import org.fox.mooc.vo.UserStudyCourseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

import org.springframework.web.multipart.commons.CommonsMultipartResolver;

/**
 * @Author by fairyfox
 * *2020/2/17-16:05
 */
@Controller
@RequestMapping(value = "/useroperator")
public class UserOperatorController {

    @Autowired
    private AuthUserService authUserService;

    @Autowired
    private UserCollectionService userCollectionService;

    @Autowired
    private UserFollowsService userFollowsService;

    @Autowired
    private UserCourseSectionService userCourseSectionService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private UserMessageService userMessageService;

    @Autowired
    private UserStudyCourseService  userStudyCourseService;


    /**
     * 发送验证码
     * @param request
     * @return
     */
    @RequestMapping(value = "/getcode", method = RequestMethod.POST)
    @ResponseBody    //该注解将对象类型的返回值变为json对象
    private Map<String, Object> GetCode(HttpServletRequest request ) throws Exception {
        Map<String ,Object> resultMap = new HashMap<>();
        //获取访问所用ip做临时用户名
        String tempName = IpUtil.getIP(request);
        EmailCode emailCode = new EmailCode();
        String email = HttpServletRequestUtil.getString(request, "email");
        //发送验证码
        try {
            AuthUser au = authUserService.getByAccount(email);
            if (au==null){
                emailCode.SendCode(email);
                //清空session，使验证码失效，并重新生成
                request.getSession().invalidate();
                request.getSession().setAttribute(tempName,EmailCode.getRandomCode());
                resultMap.put("success", true);
                resultMap.put("message"," 邮件发送成功，请及时查收");
            }else{
                resultMap.put("success", false);
                resultMap.put("message","该邮箱已被注册，请更换未被使用的邮箱！");
            }
        } catch (Exception e) {
            resultMap.put("success", false);
            resultMap.put("message", "未知错误请稍后重试！");
        }
        return resultMap;
    }

    /**
     * 用户注册
     * @param request
     * @return
     */
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody    //该注解将对象类型的返回值变为json对象
    private Map<String, Object> Register(HttpServletRequest request){
        Map<String, Object> resultMap = new HashMap<>();
        AuthUser authUser = new AuthUser();
        String account = HttpServletRequestUtil.getString(request, "account");
        String username = HttpServletRequestUtil.getString(request, "username");
        String password = HttpServletRequestUtil.getString(request, "password");
        try {
            //获取用户填写验证码
            String applyCode = HttpServletRequestUtil.getString(request, "verifyCodeActual");
            //session存入验证码
            Object emailCode = request.getSession().getAttribute(IpUtil.getIP(request));
            //、判读验证码是否正确
            if (emailCode!=null){
                if (emailCode.toString().equals(applyCode)) {
                    authUser.setAccount(account);
                    authUser.setUsername(username);
                    authUser.setPassword(MD5.getMd5(password));
                    //注册用户
                    authUserService.createSelectivity(authUser);
                    //清空session，使验证码失效
                    request.getSession().invalidate();
                    resultMap.put("success", true);
                    resultMap.put("message", "注册成功！去登录");
                } else {
                    resultMap.put("success", false);
                    resultMap.put("message", "输入的验证码有误！");
                }
            }else{
                resultMap.put("success", false);
                resultMap.put("message", "输入的验证码有误！");
            }
        }catch (Exception e) {
            resultMap.put("success", false);
            resultMap.put("message", e.getMessage());
        }
        return resultMap;
    }

    /**
     * 用户登录
     * @param request
     * @return
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    private Map<String, Object> Login(HttpServletRequest request){
        Map<String, Object> resultMap = new HashMap<>();
        //、判读验证码是否正确
        if (!Codeutil.checkVerifyCode(request)) {
            resultMap.put("success", false);
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
                //提示当前用户未读消息
                UserMessage userMessage = new UserMessage();
                userMessage.setUserId(au.getId());
                userMessage.setStatus(0);
                int count = userMessageService.getTotalItemsCount(userMessage);
                //将用户登录状态存入session中
                request.getSession().setAttribute("userinfo", au);
                resultMap.put("success", true);
                resultMap.put("count", count);
                resultMap.put("message","登录成功！");
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
     * 用户注销
     * @param request
     * @return
     */
    @RequestMapping(value = "/exit", method = RequestMethod.POST)
    @ResponseBody
    private Map<String, Object> Exit(HttpServletRequest request){
        Map<String, Object> resultMap = new HashMap<>();
        try {
            request.getSession().invalidate();
            resultMap.put("success", true);
            resultMap.put("message", "注销成功！");
        } catch (Exception e) {
            resultMap.put("success", false);
            resultMap.put("message", e.getMessage());
        }
        return resultMap;
    }

    /**
     * 用户登录状态
     * @param request
     * @return
     */
    @RequestMapping(value = "/loginstate", method = RequestMethod.POST)
    @ResponseBody
    private Map<String, Object> LoginState(HttpServletRequest request){
        Map<String, Object> resultMap = new HashMap<>();
        try {
            AuthUser authUser = (AuthUser)request.getSession().getAttribute("userinfo");
            if (authUser != null) {
                resultMap.put("success", true);
                resultMap.put("userinfo",authUser);
            } else {
                resultMap.put("success", false);
                resultMap.put("message","登录失效!");
            }
        } catch (Exception e) {
            resultMap.put("success", false);
            resultMap.put("message", e.getMessage());
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
        String username = HttpServletRequestUtil.getString(request, "username");
        Integer sex = HttpServletRequestUtil.getInteger(request, "sex");
        String mobile = HttpServletRequestUtil.getString(request, "mobile");
        String birthday = HttpServletRequestUtil.getString(request, "birthday");
        try {
            AuthUser authUser = (AuthUser)request.getSession().getAttribute("userinfo");
            if (authUser != null) {
                authUser.setUsername(username);
                authUser.setGender(sex);
                authUser.setMobile(mobile);
                if(birthday!=null){
                    authUser.setBirthday(new Date(birthday.replace("-", "/")));
                }else{
                    resultMap.put("success", true);
                    resultMap.put("message","出生日期不能空！");
                    return resultMap;
                }
                authUserService.updateSelectivity(authUser);
                resultMap.put("success", true);
                resultMap.put("message","修改信息成功！");
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
     * 用户修改头像
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/modifyheader", method = RequestMethod.POST)
    private Map<String,Object> ApplyInfo(HttpServletRequest request){
        Map<String,Object> resultMap = new HashMap<>();
        ImageDTO imageDTO = null;
        AuthUser authUser = (AuthUser)request.getSession().getAttribute("userinfo");
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        //判断文件流是否存在
        try {
            if (authUser != null) {
                if(multipartResolver.isMultipart(request)) {
                    imageDTO = ImageUtil.Image(request, imageDTO);
                }else {
                    resultMap.put("success",false);
                    resultMap.put("message","上传头像不能为空");
                }
                authUserService.headerupdate(authUser,imageDTO);
                resultMap.put("success",true);
            }else{
                resultMap.put("success", false);
                resultMap.put("message","登录失效!");
            }
        }catch(Exception e) {
            resultMap.put("success",false);
            resultMap.put("message",e.getMessage());
        }
        return resultMap;
    }

    /**
     * 修改密码
     * @param request
     * @return
     */
    @RequestMapping(value = "/modifypassword", method = RequestMethod.POST)
    @ResponseBody
    private Map<String, Object> ModifyPassword(HttpServletRequest request){
        Map<String, Object> resultMap = new HashMap<>();
        if (!Codeutil.checkVerifyCode(request)) {
            resultMap.put("success", true);
            resultMap.put("message","输入了错误的验证码！");
            return resultMap;
        }
        try {
            AuthUser authUser = (AuthUser)request.getSession().getAttribute("userinfo");
            if (authUser != null) {
                String oldpassword = HttpServletRequestUtil.getString(request, "oldpassword");
                String newpassword = HttpServletRequestUtil.getString(request, "newpassword");
                authUser.setPassword(MD5.getMd5(oldpassword));
                AuthUser au = authUserService.getByUsernameAndPassword(authUser);
                if (au.getAccount()!= null&&au.getPassword()!=null) {
                    au.setPassword(MD5.getMd5(newpassword));
                    authUserService.updateSelectivity(au);
                    resultMap.put("success", true);
                    resultMap.put("message","修改成功，请重新登录！");
                    request.getSession().invalidate();
                } else {
                    resultMap.put("success", false);
                    resultMap.put("message","原密码有误，请重新输入！");
                }
            } else {
                resultMap.put("success", false);
                resultMap.put("message","登录超时,凭证失效，请重新登录！");
            }
        } catch (Exception e) {
            resultMap.put("success", false);
            resultMap.put("message",e.getMessage());
        }
        return resultMap;
    }

    /**
     * 分页加载用户收藏
     * @param request
     * @return
     */
    @RequestMapping(value = "/usercollection", method = RequestMethod.POST)
    @ResponseBody
    private Map<String, Object> UserCollection(HttpServletRequest request){
        Map<String, Object> resultMap = new HashMap<>();
        try {
            AuthUser authUser = (AuthUser)request.getSession().getAttribute("userinfo");
            //获取当前页
            int pageIndex = HttpServletRequestUtil.getInteger(request, "pageIndex");
            //获取页面大小
            int pageSize = HttpServletRequestUtil.getInteger(request, "pageIndex");
            //计算数据库对应行
            Integer rowIndex = PageCalculator.calculateRowIndex(pageIndex, pageSize);
            if (authUser != null) {
                UserCollections userCollections = new UserCollections();
                userCollections.setUserId(authUser.getId());
                //获取评论总数量
                int count = userCollectionService.getTotalItemsCount(userCollections);
                //分页获取数据
                List<UserCollections> userCollectionsList = userCollectionService.queryPage(userCollections, rowIndex, pageSize);
                resultMap.put("success", true);
                resultMap.put("collectionsList", userCollectionsList);
                resultMap.put("count", count);
            } else {
                resultMap.put("success", false);
                resultMap.put("message", "登录失效");
            }
        } catch (Exception e) {
            resultMap.put("success", false);
            resultMap.put("message", e.getMessage());
        }
        return resultMap;
    }

    /**
     * 判断用户是否收藏
     * @param request
     * @return
     */
    @RequestMapping(value = "/collectionstate", method = RequestMethod.POST)
    @ResponseBody
    private Map<String, Object> CollectionState(HttpServletRequest request){
        Map<String, Object> resultMap = new HashMap<>();
        try {
            AuthUser authUser = (AuthUser)request.getSession().getAttribute("userinfo");
            if (authUser != null) {
                Long objectId = HttpServletRequestUtil.getLong(request, "courseid");
                UserCollections userCollections = new UserCollections();
                userCollections.setUserId(authUser.getId());
                userCollections.setObjectId(objectId);
                //获取数据
                UserCollections collections = userCollectionService.isCollection(userCollections);
                if (!StringUtils.isEmpty(collections) && collections.getDel() != 1) {
                    resultMap.put("success", true);
                } else {
                    resultMap.put("success", false);
                }
            } else {
                resultMap.put("success", false);
                resultMap.put("message", "登录失效");
            }
        } catch (Exception e) {
            resultMap.put("success", false);
            resultMap.put("message", e.getMessage());
        }
        return resultMap;
    }

    /**
     * 用户添加收藏
     * @param request
     * @return
     */
    @RequestMapping(value = "/addcollection", method = RequestMethod.POST)
    @ResponseBody
    private Map<String, Object> addCollection(HttpServletRequest request){
        Map<String, Object> resultMap = new HashMap<>();
        try {
            AuthUser authUser = (AuthUser)request.getSession().getAttribute("userinfo");
            if (authUser != null) {
                String tips = HttpServletRequestUtil.getString(request, "tips");
                Long objectId = HttpServletRequestUtil.getLong(request, "courseid");
                UserCollections userCollections = new UserCollections();
                userCollections.setUserId(authUser.getId());
                userCollections.setObjectId(objectId);
                //判断改收藏是否被逻辑删除
                if (!StringUtils.isEmpty(userCollectionService.isCollection(userCollections))) {
                    //逻辑更新
                    userCollections.setDel(0);
                    userCollectionService.updateSelectivity(userCollections);
                } else {
                    //创建新记录（收藏）
                    userCollections.setTips(tips);
                    userCollectionService.createSelectivity(userCollections);
                }
                resultMap.put("success", true);
                resultMap.put("message", "已添加到收藏，请到个人中心查看");
            } else {
                resultMap.put("success", false);
                resultMap.put("message", "登录失效");
            }
        } catch (Exception e) {
            resultMap.put("success", false);
            resultMap.put("message", "添加失败，请稍后重试");
        }
        return resultMap;
    }

    /**
     * 用户移除收藏
     * @param request
     * @return
     */
    @RequestMapping(value = "/rmovecollection", method = RequestMethod.POST)
    @ResponseBody
    private Map<String, Object> removeCollection(HttpServletRequest request){
        Map<String, Object> resultMap = new HashMap<>();
        try {
            AuthUser authUser = (AuthUser)request.getSession().getAttribute("userinfo");
            if (authUser != null) {
                Long objectId = HttpServletRequestUtil.getLong(request, "courseid");
                UserCollections userCollections = new UserCollections();
                userCollections.setUserId(authUser.getId());
                userCollections.setObjectId(objectId);
                userCollectionService.deleteLogic(userCollections);
                resultMap.put("success", true);
                resultMap.put("message", "已取消收藏");
            } else {
                resultMap.put("success", false);
                resultMap.put("message", "登录失效");
            }
        } catch (Exception e) {
            resultMap.put("success", false);
            resultMap.put("message", e.getMessage());
        }
        return resultMap;
    }

    /**
     * 用户切换关注状态
     * @param request
     * @return
     */
    @RequestMapping(value = "/dofollow", method = RequestMethod.GET)
    @ResponseBody
    private Map<String, Object> DOFollow(HttpServletRequest request){
        Map<String, Object> resultMap = new HashMap<>();
        Long followId = HttpServletRequestUtil.getLong(request, "owner");
        try {
            UserFollows userFollows = new UserFollows();
            AuthUser authUser = (AuthUser)request.getSession().getAttribute("userinfo");
            if (authUser != null) {
                userFollows.setUserId(authUser.getId());
                userFollows.setFollowId(followId);
                List<UserFollows> list = userFollowsService.queryAll(userFollows);
                if (CollectionUtils.isNotEmpty(list)) {
                    userFollowsService.delete(list.get(0));
                    resultMap.put("success", true);
                    resultMap.put("message", "已取消关注");
                } else {
                    userFollows.setCreateTime(new Date());
                    userFollowsService.createSelectivity(userFollows);
                    resultMap.put("success", true);
                    resultMap.put("message", "已添加关注");
                }
            }else{
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
     * 是否已经关注
     * @param request
     * @return
     */
    @RequestMapping(value = "/isfollow", method = RequestMethod.GET)
    @ResponseBody
    private Map<String ,Object> isFollow(HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            AuthUser authUser = (AuthUser)request.getSession().getAttribute("userinfo");
            Long followId = HttpServletRequestUtil.getLong(request, "owner");
            if (authUser != null) {
                UserFollows userFollows = new UserFollows();
                userFollows.setUserId(authUser.getId());
                userFollows.setFollowId(followId);
                List<UserFollows> list = userFollowsService.queryAll(userFollows);
                if(CollectionUtils.isNotEmpty(list)){
                    resultMap.put("success", true);
                }else{
                    resultMap.put("success", false);
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
     * 分页返回用户关注列表
     * @param request
     * @return
     */
    @RequestMapping(value = "/followlist", method = RequestMethod.GET)
    @ResponseBody
    private Map<String ,Object> FollowList(HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<>();
        //起始页
        int pageIndex = HttpServletRequestUtil.getInteger(request,"pageIndex");
        //页面大小
        int pageSize = HttpServletRequestUtil.getInteger(request,"pageSize");
        //对应数据库行数
        int rowIndex = PageCalculator.calculateRowIndex(pageIndex,pageSize);
        try {
            AuthUser authUser = (AuthUser)request.getSession().getAttribute("userinfo");
            if (authUser != null) {
                UserFollows userFollows = new UserFollows();
                userFollows.setUserId(authUser.getId());
                int count = userFollowsService.getTotalItemsCount(userFollows);
                List<AuthUser> authUsers = userFollowsService.queryPage(authUser.getId(),rowIndex,pageSize);
                resultMap.put("success", true);
                resultMap.put("authUsers", authUsers);
                resultMap.put("count", count);
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
     * 返回当前学习课程进度信息
     * @param request
     * @return
     */
    @RequestMapping(value = "/currentstudy", method = RequestMethod.GET)
    @ResponseBody
    private Map<String ,Object> CurrentStudy(HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<>();
        Long courseId = HttpServletRequestUtil.getLong(request,"courseId");
        try {
            AuthUser authUser = (AuthUser)request.getSession().getAttribute("userinfo");
            if (authUser != null) {
                UserCourseSection userCourseSection = new UserCourseSection();
                userCourseSection.setUserId(authUser.getId());
                userCourseSection.setCourseId(courseId);
                UserCourseSection record = userCourseSectionService.queryLatest(userCourseSection);
                if (record!=null){
                    resultMap.put("success", true);
                    resultMap.put("record", record);
                }else{
                    resultMap.put("success", false);
                    resultMap.put("message","未查询相关学习进度" );
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
     * 更新（新增记录）课程进度信息
     * @param request
     * @return
     */
    @RequestMapping(value = "/addrecord", method = RequestMethod.GET)
    @ResponseBody
    private Map<String ,Object> AddRecord(HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<>();
        Long courseId = HttpServletRequestUtil.getLong(request,"courseId");
        Long sectionId = HttpServletRequestUtil.getLong(request,"sectionId");
        try {
            AuthUser authUser = (AuthUser)request.getSession().getAttribute("userinfo");
            if (authUser != null) {
                UserCourseSection userCourseSection = new UserCourseSection();
                userCourseSection.setUserId(authUser.getId());
                userCourseSection.setCourseId(courseId);
                userCourseSection.setSectionId(sectionId);
                userCourseSectionService.createSelectivity(userCourseSection);
                resultMap.put("success", true);
                resultMap.put("message", "添加成功！");
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
     * 返回当前用户学习记录
     * @param request
     * @return
     */
    @RequestMapping(value = "/studylist", method = RequestMethod.GET)
    @ResponseBody
    private Map<String ,Object> studylist(HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<>();
        Long courseId = HttpServletRequestUtil.getLong(request,"courseId");
        int pageIndex = HttpServletRequestUtil.getInteger(request,"pageIndex");
        int pageSize = 10;
        int rowIndex = PageCalculator.calculateRowIndex(pageIndex,pageSize);
        try {
            AuthUser authUser = (AuthUser)request.getSession().getAttribute("userinfo");
            if (authUser != null) {
                UserCourseSectionDTO userCourseSectionDTO = new UserCourseSectionDTO();
                userCourseSectionDTO.setUserId(authUser.getId());
                userCourseSectionDTO.setCourseId(courseId);
                List<UserCourseSectionDTO> recordList = userCourseSectionService.queryPage(userCourseSectionDTO, rowIndex, pageSize);
                resultMap.put("success", true);
                resultMap.put("recordList", recordList);
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
     * 返回已加入课程
     * @param request
     * @return
     */
    @RequestMapping(value = "/usercourselist", method = RequestMethod.GET)
    @ResponseBody
    private Map<String ,Object> UserCourseList(HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<>();
        int pageIndex = HttpServletRequestUtil.getInteger(request,"pageIndex");
        //页面大小
        int pageSize = HttpServletRequestUtil.getInteger(request,"pageSize");
        //对应数据库行数
        int rowIndex = PageCalculator.calculateRowIndex(pageIndex,pageSize);
        try {
            AuthUser authUser = (AuthUser)request.getSession().getAttribute("userinfo");
            UserStudyCourse studyCourse = new UserStudyCourse();
            if (authUser != null) {
                studyCourse.setUserId(authUser.getId());
                List<UserStudyCourseVO> studyCourseList = userStudyCourseService.queryPage(studyCourse, rowIndex, pageSize);
                Integer count = userStudyCourseService.getTotalItemsCount(studyCourse);
                if(studyCourseList!=null){
                    resultMap.put("success", true);
                    resultMap.put("count", count);
                    resultMap.put("record", studyCourseList);
                }else {
                    resultMap.put("success", false);
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
     * 用户加入课程
     * @param request
     * @return
     */
    @RequestMapping(value = "/addstudycourse", method = RequestMethod.GET)
    @ResponseBody
    private Map<String ,Object> addStudyCourse(HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<>();
        long courseId = HttpServletRequestUtil.getLong(request, "courseId");
        try {
            AuthUser authUser = (AuthUser)request.getSession().getAttribute("userinfo");
            UserStudyCourse studyCourse = new UserStudyCourse();
            if (authUser != null) {
                if (courseId!=-1){
                    studyCourse.setUserId(authUser.getId());
                    studyCourse.setCourseId(courseId);
                    studyCourse.setCreateUser(authUser.getAccount());
                    studyCourse.setUpdateUser(authUser.getAccount());
                    userStudyCourseService.createSelectivity(studyCourse);
                    resultMap.put("success", true);
                    resultMap.put("message", "添加成功");
                }else{
                    resultMap.put("success", true);
                    resultMap.put("message", "添加失败，请刷新重试");
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
     * 用户移除课程
     * @param request
     * @return
     */
    @RequestMapping(value = "/rmovecourse", method = RequestMethod.GET)
    @ResponseBody
    private Map<String, Object> removeCourse(HttpServletRequest request){
        Map<String, Object> resultMap = new HashMap<>();
        Long objectId = HttpServletRequestUtil.getLong(request, "courseId");
        try {
            AuthUser authUser = (AuthUser)request.getSession().getAttribute("userinfo");
            if (authUser != null) {
                UserStudyCourse userStudyCourse = new UserStudyCourse();
                userStudyCourse.setUserId(authUser.getId());
                userStudyCourse.setCourseId(objectId);
                userStudyCourseService.delete(userStudyCourse);
                resultMap.put("success", true);
                resultMap.put("message", "课程已从列表移除");
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
}
