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
package io.github.mybatisx.reflect;

import io.github.mybatisx.Objects;
import io.github.mybatisx.exception.ReflectionException;
import io.github.mybatisx.immutable.ImmutableSet;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.ReflectPermission;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author wvkity
 * @created 2020-10-05
 * @since 1.0.0
 */
public final class Reflections {

    /**
     * ??????????????????
     */
    private static final Set<String> PRIMITIVE_NAMES;
    /**
     * ???????????????
     */
    private static final Set<Class<?>> PRIMITIVE_TYPES;
    /**
     * ?????????????????????
     */
    private static final Set<String> PRIMITIVE_DESCRIPTORS;
    /**
     * ????????????????????????
     */
    private static final Set<String> WRAPPER_NAMES;
    /**
     * ?????????????????????
     */
    private static final Set<Class<?>> WRAPPER_TYPES;
    /**
     * ????????????
     */
    private static final Set<Class<?>> SIMPLE_TYPES;
    /**
     * JDK8 TIME API
     */
    private static final String[] JDK_8_TIME_API;
    /**
     * ????????????Object???
     */
    private static final boolean INCLUDE_OBJECT = false;
    /**
     * ???????????????
     */
    public static final Set<Class<? extends Annotation>> METADATA_ANNOTATION_TYPES;
    /**
     * ?????????????????????
     */
    public static final Predicate<Class<?>> SUPER_CLASS_FILTER;
    /**
     * ????????????????????????
     */
    public static final Predicate<Annotation> METADATA_ANNOTATION_FILTER;
    /**
     * ????????????????????????
     */
    public static final Set<String> ANNOTATION_METHOD_NAMES;
    /**
     * ?????????????????????
     */
    public static final Predicate<? super Method> ANNOTATION_METHOD_FILTER;
    /**
     * ????????????
     */
    private static final Map<Class<?>, Set<Class<?>>> CLASS_CACHES = Collections.synchronizedMap(new WeakHashMap<>());

    //// JAVA???????????? ////
    /**
     * CGLIB?????????
     */
    public static final String CGLIB_PROXY = "net.sf.cglib.proxy.Factory";
    /**
     * JAVASSIST?????????
     */
    public static final String JAVASSIST_PROXY = "javassist.util.proxy.ProxyObject";
    /**
     * SPRING?????????
     */
    public static final String SPRING_CGLIB_PROXY = "org.springframework.cglib.proxy.Factory";
    /**
     * MYBATIS????????????
     */
    public static final String MYBATIS_PROXY = "org.apache.ibatis.javassist.util.proxy.ProxyObject";
    /**
     * ???????????????
     */
    private static final Set<String> PROXY_CLASS_CACHE = ImmutableSet.construct(CGLIB_PROXY, JAVASSIST_PROXY,
        SPRING_CGLIB_PROXY, MYBATIS_PROXY);

