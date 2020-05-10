package com.houcy7.panda.service.wechat.impl;

import com.alibaba.fastjson.JSONObject;
import com.houcy7.panda.common.DownloadStatusContainer;
import com.houcy7.panda.entity.wechat.message.request.BaseMessage;
import com.houcy7.panda.entity.wechat.message.response.TextMessage;
import com.houcy7.panda.enums.WeChatEnum;
import com.houcy7.panda.handler.impl.TextMessageHandler;
import com.houcy7.panda.service.wechat.WeChatService;
import com.houcy7.panda.util.MessageUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;

/**
 * @ClassName WeChatServiceImpl
 * @Description TODO
 * @Author hou
 * @Date 2020/5/9 1:13 下午
 * @Version 1.0
 **/
@Service
@Slf4j
public class WeChatServiceImpl implements WeChatService {

    private final static String DEFAULT_SUCCESS = "success";

    private final static String KEY_MESSAGE_TYPE = "MsgType";
    private final static String KEY_TO_USER_NAME = "ToUserName";
    private final static String KEY_FROM_USER_NAME = "FromUserName";
    private final static String KEY_CREATE_TIME = "CreateTime";
    private final static String KEY_MSG_ID = "MsgId";

    @Autowired
    private TextMessageHandler textMessageHandler;

    @Override
    public String doWork(HttpServletRequest request) {
        String respXml;
        try {
            Map<String, String> params = MessageUtil.parseXml(request);
            String messageType = params.get(KEY_MESSAGE_TYPE);
            String toUserName = params.get(KEY_TO_USER_NAME);
            String fromUserName = params.get(KEY_FROM_USER_NAME);
            String createTime = params.get(KEY_CREATE_TIME);
            String msgId = params.get(KEY_MSG_ID);
            log.info("接收到微信消息：messageType={}, toUserName={}, fromUserName={}, createTime={}, msgId={}", messageType, toUserName, fromUserName, createTime, msgId);
            if (StringUtils.isEmpty(messageType)) throw new RuntimeException("请求参数中未找到消息类型");
            if (StringUtils.isEmpty(toUserName)) throw new RuntimeException("请求参数中未找到接收人");
            if (StringUtils.isEmpty(fromUserName)) throw new RuntimeException("请求参数中未找到发送人");

            // 构建基础信息
            BaseMessage baseMessage = new BaseMessage();
            baseMessage.setFromUserName(fromUserName);
            baseMessage.setToUserName(toUserName);
            baseMessage.setMsgId(msgId);
            baseMessage.setMsgType(messageType);
            baseMessage.setCreateTime(Long.parseLong(createTime));

            // 设置回复的内容
            TextMessage textMessage = new TextMessage();
            textMessage.setCreateTime(new Date().getTime());
            textMessage.setFromUserName(toUserName);
            textMessage.setToUserName(fromUserName);
            textMessage.setMsgType(WeChatEnum.RespMessageEnum.TEXT.type);

            // 校验现在是否正在进行下载
            if (DownloadStatusContainer.get(fromUserName)) {
                textMessage.setContent("正在发送邮件，本次信息未处理！");
                respXml = MessageUtil.messageToXml(textMessage);
                log.info("{} 响应报文：{}", msgId, respXml);
                return respXml;
            }

            WeChatEnum.MessageEnum messageEnum = WeChatEnum.MessageEnum.getEnum(messageType);
            switch (messageEnum) {
                case TEXT:
                    textMessage.setContent(textMessageHandler.handler(params, baseMessage));
                    break;
                case VOICE:
                case IMAGE:
                case LOCATION:
                case SHORTVIDEO:
                case EVENT:
                case LINK:
                case VIDEO:
                    textMessage.setContent("接收到：" + messageEnum.desc + "的消息，预期为文本类型的消息");
                    break;
            }

            respXml = MessageUtil.messageToXml(textMessage);
            log.info("{} 响应报文：{}", msgId, respXml);
            return respXml;
        } catch (Exception e) {
            log.error("服务异常：{}", e.getMessage());
            return DEFAULT_SUCCESS;
        }

    }

}