package org.fox.mooc.controller.course;

import org.fox.mooc.entity.course.CourseSection;
import org.fox.mooc.enums.CourseEnum;
import org.fox.mooc.service.course.CourseSectionService;
import org.fox.mooc.util.HttpServletRequestUtil;
import org.fox.mooc.vo.CourseSectionVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @Author by fairyfox
 * *2020/2/21-15:59
 */
@Controller
@RequestMapping(value = "/section")
class CourseSectionController {

    @Autowired
    private CourseSectionService courseSectionService;

    @Autowired
    private CourseSectionService courseService;

    /**
     * 根据id获取当前章节列表
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/sectionlist", method = RequestMethod.GET)
    private Map<String, Object> SectionList(HttpServletRequest request){
        Map<String, Object> resultMap = new HashMap<>();
        Long id = HttpServletRequestUtil.getLong(request, "id");
        try {
            List<CourseSectionVO> courseSectionList = queryCourseSection(id);
            resultMap.put("success",true);
            resultMap.put("sectionlist",courseSectionList);
        } catch (Exception e) {
            resultMap.put("success",false);
            resultMap.put("message",e.getMessage());
        }
        return resultMap;

    }

    /**
     * 根据章节id返回对应课程url(播放页实现)
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/videourl", method = RequestMethod.POST)
    private Map<String, Object> VideoUrl(HttpServletRequest request){
        Map<String, Object> resultMap = new HashMap<>();
        Long sectionId = HttpServletRequestUtil.getLong(request, "sectionId");
        try {
            CourseSection courseSection = courseSectionService.getById(sectionId);
            resultMap.put("success",true);
            resultMap.put("videourl",courseSection);
        } catch (Exception e) {
            resultMap.put("success",false);
            resultMap.put("message",e.getMessage());
        }
        return resultMap;
    }

    /**
     * 获取课程章节(分离章节)
     */
    private List<CourseSectionVO> queryCourseSection(Long courseId){
        CourseSection courseSection = new CourseSection();
        courseSection.setCourseId(courseId);
        courseSection.setOnsale(CourseEnum.ONSALE.getValue());//上架

        Map<Long,CourseSectionVO> modelMap = new LinkedHashMap<>();
        for (CourseSection item : courseSectionService.queryAll(courseSection)) {
            //章
            if (Long.valueOf(0).equals(item.getParentId())) {
                CourseSectionVO vo = new CourseSectionVO();
                BeanUtils.copyProperties(item, vo);
                modelMap.put(vo.getId(), vo);
            } else {
                //小节添加到大章中
                modelMap.get(item.getParentId()).getSections().add(item);
            }
        }
        return new ArrayList<>(modelMap.values());
    }
}
