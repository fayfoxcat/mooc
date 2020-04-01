package org.fox.mooc.controller.auth;

import org.fox.mooc.entity.AuthUser;
import org.fox.mooc.entity.UserMessage;
import org.fox.mooc.entity.course.CourseComment;
import org.fox.mooc.service.course.CourseCommentService;
import org.fox.mooc.service.auth.UserMessageService;
import org.fox.mooc.util.HttpServletRequestUtil;
import org.fox.mooc.util.PageCalculator;
import org.fox.mooc.vo.UserMessageVO;
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
 * *2020/3/15-21:09
 */
@Controller
@RequestMapping(value = "/message")
public class MessageController {
    @Autowired
    private UserMessageService userMessageService;

    @Autowired
    private CourseCommentService courseCommentService;

    /**
     * 新建消息
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/createmessage", method = RequestMethod.POST)
    public Map<String, Object> CreateMessage(@RequestBody UserMessage userMessage, HttpServletRequest request){
        Map<String,Object> resultMap = new HashMap<>();
        try {
            AuthUser authUser = (AuthUser)request.getSession().getAttribute("userinfo");
            if (authUser != null) {
                userMessage.setSendUserId(authUser.getId());
                userMessage.setSendUserName(authUser.getUsername());
                userMessage.setType(1);
                userMessage.setCreateUser(authUser.getUsername());
                userMessage.setUpdateUser(authUser.getUsername());
                userMessageService.createSelectivity(userMessage);
                resultMap.put("success", true);
                resultMap.put("message", "消息发送成功！");
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
     * 根据用户id查询当前用户消息
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/messagelist", method = RequestMethod.POST)
    public Map<String, Object> MessageList(HttpServletRequest request){
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
                userMessage.setUserId(authUser.getId());
                Integer count = userMessageService.getTotalItemsCount(userMessage);
                List<UserMessageVO> messageList = userMessageService.queryPage(userMessage,rowIndex,pageSize);
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
     * 根据refid返回详细信息
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/comment", method = RequestMethod.GET)
    private Map<String,Object> Comment(HttpServletRequest request){
        Map<String,Object> resultMap = new HashMap<>();
        Long id = HttpServletRequestUtil.getLong(request, "id");
        try {
            CourseComment courseComment = courseCommentService.getById(id);
            resultMap.put("success",true);
            resultMap.put("courseComment",courseComment);
        } catch (Exception e) {
            resultMap.put("success",false);
            resultMap.put("message",e.getMessage());
        }
        return resultMap;
    }

    /**
     * 改变消息状态
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/modifystatus", method = RequestMethod.GET)
    public Map<String, Object> ModifyStatus(HttpServletRequest request){
        Map<String,Object> resultMap = new HashMap<>();
        AuthUser authuser = (AuthUser) request.getSession().getAttribute("userinfo");
        Long id = HttpServletRequestUtil.getLong(request, "id");
        try {
            UserMessage userMessage = new UserMessage();
            userMessage.setId(id);
            userMessage.setStatus(1);
            if (authuser != null) {
                userMessageService.updateSelectivity(userMessage);
                resultMap.put("success",true);
            } else {
                resultMap.put("success", false);
            }
        } catch (Exception e) {
            resultMap.put("success", false);
            resultMap.put("message", e.getMessage());
        }
        return resultMap;
    }
}
