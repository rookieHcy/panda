package com.houcy7.panda.controller;

import com.alibaba.fastjson.JSONObject;
import com.houcy7.panda.entity.wechat.check.CheckSignatureBean;
import com.houcy7.panda.service.wechat.WeChatService;
import com.houcy7.panda.util.WeChatUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @ClassName WeChatController
 * @Description 公众号接口 接收消息、回复消息
 * @Author hou
 * @Date 2020/5/8 10:00 下午
 * @Version 1.0
 **/
@RestController
@RequestMapping("wx")
@Slf4j
public class WeChatController {

    @Value("${wechat.token}")
    private String token;

    @Autowired
    private WeChatService weChatService;

    @GetMapping("test")
    public String test() {
        return "test";
    }

    @GetMapping("/")
    public String checkSignature(CheckSignatureBean bean) {
        String s = WeChatUtil.checkSignature(bean, token);
        log.info("公众号接入返回结果：{}", s);
        return s;
    }

    @PostMapping("/")
    public String req(HttpServletRequest request) {
        return weChatService.doWork(request);
    }

}