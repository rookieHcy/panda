package com.houcy7.panda.handler.impl;

import com.houcy7.panda.common.DownloadStatusContainer;
import com.houcy7.panda.common.EmailContainer;
import com.houcy7.panda.common.InfoContainer;
import com.houcy7.panda.entity.system.InfoEntity;
import com.houcy7.panda.entity.wechat.message.request.BaseMessage;
import com.houcy7.panda.enums.InfoEnum;
import com.houcy7.panda.handler.BaseHandler;
import com.houcy7.panda.server.DownloadServer;
import com.houcy7.panda.server.EmailServer;
import com.houcy7.panda.util.MailUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * @ClassName TextMessageHandler
 * @Description TODO
 * @Author hou
 * @Date 2020/5/9 3:26 下午
 * @Version 1.0
 **/
@Service
@Slf4j
public class TextMessageHandler implements BaseHandler {

    private final static String KEY_TEXT_CONTENT = "Content";

    @Value("${wechat.sendEmailFlag}")
    private String sendEmailFlag;

    @Value("${wechat.flushAllFlag}")
    private String flushAllFlag;

    @Value("${wechat.setEmailFlag}")
    private String setEmailFlag;

    @Value("${download.url}")
    private String url;

    @Value("${download.savePath}")
    private String savePath;

    @Autowired
    private MailUtil mailUtil;

    // 线程池
    private ExecutorService pool = Executors.newFixedThreadPool(10);

    @Override
    public String handler(Map<String, String> params, BaseMessage baseMessage) {

        // 1.处理请求参数
        String content = params.get(KEY_TEXT_CONTENT).replace("\\s", "");
        if (StringUtils.isEmpty(content)) {
            log.error("Handler 文本请求信息时 参数中未找到Content");
            throw new RuntimeException("Handler 文本请求信息时 参数中未找到Content");
        }

        String res;
        if (content.startsWith(setEmailFlag)) {
            try {
                String[] split = content.split(":");
                EmailContainer.put(baseMessage.getFromUserName(), split[1]);
                log.info("{}设置邮箱{}成功", baseMessage.getFromUserName(), split[1]);
                res = "设置邮箱[" + split[1] + "]成功，发送doi或论文链接开始下载";
            } catch (Exception e) {
                log.info("{}设置邮箱失败", content);
                res = "设置邮箱失败";
            }
            return res;
        }
        // 检查是否存在邮箱缓存
        else if (!EmailContainer.contains(baseMessage.getFromUserName())) {
            return "您的邮箱信息不存在，请先设置一个吧。发送\"设置邮箱: + 邮箱地址\"进行设置，例如\"设置邮箱:abc@qq.com\"";
        }

        // 发送邮件
        if (sendEmailFlag.equalsIgnoreCase(content)) {
            // 获取论文的下载记录
            Map<String, InfoEntity> stringInfoEntityMap = InfoContainer.get(baseMessage.getFromUserName());
            // 按照状态进行分组
            Map<Integer, List<InfoEntity>> collect = stringInfoEntityMap.values().stream().collect(Collectors.groupingBy(InfoEntity::getStatus));
            // 获取未下载的数据个数
            List<InfoEntity> infoEntities = collect.get(InfoEnum.START.type);
            if (null != infoEntities && !infoEntities.isEmpty()) {
                res = "当前含有未完成下载的任务，请稍后重试";
                return res;
            } else {
                res = "接收到发送邮件请求，请稍后查看邮箱";
            }
            // 因为微信消息等待回复有超时时间 在这里直接返回结果 开启异步下载
            pool.submit(new EmailServer(mailUtil, baseMessage, stringInfoEntityMap.values(), savePath));

//        } else if (flushAllFlag.equalsIgnoreCase(content)) {
//            res = emailService.doClear(baseMessage);
        } else {
            //存放下载信息
            InfoContainer.put(baseMessage, content);
            res = "关键词为【" + content + "】的论文开始下载！";

            // 因为微信消息等待回复有超时时间 在这里直接返回结果 开启异步下载
            pool.submit(new DownloadServer(content, baseMessage, url, savePath));
        }

        // 3.响应
        return res;
    }
}