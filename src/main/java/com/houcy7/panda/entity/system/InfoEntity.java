package com.houcy7.panda.entity.system;

import lombok.Data;

/**
 * @ClassName InfoEntity
 * @Description TODO
 * @Author hou
 * @Date 2020/5/10 3:08 下午
 * @Version 1.0
 **/
@Data
public class InfoEntity {

    // 微信相关信息
    private String ToUserName;
    private String FromUserName;
    private Long CreateTime;
    private String MsgType;
    private String MsgId;

    // 下载时间
    private Long downloadTime;

    // 论文名称
    private String pdfName;

    // 下载耗时
    private long costTime;

    private String Content;

    // 状态 0异常 1创建 2下载完成 3发送邮件
    private int status;
}