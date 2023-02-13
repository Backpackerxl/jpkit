package com.zzwl.jpkit.typeof;

import com.zzwl.jpkit.core.ITypeof;
import com.zzwl.jpkit.plugs.impl.JBaseEntryImpl;
import com.zzwl.jpkit.utils.ArrayUtil;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @since 1.0
 */
public class JString extends JBase {
    private final String value;

    public JString(String value) {
        this.value = value;
    }

    /**
     * JArray to String[]
     *
     * @param jBase 数据源
     * @return String[]
     */
    public static String[] getArr(JBase jBase) {
        return (String[]) ArrayUtil.doArrayByArray(jBase, new String[]{}, ITypeof::getValue);
    }

    /**
     * List<JBase> 转 List<String>
     *
     * @param jBase 数据源
     * @return List<String>
     */
    public static List<String> getList(JBase jBase) {
        return Arrays.stream(getArr(jBase)).collect(Collectors.toList());
    }

    /**
     * Map<String, JBase> 转 Map<String,String>
     *
     * @param jBase 数据源
     * @return Map<String, String>
     */
    public static Map<String, String> getMap(JBase jBase) {
        return ArrayUtil.doArrayByMap(jBase, (jb) -> new JBaseEntryImpl<>(jb.getKey(), ((JString) jb).getValue()));
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.format("\"%s\"", value);
    }
}
