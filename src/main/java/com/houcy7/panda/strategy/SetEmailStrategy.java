package com.houcy7.panda.strategy;

import com.houcy7.panda.common.EmailContainer;
import com.houcy7.panda.entity.wechat.message.request.BaseMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * @ClassName SetEmailStrategy
 * @Description 设置邮箱策略
 * @Author hou
 * @Date 2020/5/12 1:34 下午
 * @Version 1.0
 **/
@Component
@Slf4j
public class SetEmailStrategy extends AbsStrategy {

    private final Set<String> KEYWORDS = new HashSet<>();

    {
        KEYWORDS.add("email");
        KEYWORDS.add("EMAIL");
        KEYWORDS.add("设置邮箱");
    }

    @Override
    protected String realWork(String content, BaseMessage baseMessage) {
        try {
            // 处理中文冒号
            content = content.replaceAll("：", ":");
            String[] split = content.split(":");
            EmailContainer.put(baseMessage.getFromUserName(), split[1]);
            log.info("{}设置邮箱{}成功", baseMessage.getFromUserName(), split[1]);
            return "设置邮箱[" + split[1] + "]成功，发送doi或论文链接开始下载";
        } catch (Exception e) {
            log.info("{}设置邮箱失败", content);
            return "设置邮箱失败";
        }
    }

    @Override
    protected boolean match(BaseMessage baseMessage, String content) {
        for (String keyword : KEYWORDS) {
            if (content.startsWith(keyword)) {
                log.info("msgId={}, content={}匹配到了【设置邮箱策略】", baseMessage.getMsgId(), content);
                return true;
            }
        }
        return false;
    }
}