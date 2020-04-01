package org.fox.mooc.dao.course;

import org.fox.mooc.BaseTest;
import org.fox.mooc.entity.course.CourseSiteCarousel;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Iterator;
import java.util.List;


public class CourseSiteCarouselDaoTest extends BaseTest{
    @Autowired
    private CourseSiteCarouselDao courseSiteCarouselDao;

    @Test
    public void queryAllTest() {
        List<CourseSiteCarousel> courseSiteCarousels = courseSiteCarouselDao.queryCarousels(4);
        Iterator<CourseSiteCarousel> iterator = courseSiteCarousels.iterator();
        while (iterator.hasNext()) {
            CourseSiteCarousel cc = iterator.next();
            System.out.println(cc.getName());
        }
    }
}
