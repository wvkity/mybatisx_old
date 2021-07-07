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
package com.github.mybatisx.auditable;

import com.github.mybatisx.basic.constant.Constants;
import com.github.mybatisx.basic.metadata.Table;
import com.github.mybatisx.basic.reflect.Reflections;
import com.github.mybatisx.support.criteria.Criteria;
import com.github.mybatisx.support.helper.TableHelper;
import org.apache.ibatis.mapping.MappedStatement;

import java.util.Map;

/**
 * 源数据解析器
 * @author wvkity
 * @created 2021-03-14
 * @since 1.0.0
 */
public class MetadataParser {

    public MetadataParser() {
    }

    /**
     * 根据参数解析{@link Table}对象
     * @param ms     {@link MappedStatement}
     * @param target 参数
     * @return {@link Table}
     */
    @SuppressWarnings("unchecked")
    public Table parse(final MappedStatement ms, final Object target) {
        final Class<?> entityClass;
        if (target instanceof Map) {
            final Map<String, Object> paramMap = (Map<String, Object>) target;
            if (paramMap.containsKey(Constants.PARAM_ENTITY)) {
                final Object value = paramMap.get(Constants.PARAM_ENTITY);
                if (value != null && !Object.class.equals((entityClass = value.getClass()))
                    && !Reflections.isSimpleJavaType(entityClass)) {
                    return TableHelper.getTable(entityClass);
                }
            }
            if (paramMap.containsKey(Constants.PARAM_CRITERIA)) {
                final Object value = paramMap.get(Constants.PARAM_CRITERIA);
                if (value instanceof Criteria) {
                    return TableHelper.getTable(((Criteria<?>) value).getEntityClass());
                }
            }
        } else if (target != null && !Object.class.equals((entityClass = target.getClass()))
            && !Reflections.isSimpleJavaType(entityClass)) {
            return TableHelper.getTable(entityClass);
        }
        return null;
    }
}
