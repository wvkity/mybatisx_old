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
package io.github.mybatisx.spring.boot.autoconfig.jdbc.datasource.condition;

import io.github.mybatisx.Objects;
import io.github.mybatisx.constant.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.util.Assert;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * 属性值匹配任意一次条件
 * @author wvkity
 * @created 2021-08-07
 * @since 1.0.0
 */
public class OnPropertyAnyMatchesCondition implements Condition {

    private static final Logger log = LoggerFactory.getLogger(OnPropertyAnyMatchesCondition.class);
    private static final String DOT = Constants.DOT;

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        List<AnnotationAttributes> allAnnotationAttributes = annotationAttributesFromMultiValueMap(
            metadata.getAllAnnotationAttributes(ConditionalOnPropertyAnyMatches.class.getName()));
        return new Spec(allAnnotationAttributes.get(0)).isMatch(context.getEnvironment());
    }

    private List<AnnotationAttributes> annotationAttributesFromMultiValueMap(
        MultiValueMap<String, Object> multiValueMap) {
        List<Map<String, Object>> maps = new ArrayList<>();
        if (Objects.isNotEmpty(multiValueMap)) {
            multiValueMap.forEach((key, value) -> {
                for (int i = 0; i < value.size(); i++) {
                    Map<String, Object> map;
                    if (i < maps.size()) {
                        map = maps.get(i);
                    } else {
                        map = new HashMap<>(16);
                        maps.add(map);
                    }
                    map.put(key, value.get(i));
                }
            });
        }
        List<AnnotationAttributes> annotationAttributes = new ArrayList<>(maps.size());
        for (Map<String, Object> map : maps) {
            annotationAttributes.add(AnnotationAttributes.fromMap(map));
        }
        return annotationAttributes;
    }

    private static class Spec {

        private final String prefix;
        private final String[] names;
        private final boolean deep;
        private final String refer;
        private final boolean regex;
        private final String havingValue;
        private final boolean matchIfMissing;
        private final Set<String> configKeys = new HashSet<>();

        Spec(AnnotationAttributes annotationAttributes) {
            String prefix = annotationAttributes.getString("prefix").trim();
            if (StringUtils.hasText(prefix) && !prefix.endsWith(DOT)) {
                prefix = prefix + DOT;
            }
            this.prefix = prefix;
            this.names = getNames(annotationAttributes);
            this.deep = annotationAttributes.getBoolean("deep");
            this.refer = annotationAttributes.getString("refer");
            this.regex = annotationAttributes.getBoolean("regex");
            this.havingValue = annotationAttributes.getString("havingValue");
            this.matchIfMissing = annotationAttributes.getBoolean("matchIfMissing");
        }

        private String[] getNames(Map<String, Object> annotationAttributes) {
            String[] value = (String[]) annotationAttributes.get("value");
            String[] name = (String[]) annotationAttributes.get("name");
            Assert.state(value.length > 0 || name.length > 0,
                "The name or value attribute of @ConditionalOnPropertyAnyMatches must be specified");
            Assert.state(value.length == 0 || name.length == 0,
                "The name and value attributes of @ConditionalOnPropertyAnyMatches are exclusive");
            return (value.length > 0) ? value : name;
        }

        boolean isMatch(Environment environment) {
            this.isMatch(environment, null);
            if (this.deep && StringUtils.hasText(this.refer)) {
                if (this.regex) {
                    final Set<String> keys = this.getKeys(environment);
                    final String regexPrefix = this.prefix.replaceAll("\\.", "\\\\.");
                    final String regexSuffix = "\\.";
                    if (Objects.isNotEmpty(keys)) {
                        for (String key : keys) {
                            for (String name : this.names) {
                                final String reg = regexPrefix + this.refer + regexSuffix + name;
                                final Pattern pattern = Pattern.compile(reg, Pattern.CASE_INSENSITIVE);
                                if (pattern.matcher(key).matches()) {
                                    if (this.isMatch(key, environment)) {
                                        return true;
                                    }
                                }
                            }
                        }
                    }
                } else if (this.isMatch(environment, this.refer)) {
                    return true;
                }
            }
            return this.matchIfMissing;
        }

        private boolean isMatch(Environment environment, String refer) {
            for (String name : this.names) {
                String key = this.prefix + (Objects.isNotBlank(refer) ? (refer + DOT) : "") + name;
                return this.isMatch(key, environment);
            }
            return false;
        }

        private boolean isMatch(String key, Environment environment) {
            return environment.containsProperty(key) && this.isMatch(environment.getProperty(key),
                this.havingValue);
        }

        private Set<String> getKeys(Environment environment) {
            if (Objects.isNotEmpty(this.configKeys)) {
                return this.configKeys;
            }
            final AbstractEnvironment evn = (AbstractEnvironment) environment;
            final MutablePropertySources sources = evn.getPropertySources();
            for (PropertySource<?> it : sources) {
                if (it instanceof MapPropertySource) {
                    final MapPropertySource source = (MapPropertySource) it;
                    final Set<String> keys = source.getSource().keySet();
                    for (String key : keys) {
                        if (key.startsWith(this.prefix)) {
                            this.configKeys.add(key);
                        }
                    }
                }
            }
            return this.configKeys;
        }

        private boolean isMatch(String value, String requiredValue) {
            if (StringUtils.hasLength(requiredValue)) {
                return requiredValue.equalsIgnoreCase(value);
            }
            return !"false".equalsIgnoreCase(value);
        }

        public String getPrefix() {
            return prefix;
        }

        @Override
        public String toString() {
            StringBuilder result = new StringBuilder();
            result.append("(");
            result.append(this.prefix);
            if (this.names.length == 1) {
                result.append(this.names[0]);
            } else {
                result.append("[");
                result.append(StringUtils.arrayToCommaDelimitedString(this.names));
                result.append("]");
            }
            result.append(")");
            return result.toString();
        }

    }
}