    static {
        PRIMITIVE_NAMES = ImmutableSet.of(Arrays.asList("boolean", "char", "byte", "short", "int", "long",
            "float", "double", "void"));
        PRIMITIVE_TYPES = ImmutableSet.of(Arrays.asList(boolean.class, char.class, byte.class, short.class,
            int.class, long.class, float.class, double.class, void.class));
        PRIMITIVE_DESCRIPTORS = ImmutableSet.of(Arrays.asList("Z", "C", "B", "S", "I", "J", "F", "D", "V"));
        WRAPPER_NAMES = ImmutableSet.of(Arrays.asList("Boolean", "Character", "Byte", "Short", "Integer",
            "Long", "Float", "Double", "Void"));
        WRAPPER_TYPES = ImmutableSet.of(Arrays.asList(Boolean.class, Character.class, Byte.class, Short.class,
            Integer.class, Long.class, Float.class, Double.class, Void.class));
        JDK_8_TIME_API = new String[]{
            "java.time.Instant",
            "java.time.LocalDateTime",
            "java.time.LocalDate",
            "java.time.LocalTime",
            "java.time.OffsetDateTime",
            "java.time.OffsetTime",
            "java.time.ZonedDateTime",
            "java.time.Year",
            "java.time.Month",
            "java.time.YearMonth"
        };
        SIMPLE_TYPES = new HashSet<>(48);
        SIMPLE_TYPES.addAll(PRIMITIVE_TYPES);
        SIMPLE_TYPES.addAll(WRAPPER_TYPES);
        SIMPLE_TYPES.add(Date.class);
        SIMPLE_TYPES.add(Timestamp.class);
        SIMPLE_TYPES.add(Calendar.class);
        SIMPLE_TYPES.add(Class.class);
        SIMPLE_TYPES.add(BigInteger.class);
        SIMPLE_TYPES.add(BigDecimal.class);
        SIMPLE_TYPES.add(String.class);
        Arrays.stream(JDK_8_TIME_API).forEach(Reflections::registrySimpleType);
        METADATA_ANNOTATION_TYPES = ImmutableSet.of(Arrays.asList(Inherited.class, Documented.class, Target.class,
            Retention.class, SuppressWarnings.class, Override.class, SafeVarargs.class));
        SUPER_CLASS_FILTER = it ->
            it != null && !Object.class.equals(it) && !Serializable.class.equals(it)
                && !Annotation.class.equals(it)
                && !Map.class.isAssignableFrom(it) && !Collection.class.isAssignableFrom(it);
        METADATA_ANNOTATION_FILTER = it -> !METADATA_ANNOTATION_TYPES.contains(it.annotationType());
        ANNOTATION_METHOD_NAMES = ImmutableSet.of("annotationType", "toString", "hashCode", "equals");
        ANNOTATION_METHOD_FILTER = it -> !ANNOTATION_METHOD_NAMES.contains(it.getName());
    }

    /**
     * ??????????????????
     * @param classes ????????????([,;:]??????)
     */
    public static void registrySimpleTypeLicence(final String classes) {
        if (Objects.isNotBlank(classes)) {
            Arrays.stream(classes.split("([,:;])(\\s*)?")).filter(Objects::isNotBlank)
                .forEach(Reflections::registrySimpleType);
        }
    }

    /**
     * ??????????????????
     * @param className ??????
     */
    public static void registrySimpleType(final String className) {
        if (Objects.isNotBlank(className)) {
            try {
                registrySimpleType(Class.forName(className));
            } catch (Exception ignore) {
                // ignore
            }
        }
    }

    /**
     * ??????????????????
     * @param clazz ???
     */
    public static void registrySimpleType(final Class<?> clazz) {
        Optional.ofNullable(clazz).ifPresent(SIMPLE_TYPES::add);
    }

    /**
     * ???????????????????????????????????????
     * @param target ????????????
     * @return boolean
     */
    public static boolean isSimpleJavaObject(final Object target) {
        return Objects.nonNull(target) && Object.class != target.getClass()
            && SIMPLE_TYPES.contains(target.getClass());
    }

    /**
     * ???????????????????????????????????????
     * @param clazz ????????????
     * @return boolean
     */
    public static boolean isSimpleJavaType(final Class<?> clazz) {
        return Objects.nonNull(clazz) && SIMPLE_TYPES.contains(clazz);
    }

    /**
     * ?????????????????????
     * @return ?????????????????????
     */
    public static Set<String> getPrimitiveNames() {
        return PRIMITIVE_NAMES;
    }

    /**
     * ?????????????????????
     * @return ???????????????
     */
    public static Set<Class<?>> getPrimitiveTypes() {
        return PRIMITIVE_TYPES;
    }

    /**
     * ?????????????????????
     * @return ?????????????????????
     */
    public static Set<String> getPrimitiveDescriptors() {
        return PRIMITIVE_DESCRIPTORS;
    }

    /**
     * ?????????????????????????????????
     * @return ?????????????????????????????????
     */
    public static Set<String> getWrapperNames() {
        return WRAPPER_NAMES;
    }

    /**
     * ???????????????????????????????????????????????????
     * @param clazz ???
     * @return boolean
     */
    public static boolean isPrimitiveOrWrapType(final Class<?> clazz) {
        return PRIMITIVE_TYPES.contains(clazz) || WRAPPER_TYPES.contains(clazz);
    }

