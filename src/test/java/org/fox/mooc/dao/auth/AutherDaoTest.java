package org.fox.mooc.dao.auth;

import org.fox.mooc.BaseTest;
import org.fox.mooc.entity.AuthUser;
import org.fox.mooc.service.auth.AuthUserService;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class AutherDaoTest extends BaseTest{
    @Autowired
    private AuthUserService authUserService;

    @Autowired
    private AuthUserDao authUserDao;

    @Test
    @Ignore
    public void testupdateUser() {
        AuthUser authUser = new AuthUser();
        authUser.setId(202003L);

        String birthday = "2020-01-01";

        Date date = new Date(birthday.replace("-", "/"));
        authUser.setBirthday(date);
        authUserService.updateSelectivity(authUser);

    }

    @Test
    public void testQueryPage() {
        AuthUser authUser = new  AuthUser();
        authUser.setRole(3);
        List<AuthUser> authUserlist = authUserDao.queryPage(authUser, 0, 5);
        Iterator<AuthUser> iterator = authUserlist.iterator();
        while (iterator.hasNext()) {
            AuthUser cc = iterator.next();
            System.out.println(cc.getAccount());
        }

        int count = authUserDao.getTotalItemsCount(authUser);
        System.out.println(count);
    }

}
