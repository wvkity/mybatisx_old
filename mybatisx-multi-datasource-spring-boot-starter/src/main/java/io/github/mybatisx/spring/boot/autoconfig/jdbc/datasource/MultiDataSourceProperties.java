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
package io.github.mybatisx.spring.boot.autoconfig.jdbc.datasource;

import io.github.mybatisx.jdbc.datasource.policy.BalanceDataSourcePolicy;
import io.github.mybatisx.jdbc.datasource.policy.DataSourcePolicy;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.core.Ordered;
import org.springframework.transaction.annotation.Propagation;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * 读写数据源配
 * @author wvkity
 * @created 2021-08-04
 * @since 1.0.0
 */
@ConfigurationProperties(prefix = MultiDataSourceProperties.CFG_PREFIX)
public class MultiDataSourceProperties extends BasicDataSourceProperty {

    public static final String CFG_PREFIX = "spring.datasource.multi";

    /**
     * 是否启用
     */
    private boolean enable = true;
    /**
     * 当之前操作是写的时候，是否强制从从库读 默认（false） 当之前操作是写，默认强制从写库读
     */
    private boolean forceChoiceReadWhenWrite = true;
    /**
     * 排序值
     */
    private int order = Ordered.HIGHEST_PRECEDENCE;
    /**
     * 数据源选择策略
     */
    private Class<? extends DataSourcePolicy> policy = BalanceDataSourcePolicy.class;
    /**
     * 数据源模式
     */
    private DataSourceMode mode = DataSourceMode.LOCAL;
    /**
     * XA事务是否使用强制关闭
     */
    private boolean forceShutdown = true;
    /**
     * JTA事务设置是否允许指定自定义隔离级别
     */
    private boolean allowCustomIsolationLevels = true;
    /**
     * 切入点表达式
     */
    private String pointcutExpression;
    /**
     * 读库事务是否只读
     */
    private boolean readOnly = true;
    /**
     * 事务超时时间
     */
    private int timeout = 5000;
    /**
     * 只读事务传播特性
     */
    private Propagation readPropagation = Propagation.NOT_SUPPORTED;
    /**
     * 回滚规则
     */
    private Set<Class<?>> rollbackRules = new HashSet<>(Collections.singleton(Exception.class));
    /**
     * 只读事务方法
     */
    private Set<String> readOnlyMethods;
    /**
     * 必须事务方法
     */
    private Set<String> requireMethods;
    /**
     * 主库数据源配置
     */
    @NestedConfigurationProperty
    private WriteDataSourceProperties master;
    /**
     * 读库数据源配置
     */
    @NestedConfigurationProperty
    private ReadDataSourceProperties slave;
    /**
     * 分组数据源配置
     */
    @NestedConfigurationProperty
    private GroupedDataSourceProperties group;

    public MultiDataSourceProperties() {
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public boolean isForceChoiceReadWhenWrite() {
        return forceChoiceReadWhenWrite;
    }

    public void setForceChoiceReadWhenWrite(boolean forceChoiceReadWhenWrite) {
        this.forceChoiceReadWhenWrite = forceChoiceReadWhenWrite;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public Class<? extends DataSourcePolicy> getPolicy() {
        return policy;
    }

    public void setPolicy(Class<? extends DataSourcePolicy> policy) {
        this.policy = policy;
    }

    public DataSourceMode getMode() {
        return mode;
    }

    public void setMode(DataSourceMode mode) {
        this.mode = mode;
    }

    public boolean isForceShutdown() {
        return forceShutdown;
    }

    public void setForceShutdown(boolean forceShutdown) {
        this.forceShutdown = forceShutdown;
    }

    public boolean isAllowCustomIsolationLevels() {
        return allowCustomIsolationLevels;
    }

    public void setAllowCustomIsolationLevels(boolean allowCustomIsolationLevels) {
        this.allowCustomIsolationLevels = allowCustomIsolationLevels;
    }

    public String getPointcutExpression() {
        return pointcutExpression;
    }

    public void setPointcutExpression(String pointcutExpression) {
        this.pointcutExpression = pointcutExpression;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public Propagation getReadPropagation() {
        return readPropagation;
    }

    public void setReadPropagation(Propagation readPropagation) {
        this.readPropagation = readPropagation;
    }

    public Set<Class<?>> getRollbackRules() {
        return rollbackRules;
    }

    public void setRollbackRules(Set<Class<?>> rollbackRules) {
        this.rollbackRules = rollbackRules;
    }

    public Set<String> getReadOnlyMethods() {
        return readOnlyMethods;
    }

    public void setReadOnlyMethods(Set<String> readOnlyMethods) {
        this.readOnlyMethods = readOnlyMethods;
    }

    public Set<String> getRequireMethods() {
        return requireMethods;
    }

    public void setRequireMethods(Set<String> requireMethods) {
        this.requireMethods = requireMethods;
    }

    public WriteDataSourceProperties getMaster() {
        return master;
    }

    public void setMaster(WriteDataSourceProperties master) {
        this.master = master;
    }

    public ReadDataSourceProperties getSlave() {
        return slave;
    }

    public void setSlave(ReadDataSourceProperties slave) {
        this.slave = slave;
    }

    public GroupedDataSourceProperties getGroup() {
        return group;
    }

    public void setGroup(GroupedDataSourceProperties group) {
        this.group = group;
    }
}
