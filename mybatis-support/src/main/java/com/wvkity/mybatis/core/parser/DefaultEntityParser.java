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
package com.wvkity.mybatis.core.parser;

import com.wvkity.mybatis.annotation.Column;
import com.wvkity.mybatis.annotation.ColumnExt;
import com.wvkity.mybatis.annotation.CreatedTime;
import com.wvkity.mybatis.annotation.CreatedUserId;
import com.wvkity.mybatis.annotation.CreatedUserName;
import com.wvkity.mybatis.annotation.DeletedTime;
import com.wvkity.mybatis.annotation.DeletedUserId;
import com.wvkity.mybatis.annotation.DeletedUserName;
import com.wvkity.mybatis.annotation.Entity;
import com.wvkity.mybatis.annotation.Executing;
import com.wvkity.mybatis.annotation.GeneratedValue;
import com.wvkity.mybatis.annotation.GenerationType;
import com.wvkity.mybatis.annotation.Identity;
import com.wvkity.mybatis.annotation.LogicalDeletion;
import com.wvkity.mybatis.annotation.ModifiedTime;
import com.wvkity.mybatis.annotation.ModifiedUserId;
import com.wvkity.mybatis.annotation.ModifiedUserName;
import com.wvkity.mybatis.annotation.Naming;
import com.wvkity.mybatis.annotation.NamingStrategy;
import com.wvkity.mybatis.annotation.Option;
import com.wvkity.mybatis.annotation.PrimaryKeyStrategy;
import com.wvkity.mybatis.annotation.Priority;
import com.wvkity.mybatis.annotation.Snowflake;
import com.wvkity.mybatis.annotation.Tenant;
import com.wvkity.mybatis.annotation.Version;
import com.wvkity.mybatis.core.builder.support.ColumnBuilder;
import com.wvkity.mybatis.core.builder.support.TableBuilder;
import com.wvkity.mybatis.core.config.MyBatisGlobalConfiguration;
import com.wvkity.mybatis.core.constant.Constants;
import com.wvkity.mybatis.core.data.audit.AuditParser;
import com.wvkity.mybatis.core.data.audit.AuditStrategy;
import com.wvkity.mybatis.core.exception.MyBatisParserException;
import com.wvkity.mybatis.core.filter.ClassFilter;
import com.wvkity.mybatis.core.filter.FieldFilter;
import com.wvkity.mybatis.core.filter.Filter;
import com.wvkity.mybatis.core.filter.GetMethodFilter;
import com.wvkity.mybatis.core.filter.SetMethodFilter;
import com.wvkity.mybatis.core.immutable.ImmutableSet;
import com.wvkity.mybatis.core.metadata.Field;
import com.wvkity.mybatis.core.metadata.Table;
import com.wvkity.mybatis.core.reflect.ReflectMetadata;
import com.wvkity.mybatis.core.reflect.Reflections;
import com.wvkity.mybatis.core.reflect.Reflector;
import com.wvkity.mybatis.core.type.JdbcTypeMappingRegistry;
import com.wvkity.mybatis.core.utils.Objects;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.UnknownTypeHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

/**
 * 默认实体解析器
 * @author wvkity
 * @created 2020-10-03
 * @since 1.0.0
 */
public class DefaultEntityParser implements EntityParser, Constants {

    private static final Logger log = LoggerFactory.getLogger(DefaultEntityParser.class);
    private static final FieldParser DEFAULT_FIELD_PARSER = new DefaultFieldParser();
    /**
     * 默认父类过滤器
     */
    private static final ClassFilter SUPER_CLASS_FILTER = new ClassFilter();
    /**
     * 默认属性过滤器
     */
    private static final FieldFilter FIELD_FILTER = FieldFilter.of(true, true);
    /**
     * 默认get方法过滤器
     */
    private static final GetMethodFilter GET_METHOD_FILTER = new GetMethodFilter();
    /**
     * 默认set方法过滤器
     */
    private static final SetMethodFilter SET_METHOD_FILTER = new SetMethodFilter();
    /**
     * 乐观锁支持的类型
     */
    private static final Set<Class<?>> OPTIMISTIC_LOCK_SUPPORT_CLASSES = ImmutableSet.construct(int.class,
        Integer.class, long.class, Long.class, Date.class, LocalTime.class, LocalDate.class, LocalDateTime.class,
        OffsetTime.class, OffsetDateTime.class, ZonedDateTime.class, Instant.class, Timestamp.class);

