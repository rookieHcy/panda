package com.houcy7.panda.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @ClassName DateUtil
 * @Description TODO
 * @Author hou
 * @Date 2020/5/11 1:42 上午
 * @Version 1.0
 **/
public class DateUtil {
    public static String getYear2MS() {
        SimpleDateFormat sdf = new SimpleDateFormat("YYYYMMddHHmmssSSS");
        return sdf.format(new Date());
    }
}