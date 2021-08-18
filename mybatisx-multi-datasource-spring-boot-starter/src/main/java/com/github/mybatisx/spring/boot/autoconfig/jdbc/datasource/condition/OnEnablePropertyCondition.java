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
package com.github.mybatisx.spring.boot.autoconfig.jdbc.datasource.condition;

import com.github.mybatisx.Objects;
import com.github.mybatisx.constant.Constants;
import org.springframework.boot.autoconfigure.condition.ConditionMessage;
import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.PropertyResolver;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * enable属性条件
 * @author wvkity
 * @created 2021-08-18
 * @since 1.0.0
 */
public class OnEnablePropertyCondition extends SpringBootCondition {

    private static final String DOT = Constants.DOT;

    @Override
    public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata) {
        List<AnnotationAttributes> allAnnotationAttributes = annotationAttributesFromMultiValueMap(
            metadata.getAllAnnotationAttributes(ConditionalOnEnableProperty.class.getName()));
        List<ConditionMessage> noMatch = new ArrayList<>();
        List<ConditionMessage> match = new ArrayList<>();
        for (AnnotationAttributes annotationAttributes : allAnnotationAttributes) {
            ConditionOutcome outcome = determineOutcome(annotationAttributes, context.getEnvironment());
            (outcome.isMatch() ? match : noMatch).add(outcome.getConditionMessage());
        }
        if (!noMatch.isEmpty()) {
            return ConditionOutcome.noMatch(ConditionMessage.of(noMatch));
        }
        return ConditionOutcome.match(ConditionMessage.of(match));
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

    private ConditionOutcome determineOutcome(AnnotationAttributes annotationAttributes, PropertyResolver resolver) {
        Spec spec = new Spec(annotationAttributes);
        List<String> missingProperties = new ArrayList<>();
        List<String> nonMatchingProperties = new ArrayList<>();
        spec.collectProperties(resolver, missingProperties, nonMatchingProperties);
        if (!missingProperties.isEmpty()) {
            return ConditionOutcome.noMatch(ConditionMessage.forCondition(ConditionalOnEnableProperty.class, spec)
                .didNotFind("property", "properties").items(ConditionMessage.Style.QUOTE, missingProperties));
        }
        if (!nonMatchingProperties.isEmpty()) {
            return ConditionOutcome.noMatch(ConditionMessage.forCondition(ConditionalOnEnableProperty.class, spec)
                .found("different value in property", "different value in properties")
                .items(ConditionMessage.Style.QUOTE, nonMatchingProperties));
        }
        return ConditionOutcome
            .match(ConditionMessage.forCondition(ConditionalOnEnableProperty.class, spec).because("matched"));
    }

    private static class Spec {

        private final String name = "enable";
        private final String prefix;
        private final String havingValue;
        private final boolean matchIfMissing;

        Spec(AnnotationAttributes annotationAttributes) {
            String prefix = annotationAttributes.getString("prefix").trim();
            if (StringUtils.hasText(prefix) && !prefix.endsWith(DOT)) {
                prefix = prefix + DOT;
            }
            this.prefix = prefix;
            this.havingValue = annotationAttributes.getString("havingValue");
            this.matchIfMissing = annotationAttributes.getBoolean("matchIfMissing");
        }

        private void collectProperties(PropertyResolver resolver, List<String> missing, List<String> nonMatching) {
            String key = this.prefix + name;
            if (resolver.containsProperty(key)) {
                if (!isMatch(resolver.getProperty(key), this.havingValue)) {
                    nonMatching.add(name);
                }
            } else {
                if (!this.matchIfMissing) {
                    missing.add(name);
                }
            }
        }

        private boolean isMatch(String value, String requiredValue) {
            if (StringUtils.hasLength(requiredValue)) {
                return requiredValue.equalsIgnoreCase(value);
            }
            return !"false".equalsIgnoreCase(value);
        }

        @Override
        public String toString() {
            return "(" +
                this.prefix +
                this.name +
                ")";
        }

    }
}
