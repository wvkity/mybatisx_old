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
package com.wvkity.mybatis.basic.invoker;


import com.wvkity.mybatis.basic.reflect.Reflections;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

/**
 * 类属性set方法调用器
 * @author wvkity
 * @created 2020-10-08
 * @since 1.0.0
 */
public class FieldSetterInvoker implements Invoker {

    /**
     * 属性
     */
    private final Field field;

    public FieldSetterInvoker(Field field) {
        this.field = field;
    }


    @Override
    public Class<?> getType() {
        return this.field.getType();
    }

    @Override
    public Object invoke(Object target, Object... args) throws IllegalAccessException, InvocationTargetException {
        try {
            this.field.set(target, args[0]);
        } catch (IllegalAccessException e) {
            if (Reflections.canControlMemberAccessible()) {
                this.field.setAccessible(true);
                this.field.set(target, args[0]);
            } else {
                throw e;
            }
        }
        return null;
    }

    public static FieldSetterInvoker of(final Field field) {
        return new FieldSetterInvoker(field);
    }
}
