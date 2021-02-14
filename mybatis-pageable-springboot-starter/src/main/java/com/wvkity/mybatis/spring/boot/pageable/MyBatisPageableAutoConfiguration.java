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
package com.wvkity.mybatis.spring.boot.pageable;

import com.wvkity.mybatis.core.plugin.paging.RangePageableInterceptor;
import com.wvkity.mybatis.core.plugin.paging.StandardPageableInterceptor;
import com.wvkity.mybatis.spring.boot.pageable.config.MyBatisPageableConfigurer;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.Order;

import javax.sql.DataSource;

/**
 * Mybatis分页插件自动注册
 * @author wvkity
 * @created 2021-02-08
 * @since 1.0.0
 */
@Lazy
@Order
@Configuration
@ConditionalOnSingleCandidate(DataSource.class)
@ConditionalOnClass({SqlSessionFactory.class, SqlSessionFactoryBean.class})
@ConditionalOnProperty(prefix = "wvkity.mybatis.plugin.pageable", name = "enable", havingValue = "true", matchIfMissing = true)
public class MyBatisPageableAutoConfiguration {

    private final MyBatisPageableConfigurer pageableConfigurer;

    public MyBatisPageableAutoConfiguration(MyBatisPageableConfigurer pageableConfigurer) {
        this.pageableConfigurer = pageableConfigurer;
    }

    @Order
    @Bean
    @ConditionalOnMissingBean
    public RangePageableInterceptor rangePageableInterceptor() {
        final RangePageableInterceptor interceptor = new RangePageableInterceptor();
        interceptor.setProperties(this.pageableConfigurer.getProperties());
        return interceptor;
    }

    @Bean
    @ConditionalOnMissingBean
    public StandardPageableInterceptor standardPageableInterceptor() {
        final StandardPageableInterceptor interceptor = new StandardPageableInterceptor();
        interceptor.setProperties(this.pageableConfigurer.getProperties());
        return interceptor;
    }

}
