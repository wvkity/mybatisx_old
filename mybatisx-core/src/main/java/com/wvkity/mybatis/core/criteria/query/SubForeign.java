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
package com.wvkity.mybatis.core.criteria.query;

import com.wvkity.mybatis.basic.constant.Constants;
import com.wvkity.mybatis.basic.utils.Objects;
import com.wvkity.mybatis.core.convert.DefaultConditionConverter;
import com.wvkity.mybatis.core.convert.DefaultParameterConverter;
import com.wvkity.mybatis.core.criteria.ExtCriteria;
import com.wvkity.mybatis.support.constant.Join;

import java.util.concurrent.atomic.AtomicReference;

/**
 * 子外联表查询条件容器
 * @param <T> 实体类型
 * @author wvkity
 * @created 2021-05-21
 * @since 1.0.0
 */
public class SubForeign<T> extends AbstractSubForeignQueryCriteria<T, SubForeign<T>> {

    private static final long serialVersionUID = 6031750774691037687L;

    private SubForeign() {
    }

    public SubForeign(ExtCriteria<?> master, ExtCriteria<T> query, String alias, Join join) {
        this.master = master;
        this.query = query;
        this.join = join;
        this.clone(master.transfer());
        this.tableAliasRef = new AtomicReference<>(Objects.isBlank(alias) ? Constants.EMPTY : alias);
        this.defTableAlias = DEF_TABLE_ALIAS_PREFIX + this.tableAliasSequence.incrementAndGet();
        this.parameterConverter = new DefaultParameterConverter(this.parameterSequence, this.parameterValueMapping);
        this.conditionConverter = new DefaultConditionConverter(this, this.parameterConverter);
    }

    @Override
    protected SubForeign<T> newInstance() {
        final SubForeign<T> it = new SubForeign<>();
        it.join = join;
        it.master = master;
        it.query = query;
        it.depClone(this);
        return it;
    }
}
