package com.houcy7.panda.server;

import com.alibaba.fastjson.JSONObject;
import com.houcy7.panda.common.DownloadStatusContainer;
import com.houcy7.panda.common.EmailContainer;
import com.houcy7.panda.common.InfoContainer;
import com.houcy7.panda.entity.system.InfoEntity;
import com.houcy7.panda.entity.wechat.message.request.BaseMessage;
import com.houcy7.panda.enums.InfoEnum;
import com.houcy7.panda.util.MailUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @ClassName EmailServer
 * @Description 发送邮件
 * @Author hou
 * @Date 2020/5/8 9:59 下午
 * @Version 1.0
 **/
@Slf4j
public class EmailServer implements Runnable {

    private BaseMessage baseMessage;
    private MailUtil mailUtil;
    private List<InfoEntity> list;
    private String savePath;


    public EmailServer(MailUtil mailUtil, BaseMessage baseMessage, List<InfoEntity> values, String savePath) {
        this.baseMessage = baseMessage;
        this.mailUtil = mailUtil;
        this.list = values;
        this.savePath = savePath;
    }

    @Override
    public void run() {
        // 设置下载
        DownloadStatusContainer.add(baseMessage.getFromUserName());
        String toEmail = EmailContainer.get(baseMessage.getFromUserName());
        List<String> filePath = new ArrayList<>();
        // 获取邮件的内容和附件地址
        String context = getDownloadInfo(filePath, list, savePath);
        log.info("===发送邮件开始 fromUsername={} ===", baseMessage.getFromUserName());
        mailUtil.sendEmail(toEmail, context, filePath);
        log.info("===发送邮件结束 fromUsername={} ===", baseMessage.getFromUserName());
        // 标记本次下载的文章标识为发送邮件
        InfoContainer.delete(baseMessage.getFromUserName(), list);
        DownloadStatusContainer.remove(baseMessage.getFromUserName());
    }

    private String getDownloadInfo(List<String> filePath, List<InfoEntity> list, String savePath) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        StringBuffer content = new StringBuffer("<html><head></head><body><h2>您好，</h2><h4>本次下载论文信息如下：</h4>");
        content.append("<table border=\"5\" style=\"border:solid 1px #E8F2F9;font-size=14px;;font-size:18px;\">");
        content.append("<tr style=\"background-color: #428BCA; color:#ffffff\"><th>关键词</th><th>创建时间</th><th>文件名</th><th>下载耗时（ms）</th></tr>");
        for (InfoEntity infoEntity : list) {
            content.append("<tr>");
            content.append("<td>").append(infoEntity.getContent()).append("</td>"); //第一列
            // 微信传过来的时间戳为s
            content.append("<td>").append(simpleDateFormat.format(new Date(infoEntity.getCreateTime() * 1000))).append("</td>"); //第一列
            // 如果下载文件为空
            if (StringUtils.isEmpty(infoEntity.getPdfName())) {
                content.append("<td>").append("未找到相关论文").append("</td>"); //第一列
                content.append("<td>").append("--").append("</td>"); //第一列
            } else {
                content.append("<td>").append(infoEntity.getPdfName()).append("</td>"); //第一列
                content.append("<td>").append(infoEntity.getCostTime()).append("</td>"); //第一列
                filePath.add(savePath + File.separator + infoEntity.getPdfName());
            }
            content.append("</tr>");
        }

        content.append("</table>");
        content.append("<h4>本邮件为系统自动发送，如有疑问，请回复此邮件。</h4><br/><br/><br/>");
        content.append("<h4>Best Regards</h4>");
        content.append("<h4>--------------------------</h4>");
        content.append("<h4>猪小屁</h4>");
        content.append("</body></html>");
        return content.toString();
    }
}