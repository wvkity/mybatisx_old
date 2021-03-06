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
package io.github.mybatisx.core.inject.mapping.sql;

import io.github.mybatisx.Objects;
import io.github.mybatisx.basic.metadata.Column;
import io.github.mybatisx.basic.metadata.Table;
import io.github.mybatisx.constant.Constants;
import io.github.mybatisx.core.inject.mapping.utils.Scripts;
import io.github.mybatisx.support.config.MyBatisGlobalConfiguration;
import io.github.mybatisx.support.constant.Operation;
import io.github.mybatisx.support.criteria.Criteria;
import io.github.mybatisx.support.inject.mapping.sql.Supplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

/**
 * 抽象SQL语句供应器
 * @author wvkity
 * @created 2020-10-21
 * @since 1.0.0
 */
public abstract class AbstractSupplier implements Supplier, Constants {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    /**
     * {@link Table}
     */
    protected final Table table;
    /**
     * 实体类
     */
    protected final Class<?> entity;
    /**
     * 全局配置
     */
    protected final MyBatisGlobalConfiguration configuration;

    public AbstractSupplier(Table table, MyBatisGlobalConfiguration configuration) {
        this.table = table;
        this.entity = table.getEntity();
        this.configuration = configuration;
    }

    /**
     * 拼接完整插入SQL语句
     * @param columnSegment 字段部分
     * @param valueSegment  值部分
     * @return 完整的插入SQL语句
     */
    public String insert(final String columnSegment, final String valueSegment) {
        return "INSERT INTO " + this.table.getFullName() + " " + columnSegment + " VALUES " + valueSegment;
    }

    /**
     * 拼接完整删除SQL语句
     * @param condition 条件部分
     * @return 完整的删除SQL语句
     */
    public String delete(final String condition) {
        return "DELETE FROM " + this.table.getFullName() + (Objects.isBlank(condition) ? EMPTY : " " + condition);
    }

    /**
     * 拼接完整更新SQL语句
     * @param updateSegment 更新部分
     * @param whereSegment  条件部分
     * @return 完整更新SQL语句
     */
    public String update(final String updateSegment, final String whereSegment) {
        return "UPDATE " + this.table.getFullName() + " " + updateSegment +
            (Objects.isBlank(whereSegment) ? EMPTY : NEW_LINE + whereSegment);
    }

    /**
     * 拼接完整查询SQL语句
     * @param selectSegment 查询部分
     * @param whereSegment  条件部分
     * @return 完整查询SQL语句
     */
    public String select(final String selectSegment, final String whereSegment) {
        return "SELECT " + selectSegment + " FROM " + this.table.getFullName() +
            (Objects.isBlank(whereSegment) ? EMPTY : " " + whereSegment);
    }

    /**
     * {@link Criteria Criteria}条件查询SQL语句
     * @param whereSegment 条件部分
     * @return 完整查询SQL语句
     */
    public String criteriaSelect(final String whereSegment) {
        return this.criteriaSelect("${" + PARAM_CRITERIA + ".selectSegment}", whereSegment);
    }

    /**
     * {@link Criteria Criteria}条件查询SQL语句
     * @param selectSegment 查询部分
     * @param whereSegment  条件部分
     * @return 完整查询SQL语句
     */
    public String criteriaSelect(final String selectSegment, final String whereSegment) {
        return "SELECT <![CDATA[" + selectSegment + "]]> FROM ${" + PARAM_CRITERIA + ".tableName}" + whereSegment;
    }

    /**
     * {@link Criteria Criteria}条件查询SQL语句
     * @return 完整查询SQL语句
     */
    public String criteriaSelect() {
        return "${" + PARAM_CRITERIA + ".segment}";
    }

    /**
     * 主键条件
     * @return 条件
     */
    protected StringBuilder addPrimaryKeyCondition() {
        final StringBuilder condition = new StringBuilder(120);
        if (this.table.isOnlyOneId()) {
            final Column idColumn = this.table.getIdColumn();
            condition.append(SPACE_AND_SPACE).append(Scripts.convertToPartArg(PARAM_ENTITY, Operation.REPLACE,
                idColumn));
        } else {
            this.table.getIdColumns().forEach(it ->
                condition.append(SPACE_AND_SPACE).append(Scripts.convertToPartArg(PARAM_ENTITY, Operation.REPLACE,
                    it)));
        }
        return condition;
    }

    /**
     * 追加乐观锁条件
     * @param condition 条件拼接
     */
    protected void addOptimisticLockCondition(final StringBuilder condition) {
        this.table.optimisticLockOptional().ifPresent(it ->
            condition.append(SPACE_AND_SPACE).append(Scripts.convertToPartArg(PARAM_ENTITY, Operation.REPLACE, it)));
    }

    /**
     * 追加多租户条件
     * @param condition 条件拼接
     */
    protected void addMultiTenantCondition(final StringBuilder condition) {
        Optional.ofNullable(this.table.getMultiTenantColumn()).ifPresent(it ->
            condition.append(SPACE_AND_SPACE).append(Scripts.convertToPartArg(PARAM_ENTITY, Operation.REPLACE, it)));
    }

    /**
     * 将乐观锁字段转换成对应的if条件标签
     * @param column 乐观锁字段对象
     * @return if条件标签
     */
    protected String convertToOptimisticLockIfTag(final Column column) {
        Objects.isTrue(column.isVersion(), "The specified table field object is not an optimistic lock field. " +
            "Check the \"" + column.getProperty() + "\" attribute on the \"" + column.getEntity().getName() + "\" " +
            "entity class for the \"@Version\" annotation.");
        final StringBuilder condition = new StringBuilder(80);
        condition.append("_parameter.containsKey('").append(PARAM_OPTIMISTIC_LOCK_KEY).append("') and ");
        condition.append(PARAM_OPTIMISTIC_LOCK_KEY).append(" != null");
        if (column.isCheckNotEmpty() && String.class.isAssignableFrom(column.getJavaType())) {
            condition.append(" and ").append(PARAM_OPTIMISTIC_LOCK_KEY).append(" != ''");
        }
        final String script = SPACE + column.getColumn() + " = " +
            Scripts.safeJoining(PARAM_OPTIMISTIC_LOCK_KEY, Scripts.concatIntactTypeArg(column.getTypeHandler(),
                column.isUseJavaType(), column.getJavaType(), column.getJdbcType())) + COMMA_SPACE;
        return Scripts.convertToIfTag(script, condition.toString(), true);
    }

    public Table getTable() {
        return table;
    }

    public Class<?> getEntity() {
        return entity;
    }

}
