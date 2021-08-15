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
package com.github.mybatisx.spring.boot.autoconfig.jdbc.datasource;

import com.github.mybatisx.Objects;
import com.github.mybatisx.jdbc.datasource.DataSourceManager;
import com.github.mybatisx.jdbc.datasource.DataSourceNodeBuilder;
import com.github.mybatisx.jdbc.datasource.GenericDataSourceNodeBuilder;
import com.github.mybatisx.jdbc.datasource.MultiDataSourceManager;
import com.github.mybatisx.jdbc.datasource.MultiRoutingDataSource;
import com.github.mybatisx.jdbc.datasource.aop.DataSourceDeterminingProcessor;
import com.github.mybatisx.jdbc.datasource.aop.MultiDataSourceAspectProcessor;
import com.github.mybatisx.jdbc.datasource.aop.MultiDataSourcePointcutAdvisor;
import com.github.mybatisx.jdbc.datasource.policy.BalanceDataSourcePolicy;
import com.github.mybatisx.jdbc.datasource.policy.DataSourcePolicy;
import com.github.mybatisx.jdbc.datasource.resolver.DataSourceRealClassResolver;
import com.github.mybatisx.jdbc.datasource.resolver.ProxyClassResolver;
import com.github.mybatisx.reflect.Reflections;
import com.github.mybatisx.spring.boot.autoconfig.jdbc.datasource.condition.ConditionalOnPropertyNotEmpty;
import com.github.mybatisx.transaction.interceptor.MultiDataSourceTxInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.interceptor.NameMatchTransactionAttributeSource;
import org.springframework.transaction.interceptor.RollbackRuleAttribute;
import org.springframework.transaction.interceptor.RuleBasedTransactionAttribute;
import org.springframework.transaction.interceptor.TransactionAttribute;
import org.springframework.transaction.interceptor.TransactionAttributeSource;
import org.springframework.transaction.interceptor.TransactionInterceptor;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 读写数据源自动注入配置器
 * @author wvkity
 * @created 2021-08-04
 * @since 1.0.0
 */
@Configuration
@AutoConfigureBefore({DataSourceAutoConfiguration.class})
@EnableConfigurationProperties({MultiDataSourceProperties.class})
@ConditionalOnProperty(prefix = MultiDataSourceAutoConfiguration.CFG_PREFIX, name = "enable",
    havingValue = "true", matchIfMissing = true)
public class MultiDataSourceAutoConfiguration {

    private static final Logger log = LoggerFactory.getLogger(MultiDataSourceAutoConfiguration.class);
    public static final String CFG_PREFIX = MultiDataSourceProperties.CFG_PREFIX;
    private static final Set<String> READ_ONLY_METHODS = new HashSet<>(Arrays.asList("get*", "query*", "select*",
        "find*", "exists*", "list*", "count*"));
    private static final Set<String> REQUIRE_METHODS = new HashSet<>(Arrays.asList("save*", "add*", "create*",
        "insert*", "update*", "modify*", "delete*", "del*", "remove*", "drop*", "merge*", "put*", "sync*", "*"));
    private final MultiDataSourceProperties basicProperties;
    private final TransactionProperties txProperties;
    private final DataSourcePropertyNodeManager nodeManager;

    public MultiDataSourceAutoConfiguration(MultiDataSourceProperties properties,
                                            ApplicationContext context) {
        this.basicProperties = properties;
        this.txProperties = properties.getTransaction();
        this.nodeManager = DataSourcePropertyNodeManager.of(context, this.basicProperties);
        this.nodeManager.parse();
    }

    @Bean
    @ConditionalOnMissingBean
    public DataSourceNodeBuilder<DataSource> dataSourceNodeBuilder() {
        return new GenericDataSourceNodeBuilder(this.nodeManager.getNodes());
    }

    @Bean
    @ConditionalOnMissingBean
    public DataSourcePolicy dataSourcePolicy() {
        final Class<? extends DataSourcePolicy> target;
        if (Objects.nonNull((target = this.basicProperties.getPolicy()))) {
            try {
                return Reflections.newInstance(target);
            } catch (Exception ignore) {
                // ignore
            }
        }
        return new BalanceDataSourcePolicy();
    }

    @Bean
    @ConditionalOnMissingBean
    public DataSourceManager dataSourceManager(DataSourceNodeBuilder<DataSource> dataSourceNodeBuilder,
                                               final DataSourcePolicy dataSourcePolicy) {
        return new MultiDataSourceManager(dataSourcePolicy, dataSourceNodeBuilder);
    }

    @Bean
    @ConditionalOnMissingBean
    public DataSource dataSource(final DataSourceManager dataSourceManager) {
        return new MultiRoutingDataSource(dataSourceManager);
    }

