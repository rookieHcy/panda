package com.houcy7.panda.server;

import com.houcy7.panda.common.InfoContainer;
import com.houcy7.panda.entity.wechat.message.request.BaseMessage;
import com.houcy7.panda.entity.wechat.message.response.TextMessage;
import com.houcy7.panda.enums.WeChatEnum;
import com.houcy7.panda.util.DoiUtil;
import com.houcy7.panda.util.HttpClient;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ClassName DownloadServer
 * @Description 下载论文
 * @Author hou
 * @Date 2020/5/8 9:59 下午
 * @Version 1.0
 **/
@Slf4j
@Data
public class DownloadServer implements Runnable{

    private String content;
    private BaseMessage baseMessage;
    private String url;
    private String savePath;

    public DownloadServer(String content, BaseMessage baseMessage, String url, String savePath) {
        this.content = content;
        this.baseMessage = baseMessage;
        this.url = url;
        this.savePath = savePath;
    }



    @Override
    public void run() {
        long start = System.currentTimeMillis();
        if(StringUtils.isEmpty(content)){
            log.error("下载关键词为空");
            throw new RuntimeException("下载关键词为空");
        }

        if(content.contains("\\s")){
            log.info("关键词中含有空格，先获取doi");
            content = DoiUtil.getDoi(content);
        }

        String realUrl = url + content;
        String pdfName = "";
        try {
            //先获得的是整个页面的html标签页面

            Document doc = Jsoup.connect(realUrl).get();
            String attr = doc.select("a").first().attr("onclick");
            String[] split = attr.split("'");
            String downloadUrl = "https:" + split[1];
            log.info("msgId={}发起下载请求，请求参数： downloadUrl={}, savePath={}", baseMessage.getMsgId(), downloadUrl, savePath);
            pdfName = HttpClient.httpsGet(downloadUrl, savePath);
            log.info("msgId={}下载完成，下载论文名称{}", baseMessage.getMsgId(), pdfName);

        } catch (IOException e) {
            log.error("下载论文异常:{}", e.getMessage());
        } finally {
            // 缓存中保存下载信息和结果
            String result;
            if(StringUtils.isEmpty(pdfName)){
                result = "关键词为【" +  content + "】的论文下载失败！";
                InfoContainer.setDownloadFail(baseMessage.getFromUserName(), baseMessage.getMsgId());
                log.error(result);
            } else {
                long cost = System.currentTimeMillis() - start;
                result = "关键词为【" +  content + "】的论文下载完成，论文命名为【" + pdfName + "】！耗时" + cost + "ms";
                InfoContainer.setDownloadSuccess(baseMessage.getFromUserName(), baseMessage.getMsgId(), pdfName, cost);
                log.info(result);
            }

        }

    }
}