    @Override
    public Table parse(MyBatisGlobalConfiguration configuration, Class<?> entity, TableBuilder builder) {
        // 反射解析实体类
        final Reflector reflector = Reflector.of(entity);
        reflector.classFilter(getClassFilter(configuration));
        reflector.fieldFilter(getFieldFilter(configuration));
        reflector.getMethodFilter(getGetMethodFilter(configuration));
        reflector.setMethodFilter(getSetMethodFilter(configuration));
        reflector.parse();
        // 命名策略
        final NamingStrategy strategy = naming(configuration, reflector);
        builder.strategy(strategy);
        // 处理实体类上的注解
        handleAnnotationOnEntityClass(configuration, reflector, builder);
        // 处理实体属性
        handleEntityClassRelatedAttributes(configuration, strategy, reflector, builder);
        return builder.build();
    }

    /**
     * 处理实体类上的注解
     * @param configuration 全局配置对象
     * @param reflector     反射器
     * @param tb            表对象构建器
     */
    private void handleAnnotationOnEntityClass(final MyBatisGlobalConfiguration configuration,
                                               final Reflector reflector,
                                               final TableBuilder tb) {
        // 处理@Table注解(系统自带或依赖JPA)
        final com.wvkity.mybatis.annotation.Table table;
        if (Objects.nonNull((table = reflector.getAnnotation(com.wvkity.mybatis.annotation.Table.class)))) {
            tb.setName(table.name()).setCatalog(table.catalog()).setSchema(table.schema()).setPrefix(table.prefix());
        } else if (reflector.isAnnotationPresent(JPA_TABLE)) {
            // JPA @Table
            final ReflectMetadata metadata = reflector.getReflectMetadata(JPA_TABLE);
            tb.setName(metadata.stringValue(JPA_TABLE_PROP_NAME)).setCatalog(metadata.stringValue(JPA_TABLE_PROP_CATALOG));
            tb.setSchema(metadata.stringValue(JPA_TABLE_PROP_SCHEMA));
        }
        if (Objects.isBlank(tb.getName())) {
            final Entity entity;
            if (Objects.nonNull((entity = reflector.getAnnotation(Entity.class)))) {
                tb.setName(entity.name());
            } else if (reflector.isAnnotationPresent(JPA_ENTITY)) {
                // JPA @Entity
                final ReflectMetadata metadata = reflector.getReflectMetadata(JPA_ENTITY);
                tb.setName(metadata.stringValue(JPA_ENTITY_PROP_NAME));
            }
        }
    }

    /**
     * 处理实体类的相关属性
     * @param configuration 全局配置对象
     * @param strategy      命名策略
     * @param reflector     反射器
     * @param tb            表对象构建器
     */
    private void handleEntityClassRelatedAttributes(final MyBatisGlobalConfiguration configuration,
                                                    final NamingStrategy strategy,
                                                    final Reflector reflector,
                                                    final TableBuilder tb) {
        final FieldParser fieldParser = getFieldParser(configuration);
        // 解析属性
        final Set<Field> fields = fieldParser.parse(reflector);
        if (Objects.isNotEmpty(fields)) {
            final boolean autoAddedIsPrefixed = configuration.isBooleanPropertyAutoAddedPrefixedWithIs();
            final Class<?> entityClass = reflector.getClazz();
            fields.forEach(it -> {
                // 数据库表字段构建器
                final ColumnBuilder cb = ColumnBuilder.create()
                    .setEntity(entityClass)
                    .setAutoAddedIsPrefixed(autoAddedIsPrefixed)
                    .setProperty(it.getName())
                    .setJavaType(it.getJavaType())
                    .setField(it);
                cb.configuration(configuration).strategy(strategy);
                // 处理属性上的注解
                handleAnnotationOnField(configuration, it, reflector, tb, cb);
            });
        }
    }

