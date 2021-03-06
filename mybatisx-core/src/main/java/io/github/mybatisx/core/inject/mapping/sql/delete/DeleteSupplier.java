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
package io.github.mybatisx.core.inject.mapping.sql.delete;

import io.github.mybatisx.basic.metadata.Table;
import io.github.mybatisx.support.config.MyBatisGlobalConfiguration;
import io.github.mybatisx.support.constant.Slot;
import io.github.mybatisx.support.constant.Operation;
import io.github.mybatisx.support.constant.Symbol;
import io.github.mybatisx.core.inject.mapping.sql.AbstractSupplier;
import io.github.mybatisx.core.inject.mapping.utils.Scripts;

import java.util.stream.Collectors;

/**
 * {@code delete}方法SQL提供器
 * @author wvkity
 * @created 2021-01-03
 * @since 1.0.0
 */
public class DeleteSupplier extends AbstractSupplier {

    public DeleteSupplier(Table table, MyBatisGlobalConfiguration configuration) {
        super(table, configuration);
    }

    @Override
    public String get() {
        return this.delete(Scripts.convertToWhereTag(this.table.columns().stream().map(it ->
            Scripts.convertToIfTag(PARAM_ENTITY, Symbol.EQ, Operation.REPLACE, NULL, it, EMPTY, true, true, Slot.AND))
            .collect(Collectors.joining(NEW_LINE))));
    }
}
