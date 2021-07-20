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
package com.github.mybatisx.support.parser;

import com.github.mybatisx.Objects;
import com.github.mybatisx.annotation.Column;
import com.github.mybatisx.annotation.ColumnExt;
import com.github.mybatisx.annotation.Entity;
import com.github.mybatisx.annotation.Executing;
import com.github.mybatisx.annotation.GeneratedValue;
import com.github.mybatisx.annotation.GenerationType;
import com.github.mybatisx.annotation.IdPolicy;
import com.github.mybatisx.annotation.Identity;
import com.github.mybatisx.annotation.LogicDelete;
import com.github.mybatisx.annotation.MultiTenancy;
import com.github.mybatisx.annotation.Naming;
import com.github.mybatisx.annotation.NamingPolicy;
import com.github.mybatisx.annotation.Option;
import com.github.mybatisx.annotation.Priority;
import com.github.mybatisx.annotation.Snowflake;
import com.github.mybatisx.annotation.Version;
import com.github.mybatisx.auditable.AuditPolicy;
import com.github.mybatisx.auditable.PropertyWrapper;
import com.github.mybatisx.auditable.matcher.AuditMatcher;
import com.github.mybatisx.auditable.parser.AuditPropertyAutoScanParser;
import com.github.mybatisx.auditable.parser.AuditPropertyParser;
import com.github.mybatisx.auditable.parser.DefaultAuditPropertyAutoScanParser;
import com.github.mybatisx.auditable.parser.DefaultAuditPropertyParser;
import com.github.mybatisx.basic.builder.ColumnBuilder;
import com.github.mybatisx.basic.builder.TableBuilder;
import com.github.mybatisx.basic.filter.ClassFilter;
import com.github.mybatisx.basic.filter.FieldFilter;
import com.github.mybatisx.basic.filter.Filter;
import com.github.mybatisx.basic.filter.GetMethodFilter;
import com.github.mybatisx.basic.filter.SetMethodFilter;
import com.github.mybatisx.basic.metadata.Field;
import com.github.mybatisx.basic.metadata.Table;
import com.github.mybatisx.basic.naming.DefaultPhysicalNamingConverter;
import com.github.mybatisx.basic.naming.PhysicalNamingConverter;
import com.github.mybatisx.basic.parser.EntityParser;
import com.github.mybatisx.basic.parser.FieldParser;
import com.github.mybatisx.basic.reflect.ReflectMetadata;
import com.github.mybatisx.basic.reflect.Reflector;
import com.github.mybatisx.basic.type.JdbcTypeMappingRegistry;
import com.github.mybatisx.constant.Constants;
import com.github.mybatisx.exception.MyBatisParserException;
import com.github.mybatisx.immutable.ImmutableSet;
import com.github.mybatisx.reflect.Reflections;
import com.github.mybatisx.support.config.MyBatisGlobalConfiguration;
import com.github.mybatisx.support.config.MyBatisLocalConfigurationCache;
import org.apache.ibatis.session.Configuration;
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
import java.util.HashSet;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 默认实体解析器
 * @author wvkity
 * @created 2020-10-03
 * @since 1.0.0
 */
public class DefaultEntityParser implements EntityParser, Constants {

    private static final Logger log = LoggerFactory.getLogger(DefaultEntityParser.class);
    /**
     * 默认命名转换器
     */
    private static final PhysicalNamingConverter DEF_PHYSICAL_NAMING_CONVERTER = new DefaultPhysicalNamingConverter();
    /**
     * 默认字段解析器
     */
    private static final FieldParser DEF_FIELD_PARSER = new DefaultFieldParser();
    /**
     * 默认父类过滤器
     */
    private static final Filter<Class<?>> DEF_CLASS_FILTER = new ClassFilter();
    /**
     * 默认属性过滤器
     */
    private static final Filter<java.lang.reflect.Field> DEF_FIELD_FILTER = FieldFilter.of(true, true);
    /**
     * 默认get方法过滤器
     */
    private static final Filter<Method> DEF_GET_METHOD_FILTER = new GetMethodFilter();
    /**
     * 默认set方法过滤器
     */
    private static final Filter<Method> DEF_SET_METHOD_FILTER = new SetMethodFilter();
    /**
     * 默认审计属性解析器
     */
    private static final AuditPropertyParser DEF_AUDIT_PROP_PARSER = new DefaultAuditPropertyParser(false, null);
    /**
     * 乐观锁支持的类型
     */
    private static final Set<Class<?>> OPTIMISTIC_LOCK_SUPPORT_CLASSES = ImmutableSet.construct(int.class,
        Integer.class, long.class, Long.class, Date.class, LocalTime.class, LocalDate.class, LocalDateTime.class,
        OffsetTime.class, OffsetDateTime.class, ZonedDateTime.class, Instant.class, Timestamp.class);

