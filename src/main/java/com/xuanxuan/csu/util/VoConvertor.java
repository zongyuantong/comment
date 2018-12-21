package com.xuanxuan.csu.util;


import com.xuanxuan.csu.vo.AbstractVO;

/**
 * Dto对象转换接口,如果要自定义model->Dto的转换,请创建类实现此接口;
 *
 * @param <T>
 */
public interface VoConvertor<T, V> {

    public V conver2Vo(T model);
}