    /**
     * 处理属性上的注解
     * @param configuration 全局配置对象
     * @param field         属性包装对象
     * @param reflector     反射器
     * @param tb            表对象构建器
     * @param cb            数据库字段对象构建器
     */
    private void handleAnnotationOnField(final MyBatisGlobalConfiguration configuration,
                                         final Field field, final Reflector reflector,
                                         final TableBuilder tb, final ColumnBuilder cb) {
        // 处理@Column/@ColumnExt注解
        handleColumnAnnotation(configuration, field, cb);
        // 处理自动识别主键
        handleAutoDiscernPrimaryKey(configuration, field, tb, cb);
        // 处理多租户标识
        handleTenementAnnotation(configuration, field, tb, cb);
        // 处理乐观锁注解
        handleVersionAnnotation(configuration, field, tb, cb);
        // 检查是否为主键
        if (cb.isPrimaryKey()) {
            cb.setInsertable(true).setUpdatable(false);
            final boolean priority = field.isAnnotationPresent(Priority.class);
            final boolean hasPrimaryKey = tb.hasPrimaryKey();
            if (hasPrimaryKey) {
                tb.setOnlyOneId(false);
            }
            if (!hasPrimaryKey || priority) {
                tb.setIdColumn(cb).setIdProperty(cb.getProperty());
            }
            // 处理主键生成策略
            handlePrimaryKeyGenerated(configuration, field, tb, cb);
        } else {
            // 处理逻辑删除注解@LogicalDeletion
            handleLogicalDeletionAnnotation(configuration, field, tb, cb);
            // 处理审计注解
            handleAuditingAnnotations(configuration, field, tb, cb);
        }
        tb.addColumn(cb);
    }

    /**
     * 处理属性上的@Column/@ColumnExt注解
     * @param configuration 全局配置对象
     * @param field         属性包装对象
     * @param cb            数据库字段对象构建器
     */
    private void handleColumnAnnotation(final MyBatisGlobalConfiguration configuration,
                                        final Field field, final ColumnBuilder cb) {
        cb.setPrimaryKey(field.isPrimaryKey());
        // 处理@Column注解
        final Column column;
        if (Objects.nonNull((column = field.getAnnotation(Column.class)))) {
            cb.setColumn(column.name()).setInsertable(column.insertable()).setUpdatable(column.updatable());
        } else if (field.isAnnotationPresent(JPA_COLUMN)) {
            // JPA @Column
            final ReflectMetadata metadata = field.getReflectMetadata(JPA_COLUMN);
            cb.setColumn(metadata.stringValue(JPA_ENTITY_PROP_NAME));
            cb.setInsertable(metadata.booleanValue(JPA_COLUMN_PROP_INSERTABLE));
            cb.setUpdatable(metadata.booleanValue(JPA_COLUMN_PROP_UPDATABLE));
        }
        // 处理@ColumnExt注解
        final ColumnExt ext;
        if (Objects.nonNull((ext = field.getAnnotation(ColumnExt.class)))) {
            cb.setBlob(ext.blob());
            if (Objects.isBlank(cb.getColumn())) {
                cb.setColumn(ext.name());
            }
            cb.setUseJavaType(ext.javaType() == Option.ENABLE);
            cb.setCheckNotNull(ext.notNull() == Option.ENABLE);
            cb.setCheckNotEmpty(ext.notEmpty() == Option.ENABLE);
            Optional.of(ext.jdbcType()).filter(it -> it != JdbcType.UNDEFINED).ifPresent(cb::setJdbcType);
            Optional.of(ext.typeHandler()).filter(it -> !it.equals(UnknownTypeHandler.class))
                .ifPresent(cb::setTypeHandler);
            if (ext.javaType() == Option.CONFIG) {
                cb.setUseJavaType(configuration.isAutoSplicingJavaType());
            }
            if (ext.notNull() == Option.CONFIG) {
                cb.setCheckNotNull(configuration.isDynamicSqlNotNullChecking());
            }
            if (ext.notEmpty() == Option.CONFIG) {
                cb.setCheckNotEmpty(configuration.isDynamicSqlNotEmptyChecking());
            }
        } else {
            cb.setUseJavaType(configuration.isAutoSplicingJavaType());
            cb.setCheckNotNull(configuration.isDynamicSqlNotNullChecking());
            cb.setCheckNotEmpty(configuration.isDynamicSqlNotEmptyChecking());
        }
        if (cb.getJdbcType() == null && configuration.isJdbcTypeAutoMapped()) {
            cb.setJdbcType(JdbcTypeMappingRegistry.getJdbcType(field.getJavaType(), JdbcType.UNDEFINED));
        }
        if (cb.getJavaType().isPrimitive()) {
            // 基本数据类型禁止做非空检查
            cb.setCheckNotNull(false).setCheckNotEmpty(false);
            // 基本数据类型警告
            log.warn("The \"{}\" attribute in the entity class \"{}\" is defined as a primitive type. " +
                    "The primitive type is not null at any time in dynamic SQL because it has a default value. It is " +
                    "recommended to modify the primitive type to the corresponding wrapper type.", cb.getProperty(),
                cb.getEntity().getName());
        }
    }

