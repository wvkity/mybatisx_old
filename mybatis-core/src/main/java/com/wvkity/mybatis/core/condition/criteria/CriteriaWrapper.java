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
package com.wvkity.mybatis.core.condition.criteria;

/**
 * 条件接口包装器
 * @param <T>     泛型类
 * @param <Chain> 子类
 * @author wvkity
 * @created 2021-01-05
 * @since 1.0.0
 */
public interface CriteriaWrapper<T, Chain extends CriteriaWrapper<T, Chain>> extends Criteria<T>,
    Compare<T, Chain>, Range<T, Chain>, Fuzzy<T, Chain>, Nullable<T, Chain>, Template<T, Chain>, Nested<Chain>,
    SearchColumn<T> {

    /**
     * 纯SQL条件
     * <p>本方法存在SQL注入风险，谨慎使用，可参考{@link com.wvkity.mybatis.core.condition.expression.StandardTemplate StandardTemplate}
     * 或{@link com.wvkity.mybatis.core.condition.expression.ImmediateTemplate ImmediateTemplate}模板条件表达式实现对应的功能.</p>
     * @param criterion 条件
     * @return {@link Chain}
     */
    Chain nativeCondition(final String criterion);
}
