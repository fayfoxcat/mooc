package org.fox.mooc.service.auth;

import org.apache.commons.collections4.CollectionUtils;
import org.fox.mooc.BaseTest;
import org.fox.mooc.entity.UserFollows;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


public class UserFollowsTest extends BaseTest{

    @Autowired
    private UserFollowsService userFollowsService;

    @Test
    public void isFollowsTest() {
        UserFollows userFollows = new UserFollows();
        userFollows.setUserId(202003L);
        userFollows.setFollowId(202003L);
        List<UserFollows> list = userFollowsService.queryAll(userFollows);
        if(CollectionUtils.isNotEmpty(list)){
            System.out.println(list.get(0).getUserId());
        }else{
            System.out.println(false);
        }
    }
}
