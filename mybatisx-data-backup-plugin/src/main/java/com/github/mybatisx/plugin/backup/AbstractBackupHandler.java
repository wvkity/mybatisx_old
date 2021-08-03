/*
 * Copyright (c) 2020-2021, wvkity(wvkity@gmail.com).
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
package com.github.mybatisx.plugin.backup;

import com.github.mybatisx.CommandType;
import com.github.mybatisx.Objects;
import com.github.mybatisx.backup.BackupPolicy;
import com.github.mybatisx.backup.annotation.BackupListener;
import com.github.mybatisx.backup.annotation.BackupTarget;
import com.github.mybatisx.backup.event.BackupEvent;
import com.github.mybatisx.backup.event.BackupFilter;
import com.github.mybatisx.backup.event.BackupNotFilter;
import com.github.mybatisx.backup.event.DefaultBackupEvent;
import com.github.mybatisx.backup.event.publisher.BackupEventPublisher;
import com.github.mybatisx.backup.meta.BackupMetadata;
import com.github.mybatisx.backup.meta.DefaultBackupMetadata;
import com.github.mybatisx.batch.BatchDataWrapper;
import com.github.mybatisx.cache.LocalCache;
import com.github.mybatisx.constant.Constants;
import com.github.mybatisx.event.EventPhase;
import com.github.mybatisx.jsql.parser.SqlParser;
import com.github.mybatisx.plugin.backup.process.QueryProcessor;
import com.github.mybatisx.plugin.handler.AbstractUpdateHandler;
import com.github.mybatisx.plugin.utils.PluginUtil;
import com.github.mybatisx.reflect.Reflections;
import com.github.mybatisx.reflection.MetaObjects;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.keygen.NoKeyGenerator;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 抽象数据备份拦截处理器
 * @author wvkity
 * @created 2021-07-18
 * @since 1.0.0
 */
public abstract class AbstractBackupHandler extends AbstractUpdateHandler implements BackupHandler {

    private static final Logger log = LoggerFactory.getLogger(AbstractBackupHandler.class);
    /**
     * 缓存实现类
     */
    public static final String PROP_KEY_CACHE_CLASS = "backupCacheClass";
    /**
     * 缓存配置项前缀
     */
    public static final String PROP_KEY_CACHE_CFG_PREFIX = "backupCacheCfgPrefix";
    /**
     * 无条件是否拦截备份属性
     */
    public static final String PROP_KEY_BACKUP_NON_CONDITION_FILTER = "backupNonConditionFilter";
    /**
     * 数据备份拦截策略属性
     */
    public static final String PROP_KEY_BACKUP_POLICY = "backupPolicy";
    /**
     * MappedStatement缓存后缀
     */
    protected static final String BACKUP_QUERY_CACHE_SUFFIX = ".plugin-data-backup-query-list";

    /**
     * 无条件是否拦截备份
     */
    protected boolean nonConditionFilter;
    /**
     * 查询处理器
     */
    protected QueryProcessor queryProcessor;
    /**
     * 拦截策略(默认拦截所有)
     */
    protected Set<BackupPolicy> filterPolicies;
    /**
     * 本地缓存
     */
    protected LocalCache<String, MappedStatement> localCache;
    /**
     * 事件发布器
     */
    protected BackupEventPublisher eventPublisher;
    /**
     * SQL解析器
     */
    protected final SqlParser sqlParser = new SqlParser();

    @Override
    protected boolean isEnableReflect() {
        return true;
    }

