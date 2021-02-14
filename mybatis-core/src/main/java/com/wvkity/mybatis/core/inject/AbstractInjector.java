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
package com.wvkity.mybatis.core.inject;

import com.wvkity.mybatis.core.config.MyBatisLocalConfigurationCache;
import com.wvkity.mybatis.core.handler.TableHandler;
import com.wvkity.mybatis.core.immutable.ImmutableSet;
import com.wvkity.mybatis.core.inject.method.MappedMethod;
import com.wvkity.mybatis.core.inject.method.invoke.DeleteByIdInvoker;
import com.wvkity.mybatis.core.inject.method.invoke.DeleteInvoker;
import com.wvkity.mybatis.core.inject.method.invoke.ExistsByIdInvoker;
import com.wvkity.mybatis.core.inject.method.invoke.ExistsInvoker;
import com.wvkity.mybatis.core.inject.method.invoke.InsertInvoker;
import com.wvkity.mybatis.core.inject.method.invoke.InsertWithNonNullInvoker;
import com.wvkity.mybatis.core.inject.method.invoke.SelectAllInvoker;
import com.wvkity.mybatis.core.inject.method.invoke.SelectArrayListInvoker;
import com.wvkity.mybatis.core.inject.method.invoke.SelectCountInvoker;
import com.wvkity.mybatis.core.inject.method.invoke.SelectEmbedMapInvoker;
import com.wvkity.mybatis.core.inject.method.invoke.SelectListByCriteriaInvoker;
import com.wvkity.mybatis.core.inject.method.invoke.SelectListByEntityInvoker;
import com.wvkity.mybatis.core.inject.method.invoke.SelectListByIdsInvoker;
import com.wvkity.mybatis.core.inject.method.invoke.SelectMapInvoker;
import com.wvkity.mybatis.core.inject.method.invoke.SelectMapObjectListInvoker;
import com.wvkity.mybatis.core.inject.method.invoke.SelectObjectListInvoker;
import com.wvkity.mybatis.core.inject.method.invoke.SelectOneInvoker;
import com.wvkity.mybatis.core.inject.method.invoke.SelectPageableListByCriteriaInvoker;
import com.wvkity.mybatis.core.inject.method.invoke.SelectPageableListInvoker;
import com.wvkity.mybatis.core.inject.method.invoke.SelectTotalInvoker;
import com.wvkity.mybatis.core.inject.method.invoke.UpdateInvoker;
import com.wvkity.mybatis.core.inject.method.invoke.UpdateWithNonNullInvoker;
import com.wvkity.mybatis.core.mapper.DeleteMapper;
import com.wvkity.mybatis.core.mapper.InsertMapper;
import com.wvkity.mybatis.core.mapper.QueryMapper;
import com.wvkity.mybatis.core.mapper.SameMapper;
import com.wvkity.mybatis.core.mapper.SimpleMapper;
import com.wvkity.mybatis.core.mapper.UpdateMapper;
import com.wvkity.mybatis.core.metadata.Table;
import com.wvkity.mybatis.core.utils.Objects;
import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wvkity
 * @created 2020-10-04
 * @since 1.0.0
 */
public abstract class AbstractInjector implements Injector {

    private static final Logger log = LoggerFactory.getLogger(AbstractInjector.class);

    /**
     * 映射方法缓存
     */
    protected static final Map<Class<?>, Set<MappedMethod>> MAPPED_METHOD_CACHE = new ConcurrentHashMap<>();

