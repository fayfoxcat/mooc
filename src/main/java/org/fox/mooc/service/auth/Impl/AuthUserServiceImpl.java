package org.fox.mooc.service.auth.Impl;

import org.fox.mooc.dao.auth.AuthUserDao;
import org.fox.mooc.dao.auth.UserMessagesDao;
import org.fox.mooc.dto.ImageDTO;
import org.fox.mooc.entity.AuthUser;
import org.fox.mooc.entity.UserMessage;
import org.fox.mooc.service.auth.AuthUserService;
import org.fox.mooc.util.ImageUtil;
import org.fox.mooc.util.PathUtil;
import org.fox.mooc.vo.GraphVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @Author by fairyfox
 * *2020/2/17-1505
 */
@Service
public class AuthUserServiceImpl implements AuthUserService {

    @Autowired
    private AuthUserDao authUserDao;

    @Autowired
    private UserMessagesDao userMessagesDao;

    /*根据account返回用户*/
    @Override
    public AuthUser getByAccount(String account )  {
        return authUserDao.getByAccount(account);
    }

    @Override
    public void createSelectivity(AuthUser authUser ) {
        authUserDao.createSelectivity(authUser);
    }

    /**
     * 根据id返回用户
     */
    @Override
    public AuthUser getById(Long id){
        return authUserDao.getById(id);
    }

    /*校验登录*/
    @Override
    public AuthUser getByUsernameAndPassword(AuthUser authUser )  {
        return authUserDao.getByUsernameAndPassword(authUser);
    }

    @Override
    public List<AuthUser> queryRecomd() {
        return authUserDao.queryRecomd();
    }

    /**
     * 根据条件分页查询用户记录列表
     * @param authUser
     * @param rowIndex
     * @param pageSize
     * @return
     */
    public List<AuthUser> queryPage(AuthUser authUser , Integer rowIndex, Integer pageSize)  {
        return authUserDao.queryPage(authUser, rowIndex, pageSize);
    }

    @Override
    public List<AuthUser> byCourseIdQuery(Long courseId, Integer rowIndex, Integer pageSize) {
        return authUserDao.byCourseIdQuery(courseId, rowIndex, pageSize);
    }

    /**
     * 获取用户总记录数
     * @param authUser
     * @return
     */
    @Override
    public Integer getTotalItemsCount(AuthUser authUser ){
        return authUserDao.getTotalItemsCount(authUser);
    }

    @Override
    public List<GraphVO> queryGraph(AuthUser authUser) {
        return authUserDao.queryGraph(authUser);
    }

    @Override
    public void update(AuthUser entity ) {

    }

    /*更新用户信息（更新密码）*/
    @Override
    public void  updateSelectivity( AuthUser authUser ) {
        authUserDao.updateSelectivity(authUser);
    }

    /*更新用户信息（更新权限、角色更新）*/
    @Override
    @Transactional
    public void  updateRole( AuthUser user ,AuthUser authUser) {
        UserMessage userMessage = new UserMessage();
        userMessage.setContent("我们收到了您的申请请求，但由于我们无法核实材料的真实有效，拒绝了您的请求，请您重新整理有效的凭证！我们期待您的加入");
        if (user.getRole()==2){//更新权限为讲师
            user.setCreateTime(new Date());
            authUserDao.updateSelectivity(user);
            userMessage.setContent("我们收到了您的申请请求，鉴于您的申请材料有效，同意了该申请，现在您是一名讲师了！");
        }
        userMessage.setUserId(user.getId());
        userMessage.setSendUserId(authUser.getId());
        userMessage.setSendUserName(authUser.getUsername());
        userMessage.setRefId("-1");
        userMessage.setType(-1);
        userMessage.setStatus(0);
        userMessage.setCreateTime(new Date());
        userMessage.setUpdateTime(new Date());
        userMessage.setCreateUser(authUser.getAccount());
        userMessage.setUpdateUser(authUser.getAccount());
        userMessage.setDel(0);
        userMessagesDao.create(userMessage);
    }


    /*更新用户头像*/
    @Override
    public void headerupdate(AuthUser authUser, ImageDTO imageDTO) {
        try{
            //先获取一遍原有信息，因为原来的信息里有原图片地址
            if (authUser.getHeader() != null) {
                ImageUtil.deleteFileOrPath(authUser.getHeader());
            }
            String dest = PathUtil.AuthHeaderPath(authUser.getAccount());
            String imageAddr = ImageUtil.ImagePath(imageDTO,dest);
            authUser.setHeader(imageAddr);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        authUserDao.updateSelectivity(authUser);
    }

    /*更新用户信息（讲师信息完善等）*/
    @Override
    @Transactional
    public void applyupdate(AuthUser authUser, ImageDTO imageDTO) {
        try{
            //先获取一遍原有信息，因为原来的信息里有原图片地址
            if (authUser.getCertificate() != null) {
                ImageUtil.deleteFileOrPath(authUser.getCertificate());
            }
            String dest = PathUtil.CertificatePath(authUser.getAccount());
            String imageAddr = ImageUtil.ImagePath(imageDTO,dest);
            authUser.setCertificate(imageAddr);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        authUserDao.updateSelectivity(authUser);
        UserMessage userMessage = new UserMessage();
        userMessage.setUserId(-1L);
        userMessage.setSendUserId(authUser.getId());
        userMessage.setSendUserName(authUser.getUsername());
        userMessage.setRefId("-1");
        userMessage.setContent("用户有新的申请提交！请及时处理");
        userMessage.setType(2);
        userMessage.setStatus(0);
        userMessage.setCreateTime(new Date());
        userMessage.setUpdateTime(new Date());
        userMessage.setCreateUser(authUser.getUsername());
        userMessage.setUpdateUser(authUser.getUsername());
        userMessage.setDel(0);
        userMessagesDao.create(userMessage);
    }

    @Override
    public void delete(AuthUser authUser ) {
        authUserDao.delete(authUser);
    }

    @Override
    public void deleteLogic(AuthUser entity ) {

    }
}