    @Override
    protected Object handle(Invocation invocation, MappedStatement ms, Object parameter) throws Throwable {
        if (this.filter(ms, parameter) && !this.isAnnotationPresent(ms, BackupNotFilter.class)) {
            final BoundSql bs = ms.getBoundSql(parameter);
            final String originalSql = bs.getSql().trim();
            final String where = sqlParser.parseWhereCondition(originalSql);
            final boolean nonCondition = Objects.isBlank(where);
            if (nonCondition && !this.nonConditionFilter) {
                // 无条件不拦截备份
                return invocation.proceed();
            }
            final BackupFilter filter = this.getMethodAnt(ms, BackupFilter.class);
            final boolean hasFilter = Objects.nonNull(filter);
            final String sourceAlias = hasFilter ? filter.alias() : null;
            final List<Object> entitySources = this.getSourceParameter(parameter, sourceAlias);
            final boolean isInsert = this.isInsert(ms);
            final boolean hasEntitySource = Objects.isNotEmpty(entitySources);
            final Object criteriaTarget = this.getCriteriaTarget(parameter);
            final boolean hasCriteria =
                Objects.nonNull(criteriaTarget) && !Reflections.isSimpleJavaObject(criteriaTarget);
            final boolean isBreak = (!hasFilter || isInsert) && !hasEntitySource && !hasCriteria;
            if (isBreak) {
                // 无拦截注解且获取不到实体参数或者保存操作并且无实体参 => 不拦截
                return invocation.proceed();
            }
            Class<?> sourceClass = null;
            boolean canIntercept;
            if ((canIntercept = hasFilter && !Object.class.equals(filter.source()))) {
                sourceClass = filter.source();
            }
            if (Objects.isNull(sourceClass) && hasEntitySource) {
                sourceClass = entitySources.get(0).getClass();
            }
            if (Objects.isNull(sourceClass) && hasCriteria) {
                final MetaObject metaObject = MetaObjects.forObject(criteriaTarget);
                if (metaObject.hasGetter("entityClass")) {
                    final Object value = metaObject.getValue("entityClass");
                    if (Objects.nonNull(value)) {
                        sourceClass = (Class<?>) value;
                    }
                }
            }
            if (canIntercept || Reflections.isAnnotationPresent(sourceClass, BackupListener.class)) {
                final Executor executor = (Executor) invocation.getTarget();
                // 只拦截Mapper方法上存在@BackFilter和实体类上存在@BackupListener注解
                this.backupParameterHandle(executor, ms, originalSql, where, parameter, entitySources,
                    sourceClass, filter, hasFilter);
            }
        }
        return invocation.proceed();
    }

    /**
     * 数据备份参数份处理
     * @param executor      {@link Executor}
     * @param ms            {@link MappedStatement}
     * @param originalSql   原SQL语句
     * @param condition     条件部分
     * @param parameter     方法参数
     * @param entitySources 元数据参数
     * @param sourceClass   元数据类
     * @param filter        {@link BackupFilter}
     * @param hasFilter     是否存在拦截注解
     */
    protected void backupParameterHandle(final Executor executor, final MappedStatement ms, final String originalSql,
                                         final String condition, final Object parameter,
                                         final List<Object> entitySources, final Class<?> sourceClass,
                                         final BackupFilter filter, final boolean hasFilter) {
        // 优先级 @BackupFilter > @BackupListener
        Class<?> targetClass = null;
        String orgIdProp = null;
        String targetIdProp = null;
        String selectMethod = null;
        String processBean = null;
        String processMethod = null;
        Class<?>[] args = null;
        if (hasFilter) {
            if (!Object.class.equals(filter.target())) {
                targetClass = filter.target();
            }
            orgIdProp = filter.id();
            targetIdProp = filter.targetId();
            selectMethod = filter.selectMethod();
            processBean = filter.processBean();
            processMethod = filter.processMethod();
            args = filter.args();
        }
        final boolean hasListener = Reflections.isAnnotationPresent(sourceClass, BackupListener.class);
        final BackupListener listener;
        final BackupTarget target;
        if (hasListener) {
            listener = sourceClass.getAnnotation(BackupListener.class);
            if (Objects.isBlank(orgIdProp)) {
                orgIdProp = listener.id();
            }
            if (Objects.isBlank(selectMethod)) {
                selectMethod = listener.selectMethod();
            }
            target = listener.value();
            if (Objects.isNull(targetClass)) {
                targetClass = target.value();
            }
            if (Objects.isBlank(targetIdProp)) {
                targetIdProp = target.id();
            }
            if (Objects.isBlank(processBean)) {
                processBean = target.processBean();
            }
            if (Objects.isBlank(processMethod)) {
                processMethod = target.processMethod();
            }
            if (Objects.isEmpty(args)) {
                args = target.args();
            }
        }
        if (Objects.nonNull(targetClass) && !Object.class.equals(targetClass)) {
            // 备份类不为空才拦截处理
            this.backupHandle(executor, ms, originalSql, condition, parameter, entitySources,
                sourceClass, targetClass, orgIdProp, targetIdProp, selectMethod, processBean, processMethod,
                args, CommandType.valueOf(ms.getSqlCommandType().name()));
        }
    }

