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
package com.github.mybatisx.plugin.handler;

import com.github.mybatisx.plugin.utils.BasicDataTypeRegistry;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Invocation;

/**
 * 抽象更新操作拦截处理器
 * @author wvkity
 * @created 2020-10-25
 * @since 1.0.0
 */
public abstract class AbstractUpdateHandler extends AbstractHandler {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        final Object[] args = invocation.getArgs();
        MappedStatement ms = (MappedStatement) args[0];
        return handle(invocation, ms, args[1]);
    }

    @Override
    public boolean filter(MappedStatement ms, Object parameter) {
        final SqlCommandType command = ms.getSqlCommandType();
        return (command == SqlCommandType.INSERT || command == SqlCommandType.UPDATE) && parameter != null
            && !(BasicDataTypeRegistry.isPrimitiveOrWrapper(parameter) || String.class.equals(parameter));
    }

    /**
     * 拦截处理
     * @param invocation 代理对象信息
     * @param ms         {@link MappedStatement}
     * @param parameter  方法参数
     * @return 方法执行结果
     * @throws Throwable 异常信息
     */
    protected abstract Object handle(Invocation invocation, MappedStatement ms, Object parameter) throws Throwable;
}
