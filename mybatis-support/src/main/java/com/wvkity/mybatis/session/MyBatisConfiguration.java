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
package com.wvkity.mybatis.session;

import com.wvkity.mybatis.binding.MyBatisMapperRegistry;
import com.wvkity.mybatis.core.config.MyBatisGlobalConfiguration;
import com.wvkity.mybatis.core.config.MyBatisLocalConfigurationCache;
import com.wvkity.mybatis.core.inject.mapping.sql.Supplier;
import com.wvkity.mybatis.core.inject.mapping.sql.SupplierRegistry;
import com.wvkity.mybatis.executor.resultset.MyBatisDefaultResultSetHandler;
import com.wvkity.mybatis.reflection.factory.MyBatisDefaultObjectFactory;
import com.wvkity.mybatis.scripting.xmltags.MyBatisXMLLanguageDriver;
import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.keygen.KeyGenerator;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMap;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.parsing.XNode;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.scripting.LanguageDriver;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.transaction.Transaction;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiFunction;

/**
 * 重写{@link Configuration}
 * MyBatis配置
 * @author wvkity
 * @created 2020-10-01
 * @since 1.0.0
 */
public class MyBatisConfiguration extends Configuration {

    protected ObjectFactory objectFactory;
    /**
     * 全局配置
     */
    private final AtomicReference<MyBatisGlobalConfiguration> globalConfigurationRef = new AtomicReference<>();
    protected final MyBatisMapperRegistry myBatisMapperRegistry = new MyBatisMapperRegistry(this);
    protected final SupplierRegistry supplierRegistry = new SupplierRegistry(this);
    protected final Map<String, Cache> caches = new StrictMap<>("Caches collection");
    protected final Map<String, ResultMap> resultMaps = new StrictMap<>("Result Maps collection");
    protected final Map<String, ParameterMap> parameterMaps = new StrictMap<>("Parameter Maps collection");
    protected final Map<String, KeyGenerator> keyGenerators = new StrictMap<>("Key Generators collection");
    protected final Map<String, XNode> sqlFragments = new StrictMap<>("XML fragments parsed from previous mappers");
    protected final Map<String, MappedStatement> mappedStatements =
        new StrictMap<MappedStatement>("Mapped Statements collection")
            .conflictMessageProducer((savedValue, targetValue) ->
                ". please check " + savedValue.getResource() + " and " + targetValue.getResource());

    public MyBatisConfiguration() {
        this(MyBatisLocalConfigurationCache.newInstance());
    }

    public MyBatisConfiguration(Environment environment) {
        this();
        this.environment = environment;
    }

    public MyBatisConfiguration(MyBatisGlobalConfiguration globalConfiguration) {
        super();
        // 默认开启驼峰转换
        this.setMapUnderscoreToCamelCase(true);
        this.setGlobalConfiguration(Optional.ofNullable(globalConfiguration)
            .orElse(MyBatisLocalConfigurationCache.newInstance()));
        this.objectFactory = new MyBatisDefaultObjectFactory(this.globalConfigurationRef);
        super.setObjectFactory(this.objectFactory);
        // TODO registry JDK8+ TIME API(jsr-310)
    }

    @Override
    public void setDefaultScriptingLanguage(Class<? extends LanguageDriver> driver) {
        if (driver == null) {
            super.setDefaultScriptingLanguage(MyBatisXMLLanguageDriver.class);
        } else {
            super.setDefaultScriptingLanguage(driver);
        }
    }

    @Override
    public ObjectFactory getObjectFactory() {
        return objectFactory;
    }

    @Override
    public void setObjectFactory(ObjectFactory objectFactory) {
        this.objectFactory = objectFactory;
    }

    @Override
    public MyBatisMapperRegistry getMapperRegistry() {
        return this.myBatisMapperRegistry;
    }

    @Override
    public <T> void addMapper(Class<T> type) {
        this.myBatisMapperRegistry.addMapper(type);
    }

    @Override
    public void addMappers(String packageName) {
        this.myBatisMapperRegistry.addMappers(packageName);
    }

    @Override
    public void addMappers(String packageName, Class<?> superType) {
        this.myBatisMapperRegistry.addMappers(packageName, superType);
    }

    @Override
    public <T> T getMapper(Class<T> type, SqlSession sqlSession) {
        return this.myBatisMapperRegistry.getMapper(type, sqlSession);
    }

    @Override
    public boolean hasMapper(Class<?> type) {
        return this.myBatisMapperRegistry.hasMapper(type);
    }

