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
package io.github.mybatisx.core.convert;

import io.github.mybatisx.Objects;
import io.github.mybatisx.basic.metadata.Column;
import io.github.mybatisx.core.property.PropertiesMappingCache;
import io.github.mybatisx.core.property.Property;
import io.github.mybatisx.exception.MyBatisException;
import io.github.mybatisx.support.criteria.Criteria;
import io.github.mybatisx.support.helper.TableHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 默认属性转换器
 * @author wvkity
 * @created 2021-05-21
 * @since 1.0.0
 */
public class DefaultPropertyConverter implements PropertyConverter {

    private static final long serialVersionUID = 1794162178922854011L;
    private static final Logger log = LoggerFactory.getLogger(DefaultPropertyConverter.class);
    protected final Class<?> entity;
    protected final Criteria<?> criteria;

    public DefaultPropertyConverter(Criteria<?> criteria) {
        this.entity = criteria.getEntityClass();
        this.criteria = criteria;
    }

    /**
     * 是否使用严格模式
     * @return boolean
     */
    protected boolean isStrict() {
        return this.criteria.isStrict();
    }

    /**
     * 方法转属性
     * @param method 方法名
     * @return 属性
     */
    protected String toProperty(final String method) {
        return PropertiesMappingCache.methodToProperty(method);
    }

    @Override
    public Column convert(Property<?, ?> source) {
        return this.convert(this.toProperty(source));
    }

    @Override
    public String toProperty(Property<?, ?> property) {
        return PropertiesMappingCache.lambdaToProperty(property);
    }

    @Override
    public Column convert(String property) {
        if (Objects.isNotBlank(property)) {
            final Column it = PropertiesMappingCache.getColumn(this.entity, property);
            if (it == null) {
                if (this.isStrict()) {
                    throw new MyBatisException("The field mapping information for the entity class(" +
                        this.entity.getName() + ") cannot be found based on the `" + property + "` " +
                        "attribute. Check to see if the attribute exists or is decorated using the @Transient " +
                        "annotation.");
                } else {
                    log.warn("The field mapping information for the entity class({}) cannot be found based on the " +
                        "`{}` " +
                        "attribute. Check to see if the attribute exists or is decorated using the @Transient " +
                        "annotation.", this.entity.getName(), property);
                }
            }
            return it;
        }
        return null;
    }

    @Override
    public Column convertOfOrg(String column) {
        if (Objects.isNotBlank(column)) {
            final Column it = TableHelper.getOrgColumn(this.entity, column);
            if (it == null) {
                if (this.isStrict()) {
                    throw new MyBatisException("The column mapping information for the entity class(" +
                        this.entity.getName() + ") cannot be found based on the `" + column + "` " +
                        ". Check to see if the column exists or if the corresponding property of the entity " +
                        "class is decorated using the @Transient annotation.");
                } else {
                    log.warn("The column mapping information for the entity class(" +
                        this.entity.getName() + ") cannot be found based on the `" + column + "` " +
                        ". Check to see if the column exists or if the corresponding property of the entity " +
                        "class is decorated using the @Transient annotation.");
                }
            }
            return it;
        }
        return null;
    }

    public Class<?> getEntity() {
        return entity;
    }

}
