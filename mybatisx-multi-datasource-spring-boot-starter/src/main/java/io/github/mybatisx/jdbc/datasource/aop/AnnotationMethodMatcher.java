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
package io.github.mybatisx.jdbc.datasource.aop;

import org.springframework.aop.support.AopUtils;
import org.springframework.aop.support.StaticMethodMatcher;
import org.springframework.core.annotation.AnnotatedElementUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 注解方法匹配器
 * @author wvkity
 * @created 2021-08-05
 * @since 1.0.0
 */
public class AnnotationMethodMatcher extends StaticMethodMatcher {

    private final Class<? extends Annotation> pointcutAnnotationClass;

    public AnnotationMethodMatcher(Class<? extends Annotation> pointcutAnnotationClass) {
        this.pointcutAnnotationClass = pointcutAnnotationClass;
    }

    @Override
    public boolean matches(Method method, Class<?> targetClass) {
        if (this.matches(method)) {
            return true;
        }
        if (Proxy.isProxyClass(targetClass)) {
            return false;
        }
        final Method specificMethod = AopUtils.getMostSpecificMethod(method, targetClass);
        return specificMethod != method && this.matches(specificMethod);
    }

    protected boolean matches(final Method method) {
        return AnnotatedElementUtils.hasAnnotation(method, this.pointcutAnnotationClass);
    }
}
