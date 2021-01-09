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

import com.wvkity.mybatis.core.convert.PropertiesMappingCache;
import com.wvkity.mybatis.core.convert.Property;
import com.wvkity.mybatis.core.exception.MyBatisException;
import com.wvkity.mybatis.core.invoke.SerializedLambda;
import com.wvkity.mybatis.core.metadata.Column;
import com.wvkity.mybatis.core.utils.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 查询链式条件
 * @param <T>     泛型类型
 * @param <Chain> 子类类型
 * @author wvkity
 * @created 2021-01-04
 * @since 1.0.0
 */
@SuppressWarnings({"serial"})
abstract class AbstractChainCriteria<T, Chain extends AbstractChainCriteria<T, Chain>> extends
    AbstractBasicCriteria<T, Chain> {

    private static final Logger log = LoggerFactory.getLogger(AbstractChainCriteria.class);

    @Override
    public Column findColumn(Property<?, ?> property) {
        return getColumn(PropertiesMappingCache.parse(property));
    }

    @Override
    public Column findColumn(String property) {
        return this.getColumn(property);
    }

    /**
     * 根据{@link SerializedLambda}获取{@link Column}对象
     * @param property {@link SerializedLambda}
     * @return {@link Column}对象
     */
    protected Column getColumn(final SerializedLambda property) {
        return getColumn(methodToProperty(property.getImplMethodName()));
    }

    /**
     * 根据属性获取{@link Column}对象
     * @param property 属性
     * @return {@link Column}对象
     */
    protected Column getColumn(final String property) {
        if (Objects.isBlank(property)) {
            return null;
        }
        final Column column = PropertiesMappingCache.getColumn(this.entityClass, property);
        if (column == null) {
            if (this.notMatchingWithThrows.get()) {
                throw new MyBatisException("The field mapping information for the entity class(" +
                    this.entityClass.getName() + ") cannot be found based on the `" + property + "` " +
                    "attribute. Check to see if the attribute exists or is decorated using the @Transient " +
                    "annotation.");
            } else {
                log.warn("The field mapping information for the entity class({}) cannot be found based on the `{}` " +
                    "attribute. Check to see if the attribute exists or is decorated using the @Transient " +
                    "annotation.", this.entityClass.getName(), property);
            }
        }
        return column;
    }

    @Override
    public String convert(Property<T, ?> property) {
        return methodToProperty(PropertiesMappingCache.parse(property).getImplMethodName());
    }

    @Override
    public <E, V> String methodToProperty(Property<E, V> property) {
        return methodToProperty(PropertiesMappingCache.parse(property).getImplMethodName());
    }

    /**
     * 根据方法名获取属性名
     * @param method 方法名
     * @return 属性名
     */
    protected String methodToProperty(final String method) {
        return PropertiesMappingCache.methodToProperty(method);
    }
}