    /**
     * ????????????????????????
     * @param clazz ????????????
     * @return boolean
     */
    public static boolean isProxy(final Class<?> clazz) {
        return Optional.ofNullable(clazz).map(cz ->
            Arrays.stream(cz.getInterfaces()).anyMatch(it -> PROXY_CLASS_CACHE.contains(it.getName()))).orElse(false);
    }

    /**
     * ???????????????????????????
     * @param clazz ???
     * @return ?????????
     */
    public static Class<?> getRealClass(final Class<?> clazz) {
        return isProxy(clazz) ? clazz.getSuperclass() : clazz;
    }

    /**
     * ?????????????????????????????????
     * @param clazz ???
     * @param args  ????????????
     * @param <T>   ????????????
     * @return ??????
     * @throws InvocationTargetException {@link InvocationTargetException}??????
     * @throws NoSuchMethodException     {@link NoSuchMethodException}??????
     * @throws InstantiationException    {@link InstantiationException}??????
     * @throws IllegalAccessException    {@link IllegalAccessException}??????
     */
    public static <T> T newInstance(final Class<T> clazz, Object... args) throws InvocationTargetException,
        NoSuchMethodException, InstantiationException, IllegalAccessException {
        final Constructor<T> constructor;
        if (Objects.isEmpty(args)) {
            constructor = clazz.getDeclaredConstructor();
            try {
                return constructor.newInstance();
            } catch (IllegalAccessException e) {
                if (canControlMemberAccessible()) {
                    constructor.setAccessible(true);
                    return constructor.newInstance();
                }
                throw e;
            }
        } else {
            constructor = clazz.getDeclaredConstructor(getArgumentType(args));
            try {
                return constructor.newInstance(args);
            } catch (IllegalAccessException e) {
                if (canControlMemberAccessible()) {
                    constructor.setAccessible(true);
                    return constructor.newInstance(args);
                }
                throw e;
            }
        }
    }

    /**
     * ??????????????????
     * @param args ????????????
     * @return ??????????????????
     */
    public static Class<?>[] getArgumentType(final Object... args) {
        if (Objects.isEmpty(args)) {
            return new Class<?>[0];
        }
        return Arrays.stream(args).map(it -> it == null ? Object.class : it.getClass()).toArray(Class[]::new);
    }

    /**
     * ????????????????????????
     * @param method ????????????
     * @return ??????????????????
     */
    public static Class<?>[] getMethodArgType(final Method method) {
        return method == null ? new Class[0] : method.getParameterTypes();
    }

    /**
     * ???????????????????????????
     * @param clazz ?????????
     * @param index ??????
     * @return ?????????
     */
    public static Class<?> getGenericClass(final Class<?> clazz, final int index) {
        return getGenericClass(clazz.getGenericSuperclass(), index);
    }

    /**
     * ???????????????????????????
     * @param type  ??????
     * @param index ??????
     * @return ?????????
     */
    public static Class<?> getGenericClass(final Type type, final int index) {
        if (!(type instanceof ParameterizedType)) {
            return Object.class;
        }
        final Type[] types = ((ParameterizedType) type).getActualTypeArguments();
        if (types.length == 0 || index < 0 || index > types.length) {
            return Object.class;
        }
        final Type it = types[index];
        if (it instanceof Class) {
            return (Class<?>) it;
        }
        return Object.class;
    }

    /**
     * ??????????????????(??????????????????)
     * <p>????????????????????????</p>
     * @param clazz      ?????????
     * @param predicates {@link Predicate}????????????
     * @return ?????????
     */
    @SafeVarargs
    public static Set<Class<?>> getAllTypes(final Class<?> clazz, Predicate<? super Class<?>>... predicates) {
        final Set<Class<?>> classes = CLASS_CACHES.computeIfAbsent(clazz, k -> new LinkedHashSet<>());
        if (Objects.isEmpty(classes)) {
            classes.addAll(getAllSuperTypes(clazz, predicates));
        }
        return Objects.isEmpty(classes) ? new LinkedHashSet<>(0) : new LinkedHashSet<>(classes);
    }

