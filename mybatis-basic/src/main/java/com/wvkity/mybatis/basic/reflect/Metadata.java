/*
 * Copyright (c) 2020, wvkity(wvkity@gmail.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * https://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.wvkity.mybatis.basic.reflect;

/**
 * 元数据接口
 * @author wvkity
 * @created 2020-10-07
 * @since 1.0.0
 */
public interface Metadata {

    /**
     * value属性
     */
    String VALUE_PROP = "value";

    /**
     * 获取byte类型值
     * @return byte
     */
    default byte byteValue() {
        return byteValue(VALUE_PROP);
    }

    /**
     * 获取byte类型值
     * @param prop 属性
     * @return byte
     */
    default byte byteValue(final String prop) {
        return byteValue(prop, (byte) 0);
    }

    /**
     * 获取byte类型值
     * @param prop     属性
     * @param defValue 默认值
     * @return byte
     */
    default byte byteValue(final String prop, final byte defValue) {
        return getValue(prop, defValue, byte.class);
    }

    /**
     * 获取char类型值
     * @return char
     */
    default char charValue() {
        return charValue(VALUE_PROP);
    }

    /**
     * 获取char类型值
     * @param prop 属性
     * @return char
     */
    default char charValue(final String prop) {
        return charValue(prop, Character.MIN_VALUE);
    }

    /**
     * 获取char类型值
     * @param prop     属性
     * @param defValue 默认值
     * @return char
     */
    default char charValue(final String prop, final char defValue) {
        return getValue(prop, defValue, char.class);
    }

    /**
     * 获取boolean类型值
     * @return boolean
     */
    default boolean booleanValue() {
        return booleanValue(VALUE_PROP);
    }

    /**
     * 获取boolean类型值
     * @param prop 属性
     * @return boolean
     */
    default boolean booleanValue(final String prop) {
        return booleanValue(prop, false);
    }

    /**
     * 获取boolean类型值
     * @param prop     属性
     * @param defValue 默认值
     * @return boolean
     */
    default boolean booleanValue(final String prop, final boolean defValue) {
        return getValue(prop, defValue, boolean.class);
    }

    /**
     * 获取short类型值
     * @return short
     */
    default short shortValue() {
        return shortValue(VALUE_PROP);
    }

    /**
     * 获取short类型值
     * @param prop 属性
     * @return short
     */
    default short shortValue(final String prop) {
        return shortValue(prop, (short) 0);
    }

    /**
     * 获取short类型值
     * @param prop     属性
     * @param defValue 默认值
     * @return short
     */
    default short shortValue(final String prop, final short defValue) {
        return getValue(prop, defValue, short.class);
    }

    /**
     * 获取int类型值
     * @return int
     */
    default int intValue() {
        return intValue(VALUE_PROP);
    }

    /**
     * 获取int类型值
     * @param prop 属性
     * @return int
     */
    default int intValue(final String prop) {
        return intValue(prop, 0);
    }

    /**
     * 获取int类型值
     * @param prop     属性
     * @param defValue 默认值
     * @return int
     */
    default int intValue(final String prop, final int defValue) {
        return getValue(prop, defValue, int.class);
    }

    /**
     * 获取long类型值
     * @return char
     */
    default long longValue() {
        return longValue(VALUE_PROP);
    }

    /**
     * 获取long类型值
     * @param prop 属性
     * @return char
     */
    default long longValue(final String prop) {
        return longValue(prop, 0L);
    }

    /**
     * 获取long类型值
     * @param prop     属性
     * @param defValue 默认值
     * @return long值
     */
    default long longValue(final String prop, final long defValue) {
        return getValue(prop, defValue, long.class);
    }

    /**
     * 获取char类型值
     * @return char
     */
    default float floatValue() {
        return floatValue(VALUE_PROP);
    }

    /**
     * 获取char类型值
     * @param prop 属性
     * @return char
     */
    default float floatValue(final String prop) {
        return floatValue(prop, 0.0F);
    }

    /**
     * 获取float类型值
     * @param prop     属性
     * @param defValue 默认值
     * @return float
     */
    default float floatValue(final String prop, final float defValue) {
        return getValue(prop, defValue, float.class);
    }

    /**
     * 获取double类型值
     * @return double
     */
    default double doubleValue() {
        return doubleValue(VALUE_PROP);
    }

    /**
     * 获取double类型值
     * @param prop 属性
     * @return double
     */
    default double doubleValue(final String prop) {
        return doubleValue(prop, 0.0D);
    }

    /**
     * 获取double类型值
     * @param prop     属性
     * @param defValue 默认值
     * @return double
     */
    default double doubleValue(final String prop, final double defValue) {
        return getValue(prop, defValue, double.class);
    }

    /**
     * 获取double类型值
     * @return String
     */
    default String stringValue() {
        return stringValue(VALUE_PROP);
    }

    /**
     * 获取double类型值
     * @param prop 属性
     * @return String
     */
    default String stringValue(final String prop) {
        return stringValue(prop, "");
    }

    /**
     * 获取String类型值
     * @param prop     属性
     * @param defValue 默认值
     * @return String
     */
    default String stringValue(final String prop, final String defValue) {
        return getValue(prop, defValue, String.class);
    }

    /**
     * 获取枚举类型值
     * @return Enum
     */
    default Enum<? extends Enum<?>> enumValue() {
        return getValue(VALUE_PROP, null);
    }

    /**
     * 获取枚举类型值
     * @param prop 属性
     * @return Enum
     */
    default Enum<? extends Enum<?>> enumValue(final String prop) {
        return getValue(prop, null);
    }

    /**
     * 获取枚举类型值
     * @param prop         属性
     * @param defaultValue 默认值
     * @return Enum
     */
    default Enum<? extends Enum<?>> enumValue(final String prop, final Enum<? extends Enum<?>> defaultValue) {
        return (Enum<? extends Enum<?>>) getValue(prop, defaultValue, Enum.class);
    }

    /**
     * 获取值
     * @param clazz 类型
     * @param <T>   值类型
     * @return 值
     */
    default <T> T getValue(final Class<T> clazz) {
        return getValue(VALUE_PROP, clazz);
    }

    /**
     * 获取值
     * @param prop  属性
     * @param clazz 类型
     * @param <T>   值类型
     * @return 值
     */
    default <T> T getValue(final String prop, final Class<T> clazz) {
        return getValue(prop, null, clazz);
    }

    /**
     * 获取值
     * @param prop     属性
     * @param defValue 默认值
     * @param clazz    类型
     * @param <T>      值类型
     * @return 值
     */
    <T> T getValue(final String prop, final T defValue, final Class<T> clazz);
}
