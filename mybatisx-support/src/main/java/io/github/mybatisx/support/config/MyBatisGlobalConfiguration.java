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
package io.github.mybatisx.support.config;

import io.github.mybatisx.annotation.ColumnExt;
import io.github.mybatisx.annotation.IdPolicy;
import io.github.mybatisx.annotation.NamingPolicy;
import io.github.mybatisx.auditable.parser.AuditPropertyAutoScanParser;
import io.github.mybatisx.auditable.parser.AuditPropertyParser;
import io.github.mybatisx.basic.filter.Filter;
import io.github.mybatisx.basic.inject.Injector;
import io.github.mybatisx.basic.keygen.KeyGenerator;
import io.github.mybatisx.basic.keygen.SequenceGenerator;
import io.github.mybatisx.basic.naming.DefaultPhysicalNamingConverter;
import io.github.mybatisx.basic.naming.PhysicalNamingConverter;
import io.github.mybatisx.basic.parser.EntityParser;
import io.github.mybatisx.basic.parser.FieldParser;
import io.github.mybatisx.basic.type.JdbcTypeMappingRegistry;
import io.github.sequence.Sequence;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.JdbcType;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * MyBatis全局配置
 * @author wvkity
 * @created 2020-10-01
 * @since 1.0.0
 */
@SuppressWarnings("rawtypes")
public class MyBatisGlobalConfiguration {

    /**
     * {@link SqlSessionFactory}对象
     */
    private SqlSessionFactory factory;
    /**
     * 实体解析器
     */
    private EntityParser entityParser;
    /**
     * 属性解析器
     */
    private FieldParser fieldParser;
    /**
     * 审计属性解析器
     */
    private AuditPropertyParser auditPropertyParser;
    /**
     * 审计属性自动识别解析器
     */
    private AuditPropertyAutoScanParser auditPropertyAutoScanParser;
    /**
     * 父类类过滤器
     */
    private Filter<Class<?>> classFilter;
    /**
     * 属性过滤器
     */
    private Filter<Field> fieldFilter;
    /**
     * get方法过滤器
     */
    private Filter<Method> getterFilter;
    /**
     * set方法过滤器
     */
    private Filter<Method> setterFilter;
    /**
     * SQL注入器
     */
    private Injector injector;
    /**
     * 默认数据库表/字段命名策略
     */
    private NamingPolicy namingPolicy;
    /**
     * 默认命名转换器
     */
    private PhysicalNamingConverter physicalNamingConverter = new DefaultPhysicalNamingConverter();
    /**
     * 插入后生成主键
     */
    private boolean generatedAfter;
    /**
     * 序列生成器
     */
    private SequenceGenerator sequenceGenerator;
    /**
     * 雪花ID生成器
     */
    private Sequence sequence;
    /**
     * ID生成器
     */
    private KeyGenerator keyGenerator;
    /**
     * 自动扫描主键
     */
    private boolean idAutoScan = true;
    /**
     * 默认主键属性名
     */
    private String idProperty = "id";
    /**
     * 默认主键生成策略
     */
    private IdPolicy idPolicy = IdPolicy.UNDEFINED;
    /**
     * 自动扫描审计属性
     */
    private boolean auditAutoScan;
    /**
     * 使用简单类型
     */
    private boolean useSimpleType = true;
    /**
     * 枚举类型转成简单类型
     */
    private boolean enumAsSimpleType;
    /**
     * Boolean类型属性映射的数据库表字段是否自动添加IS前缀
     */
    private boolean booleanPropAutoAddedPrefixedWithIs = true;
    /**
     * 自动映射JDBC类型
     */
    private boolean jdbcTypeAutoMapping;
    /**
     * 是否自动拼接JAVA类名
     */
    private boolean autoSplicingJavaType;
    /**
     * 动态SQL中非空检查
     */
    private boolean dynamicSqlNotNullChecking = true;
    /**
     * 动态SQL中非空值检查
     */
    private boolean dynamicSqlNotEmptyChecking;
    /**
     * 覆盖boolean类型属性默认映射的JDBC类型
     * <p>
     * 当{@code autoMappingJdbcType}等于true时且{@code booleanPropertyOverrideMappedJdbcType}不等{@link JdbcType#UNDEFINED}
     * 会覆盖{@link JdbcTypeMappingRegistry}中的默认值，如果不需要覆盖请使用{@link ColumnExt#jdbcType()}
     * 指定对应的{@link JdbcType}即可
     * </p>
     */
    private JdbcType booleanPropOverrideJdbcType = JdbcType.UNDEFINED;
    /**
     * 自动扫描逻辑删除属性
     */
    private boolean logicDeleteAutoScan;
    /**
     * 逻辑删除属性
     */
    private Set<String> logicDeleteProperties = new HashSet<>(Collections.singletonList("deleted"));
    /**
     * 逻辑删除属性值是否自动初始化(保存操作)
     */
    private boolean logicDeletedInit;
    /**
     * 已删除标识值
     */
    private String deletedValue = "1";
    /**
     * 未删除标识值
     */
    private String undeletedValue = "0";
    /**
     * 更新操作是否自动添加逻辑删除条件
     */
    private boolean updateAutoAddedLogicDeleteCondition;
    /**
     * 自动扫描乐观锁属性
     */
    private boolean optimisticLockAutoScan;
    /**
     * 乐观锁属性
     */
    private Set<String> optimisticLockProperties = new HashSet<>(Collections.singletonList("version"));
    /**
     * 乐观锁属性是否自动初始化(保存操作)
     */
    private boolean optimisticLockInit;
    /**
     * 乐观锁默认初始值(数字类型)
     */
    private int optimisticLockInitValue = 1;
    /**
     * 关键词格式化模板
     */
    private String keyWordFormat;
    /**
     * 表前缀
     */
    private String tablePrefix;
    /**
     * 数据库catalog
     */
    private String catalog;
    /**
     * 数据库schema
     */
    private String schema;
    /**
     * 返回值为Map类型时，采用具体{@link Map}实现类
     */
    private Class<? extends Map> mapImplementClass;

