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
package io.github.mybatisx.core.inject.mapping.sql.update;

import io.github.mybatisx.basic.metadata.Column;
import io.github.mybatisx.basic.metadata.Table;
import io.github.mybatisx.constant.Constants;
import io.github.mybatisx.core.inject.mapping.sql.AbstractSupplier;
import io.github.mybatisx.core.inject.mapping.utils.Scripts;
import io.github.mybatisx.support.config.MyBatisGlobalConfiguration;
import io.github.mybatisx.support.constant.Operation;

import java.util.Iterator;
import java.util.Set;

/**
 * {@code update}方法SQL提供器
 * @author wvkity
 * @created 2020-10-22
 * @since 1.0.0
 */
public class UpdateSupplier extends AbstractSupplier {

    public UpdateSupplier(Table table, MyBatisGlobalConfiguration configuration) {
        super(table, configuration);
    }

    @Override
    public String get() {
        final Set<Column> columns = table.updateColumnsWithoutSpecial();
        final StringBuilder script = new StringBuilder(100);
        final Iterator<Column> iterator = columns.iterator();
        boolean hasNext = iterator.hasNext();
        while (hasNext) {
            final Column it = iterator.next();
            script.append(SPACE).append(Scripts.convertToPartArg(Constants.PARAM_ENTITY, Operation.REPLACE, it))
                .append(COMMA);
            hasNext = iterator.hasNext();
            if (hasNext) {
                script.append(NEW_LINE);
            }
        }
        table.optimisticLockOptional().map(this::convertToOptimisticLockIfTag).ifPresent(it ->
            script.append(NEW_LINE).append(it));
        // 主键
        final StringBuilder condition = this.addPrimaryKeyCondition();
        // 乐观锁
        this.addOptimisticLockCondition(condition);
        // 租户
        this.addMultiTenantCondition(condition);
        return update(Scripts.convertToTrimTag(script.toString(), "SET", null, null, COMMA_SPACE),
            Scripts.convertToTrimTag(condition.toString(), "WHERE", "AND |OR ", NULL, NULL));
    }
}
