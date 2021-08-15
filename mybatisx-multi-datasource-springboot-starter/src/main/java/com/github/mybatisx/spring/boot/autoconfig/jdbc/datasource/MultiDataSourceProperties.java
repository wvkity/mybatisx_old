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

import com.github.mybatisx.jdbc.datasource.policy.BalanceDataSourcePolicy;
import com.github.mybatisx.jdbc.datasource.policy.DataSourcePolicy;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.core.Ordered;

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
     * 事务配置
     */
    @NestedConfigurationProperty
    private TransactionProperties transaction = new TransactionProperties();
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

    public TransactionProperties getTransaction() {
        return transaction;
    }

    public void setTransaction(TransactionProperties transaction) {
        this.transaction = transaction;
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
