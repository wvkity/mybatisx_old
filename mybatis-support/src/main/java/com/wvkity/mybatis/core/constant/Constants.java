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
package com.wvkity.mybatis.core.constant;

/**
 * 常量接口
 * @author wvkity
 * @created 2020-10-07
 * @since 1.0.0
 */
public interface Constants {

    /**
     * JPA中的{@code @Table}注解
     */
    String JPA_TABLE = "javax.persistence.Table";

    /**
     * JPA中的{@code @Entity}注解
     */
    String JPA_ENTITY = "javax.persistence.Entity";

    /**
     * JPA中的{@code @Column}注解
     */
    String JPA_COLUMN = "javax.persistence.Column";

    /**
     * JPA中的{@code @Transient}注解
     */
    String JPA_TRANSIENT = "javax.persistence.Transient";

    /**
     * JPA中的{@code @Id}注解
     */
    String JPA_ID = "javax.persistence.Id";

    /**
     * JPA中的{@code @OrderBy}注解
     */
    String JPA_ORDER_BY = "javax.persistence.OrderBy";

    /**
     * JPA中的{@code @SequenceGenerator}注解
     */
    String JPA_SEQUENCE_GENERATOR = "javax.persistence.SequenceGenerator";

    /**
     * JPA中的{@code @GeneratedValue}注解
     */
    String JPA_GENERATED_VALUE = "javax.persistence.GeneratedValue";

    /**
     * JPA中的{@code @Version}注解
     */
    String JPA_VERSION = "javax.persistence.Version";

    /**
     * JPA Table: name属性
     */
    String JPA_TABLE_PROP_NAME = "name";
    /**
     * JPA Table: schema属性
     */
    String JPA_TABLE_PROP_SCHEMA = "schema";
    /**
     * JPA Table: catalog属性
     */
    String JPA_TABLE_PROP_CATALOG = "catalog";
    /**
     * JPA Column: name属性
     */
    String JPA_COLUMN_PROP_NAME = "name";
    /**
     * JPA Column: insertable属性
     */
    String JPA_COLUMN_PROP_INSERTABLE = "insertable";
    /**
     * JPA Column: updatable属性
     */
    String JPA_COLUMN_PROP_UPDATABLE = "updatable";
    /**
     * JPA Entity: name属性
     */
    String JPA_ENTITY_PROP_NAME = "name";
    /**
     * JPA GeneratedValue: generator属性
     */
    String JPA_GV_GENERATOR = "generator";
    /**
     * JPA GeneratedValue: strategy属性
     */
    String JPA_GV_STRATEGY = "strategy";
    /**
     * 自增主键
     */
    String PRIMARY_KEY_IDENTITY = "IDENTITY";
    /**
     * sequence序列器
     */
    String PRIMARY_KEY_SEQUENCE = "SEQUENCE";
    /**
     * UUID主键
     */
    String PRIMARY_KEY_UUID = "UUID";
    /**
     * JDBC生成主键
     */
    String PRIMARY_KEY_JDBC = "JDBC";
    /**
     * 雪花算法主键
     */
    String PRIMARY_KEY_SNOWFLAKE = "SNOWFLAKE";
    /**
     * NULL
     */
    String NULL = null;
    /**
     * #{
     */
    String HASH_OPEN_BRACE = "#{";
    /**
     * ${
     */
    String DOLLAR_OPEN_BRACE = "${";
    /**
     * }
     */
    String CLOSE_BRACE = "}";
    /**
     * 转义双引号
     */
    String CHAR_QUOTE = "\"";
    /**
     * 小于号
     */
    String LT = "<";
    /**
     * 大于号
     */
    String GT = ">";
    /**
     * 斜杠
     */
    String SLASH = "/";
    /**
     * 点
     */
    String DOT = ".";
    /**
     * 空字符
     */
    String EMPTY = "";
    /**
     * 空格
     */
    String SPACE = " ";
    /**
     * 逗号
     */
    String COMMA = ",";
    /**
     * 逗号+空格
     */
    String COMMA_SPACE = ", ";
    /**
     * (
     */
    String BRACKET_OPEN = "(";
    /**
     * )
     */
    String CLOSE_BRACKET = ")";
    /**
     * item
     */
    String ITEM = "item";
    /**
     * #{item}
     */
    String HASH_ITEM = "#{item}";
    /**
     * OR
     */
    String OR = "OR";
    /**
     * OR+空格
     */
    String OR_SPACE = "OR ";
    /**
     * 空格+OR+空格
     */
    String SPACE_OR_SPACE = " OR ";
    /**
     * AND
     */
    String AND = "AND";
    /**
     * AND+空格
     */
    String AND_SPACE = "AND ";
    /**
     * 空格+AND+空格
     */
    String SPACE_AND_SPACE = " AND ";
    /**
     * 星号
     */
    String STAR = "*";
    /**
     * &lt;![CDATA[
     */
    String OPEN_CDATA = "<![CDATA[";
    /**
     * ]]&gt;
     */
    String CLOSE_CDATA = "]]>";
    /***
     * 换行符
     */
    String NEW_LINE = System.lineSeparator();
    /**
     * 主键参数对象
     */
    String PARAM_ID = "id";
    /**
     * 主键参数对象
     */
    String PARAM_IDS = "ids";
    /**
     * 实体参数对象
     */
    String PARAM_ENTITY = "entity";
    /**
     * 实体参数对象
     */
    String PARAM_ENTITIES = "entities";
    /**
     * {@code Criteria}参数名
     */
    String PARAM_CRITERIA = "criteria";
    /**
     * 分页参数
     */
    String PARAM_PAGEABLE = "pageable";
    /**
     * 批量操作参数
     */
    String PARAM_BATCH_DATA_WRAPPER = "batchDataWrapper";
    /**
     * 乐观锁审计值key
     */
    String PARAM_OPTIMISTIC_LOCK_KEY = "optimistic_lock_auditing_value";
    /**
     * 逻辑删除审计值key
     */
    String PARAM_LOGICAL_DELETED_KEY = "logical_deleted_auditing_value";
}
