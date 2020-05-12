package com.houcy7.panda.strategy;

import com.houcy7.panda.entity.wechat.message.request.BaseMessage;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName StrategyFactory
 * @Description TODO
 * @Author hou
 * @Date 2020/5/12 2:58 下午
 * @Version 1.0
 **/
@Component
public class StrategyFactory implements InitializingBean {
    @Autowired
    private HelpStrategy helpStrategy;

    @Autowired
    private EmailStrategy emailStrategy;

    @Autowired
    private SetEmailStrategy setEmailStrategy;

    @Autowired
    private SendEmailStrategy sendEmailStrategy;

    @Autowired
    private DownloadStrategy downloadStrategy;

    @Autowired
    private ClearStrategy clearStrategy;

    @Autowired
    private StatusStrategy statusStrategy;

    @Autowired
    private CancelStrategy cancelStrategy;

    private List<AbsStrategy> strategies = new ArrayList<>();

    public String doWork(BaseMessage baseMessage, String content) {
        for (AbsStrategy strategy : strategies) {
            if (strategy.match(baseMessage, content)) {
                return strategy.realWork(content, baseMessage);
            }
        }
        return "未匹配到执行规则，请联系管理员";
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        strategies.add(helpStrategy); // 帮助
        strategies.add(emailStrategy); // 待设置邮箱
        strategies.add(setEmailStrategy); // 设置邮箱
        strategies.add(statusStrategy); // 状态
        strategies.add(sendEmailStrategy); // 发送邮件
        strategies.add(clearStrategy); // 清空
        strategies.add(cancelStrategy); // 清空
        strategies.add(downloadStrategy); // 以上策略没有匹配到 会执行此策略 一定是最后一个
    }
}