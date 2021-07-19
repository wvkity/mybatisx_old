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
package com.github.mybatisx.plugin.paging.dialect;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.sql.SQLException;

/**
 * 数据库分页方言
 * @author wvkity
 * @created 2021-02-07
 * @since 1.0.0
 */
public interface PageableDialect extends Dialect {

    /**
     * 检查是否需要执行查询总记录数
     * @param ms        {@link MappedStatement}
     * @param parameter 方法参数
     * @param rb        {@link RowBounds}
     * @return boolean
     */
    boolean canExecutingQueryRecord(final MappedStatement ms, final Object parameter, final RowBounds rb);

    /**
     * 执行查询总记录数
     * @param executor  {@link Executor}
     * @param ms        {@link MappedStatement}
     * @param parameter 方法参数
     * @param rb        {@link RowBounds}
     * @param bs        {@link BoundSql}
     * @param rh        {@link ResultHandler}
     * @return 总记录数
     * @throws SQLException 执行查询可能出现SQL异常
     */
    Long executingQueryRecord(final Executor executor, final MappedStatement ms, final Object parameter,
                              final RowBounds rb, final BoundSql bs, final ResultHandler<?> rh) throws SQLException;

    /**
     * 查询总记录数后，检查是否需要执行分页查询
     * @param executor  {@link Executor}
     * @param ms        {@link MappedStatement}
     * @param parameter 方法参数
     * @param rb        {@link RowBounds}
     * @param bs        {@link BoundSql}
     * @param rh        {@link ResultHandler}
     * @return 总记录数
     * @throws SQLException 执行查询可能出现SQL异常
     */
    default boolean executingQueryRecordAfter(final Executor executor, final MappedStatement ms,
                                              final Object parameter, final RowBounds rb,
                                              final BoundSql bs, final ResultHandler<?> rh) throws SQLException {
        return this.executingQueryRecordAfter(this.executingQueryRecord(executor, ms, parameter, rb, bs, rh),
            parameter, rb);
    }

    /**
     * 查询总记录数后，检查是否需要执行分页查询
     * @param records   总记录数
     * @param parameter 方法参数
     * @param rb        {@link RowBounds}
     * @return boolean
     */
    boolean executingQueryRecordAfter(final long records, final Object parameter, final RowBounds rb);

}
