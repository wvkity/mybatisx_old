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
package com.github.mybatisx.backup.event.handle;

import com.github.mybatisx.CommandType;
import com.github.mybatisx.Objects;
import com.github.mybatisx.Strings;
import com.github.mybatisx.backup.additional.AdditionalProcessor;
import com.github.mybatisx.backup.convert.BeanConverter;
import com.github.mybatisx.backup.event.BackupEvent;
import com.github.mybatisx.backup.exception.BackupConvertException;
import com.github.mybatisx.backup.exception.BackupEarlyException;
import com.github.mybatisx.backup.exception.BackupProcessedException;
import com.github.mybatisx.backup.message.Broadcast;
import com.github.mybatisx.backup.meta.BackupMetadata;
import com.github.mybatisx.event.EventPhase;
import com.github.mybatisx.reflection.MetaObjects;
import org.apache.ibatis.reflection.MetaObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 抽象备份数据处理器
 * @author wvkity
 * @created 2021-07-19
 * @since 1.0.0
 */
public abstract class AbstractBackupEventHandler implements BackupEventHandler {

    private static final Logger log = LoggerFactory.getLogger(AbstractBackupEventHandler.class);
    /**
     * 指定Bean缓存
     */
    protected final Map<String, Object> SPECIFIED_PROCESS_BEAN_CACHE = new ConcurrentHashMap<>(64);
    /**
     * 匹配Bean缓存
     */
    protected final Map<String, Object> MATCHES_PROCESS_BEAN_CACHE = new ConcurrentHashMap<>(64);
    /**
     * 上下文对象
     */
    protected ApplicationContext context;
    /**
     * Bean转换器
     */
    protected BeanConverter beanConverter;
    /**
     * 附加处理器
     */
    protected AdditionalProcessor additionalProcessor;
    /**
     * 广播通知
     */
    protected Broadcast broadcast;

    @Override
    public void onRollback(BackupEvent event, EventPhase phase) {
        if (Objects.nonNull(this.broadcast)) {
            this.broadcast.before(event.getSource(), phase);
        }
    }

    @Override
    public void onCommit(BackupEvent event, EventPhase phase) {
        final boolean hasBroadcast = Objects.nonNull(this.broadcast);
        if (hasBroadcast) {
            this.broadcast.before(event.getSource(), phase);
        }
        final BackupMetadata metadata = event.getSource();
        boolean success = false;
        Exception exp = null;
        try {
            // 获取处理Bean
            final Object processBean = this.early(metadata);
            if (hasBroadcast) {
                this.broadcast.early(metadata, null);
            }
            // 数据转换
            final List<Object> dataList = this.convert(metadata);
            if (hasBroadcast) {
                this.broadcast.processed(metadata, dataList, null);
            }
            // 备份
            this.process(metadata, processBean, dataList);
            if (hasBroadcast) {
                this.broadcast.after(metadata, dataList, true, null);
            }
            success = true;
        } catch (BackupEarlyException e) {
            if (hasBroadcast) {
                this.broadcast.early(metadata, e);
            }
            exp = e;
        } catch (BackupConvertException e) {
            if (hasBroadcast) {
                this.broadcast.processed(metadata, null, e);
            }
            exp = e;
        } catch (BackupProcessedException e) {
            exp = e;
            if (hasBroadcast) {
                this.broadcast.after(metadata, null, true, e);
            }
        } catch (Exception e) {
            exp = e;
        } finally {
            if (Objects.nonNull(exp)) {
                log.error("数据备份失败: {}", exp.getMessage(), exp);
            }
            if (Objects.nonNull(this.broadcast)) {
                this.broadcast.completed(metadata, success, exp);
            }
        }
    }

    /**
     * 预备工作(查找处理数据备份Bean)
     * @param metadata {@link BackupMetadata}
     * @return 处理数据备份bean
     */
    protected Object early(final BackupMetadata metadata) {
        String processBeanName = metadata.getProcessBean();
        Object processBean;
        final Class<?> target = metadata.getTarget();
        if (Objects.isNotBlank(processBeanName)) {
            processBean = this.getAndCacheProcessBean(null, processBeanName, true);
            if (Objects.nonNull(processBean)) {
                return processBean;
            }
        }
        final Set<String> beanNames = this.generateBeanName(target);
        for (String beanName : beanNames) {
            processBean = this.getAndCacheProcessBean(target, beanName, false);
            if (Objects.nonNull(processBean)) {
                return processBean;
            }
        }
        throw new BackupEarlyException("Cannot find the corresponding Service Bean based on the specified beanName " +
            "property or the backup data entity class");
    }

