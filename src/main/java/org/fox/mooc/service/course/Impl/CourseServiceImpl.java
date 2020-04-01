package org.fox.mooc.service.course.Impl;

import org.fox.mooc.dao.auth.UserMessagesDao;
import org.fox.mooc.dao.auth.UserStudyCourseDao;
import org.fox.mooc.dao.course.CourseDao;
import org.fox.mooc.dto.CourseQueryDTO;
import org.fox.mooc.dto.ImageDTO;
import org.fox.mooc.entity.AuthUser;
import org.fox.mooc.entity.UserMessage;
import org.fox.mooc.entity.UserStudyCourse;
import org.fox.mooc.entity.course.Course;
import org.fox.mooc.service.course.CourseService;
import org.fox.mooc.util.ImageUtil;
import org.fox.mooc.util.PathUtil;
import org.fox.mooc.vo.GraphVO;
import org.fox.mooc.vo.UserStudyCourseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @Author by fairyfox
 * *2020/2/20-14:11
 */
@Service
public class CourseServiceImpl implements CourseService {
    @Autowired
    private CourseDao courseDao;

    @Autowired
    private UserMessagesDao userMessagesDao;

    @Autowired
    private UserStudyCourseDao userStudyCourseDao;

    private void prepareCoursePicture(Course course) {
        return;
    }


    /*根据id返回当前课程信息*/
    @Override
    public Course getById(Long id){
        return courseDao.getById(id);
    }

    @Override
    public List<Course> queryList(CourseQueryDTO courseQueryDTO){
        /*判断前台数据是否为空*/
        if(courseQueryDTO.getAccount()==null||courseQueryDTO.getAccount().equals("null")){
            courseQueryDTO.setAccount(null);
        }
        if(courseQueryDTO.getClassify()==null||courseQueryDTO.getClassify().equals("null")){
            courseQueryDTO.setClassify(null);
        }
        if(courseQueryDTO.getSubClassify()==null||courseQueryDTO.getSubClassify().equals("null")){
            courseQueryDTO.setSubClassify(null);
        }
        if(courseQueryDTO.getSortField()==null||courseQueryDTO.getSortField().equals("null")){
            courseQueryDTO.setSortField(null);
        }
        if (courseQueryDTO.getCourseName()==null||courseQueryDTO.getCourseName().equals("null")){
            courseQueryDTO.setCourseName(null);
        }
        if (courseQueryDTO.getOwnerName()==null||courseQueryDTO.getOwnerName().equals("null")){
            courseQueryDTO.setOwnerName(null);
        }
        if (courseQueryDTO.getRowIndex()!=null) {
            if (courseQueryDTO.getRowIndex()==-1){
                courseQueryDTO.setOnsale(null);
            }
        }
        if (courseQueryDTO.getPageSize()!=null) {
            if (courseQueryDTO.getPageSize()==-1){
                courseQueryDTO.setRowIndex(null);
            }
        }
        return courseDao.queryList(courseQueryDTO);
    }

    @Override
    public List<GraphVO> queryGraph(Long courseId) {
        return courseDao.queryGraph(courseId);
    }

    @Override
    public Integer getTotalItemsCount(CourseQueryDTO course) {
        /*判断前台数据是否为空*/
        if(course.getAccount()==null||course.getAccount().equals("null")){
            course.setAccount(null);
        }
        if(course.getClassify()==null||course.getClassify().equals("null")){
            course.setClassify(null);
        }
        if(course.getSubClassify()==null||course.getSubClassify().equals("null")){
            course.setSubClassify(null);
        }
        if (course.getCourseName()==null||course.getCourseName().equals("null")){
            course.setCourseName(null);
        }
        if (course.getOwnerName()==null||course.getOwnerName().equals("null")){
            course.setOwnerName(null);
        }
        return courseDao.getTotalItemsCount(course);
    }


    @Override
    @Transactional
    public void createSelectivity(AuthUser authUser, Course course , ImageDTO imageDTO) {
        try{
            //先获取一遍原有信息，因为原来的信息里有原图片地址
            if (course.getPicture() != null) {
                ImageUtil.deleteFileOrPath(course.getPicture());
            }
            String dest = PathUtil.CoursePath(course.getId().toString());
            String imageAddr = ImageUtil.ImagePath(imageDTO,dest);
            course.setPicture(imageAddr);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        course.setCreateTime(new Date());
        course.setUpdateTime(new Date());
        courseDao.createSelectivity(course);
        /*生成消息*/
        UserMessage userMessage = new UserMessage();
        userMessage.setUserId(2020L);
        userMessage.setSendUserId(authUser.getId());
        userMessage.setSendUserName(authUser.getUsername());
        userMessage.setRefId("-1");
        userMessage.setContent("用户有新的课程提交！请及时处理");
        userMessage.setType(3);
        userMessage.setStatus(0);
        userMessage.setCreateTime(new Date());
        userMessage.setUpdateTime(new Date());
        userMessage.setCreateUser(authUser.getUsername());
        userMessage.setUpdateUser(authUser.getUsername());
        userMessage.setDel(0);
        userMessagesDao.create(userMessage);
    }

    @Override
    public void updateSelectivity(Course course, ImageDTO imageDTO) {
        try{
            //先获取一遍原有信息，因为原来的信息里有原图片地址
            if (course.getPicture() != null) {
                ImageUtil.deleteFileOrPath(course.getPicture());
            }
            String dest = PathUtil.CoursePath(course.getId().toString());
            String imageAddr = ImageUtil.ImagePath(imageDTO,dest);
            course.setPicture(imageAddr);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        courseDao.updateSelectivity(course);
    }

    /*课程上下架变更*/
    @Override
    @Transactional
    public void updateStatus(Course course) {
        if (course.getOnsale() == 0){//下架通知参加课程的用户
            //在下架之前获取课程名
            String courseName = courseDao.getById(course.getId()).getCourseName();
            UserStudyCourse userStudyCourse = new UserStudyCourse();
            userStudyCourse.setCourseId(course.getId());
            List<UserStudyCourseVO> userList = userStudyCourseDao.queryPage(userStudyCourse, null, null);
            for (UserStudyCourseVO user:userList){
                UserMessage message = new UserMessage();
                message.setUserId(user.getUserId());
                message.setSendUserId(-1L);
                message.setSendUserName("管理员");
                message.setRefId("-1");
                message.setType(3);
                message.setContent("您参加的课程"+courseName+"已下架，感谢您的参与！");
                message.setStatus(0);
                message.setDel(0);
                message.setCreateTime(new Date());
                message.setUpdateTime(new Date());
                userMessagesDao.createSelectivity(message);
            }
        }
        courseDao.updateSelectivity(course);
    }

    //物理删除
    @Override
    public void delete(Course course) {
        courseDao.delete(course);
    }

    //逻辑删除
    @Override
    public void deleteLogic(Course course) {
        courseDao.deleteLogic(course);
    }
}