    public SupplierRegistry getSupplierRegistry() {
        return this.supplierRegistry;
    }

    public boolean hasSupplier(final Class<?> type) {
        return this.supplierRegistry.hasSupplier(type);
    }

    public <T> void addSupplier(final Class<T> type) {
        this.supplierRegistry.addSupplier(type);
    }

    public <T> Supplier getSupplier(final Class<T> type, final Object... args) {
        return this.supplierRegistry.getSupplier(type, this, args);
    }

    @Override
    public ResultSetHandler newResultSetHandler(Executor executor, MappedStatement mappedStatement,
                                                RowBounds rowBounds, ParameterHandler parameterHandler,
                                                ResultHandler resultHandler, BoundSql boundSql) {
        final ResultSetHandler resultSetHandler = new MyBatisDefaultResultSetHandler(executor, mappedStatement,
            parameterHandler, resultHandler, boundSql, rowBounds);
        return (ResultSetHandler) interceptorChain.pluginAll(resultSetHandler);
    }

    @Override
    public void addKeyGenerator(String id, KeyGenerator keyGenerator) {
        this.keyGenerators.put(id, keyGenerator);
    }

    @Override
    public Collection<String> getKeyGeneratorNames() {
        return this.keyGenerators.keySet();
    }

    @Override
    public KeyGenerator getKeyGenerator(String id) {
        return this.keyGenerators.get(id);
    }

    @Override
    public Collection<KeyGenerator> getKeyGenerators() {
        return this.keyGenerators.values();
    }

    @Override
    public void addCache(Cache cache) {
        this.caches.put(cache.getId(), cache);
    }

    @Override
    public Collection<String> getCacheNames() {
        return this.caches.keySet();
    }

    @Override
    public Cache getCache(String id) {
        return this.caches.get(id);
    }

    @Override
    public Collection<Cache> getCaches() {
        return this.caches.values();
    }

    @Override
    public boolean hasCache(String id) {
        return this.caches.containsKey(id);
    }

    @Override
    public void addResultMap(ResultMap rm) {
        this.resultMaps.put(rm.getId(), rm);
        checkLocallyForDiscriminatedNestedResultMaps(rm);
        checkGloballyForDiscriminatedNestedResultMaps(rm);
    }

    @Override
    public Collection<String> getResultMapNames() {
        return this.resultMaps.keySet();
    }

    @Override
    public Collection<ResultMap> getResultMaps() {
        return this.resultMaps.values();
    }

    @Override
    public boolean hasResultMap(String id) {
        return this.resultMaps.containsKey(id);
    }

    @Override
    public void addParameterMap(ParameterMap pm) {
        this.parameterMaps.put(pm.getId(), pm);
    }

    @Override
    public Collection<String> getParameterMapNames() {
        return this.parameterMaps.keySet();
    }

    @Override
    public Collection<ParameterMap> getParameterMaps() {
        return this.parameterMaps.values();
    }

    @Override
    public ParameterMap getParameterMap(String id) {
        return this.parameterMaps.get(id);
    }

    @Override
    public boolean hasParameterMap(String id) {
        return this.parameterMaps.containsKey(id);
    }

    @Override
    public Map<String, XNode> getSqlFragments() {
        return this.sqlFragments;
    }

    @Override
    public void addMappedStatement(MappedStatement ms) {
        if (this.mappedStatements.containsKey(ms.getId())) {
            return;
        }
        this.mappedStatements.put(ms.getId(), ms);
    }

    @Override
    public Collection<String> getMappedStatementNames() {
        this.buildAllStatements();
        return this.mappedStatements.keySet();
    }

    @Override
    public Collection<MappedStatement> getMappedStatements() {
        this.buildAllStatements();
        return this.mappedStatements.values();
    }

    @Override
    public MappedStatement getMappedStatement(String id) {
        return this.mappedStatements.get(id);
    }

    @Override
    public MappedStatement getMappedStatement(String id, boolean validateIncompleteStatements) {
        if (validateIncompleteStatements) {
            this.buildAllStatements();
        }
        return this.mappedStatements.get(id);
    }

    @Override
    public boolean hasStatement(String statementName) {
        return this.mappedStatements.containsKey(statementName);
    }

    @Override
    public boolean hasStatement(String statementName, boolean validateIncompleteStatements) {
        if (validateIncompleteStatements) {
            this.buildAllStatements();
        }
        return this.mappedStatements.containsKey(statementName);
    }