    @Override
    public Table parse(Configuration configuration, Class<?> entity, TableBuilder tb) {
        final MyBatisGlobalConfiguration globalConfiguration =
            MyBatisLocalConfigurationCache.getGlobalConfiguration(configuration);
        // 反射解析实体类
        final Reflector reflector = Reflector.of(entity);
        reflector.classFilter(getClassFilter(globalConfiguration));
        reflector.fieldFilter(getFieldFilter(globalConfiguration));
        reflector.getterFilter(getGetterFilter(globalConfiguration));
        reflector.setterFilter(getSetterFilter(globalConfiguration));
        reflector.parse();
        // 命名策略
        final NamingPolicy strategy = naming(globalConfiguration, reflector);
        final PhysicalNamingConverter namingConverter = namingConverter(globalConfiguration);
        tb.strategy(strategy).namingConverter(namingConverter);
        tb.keyWordFormat(globalConfiguration.getKeyWordFormat());
        // 处理实体类上的注解
        handleAnnotationOnEntityClass(globalConfiguration, reflector, tb);
        // 处理实体属性
        handleEntityClassRelatedAttributes(globalConfiguration, strategy, namingConverter, reflector, tb);
        return tb.build();
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
        final com.github.mybatisx.annotation.Table table;
        if (Objects.nonNull((table = reflector.getAnnotation(com.github.mybatisx.annotation.Table.class)))) {
            tb.name(table.name()).catalog(table.catalog()).schema(table.schema()).prefix(table.prefix());
        } else if (reflector.isAnnotationPresent(JPA_TABLE)) {
            // JPA @Table
            final ReflectMetadata metadata = reflector.getReflectMetadata(JPA_TABLE);
            tb.name(metadata.stringValue(JPA_TABLE_PROP_NAME)).catalog(metadata.stringValue(JPA_TABLE_PROP_CATALOG));
            tb.schema(metadata.stringValue(JPA_TABLE_PROP_SCHEMA));
        }
        if (Objects.isBlank(tb.name())) {
            final Entity entity;
            if (Objects.nonNull((entity = reflector.getAnnotation(Entity.class)))) {
                tb.name(entity.name());
            } else if (reflector.isAnnotationPresent(JPA_ENTITY)) {
                // JPA @Entity
                final ReflectMetadata metadata = reflector.getReflectMetadata(JPA_ENTITY);
                tb.name(metadata.stringValue(JPA_ENTITY_PROP_NAME));
            }
        }
        if (Objects.isBlank(tb.prefix())) {
            tb.prefix(configuration.getTablePrefix());
        }
        if (Objects.isBlank(tb.schema())) {
            tb.schema(configuration.getSchema());
        }
        if (Objects.isBlank(tb.catalog())) {
            tb.catalog(configuration.getCatalog());
        }
    }

