package com.houcy7.panda;

import com.houcy7.panda.util.MailUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.mail.MessagingException;

@SpringBootTest
class PandaApplicationTests {

    @Autowired
    private MailUtil mailUtil;

    @Test
    void send() {
        mailUtil.send();
    }

    @Test
    void sendHtml() throws MessagingException {
        mailUtil.sendHtml();
    }

}
