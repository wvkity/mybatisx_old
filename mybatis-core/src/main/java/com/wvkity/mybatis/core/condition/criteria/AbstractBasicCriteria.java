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

import com.wvkity.mybatis.core.condition.basic.SegmentManager;
import com.wvkity.mybatis.core.condition.expression.Equal;
import com.wvkity.mybatis.core.condition.expression.Expression;
import com.wvkity.mybatis.core.condition.expression.GreaterThan;
import com.wvkity.mybatis.core.condition.expression.GreaterThanOrEqual;
import com.wvkity.mybatis.core.condition.expression.IdEqual;
import com.wvkity.mybatis.core.condition.expression.ImmediateEqual;
import com.wvkity.mybatis.core.condition.expression.ImmediateGreaterThan;
import com.wvkity.mybatis.core.condition.expression.ImmediateGreaterThanOrEqual;
import com.wvkity.mybatis.core.condition.expression.ImmediateNotEqual;
import com.wvkity.mybatis.core.condition.expression.NotEqual;
import com.wvkity.mybatis.core.constant.Constants;
import com.wvkity.mybatis.core.constant.Slot;
import com.wvkity.mybatis.core.convert.Property;
import com.wvkity.mybatis.core.utils.Objects;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 抽象条件
 * @param <T>     泛型类型
 * @param <Chain> 子类类型
 * @author wvkity
 * @created 2021-01-04
 * @since 1.0.0
 */
