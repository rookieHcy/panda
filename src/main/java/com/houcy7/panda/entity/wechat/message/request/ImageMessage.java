package com.houcy7.panda.entity.wechat.message.request;

import lombok.Data;

/**
 * @ClassName ImageMessage
 * @Description TODO
 * @Author hou
 * @Date 2020/5/9 11:26 上午
 * @Version 1.0
 **/
@Data
public class ImageMessage extends BaseMessage {
    // 图片链接
    private String PicUrl;
    private String MediaId;
}