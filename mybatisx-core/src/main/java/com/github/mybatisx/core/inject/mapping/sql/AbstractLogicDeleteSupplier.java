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
package com.github.mybatisx.core.inject.mapping.sql;


import com.github.mybatisx.Objects;
import com.github.mybatisx.basic.metadata.Column;
import com.github.mybatisx.basic.metadata.Table;
import com.github.mybatisx.support.config.MyBatisGlobalConfiguration;

import java.util.Set;

/**
 * 抽象逻辑删除SQL提供器
 * @author wvkity
 * @created 2021-02-02
 * @since 1.0.0
 */
public abstract class AbstractLogicDeleteSupplier extends AbstractSupplier {

    public AbstractLogicDeleteSupplier(Table table, MyBatisGlobalConfiguration configuration) {
        super(table, configuration);
    }

    protected String logicDelete(final String condition) {
        if (!this.table.isLogicDelete()) {
            return EMPTY;
        }
        final Set<Column> columns = this.table.logicDeleteAuditColumns();
        if (Objects.isNotEmpty(columns)) {
            return EMPTY;
        }
        return null;
    }
}
