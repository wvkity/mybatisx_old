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
package com.github.mybatisx.core.auditable;

import com.github.mybatisx.Objects;
import com.github.mybatisx.auditable.AuditedPattern;
import com.github.mybatisx.auditable.PropertyWrapper;
import com.github.mybatisx.basic.metadata.Table;
import com.github.mybatisx.core.utils.Columns;
import com.github.mybatisx.plugin.auditable.support.AuditedPropertyLoader;
import org.apache.ibatis.mapping.MappedStatement;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 默认审计属性加载器
 * @author wvkity
 * @created 2021-07-16
 * @since 1.0.0
 */
public class DefaultAuditedPropertyLoader implements AuditedPropertyLoader {

    protected final SourceTableParser parser = new SourceTableParser();

    @Override
    public List<PropertyWrapper> load(MappedStatement ms, Object target, AuditedPattern pattern,
                                      boolean isInsert, boolean isDelete) {
        final Table table = this.parser.parse(ms, target);
        if (Objects.nonNull(table)) {
            if (isInsert) {
                return table.insertableColumns().stream().filter(it -> it.getAuditMeta().insertedAuditable())
                    .map(Columns::toProperty).collect(Collectors.toList());
            } else if (isDelete && table.isLogicDelete()) {
                return table.updatableColumns().stream().filter(it -> it.getAuditMeta().deletedAuditable())
                    .map(Columns::toProperty).collect(Collectors.toList());
            } else {
                return table.updatableColumns().stream().filter(it -> it.getAuditMeta().modifiedAuditable())
                    .map(Columns::toProperty).collect(Collectors.toList());
            }
        }
        return null;
    }
}
