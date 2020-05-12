package com.houcy7.panda.strategy;

import com.houcy7.panda.common.InfoContainer;
import com.houcy7.panda.entity.system.InfoEntity;
import com.houcy7.panda.entity.wechat.message.request.BaseMessage;
import com.houcy7.panda.enums.InfoEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @ClassName StatusStrategy
 * @Description TODO
 * @Author hou
 * @Date 2020/5/12 1:12 下午
 * @Version 1.0
 **/
@Component
@Slf4j
public class StatusStrategy extends AbsStrategy {

    private final Set<String> KEYWORDS = new HashSet<>();

    {
        KEYWORDS.add("status");
        KEYWORDS.add("STATUS");
        KEYWORDS.add("状态");
    }

    @Override
    protected String realWork(String content, BaseMessage baseMessage) {
        Map<String, InfoEntity> map = InfoContainer.get(baseMessage.getFromUserName());
        // 为空的时候
        if (null == map || map.isEmpty()) {
            return "您还没有任何需要处理的论文呢，发送doi或论文链接开始下载";
        }

        // 按照状态进行分组
        Map<Integer, List<InfoEntity>> groupMap = map.values().stream().collect(Collectors.groupingBy(InfoEntity::getStatus));

        StringBuilder result = new StringBuilder("当前论文下载情况：\n");
        for (Map.Entry<Integer, List<InfoEntity>> entry : groupMap.entrySet()) {
            result.append(InfoEnum.getEnum(entry.getKey()).desc).append("：").append(entry.getValue().size()).append("个;").append("\n");
        }

        // 获取未下载的数据个数
        List<InfoEntity> infoEntities = groupMap.get(InfoEnum.START.type);
        if (null != infoEntities && !infoEntities.isEmpty()) {
            result.append("正在下载论文列表：").append("\n");
            // 流拼接关键词属性
            String str = infoEntities.stream().map(InfoEntity::getContent).collect(Collectors.joining(","));
            result.append("【").append(str).append("】");
        }
        return result.toString();
    }

    @Override
    protected boolean match(BaseMessage baseMessage, String content) {
        if (KEYWORDS.contains(content)) {
            log.info("msgId={}, content={}匹配到了【状态策略】", baseMessage.getMsgId(), content);
            return true;
        }
        return false;
    }
}