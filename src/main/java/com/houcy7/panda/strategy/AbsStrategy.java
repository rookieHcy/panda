package com.houcy7.panda.strategy;

import com.houcy7.panda.common.DownloadStatusContainer;
import com.houcy7.panda.entity.wechat.message.request.BaseMessage;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName AbsStrategy
 * @Description TODO
 * @Author hou
 * @Date 2020/5/12 1:05 下午
 * @Version 1.0
 **/

public abstract class AbsStrategy{

    // 真正执行任务
    protected abstract String realWork(String content, BaseMessage baseMessage);

    // 判断消息是否匹配
    protected abstract boolean match(BaseMessage baseMessage, String content);

}