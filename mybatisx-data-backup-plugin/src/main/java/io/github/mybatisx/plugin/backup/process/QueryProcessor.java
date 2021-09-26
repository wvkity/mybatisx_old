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
package io.github.mybatisx.plugin.backup.process;

import io.github.mybatisx.CommandType;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;

import java.util.List;

/**
 * 查询处理器
 * @author wvkity
 * @created 2021-07-18
 * @since 1.0.0
 */
public interface QueryProcessor {

    /**
     * 构建完整查询语句
     * @param ms          {@link MappedStatement}
     * @param commandType {@link CommandType}
     * @param originalSql 原SQL
     * @param condition   条件部分
     * @param entityClass 元数据类
     * @param targetClass 目标类
     * @return 完整查询语句
     */
    String makeQuerySql(final MappedStatement ms, final CommandType commandType, final String originalSql,
                        final String condition, final Class<?> entityClass, final Class<?> targetClass);

    /**
     * 构建{@link MappedStatement}对象
     * @param ms          {@link MappedStatement}
     * @param commandType {@link CommandType}
     * @param originalSql 原SQL
     * @param condition   条件部分
     * @param sourceClass 元数据类
     * @param targetClass 目标类
     * @return {@link MappedStatement}
     */
    MappedStatement makeMappedStatement(final MappedStatement ms, final CommandType commandType,
                                        final String originalSql, final String condition,
                                        final Class<?> sourceClass, final Class<?> targetClass);

    /**
     * 查询备份数据
     * @param executor      {@link Executor}
     * @param ms            {@link MappedStatement}
     * @param commandType   {@link CommandType}
     * @param originalSql   原SQL
     * @param condition     条件部分
     * @param parameter     方法参数
     * @param entitySources 元数据参数
     * @param sourceClass   元数据类
     * @param targetClass   目标类
     * @return 备份数据
     */
    List<Object> queryList(final Executor executor, final MappedStatement ms, final CommandType commandType,
                           final String originalSql, final String condition, final Object parameter,
                           List<Object> entitySources, final Class<?> sourceClass, final Class<?> targetClass);

}
