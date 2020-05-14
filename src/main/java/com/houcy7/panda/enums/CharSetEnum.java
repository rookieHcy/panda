package com.houcy7.panda.enums;

import java.util.HashMap;
import java.util.Map;

/**
     * 字符集
     *
     * @author yp-tc-m-2821
     */
    public enum CharSetEnum {

        UTF8(1, "UTF-8"),
        GBK(2, "GBK");

        private static final Map<Integer, CharSetEnum> VALUE_MAP = new HashMap<Integer, CharSetEnum>();

        static {
            for (CharSetEnum item : CharSetEnum.values()) {
                VALUE_MAP.put(item.value, item);
            }
        }

        private Integer value;
        private String name;

        CharSetEnum(Integer value, String name) {
            this.value = value;
            this.name = name;
        }

        public Integer getValue() {
            return value;
        }

        public String getName() {
            return this.name;
        }

        public static CharSetEnum parse(Integer value, CharSetEnum defaultEnum) {
            if (null == value || !VALUE_MAP.containsKey(value)) {
                return defaultEnum;
            }
            return VALUE_MAP.get(value);
        }
    }