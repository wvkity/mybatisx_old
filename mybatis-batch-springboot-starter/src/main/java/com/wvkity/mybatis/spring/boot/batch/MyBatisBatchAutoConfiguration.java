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
package com.wvkity.mybatis.spring.boot.batch;

import com.wvkity.mybatis.core.batch.BatchDataWrapper;
import com.wvkity.mybatis.core.plugin.batch.BatchParameterInterceptor;
import com.wvkity.mybatis.core.plugin.batch.BatchStatementInterceptor;
import com.wvkity.mybatis.spring.boot.batch.config.MyBatisBatchConfigurer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 批量操作拦截器自动注入配置
 * @author wvkity
 * @created 2021-02-23
 * @since 1.0.0
 */
@Configuration
@ConditionalOnClass(BatchDataWrapper.class)
@ConditionalOnProperty(prefix = "wvkity.mybatis.plugin.batch", name = "enable", havingValue = "true", matchIfMissing = true)
public class MyBatisBatchAutoConfiguration {

    private final MyBatisBatchConfigurer batchConfigurer;

    public MyBatisBatchAutoConfiguration(MyBatisBatchConfigurer batchConfigurer) {
        this.batchConfigurer = batchConfigurer;
    }

    @Bean
    @ConditionalOnMissingBean
    public BatchParameterInterceptor batchParameterInterceptor() {
        final BatchParameterInterceptor it = new BatchParameterInterceptor();
        it.setProperties(this.batchConfigurer.getProperties());
        return it;
    }

    @Bean
    @ConditionalOnMissingBean
    public BatchStatementInterceptor batchStatementInterceptor() {
        final BatchStatementInterceptor it = new BatchStatementInterceptor();
        it.setProperties(this.batchConfigurer.getProperties());
        return it;
    }

    public MyBatisBatchConfigurer getBatchConfigurer() {
        return batchConfigurer;
    }
}