    static {
        // insert
        MAPPED_METHOD_CACHE.put(InsertMapper.class, ImmutableSet.construct(
            new InsertInvoker(),
            new InsertWithNonNullInvoker()));
        // update
        MAPPED_METHOD_CACHE.put(UpdateMapper.class, ImmutableSet.construct(
            new UpdateInvoker(),
            new UpdateWithNonNullInvoker()));
        // delete
        MAPPED_METHOD_CACHE.put(DeleteMapper.class, ImmutableSet.construct(
            new DeleteInvoker(),
            new DeleteByIdInvoker()
        ));
        // select
        MAPPED_METHOD_CACHE.put(QueryMapper.class, ImmutableSet.construct(
            new ExistsByIdInvoker(),
            new ExistsInvoker(),
            new SelectCountInvoker(),
            new SelectTotalInvoker(),
            new SelectOneInvoker(),
            new SelectListByEntityInvoker(),
            new SelectListByIdsInvoker(),
            new SelectListByCriteriaInvoker(),
            new SelectObjectListInvoker(),
            new SelectArrayListInvoker(),
            new SelectMapInvoker(),
            new SelectEmbedMapInvoker(),
            new SelectMapObjectListInvoker(),
            new SelectPageableListInvoker(),
            new SelectPageableListByCriteriaInvoker(),
            new SelectAllInvoker()));
    }

    @Override
    public void inject(MapperBuilderAssistant assistant, Class<?> mapperInterface) {
        final Type[] types = getGenericTypes(mapperInterface);
        final Class<?> entityClass = getGenericType(types, 0);
        Optional.ofNullable(entityClass).ifPresent(it -> {
            if (MyBatisLocalConfigurationCache.mapperInterfaceRegistryIfNotExists(assistant.getConfiguration(),
                mapperInterface)) {
                final Collection<MappedMethod> methods = getMappedMethods(mapperInterface);
                if (Objects.isNotEmpty(methods)) {
                    final int index = index(mapperInterface);
                    final Class<?> resultClass = index == 0 ? entityClass : getGenericType(types, index);
                    // 解析Table对象
                    final Table table = TableHandler.parse(assistant, entityClass);
                    methods.forEach(m -> m.invoke(assistant, mapperInterface, resultClass, table));
                }
            }
        });
    }

    /**
     * 获取映射方法列表
     * @param mapperInterface Mapper接口
     * @return {@link MappedMethod}集合
     */
    public Collection<MappedMethod> getMappedMethods(final Class<?> mapperInterface) {
        return null;
    }

    /**
     * 获取实体返回值类型索引
     * @param mapperInterface Mapper接口
     * @return 索引
     */
    private int index(Class<?> mapperInterface) {
        return (SimpleMapper.class.isAssignableFrom(mapperInterface) ||
            SameMapper.class.isAssignableFrom(mapperInterface) ||
            InsertMapper.class.isAssignableFrom(mapperInterface) ||
            UpdateMapper.class.isAssignableFrom(mapperInterface) ||
            DeleteMapper.class.isAssignableFrom(mapperInterface)) ? 0 : 1;
    }

    /**
     * 获取泛型实体类
     * @param genericTypes 泛型数组
     * @param index        索引
     * @return 泛型实体类
     */
    public Class<?> getGenericType(Type[] genericTypes, int index) {
        if (genericTypes == null || genericTypes.length == 0 || index < 0) {
            return null;
        }
        final int len = genericTypes.length;
        if (len >= 2 && index > len) {
            return null;
        }
        Type target = len == 1 ? genericTypes[0] : genericTypes[index];
        return target == null ? null : (Class<?>) target;
    }


    /**
     * 获取接口泛型
     * @param mapperInterface Mapper接口
     * @return 泛型数组
     */
    protected Type[] getGenericTypes(final Class<?> mapperInterface) {
        Type[] interfaces = mapperInterface.getGenericInterfaces();
        for (Type type : interfaces) {
            if (type instanceof ParameterizedType) {
                Type[] genericTypes = ((ParameterizedType) type).getActualTypeArguments();
                for (Type it : genericTypes) {
                    if (!(it instanceof TypeVariable) && !(it instanceof WildcardType)) {
                        return genericTypes;
                    }
                    break;
                }
                break;
            }
        }
        return null;
    }
}
