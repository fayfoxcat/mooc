package org.fox.mooc.dao.auth;

import org.apache.ibatis.annotations.Param;
import org.fox.mooc.entity.UserMessage;
import org.fox.mooc.vo.UserMessageVO;

import java.util.List;

/**
 * @Author by fairyfox
 * 2020/2/29-1049
 */
public interface UserMessagesDao {
    /**
     * 根据id获取
     */
    public UserMessage getById(Long id);

    /*
     * 根据userId获取
     */
    public List<UserMessage> getByUserId(Long userId);

    /**
     * 获取所有
     */
    public List<UserMessage> queryAll(UserMessage queryEntity);

    /**
     * 获取总数量
     */
    public Integer getTotalItemsCount(UserMessage queryEntity);

    /**
     * 分页获取
     */
    public List<UserMessageVO> queryPage(@Param("userMessage") UserMessage userMessage, @Param("rowIndex") Integer rowIndex, @Param("pageSize") Integer pageSize);

    /**
     * 分页获取（申请消息）
     */
    public List<UserMessageVO> queryApplyPage(@Param("userMessage") UserMessage userMessage, @Param("rowIndex") Integer rowIndex, @Param("pageSize") Integer pageSize);

    /**
     * 创建新记录
     */
    public void create(UserMessage entity);

    /**
     * 选择性创建新记录
     */
    public void createSelectivity(UserMessage entity);

    /**
     * 根据id更新
     */
    public void update(UserMessage entity);

    /**
     * 根据id选择性更新自动
     */
    public void updateSelectivity(UserMessage userMessage);

    /**
     * 物理删除
     */
    public void delete(UserMessage entity);

    /**
     * 逻辑删除
     */
    public void deleteLogic(UserMessage entity);
}
