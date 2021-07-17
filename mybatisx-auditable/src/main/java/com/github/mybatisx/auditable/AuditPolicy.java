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
package com.github.mybatisx.auditable;

import com.github.mybatisx.auditable.annotation.Created;
import com.github.mybatisx.auditable.annotation.Deleted;
import com.github.mybatisx.auditable.annotation.Modified;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 审计策略
 * @author wvkity
 * @created 2021-07-13
 * @since 1.0.0
 */
public enum AuditPolicy {

    /**
     * 保存
     */
    Created(Created.class),
    /**
     * 更新
     */
    MODIFIED(Modified.class),
    /**
     * 逻辑删除
     */
    DELETED(Deleted.class);

    final Class<? extends Annotation> target;

    AuditPolicy(Class<? extends Annotation> target) {
        this.target = target;
    }

    public Class<? extends Annotation> getTarget() {
        return target;
    }

    private static final Map<Class<? extends Annotation>, AuditPolicy> CACHE =
        Arrays.stream(AuditPolicy.values()).collect(Collectors.toMap(AuditPolicy::getTarget, Function.identity()));

    public static AuditPolicy get(final Class<? extends Annotation> target) {
        if (target == null) {
            return null;
        }
        return AuditPolicy.CACHE.get(target);
    }

    public static <T extends Annotation> AuditPolicy get(final T target) {
        if (target == null) {
            return null;
        }
        return AuditPolicy.get(target.annotationType());
    }

    public static AuditPolicy get(final int policy) {
        if (policy >= 0) {
            switch (policy) {
                case 0:
                    return Created;
                case 1:
                    return MODIFIED;
                case 2:
                    return DELETED;
                default:
                    return null;
            }
        }
        return null;
    }

    public static Set<AuditPolicy> getSet(final Set<Integer> policies) {
        if (policies != null && !policies.isEmpty()) {
            return policies.stream().map(AuditPolicy::get).filter(Objects::nonNull).collect(Collectors.toSet());
        }
        return new HashSet<>(0);
    }
}
