package com.houcy7.panda.strategy;

import com.houcy7.panda.common.InfoContainer;
import com.houcy7.panda.entity.wechat.message.request.BaseMessage;
import com.houcy7.panda.server.DownloadServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @ClassName DownloadStrategy
 * @Description TODO
 * @Author hou
 * @Date 2020/5/12 2:18 下午
 * @Version 1.0
 **/
@Component
@Slf4j
public class DownloadStrategy extends AbsStrategy {

    @Value("${download.url}")
    private String url;

    @Value("${download.savePath}")
    private String savePath;

    // 线程池
    private ExecutorService pool = Executors.newFixedThreadPool(10);

    @Override
    protected String realWork(String content, BaseMessage baseMessage) {
        //存放下载信息
        InfoContainer.put(baseMessage, content);

        // 因为微信消息等待回复有超时时间 在这里直接返回结果 开启异步下载
        pool.submit(new DownloadServer(content, baseMessage, url, savePath));
        return "关键词为【" + content + "】的论文开始下载！";
    }

    @Override
    protected boolean match(BaseMessage baseMessage, String content) {
        log.info("msgId={}, content={}没有匹配到任何策略，识别为【下载策略】", baseMessage.getMsgId(), content);
        return true;
    }
}