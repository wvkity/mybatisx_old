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
package com.github.mybatisx.core.criteria.sql;

import com.github.mybatisx.basic.constant.Constants;
import com.github.mybatisx.basic.metadata.Column;
import com.github.mybatisx.basic.utils.Objects;
import com.github.mybatisx.core.inject.mapping.utils.Scripts;
import com.github.mybatisx.core.support.manager.StandardFragmentManager;
import com.github.mybatisx.support.constant.Slot;
import com.github.mybatisx.support.constant.Symbol;
import com.github.mybatisx.support.criteria.Criteria;
import com.github.mybatisx.core.convert.ParameterConverter;
import com.github.mybatisx.core.criteria.ExtCriteria;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 默认更新类型SQL管理器
 * @author wvkity
 * @created 2021-06-28
 * @since 1.0.0
 */
public class DefaultUpdateSqlManager extends AbstractSqlManager {

    protected final ParameterConverter parameterConverter;
    protected final Map<Column, Object> updateColumnsOfWrap;
    protected final Map<String, Object> updateColumnsOfOrg;

    public DefaultUpdateSqlManager(ExtCriteria<?> criteria, ParameterConverter parameterConverter,
                                   Map<Column, Object> updateColumnsOfWrap, Map<String, Object> updateColumnsOfOrg,
                                   StandardFragmentManager<? extends Criteria<?>> fragmentManager) {
        super(criteria, fragmentManager);
        this.parameterConverter = parameterConverter;
        this.updateColumnsOfWrap = updateColumnsOfWrap;
        this.updateColumnsOfOrg = updateColumnsOfOrg;
    }

    @Override
    public String getUpdateSegment() {
        final boolean isNotEmptyOfWrap = Objects.isNotEmpty(this.updateColumnsOfWrap);
        final boolean isNotEmptyOfOrg = Objects.isNotEmpty(this.updateColumnsOfOrg);
        if (isNotEmptyOfOrg || isNotEmptyOfWrap) {
            final List<String> segments = new ArrayList<>();
            if (isNotEmptyOfWrap) {
                this.updateColumnsOfWrap.forEach((c, v) -> segments.add(Scripts.convertToConditionArg(Symbol.EQ,
                    Slot.NONE, null, c, this.parameterConverter.convert(v))));
            }
            if (isNotEmptyOfOrg) {
                this.updateColumnsOfOrg.forEach((c, v) -> segments.add(Scripts.convertToConditionArg(Symbol.EQ,
                    Slot.NONE, null, c, this.parameterConverter.convert(v))));
            }
            return String.join(Constants.COMMA_SPACE, segments);
        }
        return Constants.EMPTY;
    }

}
