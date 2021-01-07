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
package com.wvkity.mybatis.core.inject.mapping.sql;

import com.wvkity.mybatis.core.config.MyBatisGlobalConfiguration;
import com.wvkity.mybatis.core.constant.Constants;
import com.wvkity.mybatis.core.inject.mapping.utils.Scripts;
import com.wvkity.mybatis.core.metadata.Column;
import com.wvkity.mybatis.core.metadata.Table;
import com.wvkity.mybatis.core.utils.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
     * @return 完整更新SQL语句
     */
    public String select(final String selectSegment, final String whereSegment) {
        return "SELECT " + selectSegment + " FROM " + this.table.getFullName() +
            (Objects.isBlank(whereSegment) ? EMPTY : " " + whereSegment);
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
        final StringBuilder condition = new StringBuilder(30);
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
