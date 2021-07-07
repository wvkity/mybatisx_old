package com.github.mybatisx.core.criteria.support;

import com.github.mybatisx.support.constant.Slot;

/**
 * AND/OR运算符
 * @param <T> 实体类型
 * @param <C> 子类型
 * @author wvkity
 * @created 2021-07-08
 * @since 1.0.0
 */
public interface SlotSymbol<T, C extends SlotSymbol<T, C>> {

    /**
     * {@code Slot.AND}
     * @return {@code this}
     */
    C and();

    /**
     * {@code Slot.OR}
     * @return {@code this}
     */
    C or();

    /**
     * 获取{@link Slot}
     * @return {@link Slot}
     */
    Slot getSlot();
}
