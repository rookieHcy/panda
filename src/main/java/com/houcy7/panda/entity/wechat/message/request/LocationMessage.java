package com.houcy7.panda.entity.wechat.message.request;

import lombok.Data;

/**
 * @ClassName LocationMessage
 * @Description TODO
 * @Author hou
 * @Date 2020/5/9 11:28 上午
 * @Version 1.0
 **/
@Data
public class LocationMessage extends BaseMessage {
    // 地理位置维度
    private String Location_X;
    // 地理位置经度
    private String Location_Y;
    // 地图缩放大小
    private String Scale;
    // 地理位置信息
    private String Label;
}