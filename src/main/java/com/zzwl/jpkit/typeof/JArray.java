package com.zzwl.jpkit.typeof;

import com.zzwl.jpkit.core.JSON;

import java.util.List;

/**
 * @since 1.0
 */
public class JArray extends JBase {
    private final List<JBase> value;

    public JArray(List<JBase> value) {
        this.value = value;
    }

    @Override
    public List<JBase> getValue() {
        return value;
    }

    @Override
    public String toString() {
        return JSON.stringify(value).terse();
    }
}
