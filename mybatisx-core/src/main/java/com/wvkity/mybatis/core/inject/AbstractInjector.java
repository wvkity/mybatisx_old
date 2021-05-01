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

import com.wvkity.mybatis.basic.immutable.ImmutableSet;
import com.wvkity.mybatis.basic.inject.Injector;
import com.wvkity.mybatis.basic.metadata.Table;
import com.wvkity.mybatis.basic.utils.Objects;
import com.wvkity.mybatis.core.inject.method.MappedMethod;
import com.wvkity.mybatis.core.inject.method.invoke.DeleteById;
import com.wvkity.mybatis.core.inject.method.invoke.Delete;
import com.wvkity.mybatis.core.inject.method.invoke.Exists;
import com.wvkity.mybatis.core.inject.method.invoke.ExistsById;
import com.wvkity.mybatis.core.inject.method.invoke.InsertBatch;
import com.wvkity.mybatis.core.inject.method.invoke.Insert;
import com.wvkity.mybatis.core.inject.method.invoke.InsertWithNonNull;
import com.wvkity.mybatis.core.inject.method.invoke.SelectAll;
import com.wvkity.mybatis.core.inject.method.invoke.SelectCount;
import com.wvkity.mybatis.core.inject.method.invoke.SelectListByCriteria;
import com.wvkity.mybatis.core.inject.method.invoke.SelectListByEntity;
import com.wvkity.mybatis.core.inject.method.invoke.SelectListByIds;
import com.wvkity.mybatis.core.inject.method.invoke.SelectListWithArray;
import com.wvkity.mybatis.core.inject.method.invoke.SelectListWithMapObject;
import com.wvkity.mybatis.core.inject.method.invoke.SelectListWithObject;
import com.wvkity.mybatis.core.inject.method.invoke.SelectMap;
import com.wvkity.mybatis.core.inject.method.invoke.SelectMapWithEmbed;
import com.wvkity.mybatis.core.inject.method.invoke.SelectOne;
import com.wvkity.mybatis.core.inject.method.invoke.SelectPageableListByCriteria;
import com.wvkity.mybatis.core.inject.method.invoke.SelectPageableListByEntity;
import com.wvkity.mybatis.core.inject.method.invoke.SelectPageableList;
import com.wvkity.mybatis.core.inject.method.invoke.SelectPageableListWithArray;
import com.wvkity.mybatis.core.inject.method.invoke.SelectPageableListWithMapObject;
import com.wvkity.mybatis.core.inject.method.invoke.SelectPageableListWithObject;
import com.wvkity.mybatis.core.inject.method.invoke.SelectPageableMap;
import com.wvkity.mybatis.core.inject.method.invoke.SelectPageableMapWithEmbed;
import com.wvkity.mybatis.core.inject.method.invoke.SelectTotal;
import com.wvkity.mybatis.core.inject.method.invoke.UpdateByCriteria;
import com.wvkity.mybatis.core.inject.method.invoke.Update;
import com.wvkity.mybatis.core.inject.method.invoke.UpdateWithNonNull;
import com.wvkity.mybatis.support.config.MyBatisLocalConfigurationCache;
import com.wvkity.mybatis.support.helper.TableHelper;
import com.wvkity.mybatis.support.mapper.DeleteMapper;
import com.wvkity.mybatis.support.mapper.InsertMapper;
import com.wvkity.mybatis.support.mapper.QueryMapper;
import com.wvkity.mybatis.support.mapper.SameMapper;
import com.wvkity.mybatis.support.mapper.SimpleMapper;
import com.wvkity.mybatis.support.mapper.UpdateMapper;
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
 * 抽象SQL注入器
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
            new Insert(),
            new InsertWithNonNull(),
            new InsertBatch()
        ));
        // update
        MAPPED_METHOD_CACHE.put(UpdateMapper.class, ImmutableSet.construct(
            new Update(),
            new UpdateWithNonNull(),
            new UpdateByCriteria()
        ));
        // delete
        MAPPED_METHOD_CACHE.put(DeleteMapper.class, ImmutableSet.construct(
            new Delete(),
            new DeleteById()
        ));
        // select
        MAPPED_METHOD_CACHE.put(QueryMapper.class, ImmutableSet.construct(
            new Exists(),
            new ExistsById(),
            new SelectAll(),
            new SelectCount(),
            new SelectListByCriteria(),
            new SelectListByEntity(),
            new SelectListByIds(),
            new SelectListWithArray(),
            new SelectListWithObject(),
            new SelectListWithMapObject(),
            new SelectMap(),
            new SelectMapWithEmbed(),
            new SelectPageableListByCriteria(),
            new SelectPageableListByEntity(),
            new SelectPageableList(),
            new SelectPageableListWithArray(),
            new SelectPageableListWithMapObject(),
            new SelectPageableListWithObject(),
            new SelectPageableMap(),
            new SelectPageableMapWithEmbed(),
            new SelectOne(),
            new SelectTotal()
        ));
    }

    @Override
    public void inject(MapperBuilderAssistant assistant, Class<?> mapperInterface) {
        final Type[] types = getGenericTypes(mapperInterface);
        final int maxLen = 2;
        final Class<?> entityClass = getGenericType(types, maxLen, 0);
        Optional.ofNullable(entityClass).ifPresent(it -> {
            if (MyBatisLocalConfigurationCache.mapperInterfaceRegistryIfNotExists(assistant.getConfiguration(),
                mapperInterface)) {
                final Collection<MappedMethod> methods = getMappedMethods(mapperInterface);
                if (Objects.isNotEmpty(methods)) {
                    final int index = index(mapperInterface);
                    final Class<?> resultClass = index == 0 ? entityClass : getGenericType(types, maxLen, index);
                    // 解析Table对象
                    final Table table = TableHelper.parse(assistant, entityClass);
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
            SameMapper.class.isAssignableFrom(mapperInterface)) ? 0 : 1;
    }

    /**
     * 获取泛型实体类
     * @param genericTypes 泛型数组
     * @param index        索引
     * @return 泛型实体类
     */
    public Class<?> getGenericType(Type[] genericTypes, int max, int index) {
        if (genericTypes == null || genericTypes.length == 0 || index < 0) {
            return null;
        }
        final int len = genericTypes.length;
        if (len >= max && index > len) {
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
