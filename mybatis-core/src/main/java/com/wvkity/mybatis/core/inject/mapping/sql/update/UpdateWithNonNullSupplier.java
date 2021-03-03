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
package com.wvkity.mybatis.core.inject.mapping.sql.update;

import com.wvkity.mybatis.basic.config.MyBatisGlobalConfiguration;
import com.wvkity.mybatis.basic.metadata.Column;
import com.wvkity.mybatis.basic.metadata.Table;
import com.wvkity.mybatis.support.constant.Slot;
import com.wvkity.mybatis.support.constant.Operation;
import com.wvkity.mybatis.support.constant.Symbol;
import com.wvkity.mybatis.core.inject.mapping.sql.AbstractSupplier;
import com.wvkity.mybatis.core.inject.mapping.utils.Scripts;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * {@code updateWithNonNull}方法SQL提供器
 * @author wvkity
 * @created 2020-10-29
 * @since 1.0.0
 */
public class UpdateWithNonNullSupplier extends AbstractSupplier {

    public UpdateWithNonNullSupplier(Table table, MyBatisGlobalConfiguration configuration) {
        super(table, configuration);
    }

    @Override
    public String get() {
        final Set<Column> columns = table.updateColumnsNonWithSpecial();
        final StringBuilder script = new StringBuilder(100);
        script.append(columns.stream().map(it -> Scripts.convertToIfTag(PARAM_ENTITY, Symbol.EQ,
            Operation.REPLACE, NULL, it, COMMA_SPACE, false, true, Slot.NONE)).collect(Collectors.joining(NEW_LINE)));
        // 乐观锁
        final Optional<Column> optional = Optional.ofNullable(table.getOptimisticLockColumn());
        optional.map(this::convertToOptimisticLockIfTag).ifPresent(it -> script.append(NEW_LINE).append(it));
        final StringBuilder condition = new StringBuilder(80);
        if (this.table.isOnlyOneId()) {
            final Column idColumn = this.table.getIdColumn();
            condition.append(SPACE_AND_SPACE).append(Scripts.convertToPartArg(PARAM_ENTITY, Operation.REPLACE, idColumn));
        } else {
            this.table.getIdColumns().forEach(it ->
                condition.append(SPACE_AND_SPACE).append(Scripts.convertToPartArg(PARAM_ENTITY, Operation.REPLACE, it)));
        }
        // 乐观锁
        optional.ifPresent(it ->
            condition.append(SPACE_AND_SPACE).append(Scripts.convertToPartArg(PARAM_ENTITY, Operation.REPLACE, it)));
        // 租户
        Optional.ofNullable(this.table.getTenantColumn()).ifPresent(it ->
            condition.append(SPACE_AND_SPACE).append(Scripts.convertToPartArg(PARAM_ENTITY, Operation.REPLACE, it)));
        return update(Scripts.convertToTrimTag(script.toString(), "SET", null, null, COMMA_SPACE),
            Scripts.convertToTrimTag(condition.toString(), "WHERE", "AND |OR ", NULL, NULL));
    }
}
