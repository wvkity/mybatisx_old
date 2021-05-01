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
package com.wvkity.mybatis.core.criteria;

import com.wvkity.mybatis.support.criteria.Criteria;

/**
 * 条件接口包装器
 * @param <T>     泛型类
 * @param <Chain> 子类
 * @author wvkity
 * @created 2021-01-05
 * @since 1.0.0
 */
public interface CriteriaWrapper<T, Chain extends CriteriaWrapper<T, Chain>> extends Criteria<T>,
    CompareWrapper<T, Chain>, RangeWrapper<T, Chain>, FuzzyWrapper<T, Chain>, NullableWrapper<T, Chain>, TemplateWrapper<T, Chain>, NestedWrapper<Chain> {

    /**
     * 纯SQL条件
     * <p>本方法存在SQL注入风险，谨慎使用，可参考{@link com.wvkity.mybatis.core.expr.StandardTemplate StandardTemplate}
     * 或{@link com.wvkity.mybatis.core.expr.ImmediateTemplate ImmediateTemplate}模板条件表达式实现对应的功能.</p>
     * @param condition 条件
     * @return {@link Chain}
     * @see TemplateWrapper
     */
    Chain nativeCondition(final String condition);
}