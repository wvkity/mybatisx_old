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
package com.github.mybatisx.spring.boot.autoconfig.jdbc.datasource;

import com.github.mybatisx.Objects;
import com.github.mybatisx.constant.Constants;
import com.github.mybatisx.immutable.ImmutableSet;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * 多数据源环境配置key
 * @author wvkity
 * @created 2021-08-10
 * @since 1.0.0
 */
public class MultiDataSourceEnvKey {

    private static final String CFG_PREFIX = MultiDataSourceProperties.CFG_PREFIX;
    private static final String ESCAPE_BACK = "\\";
    private final String prefix;
    private final Environment environment;
    private final Set<String> keys = new HashSet<>();

    public MultiDataSourceEnvKey(Environment environment) {
        this(CFG_PREFIX, environment);
    }

    public MultiDataSourceEnvKey(String prefix, Environment environment) {
        this.prefix = prefix;
        this.environment = environment;
    }

    public void parse() {
        if (Objects.nonNull(environment) && Objects.isEmpty(this.keys)) {
            final AbstractEnvironment evn = (AbstractEnvironment) environment;
            final MutablePropertySources sources = evn.getPropertySources();
            for (PropertySource<?> it : sources) {
                if (it instanceof MapPropertySource) {
                    final MapPropertySource source = (MapPropertySource) it;
                    final Set<String> keys = source.getSource().keySet();
                    for (String key : keys) {
                        if (key.startsWith(this.prefix)) {
                            this.keys.add(key);
                        }
                    }
                }
            }
        }
    }

    public boolean contains(final String content) {
        return this.contains(content, true);
    }

    public boolean contains(final String content, final boolean regex) {
        if (Objects.isNotBlank(content)) {
            return this.containsKey(this.prefix + Constants.DOT + content, regex);
        }
        return false;
    }

    public boolean contains(final Pattern regex) {
        if (Objects.nonNull(regex) && Objects.isNotEmpty(this.keys)) {
            for (String it : keys) {
                if (regex.matcher(it).matches()) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean containsKey(final String key) {
        return this.containsKey(key, true);
    }

    public boolean containsKey(final String key, final boolean regex) {
        if (Objects.isNotBlank(key) && Objects.isNotEmpty(this.keys)) {
            if (regex) {
                String realKey;
                if (key.contains(ESCAPE_BACK)) {
                    realKey = key;
                } else {
                    realKey = key.replaceAll("\\.", "\\\\.");
                    realKey = realKey.replaceAll("\\$", "\\\\$");
                    realKey = realKey.replaceAll("\\{", "\\\\{");
                    realKey = realKey.replaceAll("}", "\\\\}");
                    realKey = realKey.replaceAll("\\[", "\\\\[");
                    realKey = realKey.replaceAll("]", "\\\\]");
                }
                realKey = "^" + realKey + "(.*?)$";
                return this.contains(Pattern.compile(realKey, Pattern.CASE_INSENSITIVE));
            }
            return this.keys.contains(key);
        }
        return false;
    }

    public Environment getEnvironment() {
        return environment;
    }

    public Set<String> getKeys() {
        if (Objects.isNotEmpty(this.keys)) {
            return ImmutableSet.of(this.keys);
        }
        return ImmutableSet.of();
    }
}
