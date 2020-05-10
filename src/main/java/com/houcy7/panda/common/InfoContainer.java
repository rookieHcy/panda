package com.houcy7.panda.common;

import com.houcy7.panda.entity.system.InfoEntity;
import com.houcy7.panda.entity.wechat.message.request.BaseMessage;
import com.houcy7.panda.enums.InfoEnum;
import org.springframework.beans.BeanUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ClassName InfoContainer
 * @Description TODO
 * @Author hou
 * @Date 2020/5/10 3:06 下午
 * @Version 1.0
 **/
public class InfoContainer {

    private static Map<String, Map<String, InfoEntity>> infoContainer = new ConcurrentHashMap<>();

    private InfoContainer(){};

    public static void put(String fromUserName, InfoEntity infoEntity){
        if(!infoContainer.containsKey(fromUserName)){
            infoContainer.put(fromUserName, new ConcurrentHashMap<>());
        }

        infoContainer.get(fromUserName).put(infoEntity.getMsgId(), infoEntity);
    }

    public static InfoEntity get(String fromUserName, String MsgId){
        return infoContainer.get(fromUserName).get(MsgId);
    }


    public static void put(BaseMessage baseMessage, String content) {
        InfoEntity infoEntity = new InfoEntity();
        BeanUtils.copyProperties(baseMessage, infoEntity);
        infoEntity.setStatus(InfoEnum.START.type);
        infoEntity.setContent(content);
        put(baseMessage.getFromUserName(), infoEntity);
    }

    // 设置下载失败
    public static void setDownloadFail(String fromUserName, String msgId) {
        InfoEntity infoEntity = get(fromUserName, msgId);
        infoEntity.setStatus(InfoEnum.ERROR.type);
        put(fromUserName, infoEntity);
    }

    // 设置下载成功
    public static void setDownloadSuccess(String fromUserName, String msgId, String pdfName, long cost) {
        InfoEntity infoEntity = get(fromUserName, msgId);
        infoEntity.setStatus(InfoEnum.DOWNLOAD.type);
        infoEntity.setCostTime(cost);
        infoEntity.setPdfName(pdfName);
        put(fromUserName, infoEntity);
    }

    public static Map<String, InfoEntity> get(String fromUserName) {
        return infoContainer.get(fromUserName);
    }
}