package com.houcy7.panda.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Descript: 用于http请求发送，设置请求头
 *
 * @author ningning.wei
 * @version 17/5/17.
 */
public enum ContentTypeEnum {

    FORM_URLENCODE(1, "application/x-www-form-urlencoded"),
    JSON(2, "application/json"),
    XML(3, "text/xml"),
    HTML(4, "text/html");

    private static final Map<Integer, ContentTypeEnum> VALUE_MAP = new HashMap<Integer, ContentTypeEnum>();

    static {
        for (ContentTypeEnum item : ContentTypeEnum.values()) {
            VALUE_MAP.put(item.value, item);
        }
    }

    private Integer value;
    private String name;

    ContentTypeEnum(Integer value, String name) {
        this.value = value;
        this.name = name;
    }

    public Integer getValue() {
        return value;
    }

    public String getName() {
        return this.name;
    }

    public static ContentTypeEnum parse(Integer value, ContentTypeEnum defaultEnum) {
        if (null == value || !VALUE_MAP.containsKey(value)) {
            return defaultEnum;
        }
        return VALUE_MAP.get(value);
    }
}