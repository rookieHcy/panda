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
import com.houcy7.panda.strategy.AbsStrategy;
import com.houcy7.panda.strategy.StrategyFactory;
import com.houcy7.panda.util.MailUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
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

    @Autowired
    private StrategyFactory strategyFactory;


    @Override
    public String handler(Map<String, String> params, BaseMessage baseMessage) {

        // 1.处理请求参数
        String content = params.get(KEY_TEXT_CONTENT).replace("\\s", "");
        if (StringUtils.isEmpty(content)) {
            log.error("Handler 文本请求信息时 参数中未找到Content");
            throw new RuntimeException("Handler 文本请求信息时 参数中未找到Content");
        }

        return strategyFactory.doWork(baseMessage, content);

    }
}