    public MyBatisGlobalConfiguration() {
    }

    /**
     * 缓存当前对象
     * @param factory {@link SqlSessionFactory}
     * @return {@link SqlSessionFactory}
     */
    public SqlSessionFactory cacheSelf(final SqlSessionFactory factory) {
        Optional.ofNullable(factory).ifPresent(it -> cacheSelf(it.getConfiguration()));
        return factory;
    }

    /**
     * 缓存当前对象
     * @param configuration {@link Configuration}
     */
    public void cacheSelf(final Configuration configuration) {
        Optional.ofNullable(configuration).ifPresent(it ->
            MyBatisLocalConfigurationCache.cacheGlobalConfiguration(it, this));
    }

    public SqlSessionFactory getFactory() {
        return factory;
    }

    public void setFactory(SqlSessionFactory factory) {
        this.factory = factory;
    }

    public EntityParser getEntityParser() {
        return entityParser;
    }

    public void setEntityParser(EntityParser entityParser) {
        this.entityParser = entityParser;
    }

    public FieldParser getFieldParser() {
        return fieldParser;
    }

    public void setFieldParser(FieldParser fieldParser) {
        this.fieldParser = fieldParser;
    }

    public AuditPropertyParser getAuditPropertyParser() {
        return auditPropertyParser;
    }

    public void setAuditPropertyParser(AuditPropertyParser auditPropertyParser) {
        this.auditPropertyParser = auditPropertyParser;
    }

    public AuditPropertyAutoScanParser getAuditPropertyAutoScanParser() {
        return auditPropertyAutoScanParser;
    }

    public void setAuditPropertyAutoScanParser(AuditPropertyAutoScanParser auditPropertyAutoScanParser) {
        this.auditPropertyAutoScanParser = auditPropertyAutoScanParser;
    }

    public Filter<Class<?>> getClassFilter() {
        return classFilter;
    }

    public void setClassFilter(Filter<Class<?>> classFilter) {
        this.classFilter = classFilter;
    }

