package org.fox.mooc.service.course;

import org.fox.mooc.dto.CourseQueryDTO;
import org.fox.mooc.dto.ImageDTO;
import org.fox.mooc.entity.AuthUser;
import org.fox.mooc.entity.course.Course;
import org.fox.mooc.vo.GraphVO;

import java.util.List;

/**
 * @Author by fairyfox
 * 2020/2/20-1410
 */
public interface CourseService {
    /**
     * 根据id获取
     */
    public Course getById(Long id ) ;

    /**
     * 获取所有
     */
    public List<Course> queryList(CourseQueryDTO queryEntity );

    /**
     * 返回图表所需数据
     */
    public List<GraphVO> queryGraph(Long courseId);

    /**
     * 分页获取
     */
    //    public TailPage<Course> queryPage(Course queryEntity ,TailPage<Course> page);

    /**
     * 获取总数量
     */
    public Integer getTotalItemsCount(CourseQueryDTO course );

    /**
     * 创建
     */
    public void createSelectivity(AuthUser authUser,Course course, ImageDTO imageDTO );

    /**
     * 根据id 进行可选性更新
     */
    public void updateSelectivity(Course course, ImageDTO imageDTO);

    /**
     * 根据id 进行更新课状态、学习人数
     */
    public void updateStatus(Course course);

    /**
     * 物理删除
     */
    public void delete(Course entity );

    /**
     * 逻辑删除
     */
    public void deleteLogic(Course entity );
}
