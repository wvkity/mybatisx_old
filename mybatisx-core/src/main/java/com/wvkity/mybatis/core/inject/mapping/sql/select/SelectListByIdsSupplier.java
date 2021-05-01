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
package com.wvkity.mybatis.core.inject.mapping.sql.select;

import com.wvkity.mybatis.basic.metadata.Column;
import com.wvkity.mybatis.basic.metadata.Table;
import com.wvkity.mybatis.core.inject.mapping.sql.AbstractSupplier;
import com.wvkity.mybatis.core.inject.mapping.utils.Scripts;
import com.wvkity.mybatis.support.config.MyBatisGlobalConfiguration;

import java.util.stream.Collectors;

/**
 * {@code SelectListByIds}方法SQL提供器
 * @author wvkity
 * @created 2020-12-25
 * @since 1.0.0
 */
public class SelectListByIdsSupplier extends AbstractSupplier {


    public SelectListByIdsSupplier(Table table, MyBatisGlobalConfiguration configuration) {
        super(table, configuration);
    }

    @Override
    public String get() {
        if (this.table.isHasPrimaryKey()) {
            return EMPTY;
        }
        return this.select(this.table.columns().stream().map(Column::getColumn).collect(Collectors.joining(COMMA_SPACE)),
            "WHERE " + this.table.getIdColumn().getColumn() + " IN " + NEW_LINE +
                Scripts.convertToForeachTag(PARAM_IDS, "item", BRACKET_OPEN, BRACKET_CLOSE, COMMA_SPACE, "#{item}"));
    }
}
