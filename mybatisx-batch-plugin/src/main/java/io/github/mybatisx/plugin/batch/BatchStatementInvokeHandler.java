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
package io.github.mybatisx.plugin.batch;

import io.github.mybatisx.batch.BatchDataWrapper;
import io.github.mybatisx.batch.KeyGeneratorStrategy;
import io.github.mybatisx.plugin.exception.MyBatisPluginException;
import io.github.mybatisx.reflection.MetaObjects;
import org.apache.ibatis.executor.BatchExecutor;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.keygen.Jdbc3KeyGenerator;
import org.apache.ibatis.executor.keygen.KeyGenerator;
import org.apache.ibatis.executor.keygen.SelectKeyGenerator;
import org.apache.ibatis.executor.statement.PreparedStatementHandler;
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;

import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 批量操作处理器
 * @author wvkity
 * @created 2021-02-23
 * @since 1.0.0
 */
public class BatchStatementInvokeHandler extends AbstractBatchHandler {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        final Object target = invocation.getTarget();
        final Method method = invocation.getMethod();
        if (target instanceof RoutingStatementHandler) {
            final RoutingStatementHandler rsh = (RoutingStatementHandler) target;
            final MetaObject rshMeta = MetaObjects.forObject(rsh);
            final Object delegateTarget;
            if (rshMeta.hasGetter(DEF_DELEGATE) && (delegateTarget = rshMeta.getValue(DEF_DELEGATE)) != null
                && delegateTarget instanceof PreparedStatementHandler) {
                final PreparedStatementHandler delegate = (PreparedStatementHandler) delegateTarget;
                final MetaObject pshMeta = MetaObjects.forObject(delegate);
                if (pshMeta.hasGetter(DEF_MAPPED_STATEMENT)) {
                    final MappedStatement ms = (MappedStatement) pshMeta.getValue(DEF_MAPPED_STATEMENT);
                    if (this.filter(ms, null)) {
                        final BoundSql bs = delegate.getBoundSql();
                        final BatchDataWrapper<Object> bdw = this.getBatchData(bs.getParameterObject());
                        if (bdw != null) {
                            final List<Object> entities = bdw.getData();
                            if (entities == null || entities.isEmpty()) {
                                throw new MyBatisPluginException("The data must not be empty.");
                            }
                            final Executor executor = (Executor) pshMeta.getValue(DEF_EXECUTOR);
                            if (this.canExecBatch(executor, method)) {
                                final PreparedStatement ps = (PreparedStatement) invocation.getArgs()[0];
                                final KeyGeneratorStrategy strategy = parse(ms);
                                this.execBatch(ms, ps, bs, bdw, strategy);
                                return bdw.getRowCount();
                            }
                        }
                    }
                }
            }
        }
        return invocation.proceed();
    }

    protected void execBatch(final MappedStatement ms, final PreparedStatement ps, final BoundSql bs,
                           final BatchDataWrapper<Object> bdw, final KeyGeneratorStrategy strategy) throws SQLException {
        ps.clearBatch();
        ps.clearParameters();
        final int realBatchSize = bdw.getBatchSize() <= 0 ? this.defBatchSize : bdw.getBatchSize();
        final List<Object> data = bdw.getData();
        final List<Object> batchParams = new ArrayList<>(data.size());
        for (Object entity: data) {
            if (entity == null) {
                throw new MyBatisPluginException("The data to insert cannot be NULL.");
            }
            final Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("entity", entity);
            paramMap.put("param1", entity);
            final DefaultParameterHandler dph = new DefaultParameterHandler(ms, paramMap, bs);
            dph.setParameters(ps);
            ps.addBatch();
            batchParams.add(entity);
            if (batchParams.size() % realBatchSize == 0) {
                this.execBatch(ms, ps, bdw, batchParams, strategy);
                batchParams.clear();
            }
        }
        if (batchParams.size() % realBatchSize != 0) {
            this.execBatch(ms, ps, bdw, batchParams, strategy);
            batchParams.clear();
        }
        ps.clearBatch();
        ps.clearParameters();
    }

    protected void execBatch(final MappedStatement ms, final PreparedStatement ps, final BatchDataWrapper<Object> bdw,
                           final List<Object> batchParams, final KeyGeneratorStrategy strategy) throws SQLException {
        bdw.addRows(ps.executeBatch());
        if (strategy == KeyGeneratorStrategy.AFTER) {
            final String[] pks = ms.getKeyProperties();
            if (pks != null && pks.length > 0) {
                final String pk = pks[0];
                final ResultSet rs = ps.getGeneratedKeys();
                final List<Object> values = this.handleResult(rs);
                for (int i = 0, size = values.size(); i < size; i++) {
                    this.setValue(batchParams.get(i), pk, values.get(i));
                }
            }
        }
    }

    protected void setValue(Object param, String property, Object value) {
        MetaObject metadata = MetaObjects.forObject(param);
        Class<?> primaryKeyType = metadata.getSetterType(property);
        if (primaryKeyType == Integer.class || primaryKeyType == int.class) {
            metadata.setValue(property, ((Number) value).intValue());
        } else if (primaryKeyType == Long.class || primaryKeyType == long.class) {
            metadata.setValue(property, ((Number) value).longValue());
        } else {
            metadata.setValue(property, value);
        }
    }

    protected List<Object> handleResult(ResultSet resultSet) throws SQLException {
        List<Object> list = new ArrayList<>();
        while (resultSet.next()) {
            list.add(resultSet.getObject(1));
        }
        return list;
    }

    protected boolean canExecBatch(final Executor executor, final Method method) {
        final String methodName = method.getName();
        return DEF_METHOD_UPDATE.equals(methodName)
            || (executor instanceof BatchExecutor && DEF_METHOD_BATCH.equals(methodName));
    }

    protected KeyGeneratorStrategy parse(MappedStatement ms) {
        KeyGenerator keyGenerator = ms.getKeyGenerator();
        if (keyGenerator instanceof SelectKeyGenerator) {
            MetaObject skgMetadata = MetaObjects.forObject(keyGenerator);
            boolean execBefore = (boolean) skgMetadata.getValue(DEF_EXECUTE_BEFORE);
            return execBefore ? KeyGeneratorStrategy.BEFORE : KeyGeneratorStrategy.AFTER;
        } else if (keyGenerator instanceof Jdbc3KeyGenerator) {
            return KeyGeneratorStrategy.AFTER;
        }
        return KeyGeneratorStrategy.NONE;
    }

}
