package com.zzwl.jpkit.parse;

import com.zzwl.jpkit.anno.JPConfig;
import com.zzwl.jpkit.anno.JPMethod;
import com.zzwl.jpkit.bean.JPConfigAnno;
import com.zzwl.jpkit.bean.JPConfigAnnoContext;
import com.zzwl.jpkit.core.ITypeof;
import com.zzwl.jpkit.typeof.JBase;
import com.zzwl.jpkit.typeof.JObject;
import com.zzwl.jpkit.utils.ReflectUtil;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Map;

/**
 * @since 1.0
 */
public class ObjectParse {
    private final JBase jBase;
    public final static String TEMPLATE = "%s$%s$%s";

    public ObjectParse(ITypeof<Object> typeof) {
        this.jBase = (JBase) typeof;
    }


    /**
     * JSON转化转化为对象
     *
     * @param clazz 类型
     * @param <B>   转化后的类型
     * @return 转化好的对象
     */
    public <B> B parse(Class<B> clazz) {
        Object bean = createBean(clazz);
        if (!JPConfigAnnoContext.getAnnoConfigContext().getContext().containsKey(clazz.getTypeName())) {
            init(clazz);
        }
        JObject jo = (JObject) this.jBase;
        ReflectUtil.setBeanByField(bean, (name) -> jo.getValue().get(name));
        return (B) bean;
    }

    /**
     * 通过注解初始化自定义插件信息
     */
    public static void init(Class<?> clazz) {
        if (clazz.isAnnotationPresent(JPConfig.class)) {
            Map<String, JPConfigAnno> context = JPConfigAnnoContext.getAnnoConfigContext().getContext();
            JPConfig jpConfig = clazz.getDeclaredAnnotation(JPConfig.class);
            JPConfigAnno configAnno = new JPConfigAnno();
            Map<String, Object[]> methodStore = configAnno.getMethodStore();
            for (Class<?> plug : jpConfig.plugs()) {
                for (Method method : plug.getDeclaredMethods()) {
                    if (method.isAnnotationPresent(JPMethod.class)) {
                        Object[] objects = new Object[2];
                        objects[0] = createBean(plug);
                        objects[1] = method;
                        methodStore.put(String.format(TEMPLATE, plug.getTypeName(), method.getReturnType().getTypeName(), method.getDeclaredAnnotation(JPMethod.class).value()), objects);
                    }
                }
            }
            context.put(clazz.getTypeName(), configAnno);
        }
    }

    /**
     * 通过class获得一个对象
     *
     * @param clazz 源
     * @return 根据Class创建的对象
     */
    public static Object createBean(Class<?> clazz) {
        try {
            // 使用无参构造函数
            Constructor<?> constructor = Class.forName(clazz.getName()).getDeclaredConstructor();
            return constructor.newInstance();
        } catch (NoSuchMethodException | ClassNotFoundException e) {
            // log 没有无参构造函数
            try {
                Constructor<?>[] constructors = Class.forName(clazz.getName()).getDeclaredConstructors();
                Constructor<?> constructor = constructors[0];
                Parameter[] parameters = constructor.getParameters();
                Object[] objects = new Object[parameters.length];
                for (int i = 0; i < parameters.length; i++) {
                    objects[i] = JBase.getBaseValue(parameters[i].getType());
                }
                return constructor.newInstance(objects);
            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException |
                     InvocationTargetException ex) {
                throw new RuntimeException(ex);
            }
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            // log 不能创建实例
            throw new RuntimeException(e);
        }
    }
}
