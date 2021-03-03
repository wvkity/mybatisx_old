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
package com.wvkity.mybatis.core.inject.mapping.sql.insert;

import com.wvkity.mybatis.basic.config.MyBatisGlobalConfiguration;
import com.wvkity.mybatis.basic.metadata.Column;
import com.wvkity.mybatis.basic.metadata.Table;
import com.wvkity.mybatis.support.constant.Operation;
import com.wvkity.mybatis.core.inject.mapping.sql.AbstractSupplier;
import com.wvkity.mybatis.core.inject.mapping.utils.Scripts;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * {@code insertWithNonNull}方法SQL供应器
 * @author wvkity
 * @created 2020-11-08
 * @since 1.0.0
 */
public class InsertWithNonNullSupplier extends AbstractSupplier {

    public InsertWithNonNullSupplier(Table table, MyBatisGlobalConfiguration configuration) {
        super(table, configuration);
    }

    @Override
    public String get() {
        final Set<Column> columns = this.table.insertableColumns();
        String csb = BRACKET_OPEN +
            Scripts.convertToTrimTag(columns.stream().map(it ->
                Scripts.convertToIfTag(PARAM_ENTITY, null, Operation.INSERT, NULL, it, COMMA_SPACE, false, false, null))
                .collect(Collectors.joining(NEW_LINE)), NULL, NULL, NULL, COMMA_SPACE) +
            CLOSE_BRACKET;
        final String vsb = BRACKET_OPEN +
            Scripts.convertToTrimTag(columns.stream().map(it ->
                Scripts.convertToIfTag(PARAM_ENTITY, null, Operation.INSERT, NULL, it, COMMA_SPACE, false, true, null))
                .collect(Collectors.joining(NEW_LINE)), NULL, NULL, NULL, COMMA_SPACE) +
            CLOSE_BRACKET;
        return insert(csb, vsb);
    }
}
