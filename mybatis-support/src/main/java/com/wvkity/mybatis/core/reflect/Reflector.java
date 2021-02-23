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
package com.wvkity.mybatis.core.reflect;

import com.wvkity.mybatis.core.exception.ReflectionException;
import com.wvkity.mybatis.core.immutable.ImmutableLinkedSet;
import com.wvkity.mybatis.core.immutable.ImmutableMap;
import com.wvkity.mybatis.core.immutable.ImmutableSet;
import com.wvkity.mybatis.core.utils.Objects;
import org.apache.ibatis.reflection.TypeParameterResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 类反射器
 * @author wvkity
 * @created 2020-10-05
 * @since 1.0.0
 */
public class Reflector implements AnnotationMetadata {

    private static final Logger log = LoggerFactory.getLogger(Reflector.class);

    /**
     * 反射目标类
     */
    private final Class<?> clazz;
    /**
     * 类所有注解
     */
    private Set<? extends Annotation> annotations;
    /**
     * 注解缓存
     */
    private Map<String, ? extends Annotation> annotationCaches;
    /**
     * 原属性
     */
    private Set<Field> originalFields;
    /**
     * 原属性Map集合
     */
    private Map<String, Field> originalFieldCaches;
    /**
     * 所有属性
     */
    private Set<ReflectField> fields;
    /**
     * get方法
     */
    private final Map<String, Method> getMethods = new HashMap<>();
    /**
     * get方法返回值类型
     */
    private final Map<String, Class<?>> getTypes = new HashMap<>();
    /**
     * set方法
     */
    private final Map<String, Method> setMethods = new HashMap<>();
    /**
     * set方法返回值类型
     */
    private final Map<String, Class<?>> setTypes = new HashMap<>();
    /**
     * 过滤超类{@link Predicate}对象
     */
    private Predicate<? super Class<?>> classFilter;
    /**
     * 过滤属性{@link Predicate}
     */
    private Predicate<? super Field> fieldFilter;
    /**
     * get方法过滤器{@link Predicate}
     */
    private Predicate<? super Method> getMethodFilter;
    /**
     * set方法过滤器{@link Predicate}
     */
    private Predicate<? super Method> setMethodFilter;

    /**
     * 构造方法
     * @param clazz 类
     */
    public Reflector(Class<?> clazz) {
        this.clazz = clazz;
    }

    /**
     * 设置超类过滤器
     * @param predicate {@link Predicate}对象
     * @return {@link Reflector}
     */
    public Reflector classFilter(final Predicate<? super Class<?>> predicate) {
        this.classFilter = predicate;
        return this;
    }

    /**
     * 属性过滤器
     * @param predicate {@link Predicate}对象
     * @return {@link Reflector}
     */
    public Reflector fieldFilter(final Predicate<? super Field> predicate) {
        this.fieldFilter = predicate;
        return this;
    }

    /**
     * get方法过滤器
     * @param predicate {@link Predicate}对象
     * @return {@link Reflector}
     */
    public Reflector getMethodFilter(final Predicate<? super Method> predicate) {
        this.getMethodFilter = predicate;
        return this;
    }

    /**
     * set方法过滤器
     * @param predicate {@link Predicate}对象
     * @return {@link Reflector}
     */
    public Reflector setMethodFilter(final Predicate<? super Method> predicate) {
        this.setMethodFilter = predicate;
        return this;
    }

    /**
     * 解析属性
     * @return {@link Reflector}
     */
    public Reflector parse() {
        return this.invoke();
    }

    /**
     * 处理属性
     * @return {@link Reflector}
     */
    private Reflector invoke() {
        // 获取原属性
        this.originalFields = Optional.ofNullable(this.classFilter).map(it ->
            Reflections.getAllFields(this.clazz, it, this.fieldFilter)
        ).orElse(Reflections.getAllFields(this.clazz));
        if (Objects.isEmpty(this.originalFields)) {
            this.originalFieldCaches = new HashMap<>(0);
        } else {
            this.originalFieldCaches = this.originalFields.stream().collect(Collectors.toMap(Field::getName, it -> it));
        }
        // 获取类注解
        this.annotations =
            ImmutableSet.of(Reflections.getAllAnnotations(this.clazz, Reflections.METADATA_ANNOTATION_FILTER));
        if (Objects.isNotEmpty(this.annotations)) {
            this.annotationCaches = ImmutableMap.of(this.annotations.stream().collect(Collectors.toMap(it ->
                it.annotationType().getCanonicalName(), it -> it)));
        } else {
            this.annotationCaches = ImmutableMap.of();
        }
        // 获取get方法
        this.addGetMethods();
        // 获取set方法
        this.addSetMethods();
        this.fields = new LinkedHashSet<>();
        // 添加属性
        this.addFields();
        return this;
    }