    @Bean("txManager")
    @ConditionalOnMissingBean
    public PlatformTransactionManager transactionManager(final DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean("txSource")
    @ConditionalOnMissingBean
    public TransactionAttributeSource transactionAttributeSource() {
        final NameMatchTransactionAttributeSource source = new NameMatchTransactionAttributeSource();
        final Map<String, TransactionAttribute> nameMap = new HashMap<>(32);
        // 只读事务，不做更新操作
        final RuleBasedTransactionAttribute readOnlyTx = new RuleBasedTransactionAttribute();
        readOnlyTx.setReadOnly(this.txProperties.isReadOnly());
        readOnlyTx.setPropagationBehavior(this.txProperties.getReadPropagation().value());
        Set<String> methods = new HashSet<>(READ_ONLY_METHODS);
        final Set<String> readMethods;
        if (Objects.isNotEmpty((readMethods = this.txProperties.getReadOnlyMethods()))) {
            methods.addAll(readMethods);
        }
        for (String method : methods) {
            if (Objects.isNotBlank(method)) {
                nameMap.put(method, readOnlyTx);
            }
        }
        // 当前存在事务就使用当前事务，否则创建一个新的事务
        final RuleBasedTransactionAttribute requiredTx = new RuleBasedTransactionAttribute();
        final Set<Class<?>> rollbackRules;
        if (Objects.isNotNullElement((rollbackRules = this.txProperties.getRollbackRules()))) {
            requiredTx.setRollbackRules(rollbackRules.stream().map(RollbackRuleAttribute::new)
                .collect(Collectors.toList()));
        } else {
            requiredTx.setRollbackRules(Collections.singletonList(new RollbackRuleAttribute(Exception.class)));
        }
        requiredTx.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        final int timeout = this.txProperties.getTimeout();
        if (timeout > 0) {
            requiredTx.setTimeout(timeout);
        }
        methods = new HashSet<>(REQUIRE_METHODS);
        final Set<String> requireMethods;
        if (Objects.isNotEmpty((requireMethods = this.txProperties.getRequireMethods()))) {
            methods.addAll(requireMethods);
        }
        for (String method : methods) {
            if (Objects.isNotBlank(method)) {
                nameMap.put(method, requiredTx);
            }
        }
        source.setNameMap(nameMap);
        return source;
    }

    @Bean("txInterceptor")
    @ConditionalOnMissingBean
    public TransactionInterceptor transactionInterceptor(final PlatformTransactionManager txManager,
                                                         final TransactionAttributeSource txSource) {
        final TransactionInterceptor it = new TransactionInterceptor();
        it.setTransactionManager(txManager);
        it.setTransactionAttributeSource(txSource);
        return it;
    }

    @Bean("txAdvisor")
    @ConditionalOnMissingBean
    @ConditionalOnPropertyNotEmpty(prefix = CFG_PREFIX + ".transaction", name = "pointcut-expression")
    public DefaultPointcutAdvisor defaultPointcutAdvisor(final TransactionInterceptor txInterceptor) {
        final AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression(this.txProperties.getPointcutExpression());
        DefaultPointcutAdvisor it = new DefaultPointcutAdvisor();
        it.setPointcut(pointcut);
        it.setAdvice(txInterceptor);
        return it;
    }

    @Bean
    @ConditionalOnMissingBean
    public ProxyClassResolver proxyClassResolver() {
        return new DataSourceRealClassResolver();
    }

    @Bean
    @ConditionalOnMissingBean
    public DataSourceDeterminingProcessor dataSourceDeterminingProcessor(final TransactionInterceptor txInterceptor,
                                                                         final ProxyClassResolver proxyClassResolver) {
        final MultiDataSourceAspectProcessor it = new MultiDataSourceAspectProcessor();
        it.setForceChoiceReadWhenWrite(this.basicProperties.isForceChoiceReadWhenWrite());
        it.setProxyClassResolver(proxyClassResolver);
        final TransactionAttributeSource tas = txInterceptor.getTransactionAttributeSource();
        if (Objects.nonNull(tas)) {
            it.postProcessAfterInitialization(tas, "txSource");
        }
        return it;
    }

    @Bean
    @ConditionalOnMissingBean
    public MultiDataSourcePointcutAdvisor multiDataSourcePointcutAdvisor(DataSourceDeterminingProcessor processor) {
        final MultiDataSourceTxInterceptor interceptor = new MultiDataSourceTxInterceptor(processor);
        final MultiDataSourcePointcutAdvisor advisor =
            new MultiDataSourcePointcutAdvisor(this.txProperties.getPointcutExpression(), interceptor);
        advisor.setOrder(this.basicProperties.getOrder());
        return advisor;
    }

    public MultiDataSourceProperties getBasicProperties() {
        return basicProperties;
    }
}
