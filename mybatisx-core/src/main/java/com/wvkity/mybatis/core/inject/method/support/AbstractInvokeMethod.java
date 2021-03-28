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
package com.wvkity.mybatis.core.inject.method.support;

import com.wvkity.mybatis.annotation.Executing;
import com.wvkity.mybatis.basic.keygen.SequenceGenerator;
import com.wvkity.mybatis.basic.metadata.Column;
import com.wvkity.mybatis.basic.metadata.PrimaryKey;
import com.wvkity.mybatis.basic.metadata.Table;
import com.wvkity.mybatis.basic.utils.Objects;
import com.wvkity.mybatis.support.config.MyBatisGlobalConfiguration;
import com.wvkity.mybatis.support.config.MyBatisLocalConfigurationCache;
import com.wvkity.mybatis.support.inject.mapping.sql.Supplier;
import com.wvkity.mybatis.core.inject.mapping.sql.SupplierBuilder;
import com.wvkity.mybatis.core.inject.mapping.sql.SupplierCache;
import com.wvkity.mybatis.session.MyBatisConfiguration;
import org.apache.ibatis.executor.keygen.Jdbc3KeyGenerator;
import org.apache.ibatis.executor.keygen.KeyGenerator;
import org.apache.ibatis.executor.keygen.NoKeyGenerator;
import org.apache.ibatis.executor.keygen.SelectKeyGenerator;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.mapping.StatementType;

import java.util.Optional;


/**
 * 抽象方法
 * @param <T> {@link Supplier}类型
 * @author wvkity
 * @created 2020-10-04
 * @since 1.0.0
 */
public abstract class AbstractInvokeMethod<T extends Supplier> extends AbstractMappedMethod implements SupplierBuilder<T> {

    /**
     * 添加{@link MappedStatement}对象到容器中(保存类型)
     * @param mapperInterface mapper接口
     * @param resultClass     返回值类型
     * @param table           {@link Table}
     * @return {@link MappedStatement}对象
     */
    protected MappedStatement addInsertMappedStatement(final Class<?> mapperInterface,
                                                       final Class<?> resultClass, final Table table) {
        final String statementName = invokeMethod();
        final boolean hasPrimaryKey = !table.getIdColumns().isEmpty();
        final Class<?> entity = table.getEntity();
        final KeyGenerator keyGenerator;
        if (hasPrimaryKey) {
            final boolean onlyOneId = table.isOnlyOneId();
            final Column idColumn = table.getIdColumn();
            // 有且只有一个主键或者主键优先
            if (onlyOneId || idColumn.isPriority()) {
                final String msId = mapperInterface.getName() + "." + invokeMethod();
                keyGenerator = createKeyGenerator(table, msId);
                return addInsertMappedStatement(mapperInterface, statementName,
                    createSqlSource(create(table, getGlobalConfiguration()), entity), entity, keyGenerator,
                    table.getIdProperty(), table.getIdColumn().getColumn());
            }
        }
        keyGenerator = new NoKeyGenerator();
        return addInsertMappedStatement(mapperInterface, statementName,
            createSqlSource(create(table, getGlobalConfiguration()), entity), entity, keyGenerator, null, null);
    }

    /**
     * 添加{@link MappedStatement}对象到容器中(更新类型)
     * @param mapperInterface mapper接口
     * @param resultClass     返回值类型
     * @param table           {@link Table}
     * @return {@link MappedStatement}对象
     */
    protected MappedStatement addUpdateMappedStatement(final Class<?> mapperInterface,
                                                       final Class<?> resultClass, final Table table) {
        final Class<?> entity = table.getEntity();
        return addUpdateMappedStatement(mapperInterface, invokeMethod(),
            createSqlSource(create(table, getGlobalConfiguration()), entity), entity);
    }

