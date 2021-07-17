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
package com.github.mybatisx.core.inject.mapping.sql.update;

import com.github.mybatisx.basic.metadata.Table;
import com.github.mybatisx.constant.Constants;
import com.github.mybatisx.core.inject.mapping.sql.AbstractCriteriaSupplier;
import com.github.mybatisx.support.config.MyBatisGlobalConfiguration;

/**
 * {@code updateByCriteria}方法SQL提供器
 * @author wvkity
 * @created 2021-03-25
 * @since 1.0.0
 */
public class UpdateByCriteriaSupplier extends AbstractCriteriaSupplier {

    public UpdateByCriteriaSupplier(Table table, MyBatisGlobalConfiguration configuration) {
        super(table, configuration);
    }

    @Override
    public String get() {
        return this.update("SET " + Constants.DOLLAR_BRACE_OPEN + Constants.PARAM_CRITERIA + Constants.DOT +
            "updateSegment" + Constants.BRACE_CLOSE, this.getUpdateCondition());
    }
}
