package org.fox.mooc.service.course;

import org.fox.mooc.BaseTest;
import org.fox.mooc.entity.course.CourseSiteCarousel;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Iterator;
import java.util.List;


public class CourseSiteCarouselServiceTest extends BaseTest{
    @Autowired
    private CourseSiteCarouselService courseSiteCarouselService;

    @Test
    public void queryAllTest() {
        List<CourseSiteCarousel> courseSiteCarousels = courseSiteCarouselService.queryCarousels(4);
        Iterator<CourseSiteCarousel> iterator = courseSiteCarousels.iterator();
        while (iterator.hasNext()) {
            CourseSiteCarousel cc = iterator.next();
            System.out.println(cc.getName()+cc.getUrl());
        }
    }

}
