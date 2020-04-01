package org.fox.mooc.service.auth;

import org.fox.mooc.dto.ImageDTO;
import org.fox.mooc.entity.AuthUser;
import org.fox.mooc.vo.GraphVO;

import java.util.List;

/**
 * @Author by fairyfox
 * 2020/2/17-1458
 */
public interface AuthUserService {
    /**
     * 根据username获取
     */
    public AuthUser getByAccount(String account ) ;

    /**
     * 创建
     */
    public void createSelectivity(AuthUser entity );


    /**
     * 根据id获取
     */
    public AuthUser getById(Long id);

    /**
     * 根据username和password获取
     */
    public AuthUser getByUsernameAndPassword(AuthUser authUser );

    /**
     * 获取首页推荐5个讲师
     */
    public List<AuthUser> queryRecomd();

    /**
     * 分页获取(含模糊查询)
     */
    public List<AuthUser> queryPage(AuthUser authUser , Integer rowIndex, Integer pageSize) ;

    /**
     * 根据courseId查询用户
     */
    public List<AuthUser> byCourseIdQuery(Long courseId , Integer rowIndex, Integer pageSize) ;

    /**
     * 获取总数量
     */
    public Integer getTotalItemsCount(AuthUser authUser ) ;

    /**
     * 返回图表所需数据
     */
    public List<GraphVO> queryGraph(AuthUser authUser);

    /**
     * 根据id更新
     */
    public void update(AuthUser entity );

    /**
     * 根据id 进行可选性更新
     */
    public void updateSelectivity(AuthUser entity );

    /**
     * 根据id 进行角色更新
     */
    public void updateRole(AuthUser user,AuthUser authUser);

    /**
     * 更换头像申请
     */
    public void headerupdate(AuthUser authUser,ImageDTO imageDTO);

    /**
     * 提交申请更新请求
     */
    public void applyupdate(AuthUser authUser,ImageDTO imageDTO);

    /**
     * 物理删除
     */
    public void delete(AuthUser entity );

    /**
     * 逻辑删除
     */
    public void deleteLogic(AuthUser entity );
}
