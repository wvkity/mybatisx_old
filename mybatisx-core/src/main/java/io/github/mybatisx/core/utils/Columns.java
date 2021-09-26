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
package io.github.mybatisx.core.utils;

import io.github.mybatisx.auditable.AuditType;
import io.github.mybatisx.auditable.PropertyWrapper;
import io.github.mybatisx.basic.metadata.AuditMeta;
import io.github.mybatisx.basic.metadata.Column;
import io.github.mybatisx.basic.metadata.Descriptor;
import io.github.mybatisx.basic.metadata.PrimaryKey;

/**
 * 字段工具
 * @author wvkity
 * @created 2021-03-14
 * @since 1.0.0
 */
public final class Columns {

    private Columns() {
    }

    /**
     * 转成{@link PropertyWrapper}对象
     * @param column {@link Column}
     * @return {@link PropertyWrapper}
     */
    public static PropertyWrapper toProperty(final Column column) {
        final Descriptor desc = column.getDescriptor();
        final AuditMeta auditMeta = column.getAuditMeta();
        final PrimaryKey pk = column.getPrimaryKey();
        return new PropertyWrapper(column.getEntity(), desc.getField(), desc.getName(),
            desc.getJavaType(), column.isUnique(), pk.isUuid(), pk.isSnowflake(), desc.getGetter(), desc.getSetter(),
            AuditType.get(auditMeta.getAuditType()), null, null);
    }
}
