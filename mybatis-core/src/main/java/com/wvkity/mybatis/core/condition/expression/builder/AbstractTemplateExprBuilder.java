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
package com.wvkity.mybatis.core.condition.expression.builder;

import com.wvkity.mybatis.core.condition.expression.TemplateMatch;

import java.util.Collection;
import java.util.Map;

/**
 * 抽象模板条件表达式构建器
 * @param <T> 条件表达式类型
 * @param <E> 字段类型
 * @author wvkity
 * @created 2021-01-15
 * @since 1.0.0
 */
public abstract class AbstractTemplateExprBuilder<T, E> extends AbstractExprBuilder<T, E> {

    /**
     * 模板
     */
    protected String template;
    /**
     * 匹配模式
     */
    protected TemplateMatch match;
    /**
     * 列表值
     */
    protected Collection<Object> listValues;
    /**
     * map值
     */
    protected Map<String, Object> mapValues;

    public AbstractTemplateExprBuilder<T, E> template(String template) {
        this.template = template;
        return this;
    }

    public AbstractTemplateExprBuilder<T, E> match(TemplateMatch match) {
        this.match = match;
        return this;
    }

    public AbstractTemplateExprBuilder<T, E> values(Collection<Object> listValues) {
        this.listValues = listValues;
        return this;
    }

    public AbstractTemplateExprBuilder<T, E> values(Map<String, Object> mapValues) {
        this.mapValues = mapValues;
        return this;
    }
}