    @Override
    public Executor newExecutor(Transaction transaction, ExecutorType executorType) {
        return super.newExecutor(transaction, executorType);
    }

    // Slow but a one time cost. A better solution is welcome.
    protected void checkGloballyForDiscriminatedNestedResultMaps(ResultMap rm) {
        if (rm.hasNestedResultMaps()) {
            for (Map.Entry<String, ResultMap> entry : resultMaps.entrySet()) {
                ResultMap entryResultMap = entry.getValue();
                if (entryResultMap != null) {
                    if (!entryResultMap.hasNestedResultMaps() && entryResultMap.getDiscriminator() != null) {
                        Collection<String> discriminatedResultMapNames = entryResultMap.getDiscriminator().getDiscriminatorMap().values();
                        if (discriminatedResultMapNames.contains(rm.getId())) {
                            entryResultMap.forceNestedResultMaps();
                        }
                    }
                }
            }
        }
    }

    // Slow but a one time cost. A better solution is welcome.
    protected void checkLocallyForDiscriminatedNestedResultMaps(ResultMap rm) {
        if (!rm.hasNestedResultMaps() && rm.getDiscriminator() != null) {
            for (Map.Entry<String, String> entry : rm.getDiscriminator().getDiscriminatorMap().entrySet()) {
                String discriminatedResultMapName = entry.getValue();
                if (hasResultMap(discriminatedResultMapName)) {
                    ResultMap discriminatedResultMap = resultMaps.get(discriminatedResultMapName);
                    if (discriminatedResultMap.hasNestedResultMaps()) {
                        rm.forceNestedResultMaps();
                        break;
                    }
                }
            }
        }
    }

    protected static class StrictMap<V> extends HashMap<String, V> {

        private static final long serialVersionUID = -4950446264854982944L;
        private final String name;
        private BiFunction<V, V, String> conflictMessageProducer;

        public StrictMap(String name, int initialCapacity, float loadFactor) {
            super(initialCapacity, loadFactor);
            this.name = name;
        }

        public StrictMap(String name, int initialCapacity) {
            super(initialCapacity);
            this.name = name;
        }

        public StrictMap(String name) {
            super();
            this.name = name;
        }

        public StrictMap(String name, Map<String, ? extends V> m) {
            super(m);
            this.name = name;
        }

        /**
         * Assign a function for producing a conflict error message when contains value with the same key.
         * <p>
         * function arguments are 1st is saved value and 2nd is target value.
         * @param conflictMessageProducer A function for producing a conflict error message
         * @return a conflict error message
         * @since 3.5.0
         */
        public StrictMap<V> conflictMessageProducer(BiFunction<V, V, String> conflictMessageProducer) {
            this.conflictMessageProducer = conflictMessageProducer;
            return this;
        }

        @Override
        @SuppressWarnings("unchecked")
        public V put(String key, V value) {
            if (containsKey(key)) {
                throw new IllegalArgumentException(name + " already contains value for " + key
                    + (conflictMessageProducer == null ? "" : conflictMessageProducer.apply(super.get(key), value)));
            }
            if (key.contains(".")) {
                final String shortKey = getShortName(key);
                if (super.get(shortKey) == null) {
                    super.put(shortKey, value);
                } else {
                    super.put(shortKey, (V) new Ambiguity(shortKey));
                }
            }
            return super.put(key, value);
        }

        @Override
        public V get(Object key) {
            V value = super.get(key);
            if (value == null) {
                throw new IllegalArgumentException(name + " does not contain value for " + key);
            }
            if (value instanceof StrictMap.Ambiguity) {
                throw new IllegalArgumentException(((Ambiguity) value).getSubject() + " is ambiguous in " + name
                    + " (try using the full name including the namespace, or rename one of the entries)");
            }
            return value;
        }

        protected static class Ambiguity {
            final private String subject;

            public Ambiguity(String subject) {
                this.subject = subject;
            }

            public String getSubject() {
                return subject;
            }
        }

        private String getShortName(String key) {
            final String[] keyParts = key.split("\\.");
            return keyParts[keyParts.length - 1];
        }
    }

    public MyBatisGlobalConfiguration getGlobalConfiguration() {
        return this.globalConfigurationRef.get();
    }

    public void setGlobalConfiguration(MyBatisGlobalConfiguration globalConfiguration) {
        final MyBatisGlobalConfiguration local = this.globalConfigurationRef.get();
        this.globalConfigurationRef.compareAndSet(local, globalConfiguration);
    }
}
