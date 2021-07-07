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
package com.github.mybatisx.plugin.batch;

import com.github.mybatisx.batch.BatchDataWrapper;
import com.github.mybatisx.plugin.exception.MyBatisPluginException;
import com.github.mybatisx.reflection.MetaObjects;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;

import java.util.List;

/**
 * 批量操作参数设置处理器
 * @author wvkity
 * @created 2021-02-23
 * @since 1.0.0
 */
public class BatchParameterInvokeHandler extends AbstractBatchHandler {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        final Object target = invocation.getTarget();
        if (target instanceof DefaultParameterHandler) {
            final DefaultParameterHandler dph = (DefaultParameterHandler) target;
            final MetaObject metaObject = MetaObjects.forObject(dph);
            if (metaObject.hasGetter(DEF_MAPPED_STATEMENT)) {
                final MappedStatement ms = (MappedStatement) metaObject.getValue(DEF_MAPPED_STATEMENT);
                if (this.filter(ms, null)) {
                    if (metaObject.hasGetter(DEF_PARAMETER_OBJECT)) {
                        final Object parameter = metaObject.getValue(DEF_PARAMETER_OBJECT);
                        final BatchDataWrapper<Object> bdw = this.getBatchData(parameter);
                        if (bdw != null) {
                            final List<Object> entities = bdw.getData();
                            if (entities == null || entities.isEmpty()) {
                                throw new MyBatisPluginException("The data must not be empty.");
                            }
                            return null;
                        }
                    }
                }
            }
        }
        return invocation.proceed();
    }

}
