package com.xuanxuan.csu.util;


/**
 * Dto对象转换接口,如果要自定义model->Dto的转换,请创建类实现此接口;
 *
 * @param <T>
 */
public interface VoConvertor<T, V> {

    public V converToVo(T model);
}
