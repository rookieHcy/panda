package com.houcy7.panda.service.wechat;

import javax.servlet.http.HttpServletRequest;

/**
 * @InterfaceName WeChatService
 * @Description TODO
 * @Author hou
 * @Date 2020/5/9 1:12 下午
 * @Version 1.0
 **/
public interface WeChatService {
    String doWork(HttpServletRequest request);
}
