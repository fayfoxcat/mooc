package org.fox.mooc.dao.course;

import org.fox.mooc.entity.course.CourseSiteCarousel;

import java.util.List;

/**
 * @Author by fairyfox
 * *2020/2/13-916
 */
public interface CourseSiteCarouselDao {
    /**
     * 根据id获取
     */
    public CourseSiteCarousel getById(Long id);

    /**
     * 获取轮播
     */
    public List<CourseSiteCarousel> queryCarousels(Integer count);

    /**
     * 获取所有
     */
    public List<CourseSiteCarousel> queryAll(CourseSiteCarousel queryEntity);

    /**
     * 获取总数量
     */
    public Integer getTotalItemsCount(CourseSiteCarousel queryEntity);

    /**
     * 分页获取
     */
    //    public List<CourseSiteCarousel> queryPage(CourseSiteCarousel queryEntity , TailPage<CourseSiteCarousel> page);

    /**
     * 创建新记录
     */
    public void create(CourseSiteCarousel entity );

    /**
     * 创建新记录
     */
    public void createSelectivity(CourseSiteCarousel entity );

    /**
     * 根据id更新
     */
    public void update(CourseSiteCarousel entity );

    /**
     * 根据id选择性更新自动
     */
    public void updateSelectivity(CourseSiteCarousel entity );

    /**
     * 物理删除
     */
    public void delete(CourseSiteCarousel entity );

    /**
     * 逻辑删除
     */
    public void deleteLogic(CourseSiteCarousel entity );
}