    /**
     * 数据备份处理
     * @param executor      {@link Executor}
     * @param ms            {@link MappedStatement}
     * @param originalSql   原SQL
     * @param condition     条件部分
     * @param parameter     方法参数
     * @param entitySources 元数据参数
     * @param sourceClass   元数据类
     * @param targetClass   目标类
     * @param orgIdProp     元数据ID属性
     * @param targetIdProp  备份数据对应元数据ID属性
     * @param selectMethod  查询备份数据方法
     * @param processBean   数据备份处理bean名称
     * @param processMethod 数据备份处理的方法名
     * @param args          参数类型列表
     * @param commandType   当前执行类型
     */
    protected void backupHandle(final Executor executor, final MappedStatement ms, final String originalSql,
                                final String condition, final Object parameter, final List<Object> entitySources,
                                final Class<?> sourceClass, final Class<?> targetClass, final String orgIdProp,
                                final String targetIdProp, final String selectMethod, final String processBean,
                                final String processMethod, Class<?>[] args, final CommandType commandType) {
        List<Object> sources = null;
        if (commandType == CommandType.INSERT) {
            sources = entitySources;
        } else {
            try {
                // 优先级: selectMethod > QueryProcessor
                final String msId = ms.getId();
                final String namespace = msId.substring(0, msId.lastIndexOf(Constants.DOT));
                if (Objects.isNotBlank(selectMethod)) {
                    // 从指定方法获取备份方法
                    sources = this.getSourcesFromSpecifiedQueryMethod(executor, ms, namespace, selectMethod,
                        parameter, sourceClass);
                } else if (Objects.nonNull(this.queryProcessor)) {
                    // 优先级: queryList > makeMappedStatement > makeQuerySql
                    sources = this.queryProcessor.queryList(executor, ms, commandType, originalSql, condition,
                        parameter, entitySources, sourceClass, targetClass);
                    if (Objects.isNull(sources)) {
                        final MappedStatement sms = this.queryProcessor.makeMappedStatement(ms, commandType,
                            originalSql, condition, sourceClass, targetClass);
                        if (Objects.nonNull(sms)) {
                            sources = this.query(executor, sms, parameter);
                        } else {
                            final String querySql = this.queryProcessor.makeQuerySql(ms, commandType, originalSql,
                                condition, sourceClass, targetClass);
                            if (Objects.isNotBlank(querySql)) {
                                sources = this.getSourcesFromSystem(executor, ms, parameter, sourceClass, querySql,
                                    condition, commandType);
                            } else {
                                sources = this.getSourcesFromSystem(executor, ms, parameter, sourceClass,
                                    this.makeQuerySourcesSql(originalSql, condition), condition, commandType);
                            }
                        }
                    }
                } else {
                    sources = this.getSourcesFromSystem(executor, ms, parameter, sourceClass,
                        this.makeQuerySourcesSql(originalSql, condition), condition, commandType);
                }
            } catch (Exception e) {
                log.error("Data backup failed: {}", e.getMessage(), e);
            }
        }
        if (Objects.isNotEmpty(sources)) {
            try {
                // 加入事务监听
                final BackupMetadata metadata = DefaultBackupMetadata.Builder.create().uniqueCode(ms.getId())
                    .orgIdProp(orgIdProp).targetIdProp(targetIdProp).target(targetClass).processBean(processBean)
                    .processMethod(processMethod).orgParam(parameter).sources(sources)
                    .commandType(commandType).args(args)
                    .realParam(Objects.isNotEmpty(entitySources) ? entitySources.get(0) : null).build();
                final BackupEvent event = new DefaultBackupEvent(metadata, EventPhase.AFTER_COMMIT,
                    "backupPluginTransactionEvent");
                this.eventPublisher.publishEvent(event);
            } catch (Exception ignore) {
                // ignore
            }
        }
    }

    /**
     * 获取查询备份数据{@link MappedStatement}对象
     * @param ms         {@link MappedStatement}
     * @param smsId      {@link MappedStatement}唯一标识
     * @param isMapParam 是否为Map类型参数
     * @param resultType 返回值类型
     * @param sql        SQL语句
     * @return {@link MappedStatement}
     */
    protected MappedStatement getQueryListMappedStatement(final MappedStatement ms, final String smsId,
                                                          final boolean isMapParam, final Class<?> resultType,
                                                          final String sql) {
        return Optional.ofNullable(this.localCache.get(smsId))
            .orElse(this.newQueryListMappedStatement(ms, smsId, isMapParam, resultType, sql));
    }

