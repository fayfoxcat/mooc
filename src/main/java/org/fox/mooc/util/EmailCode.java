package org.fox.mooc.util;

import javax.servlet.http.HttpServletRequest;
import java.util.Random;

/**
 * @Author by fairyfox
 * *2020/2/17-10:29
 */
public class EmailCode {
    private static int RandomCode;

    //发送邮件
    public void SendCode(String email) {
        //初始化
        EmailCode.InitEmail();

        //获取随机数对象
        Random r = new Random();
        Boolean status = true;
        //获取6位随机验证码
        RandomCode = r.nextInt(899999) + 100000;
        //邮件标题
        String subject = "你的创课网验证码";
        //邮件内容
        String content = "确认你的邮件地址" + RandomCode + "是您的验证码请及时填写，验证码两小时后过期。";
        //发送邮件
        SendMailUtils.simpleMailSend(email, subject, content);
    }

    //初始化使用邮箱
    public static void InitEmail() {
        //发送端口
        String host = "smtp.qq.com";
        //邮箱地址
        String username = "2369150513@qq.com";
        //邮箱密码
        String password = "twrqdsnhkqgdeadb";
        //初始化
        SendMailUtils.setInitData(host, username, password);
    }

    //校验邮箱
    public static Boolean CheckEmail(HttpServletRequest request){
        int code = HttpServletRequestUtil.getInteger(request, "verifyCodeActual");
        return RandomCode == code;
    }

    //获取随机验证码
    public static Integer getRandomCode(){
        return RandomCode;
    }
}
