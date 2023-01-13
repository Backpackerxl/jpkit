package cn.zzwl.jpkit.change;

import cn.zzwl.jpkit.utils.ReflectUtil;
import cn.zzwl.jpkit.utils.StringUtil;

public class BeanToJSON<B> {
    private final B bean;

    public BeanToJSON(B bean) {
        this.bean = bean;
    }

    public String basicStringify() {
        return String.format("{%s}", StringUtil.substringByNumber(ReflectUtil.doBeanByField(bean, (name, obj) -> String.format("\"%s\": %s,", name, obj)), 1));
    }

    public String prettyStringify() {
        return String.format("{\n%s\n}", StringUtil.substringByNumber(ReflectUtil.doBeanByField(bean, (name, obj) -> String.format("  \"%s\": %s,\n", name, obj)), 2));
    }
}
