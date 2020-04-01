package org.fox.mooc.controller.frontend;

import org.fox.mooc.entity.course.CourseClassify;
import org.fox.mooc.entity.course.CourseSiteCarousel;
import org.fox.mooc.service.course.CourseClassifyService;
import org.fox.mooc.service.course.CourseService;
import org.fox.mooc.service.course.CourseSiteCarouselService;
import org.fox.mooc.util.HttpServletRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @Author by fairyfox
 * *2020/2/11-16:43
 */
@Controller
@RequestMapping(value = "/homeshow", method = RequestMethod.GET)
class HomeShowController {

    @Autowired
    private CourseClassifyService courseClassifyService;

    @Autowired
    private CourseSiteCarouselService courseSiteCarouselService;

    @Autowired
    private CourseService courseService;

    /*获取顶级分类列表*/
    @ResponseBody
    @RequestMapping(value = "/querytopclassify")
    private Map<String,Object> queryTopClassify() {
        Map<String,Object> resultMap = new HashMap<>();
        List<CourseClassify> topClassifylist = courseClassifyService.queryAll();
        Iterator<CourseClassify> iterator = topClassifylist.iterator();
        while (iterator.hasNext()) {
            if (!"0".equals(iterator.next().getParentCode())) {
                iterator.remove();//使用迭代器的删除方法删除二级分类
            }
        }
        resultMap.put("success",true);
        resultMap.put("classifylist",topClassifylist);
        return resultMap;
    }

    /*获取二级分类列表*/
    @ResponseBody
    @RequestMapping(value = "/subclassifylist")
    private Map<String,Object> subClassifyList(){
        Map<String,Object> resultMap = new HashMap<>();
        List<CourseClassify> subClassifylist = courseClassifyService.queryAll();
        Iterator<CourseClassify> iterator = subClassifylist.iterator();
        while (iterator.hasNext()) {
            if ("0".equals(iterator.next().getParentCode())) {
                iterator.remove();//使用迭代器的删除方法删除一级分类
            }
        }
        resultMap.put("success",true);
        resultMap.put("subclassifylist",subClassifylist);
        return resultMap;
    }

    /*根据顶级分类获取指定二级分类*/
    @ResponseBody
    @RequestMapping(value = "/querysubclassify")
    private Map<String,Object> querySubClassify(HttpServletRequest request){
        Map<String,Object> resultMap = new HashMap<>();
        String code = HttpServletRequestUtil.getString(request, "code");
        CourseClassify courseClassify = new CourseClassify();
        courseClassify.setParentCode(code);
        try {
            List<CourseClassify> SubClassifylist = courseClassifyService.queryByCondition(courseClassify);
            resultMap.put("success",true);
            resultMap.put("subcalssifylist",SubClassifylist);
        } catch (Exception e) {
            resultMap.put("success",false);
            resultMap.put("message",e.getMessage());
        }
        return resultMap;

    }

    /**
     * 轮播图
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/querysitecarousel")
    private Map<String,Object> querySiteCarousel(HttpServletRequest request){
        Map<String,Object> resultMap = new HashMap<>();
        try {
            Integer count = HttpServletRequestUtil.getInteger(request, "count");
            List<CourseSiteCarousel> courseSiteCarousels = courseSiteCarouselService.queryCarousels(count);
            resultMap.put("success",true);
            resultMap.put("sitecarousellist",courseSiteCarousels);
        } catch (Exception e) {
            resultMap.put("success",false);
            resultMap.put("message",e.getMessage());
        }
        return resultMap;
    }

}
