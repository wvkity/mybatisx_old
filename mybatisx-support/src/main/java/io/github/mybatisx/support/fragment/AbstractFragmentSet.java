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
package io.github.mybatisx.support.fragment;

import io.github.mybatisx.Objects;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

/**
 * 抽象零散碎片列表
 * @param <E> 元素类型
 * @author wvkity
 * @created 2021-01-16
 * @since 1.0.0
 */
@SuppressWarnings("serial")
public abstract class AbstractFragmentSet<E> implements FragmentCollection<E> {

    /**
     * 零散碎片集合
     */
    protected final Set<E> fragments = new LinkedHashSet<>();

    @Override
    public void add(E fragment) {
        Optional.ofNullable(fragment).ifPresent(this.fragments::add);
    }

    @Override
    public void addAll(Collection<E> fragments) {
        if (Objects.isNotEmpty(fragments)) {
            this.fragments.addAll(fragments);
        }
    }

    @Override
    public boolean isEmpty() {
        return this.fragments.isEmpty();
    }
}
