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

import com.wvkity.mybatis.basic.immutable.ImmutableLinkedMap;
import com.wvkity.mybatis.basic.utils.Objects;
import com.wvkity.mybatis.support.constant.Slot;
import com.wvkity.mybatis.core.property.Property;

import java.util.Collection;
import java.util.Map;

/**
 * 模板条件接口
 * <pre>{@code
 *     // For examples:
 *
 *     final Query<T> query = Query.from(T.class);
 *
 *     // No1:
 *     final String template1 = "user_name = ?0 AND pwd = ?1";
 *     query.tpl(template1, "admin", "123456");
 *     // output
 *     // AND user_name = ? AND pwd = ?
 *
 *     // No2:
 *     final String template2 = "user_name = :0 AND pwd = :1;
 *     query.tpl(Slot.OR, template2, "root", "654321");
 *     // output
 *     // OR user_name = ? AND pwd = ?
 *
 *     // No3:
 *     final String template3 = "user_name = :0 AND pwd = :1";
 *     final Map<String, Object> args1 = new LinkedHashMap();
 *     args1.put("0", "root");
 *     args1.put("1", "123456");
 *     query.tpl(template3, args1);
 *     // output
 *     // AND user_name = ? AND pwd = ?
 *
 *     // No4:
 *     final String template4 = "user_name = :userName AND pwd = :password";
 *     final Map<String, Object> args2 = new HashMap();
 *     args2.put("userName", "admin");
 *     args2.put("password", "123456");
 *     query.tpl(template4, args2);
 *     // output
 *     // AND user_name = ? AND pwd = ?
 *
 * }</pre>
 * @param <T>     实体类
 * @param <Chain> 子类
 * @author wvkity
 * @created 2021-01-15
 * @see com.wvkity.mybatis.core.utils.Placeholders
 * @since 1.0.0
 */
public interface TemplateWrapper<T, Chain extends TemplateWrapper<T, Chain>> {

    /**
     * 模板条件
     * @param template 模板
     * @param property 属性
     * @param value    值
     * @return {@link Chain}
     */
    default Chain tpl(final String template, final Property<T, ?> property, final Object value) {
        return tpl(Slot.AND, template, property, value);
    }

    /**
     * 模板条件
     * @param slot     {@link Slot}
     * @param template 模板
     * @param property 属性
     * @param value    值
     * @return {@link Chain}
     */
    Chain tpl(final Slot slot, final String template, final Property<T, ?> property, final Object value);

    /**
     * 模板条件
     * @param template 模板
     * @param property 属性
     * @param value    值
     * @return {@link Chain}
     */
    default Chain tpl(final String template, final String property, final Object value) {
        return tpl(Slot.AND, template, property, value);
    }

    /**
     * 模板条件
     * @param slot     {@link Slot}
     * @param template 模板
     * @param property 属性
     * @param value    值
     * @return {@link Chain}
     */
    Chain tpl(final Slot slot, final String template, final String property, final Object value);

    /**
     * 模板条件
     * @param template 模板
     * @param value    值
     * @return {@link Chain}
     */
    Chain tpl(final String template, final Object value);

    /**
     * 模板条件
     * @param slot     {@link Slot}
     * @param template 模板
     * @param value    值
     * @return {@link Chain}
     */
    Chain tpl(final Slot slot, final String template, final Object value);

    /**
     * 模板条件
     * @param template 模板
     * @param column   字段
     * @param value    值
     * @return {@link Chain}
     */
    default Chain colTpl(final String template, final String column, final Object value) {
        return colTpl(Slot.AND, template, column, value);
    }

    /**
     * 模板条件
     * @param slot     {@link Slot}
     * @param template 模板
     * @param column   字段
     * @param value    值
     * @return {@link Chain}
     */
    Chain colTpl(final Slot slot, final String template, final String column, final Object value);

    /**
     * 模板条件
     * @param template 模板
     * @param property 属性
     * @param values   多个值
     * @return {@link Chain}
     */
    default Chain tpl(final String template, final Property<T, ?> property, final Object... values) {
        return tpl(Slot.AND, template, property, Objects.asList(values));
    }

    /**
     * 模板条件
     * @param slot     {@link Slot}
     * @param template 模板
     * @param property 属性
     * @param values   多个值
     * @return {@link Chain}
     */
    default Chain tpl(final Slot slot, final String template,
                      final Property<T, ?> property, final Object... values) {
        return tpl(slot, template, property, Objects.asList(values));
    }

    /**
     * 模板条件
     * @param template 模板
     * @param property 属性
     * @param values   多个值
     * @return {@link Chain}
     */
    default Chain tpl(final String template, final Property<T, ?> property, final Collection<Object> values) {
        return tpl(Slot.AND, template, property, values);
    }

