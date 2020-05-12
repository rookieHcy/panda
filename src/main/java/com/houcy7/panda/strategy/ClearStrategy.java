package com.houcy7.panda.strategy;

import com.houcy7.panda.common.InfoContainer;
import com.houcy7.panda.entity.wechat.message.request.BaseMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * @ClassName ClearStartegy
 * @Description TODO
 * @Author hou
 * @Date 2020/5/12 2:47 下午
 * @Version 1.0
 **/
@Component
@Slf4j
public class ClearStrategy extends AbsStrategy {

    private final Set<String> KEYWORDS = new HashSet<>();

    {
        KEYWORDS.add("clear");
        KEYWORDS.add("CLEAR");
        KEYWORDS.add("清空");
    }

    @Override
    protected String realWork(String content, BaseMessage baseMessage) {
        InfoContainer.delete(baseMessage.getFromUserName());
        return "清空公众号内下载信息成功";
    }

    @Override
    protected boolean match(BaseMessage baseMessage, String content) {
        if (KEYWORDS.contains(content)) {
            log.info("msgId={}, content={}匹配到了【清空策略】", baseMessage.getMsgId(), content);
            return true;
        }
        return false;
    }
}