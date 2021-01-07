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
package com.wvkity.mybatis.annotation;

/**
 * 自增主键注解
 * @author wvkity
 * @created 2020-10-04
 * @since 1.0.0
 */
public @interface Identity {

    /**
     * 使用JDBC方式获取(优先级最高)
     * @return boolean
     */
    boolean useJdbc() default false;

    /**
     * 序列名
     * @return 序列名
     */
    String sequence() default "";

    /**
     * 获取主键SQL执行时机
     * @return {@link Executing}
     */
    Executing executing() default Executing.CONFIG;
}