    /**
     * 模板条件
     * @param slot     {@link Slot}
     * @param template 模板
     * @param property 属性
     * @param values   多个值
     * @return {@link Chain}
     */
    Chain tpl(final Slot slot, final String template, final Property<T, ?> property,
              final Collection<Object> values);

    /**
     * 模板条件
     * @param template 模板
     * @param property 属性
     * @param values   多个值
     * @return {@link Chain}
     */
    default Chain tpl(final String template, final String property, final Object... values) {
        return tpl(Slot.AND, template, property, Objects.asList(values));
    }

    /**
     * 模板条件
     * @param slot     {@link Slot}
     * @param template 模板
     * @param property 属性
     * @param values   多个值
     * @return {@link Chain}
     */
    default Chain tpl(final Slot slot, final String template, final String property, final Object... values) {
        return tpl(slot, template, property, Objects.asList(values));
    }

    /**
     * 模板条件
     * @param template 模板
     * @param property 属性
     * @param values   多个值
     * @return {@link Chain}
     */
    default Chain tpl(final String template, final String property, final Collection<Object> values) {
        return tpl(Slot.AND, template, property, values);
    }

    /**
     * 模板条件
     * @param slot     {@link Slot}
     * @param template 模板
     * @param property 属性
     * @param values   多个值
     * @return {@link Chain}
     */
    Chain tpl(final Slot slot, final String template, final String property, final Collection<Object> values);

    /**
     * 模板条件
     * @param template 模板
     * @param values   多个值
     * @return {@link Chain}
     */
    default Chain tpl(final String template, final Object... values) {
        return tpl(template, Objects.asList(values));
    }

    /**
     * 模板条件
     * @param template 模板
     * @param values   多个值
     * @return {@link Chain}
     */
    Chain tpl(final String template, final Collection<Object> values);

    /**
     * 模板条件
     * @param slot     {@link Slot}
     * @param template 模板
     * @param values   多个值
     * @return {@link Chain}
     */
    default Chain tpl(final Slot slot, final String template, final Object... values) {
        return tpl(slot, template, Objects.asList(values));
    }

    /**
     * 模板条件
     * @param slot     {@link Slot}
     * @param template 模板
     * @param values   多个值
     * @return {@link Chain}
     */
    Chain tpl(final Slot slot, final String template, final Collection<Object> values);

    /**
     * 模板条件
     * @param template 模板
     * @param column   字段
     * @param values   多个值
     * @return {@link Chain}
     */
    default Chain colTpl(final String template, final String column, final Object... values) {
        return colTpl(Slot.AND, template, column, Objects.asList(values));
    }

    /**
     * 模板条件
     * @param slot     {@link Slot}
     * @param template 模板
     * @param column   字段
     * @param values   多个值
     * @return {@link Chain}
     */
    default Chain colTpl(final Slot slot, final String template, final String column, final Object... values) {
        return colTpl(slot, template, column, Objects.asList(values));
    }

    /**
     * 模板条件
     * @param template 模板
     * @param column   字段
     * @param values   多个值
     * @return {@link Chain}
     */
    default Chain colTpl(final String template, final String column, final Collection<Object> values) {
        return colTpl(Slot.AND, template, column, values);
    }

    /**
     * 模板条件
     * @param slot     {@link Slot}
     * @param template 模板
     * @param column   字段
     * @param values   多个值
     * @return {@link Chain}
     */
    Chain colTpl(final Slot slot, final String template, final String column, final Collection<Object> values);

    /**
     * 模板条件
     * @param template 模板
     * @param property 属性
     * @param k1       占位符1
     * @param v1       占位符1对应值
     * @param k2       占位符2
     * @param v2       占位符2对应值
     * @return {@link Chain}
     */
    default Chain tpl(final String template, final Property<T, ?> property, final String k1, final Object v1,
                      final String k2, final Object v2) {
        return tpl(Slot.AND, template, property, ImmutableLinkedMap.of(k1, v1, k2, v2));
    }

    /**
     * 模板条件
     * @param slot     {@link Slot}
     * @param template 模板
     * @param property 属性
     * @param k1       占位符1
     * @param v1       占位符1对应值
     * @param k2       占位符2
     * @param v2       占位符2对应值
     * @return {@link Chain}
     */
    default Chain tpl(final Slot slot, final String template, final Property<T, ?> property, final String k1,
                      final Object v1, final String k2, final Object v2) {
        return tpl(slot, template, property, ImmutableLinkedMap.of(k1, v1, k2, v2));
    }

