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
package com.github.mybatisx.jdbc.datasource.aop;

import com.github.mybatisx.Objects;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 读写数据源拦截器
 * @author wvkity
 * @created 2021-08-04
 * @since 1.0.0
 */
public class MultiDataSourceAdvice implements MethodInterceptor {

    private static final Logger log = LoggerFactory.getLogger(MultiDataSourceAdvice.class);
    private final DataSourceDeterminingProcessor processor;

    public MultiDataSourceAdvice(DataSourceDeterminingProcessor processor) {
        this.processor = processor;
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        if (Objects.nonNull(this.processor)) {
            return this.processor.determineDataSource(invocation);
        }
        return invocation.proceed();
    }


}
