package com.houcy7.panda.strategy;

import com.houcy7.panda.entity.wechat.message.request.BaseMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * @ClassName HelpStrategy
 * @Description 处理消息为帮助的逻辑
 * @Author hou
 * @Date 2020/5/12 1:07 下午
 * @Version 1.0
 **/
@Component
@Slf4j
public class HelpStrategy extends AbsStrategy {

    private final Set<String> KEYWORDS = new HashSet<>();

    {
        KEYWORDS.add("help");
        KEYWORDS.add("HELP");
        KEYWORDS.add("帮助");
    }

    @Override
    protected String realWork(String content, BaseMessage baseMessage) {
        return "帮助指南：\n" +
                "1.help/HELP/帮助 \t 获得公众号中支持的指令。\n" +
                "2.email/EMAIL/设置邮箱 \t 设置接收论文邮箱。\n" +
                "3.status/STATUS/状态 \t 查看当前论文下载情况。\n" +
                "4.ok//OK/发送邮件 \t 发送当前已经下载好的论文。\n" +
                "5.cancel/CANCEL/取消 \t 取消正在下载的邮件。\n" +
                "6.clear/CLEAR/清空 \t 清空公众号内下载信息。\n" +
                "7.非以上标识的信息视为下载论文关键字 ";
    }

    @Override
    protected boolean match(BaseMessage baseMessage, String content) {
        if (KEYWORDS.contains(content)) {
            log.info("msgId={}, content={}匹配到了【帮助策略】", baseMessage.getMsgId(), content);
            return true;
        }
        return false;
    }
}