    /**
     * 处理自动识别主键
     * @param configuration 全局配置对象
     * @param field         属性包装对象
     * @param tb            数据库表对象构建器
     * @param cb            数据库字段构建器
     */
    private void handleAutoDiscernPrimaryKey(final MyBatisGlobalConfiguration configuration,
                                             final Field field, final TableBuilder tb,
                                             final ColumnBuilder cb) {
        if (!cb.isPrimaryKey() && configuration.isPrimaryKeyAutoDiscern()) {
            final String primaryKey = configuration.getPrimaryKeyProperty();
            cb.setPrimaryKey(Objects.isNotBlank(primaryKey) && Objects.equals(primaryKey, cb.getProperty()));
        }
    }

    /**
     * 处理多租户注解
     * @param configuration 全局配置对象
     * @param field         属性包装对象
     * @param tb            数据库表对象构建器
     * @param cb            数据库字段构建器
     */
    private void handleTenementAnnotation(final MyBatisGlobalConfiguration configuration,
                                          final Field field, final TableBuilder tb,
                                          final ColumnBuilder cb) {
        if (field.isAnnotationPresent(Tenant.class)) {
            if (cb.isPrimaryKey()) {
                throw new MyBatisParserException("The attribute \"" + cb.getProperty() + "\" of the entity class \"" +
                    tb.getEntity().getName() + "\" is the primary key. Adding \"@Tenement\" annotation is not supported.");
            }
            if (tb.isTenant()) {
                throw new MyBatisParserException("The entity class \"" + tb.getEntity().getName() + "\" already has " +
                    "the tenant attribute \"" + tb.getTenantColumn().getProperty() + "\". " +
                    "The entity class supports an tenant attribute, so attribute \"" + cb.getProperty() + "\" " +
                    "does not support adding the \"@Tenement\" annotation.");
            }
            // 租户字段禁止更新
            cb.setTenant(true).setUpdatable(false);
            tb.setTenantColumn(cb).setTenant(true);
        }
    }

