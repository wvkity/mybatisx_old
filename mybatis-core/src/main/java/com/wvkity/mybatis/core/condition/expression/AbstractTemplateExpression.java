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

import com.wvkity.mybatis.core.constant.Constants;
import com.wvkity.mybatis.core.constant.Slot;
import com.wvkity.mybatis.core.inject.mapping.utils.Scripts;
import com.wvkity.mybatis.core.metadata.Column;
import com.wvkity.mybatis.core.utils.Objects;
import com.wvkity.mybatis.core.utils.Placeholders;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * 抽象模板表达式
 * @param <E> 字段类型
 * @author wvkity
 * @created 2021-01-15
 * @since 1.0.0
 */
@SuppressWarnings({"serial"})
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
    protected void checkMatch() {
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

    @Override
    public String getSegment() {
        if (Objects.isNotBlank(this.template)) {
            this.checkMatch();
            final StringBuilder builder = new StringBuilder(60);
            if (Objects.nonNull(this.slot) && this.slot != Slot.NONE) {
                builder.append(this.slot.getSegment()).append(Constants.SPACE);
            }
            final String alias;
            if (Objects.isNotBlank((alias = this.getAlias()))) {
                builder.append(alias).append(Constants.DOT);
            }
            final String realTemplate;
            if (Objects.nonNull(this.fragment)) {
                if (this.fragment instanceof String) {
                    realTemplate = this.template.replaceAll(DEF_PLACEHOLDER_COLUMN, (String) this.fragment);
                } else if (this.fragment instanceof Column) {
                    realTemplate = this.template.replaceAll(DEF_PLACEHOLDER_COLUMN, ((Column) this.fragment).getColumn());
                } else {
                    realTemplate = this.template;
                }
            } else {
                realTemplate = this.template;
            }
            switch (this.match) {
                case MULTIPLE:
                    builder.append(Placeholders.format(realTemplate, this.listValues.stream().map(it ->
                        Scripts.safeJoining(this.defPlaceholder(it))).collect(Collectors.toList())));
                    break;
                case MAP:
                    builder.append(Placeholders.format(realTemplate, this.mapValues.entrySet().stream()
                        .collect(Collectors.toMap(Map.Entry::getKey,
                            it -> Scripts.safeJoining(this.defPlaceholder(it)),
                            (o, n) -> n, (Supplier<LinkedHashMap<String, Object>>) LinkedHashMap::new))));
                    break;
                default:
                    if (Objects.isNotEmpty(this.listValues)) {
                        builder.append(Placeholders.format(realTemplate, this.listValues.stream().map(it ->
                            Scripts.safeJoining(this.defPlaceholder(it)))
                            .collect(Collectors.joining(Constants.COMMA_SPACE))));
                    } else {
                        builder.append(Placeholders.format(realTemplate,
                            Scripts.safeJoining(this.defPlaceholder(this.value))));
                    }
                    break;
            }
            return builder.toString();
        }
        return null;
    }

    public AbstractTemplateExpression<E> match(TemplateMatch match) {
        this.match = match;
        return this;
    }

    public AbstractTemplateExpression<E> values(Collection<Object> listValues) {
        this.listValues = listValues;
        return this;
    }

    public AbstractTemplateExpression<E> values(Map<String, Object> mapValues) {
        this.mapValues = mapValues;
        return this;
    }
}
