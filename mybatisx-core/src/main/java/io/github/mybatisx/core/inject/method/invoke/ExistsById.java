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
package io.github.mybatisx.core.inject.method.invoke;

import io.github.mybatisx.basic.metadata.Table;
import io.github.mybatisx.core.inject.mapping.sql.select.ExistsByIdSupplier;
import io.github.mybatisx.core.inject.method.support.AbstractSelectMethod;
import org.apache.ibatis.mapping.MappedStatement;

/**
 * {@code existsById}方法映射
 * @author wvkity
 * @created 2020-11-15
 * @since 1.0.0
 */
public class ExistsById extends AbstractSelectMethod<ExistsByIdSupplier> {

    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperInterface, Class<?> resultType, Table table) {
        return super.injectMappedStatement(mapperInterface, int.class, table);
    }
}