    /**
     * 模板条件
     * @param template 模板
     * @param property 属性
     * @param k1       占位符1
     * @param v1       占位符1对应值
     * @param k2       占位符2
     * @param v2       占位符2对应值
     * @param k3       占位符3
     * @param v3       占位符3对应值
     * @return {@link Chain}
     */
    default Chain tpl(final String template, final Property<T, ?> property, final String k1, final Object v1,
                      final String k2, final Object v2, final String k3, final Object v3) {
        return tpl(Slot.AND, template, property, ImmutableLinkedMap.of(k1, v1, k2, v2, k3, v3));
    }

    /**
     * 模板条件
     * @param slot     {@link Slot}
     * @param template 模板
     * @param property 属性
     * @param k1       占位符1
     * @param v1       占位符1对应值
     * @param k2       占位符2
     * @param v2       占位符2对应值
     * @param k3       占位符3
     * @param v3       占位符3对应值
     * @return {@link Chain}
     */
    default Chain tpl(final Slot slot, final String template, final Property<T, ?> property, final String k1,
                      final Object v1, final String k2, final Object v2, final String k3, final Object v3) {
        return tpl(slot, template, property, ImmutableLinkedMap.of(k1, v1, k2, v2, k3, v3));
    }

    /**
     * 模板条件
     * @param template 模板
     * @param property 属性
     * @param values   多个值
     * @return {@link Chain}
     */
    default Chain tpl(final String template, final Property<T, ?> property, final Map<String, Object> values) {
        return tpl(Slot.AND, template, property, values);
    }

    /**
     * 模板条件
     * @param slot     {@link Slot}
     * @param template 模板
     * @param property 属性
     * @param values   多个值
     * @return {@link Chain}
     */
    Chain tpl(final Slot slot, final String template, final Property<T, ?> property,
              final Map<String, Object> values);

    /**
     * 模板条件
     * @param template 模板
     * @param property 属性
     * @param k1       占位符1
     * @param v1       占位符1对应值
     * @param k2       占位符2
     * @param v2       占位符2对应值
     * @return {@link Chain}
     */
    default Chain tpl(final String template, final String property, final String k1, final Object v1,
                      final String k2, final Object v2) {
        return tpl(Slot.AND, template, property, ImmutableLinkedMap.of(k1, v1, k2, v2));
    }

    /**
     * 模板条件
     * @param slot     {@link Slot}
     * @param template 模板
     * @param property 属性
     * @param k1       占位符1
     * @param v1       占位符1对应值
     * @param k2       占位符2
     * @param v2       占位符2对应值
     * @return {@link Chain}
     */
    default Chain tpl(final Slot slot, final String template, final String property, final String k1, final Object v1,
                      final String k2, final Object v2) {
        return tpl(slot, template, property, ImmutableLinkedMap.of(k1, v1, k2, v2));
    }

    /**
     * 模板条件
     * @param template 模板
     * @param property 属性
     * @param k1       占位符1
     * @param v1       占位符1对应值
     * @param k2       占位符2
     * @param v2       占位符2对应值
     * @param k3       占位符3
     * @param v3       占位符3对应值
     * @return {@link Chain}
     */
    default Chain tpl(final String template, final String property, final String k1, final Object v1,
                      final String k2, final Object v2, final String k3, final Object v3) {
        return tpl(Slot.AND, template, property, ImmutableLinkedMap.of(k1, v1, k2, v2, k3, v3));
    }

    /**
     * 模板条件
     * @param slot     {@link Slot}
     * @param template 模板
     * @param property 属性
     * @param k1       占位符1
     * @param v1       占位符1对应值
     * @param k2       占位符2
     * @param v2       占位符2对应值
     * @param k3       占位符3
     * @param v3       占位符3对应值
     * @return {@link Chain}
     */
    default Chain tpl(final Slot slot, final String template, final String property, final String k1, final Object v1,
                      final String k2, final Object v2, final String k3, final Object v3) {
        return tpl(slot, template, property, ImmutableLinkedMap.of(k1, v1, k2, v2, k3, v3));
    }

    /**
     * 模板条件
     * @param template 模板
     * @param property 属性
     * @param values   多个值
     * @return {@link Chain}
     */
    default Chain tpl(final String template, final String property, final Map<String, Object> values) {
        return tpl(Slot.AND, template, property, values);
    }

    /**
     * 模板条件
     * @param template 模板
     * @param property 属性
     * @param values   多个值
     * @param slot     {@link Slot}
     * @return {@link Chain}
     */
    Chain tpl(final Slot slot, final String template, final String property, final Map<String, Object> values);

    /**
     * 模板条件
     * @param template 模板
     * @param k1       占位符1
     * @param v1       占位符1对应值
     * @param k2       占位符2
     * @param v2       占位符2对应值
     * @return {@link Chain}
     */
    default Chain tpl(final String template, final String k1, final Object v1, final String k2, final Object v2) {
        return tpl(template, ImmutableLinkedMap.of(k1, v1, k2, v2));
    }

