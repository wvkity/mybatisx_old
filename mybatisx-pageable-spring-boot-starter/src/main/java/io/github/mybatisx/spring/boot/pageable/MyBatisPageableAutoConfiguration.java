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
package io.github.mybatisx.spring.boot.pageable;

import io.github.mybatisx.plugin.paging.RangePageableInterceptor;
import io.github.mybatisx.spring.boot.pageable.config.MyBatisPageableConfigurer;
import io.github.mybatisx.plugin.paging.StandardPageableInterceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import javax.sql.DataSource;

/**
 * Mybatis分页插件自动注册配置
 * @author wvkity
 * @created 2021-02-08
 * @since 1.0.0
 */
@Configuration
@ConditionalOnSingleCandidate(DataSource.class)
@ConditionalOnClass({SqlSessionFactory.class, SqlSessionFactoryBean.class})
@ConditionalOnProperty(prefix = "github.mybatisx.plugin.pageable", name = "enable", havingValue = "true", matchIfMissing = true)
public class MyBatisPageableAutoConfiguration {

    private final MyBatisPageableConfigurer pageableConfigurer;

    public MyBatisPageableAutoConfiguration(MyBatisPageableConfigurer pageableConfigurer) {
        this.pageableConfigurer = pageableConfigurer;
    }

    @Order(168)
    @Bean
    @ConditionalOnMissingBean
    public RangePageableInterceptor rangePageableInterceptor() {
        final RangePageableInterceptor interceptor = new RangePageableInterceptor();
        interceptor.setProperties(this.pageableConfigurer.getProperties());
        return interceptor;
    }

    @Order(138)
    @Bean
    @ConditionalOnMissingBean
    public StandardPageableInterceptor standardPageableInterceptor() {
        final StandardPageableInterceptor interceptor = new StandardPageableInterceptor();
        interceptor.setProperties(this.pageableConfigurer.getProperties());
        return interceptor;
    }

}