    /**
     * 处理实体类的相关属性
     * @param configuration   全局配置对象
     * @param strategy        命名策略
     * @param namingConverter 命名转换器
     * @param reflector       反射器
     * @param tb              表对象构建器
     */
    private void handleEntityClassRelatedAttributes(final MyBatisGlobalConfiguration configuration,
                                                    final NamingPolicy strategy,
                                                    final PhysicalNamingConverter namingConverter,
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
                    .entity(entityClass)
                    .autoAddedIsPrefixed(autoAddedIsPrefixed)
                    .property(it.getName())
                    .javaType(it.getJavaType())
                    .field(it);
                cb.namingConverter(namingConverter).strategy(strategy).keyWordFormat(configuration.getKeyWordFormat());
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
        handleMultiTenancyAnnotation(configuration, field, tb, cb);
        // 处理乐观锁注解
        handleVersionAnnotation(configuration, field, tb, cb);
        // 检查是否为主键
        if (cb.primaryKey()) {
            cb.insertable(true).updatable(false);
            final boolean priority = field.isAnnotationPresent(Priority.class);
            final boolean hasPrimaryKey = tb.hasPrimaryKey();
            if (hasPrimaryKey) {
                tb.onlyOneId(false);
            }
            if (!hasPrimaryKey || priority) {
                tb.idColumn(cb).idProperty(cb.property());
            }
            // 处理主键生成策略
            handlePrimaryKeyGenerated(configuration, field, tb, cb);
        } else {
            // 处理逻辑删除注解@LogicDelete
            handleLogicDeleteAnnotation(configuration, field, tb, cb);
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
        cb.primaryKey(field.isPrimaryKey());
        // 处理@Column注解
        final Column column;
        if (Objects.nonNull((column = field.getAnnotation(Column.class)))) {
            cb.column(column.name()).insertable(column.insertable()).updatable(column.updatable());
        } else if (field.isAnnotationPresent(JPA_COLUMN)) {
            // JPA @Column
            final ReflectMetadata metadata = field.getReflectMetadata(JPA_COLUMN);
            cb.column(metadata.stringValue(JPA_ENTITY_PROP_NAME));
            cb.insertable(metadata.booleanValue(JPA_COLUMN_PROP_INSERTABLE));
            cb.updatable(metadata.booleanValue(JPA_COLUMN_PROP_UPDATABLE));
        }
        // 处理@ColumnExt注解
        final ColumnExt ext;
        if (Objects.nonNull((ext = field.getAnnotation(ColumnExt.class)))) {
            cb.blob(ext.blob());
            if (Objects.isBlank(cb.column())) {
                cb.column(ext.name());
            }
            cb.useJavaType(ext.javaType() == Option.ENABLE);
            cb.checkNotNull(ext.notNull() == Option.ENABLE);
            cb.checkNotEmpty(ext.notEmpty() == Option.ENABLE);
            Optional.of(ext.jdbcType()).filter(it -> it != JdbcType.UNDEFINED).ifPresent(cb::jdbcType);
            Optional.of(ext.typeHandler()).filter(it -> !it.equals(UnknownTypeHandler.class))
                .ifPresent(cb::typeHandler);
            if (ext.javaType() == Option.CONFIG) {
                cb.useJavaType(configuration.isAutoSplicingJavaType());
            }
            if (ext.notNull() == Option.CONFIG) {
                cb.checkNotNull(configuration.isDynamicSqlNotNullChecking());
            }
            if (ext.notEmpty() == Option.CONFIG) {
                cb.checkNotEmpty(configuration.isDynamicSqlNotEmptyChecking());
            }
        } else {
            cb.useJavaType(configuration.isAutoSplicingJavaType());
            cb.checkNotNull(configuration.isDynamicSqlNotNullChecking());
            cb.checkNotEmpty(configuration.isDynamicSqlNotEmptyChecking());
        }
        if (cb.jdbcType() == null && configuration.isJdbcTypeAutoMapping()) {
            cb.jdbcType(JdbcTypeMappingRegistry.getJdbcType(field.getJavaType(), JdbcType.UNDEFINED));
        }
        if (cb.javaType().isPrimitive()) {
            // 基本数据类型禁止做非空检查
            cb.checkNotNull(false).checkNotEmpty(false);
            // 基本数据类型警告
            log.warn("The \"{}\" attribute in the entity class \"{}\" is defined as a primitive type. " +
                    "The primitive type is not null at any time in dynamic SQL because it has a default value. It is " +
                    "recommended to modify the primitive type to the corresponding wrapper type.", cb.property(),
                cb.entity().getName());
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
        if (!cb.primaryKey() && configuration.isIdAutoScan()) {
            final String primaryKey = configuration.getIdProperty();
            cb.primaryKey(Objects.isNotBlank(primaryKey) && Objects.equals(primaryKey, cb.property()));
        }
    }

    /**
     * 处理多租户注解
     * @param configuration 全局配置对象
     * @param field         属性包装对象
     * @param tb            数据库表对象构建器
     * @param cb            数据库字段构建器
     */
    private void handleMultiTenancyAnnotation(final MyBatisGlobalConfiguration configuration,
                                              final Field field, final TableBuilder tb,
                                              final ColumnBuilder cb) {
        if (field.isAnnotationPresent(MultiTenancy.class)) {
            if (cb.primaryKey()) {
                throw new MyBatisParserException("The attribute \"" + cb.property() + "\" of the entity class \"" +
                    tb.entity().getName() + "\" is the primary key. Adding \"@MultiTenancy\" annotation is not " +
                    "supported.");
            }
            if (tb.multiTenant()) {
                throw new MyBatisParserException("The entity class \"" + tb.entity().getName() + "\" already has " +
                    "the tenant attribute \"" + tb.multiTenantColumn().property() + "\". " +
                    "The entity class supports an tenant attribute, so attribute \"" + cb.property() + "\" " +
                    "does not support adding the \"@MultiTenancy\" annotation.");
            }
            // 租户字段禁止更新
            cb.multiTenant(true).updatable(false);
            tb.multiTenantColumn(cb).multiTenant(true);
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
            if (cb.primaryKey()) {
                throw new MyBatisParserException("The attribute \"" + cb.property() + "\" of the entity class \"" +
                    tb.entity().getName() + "\" is the primary key. The primary key is not updatable. " +
                    "Adding \"@version\" annotation is not supported.");
            }
            if (!cb.updatable()) {
                throw new MyBatisParserException("The attribute \"" + cb.property() + "\" of the entity class \"" +
                    tb.entity().getName() + "\" is not updatable and adding \"@version\" annotation is not supported.");
            }
            if (!OPTIMISTIC_LOCK_SUPPORT_CLASSES.contains(cb.javaType())) {
                throw new MyBatisParserException("The type of the \"" + cb.property() + "\" attribute of the " +
                    "entity class \"" + tb.entity().getName() + "\" is not within the type range specified by " +
                    "the optimistic lock. Adding the \"@version\" annotation is not supported.");
            }
            if (tb.optimisticLockColumn() != null) {
                throw new MyBatisParserException("The entity class \"" + tb.entity().getName() + "\" already has " +
                    "the optimistic lock attribute \"" + tb.optimisticLockColumn().property() + "\". " +
                    "The entity class supports an optimistic lock attribute, so attribute \"" + cb.property() + "\" " +
                    "does not support adding the \"@version\" annotation.");
            }
            cb.version(true);
            tb.optimisticLockColumn(cb);
        }
    }

    /**
     * 处理逻辑删除注解
     * @param configuration 全局配置对象
     * @param field         属性包装对象
     * @param tb            数据库表对象构建器
     * @param cb            数据库字段构建器
     */
    private void handleLogicDeleteAnnotation(final MyBatisGlobalConfiguration configuration,
                                             final Field field, final TableBuilder tb,
                                             final ColumnBuilder cb) {
        final String logicalDeletedProperty = configuration.getLogicDeleteProperty();
        if (field.isAnnotationPresent(LogicDelete.class)
            || Objects.equals(logicalDeletedProperty, cb.property())) {
            // 检查是否已存在逻辑删除属性
            if (tb.logicDelete()) {
                throw new MyBatisParserException("There are already \"" + tb.logicDeleteColumn()
                    .property() + "\" attributes in \"" + tb.entity().getName()
                    + "\" entity class identified as logical deleted. Only one deleted attribute " +
                    "can exist in an entity class. Please check the entity class attributes.");
            }
            cb.logicDelete(true);
            tb.logicDelete(true).logicDeleteColumn(cb);
            final LogicDelete deletion = field.getAnnotation(LogicDelete.class);
            final String deletedValue;
            final String undeletedValue;
            if (Objects.nonNull(deletion)) {
                // 注解 > 全局
                deletedValue = Optional.of(deletion.deleted()).filter(Objects::isNotBlank)
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
            cb.deletedValue(Reflections.convert(cb.javaType(), deletedValue));
            cb.undeletedValue(Reflections.convert(cb.javaType(), undeletedValue));
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
            cb.snowflake(true).executing(Executing.BEFORE);
        }
        // 如果不存在主键生成策略，则根据全局配置主键生成策略填充
        final IdPolicy policy;
        if (!cb.hasPrimaryKeyStrategy()
            && (policy = configuration.getIdPolicy()) != IdPolicy.UNDEFINED) {
            cb.identity(policy == IdPolicy.JDBC || policy == IdPolicy.IDENTITY);
            cb.uuid(policy == IdPolicy.UUID);
            cb.snowflake(policy == IdPolicy.SNOWFLAKE);
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
            cb.identity(true).executing(Executing.AFTER).generator("JDBC");
        } else if (isAfter) {
            cb.identity(true).executing(Executing.AFTER).generator("");
        } else {
            if (Objects.isBlank(identity.sequence())) {
                throw new MyBatisParserException("Invalid \"@Identity\" annotation exists on the \"" + cb.property()
                    + "\" attribute of the entity class \"" + tb.entity().getName() + "\".");
            }
            cb.identity(true).executing(Executing.BEFORE).generator(identity.sequence());
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
            cb.uuid(true);
        } else if (PRIMARY_KEY_JDBC.equalsIgnoreCase(generator)) {
            cb.identity(true).generator(generator.toLowerCase(Locale.ENGLISH));
        } else if (PRIMARY_KEY_SNOWFLAKE.equalsIgnoreCase(generator)) {
            cb.snowflake(true);
        } else {
            if (isIdentity) {
                cb.identity(true).generator(generator);
            }
            if (isSequence && Objects.isNotBlank(generator)) {
                cb.sequence(generator);
            } else {
                throw new MyBatisParserException("Invalid \"@GeneratedValue\" annotation exists on the \"" + cb.property()
                    + "\" attribute of the entity class \"" + tb.entity().getName() + "\", " +
                    "The \"@GeneratedValue\" annotation supports the following form: " +
                    "\n\t\t\t 1.@GeneratedValue(generator = \"UUID\")" +
                    "\n\t\t\t 2.@GeneratedValue(generator = \"JDBC\")" +
                    "\n\t\t\t 3.@GeneratedValue(generator = \"SNOWFLAKE\")" +
                    "\n\t\t\t 4.@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = \"SequenceName\")" +
                    "\n\t\t\t 5.@GeneratedValue(strategy = GenerationType.IDENTITY, generator = \"[MySql, MSSQL.." +
                    ".]\")");
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
        final boolean updatable = cb.canAuditing() && cb.updatable();
        final boolean insertable = cb.canAuditing() && cb.insertable();
        if (updatable || insertable) {
            final AuditPropertyParser parser = this.getAuditPropertyParser(configuration);
            final PropertyWrapper pw = new PropertyWrapper(tb.entity(), field.getOriginalField(),
                field.getName(), field.getJavaType(), field.isPrimaryKey(), field.getGetter(), field.getSetter(),
                null, field.getAnnotations(), field.getAnnotationCaches());
            final AuditMatcher am = parser.parse(pw);
            if (Objects.nonNull(am) && am.canMatches()) {
                cb.createdById(am.isCreatedById()).createdByName(am.isCreatedByName())
                    .createdDate(am.isCreatedDate()).lastModifiedById(am.isLastModifiedById())
                    .lastModifiedByName(am.isLastModifiedByName()).lastModifiedDate(am.isLastModifiedDate())
                    .deletedById(am.isDeletedById()).deletedByName(am.isDeletedByName())
                    .deletedDate(am.isDeletedDate()).auditType(am.getType().ordinal());
                final Set<AuditPolicy> policies = am.getPolicies();
                if (Objects.isNotEmpty(policies)) {
                    cb.auditPolicies(policies.stream().map(AuditPolicy::ordinal).collect(Collectors.toSet()));
                } else {
                    cb.auditPolicies(new HashSet<>(0));
                }
            }
        }
    }

    /**
     * 获取类型过滤器
     * @param configuration 全局配置对象
     * @return {@link Filter}对象
     */
    private Filter<Class<?>> getClassFilter(final MyBatisGlobalConfiguration configuration) {
        return Optional.ofNullable(configuration).map(it -> {
            final Filter<Class<?>> filter = it.getClassFilter();
            if (filter == null) {
                it.setClassFilter(DEF_CLASS_FILTER);
                return DEF_CLASS_FILTER;
            }
            return filter;
        }).orElse(DEF_CLASS_FILTER);
    }


    /**
     * 获取属性过滤器
     * <p>排除static或final修饰、@Transient注解的、非简单类型、或枚举类型不转换成简单类型的属性</p>
     * @param configuration 全局配置对象
     * @return {@link Filter}对象
     */
    private Filter<java.lang.reflect.Field> getFieldFilter(final MyBatisGlobalConfiguration configuration) {
        return Optional.ofNullable(configuration).map(it -> {
            final Filter<java.lang.reflect.Field> filter = it.getFieldFilter();
            if (filter == null) {
                final FieldFilter newFilter = FieldFilter.of(it.isUseSimpleType(), it.isEnumAsSimpleType());
                it.setFieldFilter(newFilter);
                return newFilter;
            }
            return filter;
        }).orElse(DEF_FIELD_FILTER);
    }

    /**
     * 获取get方法过滤器
     * @param configuration 全局配置对象
     * @return {@link Filter}对象
     */
    private Filter<Method> getGetterFilter(final MyBatisGlobalConfiguration configuration) {
        return Optional.ofNullable(configuration).map(it -> {
            final Filter<Method> filter = it.getGetterFilter();
            if (filter == null) {
                it.setGetterFilter(DEF_GET_METHOD_FILTER);
                return DEF_GET_METHOD_FILTER;
            }
            return filter;
        }).orElse(DEF_GET_METHOD_FILTER);
    }

    /**
     * 获取set方法过滤器
     * @param configuration 全局配置对象
     * @return {@link Filter}对象
     */
    private Filter<Method> getSetterFilter(final MyBatisGlobalConfiguration configuration) {
        return Optional.ofNullable(configuration).map(it -> {
            final Filter<Method> filter = it.getSetterFilter();
            if (filter == null) {
                it.setSetterFilter(DEF_SET_METHOD_FILTER);
                return DEF_SET_METHOD_FILTER;
            }
            return filter;
        }).orElse(DEF_SET_METHOD_FILTER);
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
                it.setFieldParser(DEF_FIELD_PARSER);
                return DEF_FIELD_PARSER;
            }
            return parser;
        }).orElse(DEF_FIELD_PARSER);
    }

    /**
     * 获取审计属性解析器
     * @param configuration – MyBatisGlobalConfiguration
     * @return 审计属性解析器
     */
    private AuditPropertyParser getAuditPropertyParser(final MyBatisGlobalConfiguration configuration) {
        return Optional.ofNullable(configuration).map(it -> {
            AuditPropertyParser parser = it.getAuditPropertyParser();
            if (parser == null) {
                AuditPropertyAutoScanParser autoScanParser = it.getAuditPropertyAutoScanParser();
                if (autoScanParser == null) {
                    autoScanParser = new DefaultAuditPropertyAutoScanParser();
                    it.setAuditPropertyAutoScanParser(autoScanParser);
                }
                parser = new DefaultAuditPropertyParser(it.isAuditAutoScan(), autoScanParser);
                it.setAuditPropertyParser(parser);
            }
            return parser;
        }).orElse(DEF_AUDIT_PROP_PARSER);
    }

    /**
     * 获取命名策略
     * @param configuration 全局配置对象
     * @param reflector     反射器
     * @return 命名策略
     */
    private NamingPolicy naming(final MyBatisGlobalConfiguration configuration, final Reflector reflector) {
        return Optional.ofNullable(reflector.getAnnotation(Naming.class))
            .map(Naming::value).orElse(Optional.ofNullable(configuration.getNamingPolicy())
                .orElse(NamingPolicy.UPPER_UNDERSCORE));
    }

    /**
     * 获取命名转换器
     * @param configuration {@link MyBatisGlobalConfiguration}
     * @return {@link PhysicalNamingConverter}
     */
    private PhysicalNamingConverter namingConverter(final MyBatisGlobalConfiguration configuration) {
        if (configuration != null) {
            final PhysicalNamingConverter converter;
            if ((converter = configuration.getPhysicalNamingConverter()) == null) {
                configuration.setPhysicalNamingConverter(DEF_PHYSICAL_NAMING_CONVERTER);
            } else {
                return converter;
            }
        }
        return DEF_PHYSICAL_NAMING_CONVERTER;
    }
}
