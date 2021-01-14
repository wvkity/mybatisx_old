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

import com.wvkity.mybatis.core.condition.criteria.Criteria;
import com.wvkity.mybatis.core.condition.expression.builder.ExprBuilder;
import com.wvkity.mybatis.core.constant.Constants;
import com.wvkity.mybatis.core.constant.Slot;
import com.wvkity.mybatis.core.segment.Fragment;
import com.wvkity.mybatis.core.utils.Objects;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 嵌套条件表达式
 * @author wvkity
 * @created 2021-01-07
 * @since 1.0.0
 */
public class StandardNesting extends AbstractExpression<Expression> {

    private static final long serialVersionUID = -8266095025799360421L;
    private static final String AND_OR_REGEX = "^(?i)(\\s*and\\s+|\\s*or\\s+)(.*)";
    private static final Pattern AND_OR_PATTERN = Pattern.compile(AND_OR_REGEX, Pattern.CASE_INSENSITIVE);
    /**
     * 是否添加NOT
     */
    private boolean not;
    /**
     * 条件表达式集合
     */
    private List<Expression> conditions = new ArrayList<>();

    public StandardNesting(Criteria<?> criteria, boolean not, Slot slot, List<Expression> conditions) {
        this.criteria = criteria;
        this.not = not;
        this.slot = slot;
        this.addAll(conditions);
    }

    /**
     * 添加多个条件表达式
     * @param conditions 多个条件表达式
     * @return {@link StandardNesting}
     */
    public StandardNesting addAll(final Expression... conditions) {
        return this.addAll(Objects.asList(conditions));
    }

    /**
     * 添加多个条件表达式
     * @param conditions 多个条件表达式
     * @return {@link StandardNesting}
     */
    public StandardNesting addAll(final Collection<Expression> conditions) {
        return this.addAll(null, conditions);
    }

    /**
     * 添加多个条件表达式
     * @param criteria   {@link Criteria}
     * @param conditions 多个条件表达式
     * @return {@link StandardNesting}
     */
    public StandardNesting addAll(final Criteria<?> criteria, final Expression... conditions) {
        return this.addAll(criteria, Objects.asList(conditions));
    }

    /**
     * 添加多个条件表达式
     * @param criteria   {@link Criteria}
     * @param conditions 多个条件表达式
     * @return {@link StandardNesting}
     */
    public StandardNesting addAll(final Criteria<?> criteria, final Collection<Expression> conditions) {
        if (Objects.isNotEmpty(conditions)) {
            final Criteria<?> ci = criteria == null ? this.getCriteria() : criteria;
            this.conditions.addAll(conditions.stream().filter(Objects::nonNull).map(it ->
                it.setIfNecessary(ci)).collect(Collectors.toList()));
        }
        return this;
    }

    @Override
    public String getSegment() {
        if (Objects.isNotEmpty(this.conditions)) {
            final StringBuilder builder = new StringBuilder(100);
            if (Objects.nonNull(this.slot)) {
                builder.append(this.slot.getSegment());
            }
            if (this.not) {
                builder.append(Constants.SPACE).append("NOT");
            }
            builder.append(Constants.SPACE).append(Constants.BRACKET_OPEN);
            final String segment = this.conditions.stream().map(Fragment::getSegment).filter(Objects::isNotBlank)
                .collect(Collectors.joining(Constants.SPACE));
            if (AND_OR_PATTERN.matcher(segment).matches()) {
                builder.append(segment.replaceFirst(AND_OR_REGEX, "$2"));
            } else {
                builder.append(segment);
            }
            builder.append(Constants.CLOSE_BRACKET);
            return builder.toString();
        }
        return Constants.EMPTY;
    }

    public StandardNesting not(boolean not) {
        this.not = not;
        return this;
    }

    public StandardNesting conditions(List<Expression> conditions) {
        this.conditions = conditions;
        return this;
    }

    public static StandardNesting.Builder create() {
        return new StandardNesting.Builder();
    }

    public static final class Builder implements ExprBuilder<StandardNesting, Object> {
        /**
         * {@link Criteria}
         */
        private Criteria<?> criteria;
        /**
         * {@link Slot}
         */
        private Slot slot;
        /**
         * 是否添加NOT
         */
        private boolean not = false;
        /**
         * 条件集合
         */
        private List<Expression> conditions;

        private Builder() {
        }

        @Override
        public StandardNesting build() {
            return new StandardNesting(this.criteria, this.not, this.slot, this.conditions);
        }

        public Builder criteria(Criteria<?> criteria) {
            this.criteria = criteria;
            return this;
        }

        public Builder slot(Slot slot) {
            this.slot = slot;
            return this;
        }

        public Builder not(boolean not) {
            this.not = not;
            return this;
        }

        public Builder conditions(List<Expression> conditions) {
            this.conditions = conditions;
            return this;
        }
    }
}
