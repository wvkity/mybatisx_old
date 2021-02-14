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
package com.wvkity.mybatis.core.plugin.paging.adapter;

import com.wvkity.mybatis.core.plugin.exception.MyBatisPluginException;
import com.wvkity.mybatis.core.plugin.paging.config.StandardPageableConfig;
import com.wvkity.mybatis.core.plugin.paging.config.StandardPageableThreadLocalCache;
import com.wvkity.mybatis.core.plugin.paging.dialect.PageableDialect;
import com.wvkity.mybatis.core.plugin.paging.proxy.StandardPageableDialectProxy;
import com.wvkity.paging.Pageable;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.sql.SQLException;
import java.util.Optional;
import java.util.Properties;

/**
 * @author wvkity
 * @created 2021-02-07
 * @since 1.0.0
 */
public class StandardPageableAdapter extends RangePageableAdapter implements PageableDialect {

    /**
     * 配置
     */
    private StandardPageableConfig config;

    @Override
    public boolean filter(MappedStatement ms, Object parameter, RowBounds rb) {
        if (ms.getId().endsWith(DEF_PAGEABLE_RECORD_SUFFIX)) {
            throw new MyBatisPluginException("Multiple paging plugins have been found in the current system, " +
                "please check the paging plugin configuration!");
        }
        final Pageable pageable = this.config.getPageable(parameter, rb);
        if (pageable != null) {
            this.proxy.initDialect(ms);
            return true;
        }
        return false;
    }

    @Override
    public boolean canExecutingQueryRecord(MappedStatement ms, Object parameter, RowBounds rb) {
        return ((StandardPageableDialectProxy) this.proxy).getDelegate().canExecutingQueryRecord(ms, parameter, rb);
    }

    @Override
    public Long executingQueryRecord(Executor executor, MappedStatement ms, Object parameter, RowBounds rb,
                                     BoundSql bs, ResultHandler<?> rh) throws SQLException {
        return ((StandardPageableDialectProxy) this.proxy).getDelegate()
            .executingQueryRecord(executor, ms, parameter, rb, bs, rh);
    }

    @Override
    public boolean executingQueryRecordAfter(long records, Object parameter, RowBounds rb) {
        return ((StandardPageableDialectProxy) this.proxy).getDelegate()
            .executingQueryRecordAfter(records, parameter, rb);
    }

    @Override
    public void completed() {
        Optional.ofNullable(this.getDelegate()).ifPresent(it -> {
            it.completed();
            this.proxy.removeDelegate();
        });
        StandardPageableThreadLocalCache.remove();
    }

    @Override
    public void setProperties(Properties properties) {
        this.proxy = new StandardPageableDialectProxy();
        this.proxy.setProperties(properties);
        this.config = new StandardPageableConfig();
        this.config.setProperties(properties);
    }
}