    /**
     * 添加{@link MappedStatement}对象到容器中(删除类型)
     * @param mapperInterface mapper接口
     * @param resultClass     返回值类型
     * @param table           {@link Table}
     * @return {@link MappedStatement}对象
     */
    protected MappedStatement addDeleteMappedStatement(final Class<?> mapperInterface,
                                                       final Class<?> resultClass, final Table table) {
        final Class<?> entity = table.getEntity();
        return addDeleteMappedStatement(mapperInterface, invokeMethod(),
            createSqlSource(create(table, getGlobalConfiguration()), entity), entity);
    }

    /**
     * 添加{@link MappedStatement}对象到容器中(查询类型)
     * @param mapperInterface mapper接口
     * @param resultClass     返回值类型
     * @param table           {@link Table}
     * @return {@link MappedStatement}对象
     */
    protected MappedStatement addSelectMappedStatement(final Class<?> mapperInterface,
                                                       final Class<?> resultClass, final Table table) {
        final Class<?> entity = table.getEntity();
        return addSelectMappedStatement(mapperInterface, invokeMethod(),
            createSqlSource(create(table, getGlobalConfiguration()), entity), entity, resultClass);
    }

    /**
     * 主键生成(自增/sequence)
     * @param table {@link Table}
     * @param msId  {@link MappedStatement}唯一标识
     * @return {@link KeyGenerator}
     */
    protected KeyGenerator createKeyGenerator(final Table table, final String msId) {
        KeyGenerator keyGenerator = new NoKeyGenerator();
        final boolean hasPrimaryKey = !table.getIdColumns().isEmpty();
        if (hasPrimaryKey) {
            final Column idColumn = table.getIdColumn();
            final String sequence;
            if (table.isOnlyOneId() || idColumn.isPriority()) {
                final PrimaryKey primaryKey = idColumn.getPrimaryKey();
                if (primaryKey.isIdentity()) {
                    // 自增主键
                    keyGenerator = new Jdbc3KeyGenerator();
                    this.configuration.setUseGeneratedKeys(true);
                } else if (Objects.isNotBlank((sequence = idColumn.getSequence()))) {
                    // 根据SQL获取主键
                    final MyBatisGlobalConfiguration globalConfiguration =
                        MyBatisLocalConfigurationCache.getGlobalConfiguration(this.configuration);
                    final SequenceGenerator generator;
                    if (globalConfiguration != null &&
                        (generator = globalConfiguration.getSequenceGenerator()) != null) {
                        final String sequenceScript = generator.sequenceScript(sequence);
                        final SqlSource sqlSource = this.languageDriver.createSqlSource(this.configuration,
                            sequenceScript, null);
                        this.assistant.addMappedStatement((msId + SelectKeyGenerator.SELECT_KEY_SUFFIX), sqlSource,
                            StatementType.PREPARED, SqlCommandType.SELECT, null, null, null, null, null,
                            idColumn.getJavaType(), null, false, false, false, new NoKeyGenerator(),
                            idColumn.getProperty(), idColumn.getColumn(), null, this.languageDriver, null);
                        final String id = this.assistant.applyCurrentNamespace(msId, false);
                        final MappedStatement ms = this.configuration.getMappedStatement(id, false);
                        keyGenerator = new SelectKeyGenerator(ms, Optional.ofNullable(primaryKey.getExecuting())
                            .map(Executing::isBefore).orElse(false));
                        // useGeneratorKeys = true;
                        this.assistant.getConfiguration().addKeyGenerator(id, keyGenerator);
                        this.assistant.getConfiguration().setUseGeneratedKeys(true);
                    }
                }
            }
        }
        return keyGenerator;
    }

    @SuppressWarnings("unchecked")
    @Override
    public T create(final Object... args) {
        if (this.configuration instanceof MyBatisConfiguration) {
            return (T) ((MyBatisConfiguration) this.configuration).getSupplier(getClass(), args);
        }
        return (T) SupplierCache.newInstance(getClass(), args);
    }
}
