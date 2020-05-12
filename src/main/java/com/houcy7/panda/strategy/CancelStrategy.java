package com.houcy7.panda.strategy;

import com.houcy7.panda.common.InfoContainer;
import com.houcy7.panda.entity.wechat.message.request.BaseMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * @ClassName CancelStrategy
 * @Description TODO
 * @Author hou
 * @Date 2020/5/12 2:30 下午
 * @Version 1.0
 **/
@Component
@Slf4j
public class CancelStrategy extends AbsStrategy {

    private final Set<String> KEYWORDS = new HashSet<>();

    {
        KEYWORDS.add("cancel");
        KEYWORDS.add("CANCEL");
        KEYWORDS.add("取消");
    }

    @Override
    protected String realWork(String content, BaseMessage baseMessage) {
        // 处理中文冒号
        content = content.replaceAll("：", ":");
        String[] split = content.split(":", 2);
        boolean result = InfoContainer.cancel(baseMessage.getFromUserName(), split[1]);
        return result ? "取消【" + content + "】下载成功" : "取消【" + content + "】下载失败";
    }

    @Override
    protected boolean match(BaseMessage baseMessage, String content) {
        for (String keyword : KEYWORDS) {
            if (content.startsWith(keyword)) {
                log.info("msgId={}, content={}匹配到了【取消策略】", baseMessage.getMsgId(), content);
                return true;
            }
        }
        return false;
    }
}