    /**
     * 数据转换
     * @param metadata {@link BackupMetadata}
     * @return 备份数据列表
     */
    protected List<Object> convert(final BackupMetadata metadata) {
        try {
            final List<Object> sources = metadata.getSources();
            final String sourceIdProp = metadata.getOrgIdProp();
            final String targetIdProp = metadata.getTargetIdProp();
            final Class<?> target = metadata.getTarget();
            final List<Object> dataList = new ArrayList<>(sources.size());
            final boolean hasAdditional = Objects.nonNull(this.additionalProcessor);
            final CommandType commandType = metadata.getCommandType();
            for (Object source : sources) {
                final Object data = this.beanConverter.convert(source, sourceIdProp, target, targetIdProp);
                if (hasAdditional) {
                    this.additionalProcessor.process(metadata, source, sourceIdProp, target, data, targetIdProp,
                        commandType);
                }
                dataList.add(data);
            }
            return dataList;
        } catch (Exception e) {
            throw new BackupConvertException("Failed to convert metadata to backup data: " + e.getMessage(), e);
        }
    }

    /**
     * 开始备份数据
     * @param metadata    {@link BackupMetadata}
     * @param processBean 数据备份处理Bean
     * @param dataList    数据列表
     */
    protected void process(final BackupMetadata metadata, final Object processBean, final List<Object> dataList) {
        try {
            List<Object> parameters = null;
            if (Objects.nonNull(this.additionalProcessor)) {
                parameters = this.additionalProcessor.convert(metadata, dataList, metadata.getCommandType());
            }
            final Object[] args;
            if (Objects.nonNull(parameters)) {
                args = parameters.toArray(new Object[0]);
            } else {
                args = new Object[]{dataList};
            }
            final Class<?>[] argClasses =
                Objects.isEmpty(metadata.getArgs()) ? new Class<?>[]{dataList.getClass()} : metadata.getArgs();
            final String processMethod = Objects.isNotBlank(metadata.getProcessMethod()) ?
                metadata.getProcessMethod() : "saveBatch";
            final Method method = processBean.getClass().getMethod(processMethod, argClasses);
            method.invoke(processBean, args);
        } catch (Exception e) {
            throw new BackupProcessedException("Data backup failed: " + e.getMessage(), e);
        }
    }

    /**
     * 根据备份类生成processBean对应的名称
     * @param target 备份数据类
     * @return Bean名称列表
     */
    protected Set<String> generateBeanName(final Class<?> target) {
        final String targetName = target.getSimpleName();
        final String lowerCamelName = Strings.firstCharToLower(targetName);
        final Set<String> names = new HashSet<>();
        String beanName = lowerCamelName + "Service";
        names.add(beanName);
        names.add(beanName + "Impl");
        beanName = "default" + targetName + "Service";
        names.add(beanName);
        names.add(beanName + "Impl");
        return names;
    }

    /**
     * 获取并缓存处理数据备份Bean
     * @param target    备份数据类
     * @param beanName  指定的bean名称
     * @param specified 是否指定
     * @return 处理数据备份Bean
     */
    protected Object getAndCacheProcessBean(final Class<?> target, final String beanName, final boolean specified) {
        Object processBean;
        if (specified) {
            processBean = this.SPECIFIED_PROCESS_BEAN_CACHE.get(beanName);
        } else {
            final String cacheKey = target.getCanonicalName();
            processBean = this.MATCHES_PROCESS_BEAN_CACHE.get(cacheKey);
        }
        if (Objects.nonNull(processBean)) {
            return processBean;
        }
        if (this.context.containsBean(beanName)) {
            processBean = this.context.getBean(beanName);
            if (specified) {
                this.SPECIFIED_PROCESS_BEAN_CACHE.putIfAbsent(beanName, processBean);
            } else {
                this.MATCHES_PROCESS_BEAN_CACHE.putIfAbsent(target.getCanonicalName(), processBean);
            }
            return processBean;
        }
        return null;
    }

    /**
     * 处理cglib代理bean
     * @param processBean cglib代理的bean
     * @return 真实bean
     */
    protected Object getCglibProxyTargetObject(final Object processBean) {
        final MetaObject target = MetaObjects.forObject(processBean);
        return target.getValue("CGLIB$CALLBACK_0.advised.targetSource.target");
    }

    /**
     * 处理jdk代理bean
     * @param processBean jdk代理的bean
     * @return 真实bean
     */
    protected Object getJdkDynamicProxyTargetObject(final Object processBean) {
        final MetaObject target = MetaObjects.forObject(processBean);
        return target.getValue("h.advised.targetSource.target");
    }

}
