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
package com.wvkity.mybatis.core.handler;

import com.wvkity.mybatis.core.builder.support.TableBuilder;
import com.wvkity.mybatis.core.config.MyBatisGlobalConfiguration;
import com.wvkity.mybatis.core.config.MyBatisLocalConfigurationCache;
import com.wvkity.mybatis.core.immutable.ImmutableLinkedMap;
import com.wvkity.mybatis.core.metadata.Column;
import com.wvkity.mybatis.core.metadata.Table;
import com.wvkity.mybatis.core.parser.DefaultEntityParser;
import com.wvkity.mybatis.core.parser.EntityParser;
import com.wvkity.mybatis.core.reflect.Reflections;
import com.wvkity.mybatis.core.utils.Objects;
import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.apache.ibatis.session.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 数据库表映射处理器
 * @author wvkity
 * @created 2020-10-04
 * @since 1.0.0
 */
public final class TableHandler {

    private static final Logger log = LoggerFactory.getLogger(TableHandler.class);

    private TableHandler() {
    }

    /**
     * 默认实体解析器
     */
    private static final EntityParser DEFAULT_ENTITY_PARSER = new DefaultEntityParser();
    /**
     * 数据库表信息缓存
     */
    private static final Map<String, Table> DATABASE_TABLE_CACHE = new ConcurrentHashMap<>(32);
    /**
     * 属性-字段缓存(只读: Map<EntityClassName, Map<Property, Column>>)
     */
    private static final Map<String, Map<String, Column>> PROPERTY_COLUMN_MAPPED_CACHE = new ConcurrentHashMap<>(32);

    /**
     * 拦截解析实体-数据库表映射信息
     * @param assistant {@link MapperBuilderAssistant}
     * @param entity    实体类
     * @return {@link Table}
     */
    public synchronized static Table parse(final MapperBuilderAssistant assistant, final Class<?> entity) {
        if (Objects.nonNull(entity)) {
            final String entityName = Reflections.getRealClass(entity).getName();
            final Table oldTable = DATABASE_TABLE_CACHE.getOrDefault(entityName, null);
            if (oldTable == null) {
                log.debug("Parsing the entity class corresponding table mapping information: {}", entityName);
                final Configuration configuration = assistant.getConfiguration();
                final MyBatisGlobalConfiguration globalConfiguration =
                    MyBatisLocalConfigurationCache.getGlobalConfiguration(configuration);
                final TableBuilder builder = TableBuilder.create().setEntity(entity)
                    .setNamespace(assistant.getCurrentNamespace());
                builder.configuration(globalConfiguration);
                // 解析实体类
                final Table table = getEntityParser(globalConfiguration).parse(globalConfiguration, entity, builder);
                if (!DATABASE_TABLE_CACHE.containsKey(entityName)) {
                    DATABASE_TABLE_CACHE.putIfAbsent(entityName, table);
                    cache(table);
                }
                return table;
            }
            return oldTable;
        }
        return null;
    }

    /**
     * 获取实体解析器
     * @param configuration {@link MyBatisGlobalConfiguration}
     * @return {@link EntityParser}
     */
    private static EntityParser getEntityParser(final MyBatisGlobalConfiguration configuration) {
        return Optional.ofNullable(configuration).map(it -> {
            final EntityParser parser = it.getEntityParser();
            if (parser == null) {
                it.setEntityParser(DEFAULT_ENTITY_PARSER);
                return DEFAULT_ENTITY_PARSER;
            }
            return parser;
        }).orElse(DEFAULT_ENTITY_PARSER);
    }


    /**
     * 缓存属性-字段映射
     * @param table {@link Table}
     */
    private static void cache(final Table table) {
        Optional.ofNullable(table.getEntity()).ifPresent(it ->
            PROPERTY_COLUMN_MAPPED_CACHE.putIfAbsent(it.getName(), table.propertyMappingColumns()));
    }

    /**
     * 获取表字段缓存
     * @param clazz 实体类
     * @return 字段集合
     */
    public static Map<String, Column> getColumnCache(final Class<?> clazz) {
        return Optional.ofNullable(clazz).map(it -> PROPERTY_COLUMN_MAPPED_CACHE.get(clazz.getName()))
            .orElse(ImmutableLinkedMap.of());
    }

    /**
     * 根据实体类获取其对应的表对象
     * @param clazz 实体类
     * @return {@link Table}
     */
    public static Table getTable(final Class<?> clazz) {
        return Optional.ofNullable(clazz).map(Reflections::getRealClass).filter(it ->
            !Reflections.isPrimitiveOrWrapType(it) && String.class != clazz).map(it -> getTable(it.getName()))
            .orElse(null);
    }

    /**
     * 根据实体类名获取对应的表对象
     * @param entityName 实体类名
     * @return {@link Table}
     */
    public static Table getTable(final String entityName) {
        return Optional.ofNullable(entityName).filter(Objects::isNotBlank).map(DATABASE_TABLE_CACHE::get).orElse(null);
    }

    /**
     * 根据实体类获取主键
     * @param clazz 实体类
     * @return {@link Column}
     */
    public static Column getId(final Class<?> clazz) {
        return Optional.ofNullable(clazz).flatMap(it ->
            Optional.ofNullable(TableHandler.getTable(it)).map(Table::getIdColumn)).orElse(null);
    }

    /**
     * 根据实体类、属性名获取字段信息
     * @param clazz    实体类
     * @param property 属性
     * @return {@link Column}
     */
    public static Column getColumn(final Class<?> clazz, final String property) {
        if (Objects.isBlank(property)) {
            return null;
        }
        return Optional.ofNullable(clazz).map(it -> TableHandler.getColumnCache(it).get(property)).orElse(null);
    }
}
