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
package io.github.mybatisx.datetime;

import io.github.mybatisx.Objects;
import io.github.mybatisx.datetime.provider.DateTimeProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Optional;

/**
 * 时间代理
 * @author wvkity
 * @created 2021-03-11
 * @since 1.0.0
 */
public class DateTimeProviderProxy implements InvocationHandler {

    private static final Logger log = LoggerFactory.getLogger(DateTimeProviderProxy.class);
    private static final ThreadLocal<Object> THREAD_LOCAL = new ThreadLocal<>();
    private final DateTimeProvider<?> target;

    public DateTimeProviderProxy(DateTimeProvider<?> target) {
        this.target = target;
    }

    public DateTimeProvider<?> getTarget() {
        return (DateTimeProvider<?>) Proxy.newProxyInstance(this.getClass().getClassLoader(),
            target.getClass().getInterfaces(), this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return method.invoke(this.target, args);
    }

    public static Builder create() {
        return DateTimeProviderProxy.Builder.create();
    }

    public static final class Builder {

        private Class<?> target;

        private Builder() {
        }

        public Builder target(final Class<?> target) {
            if (target == null) {
                throw new NullPointerException("The time type parameter cannot be null.");
            }
            this.target = target;
            return this;
        }

        public DateTimeProvider<?> build() {
            final DateTimeProvider<?> provider = Optional.ofNullable(DateTimeProviderRegistry.get(this.target))
                .map(it -> new DateTimeProviderProxy(newInstance(it)).getTarget()).orElse(null);
            if (provider == null) {
                log.warn("The corresponding time supplier cannot be found according to '" + this.target + "', " +
                    "please register the corresponding supplier by yourself, and the system will automatically ignore" +
                    " injection.");
            }
            return provider;
        }

        public static Builder create() {
            return new Builder();
        }

        @SuppressWarnings("unchecked")
        public static <T> T newInstance(final Class<T> clazz) {
            try {
                final MethodHandles.Lookup lookup = MethodHandles.lookup();
                return (T) lookup.findConstructor(clazz, MethodType.methodType(void.class)).invokeWithArguments();
            } catch (Throwable ignore) {
                // ignore
            }
            return null;
        }
    }

    /**
     * 获取当前时间
     * @param target 时间类
     * @return 时间值
     */
    public static Object getNow(final Class<?> target) {
        return Optional.ofNullable(DateTimeProviderProxy.create().target(target).build())
            .map(DateTimeProvider::getNow).orElse(null);
    }

    /**
     * 从{@link ThreadLocal}中获取时间
     * @param target 时间类
     * @return 时间值
     */
    public static Object get(final Class<?> target) {
        Object value = THREAD_LOCAL.get();
        if (Objects.nonNull(value)) {
            return value;
        }
        value = getNow(target);
        if (Objects.nonNull(value)) {
            THREAD_LOCAL.set(value);
        }
        return value;
    }

    /**
     * 移除{@link ThreadLocal}值
     */
    public static void remove() {
        THREAD_LOCAL.remove();
    }

}
