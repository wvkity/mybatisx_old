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
package io.github.mybatisx.backup.event;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 数据备份拦截处理
 * @author wvkity
 * @created 2021-07-17
 * @since 1.0.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface BackupFilter {

    /**
     * 实体参数别名(如: {@code @Param("entity")})
     * @return 实体参数别名
     */
    String alias() default "";

    /**
     * 元数据类型
     * @return 元数据类型
     */
    Class<?> source() default Object.class;

    /**
     * 备份数据对应元数据ID属性
     * @return ID属性
     */
    String id() default "";

    /**
     * 目标类型
     * @return 目标类型
     */
    Class<?> target() default Object.class;

    /**
     * 备份数据对应元数据ID属性
     * @return ID属性
     */
    String targetId() default "";

    /**
     * 查询备份数据方法
     * @return 方法名称
     */
    String selectMethod() default "";

    /**
     * 数据备份处理bean名称
     * @return bean名称
     */
    String processBean() default "";

    /**
     * 数据备份处理的方法名
     * @return 方法名
     */
    String processMethod() default "";

    /**
     * 方法参数类型
     * @return 参数类型列表
     */
    Class<?>[] args() default {};
}
