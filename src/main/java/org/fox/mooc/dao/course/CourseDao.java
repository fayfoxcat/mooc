package org.fox.mooc.dao.course;

import org.fox.mooc.dto.CourseQueryDTO;
import org.fox.mooc.entity.course.Course;
import org.fox.mooc.vo.GraphVO;

import java.util.List;

/**
 * @Author by fairyfox
 * 2020/2/20-1115
 */
public interface CourseDao {

    /**
     * 根据id获取
     */
    public Course getById(Long id) ;

    /**
     * 根据条件获取所有，
     * queryEntity：查询条件；
     */
    public List<Course> queryList(CourseQueryDTO queryEntity ) ;

    /**
     * 返回图表所需数据
     */
    public List<GraphVO> queryGraph(Long courseId);

    /**
     * 获取总数量
     */
    public Integer getTotalItemsCount(Course course );

    /**
     * 分页获取
     */
    //    public List<Course> queryPage(Course queryEntity , TailPage<Course> page);

    /**
     * 创建新记录
     */
    public void create(Course entity);

    public void createSelectivity(Course entity );

    /**
     * 根据id更新
     */
    public void update(Course entity );

    /**
     * 根据id选择性更新自动
     */
    public void updateSelectivity(Course entity );

    /**
     * 物理删除
     */
    public void delete(Course entity );

    /**
     * 逻辑删除
     */
    public void deleteLogic(Course entity );
}
