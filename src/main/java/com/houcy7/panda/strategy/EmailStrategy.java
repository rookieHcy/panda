package com.houcy7.panda.strategy;

import com.houcy7.panda.common.EmailContainer;
import com.houcy7.panda.entity.wechat.message.request.BaseMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * @ClassName EmailStrategy
 * @Description 待设置邮箱策略
 * @Author hou
 * @Date 2020/5/12 1:28 下午
 * @Version 1.0
 **/
@Component
@Slf4j
public class EmailStrategy extends AbsStrategy {

    private final Set<String> KEYWORDS = new HashSet<>();

    {
        KEYWORDS.add("email");
        KEYWORDS.add("EMAIL");
        KEYWORDS.add("设置邮箱");
    }

    @Override
    protected String realWork(String content, BaseMessage baseMessage) {
        return "您的邮箱信息不存在，请先设置一个吧。发送\"email/EMAIL/设置邮箱: + 邮箱地址\"进行设置，例如\"设置邮箱:abc@qq.com\"";
    }

    @Override
    protected boolean match(BaseMessage baseMessage, String content) {
        // 如果匹配到了设置邮箱策略
        for (String keyword : KEYWORDS) {
            if (content.startsWith(keyword)) {
                return false;
            }
        }

        if (!EmailContainer.contains(baseMessage.getFromUserName())) {
            log.info("msgId={}, content={}匹配到了【待设置邮箱策略】", baseMessage.getMsgId(), content);
            return true;
        }
        return false;
    }

}