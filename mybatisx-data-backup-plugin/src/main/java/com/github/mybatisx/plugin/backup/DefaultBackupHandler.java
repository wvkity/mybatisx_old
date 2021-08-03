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
package com.github.mybatisx.plugin.backup;

import com.github.mybatisx.Objects;
import com.github.mybatisx.backup.BackupPolicy;
import com.github.mybatisx.backup.event.publisher.BackupEventPublisher;
import com.github.mybatisx.cache.LocalCache;
import com.github.mybatisx.cache.LocalCacheFactory;
import com.github.mybatisx.plugin.backup.process.QueryProcessor;
import org.apache.ibatis.mapping.MappedStatement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.Set;

/**
 * 默认数据备份拦截处理器
 * @author wvkity
 * @created 2021-07-17
 * @since 1.0.0
 */
public class DefaultBackupHandler extends AbstractBackupHandler {

    private static final Logger log = LoggerFactory.getLogger(DefaultBackupHandler.class);

    public DefaultBackupHandler(boolean nonConditionFilter, QueryProcessor queryProcessor,
                                Set<BackupPolicy> filterPolicies, BackupEventPublisher eventPublisher) {
        this(nonConditionFilter, queryProcessor, filterPolicies, null, eventPublisher);
    }

    public DefaultBackupHandler(boolean nonConditionFilter, QueryProcessor queryProcessor,
                                Set<BackupPolicy> filterPolicies, LocalCache<String, MappedStatement> localCache,
                                BackupEventPublisher eventPublisher) {
        this.nonConditionFilter = nonConditionFilter;
        this.queryProcessor = queryProcessor;
        this.filterPolicies = filterPolicies;
        this.localCache = localCache;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void setProperties(Properties properties) {
        super.setProperties(properties);
        if (Objects.isNull(this.localCache)) {
            this.localCache = LocalCacheFactory.create(properties.getProperty(PROP_KEY_CACHE_CLASS),
                properties, properties.getProperty(PROP_KEY_CACHE_CFG_PREFIX));
        }
    }
}