    /**
     * 处理get方法
     */
    private void addGetMethods() {
        final Set<Method> methods = Reflections.getAllMethods(this.clazz, this.classFilter, this.getMethodFilter);
        if (Objects.isNotEmpty(methods)) {
            final Map<String, List<Method>> conflictingGetters = new HashMap<>();
            methods.forEach(it -> addMethodConflict(conflictingGetters, Reflections.methodToProperty(it.getName()), it));
            resolveGetterConflicts(conflictingGetters);
        }
    }

    /**
     * 解决get方法冲突
     * @param conflictingGetters 冲突方法缓存
     */
    private void resolveGetterConflicts(Map<String, List<Method>> conflictingGetters) {
        for (Map.Entry<String, List<Method>> entry : conflictingGetters.entrySet()) {
            Method winner = null;
            String property = entry.getKey();
            for (Method candidate : entry.getValue()) {
                if (winner == null) {
                    winner = candidate;
                    continue;
                }
                final Class<?> winnerType = winner.getReturnType();
                final Class<?> candidateType = candidate.getReturnType();
                if (candidateType.equals(winnerType)) {
                    if (!boolean.class.equals(candidateType)) {
                        log.warn("Illegal overloaded getter method with ambiguous type for property {} in class {}" +
                                ". This breaks the JavaBeans specification and can cause unpredictable results.",
                            property, winner.getDeclaringClass());
                    } else if (candidate.getName().startsWith("is")) {
                        winner = candidate;
                    }
                } else if (candidateType.isAssignableFrom(winnerType)) {
                    // OK getter type is descendant
                } else if (winnerType.isAssignableFrom(candidateType)) {
                    winner = candidate;
                } else {
                    log.warn("Illegal overloaded getter method with ambiguous type for property "
                        + "{} in class {}. This breaks the JavaBeans specification and can " +
                        "cause unpredictable results.", property, winner.getDeclaringClass());
                }
            }
            addGetMethod(property, winner);
        }
    }

    /**
     * 添加get方法
     * @param property 属性
     * @param method   get方法
     */
    private void addGetMethod(final String property, final Method method) {
        this.getMethods.put(property, method);
        this.getTypes.put(property, Reflections.typeToClass(TypeParameterResolver.resolveReturnType(method, this.clazz)));
    }

    /**
     * 处理set方法
     */
    private void addSetMethods() {
        final Set<Method> methods = Reflections.getAllMethods(this.clazz, this.classFilter, this.setMethodFilter);
        if (Objects.isNotEmpty(methods)) {
            final Map<String, List<Method>> conflictingSetters = new HashMap<>();
            methods.forEach(it -> addMethodConflict(conflictingSetters, Reflections.methodToProperty(it.getName()), it));
            resolveSetterConflicts(conflictingSetters);
        }
    }

    /**
     * 解决set方法冲突
     * @param conflictingSetters 冲突方法缓存
     */
    private void resolveSetterConflicts(Map<String, List<Method>> conflictingSetters) {
        for (String property : conflictingSetters.keySet()) {
            final List<Method> setters = conflictingSetters.get(property);
            final Class<?> getterType = this.getTypes.get(property);
            Method match = null;
            ReflectionException error = null;
            for (Method setter : setters) {
                if (setter.getParameterTypes()[0].equals(getterType)) {
                    match = setter;
                    break;
                }
                if (error == null) {
                    try {
                        match = pickBetterSetter(match, setter, property);
                    } catch (ReflectionException e) {
                        match = null;
                        error = e;
                    }
                }
            }
            if (match == null) {
                log.warn("", error);
            } else {
                addSetMethod(property, match);
            }
        }
    }

    /**
     * 添加get方法
     * @param property 属性
     * @param method   get方法
     */
    private void addSetMethod(final String property, final Method method) {
        this.setMethods.put(property, method);
        this.setTypes.put(property,
            Reflections.typeToClass(TypeParameterResolver.resolveParamTypes(method, this.clazz)[0]));
    }

