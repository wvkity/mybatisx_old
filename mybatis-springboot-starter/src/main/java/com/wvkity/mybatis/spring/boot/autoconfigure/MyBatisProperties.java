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
package com.wvkity.mybatis.spring.boot.autoconfigure;

import com.wvkity.mybatis.core.config.MyBatisGlobalConfiguration;
import com.wvkity.mybatis.session.MyBatisConfiguration;
import org.apache.ibatis.scripting.LanguageDriver;
import org.apache.ibatis.session.ExecutorType;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.IOException;
import java.util.Optional;
import java.util.Properties;
import java.util.stream.Stream;

/**
 * MyBatis配置
 * @author wvkity
 * @created 2020-10-01
 * @since 1.0.0
 */
@ConfigurationProperties(prefix = "wvkity.mybatis", ignoreInvalidFields = true)
public class MyBatisProperties {

    private static final ResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();

    /**
     * MyBatis配置文件
     */
    private String configLocation;
    /**
     * MyBatis接口映射文件
     */
    private String[] mapperLocations = new String[]{"classpath*:/mybatis/mapper/**/*.xml"};
    /**
     * 类型包别名(分隔符: [,;\t\n])
     */
    private String typeAliasesPackage;
    /**
     * 过滤类型别名的父类
     */
    private Class<?> typeAliasesSuperType;
    /**
     * 类型处理器包
     */
    private String typeHandlersPackage;
    /**
     * 枚举类型包
     */
    private String typeEnumsPackage;
    /**
     * 是否对MyBatis配置文件执行状态检查
     */
    private boolean checkConfigLocation = false;
    /**
     * 执行模式
     */
    private ExecutorType executorType;
    /**
     * 脚本语言驱动
     */
    private Class<? extends LanguageDriver> defaultScriptingLanguageDriver;
    /**
     * 其他配置
     */
    private Properties configurationProperties;
    /**
     * MyBatis配置对象
     */
    @NestedConfigurationProperty
    private MyBatisConfiguration configuration;
    /**
     * MyBatis自定义全局配置
     */
    @NestedConfigurationProperty
    private MyBatisGlobalConfiguration globalConfiguration;

    public Resource[] resolveMapperLocations() {
        return Stream.of(Optional.ofNullable(this.mapperLocations).orElse(new String[0]))
            .flatMap(location -> Stream.of(getResources(location)))
            .toArray(Resource[]::new);
    }

    private Resource[] getResources(String location) {
        try {
            return resourceResolver.getResources(location);
        } catch (IOException e) {
            return new Resource[0];
        }
    }

    public String getConfigLocation() {
        return configLocation;
    }

    public void setConfigLocation(String configLocation) {
        this.configLocation = configLocation;
    }

    public String[] getMapperLocations() {
        return mapperLocations;
    }

    public void setMapperLocations(String[] mapperLocations) {
        this.mapperLocations = mapperLocations;
    }

    public String getTypeAliasesPackage() {
        return typeAliasesPackage;
    }

    public void setTypeAliasesPackage(String typeAliasesPackage) {
        this.typeAliasesPackage = typeAliasesPackage;
    }

    public Class<?> getTypeAliasesSuperType() {
        return typeAliasesSuperType;
    }

    public void setTypeAliasesSuperType(Class<?> typeAliasesSuperType) {
        this.typeAliasesSuperType = typeAliasesSuperType;
    }

    public String getTypeHandlersPackage() {
        return typeHandlersPackage;
    }

    public void setTypeHandlersPackage(String typeHandlersPackage) {
        this.typeHandlersPackage = typeHandlersPackage;
    }

    public String getTypeEnumsPackage() {
        return typeEnumsPackage;
    }

    public void setTypeEnumsPackage(String typeEnumsPackage) {
        this.typeEnumsPackage = typeEnumsPackage;
    }

    public boolean isCheckConfigLocation() {
        return checkConfigLocation;
    }

    public void setCheckConfigLocation(boolean checkConfigLocation) {
        this.checkConfigLocation = checkConfigLocation;
    }

    public ExecutorType getExecutorType() {
        return executorType;
    }

    public void setExecutorType(ExecutorType executorType) {
        this.executorType = executorType;
    }

    public Class<? extends LanguageDriver> getDefaultScriptingLanguageDriver() {
        return defaultScriptingLanguageDriver;
    }

    public void setDefaultScriptingLanguageDriver(Class<? extends LanguageDriver> defaultScriptingLanguageDriver) {
        this.defaultScriptingLanguageDriver = defaultScriptingLanguageDriver;
    }

    public Properties getConfigurationProperties() {
        return configurationProperties;
    }

    public void setConfigurationProperties(Properties configurationProperties) {
        this.configurationProperties = configurationProperties;
    }

    public MyBatisConfiguration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(MyBatisConfiguration configuration) {
        this.configuration = configuration;
    }

    public MyBatisGlobalConfiguration getGlobalConfiguration() {
        return globalConfiguration;
    }

    public void setGlobalConfiguration(MyBatisGlobalConfiguration globalConfiguration) {
        this.globalConfiguration = globalConfiguration;
    }
}
