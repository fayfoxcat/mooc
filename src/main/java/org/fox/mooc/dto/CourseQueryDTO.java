package org.fox.mooc.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.fox.mooc.entity.course.Course;

/**
 * @Author by fairyfox
 * *2020/2/20-11:50
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CourseQueryDTO extends Course{

    private static final long serialVersionUID = -2867209700477783861L;
    //排序方法（学习数量、权重）
    private String sortField;

    //当前页
    private Integer rowIndex;

    //返回多少条数据
    private Integer pageSize;

}
