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
package com.github.mybatisx.core.inject.mapping.sql.select;

import com.github.mybatisx.basic.metadata.Column;
import com.github.mybatisx.basic.metadata.Table;
import com.github.mybatisx.support.config.MyBatisGlobalConfiguration;
import com.github.mybatisx.support.constant.Operation;
import com.github.mybatisx.core.inject.mapping.sql.AbstractSupplier;
import com.github.mybatisx.core.inject.mapping.utils.Scripts;

import java.util.Optional;
import java.util.stream.Collectors;

/**
 * {@code selectOne}方法SQL提供器
 * @author wvkity
 * @created 2020-11-09
 * @since 1.0.0
 */
public class SelectOneSupplier extends AbstractSupplier {

    public SelectOneSupplier(Table table, MyBatisGlobalConfiguration configuration) {
        super(table, configuration);
    }

    @Override
    public String get() {
        final StringBuilder condition = new StringBuilder(60);
        // 主键
        if (this.table.isOnlyOneId()) {
            condition.append(SPACE_AND_SPACE).append(Scripts.convertToPartArg(PARAM_ENTITY, Operation.REPLACE,
                this.table.getIdColumn()));
        } else {
            this.table.getIdColumns().forEach(it ->
                condition.append(SPACE_AND_SPACE).append(Scripts.convertToPartArg(PARAM_ENTITY, Operation.REPLACE, it)));
        }
        // 多租户
        Optional.ofNullable(this.table.getMultiTenantColumn()).ifPresent(it ->
            condition.append(SPACE_AND_SPACE).append(Scripts.convertToPartArg(PARAM_ENTITY, Operation.REPLACE, it)));
        return this.select(this.table.columns().stream().map(Column::getColumn).collect(Collectors.joining(COMMA_SPACE)),
            Scripts.convertToTrimTag(condition.toString(), "WHERE", "AND |OR ", NULL, NULL));
    }
}
