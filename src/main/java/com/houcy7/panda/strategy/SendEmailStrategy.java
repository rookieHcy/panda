package com.houcy7.panda.strategy;

import com.houcy7.panda.common.InfoContainer;
import com.houcy7.panda.entity.system.InfoEntity;
import com.houcy7.panda.entity.wechat.message.request.BaseMessage;
import com.houcy7.panda.enums.InfoEnum;
import com.houcy7.panda.server.EmailServer;
import com.houcy7.panda.util.MailUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * @ClassName SendEmailStrategy
 * @Description 发送邮件策略
 * @Author hou
 * @Date 2020/5/12 1:56 下午
 * @Version 1.0
 **/
@Component
@Slf4j
public class SendEmailStrategy extends AbsStrategy {

    private final Set<String> KEYWORDS = new HashSet<>();

    {
        KEYWORDS.add("ok");
        KEYWORDS.add("OK");
        KEYWORDS.add("/OK");
        KEYWORDS.add("发送邮件");
    }

    // 线程池
    private ExecutorService pool = Executors.newFixedThreadPool(5);

    @Value("${download.savePath}")
    private String savePath;

    @Autowired
    private MailUtil mailUtil;

    @Override
    protected String realWork(String content, BaseMessage baseMessage) {

        // 获取论文的下载记录
        Map<String, InfoEntity> map = InfoContainer.get(baseMessage.getFromUserName());
        if (null == map || map.isEmpty()) {
            return "没有找到待发送邮件的论文信息！请先发送关键词进行下载";
        }

        // 按照状态进行分组
        Map<Integer, List<InfoEntity>> groupMap = map.values().stream().collect(Collectors.groupingBy(InfoEntity::getStatus));
        // 获取未下载的数据个数
        List<InfoEntity> infoEntities = groupMap.get(InfoEnum.START.type);
        // 获取到待下载的论文
        List<InfoEntity> toSendList = groupMap.get(InfoEnum.DOWNLOAD.type);
        if (null == toSendList || toSendList.isEmpty()) {
            return "没有找到待发送邮件的论文信息！请先发送关键词进行下载";
        }


        String result = "接收到发送邮件请求，请稍后查看邮箱；";
        if (null != infoEntities && !infoEntities.isEmpty()) {
            result += "未完成下载的论文数量：" + infoEntities.size() + "。";
        }
        // 因为微信消息等待回复有超时时间 在这里直接返回结果 开启异步发送邮件
        pool.submit(new EmailServer(mailUtil, baseMessage, toSendList, savePath));
        return result;
    }

    @Override
    protected boolean match(BaseMessage baseMessage, String content) {
        if (KEYWORDS.contains(content)) {
            log.info("msgId={}, content={}匹配到了【发送邮件策略】", baseMessage.getMsgId(), content);
            return true;
        }
        return false;
    }
}