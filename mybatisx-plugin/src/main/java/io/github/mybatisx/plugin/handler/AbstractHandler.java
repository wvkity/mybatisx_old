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
package io.github.mybatisx.plugin.handler;

import io.github.mybatisx.Objects;
import io.github.mybatisx.constant.Constants;
import io.github.mybatisx.plugin.filter.Filter;
import io.github.mybatisx.reflect.Reflections;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 抽象拦截处理器
 * @author wvkity
 * @created 2020-10-25
 * @since 1.0.0
 */
public abstract class AbstractHandler implements Handler, Filter {

    protected static final String PARAM_COLLECTION = "collection";
    protected static final String PARAM_LIST = "list";
    protected static final String PARAM_ARRAY = "array";
    protected static final String PARAM_ENTITY = "entity";
    /**
     * Mapper接口方法注解缓存
     */
    protected final AntSingletonShareCache antShareCache = AntSingletonShareCache.getInstance();
    /**
     * 相关配置
     */
    protected Properties properties;

    /**
     * 获取当前执行的方法名
     * @param ms {@link MappedStatement}对象
     * @return 方法名
     */
    protected String execMethod(final MappedStatement ms) {
        final String msId = ms.getId();
        final int index = msId.lastIndexOf(".");
        return index < 0 ? msId : msId.substring(index + 1);
    }

    /**
     * 是否反射Mapper方法
     * @return boolean
     */
    protected boolean isEnableReflect() {
        return false;
    }

    /**
     * 检查Mapper方法是否存在指定注解
     * @param ms     {@link MappedStatement}
     * @param target 指定注解
     * @return boolean
     */
    protected boolean isAnnotationPresent(final MappedStatement ms, final Class<? extends Annotation> target) {
        return this.antShareCache.isAnnotationPresent(ms.getId(), target, this.isEnableReflect());
    }

    /**
     * 缓存方法上的注解
     * @param ms {@link MappedStatement}
     * @return 注解列表
     */
    protected Set<Annotation> cacheMethodAnnotation(final MappedStatement ms) {
        if (this.isEnableReflect()) {
            return this.antShareCache.getAndCacheAnnotations(ms.getId());
        }
        return null;
    }

    /**
     * 获取Mapper方法上指定的注解实例
     * @param ms     {@link MappedStatement}
     * @param target 指定注解类
     * @param <T>    注解类型
     * @return 注解实例
     */
    protected <T extends Annotation> T getMethodAnt(final MappedStatement ms, final Class<T> target) {
        return this.antShareCache.getAnnotation(ms.getId(), target, this.isEnableReflect());
    }

    /**
     * 检查是否为保存操作
     * @param ms {@link MappedStatement}
     * @return boolean
     */
    protected boolean isInsert(final MappedStatement ms) {
        return ms.getSqlCommandType() == SqlCommandType.INSERT;
    }

    /**
     * 检查是否为更新操作
     * @param ms {@link MappedStatement}
     * @return boolean
     */
    protected boolean isUpdate(final MappedStatement ms) {
        return ms.getSqlCommandType() == SqlCommandType.UPDATE;
    }

    /**
     * 检查是否为删除操作
     * @param ms {@link MappedStatement}
     * @return boolean
     */
    protected boolean isDelete(final MappedStatement ms) {
        return ms.getSqlCommandType() == SqlCommandType.DELETE;
    }

    /**
     * 获取原始参数列表
     * @param parameter 方法参数
     * @param action    {@link Function} 当返回值为null或false时，则返回null，否则继续后续判断
     * @return 参数列表
     */
    @SuppressWarnings("unchecked")
    protected List<Object> getOriginalParameter(final Object parameter,
                                                final Function<Map<String, Object>, Object> action) {
        if (parameter instanceof Collection) {
            return this.toList((Collection<Object>) parameter);
        } else if (Objects.isArray(parameter)) {
            return this.toList(Arrays.asList((Object[]) parameter));
        } else if (parameter instanceof Map) {
            final Map<String, Object> paramMap = (Map<String, Object>) parameter;
            if (Objects.nonNull(action)) {
                final Object value = action.apply(paramMap);
                if (Objects.isNull(value)) {
                    return null;
                }
                if (value instanceof Collection) {
                    return this.toList((Collection<Object>) value);
                } else if (Objects.isArray(value)) {
                    return this.toList(Arrays.asList((Object[]) value));
                } else if (!(value instanceof Boolean)) {
                    return this.toList(Collections.singletonList(value));
                } else if (!((Boolean) value)) {
                    return null;
                }
            }
            if (paramMap.containsKey(PARAM_COLLECTION)) {
                final Object value = paramMap.get(PARAM_COLLECTION);
                if (value instanceof Collection) {
                    return this.toList((Collection<Object>) value);
                }
            }
            if (paramMap.containsKey(PARAM_LIST)) {
                final Object value = paramMap.get(PARAM_LIST);
                if (value instanceof Collection) {
                    return this.toList((Collection<Object>) value);
                }
            }
            if (paramMap.containsKey(PARAM_ARRAY)) {
                final Object value = paramMap.get(PARAM_ARRAY);
                if (Objects.isArray(value)) {
                    return this.toList(Arrays.asList((Object[]) value));
                }
            }
            if (paramMap.containsKey(PARAM_ENTITY)) {
                final Object value = paramMap.get(PARAM_ENTITY);
                if (value != null) {
                    return this.toList(Collections.singletonList(value));
                }
            }
            if (paramMap.containsKey(Constants.PARAM_ENTITIES)) {
                final Object value = paramMap.get(Constants.PARAM_ENTITIES);
                if (value instanceof Collection) {
                    return this.toList((Collection<Object>) value);
                } else if (Objects.isArray(value)) {
                    return this.toList(Arrays.asList((Object[]) value));
                }
            }
            if (paramMap.size() == 1) {
                return this.toList(paramMap.values());
            }
        } else if (!Reflections.isSimpleJavaType(parameter.getClass())) {
            return this.toList(Collections.singletonList(parameter));
        }
        return null;
    }

    /**
     * 获取{@code Criteria}对象
     * @param parameter 参数
     * @return {@code Criteria}对象
     */
    protected Object getCriteriaTarget(final Object parameter) {
        final Map<?, ?> paramMap;
        if (parameter instanceof Map && ((paramMap = (Map<?, ?>) parameter)).containsKey(Constants.PARAM_CRITERIA)) {
            return paramMap.get(Constants.PARAM_CRITERIA);
        }
        return null;
    }

    /**
     * {@link Collection}参数转{@link List}参数
     * @param values 参数列表
     * @return {@link List}参数
     */
    protected List<Object> toList(final Collection<Object> values) {
        if (Objects.isNotNullElement(values)) {
            if (values instanceof List) {
                return (List<Object>) values;
            }
            return values.stream().filter(Objects::nonNull).collect(Collectors.toList());
        }
        return null;
    }

    public Properties getProperties() {
        return properties;
    }

    /**
     * 获取配置项值
     * @param key 键
     * @return 值
     */
    protected String getProperty(final String key) {
        return this.properties.getProperty(key);
    }

    @Override
    public void setProperties(Properties properties) {
        this.properties = Objects.nonNull(properties) ? properties : new Properties();
    }

}
