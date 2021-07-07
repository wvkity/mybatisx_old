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
package com.github.mybatisx.plugin.batch;

import com.github.mybatisx.batch.BatchDataWrapper;
import com.github.mybatisx.plugin.handler.AbstractHandler;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 抽象批量处理器
 * @author wvkity
 * @created 2021-02-23
 * @since 1.0.0
 */
public abstract class AbstractBatchHandler extends AbstractHandler {

    public static final String PROP_KEY_BATCH_SIZE = "batchSize";
    public static final String PROP_KEY_BATCH_INTERCEPT_METHODS = "batchInterceptMethods";
    protected static final String DEF_DELEGATE = "delegate";
    protected static final String DEF_BOUND_SQL = "boundSql";
    protected static final String DEF_CONFIGURATION = "configuration";
    protected static final String DEF_MAPPED_STATEMENT = "mappedStatement";
    protected static final String DEF_PARAMETER_OBJECT = "parameterObject";
    protected static final String DEF_EXECUTOR = "executor";
    protected static final String DEF_PARAMETER_BATCH_DATA = BatchDataWrapper.PARAM_BATCH_DATA_WRAPPER;
    protected static final String DEF_METHOD_UPDATE = "update";
    protected static final String DEF_METHOD_BATCH = "batch";
    protected static final String DEF_EXECUTE_BEFORE = "executeBefore";
    protected static final Pattern REGEX_INTEGER = Pattern.compile("0|[1-9]\\d*");
    protected Set<String> batchInterceptMethods;

    /**
     * 批量数目
     */
    protected int defBatchSize;

    @Override
    public boolean filter(final MappedStatement ms, final Object parameter) {
        final SqlCommandType sct = ms.getSqlCommandType();
        return sct == SqlCommandType.INSERT && batchInterceptMethods.contains(this.execMethod(ms));
    }

    @SuppressWarnings("unchecked")
    protected <T> BatchDataWrapper<T> getBatchData(final Object parameter) {
        if (parameter instanceof BatchDataWrapper) {
            return (BatchDataWrapper<T>) parameter;
        } else if (parameter instanceof Map) {
            final Map<String, Object> paramMap = (Map<String, Object>) parameter;
            if (paramMap.containsKey(DEF_PARAMETER_BATCH_DATA)) {
                final Object value = paramMap.get(DEF_PARAMETER_BATCH_DATA);
                if (value instanceof BatchDataWrapper) {
                    return (BatchDataWrapper<T>) value;
                }
            }
            for (Object param : paramMap.values()) {
                if (param instanceof BatchDataWrapper) {
                    return (BatchDataWrapper<T>) param;
                }
            }
        }
        return null;
    }

    @Override
    public void setProperties(Properties properties) {
        super.setProperties(properties);
        this.defBatchSize = Optional.ofNullable(this.properties.getProperty(PROP_KEY_BATCH_SIZE)).filter(it ->
            REGEX_INTEGER.matcher(it).matches()).map(Integer::parseInt).filter(it -> it > 0)
            .orElse(BatchDataWrapper.DEF_BATCH_SIZE);
        final String methodStr = properties.getProperty(PROP_KEY_BATCH_INTERCEPT_METHODS);
        final Set<String> methods = new HashSet<>();
        methods.add("insertBatch");
        methods.add("insertBatchNonAudit");
        if (this.isNotBlank(methodStr)) {
            methods.addAll(Arrays.stream(methodStr.split(",")).filter(this::isNotBlank).map(String::trim)
                .collect(Collectors.toList()));
        }
        this.batchInterceptMethods = Collections.unmodifiableSet(methods);
    }

    private boolean isNotBlank(final String value) {
        return value != null && !value.trim().isEmpty();
    }
}
