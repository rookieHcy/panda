package com.houcy7.panda.entity;

import lombok.Data;

/**
 * @ClassName CheckSignatureBean
 * @Description 接入的bean
 * @Author hou
 * @Date 2020/5/9 12:55 上午
 * @Version 1.0
 **/
@Data
public class CheckSignatureBean {

    // 微信加密签名，signature结合了开发者填写的token参数和请求中的timestamp参数、nonce参数。
    private String signature;

    //时间戳
    private String timestamp;

    // 随机数
    private String nonce;

    // 随机字符串
    private String echostr;
}