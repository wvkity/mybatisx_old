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
package io.github.mybatisx.core.inject.mapping.sql.update;

import io.github.mybatisx.basic.metadata.Table;
import io.github.mybatisx.constant.Constants;
import io.github.mybatisx.core.inject.mapping.sql.AbstractSupplier;
import io.github.mybatisx.core.inject.mapping.utils.Scripts;
import io.github.mybatisx.support.config.MyBatisGlobalConfiguration;
import io.github.mybatisx.support.constant.Operation;

import java.util.stream.Collectors;

/**
 * {@code updateWithSpecial}方法SQL提供器
 * @author wvkity
 * @created 2021-07-30
 * @since 1.0.0
 */
public class UpdateWithSpecialSupplier extends AbstractSupplier {

    public UpdateWithSpecialSupplier(Table table, MyBatisGlobalConfiguration configuration) {
        super(table, configuration);
    }

    @Override
    public String get() {
        // 主键
        final StringBuilder condition = this.addPrimaryKeyCondition();
        // 租户
        this.addMultiTenantCondition(condition);
        String script = table.filtrate(it -> it.isUpdatable() && !it.isMultiTenant()).stream().map(it ->
            SPACE + Scripts.convertToPartArg(Constants.PARAM_ENTITY, Operation.REPLACE, it))
            .collect(Collectors.joining(COMMA + NEW_LINE));
        return update(Scripts.convertToTrimTag(script, "SET", null, null, COMMA_SPACE),
            Scripts.convertToTrimTag(condition.toString(), "WHERE", "AND |OR ", NULL, NULL));
    }
}