    private Method pickBetterSetter(Method setter1, Method setter2, String property) {
        if (setter1 == null) {
            return setter2;
        }
        final Class<?> paramType1 = setter1.getParameterTypes()[0];
        final Class<?> paramType2 = setter2.getParameterTypes()[0];
        if (paramType1.isAssignableFrom(paramType2)) {
            return setter2;
        } else if (paramType2.isAssignableFrom(paramType1)) {
            return setter1;
        }
        throw new ReflectionException("Ambiguous setters defined for property '" + property + "' in class '"
            + setter2.getDeclaringClass() + "' with types '" + paramType1.getName() + "' and '"
            + paramType2.getName() + "'.");
    }

    /**
     * 添加冲突方法
     * @param conflictingMethods 冲突方法缓存
     * @param name               方法名称
     * @param method             {@link Method}
     */
    private void addMethodConflict(final Map<String, List<Method>> conflictingMethods,
                                   final String name, final Method method) {
        final List<Method> methods = conflictingMethods.computeIfAbsent(name, k -> new ArrayList<>());
        methods.add(method);
    }

    /**
     * 添加属性
     */
    private void addFields() {
        if (Objects.isNotEmpty(this.originalFields)) {
            for (Field field : this.originalFields) {
                final String property = field.getName();
                final int modifiers = field.getModifiers();
                if (!this.setMethods.containsKey(property)
                    && !Modifier.isFinal(modifiers) && !Modifier.isStatic(modifiers)) {
                    this.addSetField(field);
                }
                if (!this.getMethods.containsKey(property)) {
                    this.addGetField(field);
                } else {
                    // 属性存在，且get方法存在则认为是有效属性
                    this.fields.add(new ReflectField(field, this.getTypes.get(property),
                        property, this.getMethods.get(property), this.setMethods.get(property)));
                }
            }
        }
    }

    /**
     * 添加set属性
     * @param field {@link Field}
     */
    private void addSetField(final Field field) {
        final String property;
        if (Reflections.isValidPropertyName((property = field.getName()))) {
            this.setMethods.put(property, null);
            this.setTypes.put(property,
                Reflections.typeToClass(TypeParameterResolver.resolveFieldType(field, this.clazz)));
        }
    }

    /**
     * 添加get属性
     * @param field {@link Field}
     */
    private void addGetField(final Field field) {
        final String property;
        if (Reflections.isValidPropertyName((property = field.getName()))) {
            this.getMethods.put(property, null);
            this.getTypes.put(property,
                Reflections.typeToClass(TypeParameterResolver.resolveFieldType(field, this.clazz)));
        }
    }

    /**
     * 创建反射器对象
     * @param clazz 目标类
     * @return 反射器对象
     */
    public static Reflector of(final Class<?> clazz) {
        return new Reflector(clazz);
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public Set<? extends Annotation> getAnnotations() {
        return this.annotations;
    }

    public Map<String, ? extends Annotation> getAnnotationCaches() {
        return this.annotationCaches;
    }

    public Set<Field> getOriginalFields() {
        return ImmutableLinkedSet.of(this.originalFields);
    }

    public Map<String, Field> getOriginalFieldCaches() {
        return ImmutableMap.of(this.originalFieldCaches);
    }

    public Set<ReflectField> getFields() {
        return ImmutableLinkedSet.of(this.fields);
    }

    public Map<String, Method> getGetMethods() {
        return ImmutableMap.of(this.getMethods);
    }

    public Map<String, Class<?>> getGetTypes() {
        return ImmutableMap.of(this.getTypes);
    }

    public Map<String, Method> getSetMethods() {
        return ImmutableMap.of(this.setMethods);
    }

    public Map<String, Class<?>> getSetTypes() {
        return ImmutableMap.of(this.setTypes);
    }

    public Predicate<? super Class<?>> getClassFilter() {
        return classFilter;
    }

    public Predicate<? super Field> getFieldFilter() {
        return fieldFilter;
    }

    public Predicate<? super Method> getGetMethodFilter() {
        return getMethodFilter;
    }

    public Predicate<? super Method> getSetMethodFilter() {
        return setMethodFilter;
    }
}
