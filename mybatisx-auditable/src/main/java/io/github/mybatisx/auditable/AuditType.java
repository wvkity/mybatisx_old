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
package io.github.mybatisx.auditable;

import io.github.mybatisx.auditable.annotation.Other;
import io.github.mybatisx.auditable.annotation.Remark;
import io.github.mybatisx.auditable.annotation.Time;
import io.github.mybatisx.auditable.annotation.Unique;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 审计类型
 * @author wvkity
 * @created 2021-07-13
 * @since 1.0.0
 */
public enum AuditType {

    /**
     * 标识
     */
    ID(Unique.class),
    /**
     * 备注
     */
    NAME(Remark.class),
    /**
     * 时间
     */
    TIME(Time.class),
    /**
     * 其他
     */
    OTHER(Other.class);

    final Class<? extends Annotation> target;

    AuditType(Class<? extends Annotation> target) {
        this.target = target;
    }

    public Class<? extends Annotation> getTarget() {
        return target;
    }

    private static final Map<Class<? extends Annotation>, AuditType> CACHE =
        Arrays.stream(AuditType.values()).collect(Collectors.toMap(AuditType::getTarget, Function.identity()));

    public static AuditType get(final Class<? extends Annotation> target) {
        if (target == null) {
            return null;
        }
        return AuditType.CACHE.get(target);
    }

    public static <T extends Annotation> AuditType get(final T target) {
        if (target == null) {
            return null;
        }
        return AuditType.get(target.annotationType());
    }

    public static AuditType get(final int target) {
        if (target >= 0) {
            switch (target) {
                case 0:
                    return ID;
                case 1:
                    return NAME;
                case 2:
                    return TIME;
                case 3:
                    return OTHER;
                default:
                    return null;
            }
        }
        return null;
    }

}
