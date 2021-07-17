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
package com.github.mybatisx.support.parser;


import com.github.mybatisx.Objects;
import com.github.mybatisx.basic.metadata.Field;
import com.github.mybatisx.basic.parser.FieldParser;
import com.github.mybatisx.basic.reflect.ReflectField;
import com.github.mybatisx.basic.reflect.Reflector;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * 默认属性解析器
 * @author wvkity
 * @created 2020-10-03
 * @since 1.0.0
 */
public class DefaultFieldParser implements FieldParser {

    @Override
    public Set<Field> parse(Reflector reflector) {
        final Set<ReflectField> fields = reflector.getFields();
        final Set<Field> result;
        if (Objects.isNotEmpty(fields)) {
            result = new LinkedHashSet<>(fields.size());
            fields.forEach(it -> {
                final Field field = new Field(it.getField(), it.getName(), it.getJavaType(),
                    it.getGetter(), it.getSetter());
                field.handle();
                result.add(field);
            });
        } else {
            result = new LinkedHashSet<>(0);
        }
        return result;
    }
}
