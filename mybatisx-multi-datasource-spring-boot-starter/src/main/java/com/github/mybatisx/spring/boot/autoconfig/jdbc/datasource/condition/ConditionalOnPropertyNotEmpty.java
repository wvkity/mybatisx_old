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
package com.github.mybatisx.spring.boot.autoconfig.jdbc.datasource.condition;

import org.springframework.context.annotation.Conditional;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 属性值不为空
 * @author wvkity
 * @created 2021-08-05
 * @since 1.0.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Conditional(OnPropertyNotEmptyCondition.class)
public @interface ConditionalOnPropertyNotEmpty {

    /**
     * 配置项前缀
     * @return 前缀
     */
    String prefix() default "";

    /**
     * 属性名列表
     * @return 属性名列表
     */
    String[] value() default {};

    /**
     * 属性名列表
     * @return 属性名列表
     */
    String[] name() default {};

    /**
     * 缺失属性值是否匹配
     * @return boolean
     */
    boolean matchIfMissing() default false;
}