@SuppressWarnings({"serial"})
public abstract class AbstractBasicCriteria<T, Chain extends AbstractBasicCriteria<T, Chain>> implements
    CriteriaWrapper<T, Chain> {

    // region Basic fields

    /**
     * AND、OR运算符正则字符串
     */
    protected static final String DEF_REGEX_AND_OR_STR = "^(?i)(\\s*and\\s+|\\s*or\\s+)(.*)";
    /**
     * AND、OR运算符正则
     */
    protected static final Pattern DEF_PATTERN_AND_OR = Pattern.compile(DEF_REGEX_AND_OR_STR, Pattern.CASE_INSENSITIVE);
    /**
     * 参数前缀
     */
    protected static final String DEF_PARAMETER_KEY_PREFIX = "_v_idx_";
    /**
     * 参数值映射
     */
    protected static final String DEF_PARAMETER_VALUE_MAPPING = "%s.parameterValueMapping.%s";
    /**
     * 参数占位符
     */
    protected static final String DEF_PARAMETER_PLACEHOLDER = "{%s}";
    /**
     * {@link Criteria}默认参数名
     */
    protected static final String DEF_PARAMETER_ALIAS = Constants.PARAM_CRITERIA;
    /**
     * 默认表别名前缀
     */
    protected static final String DEF_TABLE_ALIAS_PREFIX = "_it_";
    /**
     * 当前对象
     */
    @SuppressWarnings("unchecked")
    protected final Chain context = (Chain) this;
    /**
     * 实体类型
     */
    protected Class<T> entityClass;
    /**
     * 参数值映射
     */
    protected Map<String, Object> parameterValueMapping;
    /**
     * SQL片段管理器
     */
    protected SegmentManager segmentManager;
    /**
     * SQL片段
     */
    protected String segment;
    /**
     * 参数序列
     */
    protected AtomicInteger parameterSequence;
    /**
     * 属性不匹配是否抛出异常(查找失败)
     */
    protected AtomicBoolean notMatchingWithThrows;
    /**
     * 表别名序列
     */
    protected AtomicInteger tableAliasSequence;
    /**
     * 表别名
     */
    protected String tableAlias;
    /**
     * 是否使用别名
     */
    protected boolean useAlias;
    /**
     * 默认别名
     */
    protected String defTableAlias;

    // endregion

    /**
     * 初始化方法
     */
    protected void inits() {
        this.parameterValueMapping = new ConcurrentHashMap<>();
        this.parameterSequence = new AtomicInteger(0);
        this.segmentManager = new SegmentManager();
        this.notMatchingWithThrows = new AtomicBoolean(true);
        this.tableAliasSequence = new AtomicInteger(0);
        this.defTableAlias = DEF_TABLE_ALIAS_PREFIX + this.tableAliasSequence.incrementAndGet();
    }

    // region Add criterion methods

    /**
     * 添加条件
     * @param criterion {@link Expression}
     * @return {@link Chain}
     */
    protected Chain add(final Expression criterion) {
        Optional.ofNullable(criterion).ifPresent(it -> this.segmentManager.where(it.setIfNecessary(this)));
        return this.context;
    }

    @Override
    public Chain where(Expression... expressions) {
        if (!Objects.isEmpty(expressions)) {
            final int size = expressions.length;
            if (size == 1) {
                this.add(expressions[0]);
            } else if (size == 2) {
                this.add(expressions[0]).add(expressions[1]);
            } else {
                this.where(Objects.asList(expressions));
            }
        }
        return this.context;
    }

    @Override
    public Chain where(Collection<Expression> expressions) {
        if (Objects.isNotEmpty(expressions)) {
            this.segmentManager.where(expressions.stream().filter(Objects::nonNull).map(it -> it.setIfNecessary(this))
                .collect(Collectors.toList()));
        }
        return this.context;
    }

    // endregion

    // region Compare condition

    @Override
    public Chain idEq(Object value, Slot slot) {
        return add(IdEqual.create().criteria(this).slot(slot).value(value).build());
    }

    @Override
    public <V> Chain propEq(Property<T, V> property, V value, Slot slot) {
        return add(Equal.create().criteria(this).property(property).slot(slot).value(value).build());
    }

    @Override
    public Chain propEq(String property, Object value, Slot slot) {
        return add(Equal.create().criteria(this).property(property).slot(slot).value(value).build());
    }

    @Override
    public Chain colEq(String column, Object value, Slot slot) {
        return add(ImmediateEqual.create().criteria(this).column(column).slot(slot).value(value).build());
    }

    @Override
    public <V> Chain propNe(Property<T, V> property, V value, Slot slot) {
        return add(NotEqual.create().criteria(this).property(property).slot(slot).value(value).build());
    }

    @Override
    public Chain propNe(String property, Object value, Slot slot) {
        return add(NotEqual.create().criteria(this).property(property).slot(slot).value(value).build());
    }

    @Override
    public Chain colNe(String column, Object value, Slot slot) {
        return add(ImmediateNotEqual.create().criteria(this).column(column).slot(slot).value(value).build());
    }

    @Override
    public <V> Chain propGt(Property<T, V> property, V value, Slot slot) {
        return add(GreaterThan.create().criteria(this).property(property).slot(slot).value(value).build());
    }

    @Override
    public Chain propGt(String property, Object value, Slot slot) {
        return add(GreaterThan.create().criteria(this).property(property).slot(slot).value(value).build());
    }

    @Override
    public Chain colGt(String column, Object value, Slot slot) {
        return add(ImmediateGreaterThan.create().criteria(this).column(column).slot(slot).value(value).build());
    }

    @Override
    public <V> Chain propGe(Property<T, V> property, V value, Slot slot) {
        return add(GreaterThanOrEqual.create().criteria(this).property(property).slot(slot).value(value).build());
    }

    @Override
    public Chain propGe(String property, Object value, Slot slot) {
        return add(GreaterThanOrEqual.create().criteria(this).property(property).slot(slot).value(value).build());
    }

    @Override
    public Chain colGe(String column, Object value, Slot slot) {
        return add(ImmediateGreaterThanOrEqual.create().criteria(this).column(column).slot(slot).value(value).build());
    }

    // endregion

    // region Other methods

    @Override
    public Class<T> getEntityClass() {
        return this.entityClass;
    }

    @Override
    public String as() {
        if (this.useAlias) {
            return Objects.isBlank(this.tableAlias) ? this.defTableAlias : this.tableAlias;
        }
        return Constants.EMPTY;
    }

    @Override
    public Chain as(String alias) {
        this.tableAlias = alias;
        return this.context;
    }

    @Override
    public Chain useAlias() {
        return this.useAlias(true);
    }

    @Override
    public Chain useAlias(boolean used) {
        this.useAlias = used;
        return this.context;
    }

    @Override
    public Chain notMatchingWithThrows(boolean matching) {
        this.notMatchingWithThrows.compareAndSet(!matching, matching);
        return this.context;
    }

    @Override
    public String convert(String source, boolean format, Object... args) {
        if (Objects.isBlank(source)) {
            return null;
        }
        if (format && !Objects.isEmpty(args)) {
            final int size = args.length;
            String template = source;
            for (int i = 0; i < size; i++) {
                final String paramName = DEF_PARAMETER_KEY_PREFIX + this.parameterSequence.incrementAndGet();
                template = template.replace(String.format(DEF_PARAMETER_PLACEHOLDER, i),
                    String.format(DEF_PARAMETER_VALUE_MAPPING, DEF_PARAMETER_ALIAS, paramName));
                this.parameterValueMapping.put(paramName, args[i]);
            }
            return template;
        }
        return source;
    }

    @Override
    public List<String> converts(String source, Collection<Object> args) {
        if (Objects.isNotBlank(source) && Objects.isNotEmpty(args)) {
            return args.stream().map(it -> {
                final String paramName = DEF_PARAMETER_KEY_PREFIX + this.parameterSequence.incrementAndGet();
                final Object value = it == null ? "null" : it;
                this.parameterValueMapping.put(paramName, value);
                return source.replace(String.format(DEF_PARAMETER_PLACEHOLDER, 0),
                    String.format(DEF_PARAMETER_VALUE_MAPPING, DEF_PARAMETER_ALIAS, paramName));
            }).collect(Collectors.toList());
        }
        return null;
    }

    @Override
    public String getSegment() {
        return this.segmentManager.getSegment();
    }

    // endregion
}
