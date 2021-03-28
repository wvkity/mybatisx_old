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
package com.wvkity.mybatis.core.condition.expression;

import com.wvkity.mybatis.basic.utils.Objects;

import java.util.Collection;
import java.util.Map;

/**
 * 抽象模板表达式
 * @param <E> 字段类型
 * @author wvkity
 * @created 2021-01-15
 * @since 1.0.0
 */
public abstract class AbstractTemplateExpression<E> extends AbstractExpression<E> {

    /**
     * 字段默认占位符
     */
    public static final String DEF_PLACEHOLDER_COLUMN = "(?<!\\\\):@";
    /**
     * 模板
     */
    protected String template;
    /**
     * 匹配模式
     */
    protected TemplateMatch match;
    /**
     * 单个值
     */
    protected Object value;
    /**
     * 列表值
     */
    protected Collection<Object> listValues;
    /**
     * map值
     */
    protected Map<String, Object> mapValues;

    /**
     * 检查匹配模式
     */
    public void checkMatch() {
        if (Objects.isNull(this.match)) {
            if (Objects.isNotEmpty(this.mapValues)) {
                this.match = TemplateMatch.MAP;
            } else if (Objects.isNotEmpty(this.listValues)) {
                this.match = TemplateMatch.MULTIPLE;
            } else {
                this.match = TemplateMatch.SINGLE;
            }
        }
    }

    public String getTemplate() {
        return template;
    }

    public TemplateMatch getMatch() {
        return match;
    }

    public Object getValue() {
        return value;
    }

    public Collection<Object> getListValues() {
        return listValues;
    }

    public Map<String, Object> getMapValues() {
        return mapValues;
    }

}
