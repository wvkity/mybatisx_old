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
package io.github.mybatisx.core.inject.mapping.sql.select;

import io.github.mybatisx.basic.metadata.Column;
import io.github.mybatisx.basic.metadata.Table;
import io.github.mybatisx.core.inject.mapping.sql.AbstractSupplier;
import io.github.mybatisx.support.config.MyBatisGlobalConfiguration;

import java.util.stream.Collectors;

/**
 * {@code selectAll}方法SQL提供器
 * @author wvkity
 * @created 2020-12-25
 * @since 1.0.0
 */
public class SelectAllSupplier extends AbstractSupplier {

    public SelectAllSupplier(Table table, MyBatisGlobalConfiguration configuration) {
        super(table, configuration);
    }

    @Override
    public String get() {
        return this.select(this.table.columns().stream().map(Column::getColumn)
            .collect(Collectors.joining(COMMA_SPACE)), "");
    }
}