    /**
     * ??????????????????????????????(??????????????????)
     * @param clazz      ?????????
     * @param predicates {@link Predicate}????????????
     * @return ?????????
     */
    @SafeVarargs
    public static Set<Class<?>> getAllSuperTypes(final Class<?> clazz, Predicate<? super Class<?>>... predicates) {
        final Set<Class<?>> result = new LinkedHashSet<>();
        if (clazz != null && (INCLUDE_OBJECT || !clazz.equals(Object.class))) {
            result.add(clazz);
            for (Class<?> superType : getSuperTypes(clazz)) {
                final Set<Class<?>> parentClass = getAllSuperTypes(superType);
                if (Objects.isNotEmpty(parentClass)) {
                    result.addAll(parentClass);
                }
            }
        }
        return Objects.filters(result, predicates);
    }

    /**
     * ??????????????????????????????
     * @param clazz ?????????
     * @return ?????????
     */
    public static Set<Class<?>> getSuperTypes(final Class<?> clazz) {
        final Set<Class<?>> result = new LinkedHashSet<>();
        final Class<?> superClass = clazz.getSuperclass();
        final Class<?>[] interfaces = clazz.getInterfaces();
        if (superClass != null && (INCLUDE_OBJECT || !superClass.equals(Object.class))) {
            result.add(superClass);
        }
        if (!Objects.isEmpty(interfaces)) {
            result.addAll(Arrays.asList(interfaces));
        }
        return result;
    }

    /**
     * ??????????????????????????????
     * @param clazz      ?????????
     * @param predicates {@link Predicate}????????????
     * @return ????????????
     */
    @SafeVarargs
    public static Set<Field> getAllFields(final Class<?> clazz, final Predicate<? super Field>... predicates) {
        return getAllFields(clazz, SUPER_CLASS_FILTER, predicates);
    }

    /**
     * ??????????????????????????????
     * @param clazz      ?????????
     * @param predicate  {@link Predicate}??????
     * @param predicates {@link Predicate}????????????
     * @return ????????????
     */
    @SafeVarargs
    public static Set<Field> getAllFields(final Class<?> clazz, final Predicate<? super Class<?>> predicate,
                                          final Predicate<? super Field>... predicates) {
        return getAllFields(getAllTypes(clazz, predicate).toArray(new Class<?>[0]), predicates);
    }

    /**
     * ??????????????????????????????
     * @param classes    ???????????????
     * @param predicates {@link Predicate}????????????
     * @return ????????????
     */
    @SafeVarargs
    public static Set<Field> getAllFields(final Class<?>[] classes, final Predicate<? super Field>... predicates) {
        final Set<Field> result = new LinkedHashSet<>();
        if (!Objects.isEmpty(classes)) {
            for (Class<?> clazz : classes) {
                result.addAll(getFields(clazz, predicates));
            }
        }
        return result;
    }

    /**
     * ??????????????????????????????
     * @param clazz      ?????????
     * @param predicates {@link Predicate}????????????
     * @return ????????????
     */
    @SafeVarargs
    public static Set<Field> getFields(final Class<?> clazz, final Predicate<? super Field>... predicates) {
        return Objects.filters(clazz.getDeclaredFields(), predicates);
    }

    /**
     * ??????????????????????????????
     * @param clazz      ?????????
     * @param predicates {@link Predicate}????????????
     * @return ????????????
     */
    @SafeVarargs
    public static Set<Method> getAllMethods(final Class<?> clazz,
                                            final Predicate<? super Method>... predicates) {
        return getAllMethods(clazz, SUPER_CLASS_FILTER, predicates);
    }

    /**
     * ??????????????????????????????
     * @param clazz      ?????????
     * @param predicate  {@link Predicate}??????
     * @param predicates {@link Predicate}????????????
     * @return ????????????
     */
    @SafeVarargs
    public static Set<Method> getAllMethods(final Class<?> clazz,
                                            final Predicate<? super Class<?>> predicate,
                                            final Predicate<? super Method>... predicates) {
        return getAllMethods(getAllTypes(clazz, predicate).toArray(new Class<?>[0]), predicates);
    }

