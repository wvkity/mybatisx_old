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
package com.wvkity.mybatis.core.config;

import com.wvkity.mybatis.annotation.ColumnExt;
import com.wvkity.mybatis.annotation.NamingStrategy;
import com.wvkity.mybatis.annotation.PrimaryKeyStrategy;
import com.wvkity.mybatis.core.filter.Filter;
import com.wvkity.mybatis.core.inject.Injector;
import com.wvkity.mybatis.core.keygen.SequenceGenerator;
import com.wvkity.mybatis.core.naming.DefaultPhysicalNamingConverter;
import com.wvkity.mybatis.core.naming.PhysicalNamingConverter;
import com.wvkity.mybatis.core.parser.EntityParser;
import com.wvkity.mybatis.core.parser.FieldParser;
import com.wvkity.mybatis.core.sequence.Sequence;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.JdbcType;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Optional;

/**
 * MyBatis全局配置
 * @author wvkity
 * @created 2020-10-01
 * @since 1.0.0
 */
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
    private Filter<Method> getMethodFilter;
    /**
     * set方法过滤器
     */
    private Filter<Method> setMethodFilter;
    /**
     * SQL注入器
     */
    private Injector injector;
    /**
     * 默认数据库表/字段命名策略
     */
    private NamingStrategy namingStrategy;
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
     * ID生成器
     */
    private Sequence sequence;
    /**
     * 自动识别主键
     */
    private boolean primaryKeyAutoDiscern = true;
    /**
     * 默认主键属性名
     */
    private String primaryKeyProperty = "id";
    /**
     * 主键生成策略
     */
    private PrimaryKeyStrategy primaryKeyStrategy = PrimaryKeyStrategy.UNDEFINED;
    /**
     * 自动扫描审计属性
     */
    private boolean auditPropertyAutoScan;
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
    private boolean booleanPropertyAutoAddedPrefixedWithIs = true;
    /**
     * 自动映射JDBC类型
     */
    private boolean jdbcTypeAutoMapped;
    /**
     * 是否自动拼接JAVA类名
     */
    private boolean autoSplicingOfJavaType;
    /**
     * 动态SQL中非空检查
     */
    private boolean dynamicSqlNotNullChecking;
    /**
     * 动态SQL中非空值检查
     */
    private boolean dynamicSqlNotEmptyChecking;
    /**
     * 覆盖boolean类型属性默认映射的JDBC类型
     * <p>
     * 当{@code autoMappingJdbcType}等于true时且{@code booleanPropertyOverrideMappedJdbcType}不等{@link JdbcType#UNDEFINED}
     * 会覆盖{@link com.wvkity.mybatis.core.type.JdbcTypeMappingRegistry}中的默认值，如果不需要覆盖请使用{@link ColumnExt#jdbcType()}
     * 指定对应的{@link JdbcType}即可
     * </p>
     */
    private JdbcType booleanPropertyOverrideMappedJdbcType = JdbcType.UNDEFINED;
    /**
     * 逻辑删除属性
     */
    private String logicalDeletedProperty;
    /**
     * 已删除标识值
     */
    private String deletedValue = "1";
    /**
     * 未删除标识值
     */
    private String undeletedValue = "0";
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
    private Class<? extends Map> returnMapImplementClass;
    /**
     * 将内置的拦截器注册到Spring上下文中
     */
    private boolean systemInterceptorRegisteredIntoContext = true;

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

    public Filter<Method> getGetMethodFilter() {
        return getMethodFilter;
    }

    public void setGetMethodFilter(Filter<Method> getMethodFilter) {
        this.getMethodFilter = getMethodFilter;
    }

    public Filter<Method> getSetMethodFilter() {
        return setMethodFilter;
    }

    public void setSetMethodFilter(Filter<Method> setMethodFilter) {
        this.setMethodFilter = setMethodFilter;
    }

    public Injector getInjector() {
        return injector;
    }

    public void setInjector(Injector injector) {
        this.injector = injector;
    }

    public NamingStrategy getNamingStrategy() {
        return namingStrategy;
    }

    public void setNamingStrategy(NamingStrategy namingStrategy) {
        this.namingStrategy = namingStrategy;
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

    public boolean isPrimaryKeyAutoDiscern() {
        return primaryKeyAutoDiscern;
    }

    public void setPrimaryKeyAutoDiscern(boolean primaryKeyAutoDiscern) {
        this.primaryKeyAutoDiscern = primaryKeyAutoDiscern;
    }

    public String getPrimaryKeyProperty() {
        return primaryKeyProperty;
    }

    public void setPrimaryKeyProperty(String primaryKeyProperty) {
        this.primaryKeyProperty = primaryKeyProperty;
    }

    public PrimaryKeyStrategy getPrimaryKeyStrategy() {
        return primaryKeyStrategy;
    }

    public void setPrimaryKeyStrategy(PrimaryKeyStrategy primaryKeyStrategy) {
        this.primaryKeyStrategy = primaryKeyStrategy;
    }

    public boolean isAuditPropertyAutoScan() {
        return auditPropertyAutoScan;
    }

    public void setAuditPropertyAutoScan(boolean auditPropertyAutoScan) {
        this.auditPropertyAutoScan = auditPropertyAutoScan;
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

    public boolean isBooleanPropertyAutoAddedPrefixedWithIs() {
        return booleanPropertyAutoAddedPrefixedWithIs;
    }

    public void setBooleanPropertyAutoAddedPrefixedWithIs(boolean booleanPropertyAutoAddedPrefixedWithIs) {
        this.booleanPropertyAutoAddedPrefixedWithIs = booleanPropertyAutoAddedPrefixedWithIs;
    }

    public boolean isJdbcTypeAutoMapped() {
        return jdbcTypeAutoMapped;
    }

    public void setJdbcTypeAutoMapped(boolean jdbcTypeAutoMapped) {
        this.jdbcTypeAutoMapped = jdbcTypeAutoMapped;
    }

    public boolean isAutoSplicingOfJavaType() {
        return autoSplicingOfJavaType;
    }

    public void setAutoSplicingOfJavaType(boolean autoSplicingOfJavaType) {
        this.autoSplicingOfJavaType = autoSplicingOfJavaType;
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

    public JdbcType getBooleanPropertyOverrideMappedJdbcType() {
        return booleanPropertyOverrideMappedJdbcType;
    }

    public void setBooleanPropertyOverrideMappedJdbcType(JdbcType booleanPropertyOverrideMappedJdbcType) {
        this.booleanPropertyOverrideMappedJdbcType = booleanPropertyOverrideMappedJdbcType;
    }

    public String getLogicalDeletedProperty() {
        return logicalDeletedProperty;
    }

    public void setLogicalDeletedProperty(String logicalDeletedProperty) {
        this.logicalDeletedProperty = logicalDeletedProperty;
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

    public Class<? extends Map> getReturnMapImplementClass() {
        return returnMapImplementClass;
    }

    public MyBatisGlobalConfiguration setReturnMapImplementClass(Class<? extends Map> returnMapImplementClass) {
        this.returnMapImplementClass = returnMapImplementClass;
        return this;
    }

    public boolean isSystemInterceptorRegisteredIntoContext() {
        return systemInterceptorRegisteredIntoContext;
    }

    public void setSystemInterceptorRegisteredIntoContext(boolean systemInterceptorRegisteredIntoContext) {
        this.systemInterceptorRegisteredIntoContext = systemInterceptorRegisteredIntoContext;
    }
}
