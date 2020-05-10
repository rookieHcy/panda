package com.houcy7.panda.entity.wechat.message.request;

import lombok.Data;

/**
 * @ClassName VoiceMessage
 * @Description TODO
 * @Author hou
 * @Date 2020/5/9 11:27 上午
 * @Version 1.0
 **/
@Data
public class VoiceMessage extends BaseMessage {
    // 媒体ID
    private String MediaId;
    // 语音格式
    private String Format;
}