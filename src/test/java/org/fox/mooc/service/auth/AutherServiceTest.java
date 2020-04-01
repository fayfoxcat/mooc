package org.fox.mooc.service.auth;

import org.fox.mooc.BaseTest;
import org.fox.mooc.entity.AuthUser;
import org.fox.mooc.vo.GraphVO;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class AutherServiceTest extends BaseTest{
    @Autowired
    private AuthUserService authUserService;



    @Test
    @Ignore
    public void queryRecomd() {
        List<AuthUser> authUsers = authUserService.queryRecomd();
        System.out.println(authUsers.toString());
        System.out.println(authUsers.size());

    }

    @Test
    public void querygraph() {
        AuthUser authUser = new AuthUser();
        List<GraphVO> userGraph = authUserService.queryGraph(authUser);
        authUser.setRole(2);
        List<GraphVO> teacherGraph = authUserService.queryGraph(authUser);
        System.out.println(teacherGraph.toString());

    }

    @Test
    @Ignore
    public void updateuserinfo() {
        AuthUser authUser = new AuthUser();
        authUser.setId(202012L);
        authUser.setRole(2);
        authUserService.updateSelectivity(authUser);
    }

    @Test
    public void byCourseIdQueryTest() {
       Long courseId = 19960101L;
        List<AuthUser> authUsers = authUserService.byCourseIdQuery(courseId, 0, 10);
        System.out.println(authUsers.toString());

    }

}
