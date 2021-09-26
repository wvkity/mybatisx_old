/*
 * Copyright (c) 2020-2021, wvkity(wvkity@gmail.com).
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
package io.github.mybatisx.datetime;

import io.github.mybatisx.Objects;
import io.github.mybatisx.datetime.provider.CalendarProvider;
import io.github.mybatisx.datetime.provider.DateProvider;
import io.github.mybatisx.datetime.provider.DateTimeProvider;
import io.github.mybatisx.datetime.provider.InstantProvider;
import io.github.mybatisx.datetime.provider.LocalDateProvider;
import io.github.mybatisx.datetime.provider.LocalDateTimeProvider;
import io.github.mybatisx.datetime.provider.LocalTimeProvider;
import io.github.mybatisx.datetime.provider.OffsetTimeProvider;
import io.github.mybatisx.datetime.provider.TimestampProvider;
import io.github.mybatisx.datetime.provider.ZoneDateTimeProvider;

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
 * @created 2021-07-16
 * @since 1.0.0
 */
public final class DateTimeProviderRegistry {

    private DateTimeProviderRegistry() {
    }

    private static final Map<Class<?>, Class<? extends DateTimeProvider<?>>> PROVIDER_CACHE = new ConcurrentHashMap<>();

    static {
        PROVIDER_CACHE.put(LocalDate.class, LocalDateProvider.class);
        PROVIDER_CACHE.put(LocalTime.class, LocalTimeProvider.class);
        PROVIDER_CACHE.put(LocalDateTime.class, LocalDateTimeProvider.class);
        PROVIDER_CACHE.put(OffsetTime.class, OffsetTimeProvider.class);
        PROVIDER_CACHE.put(OffsetDateTime.class, OffsetTimeProvider.class);
        PROVIDER_CACHE.put(ZonedDateTime.class, ZoneDateTimeProvider.class);
        PROVIDER_CACHE.put(Instant.class, InstantProvider.class);
        PROVIDER_CACHE.put(Timestamp.class, TimestampProvider.class);
        PROVIDER_CACHE.put(Date.class, DateProvider.class);
        PROVIDER_CACHE.put(Calendar.class, CalendarProvider.class);
    }

    public static Class<? extends DateTimeProvider<?>> get(final Class<?> target) {
        if (Objects.nonNull(target)) {
            return PROVIDER_CACHE.get(target);
        }
        return null;
    }

    public static void add(final Class<?> target, final Class<? extends DateTimeProvider<?>> provider) {
        if (Objects.nonNull(target) && Objects.nonNull(provider)) {
            PROVIDER_CACHE.put(target, provider);
        }
    }

    public static void addIfAbsent(final Class<?> target, final Class<? extends DateTimeProvider<?>> provider) {
        if (Objects.nonNull(target) && Objects.nonNull(provider)) {
            PROVIDER_CACHE.putIfAbsent(target, provider);
        }
    }
}
