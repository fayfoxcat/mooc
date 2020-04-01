package org.fox.mooc.controller.frontend;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @Author by fairyfox
 * *2020/1/31-14:10
 */
@Controller
class FrontendController {
    @RequestMapping(value = "/course", method = RequestMethod.GET)
    private String Course(){
        return "/students/course";
    }

    @RequestMapping(value = "/video", method = RequestMethod.GET)
    private String Video(){
        return "/students/video";
    }

    @RequestMapping(value = "/personinfo", method = RequestMethod.GET)
    private String Personinfo(){
        return "/students/personinfo";
    }

    @RequestMapping(value = "/classifypage", method = RequestMethod.GET)
    private String ClassifyPage(){
        return "/students/classifypage";
    }

    @RequestMapping(value = "/teachinfo", method =RequestMethod.GET)
    private String TeachInfo(){
        return "/teachers/teachinfo";
    }

    @RequestMapping(value = "/coursemanage", method = RequestMethod.GET)
    private String CourseManage(){
        return "/teachers/coursemanage";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    private String Login(){
        return "/superadmin/login";
    }

    @RequestMapping(value = "/admin", method = RequestMethod.GET)
    private String Admin(){
        return "/superadmin/admin";
    }
}