    /**
     * ??????????????????????????????
     * @param classes    ?????????
     * @param predicates {@link Predicate}????????????
     * @return ????????????
     */
    @SafeVarargs
    public static Set<Method> getAllMethods(final Class<?>[] classes, final Predicate<? super Method>... predicates) {
        final Set<Method> result = new LinkedHashSet<>();
        if (!Objects.isEmpty(classes)) {
            final Map<String, Method> uniqueMethods = new HashMap<>();
            for (Class<?> clazz : classes) {
                addUniqueMethods(uniqueMethods, getMethods(clazz, predicates));
            }
            result.addAll(uniqueMethods.values());
        }
        return result;
    }

    /**
     * ??????????????????????????????
     * @param clazz      ?????????
     * @param predicates {@link Predicate}????????????
     * @return ????????????
     */
    @SafeVarargs
    public static Set<Method> getMethods(final Class<?> clazz, final Predicate<? super Method>... predicates) {
        return Objects.filters(clazz.isInterface() ? clazz.getMethods() : clazz.getDeclaredMethods(), predicates);
    }

    /**
     * ??????????????????
     * @param uniqueMethods ????????????
     * @param methods       ?????????????????????
     */
    private static void addUniqueMethods(final Map<String, Method> uniqueMethods, final Set<Method> methods) {
        if (Objects.isNotEmpty(methods)) {
            for (Method method : methods) {
                if (!method.isBridge()) {
                    final String signature = getMethodSignature(method);
                    uniqueMethods.putIfAbsent(signature, method);
                }
            }
        }
    }

    /**
     * ??????????????????
     * @param method {@link Method}
     * @return ??????
     */
    public static String getMethodSignature(final Method method) {
        return Optional.ofNullable(method).map(it -> {
            final StringBuilder builder = new StringBuilder();
            final Class<?> returnType = method.getReturnType();
            if (returnType != null) {
                builder.append(returnType.getName()).append("#");
            }
            builder.append(method.getName());
            final Class<?>[] parameterTypes = method.getParameterTypes();
            if (!Objects.isEmpty(parameterTypes)) {
                builder.append(':');
                builder.append(Arrays.stream(parameterTypes).map(Class::getName).collect(Collectors.joining(",")));
            }
            return builder.toString();
        }).orElse(null);
    }

    /**
     * ??????????????????
     * @param target     ????????????
     * @param methodName ?????????
     * @param args       ????????????
     * @param <T>        ????????????
     * @throws Exception ????????????????????????
     */
    public static <T> void invokeVoidVirtual(final T target, final String methodName,
                                             final Object... args) throws Exception {
        invokeVirtual(target, methodName, Void.class, args);
    }

    /**
     * ??????????????????
     * @param target         ????????????
     * @param methodName     ?????????
     * @param parameterTypes ????????????????????????
     * @param args           ????????????
     * @param <T>            ????????????
     * @throws Exception ????????????????????????
     */
    public static <T> void invokeVoidVirtual(final T target, final String methodName,
                                             final Class<?>[] parameterTypes,
                                             final Object... args) throws Exception {
        if (Objects.nonNull(target)) {
            invokeVirtual(target, methodName, Void.class, parameterTypes, args);
        }
    }

    /**
     * ??????????????????
     * @param target     ????????????
     * @param methodName ?????????
     * @param args       ????????????
     * @param <T>        ????????????
     * @throws Exception ????????????????????????
     */
    public static <T> void invokeConsistentVirtual(final T target, final String methodName,
                                                   final Object... args) throws Exception {
        if (Objects.nonNull(target)) {
            invokeVirtual(target, methodName, target.getClass().getSuperclass(), args);
        }
    }

    /**
     * ??????????????????
     * @param target     ????????????
     * @param methodName ?????????
     * @param returnType ???????????????
     * @param args       ????????????
     * @param <T>        ????????????
     * @throws Exception ????????????????????????
     */
    public static <T> void invokeVirtual(final T target, final String methodName,
                                         final Class<?> returnType, final Object... args) throws Exception {
        invokeVirtual(target, methodName, returnType, Reflections.getArgumentType(args), args);
    }

