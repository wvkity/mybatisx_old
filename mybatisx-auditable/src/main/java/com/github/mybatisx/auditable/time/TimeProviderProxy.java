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

import com.github.mybatisx.auditable.AuditingException;
import com.github.mybatisx.auditable.time.provider.TimeProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.WeakHashMap;

/**
 * 时间代理
 * @author wvkity
 * @created 2021-03-11
 * @since 1.0.0
 */
public class TimeProviderProxy implements InvocationHandler {

    private static final Logger log = LoggerFactory.getLogger(TimeProviderProxy.class);
    private static final Map<Class<?>, TimeProvider<?>> TIME_PROVIDER_REFERENCE = Collections.synchronizedMap(new WeakHashMap<>());
    private final TimeProvider<?> target;

    public TimeProviderProxy(TimeProvider<?> target) {
        this.target = target;
    }

    public TimeProvider<?> getTarget() {
        return (TimeProvider<?>) Proxy.newProxyInstance(this.getClass().getClassLoader(),
            target.getClass().getInterfaces(), this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return method.invoke(this.target, args);
    }

    public static Builder create() {
        return TimeProviderProxy.Builder.create();
    }

    public static final class Builder {

        private Class<?> target;

        private Builder() {
        }

        public Builder target(final Class<?> target) {
            if (target == null) {
                throw new AuditingException("The time type parameter cannot be null.");
            }
            this.target = target;
            return this;
        }

        public TimeProvider<?> build() {
            final TimeProvider<?> cache = TIME_PROVIDER_REFERENCE.get(this.target);
            if (cache != null) {
                return cache;
            }
            final TimeProvider<?> provider = Optional.ofNullable(TimeProviderRegistry.get(this.target))
                .map(it -> new TimeProviderProxy(newInstance(it)).getTarget()).orElse(null);
            if (provider != null) {
                TIME_PROVIDER_REFERENCE.put(this.target, provider);
            } else {
                log.warn("The corresponding time supplier cannot be found according to '"+ this.target +"', " +
                    "please register the corresponding supplier by yourself, and the system will automatically ignore injection.");
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
}
