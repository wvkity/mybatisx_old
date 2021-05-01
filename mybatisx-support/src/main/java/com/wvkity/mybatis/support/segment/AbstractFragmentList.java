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
package com.wvkity.mybatis.support.segment;


import com.wvkity.mybatis.basic.immutable.ImmutableList;
import com.wvkity.mybatis.basic.utils.Objects;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 抽象零散片段列表
 * @author wvkity
 * @created 2021-01-05
 * @since 1.0.0
 */
@SuppressWarnings({"serial"})
public abstract class AbstractFragmentList<E> implements FragmentCollection<E> {

    /**
     * 零散片段列表
     */
    protected final List<E> fragments = new ArrayList<>();

    /**
     * 去重
     * @param values 值列表
     * @return 新的列表
     */
    public List<String> distinct(final Collection<E> values) {
        if (Objects.isNotEmpty(values)) {
            final Set<String> v = values.stream().filter(this::filter).map(this::toString)
                .collect(Collectors.toCollection(LinkedHashSet::new));
            if (Objects.isNotEmpty(v)) {
                return ImmutableList.of(v);
            }
        }
        return ImmutableList.of();
    }

    @Override
    public String toString(E fragment) {
        if (Objects.isNull(fragment)) {
            return null;
        }
        return fragment.toString();
    }

    @Override
    public void add(final E fragment) {
        Optional.ofNullable(fragment).ifPresent(fragments::add);
    }

    @Override
    public void addAll(final Collection<E> fragments) {
        if (Objects.isNotEmpty(fragments)) {
            this.fragments.addAll(fragments.stream().filter(Objects::nonNull).collect(Collectors.toList()));
        }
    }

    @Override
    public boolean isEmpty() {
        return this.fragments.isEmpty();
    }
}
