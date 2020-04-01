package org.fox.mooc.dao.auth;

import org.fox.mooc.BaseTest;
import org.fox.mooc.entity.UserCollections;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Iterator;
import java.util.List;

public class UserCollectionDaoTest extends BaseTest{
    @Autowired
    private UserCollectionDao userCollectionDao;

    @Test
    public void testupdateUser() {
        UserCollections userCollections = new  UserCollections();
        userCollections.setUserId(202003L);

        List<UserCollections> userCollectionsList = userCollectionDao.queryPage(userCollections, 0, 5);
        Iterator<UserCollections> iterator = userCollectionsList.iterator();
        while (iterator.hasNext()) {
            UserCollections cc = iterator.next();
            System.out.println(cc.getBrief());
        }

    }

}
