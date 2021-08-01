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
package com.github.mybatisx.core.inject.mapping.sql.update;

import com.github.mybatisx.basic.metadata.Column;
import com.github.mybatisx.basic.metadata.Table;
import com.github.mybatisx.core.inject.mapping.sql.AbstractSupplier;
import com.github.mybatisx.core.inject.mapping.utils.Scripts;
import com.github.mybatisx.support.config.MyBatisGlobalConfiguration;
import com.github.mybatisx.support.constant.Operation;
import com.github.mybatisx.support.constant.Slot;
import com.github.mybatisx.support.constant.Symbol;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * {@code updateWithSpecialExcNull}方法SQL提供器
 * @author wvkity
 * @created 2021-08-02
 * @since 1.0.0
 */
public class UpdateWithSpecialExcNullSupplier extends AbstractSupplier {

    public UpdateWithSpecialExcNullSupplier(Table table, MyBatisGlobalConfiguration configuration) {
        super(table, configuration);
    }

    @Override
    public String get() {
        final Set<Column> columns = table.filtrate(it -> it.isUpdatable() && !it.isMultiTenant());
        // 主键
        final StringBuilder condition = this.addPrimaryKeyCondition();
        // 租户
        this.addMultiTenantCondition(condition);
        String script = columns.stream().map(it -> Scripts.convertToIfTag(PARAM_ENTITY, Symbol.EQ,
            Operation.REPLACE, NULL, it, COMMA_SPACE, false, true, Slot.NONE)).collect(Collectors.joining(NEW_LINE));
        return update(Scripts.convertToTrimTag(script, "SET", null, null, COMMA_SPACE),
            Scripts.convertToTrimTag(condition.toString(), "WHERE", "AND |OR ", NULL, NULL));
    }
}
