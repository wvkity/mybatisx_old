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
package com.wvkity.mybatis.core.inject.method.support;

import com.wvkity.mybatis.core.config.MyBatisGlobalConfiguration;
import com.wvkity.mybatis.core.config.MyBatisLocalConfigurationCache;
import com.wvkity.mybatis.core.inject.mapping.script.ScriptBuilder;
import com.wvkity.mybatis.core.inject.mapping.sql.Supplier;
import com.wvkity.mybatis.core.inject.method.MappedMethod;
import com.wvkity.mybatis.core.metadata.Table;
import com.wvkity.mybatis.core.utils.Objects;
import com.wvkity.mybatis.core.utils.Strings;
import com.wvkity.mybatis.session.MyBatisConfiguration;
import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.apache.ibatis.executor.keygen.KeyGenerator;
import org.apache.ibatis.executor.keygen.NoKeyGenerator;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.mapping.StatementType;
import org.apache.ibatis.scripting.LanguageDriver;
import org.apache.ibatis.session.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 抽象映射方法
 * @author wvkity
 * @created 2020-10-04
 * @since 1.0.0
 */
public abstract class AbstractMappedMethod implements MappedMethod {

    private static final Logger log = LoggerFactory.getLogger(AbstractMappedMethod.class);
    /**
     * 截取映射方法正则表达式
     */
    protected final String REGEX_INVOKE_METHOD = "^([a-zA-Z0-9_]+)(Invoker)$";
    /**
     * {@link MapperBuilderAssistant}
     */
    protected MapperBuilderAssistant assistant;
    /**
     * {@link Configuration}
     */
    protected Configuration configuration;
    /**
     * {@link LanguageDriver}
     */
    protected LanguageDriver languageDriver;

    @Override
    public void invoke(MapperBuilderAssistant assistant, Class<?> mapperInterface, Class<?> resultType, Table table) {
        this.assistant = assistant;
        this.configuration = assistant.getConfiguration();
        this.languageDriver = this.configuration.getDefaultScriptingLanguageInstance();
        // 缓存Supplier
        if (this.configuration instanceof MyBatisConfiguration) {
            ((MyBatisConfiguration) this.configuration).addSupplier(getClass());
        }
        // 注入
        this.injectMappedStatement(mapperInterface, resultType, table);
    }

    /**
     * 映射的方法
     * @return 方法名
     */
    public String invokeMethod() {
        final String className = this.getClass().getSimpleName();
        return Strings.firstCharToLower(className.replaceAll(REGEX_INVOKE_METHOD, "$1"));
    }

    /**
     * 添加{@link MappedStatement}对象到容器
     * @param mapperInterface mapper接口
     * @param id              唯一标识
     * @param sqlSource       {@link SqlSource}
     * @param commandType     {@link SqlCommandType}
     * @param parameterType   参数类型
     * @param resultMap       返回结果集
     * @param resultType      返回结果类型
     * @param keyGenerator    {@link KeyGenerator}
     * @param keyProperty     主键属性
     * @param keyColumn       主键列
     * @return {@link MappedStatement}对象
     */
    protected MappedStatement addMappedStatement(final Class<?> mapperInterface, final String id,
                                                 final SqlSource sqlSource, final SqlCommandType commandType,
                                                 final Class<?> parameterType, final String resultMap,
                                                 final Class<?> resultType, final KeyGenerator keyGenerator,
                                                 final String keyProperty, final String keyColumn) {
        final String realId;
        if (Objects.isBlank(id)) {
            realId = invokeMethod();
        } else {
            realId = id;
        }
        final String statementName = mapperInterface.getName() + "." + realId;
        if (hasStatement(statementName)) {
            log.warn("The `{}` MappedStatement object has been loaded into the container by XML or SqlProvider configuration, " +
                "and the SQL injection is automatically ignored.", statementName);
            return this.configuration.getMappedStatement(statementName, false);
        }
        final boolean isSelect = SqlCommandType.SELECT == commandType;
        return this.assistant.addMappedStatement(realId, sqlSource, StatementType.PREPARED, commandType,
            null, null, null, parameterType, resultMap, resultType, null, !isSelect, isSelect, false,
            keyGenerator, keyProperty, keyColumn, this.configuration.getDatabaseId(), this.languageDriver, null);
    }

