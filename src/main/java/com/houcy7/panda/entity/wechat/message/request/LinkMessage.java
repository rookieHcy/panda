package com.houcy7.panda.entity.wechat.message.request;

import lombok.Data;

/**
 * @ClassName LinkMessage
 * @Description TODO
 * @Author hou
 * @Date 2020/5/9 11:29 上午
 * @Version 1.0
 **/
@Data
public class LinkMessage extends BaseMessage {
    // 消息标题
    private String Title;
    // 消息描述
    private String Description;
    // 消息链接
    private String Url;
}