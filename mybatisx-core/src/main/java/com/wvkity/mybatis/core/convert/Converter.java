package com.wvkity.mybatis.core.convert;

import java.io.Serializable;

/**
 * 转换接口
 * @param <S> 源类型
 * @param <T> 目标类型
 * @author wvkity
 * @created 2021-05-10
 * @since 1.0.0
 */
public interface Converter<S, T> extends Serializable {

    /**
     * 转换
     * @param source 源对象
     * @return 目标对象
     */
    T convert(final S source);
}
