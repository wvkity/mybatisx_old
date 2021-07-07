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
package com.github.mybatisx.auditable.time;

import com.github.mybatisx.auditable.time.provider.CalendarProvider;
import com.github.mybatisx.auditable.time.provider.DateProvider;
import com.github.mybatisx.auditable.time.provider.InstantProvider;
import com.github.mybatisx.auditable.time.provider.LocalDateProvider;
import com.github.mybatisx.auditable.time.provider.LocalDateTimeProvider;
import com.github.mybatisx.auditable.time.provider.LocalTimeProvider;
import com.github.mybatisx.auditable.time.provider.OffsetTimeProvider;
import com.github.mybatisx.auditable.time.provider.TimeProvider;
import com.github.mybatisx.auditable.time.provider.TimestampProvider;
import com.github.mybatisx.auditable.time.provider.ZoneDateTimeProvider;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 时间注册器
 * @author wvkity
 * @created 2021-03-11
 * @since 1.0.0
 */
public final class TimeProviderRegistry {

    private TimeProviderRegistry(){}

    private static final Map<Class<?>, Class<? extends TimeProvider<?>>> TIME_PROVIDER_CACHE = new ConcurrentHashMap<>();

    static {
        TIME_PROVIDER_CACHE.put(LocalDate.class, LocalDateProvider.class);
        TIME_PROVIDER_CACHE.put(LocalTime.class, LocalTimeProvider.class);
        TIME_PROVIDER_CACHE.put(LocalDateTime.class, LocalDateTimeProvider.class);
        TIME_PROVIDER_CACHE.put(OffsetTime.class, OffsetTimeProvider.class);
        TIME_PROVIDER_CACHE.put(OffsetDateTime.class, OffsetTimeProvider.class);
        TIME_PROVIDER_CACHE.put(ZonedDateTime.class, ZoneDateTimeProvider.class);
        TIME_PROVIDER_CACHE.put(Instant.class, InstantProvider.class);
        TIME_PROVIDER_CACHE.put(Timestamp.class, TimestampProvider.class);
        TIME_PROVIDER_CACHE.put(Date.class, DateProvider.class);
        TIME_PROVIDER_CACHE.put(Calendar.class, CalendarProvider.class);
    }

    public static Class<? extends TimeProvider<?>> get(final Class<?> target) {
        if (target == null) {
            return null;
        }
        return TIME_PROVIDER_CACHE.get(target);
    }

    public static void add(final Class<?> target, final Class<? extends TimeProvider<?>> source) {
        if (target != null && source != null) {
            TIME_PROVIDER_CACHE.put(target, source);
        }
    }

    public static void addIfAbsent(final Class<?> target, final Class<? extends TimeProvider<?>> source) {
        if (target != null && source != null) {
            TIME_PROVIDER_CACHE.putIfAbsent(target, source);
        }
    }
}