    /**
     * ??????????????????
     * @param target         ????????????
     * @param methodName     ?????????
     * @param returnType     ???????????????
     * @param parameterTypes ????????????????????????
     * @param args           ????????????
     * @param <T>            ????????????
     * @throws Exception ????????????????????????
     */
    public static <T> void invokeVirtual(final T target, final String methodName,
                                         final Class<?> returnType, final Class<?>[] parameterTypes,
                                         final Object... args) throws Exception {
        if (Objects.nonNull(target) && Objects.isNotBlank(methodName) && Objects.nonNull(returnType)) {
            invokeVirtual(target, methodName, MethodType.methodType(returnType, parameterTypes), args);
        }
    }

    /**
     * ??????????????????
     * @param target     ????????????
     * @param methodName ?????????
     * @param methodType {@link MethodType}
     * @param args       ????????????
     * @param <T>        ????????????
     * @throws Exception ????????????????????????
     */
    public static <T> void invokeVirtual(final T target, final String methodName, final MethodType methodType,
                                         final Object... args) throws Exception {
        if (Objects.nonNull(target) && Objects.isNotBlank(methodName) && Objects.nonNull(methodType)) {
            try {
                final MethodHandle methodHandle =
                    MethodHandles.lookup().findVirtual(target.getClass(), methodName, methodType);
                methodHandle.bindTo(target).invokeWithArguments(args);
            } catch (Throwable e) {
                throw new Exception(e);
            }
        }
    }

    /**
     * ?????????????????????????????????
     * @param type       ????????????
     * @param predicates {@link Predicate}????????????
     * @param <T>        ????????????
     * @return ????????????
     */
    @SafeVarargs
    public static <T extends AnnotatedElement> Set<Annotation> getAllAnnotations(final T type,
                                                                                 Predicate<Annotation>... predicates) {
        return getAllAnnotations(type, SUPER_CLASS_FILTER, predicates);
    }

    /**
     * ?????????????????????????????????
     * @param type       ????????????
     * @param predicate  ???????????????
     * @param predicates ?????????????????????
     * @param <T>        ????????????
     * @return ????????????
     */
    @SafeVarargs
    public static <T extends AnnotatedElement> Set<Annotation> getAllAnnotations(final T type,
                                                                                 Predicate<? super Class<?>> predicate,
                                                                                 Predicate<Annotation>... predicates) {
        final List<AnnotatedElement> types = new ArrayList<>();
        if (type instanceof Class) {
            types.addAll(Optional.ofNullable(predicate).map(it ->
                getAllTypes((Class<?>) type, it)).orElse(getAllTypes((Class<?>) type)));
        } else {
            types.add(type);
        }
        return getAllAnnotations(types, predicates);
    }

    /**
     * ?????????????????????????????????
     * @param types      ??????????????????
     * @param predicates {@link Predicate}????????????
     * @param <T>        ????????????
     * @return ????????????
     */
    @SafeVarargs
    public static <T extends AnnotatedElement> Set<Annotation> getAllAnnotations(final List<T> types,
                                                                                 Predicate<Annotation>... predicates) {
        final Set<Annotation> result = new LinkedHashSet<>();
        if (Objects.isNotEmpty(types)) {
            int size = types.size();
            final List<AnnotatedElement> keys = new ArrayList<>(types);
            for (int i = 0; i < size; i++) {
                for (Annotation annotation : getAnnotations(keys.get(i), predicates)) {
                    if (result.add(annotation)) {
                        keys.add(i + 1, annotation.annotationType());
                        size += 1;
                    }
                }
            }
        }
        return result;
    }

    /**
     * ?????????????????????????????????
     * @param type       ????????????
     * @param predicates {@link Predicate}????????????
     * @param <T>        ????????????
     * @return ????????????
     */
    @SafeVarargs
    public static <T extends AnnotatedElement> Set<Annotation> getAnnotations(final T type,
                                                                              Predicate<Annotation>... predicates) {
        return Objects.filters(type.getDeclaredAnnotations(), predicates);
    }

