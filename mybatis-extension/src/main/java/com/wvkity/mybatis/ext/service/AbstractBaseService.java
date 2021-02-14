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
package com.wvkity.mybatis.ext.service;

import com.wvkity.mybatis.core.condition.criteria.Criteria;
import com.wvkity.mybatis.core.mapper.BaseMapper;
import com.wvkity.mybatis.core.reflect.Reflections;
import com.wvkity.mybatis.core.utils.Objects;
import com.wvkity.mybatis.executor.resultset.EmbeddedResult;
import com.wvkity.paging.Pageable;
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
    public int saveWithNonNull(T entity) {
        return this.mapper.insertWithNonNull(entity);
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
    public int update(T entity) {
        return this.mapper.update(entity);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int updateWithNonNull(T entity) {
        return this.mapper.updateWithNonNull(entity);
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
    public <E> List<E> selectEmbedList(Criteria<T> criteria, Class<E> resultType) {
        if (criteria instanceof EmbeddedResult) {
            try {
                Reflections.invokeConsistentVirtual(criteria, "resultType", resultType);
            } catch (Exception ignore) {
                // ignore
            }
        }
        return this.selectEmbedList(criteria);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <E> List<E> selectEmbedList(Criteria<T> criteria) {
        final List<Object> result = this.selectObjectList(criteria);
        if (Objects.isNotEmpty(result)) {
            return (List<E>) result;
        }
        return new ArrayList<>(0);
    }

    @Override
    public List<Object> selectObjectList(Criteria<T> criteria) {
        if (criteria == null) {
            return new ArrayList<>(0);
        }
        return this.mapper.selectObjectList(criteria);
    }

    @Override
    public List<Object[]> selectArrayList(Criteria<T> criteria) {
        return this.mapper.selectArrayList(criteria);
    }

    @Override
    public Map<Object, U> selectMap(Criteria<T> criteria) {
        return this.mapper.selectMap(criteria);
    }

    @Override
    public <E> Map<Object, E> selectEmbedMap(Criteria<T> criteria, Class<E> resultType) {
        if (criteria instanceof EmbeddedResult) {
            try {
                Reflections.invokeConsistentVirtual(criteria, "resultType", resultType);
            } catch (Exception ignore) {
                // ignore
            }
        }
        return this.selectEmbedMap(criteria);
    }

    @Override
    public <E> Map<Object, E> selectEmbedMap(Criteria<T> criteria) {
        return this.mapper.selectEmbedMap(criteria);
    }

    @Override
    public List<Map<String, Object>> selectMapObjectList(Criteria<T> criteria) {
        return this.mapper.selectMapObjectList(criteria);
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
}
