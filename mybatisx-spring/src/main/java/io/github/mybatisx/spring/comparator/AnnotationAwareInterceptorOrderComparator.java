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
package io.github.mybatisx.spring.comparator;

import org.springframework.core.DecoratingProxy;
import org.springframework.core.annotation.MergedAnnotation;
import org.springframework.core.annotation.MergedAnnotations;
import org.springframework.core.annotation.Order;
import org.springframework.core.annotation.OrderUtils;
import org.springframework.lang.Nullable;
import org.springframework.util.ConcurrentReferenceHashMap;

import java.lang.reflect.AnnotatedElement;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 拦截器排序器
 * <p> copy {@link org.springframework.core.annotation.AnnotationAwareOrderComparator AnnotationAwareOrderComparator}
 * @author wvkity
 * @created 2021-02-10
 * @since 1.0.0
 */
public class AnnotationAwareInterceptorOrderComparator extends InterceptorOrderComparator {

    /**
     * Cache marker for a non-annotated Class.
     */
    private static final Object NOT_ANNOTATED = new Object();

    private static final String JAVAX_PRIORITY_ANNOTATION = "javax.annotation.Priority";

    /**
     * Cache for @Order value (or NOT_ANNOTATED marker) per Class.
     */
    private static final Map<AnnotatedElement, Object> orderCache = new ConcurrentReferenceHashMap<>(64);
    public static final AnnotationAwareInterceptorOrderComparator INSTANCE = new AnnotationAwareInterceptorOrderComparator();

    @Override
    protected Integer findOrder(Object obj) {
        Integer order = super.findOrder(obj);
        if (order != null) {
            return order;
        }
        return findOrderFromAnnotation(obj);
    }

    @Nullable
    private Integer findOrderFromAnnotation(Object obj) {
        AnnotatedElement element = (obj instanceof AnnotatedElement ? (AnnotatedElement) obj : obj.getClass());
        MergedAnnotations annotations = MergedAnnotations.from(element, MergedAnnotations.SearchStrategy.TYPE_HIERARCHY);
        Integer order = getOrderFromAnnotations(element, annotations);
        if (order == null && obj instanceof DecoratingProxy) {
            return findOrderFromAnnotation(((DecoratingProxy) obj).getDecoratedClass());
        }
        return order;
    }

    /**
     * This implementation retrieves an @{@link javax.annotation.Priority}
     * value, allowing for additional semantics over the regular @{@link Order}
     * annotation: typically, selecting one object over another in case of
     * multiple matches but only one object to be returned.
     */
    @Override
    @Nullable
    public Integer getPriority(Object obj) {
        if (obj instanceof Class) {
            return OrderUtils.getPriority((Class<?>) obj);
        }
        Integer priority = OrderUtils.getPriority(obj.getClass());
        if (priority == null && obj instanceof DecoratingProxy) {
            return getPriority(((DecoratingProxy) obj).getDecoratedClass());
        }
        return priority;
    }

    /**
     * Return the order from the specified annotations collection.
     * <p>Takes care of {@link Order @Order} and
     * {@code @javax.annotation.Priority}.
     * @param element     the source element
     * @param annotations the annotation to consider
     * @return the order value, or {@code null} if none can be found
     */
    @Nullable
    static Integer getOrderFromAnnotations(AnnotatedElement element, MergedAnnotations annotations) {
        if (!(element instanceof Class)) {
            return findOrder(annotations);
        }
        Object cached = orderCache.get(element);
        if (cached != null) {
            return (cached instanceof Integer ? (Integer) cached : null);
        }
        Integer result = findOrder(annotations);
        orderCache.put(element, result != null ? result : NOT_ANNOTATED);
        return result;
    }

    @Nullable
    private static Integer findOrder(MergedAnnotations annotations) {
        MergedAnnotation<Order> orderAnnotation = annotations.get(Order.class);
        if (orderAnnotation.isPresent()) {
            return orderAnnotation.getInt(MergedAnnotation.VALUE);
        }
        MergedAnnotation<io.github.mybatisx.plugin.annotation.Order> pluginOrderAnnotation =
            annotations.get(io.github.mybatisx.plugin.annotation.Order.class);
        if (pluginOrderAnnotation.isPresent()) {
            return pluginOrderAnnotation.getInt(MergedAnnotation.VALUE);
        }
        MergedAnnotation<?> priorityAnnotation = annotations.get(JAVAX_PRIORITY_ANNOTATION);
        if (priorityAnnotation.isPresent()) {
            return priorityAnnotation.getInt(MergedAnnotation.VALUE);
        }
        return null;
    }

    /**
     * Sort the given list with a default {@link AnnotationAwareInterceptorOrderComparator}.
     * <p>Optimized to skip sorting for lists with size 0 or 1,
     * in order to avoid unnecessary array extraction.
     * @param list the List to sort
     * @see java.util.List#sort(java.util.Comparator)
     */
    public static void sort(List<?> list) {
        if (list.size() > 1) {
            list.sort(INSTANCE);
        }
    }

    /**
     * Sort the given array with a default AnnotationAwareOrderComparator.
     * <p>Optimized to skip sorting for lists with size 0 or 1,
     * in order to avoid unnecessary array extraction.
     * @param array the array to sort
     * @see java.util.Arrays#sort(Object[], java.util.Comparator)
     */
    public static void sort(Object[] array) {
        if (array.length > 1) {
            Arrays.sort(array, INSTANCE);
        }
    }

    /**
     * Sort the given array or List with a default AnnotationAwareOrderComparator,
     * if necessary. Simply skips sorting when given any other value.
     * <p>Optimized to skip sorting for lists with size 0 or 1,
     * in order to avoid unnecessary array extraction.
     * @param value the array or List to sort
     * @see java.util.Arrays#sort(Object[], java.util.Comparator)
     */
    public static void sortIfNecessary(Object value) {
        if (value instanceof Object[]) {
            sort((Object[]) value);
        } else if (value instanceof List) {
            sort((List<?>) value);
        }
    }

}
