package com.houcy7.panda.handler;

import com.houcy7.panda.entity.wechat.message.request.BaseMessage;

import java.util.Map;

/**
 * @ClassName BaseHandler
 * @Description TODO
 * @Author hou
 * @Date 2020/5/9 3:19 下午
 * @Version 1.0
 **/
public interface BaseHandler {

    String handler(Map<String, String> params, BaseMessage baseMessage);

}