    /**
     * 创建查询备份数据{@link MappedStatement}对象
     * @param ms         {@link MappedStatement}
     * @param smsId      {@link MappedStatement}唯一标识
     * @param isMapParam 是否为Map类型参数
     * @param resultType 返回值类型
     * @param sql        SQL语句
     * @return {@link MappedStatement}
     */
    protected MappedStatement newQueryListMappedStatement(final MappedStatement ms, final String smsId,
                                                          final boolean isMapParam, final Class<?> resultType,
                                                          final String sql) {
        final String script = this.script(sql);
        final Configuration configuration = ms.getConfiguration();
        configuration.getDefaultScriptingLanguageInstance();
        final SqlSource sqlSource = configuration.getDefaultScriptingLanguageInstance().createSqlSource(configuration,
            script, isMapParam ? MapperMethod.ParamMap.class : resultType);
        final MappedStatement.Builder it =
            new MappedStatement.Builder(ms.getConfiguration(), smsId, sqlSource, SqlCommandType.SELECT);
        it.resource(ms.getResource());
        it.fetchSize(ms.getFetchSize());
        it.statementType(ms.getStatementType());
        it.keyGenerator(NoKeyGenerator.INSTANCE);
        final String[] props = ms.getKeyProperties();
        if (props != null && props.length > 0) {
            it.keyProperty(String.join(",", props));
        }
        it.timeout(ms.getTimeout());
        it.parameterMap(ms.getParameterMap());
        final List<ResultMap> resultMaps = new ArrayList<>();
        ResultMap resultMap = new ResultMap.Builder(ms.getConfiguration(), ms.getId(),
            resultType, new ArrayList<>(0)).build();
        resultMaps.add(resultMap);
        it.resultMaps(resultMaps);
        it.resultSetType(ms.getResultSetType());
        it.cache(ms.getCache());
        it.flushCacheRequired(false);
        it.useCache(true);
        final MappedStatement rms = it.build();
        this.localCache.put(smsId, rms);
        return rms;
    }

    /**
     * 构建SQL脚本
     * @param sql SQL语句
     * @return SQL脚本
     */
    protected String script(final String sql) {
        if (Objects.isNotBlank(sql)) {
            if (sql.toLowerCase(Locale.ENGLISH).startsWith(Constants.SCRIPT_OPEN)) {
                return sql;
            }
            return "<script>" + sql + "</script>";
        }
        return sql;
    }

    /**
     * 系统自动查询备份数据
     * @param executor    {@link Executor}
     * @param ms          {@link MappedStatement}
     * @param parameter   方法参数
     * @param sourceClass 元数据类
     * @param sql         SQL语句
     * @param condition   条件部分
     * @param commandType 当前执行类型
     * @return 备份数据
     * @throws SQLException 查询备份数据可能会出现异常
     */
    protected List<Object> getSourcesFromSystem(final Executor executor, final MappedStatement ms,
                                                final Object parameter, final Class<?> sourceClass,
                                                final String sql, final String condition,
                                                final CommandType commandType) throws SQLException {
        if (Objects.isBlank(sql)) {
            return null;
        }
        final String msId = ms.getId() + BACKUP_QUERY_CACHE_SUFFIX;
        final BoundSql bs = ms.getBoundSql(parameter);
        final MappedStatement sms =
            this.getQueryListMappedStatement(ms, msId, parameter instanceof Map, sourceClass, sql);
        final Map<String, Object> additionalParameter = PluginUtil.getAdditionalParameter(bs);
        final List<ParameterMapping> pms;
        final List<ParameterMapping> oldPms = bs.getParameterMappings();
        if (commandType == CommandType.UPDATE) {
            // 更新操作存在更新值参数，需要处理，否则参数对不上，导致查询失败
            pms = new ArrayList<>();
            final int cpl = this.getConditionParamLength(condition);
            if (cpl > 0) {
                final int end = oldPms.size();
                final int start = end - cpl;
                for (int i = start; i < end; i++) {
                    pms.add(oldPms.get(i));
                }
            }
        } else {
            pms = oldPms;
        }
        final BoundSql sbs = new BoundSql(ms.getConfiguration(), sql, pms, parameter);
        final CacheKey cacheKey = executor.createCacheKey(ms, parameter, RowBounds.DEFAULT, bs);
        for (Map.Entry<String, Object> entry : additionalParameter.entrySet()) {
            sbs.setAdditionalParameter(entry.getKey(), entry.getValue());
        }
        return executor.query(sms, parameter, RowBounds.DEFAULT, Executor.NO_RESULT_HANDLER, cacheKey, sbs);
    }

    /**
     * 统计条件参数个数
     * @param conditionSql 条件语句
     * @return 参数个数
     */
    protected int getConditionParamLength(final String conditionSql) {
        if (Objects.isNotBlank(conditionSql)) {
            final char[] chars = conditionSql.trim().toCharArray();
            int count = 0;
            for (char c : chars) {
                if (c == '?') {
                    count++;
                }
            }
            return count;
        }
        return 0;
    }

