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

import com.atomikos.icatch.jta.UserTransactionImp;
import com.atomikos.icatch.jta.UserTransactionManager;
import com.atomikos.jdbc.AtomikosDataSourceBean;
import com.github.mybatisx.Objects;
import com.github.mybatisx.jdbc.datasource.DataSourceManager;
import com.github.mybatisx.jdbc.datasource.DataSourceNodeBuilder;
import com.github.mybatisx.jdbc.datasource.LocalDataSourceNodeBuilder;
import com.github.mybatisx.jdbc.datasource.MultiDataSourceManager;
import com.github.mybatisx.jdbc.datasource.MultiRoutingDataSource;
import com.github.mybatisx.jdbc.datasource.XaDataSourceNodeBuilder;
import com.github.mybatisx.jdbc.datasource.aop.AspectResource;
import com.github.mybatisx.jdbc.datasource.aop.MultiDataSourceAdvice;
import com.github.mybatisx.jdbc.datasource.aop.MultiDataSourceAspectResource;
import com.github.mybatisx.jdbc.datasource.aop.MultiDataSourcePointcutAdvisor;
import com.github.mybatisx.jdbc.datasource.policy.BalanceDataSourcePolicy;
import com.github.mybatisx.jdbc.datasource.policy.DataSourcePolicy;
import com.github.mybatisx.jdbc.datasource.resolver.DataSourceRealClassResolver;
import com.github.mybatisx.jdbc.datasource.resolver.ProxyClassResolver;
import com.github.mybatisx.reflect.Reflections;
import com.github.mybatisx.spring.boot.autoconfig.jdbc.datasource.condition.ConditionalOnEnableProperty;
import com.github.mybatisx.spring.boot.autoconfig.jdbc.datasource.condition.ConditionalOnPropertyNotEmpty;
import io.seata.rm.datasource.DataSourceProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.XADataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.XADataSourceWrapper;
import org.springframework.boot.jta.atomikos.AtomikosXADataSourceWrapper;
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
import org.springframework.transaction.jta.JtaTransactionManager;

import javax.sql.DataSource;
import javax.sql.XADataSource;
import javax.transaction.SystemException;
import javax.transaction.TransactionManager;
import javax.transaction.UserTransaction;
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
@ConditionalOnClass({DataSource.class})
@AutoConfigureBefore({XADataSourceAutoConfiguration.class, DataSourceAutoConfiguration.class})
@EnableConfigurationProperties({MultiDataSourceProperties.class})
@ConditionalOnEnableProperty(prefix = MultiDataSourceAutoConfiguration.CFG_PREFIX, havingValue = "true",
    matchIfMissing = true)
public class MultiDataSourceAutoConfiguration {

    private static final Logger log = LoggerFactory.getLogger(MultiDataSourceAutoConfiguration.class);
    public static final String CFG_PREFIX = MultiDataSourceProperties.CFG_PREFIX;
    private static final Set<String> READ_ONLY_METHODS = new HashSet<>(Arrays.asList("get*", "query*", "select*",
        "find*", "exists*", "list*", "count*"));
    private static final Set<String> REQUIRE_METHODS = new HashSet<>(Arrays.asList("save*", "add*", "create*",
        "insert*", "update*", "modify*", "delete*", "del*", "remove*", "drop*", "merge*", "put*", "sync*", "*"));
    private final MultiDataSourceProperties properties;
    private final DataSourcePropertyNodeManager nodeManager;

    public MultiDataSourceAutoConfiguration(MultiDataSourceProperties properties,
                                            ApplicationContext context) {
        this.properties = properties;
        this.nodeManager = DataSourcePropertyNodeManager.of(context, this.properties);
        this.nodeManager.parse();
    }

    @Bean
    @ConditionalOnMissingBean
    public DataSourcePolicy dataSourcePolicy() {
        final Class<? extends DataSourcePolicy> target;
        if (Objects.nonNull((target = this.properties.getPolicy()))) {
            try {
                return Reflections.newInstance(target);
            } catch (Exception ignore) {
                // ignore
            }
        }
        return new BalanceDataSourcePolicy();
    }

