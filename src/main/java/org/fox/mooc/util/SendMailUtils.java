package org.fox.mooc.util;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.internet.*;
import java.util.ArrayList;
import java.util.Properties;

/**
 * @Author by fairyfox
 * *2020/2/19-11:41
 */
public class SendMailUtils {
    // Spring的邮件工具类，实现了MailSender和JavaMailSender接口
    private static JavaMailSenderImpl mailSender;
    /**
     * 初始化邮件发送数据
     * @param host 服务器
     * @param username 发送人
     * @param passwd 发送人密码
     */
    public static void setInitData(String host,String username,String passwd){
        //创建邮件发送服务器
        mailSender = new JavaMailSenderImpl();
        mailSender.setHost(host);
        mailSender.setUsername(username);
        mailSender.setPassword(passwd);
        //加认证机制
        Properties javaMailProperties = new Properties();
        javaMailProperties.put("mail.smtp.auth", true);
        javaMailProperties.put("mail.smtp.starttls.enable", true);
        javaMailProperties.put("mail.smtp.timeout", 5000);
        mailSender.setJavaMailProperties(javaMailProperties);
        System.out.println("初始化邮件发送信息完成");
    }
    /**
     * 发送普通文本
     * @param email 对方邮箱地址
     * @param subject 主题
     * @param text 邮件内容
     */
    public static void simpleMailSend(String email,String subject,String text) {
        //创建邮件内容
        SimpleMailMessage message=new SimpleMailMessage();
        message.setFrom(mailSender.getUsername());
        message.setTo(email);
        message.setSubject(subject);
        message.setText(text);
        //发送邮件
        mailSender.send(message);
        System.out.println("发送成功");
    }
}
