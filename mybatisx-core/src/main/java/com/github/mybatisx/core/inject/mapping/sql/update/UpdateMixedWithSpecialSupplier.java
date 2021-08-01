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

import com.github.mybatisx.basic.metadata.Table;
import com.github.mybatisx.core.inject.mapping.sql.AbstractCriteriaSupplier;
import com.github.mybatisx.core.inject.mapping.utils.Scripts;
import com.github.mybatisx.support.config.MyBatisGlobalConfiguration;
import com.github.mybatisx.support.constant.Operation;
import com.github.mybatisx.support.constant.Slot;
import com.github.mybatisx.support.constant.Symbol;

import java.util.stream.Collectors;

/**
 * {@code updateMixedWithSpecial}方法SQL提供器
 * @author wvkity
 * @created 2021-08-02
 * @since 1.0.0
 */
public class UpdateMixedWithSpecialSupplier extends AbstractCriteriaSupplier {

    public UpdateMixedWithSpecialSupplier(Table table, MyBatisGlobalConfiguration configuration) {
        super(table, configuration);
    }

    @Override
    public String get() {
        final String setBody = this.table.updatableColumns().stream().map(it ->
            Scripts.convertToIfTag(PARAM_ENTITY, Symbol.EQ, Operation.REPLACE, NULL, it, COMMA_SPACE,
                false, true, Slot.NONE)).collect(Collectors.joining(NEW_LINE));
        return this.update(Scripts.convertToTrimTag(setBody, "SET", null, null, COMMA_SPACE),
            this.getUpdateCondition());
    }
}