    /**
     * 处理@Version注解
     * @param configuration 全局配置对象
     * @param field         属性包装对象
     * @param tb            数据库表对象构建器
     * @param cb            数据库字段构建器
     */
    private void handleVersionAnnotation(final MyBatisGlobalConfiguration configuration,
                                         final Field field, final TableBuilder tb,
                                         final ColumnBuilder cb) {
        if (field.isAnnotationPresent(Version.class) || field.isAnnotationPresent(JPA_VERSION)) {
            if (cb.isPrimaryKey()) {
                throw new MyBatisParserException("The attribute \"" + cb.getProperty() + "\" of the entity class \"" +
                    tb.getEntity().getName() + "\" is the primary key. The primary key is not updatable. " +
                    "Adding \"@version\" annotation is not supported.");
            }
            if (!cb.isUpdatable()) {
                throw new MyBatisParserException("The attribute \"" + cb.getProperty() + "\" of the entity class \"" +
                    tb.getEntity().getName() + "\" is not updatable and adding \"@version\" annotation is not supported.");
            }
            if (!OPTIMISTIC_LOCK_SUPPORT_CLASSES.contains(cb.getJavaType())) {
                throw new MyBatisParserException("The type of the \"" + cb.getProperty() + "\" attribute of the " +
                    "entity class \"" + tb.getEntity().getName() + "\" is not within the type range specified by " +
                    "the optimistic lock. Adding the \"@version\" annotation is not supported.");
            }
            if (tb.getOptimisticLockColumn() != null) {
                throw new MyBatisParserException("The entity class \"" + tb.getEntity().getName() + "\" already has " +
                    "the optimistic lock attribute \"" + tb.getOptimisticLockColumn().getProperty() + "\". " +
                    "The entity class supports an optimistic lock attribute, so attribute \"" + cb.getProperty() + "\" " +
                    "does not support adding the \"@version\" annotation.");
            }
            cb.setVersion(true);
            tb.setOptimisticLockColumn(cb);
        }
    }

    /**
     * 处理逻辑删除注解
     * @param configuration 全局配置对象
     * @param field         属性包装对象
     * @param tb            数据库表对象构建器
     * @param cb            数据库字段构建器
     */
    private void handleLogicalDeletionAnnotation(final MyBatisGlobalConfiguration configuration,
                                                 final Field field, final TableBuilder tb,
                                                 final ColumnBuilder cb) {
        final String logicalDeletedProperty = configuration.getLogicalDeletedProperty();
        if (field.isAnnotationPresent(LogicalDeletion.class)
            || Objects.equals(logicalDeletedProperty, cb.getProperty())) {
            // 检查是否已存在逻辑删除属性
            if (tb.isLogicDelete()) {
                throw new MyBatisParserException("There are already \"" + tb.getLogicDeleteColumn()
                    .getProperty() + "\" attributes in \"" + tb.getEntity().getName()
                    + "\" entity class identified as logical deleted. Only one deleted attribute " +
                    "can exist in an entity class. Please check the entity class attributes.");
            }
            cb.setLogicalDelete(true);
            tb.setLogicDelete(true).setLogicDeleteColumn(cb);
            final LogicalDeletion deletion = field.getAnnotation(LogicalDeletion.class);
            final String deletedValue;
            final String undeletedValue;
            if (Objects.nonNull(deletion)) {
                // 注解 > 全局
                deletedValue = Optional.ofNullable(deletion.deleted()).filter(Objects::isNotBlank)
                    .orElseGet(configuration::getDeletedValue);
                undeletedValue = Optional.of(deletion.undeleted()).filter(Objects::isNotBlank)
                    .orElseGet(configuration::getUndeletedValue);
            } else {
                // 全局
                deletedValue = configuration.getDeletedValue();
                undeletedValue = configuration.getUndeletedValue();
            }
            Objects.requireNonEmpty(deletedValue, "The deleted value cannot be null.");
            Objects.requireNonEmpty(undeletedValue, "The undeleted value cannot be null.");
            // 将值转换成对应的值
            cb.setDeletedValue(Reflections.convert(cb.getJavaType(), deletedValue));
            cb.setUndeletedValue(Reflections.convert(cb.getJavaType(), undeletedValue));
        }
    }

