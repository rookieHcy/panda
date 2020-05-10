package com.houcy7.panda.entity.wechat.message.request;

import lombok.Data;

/**
 * @ClassName BaseMessage
 * @Description TODO
 * @Author hou
 * @Date 2020/5/9 10:40 上午
 * @Version 1.0
 **/
@Data
public class BaseMessage {
    private String ToUserName;
    private String FromUserName;
    private Long CreateTime;
    private String MsgType;
    private String MsgId;

}