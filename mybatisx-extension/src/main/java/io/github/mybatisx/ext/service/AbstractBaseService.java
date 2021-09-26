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
package io.github.mybatisx.ext.service;

import io.github.mybatisx.Objects;
import io.github.mybatisx.batch.BatchDataWrapper;
import io.github.mybatisx.executor.resultset.EmbedResult;
import io.github.mybatisx.reflect.Reflections;
import io.github.mybatisx.support.criteria.Criteria;
import io.github.mybatisx.support.mapper.BaseMapper;
import io.github.paging.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 基础Service抽象类
 * @param <M>  Mapper接口
 * @param <T>  实体类型
 * @param <U>  返回值类型
 * @param <ID> 主键类型
 * @author wvkity
 * @created 2020-10-03
 * @since 1.0.0
 */
public abstract class AbstractBaseService<M extends BaseMapper<T, U, ID>, T, U, ID> implements
    BaseService<M, T, U, ID> {

    /**
     * 标识存在
     */
    private static final int EXISTS = 1;

    /**
     * 通用基础Mapper接口
     */
    protected M mapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int save(T entity) {
        return this.mapper.insert(entity);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int saveWithoutNull(T entity) {
        return this.mapper.insertWithoutNull(entity);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int saveBatch(List<T> entities) {
        return this.saveBatch(BatchDataWrapper.wrap(entities));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int saveBatch(int batchSize, List<T> entities) {
        return this.saveBatch(BatchDataWrapper.wrap(batchSize, entities));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int saveBatch(BatchDataWrapper<T> wrapper) {
        return this.mapper.insertBatch(wrapper);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int delete(T entity) {
        return this.mapper.delete(entity);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int deleteById(ID id) {
        return this.mapper.deleteById(id);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int deleteWithLogic(T entity) {
        return this.mapper.logicDelete(entity);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int deleteWithLogic(Criteria<T> criteria) {
        return this.mapper.logicDeleteByCriteria(criteria);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int update(T entity) {
        return this.mapper.update(entity);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int updateWithoutNull(T entity) {
        return this.mapper.updateWithoutNull(entity);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int update(Criteria<T> criteria) {
        return this.mapper.updateByCriteria(criteria);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int updateMixed(T entity, Criteria<T> criteria) {
        return this.mapper.updateMixed(entity, criteria);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int updateWithSpecial(T entity) {
        return this.mapper.updateWithSpecial(entity);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int updateWithSpecialExcNull(T entity) {
        return this.mapper.updateWithSpecialExcNull(entity);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int updateMixedWithSpecial(T entity, Criteria<T> criteria) {
        return this.mapper.updateMixedWithSpecial(entity, criteria);
    }

    @Override
    public boolean existsById(ID id) {
        return this.mapper.existsById(id) == EXISTS;
    }

    @Override
    public boolean exists(T entity) {
        return this.mapper.exists(entity) == EXISTS;
    }

    @Override
    public long selectCount(T entity) {
        return this.mapper.selectCount(entity);
    }

    @Override
    public long selectTotal() {
        return this.mapper.selectTotal();
    }

    @Override
    public Optional<U> selectOne(T entity) {
        return this.mapper.selectOne(entity);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<U> selectList(ID... ids) {
        return Objects.isEmpty(ids) ? new ArrayList<>(0) : this.selectList(new ArrayList<>(Arrays.asList(ids)));
    }

    @Override
    public List<U> selectList(Collection<ID> ids) {
        return Objects.isEmpty(ids) ? new ArrayList<>(0) : this.mapper.selectListByIds(ids);
    }

    @Override
    public List<U> selectList(T entity) {
        return this.mapper.selectListByEntity(entity);
    }

    @Override
    public List<U> selectList(Criteria<T> criteria) {
        return this.mapper.selectListByCriteria(criteria);
    }

    @Override
    public <E> List<E> selectListWithEmbed(Criteria<T> criteria, Class<E> resultType) {
        return this.invokeEmbeddedResult(criteria, resultType).selectListWithEmbed(criteria);
    }

    @Override
    public <E> List<E> selectListWithEmbed(Criteria<T> criteria) {
        return this.handleResult(this.selectListWithObject(criteria));
    }

    @Override
    public List<Object> selectListWithObject(Criteria<T> criteria) {
        if (criteria == null) {
            return new ArrayList<>(0);
        }
        return this.mapper.selectListWithObject(criteria);
    }

    @Override
    public List<Object[]> selectListWithArray(Criteria<T> criteria) {
        return this.mapper.selectListWithArray(criteria);
    }

    @Override
    public Map<Object, U> selectMap(Criteria<T> criteria) {
        return this.requireMapKey(criteria).mapper.selectMap(criteria);
    }

    @Override
    public <E> Map<Object, E> selectMapWithEmbed(Criteria<T> criteria, Class<E> resultType) {
        return this.invokeEmbeddedResult(criteria, resultType).selectMapWithEmbed(criteria);
    }

    @Override
    public <E> Map<Object, E> selectMapWithEmbed(Criteria<T> criteria) {
        return this.requireMapKey(criteria).mapper.selectMapWithEmbed(criteria);
    }

    @Override
    public List<Map<String, Object>> selectListWithMapObject(Criteria<T> criteria) {
        return this.mapper.selectListWithMapObject(criteria);
    }

    @Override
    public List<U> selectList(Pageable pageable) {
        return this.mapper.selectPageableList(pageable);
    }

    @Override
    public List<U> selectList(T entity, Pageable pageable) {
        return this.mapper.selectPageableListByEntity(entity, pageable);
    }

    @Override
    public List<U> selectList(Criteria<T> criteria, Pageable pageable) {
        return this.mapper.selectPageableListByCriteria(criteria, pageable);
    }

    @Override
    public List<Object> selectListWithObject(Criteria<T> criteria, Pageable pageable) {
        return this.mapper.selectPageableListWithObject(criteria, pageable);
    }

    @Override
    public List<Object[]> selectListWithArray(Criteria<T> criteria, Pageable pageable) {
        return this.mapper.selectPageableListWithArray(criteria, pageable);
    }

    @Override
    public <E> List<E> selectListWithEmbed(Criteria<T> criteria, Class<E> resultType, Pageable pageable) {
        return this.invokeEmbeddedResult(criteria, resultType).selectListWithEmbed(criteria, pageable);
    }

    @Override
    public <E> List<E> selectListWithEmbed(Criteria<T> criteria, Pageable pageable) {
        return this.handleResult(this.mapper.selectPageableListWithObject(criteria, pageable));
    }

    @Override
    public Map<Object, U> selectMap(Criteria<T> criteria, Pageable pageable) {
        return this.requireMapKey(criteria).mapper.selectPageableMap(criteria, pageable);
    }

    @Override
    public <E> Map<Object, E> selectMapWithEmbed(Criteria<T> criteria, Class<E> resultType, Pageable pageable) {
        return this.invokeEmbeddedResult(criteria, resultType).selectMapWithEmbed(criteria, pageable);
    }

    @Override
    public <E> Map<Object, E> selectMapWithEmbed(Criteria<T> criteria, Pageable pageable) {
        return this.requireMapKey(criteria).mapper.selectPageableMapWithEmbed(criteria, pageable);
    }

    @Override
    public List<Map<String, Object>> selectListWithMapObject(Criteria<T> criteria, Pageable pageable) {
        return this.mapper.selectPageableListWithMapObject(criteria, pageable);
    }

    @Override
    public List<U> selectAll() {
        return this.mapper.selectAll();
    }

    @Override
    public M getMapper() {
        return this.mapper;
    }

    @Autowired(required = false)
    @Override
    public void setMapper(M mapper) {
        this.mapper = mapper;
    }

    /**
     * 处理指定返回值
     * @param criteria   {@link Criteria}
     * @param resultType 返回值类型
     * @return {@link AbstractBaseService}
     */
    protected AbstractBaseService<M, T, U, ID> invokeEmbeddedResult(final Criteria<T> criteria,
                                                                    final Class<?> resultType) {
        if (criteria instanceof EmbedResult) {
            ((EmbedResult) criteria).resultType(resultType);
        } else {
            try {
                Reflections.invokeConsistentVirtual(criteria, "resultType", resultType);
            } catch (Exception ignore) {
                // ignore
            }
        }
        return this;
    }

    /**
     * 处理结果
     * @param result 查询结果
     * @param <E>    返回值类型
     * @return 多条记录
     */
    @SuppressWarnings("unchecked")
    protected <E> List<E> handleResult(final List<Object> result) {
        if (Objects.isNotEmpty(result)) {
            return (List<E>) result;
        }
        return new ArrayList<>(0);
    }

    /**
     * 校验mapKey是否为空
     * @param criteria {@link Criteria}
     * @return {@link AbstractBaseService}
     */
    protected AbstractBaseService<M, T, U, ID> requireMapKey(final Criteria<T> criteria) {
        if (criteria instanceof EmbedResult) {
            final String mapKey = ((EmbedResult) criteria).getMapKey();
            if (Objects.isBlank(mapKey)) {
                throw new IllegalArgumentException("The mapKey attribute value cannot be null.");
            }
        }
        return this;
    }
}