    /**
     * 处理主键生成策略
     * @param configuration 全局配置对象
     * @param field         属性包装对象
     * @param tb            数据库表对象构建器
     * @param cb            数据库字段构建器
     */
    private void handlePrimaryKeyGenerated(final MyBatisGlobalConfiguration configuration,
                                           final Field field, final TableBuilder tb,
                                           final ColumnBuilder cb) {
        if (field.isAnnotationPresent(Identity.class)) {
            handleIdentityAnnotation(configuration, field, tb, cb);
        } else if (field.isAnnotationPresent(GeneratedValue.class)
            || field.isAnnotationPresent(JPA_GENERATED_VALUE)) {
            handleGeneratedValueAnnotation(configuration, field, tb, cb);
        } else if (field.isAnnotationPresent(Snowflake.class)) {
            cb.setSnowflake(true);
        }
        // 如果不存在主键生成策略，则根据全局配置主键生成策略填充
        final PrimaryKeyStrategy strategy;
        if (!cb.hasPrimaryKeyStrategy()
            && (strategy = configuration.getPrimaryKeyStrategy()) != PrimaryKeyStrategy.UNDEFINED) {
            cb.setIdentity(strategy == PrimaryKeyStrategy.JDBC || strategy == PrimaryKeyStrategy.IDENTITY);
            cb.setUuid(strategy == PrimaryKeyStrategy.UUID);
            cb.setSnowflake(strategy == PrimaryKeyStrategy.SNOWFLAKE);
        }
    }

    /**
     * 处理@Identity主键生成策略
     * @param configuration 全局配置对象
     * @param field         属性包装对象
     * @param tb            数据库表对象构建器
     * @param cb            数据库字段构建器
     */
    private void handleIdentityAnnotation(final MyBatisGlobalConfiguration configuration,
                                          final Field field, final TableBuilder tb, final ColumnBuilder cb) {
        final Identity identity = field.getAnnotation(Identity.class);
        final Executing executing = identity.executing();
        final boolean isAfter = executing == Executing.AFTER ||
            (executing == Executing.CONFIG && configuration.isGeneratedAfter());
        if (identity.useJdbc()) {
            cb.setIdentity(true).setExecuting(Executing.AFTER).setGenerator("JDBC");
        } else if (isAfter) {
            cb.setIdentity(true).setExecuting(Executing.AFTER).setGenerator("");
        } else {
            if (Objects.isBlank(identity.sequence())) {
                throw new MyBatisParserException("Invalid \"@Identity\" annotation exists on the \"" + cb.getProperty()
                    + "\" attribute of the entity class \"" + tb.getEntity().getName() + "\".");
            }
            cb.setIdentity(true).setExecuting(Executing.BEFORE).setGenerator(identity.sequence());
        }
    }