    public Filter<Field> getFieldFilter() {
        return fieldFilter;
    }

    public void setFieldFilter(Filter<Field> fieldFilter) {
        this.fieldFilter = fieldFilter;
    }

    public Filter<Method> getGetterFilter() {
        return getterFilter;
    }

    public void setGetterFilter(Filter<Method> getterFilter) {
        this.getterFilter = getterFilter;
    }

    public Filter<Method> getSetterFilter() {
        return setterFilter;
    }

    public void setSetterFilter(Filter<Method> setterFilter) {
        this.setterFilter = setterFilter;
    }

    public Injector getInjector() {
        return injector;
    }

    public void setInjector(Injector injector) {
        this.injector = injector;
    }

    public NamingPolicy getNamingPolicy() {
        return namingPolicy;
    }

    public void setNamingPolicy(NamingPolicy namingPolicy) {
        this.namingPolicy = namingPolicy;
    }

    public PhysicalNamingConverter getPhysicalNamingConverter() {
        return physicalNamingConverter;
    }

    public void setPhysicalNamingConverter(PhysicalNamingConverter physicalNamingConverter) {
        this.physicalNamingConverter = physicalNamingConverter;
    }

    public boolean isGeneratedAfter() {
        return generatedAfter;
    }

    public void setGeneratedAfter(boolean generatedAfter) {
        this.generatedAfter = generatedAfter;
    }

    public SequenceGenerator getSequenceGenerator() {
        return sequenceGenerator;
    }

    public void setSequenceGenerator(SequenceGenerator sequenceGenerator) {
        this.sequenceGenerator = sequenceGenerator;
    }

    public Sequence getSequence() {
        return sequence;
    }

    public void setSequence(Sequence sequence) {
        this.sequence = sequence;
    }

    public KeyGenerator getKeyGenerator() {
        return keyGenerator;
    }

    public void setKeyGenerator(KeyGenerator keyGenerator) {
        this.keyGenerator = keyGenerator;
    }

    public boolean isIdAutoScan() {
        return idAutoScan;
    }

    public void setIdAutoScan(boolean idAutoScan) {
        this.idAutoScan = idAutoScan;
    }

    public String getIdProperty() {
        return idProperty;
    }

    public void setIdProperty(String idProperty) {
        this.idProperty = idProperty;
    }

    public IdPolicy getIdPolicy() {
        return idPolicy;
    }

    public void setIdPolicy(IdPolicy idPolicy) {
        this.idPolicy = idPolicy;
    }

    public boolean isAuditAutoScan() {
        return auditAutoScan;
    }

    public void setAuditAutoScan(boolean auditAutoScan) {
        this.auditAutoScan = auditAutoScan;
    }

    public boolean isLogicDeletedInit() {
        return logicDeletedInit;
    }

    public void setLogicDeletedInit(boolean logicDeletedInit) {
        this.logicDeletedInit = logicDeletedInit;
    }

    public boolean isUseSimpleType() {
        return useSimpleType;
    }

    public void setUseSimpleType(boolean useSimpleType) {
        this.useSimpleType = useSimpleType;
    }

    public boolean isEnumAsSimpleType() {
        return enumAsSimpleType;
    }

    public void setEnumAsSimpleType(boolean enumAsSimpleType) {
        this.enumAsSimpleType = enumAsSimpleType;
    }

    public boolean isBooleanPropAutoAddedPrefixedWithIs() {
        return booleanPropAutoAddedPrefixedWithIs;
    }

    public void setBooleanPropAutoAddedPrefixedWithIs(boolean booleanPropAutoAddedPrefixedWithIs) {
        this.booleanPropAutoAddedPrefixedWithIs = booleanPropAutoAddedPrefixedWithIs;
    }

    public boolean isJdbcTypeAutoMapping() {
        return jdbcTypeAutoMapping;
    }

    public void setJdbcTypeAutoMapping(boolean jdbcTypeAutoMapping) {
        this.jdbcTypeAutoMapping = jdbcTypeAutoMapping;
    }

