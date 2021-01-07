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

import com.wvkity.mybatis.annotation.Entity;
import com.wvkity.mybatis.core.constant.Constants;
import com.wvkity.mybatis.core.reflect.Reflections;
import com.wvkity.mybatis.core.utils.Strings;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

/**
 * @author wvkity
 * @created 2020-10-06
 * @since 1.0.0
 */
public class SupperTypeTest {

    private static final Predicate<Class<?>> DEFAULT_SUPER_CLASS_FILTER = it ->
        it != null
            && !Object.class.equals(it)
            && (Reflections.isAnnotationPresent(it, Entity.class, Constants.JPA_ENTITY) ||
            (!Map.class.isAssignableFrom(it) && !Collection.class.isAssignableFrom(it)));

    @Test
    public void test() {
        /*final Set<Class<?>> classes = Reflections.getAllSuperTypes(BaseEntity.class, DEFAULT_SUPER_CLASS_FILTER);
        System.out.println(classes);*/
        final Set<Annotation> annotations = Reflections.getAllAnnotations(BaseEntity.class,
            Reflections.METADATA_ANNOTATION_FILTER);
        System.out.println(annotations);

        final Map<String, Object> map = new ConcurrentHashMap<>();
        map.putIfAbsent("a", 1);
        map.putIfAbsent("b", 1);
        System.out.println(map.get("c"));
        System.out.println(map.get("a"));
    }

    @Test
    public void test1() {
        System.out.println(Strings.firstCharToLower("InsertInvoker".replaceAll("^([a-zA-Z0-9_]+)(Invoker)$", "$1")));
        System.out.println(new Date().getTime());
        System.out.println(Instant.now().toEpochMilli());
    }
}
