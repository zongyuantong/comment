package com.xuanxuan.csu.core;

import lombok.ToString;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 条件类，封装条件
 */
@ToString
public class ConditionMap {

    private final Map<String, Object> conditionMap = new HashMap<>();

    public ConditionMap() {
    }

    /**
     * 带参构造函数
     *
     * @param object
     */
    public ConditionMap(Object object) {
        this.converFromBean(object);
    }


    /**
     * 添加查询的条件
     */
    public void addCondition(String key, Object value) {
        conditionMap.put(key, value);
    }

    public Map<String, Object> getConditionMap() {
        return conditionMap;
    }

    /**
     * 移除查询条件
     */
    public void removeCondition(String key) {
        conditionMap.remove(key);
    }

    /**
     * 移除所有条件
     */
    public void removeAll() {
        conditionMap.clear();
    }

    public void converFromBean(Object object) {
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(object.getClass());
            //得到转换属性
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
                //过滤class属性
                String key = propertyDescriptor.getName();
                if (!key.equals("class")) {
                    Method getter = propertyDescriptor.getReadMethod();
                    Object value = getter.invoke(object);
                    //放入map中
                    conditionMap.put(key, value);
                }
            }
        } catch (IntrospectionException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}