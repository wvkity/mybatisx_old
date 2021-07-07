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
package com.github.mybatisx.auditable;

import com.github.mybatisx.basic.metadata.Column;
import com.github.mybatisx.basic.metadata.Table;
import com.github.mybatisx.core.utils.Columns;
import com.github.mybatisx.plugin.auditable.support.AuditMatcher;
import org.apache.ibatis.mapping.MappedStatement;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 默认审计属性匹配器
 * @author wvkity
 * @created 2021-03-06
 * @since 1.0.0
 */
public class DefaultAuditMatcher implements AuditMatcher {

    private final MetadataParser parser = new MetadataParser();

    @Override
    public List<OriginalProperty> matches(MappedStatement ms, Object target, MatchMode mode,
                                          boolean insert, boolean logicDelete) {
        final Table table = this.parser.parse(ms, target);
        if (table != null) {
            if (insert) {
                return table.insertableColumns().stream()
                    .filter(it -> it.getAuditor().insertedAuditable())
                    .map(this::convert)
                    .collect(Collectors.toList());
            } else if (logicDelete) {
                return table.updatableColumns().stream()
                    .filter(it -> it.getAuditor().deletedAuditable())
                    .map(this::convert)
                    .collect(Collectors.toList());
            } else {
                return table.updatableColumns().stream()
                    .filter(it -> it.getAuditor().modifiedAuditable())
                    .map(this::convert)
                    .collect(Collectors.toList());
            }
        }
        return null;
    }

    protected OriginalProperty convert(final Column column) {
        return Columns.toProperty(column);
    }

}
