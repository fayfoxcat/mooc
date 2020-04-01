package org.fox.mooc.service.auth;

import org.fox.mooc.BaseTest;
import org.fox.mooc.entity.UserMessage;
import org.fox.mooc.vo.UserMessageVO;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


public class UserMessageDaoTest extends BaseTest{

    @Autowired
    private UserMessageService userMessageService;

    @Test
    public void UserMessageServiceTest() {
        UserMessage userMessage = new UserMessage();
        userMessage.setUserId(2020L);
        userMessage.setType(2);
        List<UserMessageVO> byUserId = userMessageService.queryPage(userMessage,0,10);
        System.out.println(byUserId.toString());

    }

    @Test
    @Ignore
    public void getcount() {
        UserMessage userMessage = new UserMessage();
        userMessage.setUserId(2020L);
        userMessage.setStatus(0);
        System.out.println(userMessageService.getTotalItemsCount(userMessage));

    }

}
