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
package com.github.mybatisx.core.inject.mapping.sql.select;

import com.github.mybatisx.basic.metadata.Table;
import com.github.mybatisx.core.inject.mapping.sql.AbstractSupplier;
import com.github.mybatisx.support.config.MyBatisGlobalConfiguration;

import java.util.Optional;

/**
 * {@code selectTotal}方法SQL提供器
 * @author wvkity
 * @created 2020-12-08
 * @since 1.0.0
 */
public class SelectTotalSupplier extends AbstractSupplier {

    public SelectTotalSupplier(Table table, MyBatisGlobalConfiguration configuration) {
        super(table, configuration);
    }

    @Override
    public String get() {
        return this.select("COUNT(*) RECORDS", Optional.ofNullable(this.table.getMultiTenantColumn()).map(it ->
            "WHERE " + it.getColumn() + " = #{tenant_value}").orElse(EMPTY));
    }
}
