package org.fox.mooc.service.course;

import org.fox.mooc.dto.ImageDTO;
import org.fox.mooc.entity.course.CourseSiteCarousel;

import java.util.List;

/**
 * @Author by fairyfox
 * 2020/2/13-939
 */
public interface CourseSiteCarouselService {

    /**
     * 根据id获取
     */
    public CourseSiteCarousel getById(Long id) ;

    /**
     * 获取所有
     */
    public List<CourseSiteCarousel> queryCarousels(Integer count) ;

    /**
     * 分页获取
     */
    //    public TailPage<CourseSiteCarousel> queryPage(CourseSiteCarousel queryEntity ,TailPage<CourseSiteCarousel> page);

    /**
     * 创建
     */
    public void create(CourseSiteCarousel entity );

    /**
     * 创建新记录
     */
    public void createSelectivity(CourseSiteCarousel courseSiteCarousel, ImageDTO imageDTO);

    /**
     * 根据id更新
     */
    public void update(CourseSiteCarousel entity );

    /**
     * 根据id 进行可选性更新
     */
    public void updateSelectivity(CourseSiteCarousel entity,ImageDTO imageDTO );

    /**
     * 物理删除
     */
    public void delete(CourseSiteCarousel entity );

    /**
     * 逻辑删除
     */
    public void deleteLogic(CourseSiteCarousel entity );
}