    public boolean isAutoSplicingJavaType() {
        return autoSplicingJavaType;
    }

    public void setAutoSplicingJavaType(boolean autoSplicingJavaType) {
        this.autoSplicingJavaType = autoSplicingJavaType;
    }

    public boolean isDynamicSqlNotNullChecking() {
        return dynamicSqlNotNullChecking;
    }

    public void setDynamicSqlNotNullChecking(boolean dynamicSqlNotNullChecking) {
        this.dynamicSqlNotNullChecking = dynamicSqlNotNullChecking;
    }

    public boolean isDynamicSqlNotEmptyChecking() {
        return dynamicSqlNotEmptyChecking;
    }

    public void setDynamicSqlNotEmptyChecking(boolean dynamicSqlNotEmptyChecking) {
        this.dynamicSqlNotEmptyChecking = dynamicSqlNotEmptyChecking;
    }

    public JdbcType getBooleanPropOverrideJdbcType() {
        return booleanPropOverrideJdbcType;
    }

    public void setBooleanPropOverrideJdbcType(JdbcType booleanPropOverrideJdbcType) {
        this.booleanPropOverrideJdbcType = booleanPropOverrideJdbcType;
    }

    public Set<String> getLogicDeleteProperties() {
        return logicDeleteProperties;
    }

    public void setLogicDeleteProperties(Set<String> logicDeleteProperties) {
        this.logicDeleteProperties = logicDeleteProperties;
    }

    public boolean isLogicDeleteAutoScan() {
        return logicDeleteAutoScan;
    }

    public void setLogicDeleteAutoScan(boolean logicDeleteAutoScan) {
        this.logicDeleteAutoScan = logicDeleteAutoScan;
    }

    public String getDeletedValue() {
        return deletedValue;
    }

    public void setDeletedValue(String deletedValue) {
        this.deletedValue = deletedValue;
    }

    public String getUndeletedValue() {
        return undeletedValue;
    }

    public void setUndeletedValue(String undeletedValue) {
        this.undeletedValue = undeletedValue;
    }

    public boolean isUpdateAutoAddedLogicDeleteCondition() {
        return updateAutoAddedLogicDeleteCondition;
    }

    public void setUpdateAutoAddedLogicDeleteCondition(boolean updateAutoAddedLogicDeleteCondition) {
        this.updateAutoAddedLogicDeleteCondition = updateAutoAddedLogicDeleteCondition;
    }

    public boolean isOptimisticLockAutoScan() {
        return optimisticLockAutoScan;
    }

    public void setOptimisticLockAutoScan(boolean optimisticLockAutoScan) {
        this.optimisticLockAutoScan = optimisticLockAutoScan;
    }

    public Set<String> getOptimisticLockProperties() {
        return optimisticLockProperties;
    }

    public void setOptimisticLockProperties(Set<String> optimisticLockProperties) {
        this.optimisticLockProperties = optimisticLockProperties;
    }

    public boolean isOptimisticLockInit() {
        return optimisticLockInit;
    }

    public void setOptimisticLockInit(boolean optimisticLockInit) {
        this.optimisticLockInit = optimisticLockInit;
    }

    public int getOptimisticLockInitValue() {
        return optimisticLockInitValue;
    }

    public void setOptimisticLockInitValue(int optimisticLockInitValue) {
        this.optimisticLockInitValue = optimisticLockInitValue;
    }

    public String getKeyWordFormat() {
        return keyWordFormat;
    }

    public void setKeyWordFormat(String keyWordFormat) {
        this.keyWordFormat = keyWordFormat;
    }

    public String getTablePrefix() {
        return tablePrefix;
    }

    public void setTablePrefix(String tablePrefix) {
        this.tablePrefix = tablePrefix;
    }

    public String getCatalog() {
        return catalog;
    }

    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public Class<? extends Map> getMapImplementClass() {
        return mapImplementClass;
    }

    public MyBatisGlobalConfiguration setMapImplementClass(Class<? extends Map> mapImplementClass) {
        this.mapImplementClass = mapImplementClass;
        return this;
    }

}
