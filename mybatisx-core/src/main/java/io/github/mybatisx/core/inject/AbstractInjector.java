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
package io.github.mybatisx.core.inject;

import io.github.mybatisx.Objects;
import io.github.mybatisx.basic.inject.Injector;
import io.github.mybatisx.basic.metadata.Table;
import io.github.mybatisx.core.inject.method.MappedMethod;
import io.github.mybatisx.core.inject.method.invoke.Delete;
import io.github.mybatisx.core.inject.method.invoke.DeleteById;
import io.github.mybatisx.core.inject.method.invoke.Exists;
import io.github.mybatisx.core.inject.method.invoke.ExistsById;
import io.github.mybatisx.core.inject.method.invoke.Insert;
import io.github.mybatisx.core.inject.method.invoke.InsertBatch;
import io.github.mybatisx.core.inject.method.invoke.InsertWithoutNull;
import io.github.mybatisx.core.inject.method.invoke.SelectAll;
import io.github.mybatisx.core.inject.method.invoke.SelectCount;
import io.github.mybatisx.core.inject.method.invoke.SelectListByCriteria;
import io.github.mybatisx.core.inject.method.invoke.SelectListByEntity;
import io.github.mybatisx.core.inject.method.invoke.SelectListByIds;
import io.github.mybatisx.core.inject.method.invoke.SelectListWithArray;
import io.github.mybatisx.core.inject.method.invoke.SelectListWithMapObject;
import io.github.mybatisx.core.inject.method.invoke.SelectListWithObject;
import io.github.mybatisx.core.inject.method.invoke.SelectMap;
import io.github.mybatisx.core.inject.method.invoke.SelectMapWithEmbed;
import io.github.mybatisx.core.inject.method.invoke.SelectOne;
import io.github.mybatisx.core.inject.method.invoke.SelectPageableList;
import io.github.mybatisx.core.inject.method.invoke.SelectPageableListByCriteria;
import io.github.mybatisx.core.inject.method.invoke.SelectPageableListByEntity;
import io.github.mybatisx.core.inject.method.invoke.SelectPageableListWithArray;
import io.github.mybatisx.core.inject.method.invoke.SelectPageableListWithMapObject;
import io.github.mybatisx.core.inject.method.invoke.SelectPageableListWithObject;
import io.github.mybatisx.core.inject.method.invoke.SelectPageableMap;
import io.github.mybatisx.core.inject.method.invoke.SelectPageableMapWithEmbed;
import io.github.mybatisx.core.inject.method.invoke.SelectTotal;
import io.github.mybatisx.core.inject.method.invoke.Update;
import io.github.mybatisx.core.inject.method.invoke.UpdateByCriteria;
import io.github.mybatisx.core.inject.method.invoke.UpdateMixed;
import io.github.mybatisx.core.inject.method.invoke.UpdateMixedWithSpecial;
import io.github.mybatisx.core.inject.method.invoke.UpdateWithSpecialExcNull;
import io.github.mybatisx.core.inject.method.invoke.UpdateWithoutNull;
import io.github.mybatisx.core.inject.method.invoke.UpdateWithSpecial;
import io.github.mybatisx.immutable.ImmutableSet;
import io.github.mybatisx.support.config.MyBatisLocalConfigurationCache;
import io.github.mybatisx.support.helper.TableHelper;
import io.github.mybatisx.support.mapper.DeleteMapper;
import io.github.mybatisx.support.mapper.InsertMapper;
import io.github.mybatisx.support.mapper.QueryMapper;
import io.github.mybatisx.support.mapper.SameMapper;
import io.github.mybatisx.support.mapper.SimpleMapper;
import io.github.mybatisx.support.mapper.UpdateMapper;
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
 * ??????SQL?????????
 * @author wvkity
 * @created 2020-10-04
 * @since 1.0.0
 */
public abstract class AbstractInjector implements Injector {

    private static final Logger log = LoggerFactory.getLogger(AbstractInjector.class);

    /**
     * ??????????????????
     */
    protected static final Map<Class<?>, Set<MappedMethod>> MAPPED_METHOD_CACHE = new ConcurrentHashMap<>();

    static {
        // insert
        MAPPED_METHOD_CACHE.put(InsertMapper.class, ImmutableSet.construct(
            new Insert(),
            new InsertBatch(),
            new InsertWithoutNull()
        ));
        // update
        MAPPED_METHOD_CACHE.put(UpdateMapper.class, ImmutableSet.construct(
            new Update(),
            new UpdateByCriteria(),
            new UpdateMixed(),
            new UpdateMixedWithSpecial(),
            new UpdateWithoutNull(),
            new UpdateWithSpecial(),
            new UpdateWithSpecialExcNull()
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
                    // ??????Table??????
                    final Table table = TableHelper.parse(assistant, entityClass);
                    methods.forEach(m -> m.invoke(assistant, mapperInterface, resultClass, table));
                }
            }
        });
    }

    /**
     * ????????????????????????
     * @param mapperInterface Mapper??????
     * @return {@link MappedMethod}??????
     */
    public Collection<MappedMethod> getMappedMethods(final Class<?> mapperInterface) {
        return null;
    }

    /**
     * ?????????????????????????????????
     * @param mapperInterface Mapper??????
     * @return ??????
     */
    private int index(Class<?> mapperInterface) {
        return (SimpleMapper.class.isAssignableFrom(mapperInterface) ||
            SameMapper.class.isAssignableFrom(mapperInterface)) ? 0 : 1;
    }

    /**
     * ?????????????????????
     * @param genericTypes ????????????
     * @param max          ???????????????
     * @param index        ??????
     * @return ???????????????
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
     * ??????????????????
     * @param mapperInterface Mapper??????
     * @return ????????????
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
