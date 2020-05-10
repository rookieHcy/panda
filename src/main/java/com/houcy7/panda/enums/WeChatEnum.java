package com.houcy7.panda.enums;

/**
 * @ClassName WeChatEnum
 * @Description TODO
 * @Author hou
 * @Date 2020/5/9 12:41 下午
 * @Version 1.0
 **/
public class WeChatEnum {

    /**
     * 消息类型枚举
     */
    public enum MessageEnum {
        TEXT("text", "文本消息类型"),
        IMAGE("image", "图片消息类型"),
        VOICE("voice", "语音消息类型"),
        VIDEO("video", "视频消息类型"),
        SHORTVIDEO("shortvideo", "小视频消息类型"),
        LOCATION("location", "定位消息类型"),
        LINK("link", "链接消息类型"),
        EVENT("event", "事件推送消息类型");

        public final String type;
        public final String desc;

        MessageEnum(String type, String desc) {
            this.type = type;
            this.desc = desc;
        }

        public static MessageEnum getEnum(String type) {
            for (MessageEnum method : values()) {
                if (method.type.equalsIgnoreCase(type)) {
                    return method;
                }
            }
            return MessageEnum.TEXT;
        }
    }


    /**
     * 推送事件类型枚举
     */
    public enum EventEnum {
        SUBSCRIBE("subscribe", "订阅事件"),
        UNSUBSCRIBE("unsubscribe", "取消订阅事件"),
        SCAN("scan", "扫描二维码"),
        LOCATION("location", "定位"),
        CLICK("click", "点击事件");

        public final String type;
        public final String desc;

        EventEnum(String type, String desc) {
            this.type = type;
            this.desc = desc;
        }

        public static EventEnum getEnum(String type) {
            for (EventEnum eventEnum : values()) {
                if (eventEnum.type.equalsIgnoreCase(type)) {
                    return eventEnum;
                }
            }
            throw new RuntimeException("未找到相应事件类型枚举");
        }
    }


    /**
     * 响应消息类型枚举
     */
    public enum RespMessageEnum {
        TEXT("text", "文本消息类型"),
        IMAGE("image", "图片消息类型"),
        VOICE("voice", "语音消息类型"),
        VIDEO("video", "视频消息类型"),
        MUSIC("music", "音乐消息类型"),
        NEWS("news", "图文消息类型");

        public final String type;
        public final String desc;

        RespMessageEnum(String type, String desc) {
            this.type = type;
            this.desc = desc;
        }

        public static RespMessageEnum getEnum(String type) {
            for (RespMessageEnum method : values()) {
                if (method.type.equalsIgnoreCase(type)) {
                    return method;
                }
            }
            return RespMessageEnum.TEXT;
        }
    }

}