    @Bean("txSource")
    @ConditionalOnMissingBean
    public TransactionAttributeSource transactionAttributeSource() {
        final NameMatchTransactionAttributeSource source = new NameMatchTransactionAttributeSource();
        final Map<String, TransactionAttribute> nameMap = new HashMap<>(32);
        // 只读事务，不做更新操作
        final RuleBasedTransactionAttribute readOnlyTx = new RuleBasedTransactionAttribute();
        readOnlyTx.setReadOnly(this.properties.isReadOnly());
        readOnlyTx.setPropagationBehavior(this.properties.getReadPropagation().value());
        Set<String> methods = new HashSet<>(READ_ONLY_METHODS);
        final Set<String> readMethods;
        if (Objects.isNotEmpty((readMethods = this.properties.getReadOnlyMethods()))) {
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
        if (Objects.isNotNullElement((rollbackRules = this.properties.getRollbackRules()))) {
            requiredTx.setRollbackRules(rollbackRules.stream().map(RollbackRuleAttribute::new)
                .collect(Collectors.toList()));
        } else {
            requiredTx.setRollbackRules(Collections.singletonList(new RollbackRuleAttribute(Exception.class)));
        }
        requiredTx.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        final int timeout = this.properties.getTimeout();
        if (timeout > 0) {
            requiredTx.setTimeout(timeout);
        }
        methods = new HashSet<>(REQUIRE_METHODS);
        final Set<String> requireMethods;
        if (Objects.isNotEmpty((requireMethods = this.properties.getRequireMethods()))) {
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

    @Bean
    @ConditionalOnMissingBean
    public AspectResource aspectResource() {
        final AspectResource aspectResource = new MultiDataSourceAspectResource();
        aspectResource.setForceChoiceReadWhenWrite(this.properties.isForceChoiceReadWhenWrite());
        return aspectResource;
    }

    @Bean
    @ConditionalOnMissingBean
    public ProxyClassResolver proxyClassResolver() {
        return new DataSourceRealClassResolver();
    }

    @Bean
    @ConditionalOnMissingBean
    public MultiDataSourceAdvice multiDataSourceAdvice(final ProxyClassResolver proxyClassResolver,
                                                       final AspectResource aspectResource) {
        return new MultiDataSourceAdvice(proxyClassResolver, aspectResource);
    }

    @Bean("txAspectAdvisor")
    @ConditionalOnMissingBean
    public MultiDataSourcePointcutAdvisor multiDataSourcePointcutAdvisor(final MultiDataSourceAdvice advice) {
        final MultiDataSourcePointcutAdvisor advisor =
            new MultiDataSourcePointcutAdvisor(this.properties.getPointcutExpression(), advice);
        advisor.setOrder(this.properties.getOrder());
        return advisor;
    }


    @Configuration
    @ConditionalOnProperty(prefix = CFG_PREFIX, name = "mode", havingValue = "LOCAL", matchIfMissing = true)
    @ConditionalOnEnableProperty(prefix = CFG_PREFIX, havingValue = "true", matchIfMissing = true)
    class LocalDataSourceConfiguration {

        @Bean
        @ConditionalOnMissingBean
        public DataSourceNodeBuilder<DataSource> dataSourceNodeBuilder() {
            return new LocalDataSourceNodeBuilder(nodeManager.getNodes());
        }

        @Bean
        @ConditionalOnMissingBean
        public DataSourceManager dataSourceManager(ObjectProvider<DataSourceNodeBuilder<DataSource>> dsNodeBuilderProvider,
                                                   final ObjectProvider<DataSourcePolicy> dsPolicyProvider) {
            return new MultiDataSourceManager(dsPolicyProvider.getIfAvailable(),
                dsNodeBuilderProvider.getIfAvailable());
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

        @Bean("txInterceptor")
        @ConditionalOnMissingBean
        public TransactionInterceptor transactionInterceptor(@Qualifier("txManager") final PlatformTransactionManager txManager,
                                                             @Qualifier("txSource") final ObjectProvider<TransactionAttributeSource> txSourceProvider) {
            final TransactionInterceptor it = new TransactionInterceptor();
            it.setTransactionManager(txManager);
            it.setTransactionAttributeSource(txSourceProvider.getIfAvailable());
            return it;
        }

        @Bean("txAdvisor")
        @ConditionalOnMissingBean
        @ConditionalOnPropertyNotEmpty(prefix = CFG_PREFIX, name = "pointcut-expression")
        public DefaultPointcutAdvisor defaultPointcutAdvisor(final TransactionInterceptor txInterceptor) {
            final AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
            pointcut.setExpression(properties.getPointcutExpression());
            DefaultPointcutAdvisor it = new DefaultPointcutAdvisor();
            it.setPointcut(pointcut);
            it.setAdvice(txInterceptor);
            return it;
        }
    }

    @Configuration
    @ConditionalOnClass({XADataSource.class})
    @ConditionalOnProperty(prefix = CFG_PREFIX, name = "mode", havingValue = "XA")
    @ConditionalOnEnableProperty(prefix = CFG_PREFIX, havingValue = "true", matchIfMissing = true)
    class XaDistributedDataSourceConfiguration {

        @Bean
        @ConditionalOnMissingBean
        @ConditionalOnClass({AtomikosDataSourceBean.class})
        public XADataSourceWrapper xaDataSourceWrapper() {
            return new AtomikosXADataSourceWrapper();
        }

        @Bean
        @ConditionalOnMissingBean
        public DataSourceNodeBuilder<DataSource> dataSourceNodeBuilder(final ObjectProvider<XADataSourceWrapper> xaDataSourceWrapperProvider) {
            return new XaDataSourceNodeBuilder(xaDataSourceWrapperProvider.getIfAvailable(), nodeManager.getNodes());
        }

        @Bean
        @ConditionalOnMissingBean
        public DataSourceManager dataSourceManager(ObjectProvider<DataSourceNodeBuilder<DataSource>> dsNodeBuilderProvider,
                                                   final ObjectProvider<DataSourcePolicy> dsPolicyProvider) {
            return new MultiDataSourceManager(dsPolicyProvider.getIfAvailable(),
                dsNodeBuilderProvider.getIfAvailable());
        }

        @Bean
        @ConditionalOnMissingBean
        public DataSource dataSource(final DataSourceManager dataSourceManager) {
            return new MultiRoutingDataSource(dataSourceManager);
        }

        @Bean("jtaUserTransactionManager")
        @ConditionalOnMissingBean
        @ConditionalOnClass({UserTransactionManager.class})
        public TransactionManager userTransactionManager() {
            UserTransactionManager userTransactionManager = new UserTransactionManager();
            userTransactionManager.setForceShutdown(properties.isForceShutdown());
            return userTransactionManager;
        }

        @Bean("jtaUserTransaction")
        @ConditionalOnMissingBean
        @ConditionalOnClass({UserTransactionImp.class})
        public UserTransaction userTransactionImp() throws SystemException {
            UserTransactionImp userTransactionImp = new UserTransactionImp();
            userTransactionImp.setTransactionTimeout(properties.getTimeout());
            return userTransactionImp;
        }

        @Bean("txManager")
        @ConditionalOnMissingBean
        public JtaTransactionManager jtaTransactionManager(@Qualifier("jtaUserTransactionManager") final TransactionManager transactionManager,
                                                           @Qualifier("jtaUserTransaction") final UserTransaction userTransaction) {
            final JtaTransactionManager jtaTransactionManager =
                new JtaTransactionManager(userTransaction, transactionManager);
            jtaTransactionManager.setAllowCustomIsolationLevels(properties.isAllowCustomIsolationLevels());
            return jtaTransactionManager;
        }

        @Bean("txInterceptor")
        @ConditionalOnMissingBean
        public TransactionInterceptor transactionInterceptor(@Qualifier("txManager") final PlatformTransactionManager txManager,
                                                             @Qualifier("txSource") final ObjectProvider<TransactionAttributeSource> txSourceProvider) {
            final TransactionInterceptor it = new TransactionInterceptor();
            it.setTransactionManager(txManager);
            it.setTransactionAttributeSource(txSourceProvider.getIfAvailable());
            return it;
        }

        @Bean("txAdvisor")
        @ConditionalOnMissingBean
        @ConditionalOnPropertyNotEmpty(prefix = CFG_PREFIX, name = "pointcut-expression")
        public DefaultPointcutAdvisor defaultPointcutAdvisor(final TransactionInterceptor txInterceptor) {
            final AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
            pointcut.setExpression(properties.getPointcutExpression());
            DefaultPointcutAdvisor it = new DefaultPointcutAdvisor();
            it.setPointcut(pointcut);
            it.setAdvice(txInterceptor);
            return it;
        }

    }

    @Configuration
    @ConditionalOnClass(DataSourceProxy.class)
    @ConditionalOnProperty(prefix = CFG_PREFIX, name = "mode", havingValue = "SEATA")
    @ConditionalOnEnableProperty(prefix = CFG_PREFIX, havingValue = "true", matchIfMissing = true)
    class SeataDistributeDataSourceConfiguration {
    }

}
