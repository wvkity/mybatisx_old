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
package com.wvkity.mybatis.spring.jdbc.datasource.aop;

import org.springframework.aop.ClassFilter;
import org.springframework.aop.MethodMatcher;
import org.springframework.aop.Pointcut;

import java.lang.annotation.Annotation;

/**
 * 方法注解切入点
 * @author wvkity
 * @created 2020-11-23
 * @since 1.0.0
 */
public class AnnotationMethodMatchingPointcut implements Pointcut {

    private final Class<? extends Annotation> pointcutAnnotationClass;

    public AnnotationMethodMatchingPointcut(Class<? extends Annotation> pointcutAnnotationClass) {
        this.pointcutAnnotationClass = pointcutAnnotationClass;
    }

    @Override
    public ClassFilter getClassFilter() {
        return ClassFilter.TRUE;
    }

    @Override
    public MethodMatcher getMethodMatcher() {
        return new AnnotationMethodMatcher(this.pointcutAnnotationClass);
    }
}
