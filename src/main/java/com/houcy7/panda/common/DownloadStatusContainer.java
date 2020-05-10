package com.houcy7.panda.common;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @ClassName DownloadStatusContainer
 * @Description TODO
 * @Author hou
 * @Date 2020/5/10 3:29 下午
 * @Version 1.0
 **/
public class DownloadStatusContainer {
    private static Set<String> downloadStatusContainer = Collections.synchronizedSet(new HashSet<>());

    private DownloadStatusContainer(){};

    // 存储 fromUserName和邮箱映射关系
    public static void add(String fromUserName){
        downloadStatusContainer.add(fromUserName);
    }

    // 获取邮箱关系
    public static boolean get(String fromUserName){
        return downloadStatusContainer.contains(fromUserName);
    }

    // 移除
    public static boolean remove(String fromUserName){
        return downloadStatusContainer.remove(fromUserName);
    }
}