    /**
     * 添加{@link MappedStatement}对象到容器(保存类型)
     * @param mapperInterface mapper接口
     * @param id              唯一标识
     * @param sqlSource       {@link SqlSource}
     * @param parameterType   参数类型
     * @param keyGenerator    {@link KeyGenerator}
     * @param keyProperty     主键属性
     * @param keyColumn       主键列
     * @return {@link MappedStatement}对象
     */
    protected MappedStatement addInsertMappedStatement(final Class<?> mapperInterface, final String id,
                                                       final SqlSource sqlSource, final Class<?> parameterType,
                                                       final KeyGenerator keyGenerator, final String keyProperty,
                                                       final String keyColumn) {
        return addMappedStatement(mapperInterface, id, sqlSource, SqlCommandType.INSERT, parameterType,
            null, Integer.class, keyGenerator, keyProperty, keyColumn);
    }

    /**
     * 添加{@link MappedStatement}对象到容器(更新类型)
     * @param mapperInterface mapper接口
     * @param id              唯一标识
     * @param sqlSource       {@link SqlSource}
     * @param parameterType   参数类型
     * @return {@link MappedStatement}对象
     */
    protected MappedStatement addUpdateMappedStatement(final Class<?> mapperInterface, final String id,
                                                       final SqlSource sqlSource, final Class<?> parameterType) {
        return addMappedStatement(mapperInterface, id, sqlSource, SqlCommandType.UPDATE, parameterType,
            null, Integer.class, new NoKeyGenerator(), null, null);
    }

    /**
     * 添加{@link MappedStatement}对象到容器(删除类型)
     * @param mapperInterface mapper接口
     * @param id              唯一标识
     * @param sqlSource       {@link SqlSource}
     * @param parameterType   参数类型
     * @return {@link MappedStatement}对象
     */
    protected MappedStatement addDeleteMappedStatement(final Class<?> mapperInterface, final String id,
                                                       final SqlSource sqlSource, final Class<?> parameterType) {
        return addMappedStatement(mapperInterface, id, sqlSource, SqlCommandType.DELETE, parameterType,
            null, Integer.class, new NoKeyGenerator(), null, null);
    }

    /**
     * 添加{@link MappedStatement}对象到容器(查询类型)
     * @param mapperInterface mapper接口
     * @param id              唯一标识
     * @param sqlSource       {@link SqlSource}
     * @param parameterType   参数类型
     * @param resultType      返回值类型
     * @return {@link MappedStatement}对象
     */
    protected MappedStatement addSelectMappedStatement(final Class<?> mapperInterface, final String id,
                                                       final SqlSource sqlSource, final Class<?> parameterType,
                                                       final Class<?> resultType) {
        return addMappedStatement(mapperInterface, id, sqlSource, SqlCommandType.SELECT, parameterType,
            null, resultType, new NoKeyGenerator(), null, null);
    }

    /**
     * 检查容器中是否存在{@link MappedStatement}对象
     * @param statementName 唯一名称
     * @return boolean
     */
    protected boolean hasStatement(final String statementName) {
        return this.configuration.hasStatement(statementName, false);
    }

    /**
     * 创建SQL脚本
     * @param supplier {@link Supplier}
     * @return SQL脚本
     */
    protected String createScript(final Supplier supplier) {
        return ScriptBuilder.build(supplier.get());
    }

    /**
     * 创建{@link SqlSource}对象
     * @param script        SQL脚本
     * @param parameterType 参数类
     * @return {@link SqlSource}对象
     */
    protected SqlSource createSqlSource(final String script, final Class<?> parameterType) {
        return this.languageDriver.createSqlSource(this.configuration, script, parameterType);
    }

    /**
     * 创建{@link SqlSource}对象
     * @param supplier      {@link Supplier}对象
     * @param parameterType 参数类
     * @return {@link SqlSource}对象
     */
    protected SqlSource createSqlSource(final Supplier supplier, final Class<?> parameterType) {
        return createSqlSource(createScript(supplier), parameterType);
    }

    /**
     * 获取全局配置
     * @return {@link MyBatisGlobalConfiguration}
     */
    protected MyBatisGlobalConfiguration getGlobalConfiguration() {
        return MyBatisLocalConfigurationCache.getGlobalConfiguration(this.configuration);
    }

    /**
     * 注入{@link MappedStatement}对象
     * @param mapperInterface mapper接口
     * @param resultType      返回值类型
     * @param table           {@link Table}
     * @return {@link MappedStatement}对象
     */
    public abstract MappedStatement injectMappedStatement(final Class<?> mapperInterface,
                                                          final Class<?> resultType, final Table table);
}
