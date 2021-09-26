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
package io.github.mybatisx.support.parser;

import io.github.mybatisx.Objects;
import io.github.mybatisx.annotation.Column;
import io.github.mybatisx.annotation.ColumnExt;
import io.github.mybatisx.annotation.Entity;
import io.github.mybatisx.annotation.Executing;
import io.github.mybatisx.annotation.GeneratedValue;
import io.github.mybatisx.annotation.GenerationType;
import io.github.mybatisx.annotation.IdPolicy;
import io.github.mybatisx.annotation.Identity;
import io.github.mybatisx.annotation.LogicDelete;
import io.github.mybatisx.annotation.MultiTenancy;
import io.github.mybatisx.annotation.Naming;
import io.github.mybatisx.annotation.NamingPolicy;
import io.github.mybatisx.annotation.Option;
import io.github.mybatisx.annotation.Priority;
import io.github.mybatisx.annotation.Snowflake;
import io.github.mybatisx.annotation.Version;
import io.github.mybatisx.auditable.AuditPolicy;
import io.github.mybatisx.auditable.PropertyWrapper;
import io.github.mybatisx.auditable.matcher.AuditMatcher;
import io.github.mybatisx.auditable.parser.AuditPropertyAutoScanParser;
import io.github.mybatisx.auditable.parser.AuditPropertyParser;
import io.github.mybatisx.auditable.parser.DefaultAuditPropertyAutoScanParser;
import io.github.mybatisx.auditable.parser.DefaultAuditPropertyParser;
import io.github.mybatisx.basic.builder.ColumnBuilder;
import io.github.mybatisx.basic.builder.TableBuilder;
import io.github.mybatisx.basic.filter.ClassFilter;
import io.github.mybatisx.basic.filter.FieldFilter;
import io.github.mybatisx.basic.filter.Filter;
import io.github.mybatisx.basic.filter.GetMethodFilter;
import io.github.mybatisx.basic.filter.SetMethodFilter;
import io.github.mybatisx.basic.metadata.Field;
import io.github.mybatisx.basic.metadata.Table;
import io.github.mybatisx.basic.naming.DefaultPhysicalNamingConverter;
import io.github.mybatisx.basic.naming.PhysicalNamingConverter;
import io.github.mybatisx.basic.parser.EntityParser;
import io.github.mybatisx.basic.parser.FieldParser;
import io.github.mybatisx.basic.reflect.ReflectMetadata;
import io.github.mybatisx.basic.reflect.Reflector;
import io.github.mybatisx.basic.type.JdbcTypeMappingRegistry;
import io.github.mybatisx.constant.Constants;
import io.github.mybatisx.exception.MyBatisParserException;
import io.github.mybatisx.immutable.ImmutableSet;
import io.github.mybatisx.reflect.Reflections;
import io.github.mybatisx.support.config.MyBatisGlobalConfiguration;
import io.github.mybatisx.support.config.MyBatisLocalConfigurationCache;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.UnknownTypeHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
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
        Integer.class, long.class, Long.class, Date.class, LocalDateTime.class, OffsetDateTime.class,
        Instant.class, Timestamp.class);

    @Override
    public Table parse(Configuration configuration, Class<?> entity, TableBuilder tb) {
        final MyBatisGlobalConfiguration mgc = MyBatisLocalConfigurationCache.getGlobalConfiguration(configuration);
        // 反射解析实体类
        final Reflector reflector = Reflector.of(entity);
        reflector.classFilter(getClassFilter(mgc));
        reflector.fieldFilter(getFieldFilter(mgc));
        reflector.getterFilter(getGetterFilter(mgc));
        reflector.setterFilter(getSetterFilter(mgc));
        reflector.parse();
        // 命名策略
        final NamingPolicy strategy = naming(mgc, reflector);
        final PhysicalNamingConverter namingConverter = namingConverter(mgc);
        tb.strategy(strategy).namingConverter(namingConverter);
        tb.keyWordFormat(mgc.getKeyWordFormat());
        // 处理实体类上的注解
        handleAnnotationOnEntityClass(mgc, reflector, tb);
        // 处理实体属性
        handleEntityClassRelatedAttributes(mgc, strategy, namingConverter, reflector, tb);
        return tb.build();
    }

    /**
     * 处理实体类上的注解
     * @param mgc       全局配置对象
     * @param reflector 反射器
     * @param tb        表对象构建器
     */
    private void handleAnnotationOnEntityClass(final MyBatisGlobalConfiguration mgc,
                                               final Reflector reflector,
                                               final TableBuilder tb) {
        // 处理@Table注解(系统自带或依赖JPA)
        final io.github.mybatisx.annotation.Table table;
        if (Objects.nonNull((table = reflector.getAnnotation(io.github.mybatisx.annotation.Table.class)))) {
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
            tb.prefix(mgc.getTablePrefix());
        }
        if (Objects.isBlank(tb.schema())) {
            tb.schema(mgc.getSchema());
        }
        if (Objects.isBlank(tb.catalog())) {
            tb.catalog(mgc.getCatalog());
        }
    }

    /**
     * 处理实体类的相关属性
     * @param mgc             全局配置对象
     * @param strategy        命名策略
     * @param namingConverter 命名转换器
     * @param reflector       反射器
     * @param tb              表对象构建器
     */
    private void handleEntityClassRelatedAttributes(final MyBatisGlobalConfiguration mgc,
                                                    final NamingPolicy strategy,
                                                    final PhysicalNamingConverter namingConverter,
                                                    final Reflector reflector,
                                                    final TableBuilder tb) {
        final FieldParser fieldParser = getFieldParser(mgc);
        // 解析属性
        final Set<Field> fields = fieldParser.parse(reflector);
        if (Objects.isNotEmpty(fields)) {
            final boolean autoAddedIsPrefixed = mgc.isBooleanPropAutoAddedPrefixedWithIs();
            final Class<?> entityClass = reflector.getClazz();
            fields.forEach(it -> {
                // 数据库表字段构建器
                final ColumnBuilder cb = ColumnBuilder.create()
                    .entity(entityClass)
                    .autoAddedIsPrefixed(autoAddedIsPrefixed)
                    .property(it.getName())
                    .javaType(it.getJavaType())
                    .field(it);
                cb.namingConverter(namingConverter).strategy(strategy).keyWordFormat(mgc.getKeyWordFormat());
                // 处理属性上的注解
                handleAnnotationOnField(mgc, it, reflector, tb, cb);
            });
        }
    }

    /**
     * 处理属性上的注解
     * @param mgc       全局配置对象
     * @param field     属性包装对象
     * @param reflector 反射器
     * @param tb        表对象构建器
     * @param cb        数据库字段对象构建器
     */
    private void handleAnnotationOnField(final MyBatisGlobalConfiguration mgc,
                                         final Field field, final Reflector reflector,
                                         final TableBuilder tb, final ColumnBuilder cb) {
        // 处理@Column/@ColumnExt注解
        handleColumnAnnotation(mgc, field, cb);
        // 处理自动识别主键
        handleAutoDiscernPrimaryKey(mgc, field, tb, cb);
        // 处理多租户标识
        handleMultiTenancyAnnotation(mgc, field, tb, cb);
        // 处理乐观锁注解
        handleVersionAnnotation(mgc, field, tb, cb);
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
            handlePrimaryKeyGenerated(mgc, field, tb, cb);
        } else {
            // 处理逻辑删除注解@LogicDelete
            handleLogicDeleteAnnotation(mgc, field, tb, cb);
            // 处理审计注解
            handleAuditingAnnotations(mgc, field, tb, cb);
        }
        tb.addColumn(cb);
    }

    /**
     * 处理属性上的@Column/@ColumnExt注解
     * @param mgc   全局配置对象
     * @param field 属性包装对象
     * @param cb    数据库字段对象构建器
     */
    private void handleColumnAnnotation(final MyBatisGlobalConfiguration mgc,
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
            cb.useJavaType(ext.javaType() == Option.REQUIRE);
            cb.checkNotNull(ext.notNull() == Option.REQUIRE);
            cb.checkNotEmpty(ext.notEmpty() == Option.REQUIRE);
            Optional.of(ext.jdbcType()).filter(it -> it != JdbcType.UNDEFINED).ifPresent(cb::jdbcType);
            Optional.of(ext.typeHandler()).filter(it -> !it.equals(UnknownTypeHandler.class))
                .ifPresent(cb::typeHandler);
            if (ext.javaType() == Option.CONFIG) {
                cb.useJavaType(mgc.isAutoSplicingJavaType());
            }
            if (ext.notNull() == Option.CONFIG) {
                cb.checkNotNull(mgc.isDynamicSqlNotNullChecking());
            }
            if (ext.notEmpty() == Option.CONFIG) {
                cb.checkNotEmpty(mgc.isDynamicSqlNotEmptyChecking());
            }
        } else {
            cb.useJavaType(mgc.isAutoSplicingJavaType());
            cb.checkNotNull(mgc.isDynamicSqlNotNullChecking());
            cb.checkNotEmpty(mgc.isDynamicSqlNotEmptyChecking());
        }
        if (cb.checkNotEmpty()) {
            cb.checkNotNull(true);
        }
        if (cb.jdbcType() == null && mgc.isJdbcTypeAutoMapping()) {
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
     * @param mgc   全局配置对象
     * @param field 属性包装对象
     * @param tb    数据库表对象构建器
     * @param cb    数据库字段构建器
     */
    private void handleAutoDiscernPrimaryKey(final MyBatisGlobalConfiguration mgc,
                                             final Field field, final TableBuilder tb,
                                             final ColumnBuilder cb) {
        if (!cb.primaryKey() && mgc.isIdAutoScan()) {
            final String primaryKey = mgc.getIdProperty();
            cb.primaryKey(Objects.isNotBlank(primaryKey) && Objects.equals(primaryKey, cb.property()));
        }
    }

    /**
     * 处理多租户注解
     * @param mgc   全局配置对象
     * @param field 属性包装对象
     * @param tb    数据库表对象构建器
     * @param cb    数据库字段构建器
     */
    private void handleMultiTenancyAnnotation(final MyBatisGlobalConfiguration mgc,
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
     * @param mgc   全局配置对象
     * @param field 属性包装对象
     * @param tb    数据库表对象构建器
     * @param cb    数据库字段构建器
     */
    private void handleVersionAnnotation(final MyBatisGlobalConfiguration mgc,
                                         final Field field, final TableBuilder tb,
                                         final ColumnBuilder cb) {
        final boolean isVersion;
        final Version version;
        if ((isVersion = Objects.nonNull(version = field.getAnnotation(Version.class)))
            || field.isAnnotationPresent(JPA_VERSION) || this.checkVersion(mgc, cb.property())) {
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
            if (Reflections.isPrimitiveOrWrapType(cb.javaType())) {
                final int defValue = mgc.getOptimisticLockInitValue();
                if (isVersion) {
                    if (version.value()) {
                        cb.versionInitValue(version.init());
                    } else {
                        cb.versionInitValue(defValue);
                    }
                } else {
                    cb.versionInitValue(defValue);
                }
            }
        }
    }

    /**
     * 检查是否为乐观锁属性
     * @param mgc      全局配置对象
     * @param property 属性
     * @return boolean
     */
    private boolean checkVersion(final MyBatisGlobalConfiguration mgc, final String property) {
        final Set<String> properties = mgc.getOptimisticLockProperties();
        return mgc.isOptimisticLockAutoScan() && Objects.isNotNullElement(properties) && properties.contains(property);
    }

    /**
     * 处理逻辑删除注解
     * @param mgc   全局配置对象
     * @param field 属性包装对象
     * @param tb    数据库表对象构建器
     * @param cb    数据库字段构建器
     */
    private void handleLogicDeleteAnnotation(final MyBatisGlobalConfiguration mgc,
                                             final Field field, final TableBuilder tb,
                                             final ColumnBuilder cb) {
        if (field.isAnnotationPresent(LogicDelete.class)
            || this.checkLogicDelete(mgc, cb.property())) {
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
                    .orElseGet(mgc::getDeletedValue);
                undeletedValue = Optional.of(deletion.undeleted()).filter(Objects::isNotBlank)
                    .orElseGet(mgc::getUndeletedValue);
            } else {
                // 全局
                deletedValue = mgc.getDeletedValue();
                undeletedValue = mgc.getUndeletedValue();
            }
            Objects.requireNonEmpty(deletedValue, "The deleted value cannot be null.");
            Objects.requireNonEmpty(undeletedValue, "The undeleted value cannot be null.");
            // 将值转换成对应的值
            cb.deletedValue(Reflections.convert(cb.javaType(), deletedValue));
            cb.undeletedValue(Reflections.convert(cb.javaType(), undeletedValue));
        }
    }

    /**
     * 检查是否为逻辑删除属性
     * @param mgc      全局配置对象
     * @param property 属性
     * @return boolean
     */
    private boolean checkLogicDelete(final MyBatisGlobalConfiguration mgc, final String property) {
        final Set<String> properties = mgc.getLogicDeleteProperties();
        return mgc.isLogicDeleteAutoScan() && Objects.isNotNullElement(properties)
            && properties.contains(property);
    }

    /**
     * 处理主键生成策略
     * @param mgc   全局配置对象
     * @param field 属性包装对象
     * @param tb    数据库表对象构建器
     * @param cb    数据库字段构建器
     */
    private void handlePrimaryKeyGenerated(final MyBatisGlobalConfiguration mgc,
                                           final Field field, final TableBuilder tb,
                                           final ColumnBuilder cb) {
        if (field.isAnnotationPresent(Identity.class)) {
            handleIdentityAnnotation(mgc, field, tb, cb);
        } else if (field.isAnnotationPresent(GeneratedValue.class)
            || field.isAnnotationPresent(JPA_GENERATED_VALUE)) {
            handleGeneratedValueAnnotation(mgc, field, tb, cb);
        } else if (field.isAnnotationPresent(Snowflake.class)) {
            cb.snowflake(true).executing(Executing.BEFORE);
        }
        // 如果不存在主键生成策略，则根据全局配置主键生成策略填充
        final IdPolicy policy;
        if (!cb.hasPrimaryKeyStrategy()
            && (policy = mgc.getIdPolicy()) != IdPolicy.UNDEFINED) {
            cb.identity(policy == IdPolicy.JDBC || policy == IdPolicy.IDENTITY);
            cb.uuid(policy == IdPolicy.UUID);
            cb.snowflake(policy == IdPolicy.SNOWFLAKE);
        }
    }

    /**
     * 处理@Identity主键生成策略
     * @param mgc   全局配置对象
     * @param field 属性包装对象
     * @param tb    数据库表对象构建器
     * @param cb    数据库字段构建器
     */
    private void handleIdentityAnnotation(final MyBatisGlobalConfiguration mgc,
                                          final Field field, final TableBuilder tb, final ColumnBuilder cb) {
        final Identity identity = field.getAnnotation(Identity.class);
        final Executing executing = identity.executing();
        final boolean isAfter = executing == Executing.AFTER ||
            (executing == Executing.CONFIG && mgc.isGeneratedAfter());
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
     * @param mgc   全局配置对象
     * @param field 属性包装对象
     * @param tb    数据库表对象构建器
     * @param cb    数据库字段构建器
     */
    private void handleGeneratedValueAnnotation(final MyBatisGlobalConfiguration mgc,
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
     * @param mgc   全局配置对象
     * @param field 属性包装对象
     * @param tb    数据库表对象构建器
     * @param cb    数据库字段构建器
     */
    private void handleAuditingAnnotations(final MyBatisGlobalConfiguration mgc,
                                           final Field field, final TableBuilder tb,
                                           final ColumnBuilder cb) {
        final boolean updatable = cb.canAuditing() && cb.updatable();
        final boolean insertable = cb.canAuditing() && cb.insertable();
        if (updatable || insertable) {
            final AuditPropertyParser parser = this.getAuditPropertyParser(mgc);
            final PropertyWrapper pw = new PropertyWrapper(tb.entity(), field.getOriginalField(), field.getName(),
                field.getJavaType(), field.isPrimaryKey(), cb.uuid(), cb.snowflake(), field.getGetter(),
                field.getSetter(), null, field.getAnnotations(), field.getAnnotationCaches());
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
     * @param mgc 全局配置对象
     * @return {@link Filter}对象
     */
    private Filter<Class<?>> getClassFilter(final MyBatisGlobalConfiguration mgc) {
        return Optional.ofNullable(mgc).map(it -> {
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
     * @param mgc 全局配置对象
     * @return {@link Filter}对象
     */
    private Filter<java.lang.reflect.Field> getFieldFilter(final MyBatisGlobalConfiguration mgc) {
        return Optional.ofNullable(mgc).map(it -> {
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
     * @param mgc 全局配置对象
     * @return {@link Filter}对象
     */
    private Filter<Method> getGetterFilter(final MyBatisGlobalConfiguration mgc) {
        return Optional.ofNullable(mgc).map(it -> {
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
     * @param mgc 全局配置对象
     * @return {@link Filter}对象
     */
    private Filter<Method> getSetterFilter(final MyBatisGlobalConfiguration mgc) {
        return Optional.ofNullable(mgc).map(it -> {
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
     * @param mgc {@link MyBatisGlobalConfiguration}
     * @return 属性解析器
     */
    private FieldParser getFieldParser(final MyBatisGlobalConfiguration mgc) {
        return Optional.ofNullable(mgc).map(it -> {
            final FieldParser parser = mgc.getFieldParser();
            if (parser == null) {
                it.setFieldParser(DEF_FIELD_PARSER);
                return DEF_FIELD_PARSER;
            }
            return parser;
        }).orElse(DEF_FIELD_PARSER);
    }

    /**
     * 获取审计属性解析器
     * @param mgc 全局配置对象
     * @return 审计属性解析器
     */
    private AuditPropertyParser getAuditPropertyParser(final MyBatisGlobalConfiguration mgc) {
        return Optional.ofNullable(mgc).map(it -> {
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
     * @param mgc       全局配置对象
     * @param reflector 反射器
     * @return 命名策略
     */
    private NamingPolicy naming(final MyBatisGlobalConfiguration mgc, final Reflector reflector) {
        return Optional.ofNullable(reflector.getAnnotation(Naming.class))
            .map(Naming::value).orElse(Optional.ofNullable(mgc.getNamingPolicy())
                .orElse(NamingPolicy.UPPER_UNDERSCORE));
    }

    /**
     * 获取命名转换器
     * @param mgc 全局配置对象
     * @return {@link PhysicalNamingConverter}
     */
    private PhysicalNamingConverter namingConverter(final MyBatisGlobalConfiguration mgc) {
        if (mgc != null) {
            final PhysicalNamingConverter converter;
            if ((converter = mgc.getPhysicalNamingConverter()) == null) {
                mgc.setPhysicalNamingConverter(DEF_PHYSICAL_NAMING_CONVERTER);
            } else {
                return converter;
            }
        }
        return DEF_PHYSICAL_NAMING_CONVERTER;
    }
}
