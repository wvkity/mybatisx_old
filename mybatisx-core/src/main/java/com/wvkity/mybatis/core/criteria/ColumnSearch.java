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
package com.wvkity.mybatis.core.criteria;

import com.wvkity.mybatis.basic.exception.MyBatisException;
import com.wvkity.mybatis.basic.metadata.Column;
import com.wvkity.mybatis.basic.utils.Objects;
import com.wvkity.mybatis.core.invoke.SerializedLambda;
import com.wvkity.mybatis.core.property.PropertiesMappingCache;
import com.wvkity.mybatis.core.property.Property;
import com.wvkity.mybatis.support.criteria.Criteria;
import com.wvkity.mybatis.support.helper.TableHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 字段查找
 * @author wvkity
 * @created 2021-05-03
 * @since 1.0.0
 */
public class ColumnSearch {

    private static final Logger log = LoggerFactory.getLogger(ColumnSearch.class);
    private final Class<?> entityClass;
    private final Criteria<?> criteria;
    public ColumnSearch(Criteria<?> criteria) {
        this.criteria = criteria;
        this.entityClass = criteria.getEntityClass();
    }

    public boolean isStrict() {
        return this.criteria.isStrict();
    }

    /**
     * 根据{@link Property}查找{@link Column}对象
     * @param property {@link Property}
     * @return {@link Column}对象
     */
    public Column findColumn(final Property<?, ?> property) {
        return getColumn(PropertiesMappingCache.parse(property));
    }

    /**
     * 根据属性名查找{@link Column}对象
     * @param property 属性名
     * @return {@link Column}对象
     */
    public Column findColumn(final String property) {
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
            if (this.isStrict()) {
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

    /**
     * 根据列名获取{@link Column}对象
     * @param column 列名
     * @return {@link Column}
     */
    public Column findOrgColumn(final String column) {
        if (Objects.isBlank(column)) {
            return null;
        }
        final Column col = TableHelper.getOrgColumn(this.entityClass, column);
        if (col == null) {
            if (this.isStrict()) {
                throw new MyBatisException("The column mapping information for the entity class(" +
                    this.entityClass.getName() + ") cannot be found based on the `" + column + "` " +
                    ". Check to see if the column exists or if the corresponding property of the entity " +
                    "class is decorated using the @Transient annotation.");
            } else {
                log.warn("The column mapping information for the entity class(" +
                    this.entityClass.getName() + ") cannot be found based on the `" + column + "` " +
                    ". Check to see if the column exists or if the corresponding property of the entity " +
                    "class is decorated using the @Transient annotation.");
            }
        }
        return col;
    }

    /**
     * lambda属性转成字符串属性
     * @param property 属性
     * @return 属性
     */
    public String convert(Property<?, ?> property) {
        return methodToProperty(PropertiesMappingCache.parse(property).getImplMethodName());
    }

    /**
     * lambda属性转成字符串属性
     * @param properties lambda属性列表
     * @return 属性列表
     */
    public List<String> convert(final List<Property<?, ?>> properties) {
        if (Objects.isNotEmpty(properties)) {
            return properties.stream().filter(Objects::nonNull).map(this::convert).collect(Collectors.toList());
        }
        return new ArrayList<>(0);
    }

    /**
     * 根据方法名获取属性名
     * @param method 方法名
     * @return 属性名
     */
    public String methodToProperty(final String method) {
        return PropertiesMappingCache.methodToProperty(method);
    }
}
