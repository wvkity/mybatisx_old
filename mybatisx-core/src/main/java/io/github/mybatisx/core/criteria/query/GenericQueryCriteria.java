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
package io.github.mybatisx.core.criteria.query;

import io.github.mybatisx.core.criteria.support.GenericCriteriaWrapper;

/**
 * 通用条件/查询接口
 * @param <T> 实体类型
 * @param <C> 子类型
 * @author wvkity
 * @created 2021-05-11
 * @since 1.0.0
 */
public interface GenericQueryCriteria<T, C extends GenericQueryCriteria<T, C>> extends GenericCriteriaWrapper<T, C>,
    CommonQueryCriteria<T, C>, LambdaQueryCriteria<T, C>, OtherSelect<T, C>, MixinWrapper<T, C>, HavingWrapper<T, C> {

}
