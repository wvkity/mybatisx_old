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
package com.github.mybatisx.plugin.auditable;

import com.github.mybatisx.Objects;
import com.github.mybatisx.auditable.PropertyWrapper;
import com.github.mybatisx.auditable.event.AuditedEvent;
import com.github.mybatisx.auditable.event.AuditedFilter;
import com.github.mybatisx.auditable.event.AuditedNotFilter;
import com.github.mybatisx.auditable.event.DefaultAuditedEvent;
import com.github.mybatisx.constant.Constants;
import com.github.mybatisx.event.EventPhase;
import org.apache.ibatis.mapping.MappedStatement;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 抽象元数据审计拦截处理器
 * @author wvkity
 * @created 2021-07-16
 * @since 1.0.0
 */
public abstract class AbstractMetadataAuditedHandler extends AbstractAuditedHandler<List<PropertyWrapper>> implements
    MetadataAuditedHandler {

    public static final String PROP_KEY_AUDITED_INTERCEPT_METHODS = "auditedInterceptMethods";
    public static final String PROP_KEY_AUDITED_IGNORE_METHODS = "auditedIgnoreMethods";

    /**
     * 是否启用识别拦截注解
     */
    protected boolean annotationEnable;
    /**
     * 拦截方法列表
     */
    protected Set<String> interceptMethods;
    /**
     * 忽略方法列表
     */
    protected Set<String> ignoreMethods;

    @Override
    protected boolean isEnableReflect() {
        return true;
    }

    @Override
    public boolean canAudited(MappedStatement ms, Object parameter) {
        if (this.isAnnotationPresent(ms, AuditedNotFilter.class)) {
            return false;
        }
        final String execMethod = this.execMethod(ms);
        if (Objects.isNotEmpty(this.ignoreMethods) && this.ignoreMethods.contains(execMethod)) {
            return false;
        }
        return Objects.isEmpty(this.interceptMethods) || this.interceptMethods.contains(execMethod)
            || this.isAnnotationPresent(ms, AuditedFilter.class);
    }

    @Override
    protected AuditedEvent auditedHandle(MappedStatement ms, Object parameter, List<Object> sources,
                                         boolean isInsert, boolean isLogicDelete) {
        final Object first = sources.get(0);
        final List<PropertyWrapper> pws = this.loadProperties(ms, first, isInsert, isLogicDelete);
        if (Objects.isNotEmpty(pws)) {
            if (this.rollbackRestore) {
                final DefaultAuditedEvent event = DefaultAuditedEvent.of(EventPhase.AFTER_ROLLBACK,
                    "metadataAuditedEvent");
                sources.forEach(it -> event.addAll(this.audited(ms, parameter, it, pws)));
                return event;
            } else {
                sources.forEach(it -> this.audited(ms, parameter, it, pws));
            }
        }
        return null;
    }

    @Override
    public void setProperties(Properties properties) {
        super.setProperties(properties);
        if (Objects.isEmpty(this.interceptMethods)) {
            if (Objects.isNull(this.interceptMethods)) {
                this.interceptMethods = new HashSet<>();
            }
            final String aimStr = properties.getProperty(PROP_KEY_AUDITED_INTERCEPT_METHODS);
            if (Objects.isNotBlank(aimStr)) {
                this.interceptMethods.addAll(Arrays.stream(aimStr.split(Constants.COMMA)).filter(Objects::isNotBlank)
                    .map(String::trim).collect(Collectors.toSet()));
            }
        }
        if (Objects.isEmpty(this.ignoreMethods)) {
            if (Objects.isNull(this.ignoreMethods)) {
                this.ignoreMethods = new HashSet<>();
            }
            final String aimStr = properties.getProperty(PROP_KEY_AUDITED_IGNORE_METHODS);
            if (Objects.isNotBlank(aimStr)) {
                this.ignoreMethods.addAll(Arrays.stream(aimStr.split(Constants.COMMA)).filter(Objects::isNotBlank)
                    .map(String::trim).collect(Collectors.toSet()));
            }
        }
    }
}