    /**
     * 处理@GeneratedValue主键生成策略
     * @param configuration 全局配置对象
     * @param field         属性包装对象
     * @param tb            数据库表对象构建器
     * @param cb            数据库字段构建器
     */
    private void handleGeneratedValueAnnotation(final MyBatisGlobalConfiguration configuration,
                                                final Field field, final TableBuilder tb,
                                                final ColumnBuilder cb) {
        final String generator;
        final boolean isIdentity;
        final boolean isSequence;
        final GeneratedValue generatedValue;
        if (Objects.nonNull(generatedValue = field.getAnnotation(GeneratedValue.class))) {
            final GenerationType type = generatedValue.strategy();
            generator = generatedValue.generator();
            isIdentity = type == GenerationType.IDENTITY;
            isSequence = !isIdentity && type == GenerationType.SEQUENCE;
        } else if (field.isAnnotationPresent(JPA_GENERATED_VALUE)) {
            final ReflectMetadata metadata = field.getReflectMetadata(JPA_GENERATED_VALUE);
            generator = metadata.stringValue(JPA_GV_GENERATOR);
            Enum<?> value = metadata.enumValue(JPA_GV_STRATEGY);
            if (value != null) {
                final String type = value.name().toUpperCase(Locale.ENGLISH);
                isIdentity = PRIMARY_KEY_IDENTITY.equals(type);
                isSequence = !isIdentity && PRIMARY_KEY_SEQUENCE.equals(type);
            } else {
                isIdentity = false;
                isSequence = false;
            }
        } else {
            generator = null;
            isIdentity = false;
            isSequence = false;
        }
        if (PRIMARY_KEY_UUID.equalsIgnoreCase(generator)) {
            cb.setUuid(true);
        } else if (PRIMARY_KEY_JDBC.equalsIgnoreCase(generator)) {
            cb.setIdentity(true).setGenerator(generator.toLowerCase(Locale.ENGLISH));
        } else if (PRIMARY_KEY_SNOWFLAKE.equalsIgnoreCase(generator)) {
            cb.setSnowflake(true);
        } else {
            if (isIdentity) {
                cb.setIdentity(true).setGenerator(generator);
            }
            if (isSequence && Objects.isNotBlank(generator)) {
                cb.setSequence(generator);
            } else {
                throw new MyBatisParserException("Invalid \"@GeneratedValue\" annotation exists on the \"" + cb.getProperty()
                    + "\" attribute of the entity class \"" + tb.getEntity().getName() + "\", " +
                    "The \"@GeneratedValue\" annotation supports the following form: " +
                    "\n\t\t\t 1.@GeneratedValue(generator = \"UUID\")" +
                    "\n\t\t\t 2.@GeneratedValue(generator = \"JDBC\")" +
                    "\n\t\t\t 3.@GeneratedValue(generator = \"SNOWFLAKE\")" +
                    "\n\t\t\t 4.@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = \"SequenceName\")" +
                    "\n\t\t\t 5.@GeneratedValue(strategy = GenerationType.IDENTITY, generator = \"[MySql, MSSQL...]\")");
            }
        }
    }

    /**
     * 处理审计注解
     * @param configuration 全局配置对象
     * @param field         属性包装对象
     * @param tb            数据库表对象构建器
     * @param cb            数据库字段构建器
     */
    private void handleAuditingAnnotations(final MyBatisGlobalConfiguration configuration,
                                           final Field field, final TableBuilder tb,
                                           final ColumnBuilder cb) {
        final boolean updatable = cb.canAuditing() && cb.isUpdatable();
        final boolean insertable = cb.canAuditing() && cb.isInsertable();
        final boolean isAutoScan = configuration.isAuditPropertyAutoScan();
        if (insertable) {
            // 保存审计
            cb.setCreatedUserId(AuditParser.matchingWithId(field, isAutoScan,
                AuditStrategy.INSERTED, CreatedUserId.class));
            cb.setCreatedUserName(AuditParser.matchingWithName(field, isAutoScan,
                AuditStrategy.INSERTED, CreatedUserName.class));
            cb.setCreatedDate(AuditParser.matchingWithTime(field, isAutoScan,
                AuditStrategy.INSERTED, CreatedTime.class));
        }
        if (updatable) {
            // 更新审计
            cb.setLastModifiedUserId(AuditParser.matchingWithId(field, isAutoScan,
                AuditStrategy.MODIFIED, ModifiedUserId.class));
            cb.setLastModifiedUserName(AuditParser.matchingWithName(field, isAutoScan,
                AuditStrategy.MODIFIED, ModifiedUserName.class));
            cb.setLastModifiedUserId(AuditParser.matchingWithTime(field, isAutoScan,
                AuditStrategy.MODIFIED, ModifiedTime.class));
            // 逻辑删除审计
            cb.setLogicDeletedUserId(AuditParser.matchingWithId(field, isAutoScan,
                AuditStrategy.DELETED, DeletedUserId.class));
            cb.setLogicDeletedUserName(AuditParser.matchingWithName(field, isAutoScan,
                AuditStrategy.DELETED, DeletedUserName.class));
            cb.setLogicDeletedDate(AuditParser.matchingWithTime(field, isAutoScan,
                AuditStrategy.DELETED, DeletedTime.class));
        }
    }

