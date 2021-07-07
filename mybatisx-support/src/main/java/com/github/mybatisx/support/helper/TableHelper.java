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
package com.github.mybatisx.support.helper;

import com.github.mybatisx.basic.builder.support.TableBuilder;
import com.github.mybatisx.basic.immutable.ImmutableLinkedMap;
import com.github.mybatisx.basic.immutable.ImmutableList;
import com.github.mybatisx.basic.metadata.Column;
import com.github.mybatisx.basic.metadata.Table;
import com.github.mybatisx.basic.parser.EntityParser;
import com.github.mybatisx.basic.reflect.Reflections;
import com.github.mybatisx.basic.utils.Objects;
import com.github.mybatisx.support.parser.DefaultEntityParser;
import com.github.mybatisx.support.config.MyBatisGlobalConfiguration;
import com.github.mybatisx.support.config.MyBatisLocalConfigurationCache;
import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.apache.ibatis.session.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 数据库表映射处理器
 * @author wvkity
 * @created 2020-10-04
 * @since 1.0.0
 */
public final class TableHelper {

    private static final Logger log = LoggerFactory.getLogger(TableHelper.class);

    private TableHelper() {
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
     * 字段名-字段对象缓存
     */
    private static final Map<String, Map<String, Column>> COLUMN_NAME_MAPPED_CACHE = new ConcurrentHashMap<>(32);

    /**
     * 拦截解析实体-数据库表映射信息
     * @param assistant {@link MapperBuilderAssistant}
     * @param entity    实体类
     * @return {@link Table}
     */
    public synchronized static Table parse(final MapperBuilderAssistant assistant, final Class<?> entity) {
        if (Objects.nonNull(entity)) {
            final String entityName = Reflections.getRealClass(entity).getName();
            final Table oldTable = DATABASE_TABLE_CACHE.get(entityName);
            if (oldTable == null) {
                log.debug("Parsing the entity class corresponding table mapping information: {}", entityName);
                final Configuration configuration = assistant.getConfiguration();
                final MyBatisGlobalConfiguration globalConfiguration =
                    MyBatisLocalConfigurationCache.getGlobalConfiguration(configuration);
                final TableBuilder builder =
                    TableBuilder.create().entity(entity).namespace(assistant.getCurrentNamespace());
                // 解析实体类
                final Table table = getEntityParser(globalConfiguration).parse(configuration, entity, builder);
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
        Optional.ofNullable(table.getEntity()).ifPresent(it -> {
            final String entityName = it.getName();
            PROPERTY_COLUMN_MAPPED_CACHE.putIfAbsent(entityName, table.propertyMappingColumns());
            final Map<String, Column> map = table.columns().stream().collect(LinkedHashMap::new, (m, v) ->
                m.put(v.getColumn().toUpperCase(Locale.ENGLISH), v), Map::putAll);
            COLUMN_NAME_MAPPED_CACHE.putIfAbsent(entityName, ImmutableLinkedMap.of(map));
        });

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
            Optional.ofNullable(TableHelper.getTable(it)).map(Table::getIdColumn)).orElse(null);
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
        return Optional.ofNullable(clazz).map(it -> TableHelper.getColumnCache(it).get(property)).orElse(null);
    }

    /**
     * 根据实体类、字段名获取字段信息
     * @param clazz      实体类
     * @param columnName 字段名
     * @return {@link Column}
     */
    public static Column getOrgColumn(final Class<?> clazz, final String columnName) {
        if (Objects.isBlank(columnName)) {
            return null;
        }
        return Optional.ofNullable(clazz).map(it -> {
            final Map<String, Column> cache = TableHelper.COLUMN_NAME_MAPPED_CACHE.get(it.getName());
            if (Objects.isNotEmpty(cache)) {
                return cache.get(columnName.toUpperCase(Locale.ENGLISH));
            }
            return null;
        }).orElse(null);
    }

    /**
     * 根据实体类、过滤器筛选字段信息
     * @param clazz  实体类
     * @param filter 过滤器
     * @return {@link Column}集合
     */
    public static List<Column> getColumns(final Class<?> clazz, final Predicate<Column> filter) {
        if (Objects.nonNull(clazz)) {
            final Table table = TableHelper.getTable(clazz);
            final Set<Column> columns;
            if (Objects.nonNull(table) && Objects.isNotEmpty((columns = table.columns()))) {
                if (Objects.nonNull(filter)) {
                    final List<Column> it = columns.stream().filter(filter).collect(Collectors.toList());
                    if (Objects.isNotEmpty(it)) {
                        return ImmutableList.of(it);
                    }
                }
                return ImmutableList.of(columns);
            }
        }
        return ImmutableList.of();
    }
}
