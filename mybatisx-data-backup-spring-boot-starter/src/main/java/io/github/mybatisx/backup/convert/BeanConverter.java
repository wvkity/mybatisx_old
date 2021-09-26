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
package io.github.mybatisx.backup.convert;

/**
 * Bean转换器
 * @author wvkity
 * @created 2021-07-19
 * @since 1.0.0
 */
public interface BeanConverter {

    /**
     * 将元数据转换成目标类对象
     * @param source       元数据
     * @param sourceIdProp 元数据ID属性
     * @param target       目标类
     * @param targetIdProp 目标类ID属性对应元数据ID属性
     * @param <T>          目标类型
     * @return 目标实例
     * @throws Exception 通过反射创建实例可能出现异常
     */
    <T> T convert(final Object source, final String sourceIdProp, final Class<T> target,
                  final String targetIdProp) throws Exception;
}
