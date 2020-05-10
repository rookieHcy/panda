package com.houcy7.panda.common;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ClassName EmailContainer
 * @Description TODO
 * @Author hou
 * @Date 2020/5/10 3:17 下午
 * @Version 1.0
 **/
public class EmailContainer {
    private static Map<String, String> emailContainer = new ConcurrentHashMap<>();

    private EmailContainer(){};

    // 存储 fromUserName和邮箱映射关系
    public static void put(String fromUserName, String email){
        emailContainer.put(fromUserName, email);
    }

    // 获取邮箱关系
    public static String get(String fromUserName){
        return emailContainer.get(fromUserName);
    }

    public static boolean contains(String fromUserName) {
        return emailContainer.containsKey(fromUserName);
    }
}