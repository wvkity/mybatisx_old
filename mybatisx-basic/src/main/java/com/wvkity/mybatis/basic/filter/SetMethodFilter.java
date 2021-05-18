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
package com.wvkity.mybatis.basic.filter;


import com.wvkity.mybatis.basic.reflect.Reflections;

import java.lang.reflect.Method;

/**
 * set方法过滤器
 * @author wvkity
 * @created 2020-10-08
 * @since 1.0.0
 */
public class SetMethodFilter extends MethodFilter {

    @Override
    public boolean matches(Method it) {
        return super.matches(it) && it.getParameterTypes().length == 1 && Reflections.isSetter(it.getName())
            && Reflections.isValidPropertyName(Reflections.methodToProperty(it.getName()));
    }
}