    /**
     * 模板条件
     * @param template 模板
     * @param k1       占位符1
     * @param v1       占位符1对应值
     * @param k2       占位符2
     * @param v2       占位符2对应值
     * @param k3       占位符3
     * @param v3       占位符3对应值
     * @return {@link Chain}
     */
    default Chain tpl(final String template, final String k1, final Object v1,
                      final String k2, final Object v2, final String k3, final Object v3) {
        return tpl(template, ImmutableLinkedMap.of(k1, v1, k2, v2, k3, v3));
    }

    /**
     * 模板条件
     * @param template 模板
     * @param values   多个值
     * @return {@link Chain}
     */
    Chain tpl(final String template, final Map<String, Object> values);

    /**
     * 模板条件
     * @param template 模板
     * @param k1       占位符1
     * @param v1       占位符1对应值
     * @param k2       占位符2
     * @param v2       占位符2对应值
     * @param slot     {@link Slot}
     * @return {@link Chain}
     */
    default Chain tpl(final Slot slot, final String template, final String k1, final Object v1,
                      final String k2, final Object v2) {
        return tpl(slot, template, ImmutableLinkedMap.of(k1, v1, k2, v2));
    }

    /**
     * 模板条件
     * @param slot     {@link Slot}
     * @param template 模板
     * @param k1       占位符1
     * @param v1       占位符1对应值
     * @param k2       占位符2
     * @param v2       占位符2对应值
     * @param k3       占位符3
     * @param v3       占位符3对应值
     * @return {@link Chain}
     */
    default Chain tpl(final Slot slot, final String template, final String k1, final Object v1,
                      final String k2, final Object v2, final String k3, final Object v3) {
        return tpl(slot, template, ImmutableLinkedMap.of(k1, v1, k2, v2, k3, v3));
    }

    /**
     * 模板条件
     * @param slot     {@link Slot}
     * @param template 模板
     * @param values   多个值
     * @return {@link Chain}
     */
    Chain tpl(final Slot slot, final String template, final Map<String, Object> values);

    /**
     * 模板条件
     * @param template 模板
     * @param column   字段
     * @param k1       占位符1
     * @param v1       占位符1对应值
     * @param k2       占位符2
     * @param v2       占位符2对应值
     * @return {@link Chain}
     */
    default Chain colTpl(final String template, final String column, final String k1, final Object v1,
                         final String k2, final Object v2) {
        return colTpl(Slot.AND, template, column, ImmutableLinkedMap.of(k1, v1, k2, v2));
    }

    /**
     * 模板条件
     * @param slot     {@link Slot}
     * @param template 模板
     * @param column   字段
     * @param k1       占位符1
     * @param v1       占位符1对应值
     * @param k2       占位符2
     * @param v2       占位符2对应值
     * @return {@link Chain}
     */
    default Chain colTpl(final Slot slot, final String template, final String column, final String k1, final Object v1,
                         final String k2, final Object v2) {
        return colTpl(slot, template, column, ImmutableLinkedMap.of(k1, v1, k2, v2));
    }

    /**
     * 模板条件
     * @param template 模板
     * @param column   字段
     * @param k1       占位符1
     * @param v1       占位符1对应值
     * @param k2       占位符2
     * @param v2       占位符2对应值
     * @param k3       占位符3
     * @param v3       占位符3对应值
     * @return {@link Chain}
     */
    default Chain colTpl(final String template, final String column, final String k1, final Object v1,
                         final String k2, final Object v2, final String k3, final Object v3) {
        return colTpl(Slot.AND, template, column, ImmutableLinkedMap.of(k1, v1, k2, v2, k3, v3));
    }

    /**
     * 模板条件
     * @param slot     {@link Slot}
     * @param template 模板
     * @param column   字段
     * @param k1       占位符1
     * @param v1       占位符1对应值
     * @param k2       占位符2
     * @param v2       占位符2对应值
     * @param k3       占位符3
     * @param v3       占位符3对应值
     * @return {@link Chain}
     */
    default Chain colTpl(final Slot slot, final String template, final String column, final String k1, final Object v1,
                         final String k2, final Object v2, final String k3, final Object v3) {
        return colTpl(slot, template, column, ImmutableLinkedMap.of(k1, v1, k2, v2, k3, v3));
    }

    /**
     * 模板条件
     * @param template 模板
     * @param column   字段
     * @param values   多个值
     * @return {@link Chain}
     */
    default Chain colTpl(final String template, final String column, final Map<String, Object> values) {
        return colTpl(Slot.AND, template, column, values);
    }

    /**
     * 模板条件
     * @param slot     {@link Slot}
     * @param template 模板
     * @param column   字段
     * @param values   多个值
     * @return {@link Chain}
     */
    Chain colTpl(final Slot slot, final String template, final String column, final Map<String, Object> values);

}
