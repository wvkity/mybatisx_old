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
package com.wvkity.mybatis.core.inject.mapping.sql;

import com.wvkity.mybatis.basic.constant.Constants;
import com.wvkity.mybatis.basic.metadata.Table;
import com.wvkity.mybatis.core.inject.mapping.utils.Scripts;
import com.wvkity.mybatis.support.condition.criteria.Criteria;
import com.wvkity.mybatis.support.config.MyBatisGlobalConfiguration;

/**
 * 抽象{@link Criteria Criteria}相关SQL供应器
 * @author wvkity
 * @created 2021-02-02
 * @since 1.0.0
 */
public abstract class AbstractCriteriaSupplier extends AbstractLogicDeleteSupplier {

    /**
     * criteria != null and criteria.hasCondition
     */
    protected static final String CRITERIA_HAS_CONDITION_SEGMENT = PARAM_CRITERIA + " != null and "
        + PARAM_CRITERIA + ".hasCondition";
    /**
     * ${criteria.whereSegment}
     */
    protected static final String CRITERIA_WHERE_SEGMENT = Scripts.unsafeJoining(PARAM_CRITERIA,
        Constants.DOT, "whereSegment");

    public AbstractCriteriaSupplier(Table table, MyBatisGlobalConfiguration configuration) {
        super(table, configuration);
    }

    /**
     * 查询条件
     * @return 查询条件
     */
    protected String getSelectCondition() {
        return SPACE + Scripts.convertToIfTag(CRITERIA_WHERE_SEGMENT, CRITERIA_HAS_CONDITION_SEGMENT, true) + NEW_LINE;
    }

    /**
     * 更新条件
     * @return 更新条件
     */
    protected String getUpdateCondition() {
        return SPACE + Scripts.convertToIfTag(CRITERIA_WHERE_SEGMENT, CRITERIA_HAS_CONDITION_SEGMENT, true) + NEW_LINE;
    }
}
