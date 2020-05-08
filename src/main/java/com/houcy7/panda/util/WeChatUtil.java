package com.houcy7.panda.util;

import com.houcy7.panda.entity.CheckSignatureBean;

import java.util.Arrays;

/**
 * @ClassName WeChatUtil
 * @Description TODO
 * @Author hou
 * @Date 2020/5/9 1:00 上午
 * @Version 1.0
 **/
public class WeChatUtil {

    public static String checkSignature(CheckSignatureBean bean, String token) {
        String[] arr = {token, bean.getTimestamp(), bean.getNonce()};
        Arrays.sort(arr);
        String requireSign = Sha1Util.arr2SHA1(arr);
        if (bean.getSignature().equals(requireSign)) {
            return bean.getEchostr();
        }
        return "";
    }

}