    /**
     * 获取类型过滤器
     * @param configuration 全局配置对象
     * @return {@link Predicate}对象
     */
    private Predicate<? super Class<?>> getClassFilter(final MyBatisGlobalConfiguration configuration) {
        return Optional.ofNullable(configuration).map(it -> {
            final Filter<Class<?>> filter = it.getClassFilter();
            if (filter == null) {
                it.setClassFilter(SUPER_CLASS_FILTER);
                return SUPER_CLASS_FILTER;
            }
            return filter;
        }).orElse(SUPER_CLASS_FILTER);
    }


    /**
     * 获取属性过滤器
     * <p>排除static或final修饰、@Transient注解的、非简单类型、或枚举类型不转换成简单类型的属性</p>
     * @param configuration 全局配置对象
     * @return {@link Predicate}对象
     */
    private Predicate<? super java.lang.reflect.Field> getFieldFilter(
        final MyBatisGlobalConfiguration configuration) {
        return Optional.ofNullable(configuration).map(it -> {
            final Filter<java.lang.reflect.Field> filter = it.getFieldFilter();
            if (filter == null) {
                final FieldFilter newFilter = FieldFilter.of(it.isUseSimpleType(), it.isEnumAsSimpleType());
                it.setFieldFilter(newFilter);
                return newFilter;
            }
            return filter;
        }).orElse(FIELD_FILTER);
    }

    /**
     * 获取get方法过滤器
     * @param configuration 全局配置对象
     * @return {@link Predicate}对象
     */
    private Predicate<? super Method> getGetMethodFilter(final MyBatisGlobalConfiguration configuration) {
        return Optional.ofNullable(configuration).map(it -> {
            final Filter<Method> filter = it.getGetMethodFilter();
            if (filter == null) {
                it.setGetMethodFilter(GET_METHOD_FILTER);
                return GET_METHOD_FILTER;
            }
            return filter;
        }).orElse(GET_METHOD_FILTER);
    }

    /**
     * 获取set方法过滤器
     * @param configuration 全局配置对象
     * @return {@link Predicate}对象
     */
    private Predicate<? super Method> getSetMethodFilter(final MyBatisGlobalConfiguration configuration) {
        return Optional.ofNullable(configuration).map(it -> {
            final Filter<Method> filter = it.getSetMethodFilter();
            if (filter == null) {
                it.setSetMethodFilter(SET_METHOD_FILTER);
                return SET_METHOD_FILTER;
            }
            return filter;
        }).orElse(SET_METHOD_FILTER);
    }

    /**
     * 获取属性解析器
     * @param configuration {@link MyBatisGlobalConfiguration}
     * @return 属性解析器
     */
    private FieldParser getFieldParser(final MyBatisGlobalConfiguration configuration) {
        return Optional.ofNullable(configuration).map(it -> {
            final FieldParser parser = configuration.getFieldParser();
            if (parser == null) {
                it.setFieldParser(DEFAULT_FIELD_PARSER);
                return DEFAULT_FIELD_PARSER;
            }
            return parser;
        }).orElse(DEFAULT_FIELD_PARSER);
    }

    /**
     * 获取命名策略
     * @param configuration 全局配置对象
     * @param reflector     反射器
     * @return 命名策略
     */
    private NamingStrategy naming(final MyBatisGlobalConfiguration configuration, final Reflector reflector) {
        return Optional.ofNullable(reflector.getAnnotation(Naming.class))
            .map(Naming::value).orElse(Optional.ofNullable(configuration.getNamingStrategy())
                .orElse(NamingStrategy.UPPER_UNDERSCORE));
    }
}
