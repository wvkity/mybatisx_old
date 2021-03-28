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
package com.wvkity.mybatis.basic.type;

import org.apache.ibatis.type.JdbcType;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZonedDateTime;
import java.time.chrono.JapaneseDate;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;

/**
 * JDBC类型映射注册
 * @author wvkity
 * @created 2020-10-11
 * @since 1.0.0
 */
public final class JdbcTypeMappingRegistry {

    private JdbcTypeMappingRegistry() {
    }

    /**
     * JDBC类型映射缓存
     */
    private static final Map<Class<?>, JdbcType> JDBC_TYPE_MAPPING_CACHE = new ConcurrentHashMap<>(64);

    static {
        // Basic
        JDBC_TYPE_MAPPING_CACHE.put(byte.class, JdbcType.BIT);
        JDBC_TYPE_MAPPING_CACHE.put(Byte.class, JdbcType.BIT);
        JDBC_TYPE_MAPPING_CACHE.put(boolean.class, JdbcType.BIT);
        JDBC_TYPE_MAPPING_CACHE.put(Boolean.class, JdbcType.BIT);
        JDBC_TYPE_MAPPING_CACHE.put(short.class, JdbcType.SMALLINT);
        JDBC_TYPE_MAPPING_CACHE.put(Short.class, JdbcType.SMALLINT);
        JDBC_TYPE_MAPPING_CACHE.put(int.class, JdbcType.INTEGER);
        JDBC_TYPE_MAPPING_CACHE.put(Integer.class, JdbcType.INTEGER);
        JDBC_TYPE_MAPPING_CACHE.put(long.class, JdbcType.BIGINT);
        JDBC_TYPE_MAPPING_CACHE.put(Long.class, JdbcType.BIGINT);
        JDBC_TYPE_MAPPING_CACHE.put(float.class, JdbcType.FLOAT);
        JDBC_TYPE_MAPPING_CACHE.put(Float.class, JdbcType.FLOAT);
        JDBC_TYPE_MAPPING_CACHE.put(double.class, JdbcType.DOUBLE);
        JDBC_TYPE_MAPPING_CACHE.put(Double.class, JdbcType.DOUBLE);
        JDBC_TYPE_MAPPING_CACHE.put(BigDecimal.class, JdbcType.NUMERIC);
        JDBC_TYPE_MAPPING_CACHE.put(BigInteger.class, JdbcType.BIGINT);
        JDBC_TYPE_MAPPING_CACHE.put(char.class, JdbcType.CHAR);
        JDBC_TYPE_MAPPING_CACHE.put(Character.class, JdbcType.CHAR);
        JDBC_TYPE_MAPPING_CACHE.put(String.class, JdbcType.VARCHAR);
        // String
        JDBC_TYPE_MAPPING_CACHE.put(Locale.class, JdbcType.VARCHAR);
        JDBC_TYPE_MAPPING_CACHE.put(Currency.class, JdbcType.VARCHAR);
        JDBC_TYPE_MAPPING_CACHE.put(TimeZone.class, JdbcType.VARCHAR);
        JDBC_TYPE_MAPPING_CACHE.put(Class.class, JdbcType.VARCHAR);
        JDBC_TYPE_MAPPING_CACHE.put(Clob.class, JdbcType.CLOB);
        JDBC_TYPE_MAPPING_CACHE.put(Blob.class, JdbcType.BLOB);
        JDBC_TYPE_MAPPING_CACHE.put(byte[].class, JdbcType.BLOB);
        JDBC_TYPE_MAPPING_CACHE.put(Byte[].class, JdbcType.BLOB);
        // Time
        JDBC_TYPE_MAPPING_CACHE.put(Date.class, JdbcType.TIMESTAMP);
        JDBC_TYPE_MAPPING_CACHE.put(java.sql.Date.class, JdbcType.TIMESTAMP);
        JDBC_TYPE_MAPPING_CACHE.put(Calendar.class, JdbcType.TIMESTAMP);
        JDBC_TYPE_MAPPING_CACHE.put(Timestamp.class, JdbcType.TIMESTAMP);
        JDBC_TYPE_MAPPING_CACHE.put(Instant.class, JdbcType.TIMESTAMP);
        JDBC_TYPE_MAPPING_CACHE.put(LocalDateTime.class, JdbcType.TIMESTAMP);
        JDBC_TYPE_MAPPING_CACHE.put(OffsetDateTime.class, JdbcType.TIMESTAMP);
        JDBC_TYPE_MAPPING_CACHE.put(ZonedDateTime.class, JdbcType.TIMESTAMP);
        JDBC_TYPE_MAPPING_CACHE.put(LocalDate.class, JdbcType.DATE);
        JDBC_TYPE_MAPPING_CACHE.put(JapaneseDate.class, JdbcType.DATE);
        JDBC_TYPE_MAPPING_CACHE.put(OffsetTime.class, JdbcType.TIME);
        JDBC_TYPE_MAPPING_CACHE.put(LocalTime.class, JdbcType.TIME);
    }

    /**
     * 注册JDBC类型映射
     * @param key   类型
     * @param value JDBC类型
     */
    public static void registry(final Class<?> key, final JdbcType value) {
        registry(key, value, false);
    }

    /**
     * 注册JDBC类型映射
     * @param key      类型
     * @param value    JDBC类型
     * @param override 是否覆盖已存在的
     */
    public static void registry(final Class<?> key, final JdbcType value, final boolean override) {
        if (key != null && value != null && value != JdbcType.UNDEFINED) {
            if (override) {
                JDBC_TYPE_MAPPING_CACHE.put(key, value);
            } else {
                JDBC_TYPE_MAPPING_CACHE.putIfAbsent(key, value);
            }
        }
    }

    /**
     * 获取JDBC类型
     * @param key 类型
     * @return {@link JdbcType}
     */
    public static JdbcType getJdbcType(final Class<?> key) {
        return getJdbcType(key, null);
    }

    /**
     * 获取JDBC类型
     * @param key          类型
     * @param defaultValue 默认值
     * @return {@link JdbcType}
     */
    public static JdbcType getJdbcType(final Class<?> key, final JdbcType defaultValue) {
        return Optional.ofNullable(key).map(it ->
            JDBC_TYPE_MAPPING_CACHE.getOrDefault(key, defaultValue)
        ).orElse(defaultValue);
    }

    /**
     * 检查JDBC类型映射缓存中是否存在指定的类型
     * @param key 类型
     * @return boolean
     */
    public static boolean contains(final Class<?> key) {
        return key != null && JDBC_TYPE_MAPPING_CACHE.containsKey(key);
    }
}
