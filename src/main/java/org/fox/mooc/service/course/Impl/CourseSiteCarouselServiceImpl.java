package org.fox.mooc.service.course.Impl;

import org.fox.mooc.dao.course.CourseSiteCarouselDao;
import org.fox.mooc.dto.ImageDTO;
import org.fox.mooc.entity.course.CourseSiteCarousel;
import org.fox.mooc.service.course.CourseSiteCarouselService;
import org.fox.mooc.util.ImageUtil;
import org.fox.mooc.util.PathUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @Author by fairyfox
 * *2020/2/13-945
 */
@Service
public class CourseSiteCarouselServiceImpl implements CourseSiteCarouselService {

    @Autowired
    private CourseSiteCarouselDao courseSiteCarouselDao;

    @Override 
    public CourseSiteCarousel getById(Long id )  {
        return courseSiteCarouselDao.getById(id);
    }

    /**
     * 获取轮播列表
     * @param count
     * @return
     */
    @Override 
    public List<CourseSiteCarousel> queryCarousels(Integer count )  {
        return courseSiteCarouselDao.queryCarousels(count);
    }

    @Override 
    public void create(CourseSiteCarousel entity ) {

    }

    @Override 
    public void createSelectivity(CourseSiteCarousel courseSiteCarousel , ImageDTO imageDTO) {
        try{
            //先获取一遍原有信息，因为原来的信息里有原图片地址
            if (courseSiteCarousel.getUrl() != null) {
                ImageUtil.deleteFileOrPath(courseSiteCarousel.getUrl());
            }
            String dest = PathUtil.CourselPath();
            String imageAddr = ImageUtil.ImagePath(imageDTO,dest);
            courseSiteCarousel.setPicture(imageAddr);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        courseSiteCarousel.setCreateTime(new Date());
        courseSiteCarouselDao.createSelectivity(courseSiteCarousel);
    }

    @Override 
    public void update(CourseSiteCarousel entity ) {

    }

    @Override 
    public void updateSelectivity(CourseSiteCarousel courseSiteCarousel ,ImageDTO imageDTO ) {
        try{
            //先获取一遍原有信息，因为原来的信息里有原图片地址
            if (courseSiteCarousel.getPicture() != null) {
                ImageUtil.deleteFileOrPath(courseSiteCarousel.getPicture());
            }
            String dest = PathUtil.CourselPath();
            String imageAddr = ImageUtil.ImagePath(imageDTO,dest);
            courseSiteCarousel.setPicture(imageAddr);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        courseSiteCarouselDao.updateSelectivity(courseSiteCarousel);
    }

    @Override 
    public void delete(CourseSiteCarousel courseSiteCarousel ) {
        courseSiteCarouselDao.delete(courseSiteCarousel);
    }

    @Override 
    public void deleteLogic(CourseSiteCarousel entity ) {

    }
}
