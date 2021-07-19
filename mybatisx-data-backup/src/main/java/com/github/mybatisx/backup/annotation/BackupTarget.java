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
package com.github.mybatisx.backup.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 数据备份目标类注解
 * @author wvkity
 * @created 2021-07-18
 * @since 1.0.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface BackupTarget {

    /**
     * 目标类
     * @return 目标类
     */
    Class<?> value();

    /**
     * 备份数据对应元数据ID属性
     * @return ID属性
     */
    String id() default "orgId";

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
