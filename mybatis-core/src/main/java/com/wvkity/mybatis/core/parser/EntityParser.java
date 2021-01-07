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
package com.wvkity.mybatis.core.parser;

import com.wvkity.mybatis.core.config.MyBatisGlobalConfiguration;
import com.wvkity.mybatis.core.metadata.Table;
import com.wvkity.mybatis.core.builder.support.TableBuilder;

/**
 * 实体解析器
 * @author wvkity
 * @created 2020-10-03
 * @since 1.0.0
 */
public interface EntityParser {

    /**
     * 解析实体-数据库表映射信息
     * @param configuration {@link MyBatisGlobalConfiguration}
     * @param entity        实体类
     * @param builder       数据库表映射构建器
     * @return {@link Table}
     */
    Table parse(final MyBatisGlobalConfiguration configuration, final Class<?> entity, final TableBuilder builder);
}
