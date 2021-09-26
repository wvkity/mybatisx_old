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
package io.github.mybatisx.support.criteria;

import io.github.mybatisx.support.fragment.Fragment;

/**
 * 条件包装接口
 * @author wvkity
 * @created 2021-01-04
 * @since 1.0.0
 */
public interface Criteria<T> extends Fragment {

    /**
     * 获取实体类
     * @return 实体类
     */
    Class<T> getEntityClass();

    /**
     * 获取别名
     * @return 别名
     */
    String as();

    /**
     * 设置是否使用严格模式(属性不匹配是否抛出异常，默认为严格模式)
     * @param throwing 是否抛异常
     * @return {@link Criteria}
     */
    Criteria<T> strict(final boolean throwing);

    /**
     * 是否使用严格模式
     * @return boolean
     */
    boolean isStrict();

    /**
     * 获取条件SQL语句
     * @return 条件语句
     */
    String getWhereSegment();

    /**
     * 获取乐观锁条件值
     * @return 值
     */
    Object getVersionConditionValue();

    /**
     * 设置乐观锁值
     * @param value 乐观锁值
     * @return {@code this}
     */
    Criteria<T> setVersion(final Object value);

    // region Default methods

    /**
     * 获取查询字段语句
     * @return 查询字段语句
     */
    default String getSelectSegment() {
        return null;
    }

    /**
     * 获取分组SQL语句
     * @return 分组语句
     */
    default String getGroupSegment() {
        return null;
    }

    /**
     * 是否使用属性名只作为别名
     * @return boolean
     */
    default boolean isPropAsAlias() {
        return false;
    }

    /**
     * 获取联表查询引用属性
     * @return 属性
     */
    default String getReference() {
        return null;
    }

    /**
     * 是否存在where子句(包含where/group by/having/order by)
     * @return boolean
     */
    default boolean isHasCondition() {
        return false;
    }

    /**
     * 完整SQL语句
     * @return SQL语句
     */
    default String completeString() {
        return null;
    }

    // endregion

}
