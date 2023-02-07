package com.zzwl.jpkit.typeof;

import com.zzwl.jpkit.core.ITypeof;
public abstract class JBase implements ITypeof<Object> {
    /**
     * 判断是否为基础数据类型的包装类型
     *
     * @param o 数据
     * @return 是否为 Integer Boolean Byte Long Short Double Float
     */
    public static boolean isBase(Object o) {
        return o.getClass().getTypeName().startsWith("java.math") || o.getClass().getTypeName().startsWith("java.util") || o.getClass().getTypeName().startsWith("java.lang");
    }
}
