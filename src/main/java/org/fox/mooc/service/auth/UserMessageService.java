package org.fox.mooc.service.auth;

import org.fox.mooc.entity.UserMessage;
import org.fox.mooc.vo.UserMessageVO;

import java.util.List;

/**
 * @Author by fairyfox
 * 2020/2/29-1208
 */
public interface UserMessageService {
    /**
     * 根据id获取
     */
    public UserMessage getById(Long id) ;

    /*
     * 根据userId获取
     */
    public List<UserMessage> getByUserId(Long userId);

    /**
     * 分页获取
     */
    public List<UserMessageVO> queryPage(UserMessage userMessage, Integer rowIndex, Integer pageSize);

    /**
     * 分页获取（申请消息）
     */
    public List<UserMessageVO> queryApplyPage( UserMessage userMessage,  Integer rowIndex,  Integer pageSize);

    /**
     * 获取所有
     */
    public List<UserMessage> queryAll(UserMessage queryEntity );

    /**
     * 获取当前用户未读消息数量
     * @param queryEntity
     * @return
     */
    public Integer getTotalItemsCount(UserMessage queryEntity);

    /**
     * 分页获取
     */
    //    public TailPage<UserMessage> queryPage(UserMessage queryEntity ,TailPage<UserMessage> page);

    /**
     * 创建
     */
    public void create(UserMessage entity );

    /**
     * 选择性创建新记录
     */
    public void createSelectivity(UserMessage userMessage);

    /**
     * 根据id更新
     */
    public void update(UserMessage entity );

    /**
     * 根据id 进行可选性更新
     */
    public void updateSelectivity(UserMessage entity );

    /**
     * 物理删除
     */
    public void delete(UserMessage entity );

    /**
     * 逻辑删除
     */
    public void deleteLogic(UserMessage entity );
}
