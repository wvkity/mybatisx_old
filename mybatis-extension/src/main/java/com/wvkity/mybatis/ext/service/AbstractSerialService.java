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

import com.wvkity.mybatis.core.mapper.SerialMapper;

import java.io.Serializable;

/**
 * Serializable主键抽象Service类
 * @param <M> Mapper接口
 * @param <T> 实体类型
 * @param <U> 返回值类型
 * @author wvkity
 * @created 2020-10-03
 * @since 1.0.0
 */
public abstract class AbstractSerialService<M extends SerialMapper<T, U>, T, U> extends
    AbstractBaseService<M, T, U, Serializable> implements SerialService<M, T, U> {

}
