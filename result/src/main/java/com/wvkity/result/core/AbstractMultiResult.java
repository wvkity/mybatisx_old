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
package com.wvkity.result.core;

import com.wvkity.result.model.AbstractMultiModelResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 抽象{@link Map}类型响应结果
 * @author wvkity
 * @created 2021-02-16
 * @since 1.0.0
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class AbstractMultiResult extends AbstractMultiModelResult {

    /**
     * 数据
     */
    protected Map<Object, Object> data = new HashMap<>();

    @Override
    public Map<Object, Object> getData() {
        return this.data;
    }

    @Override
    public boolean isEmpty() {
        return this.data.isEmpty();
    }

    @Override
    public Object getObject(Object key) {
        if (this.isNonNull(key) && !this.isEmpty()) {
            return this.data.get(key);
        }
        return null;
    }

    @Override
    public <T> T get(Object key) {
        final Object value = this.getObject(key);
        if (value != null) {
            return (T) value;
        }
        return null;
    }

    @Override
    public <T> T get(Object key, Class<T> clazz) {
        final Object value = this.getObject(key);
        if (value != null && clazz != null && clazz.isAssignableFrom(value.getClass())) {
            return (T) value;
        }
        return null;
    }

    @Override
    public AbstractMultiResult put(Object key, Object value) {
        if (this.isNonNull(key)) {
            this.data.put(key, value);
        }
        return this;
    }

    @Override
    public AbstractMultiResult putIfAbsent(Object key, Object value) {
        if (this.isNonNull(key)) {
            this.data.putIfAbsent(key, value);
        }
        return this;
    }

    @Override
    public AbstractMultiResult putAll(Map<?, ?> data) {
        if (data != null && !data.isEmpty()) {
            for (Map.Entry<?, ?> entry : data.entrySet()) {
                this.put(entry.getKey(), entry.getValue());
            }
        }
        return this;
    }

    @Override
    public AbstractMultiResult putAllIfAbsent(Map<?, ?> data) {
        if (data != null && !data.isEmpty()) {
            for (Map.Entry<?, ?> entry : data.entrySet()) {
                this.putIfAbsent(entry.getKey(), entry.getValue());
            }
        }
        return this;
    }

    @Override
    public boolean containsKey(Object key) {
        return !this.isEmpty() && this.data.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return !this.isEmpty() && this.data.containsValue(value);
    }

    @Override
    public int getSize() {
        return this.data.size();
    }

    @Override
    public AbstractMultiResult remove(Object key) {
        if (this.containsKey(key)) {
            this.data.remove(key);
        }
        return this;
    }

    @Override
    public AbstractMultiResult clear() {
        if (!this.isEmpty()) {
            this.data.clear();
        }
        return this;
    }

    ///// Add elements methods /////


    @Override
    public <T> AbstractMultiResult array(Object key, T... values) {
        return this.put(key, values);
    }

    @Override
    public <T> AbstractMultiResult addArray(Object key, T... values) {
        if (values != null && values.length > 0) {
            final T[] array = this.getArray(key);
            if (array == null) {
                this.put(key, values);
            } else {
                final int orgLen = array.length;
                final int joinLen = values.length;
                final T[] newArray = Arrays.copyOf(array, orgLen + joinLen);
                System.arraycopy(values, 0, newArray, orgLen, joinLen);
                this.put(key, newArray);
            }
        }
        return this;
    }

    @Override
    public <T> AbstractMultiResult addArray(Object key, Collection<T> values) {
        if (this.isNonNull(key) && this.isNotEmpty(values)) {
            this.addArray(key, (T[]) values.toArray());
        }
        return this;
    }

    @Override
    public <T> AbstractMultiResult set(Object key, T... values) {
        if (this.isNonNull(key) && values != null && values.length > 0) {
            this.put(key, new HashSet<>(Arrays.asList(values)));
        }
        return this;
    }

    @Override
    public <T> AbstractMultiResult addSet(Object key, T... values) {
        if (this.isNonNull(key) && values != null && values.length > 0) {
            this.addSet(key, Arrays.asList(values));
        }
        return this;
    }

    @Override
    public <T> AbstractMultiResult addSet(Object key, Collection<T> values) {
        if (this.isNotEmpty(values)) {
            final Set<T> set = this.getSet(key);
            if (set == null) {
                this.put(key, new HashSet<>(values));
            } else {
                set.addAll(values);
            }
        }
        return this;
    }

    @Override
    public <T> AbstractMultiResult list(Object key, T... values) {
        if (this.isNonNull(key) && values != null && values.length > 0) {
            this.put(key, new ArrayList<>(Arrays.asList(values)));
        }
        return this;
    }

    @Override
    public <T> AbstractMultiResult addList(Object key, T... values) {
        if (this.isNonNull(key) && values != null && values.length > 0) {
            this.addList(key, Arrays.asList(values));
        }
        return this;
    }

    @Override
    public <T> AbstractMultiResult addList(Object key, Collection<T> values) {
        if (this.isNotEmpty(values)) {
            final List<T> list = this.getList(key);
            if (list == null) {
                this.put(key, new ArrayList<>(values));
            } else {
                list.addAll(values);
            }
        }
        return this;
    }

    @Override
    public AbstractMultiResult map(Object key, Object k, Object v) {
        if (this.isNonNull(key) && k != null) {
            this.put(key, toMap(k, v));
        }
        return this;
    }

    @Override
    public AbstractMultiResult addMap(Object key, Object k, Object v) {
        if (k != null) {
            final Map<Object, Object> map = this.getMap(key);
            if (map == null) {
                this.put(key, toMap(k, v));
            } else {
                map.put(k, v);
            }
        }
        return this;
    }

    @Override
    public AbstractMultiResult addMap(Object key, Map<Object, Object> values) {
        if (values != null) {
            final Map<Object, Object> map = this.getMap(key);
            if (map == null) {
                this.put(key, values);
            } else {
                if (!values.isEmpty()) {
                    map.putAll(values);
                }
            }
        }
        return this;
    }

    @Override
    public AbstractMultiResult mapIfAbsent(Object key, Object k, Object v) {
        if (this.isNonNull(key) && k != null) {
            this.putIfAbsent(key, toMap(k, v));
        }
        return this;
    }

    @Override
    public AbstractMultiResult addMapIfAbsent(Object key, Object k, Object v) {
        if (k != null) {
            final Map<Object, Object> map = this.getMap(key);
            if (map == null) {
                this.putIfAbsent(key, toMap(k, v));
            } else {
                map.putIfAbsent(k, v);
            }
        }
        return this;
    }

    @Override
    public AbstractMultiResult addMapIfAbsent(Object key, Map<Object, Object> values) {
        if (values != null) {
            final Map<Object, Object> map = this.getMap(key);
            if (map == null) {
                this.putIfAbsent(key, values);
            } else {
                if (!values.isEmpty()) {
                    for (Map.Entry<Object, Object> entry : values.entrySet()) {
                        final Object k = entry.getKey();
                        if (k != null) {
                            map.putIfAbsent(k, entry.getValue());
                        }
                    }
                }
            }
        }
        return this;
    }

    private Map<Object, Object> toMap(final Object k, final Object v) {
        final Map<Object, Object> map = new HashMap<>();
        if (k != null) {
            map.put(k, v);
        }
        return map;
    }
}
