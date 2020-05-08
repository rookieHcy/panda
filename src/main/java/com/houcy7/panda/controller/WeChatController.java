package com.houcy7.panda.controller;

import com.houcy7.panda.entity.CheckSignatureBean;
import com.houcy7.panda.util.WeChatUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName WeChatController
 * @Description 公众号接口 接收消息、回复消息
 * @Author hou
 * @Date 2020/5/8 10:00 下午
 * @Version 1.0
 **/
@RestController("wx")
public class WeChatController {

    @Value("${wechat.token}")
    private String token;

    @GetMapping("")
    public String checkSignature(CheckSignatureBean bean){
        return WeChatUtil.checkSignature(bean, token);
    }
}