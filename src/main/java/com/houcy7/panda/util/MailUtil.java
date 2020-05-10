package com.houcy7.panda.util;

import java.io.File;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Component
@Slf4j
public class MailUtil {

    @Value("${spring.mail.username}")
    private String username;


    @Autowired
    JavaMailSender jms;

    //发送文本消息，不带附件
    public String send() {
        //建立邮件消息
        SimpleMailMessage mainMessage = new SimpleMailMessage();
        //发送者
        mainMessage.setFrom(username);
        //接收者
        mainMessage.setTo(username);
        //发送的标题
        mainMessage.setSubject("测试标题");
        //发送的内容
        mainMessage.setText("测试内容");
        jms.send(mainMessage);
        return "true";

    }

    //发送html消息，不带附件
    public String sendHtml() throws MessagingException {
        MimeMessage message = jms.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        //发送者
        helper.setFrom(username);
        //接收者
        helper.setTo(username);
        //发送的标题
        helper.setSubject("测试标题");
        //发送的内容
//        mainMessage.setText("测试内容");
        StringBuilder content = new StringBuilder("<html><head></head><body><h2>title</h2>");
        content.append("<table border=\"5\" style=\"border:solid 1px #E8F2F9;font-size=14px;;font-size:18px;\">");
        content.append("<tr style=\"background-color: #428BCA; color:#ffffff\"><th>column1</th><th>column2</th><th>column3</th></tr>");
        for (int i = 0; i < 10; i++) {
            content.append("<tr>");
            content.append("<td>" + i + "第一列</td>"); //第一列
            content.append("<td>" + i + "第二列</td>"); //第一列
            content.append("<td>" + i + "第三列</td>"); //第一列
            content.append("</tr>");
        }
        content.append("</table>");
        content.append("<h3>description</h3>");
        content.append("</body></html>");
        helper.setText(content.toString(), true);
        jms.send(message);
        return "true";

    }

    // 发送含有附件的邮件
    public void sendEmail(String toEmail, String context, List<String> list) {
        log.info("发送邮件: toEmail={}, context={}, list={}", toEmail, context, JSONObject.toJSONString(list));
        MimeMessage message = jms.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(username);
            helper.setTo(toEmail);
            helper.setSubject("论文下载");
            helper.setText(context, true);
            //验证文件数据是否为空
            if (!list.isEmpty()) {
                FileSystemResource file = null;
                for (String path : list) {
                    //添加附件
                    file = new FileSystemResource(path);
                    helper.addAttachment(path, file);
                }
            }
            jms.send(message);
            log.info("带有附件的邮件发送成功");
        } catch (Exception e) {
            log.error("带有附件的邮件发送失败，{}", e.getMessage());
        }
    }

}