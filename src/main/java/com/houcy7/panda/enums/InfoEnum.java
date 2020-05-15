package com.houcy7.panda.enums;

/**
 * @EnumName InfoEnum
 * @Description TODO
 * @Author hou
 * @Date 2020/5/10 3:44 下午
 * @Version 1.0
 **/
public enum InfoEnum {
    START(1, "正在下载"),
    DOWNLOAD(2, "下载完成"),
    SEND_EMAIL(3, "已发送邮件"),
    ERROR(0, "异常");

    public final Integer type;
    public final String desc;

    InfoEnum(Integer type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public static InfoEnum getEnum(Integer type) {
        for (InfoEnum method : values()) {
            if (method.type.compareTo(type) == 0) {
                return method;
            }
        }
        return InfoEnum.ERROR;
    }
}