    /**
     * ??????????????????????????????????????????
     * @param type       ????????????
     * @param annotation ????????????
     * @param <T>        ????????????
     * @return boolean
     */
    public static <T extends AnnotatedElement> boolean isAnnotationPresent(final T type,
                                                                           Class<? extends Annotation> annotation) {
        return annotation != null && withAnnotation(annotation).test(type);
    }

    /**
     * ??????????????????????????????????????????
     * @param type           ????????????
     * @param annotationName ??????????????????
     * @param predicates     {@link Predicate}????????????
     * @param <T>            ????????????
     * @return boolean
     */
    @SafeVarargs
    public static <T extends AnnotatedElement> boolean isAnnotationPresent(final T type,
                                                                           final String annotationName,
                                                                           final Predicate<Annotation>... predicates) {
        if (annotationName == null || annotationName.length() == 0) {
            return false;
        }
        final Set<Annotation> annotations = getAllAnnotations(type, predicates);
        return Objects.isNotEmpty(annotations) && annotations.stream().anyMatch(it ->
            Objects.equals(annotationName, it.annotationType().getCanonicalName()));
    }

    /**
     * ??????????????????????????????????????????
     * @param type           ????????????
     * @param annotation     ????????????
     * @param annotationName ??????????????????
     * @param predicates     {@link Predicate}????????????
     * @param <T>            ????????????
     * @return boolean
     */
    @SafeVarargs
    public static <T extends AnnotatedElement> boolean isAnnotationPresent(final T type,
                                                                           Class<? extends Annotation> annotation,
                                                                           String annotationName,
                                                                           Predicate<Annotation>... predicates) {
        return isAnnotationPresent(type, annotation) || isAnnotationPresent(type, annotationName, predicates);
    }

    public static <T extends AnnotatedElement> Predicate<T> withAnnotation(Class<? extends Annotation> annotation) {
        return it -> it != null && it.isAnnotationPresent(annotation);
    }

    /**
     * ??????????????????????????????
     * @param name ????????????
     * @return boolean
     */
    public static boolean isValidPropertyName(final String name) {
        return !(name.startsWith("$") || "serialVersionUID".equals(name) || "class".equals(name));
    }

    /**
     * ???????????????get??????
     * @param name ?????????
     * @return boolean
     */
    public static boolean isGetter(final String name) {
        return name != null && ((name.startsWith("get") && name.length() > 3)
            || (name.startsWith("is") && name.length() > 2));
    }

    /**
     * ???????????????set??????
     * @param name ?????????
     * @return boolean
     */
    public static boolean isSetter(final String name) {
        return name != null && name.startsWith("set") && name.length() > 3;
    }

    /**
     * get/set?????????????????????
     * @param name get/set?????????
     * @return ?????????
     */
    public static String methodToProperty(final String name) {
        final String newName;
        if (name.startsWith("is")) {
            newName = name.substring(2);
        } else if (name.startsWith("get") || name.startsWith("set")) {
            newName = name.substring(3);
        } else {
            throw new ReflectionException("Error parsing property name '" + name + "'.  " +
                "Didn't start with 'is', 'get' or 'set'.");
        }
        if (newName.length() == 1 || (newName.length() > 1 && !Character.isUpperCase(newName.charAt(1)))) {
            return newName.substring(0, 1).toLowerCase(Locale.ENGLISH) + newName.substring(1);
        }
        return newName;
    }

    /**
     * ???????????????????????????
     * @param src ????????????
     * @return {@link Class}
     */
    public static Class<?> typeToClass(final Type src) {
        Class<?> result = null;
        if (src instanceof Class) {
            result = (Class<?>) src;
        } else if (src instanceof ParameterizedType) {
            result = (Class<?>) ((ParameterizedType) src).getRawType();
        } else if (src instanceof GenericArrayType) {
            Type componentType = ((GenericArrayType) src).getGenericComponentType();
            if (componentType instanceof Class) {
                result = Array.newInstance((Class<?>) componentType, 0).getClass();
            } else {
                Class<?> componentClass = typeToClass(componentType);
                result = Array.newInstance(componentClass, 0).getClass();
            }
        }
        if (result == null) {
            result = Object.class;
        }
        return result;
    }