    /**
     * 从指定方法获取备份数据
     * @param executor     {@link Executor}
     * @param ms           {@link MappedStatement}
     * @param namespace    Mapper接口
     * @param selectMethod 查询方法
     * @param parameter    方法参数
     * @param sourceClass  元数据类
     * @return 备份数据
     * @throws SQLException 查询备份数据可能会出现异常
     */
    protected List<Object> getSourcesFromSpecifiedQueryMethod(final Executor executor, final MappedStatement ms,
                                                              final String namespace, final String selectMethod,
                                                              final Object parameter,
                                                              final Class<?> sourceClass) throws SQLException {
        final String msId = namespace + Constants.DOT + selectMethod;
        final Configuration cfg = ms.getConfiguration();
        if (cfg.hasStatement(msId, false)) {
            return this.query(executor, cfg.getMappedStatement(msId, false), parameter);
        } else {
            log.warn("Data backup failed: method '{}' was not found in Mapper interface({}) of metadata entity class " +
                "({}), please add corresponding method.", selectMethod, namespace, sourceClass.getCanonicalName());
            return null;
        }
    }

    /**
     * 构建查询备份数据SQL语句
     * @param originalSql 原SQL语句
     * @param condition   条件部分
     * @return 完整SQL语句
     */
    protected String makeQuerySourcesSql(final String originalSql, final String condition) {
        final String tableName = this.sqlParser.parseTableName(originalSql);
        if (Objects.isNotBlank(tableName)) {
            return "SELECT * FROM " + tableName + (Objects.isBlank(condition) ? Constants.EMPTY :
                (" WHERE " + condition));
        }
        return null;
    }

    /**
     * 执行查询
     * @param executor  {@link Executor}
     * @param sms       {@link MappedStatement}(查询类型)
     * @param parameter 参数
     * @return 备份数据
     * @throws SQLException 查询备份数据可能会出现异常
     */
    protected List<Object> query(final Executor executor, final MappedStatement sms,
                                 final Object parameter) throws SQLException {
        return executor.query(sms, parameter, RowBounds.DEFAULT, Executor.NO_RESULT_HANDLER);
    }

    /**
     * 获取元数据参数
     * @param parameter   方法参数
     * @param entityAlias 实体别名
     * @return 元数据参数列表
     */
    @SuppressWarnings("unchecked")
    protected List<Object> getSourceParameter(final Object parameter, final String entityAlias) {
        return this.getOriginalParameter(parameter, it -> {
            if (Objects.isNotBlank(entityAlias) && it.containsKey(entityAlias)) {
                return it.get(entityAlias);
            } else if (it.containsKey(BatchDataWrapper.PARAM_BATCH_DATA_WRAPPER)) {
                final Object value = it.get(BatchDataWrapper.PARAM_BATCH_DATA_WRAPPER);
                if (value instanceof BatchDataWrapper) {
                    return ((BatchDataWrapper<Object>) value).getData();
                }
            }
            return true;
        });
    }

    @Override
    public boolean filter(MappedStatement ms, Object parameter) {
        return this.specifiedFilterMatches(BackupPolicy.ALL)
            || (this.isInsert(ms) && this.specifiedFilterMatches(BackupPolicy.INSERT))
            || (this.isUpdate(ms) && this.specifiedFilterMatches(BackupPolicy.UPDATE))
            || (this.isDelete(ms) && this.specifiedFilterMatches(BackupPolicy.DELETE));
    }

    /**
     * 是否匹配指定策略
     * @param policy {@link BackupPolicy}
     * @return boolean
     */
    protected boolean specifiedFilterMatches(final BackupPolicy policy) {
        return this.filterPolicies.contains(policy);
    }

    @Override
    public void setProperties(Properties properties) {
        super.setProperties(properties);
        final String ncfStr;
        if (Objects.isNotBlank((ncfStr = properties.getProperty(PROP_KEY_BACKUP_NON_CONDITION_FILTER)))) {
            this.nonConditionFilter = Objects.toBool(ncfStr);
        }
        final String policyStr;
        if (Objects.isNotBlank((policyStr = properties.getProperty(PROP_KEY_BACKUP_POLICY)))) {
            final Set<BackupPolicy> policies = Arrays.stream(policyStr.split(Constants.COMMA))
                .filter(Objects::isNotBlank).map(BackupPolicy::valueOf).collect(Collectors.toSet());
            if (Objects.isNotEmpty(policies)) {
                this.filterPolicies = policies;
            }
        }
        if (Objects.isEmpty(this.filterPolicies)) {
            this.filterPolicies = new HashSet<>(Collections.singletonList(BackupPolicy.ALL));
        }
    }

}