    /**
     * ???????????????Map??????
     * @param annotation ????????????
     * @param <T>        ??????????????????
     * @return map??????
     */
    public static <T extends Annotation> Map<String, Object> annotationToMap(final T annotation) {
        if (Objects.nonNull(annotation)) {
            final Set<Method> methods = getAllMethods(annotation.annotationType(),
                SUPER_CLASS_FILTER, ANNOTATION_METHOD_FILTER);
            if (Objects.isNotEmpty(methods)) {
                final Map<String, Object> result = new HashMap<>(methods.size());
                for (Method method : methods) {
                    final String property = method.getName();
                    try {
                        final Object value = method.invoke(annotation);
                        result.put(property, value);
                    } catch (IllegalAccessException e) {
                        if (canControlMemberAccessible()) {
                            try {
                                method.setAccessible(true);
                                final Object value = method.invoke(annotation);
                                result.put(property, value);
                            } catch (Exception ignore) {
                                // ignore
                            }
                        }
                    } catch (Exception ignore) {
                        // ignore
                    }
                }
                return result;
            }
        }
        return new HashMap<>(0);
    }

    /**
     * ???????????????????????????????????????
     * @return boolean
     */
    public static boolean canControlMemberAccessible() {
        try {
            SecurityManager securityManager = System.getSecurityManager();
            if (null != securityManager) {
                securityManager.checkPermission(new ReflectPermission("suppressAccessChecks"));
            }
        } catch (SecurityException ignore) {
            return false;
        }
        return true;
    }

    /**
     * ????????????
     * @param target ????????????
     * @param method ????????????
     * @param args   ??????
     * @return ????????????
     * @throws InvocationTargetException ??????????????????????????????
     * @throws IllegalAccessException    ??????????????????????????????????????????
     */
    public static Object methodInvoke(final Object target, final Method method, final Object... args) throws InvocationTargetException, IllegalAccessException {
        Objects.requireNonNull(target);
        Objects.requireNonNull(method);
        try {
            return method.invoke(target, args);
        } catch (IllegalAccessException e) {
            if (Reflections.canControlMemberAccessible()) {
                method.setAccessible(true);
                return method.invoke(target, args);
            }
        }
        return null;
    }

    /**
     * ???????????????byte/char/boolean/short/int/long?????????
     * @param javaType java??????
     * @param value    ????????????
     * @return ???
     */
    public static Object convert(final Class<?> javaType, final String value) {
        if (javaType == null || Objects.isBlank(value) || String.class.equals(javaType)) {
            return value;
        }
        if (javaType == Long.class || javaType == long.class) {
            return Long.valueOf(value);
        }
        if (javaType == Integer.class || javaType == int.class) {
            return Integer.valueOf(value);
        }
        if (javaType == Short.class || javaType == short.class) {
            return Short.valueOf(value);
        }
        if (javaType == Character.class || javaType == char.class) {
            return value.charAt(0);
        }
        if (javaType == Boolean.class || javaType == boolean.class) {
            return "1".equals(value) || Boolean.parseBoolean(value);
        }
        if (javaType == Byte.class || javaType == byte.class) {
            return Byte.parseByte(value);
        }
        return value;
    }

    /**
     * ????????????????????????????????????????????????
     * @param args ????????????
     * @return boolean
     */
    public static boolean isPureType(final Iterable<?> args) {
        Class<?> current = null;
        for (Object v : args) {
            final Class<?> clazz = v.getClass();
            if (clazz.isArray() || Map.class.isAssignableFrom(clazz) || Iterable.class.isAssignableFrom(clazz)) {
                return false;
            }
            if (Objects.nonNull(current) && !current.isAssignableFrom(clazz)) {
                return false;
            }
            current = clazz;
        }
        return true;
    }
}
