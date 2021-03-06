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
package io.github.mybatisx.core.condition;

import io.github.mybatisx.Objects;
import io.github.mybatisx.constant.Constants;
import io.github.mybatisx.support.constant.Slot;
import io.github.mybatisx.support.fragment.Fragment;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 嵌套条件
 * @author wvkity
 * @created 2021-05-17
 * @since 1.0.0
 */
public class NestedCondition implements Criterion {

    private static final long serialVersionUID = -7465372686832568516L;
    private static final String AND_OR_REGEX = "^(?i)(\\s*and\\s+|\\s*or\\s+)(.*)";
    private static final Pattern AND_OR_PATTERN = Pattern.compile(AND_OR_REGEX, Pattern.CASE_INSENSITIVE);
    /**
     * 是否拼接not
     */
    private final boolean not;
    /**
     * 运算符
     */
    private final Slot slot;
    /**
     * 条件表达式列表
     */
    private final List<Criterion> conditions;

    public NestedCondition(boolean not, Slot slot, List<Criterion> conditions) {
        this.not = not;
        this.slot = slot;
        this.conditions = conditions;
    }

    @Override
    public String getSegment() {
        final StringBuilder builder = new StringBuilder(100);
        if (Objects.nonNull(this.slot)) {
            builder.append(this.slot.getSegment());
        }
        if (this.not) {
            builder.append(Constants.SPACE).append("NOT");
        }
        builder.append(Constants.SPACE).append(Constants.BRACKET_OPEN);
        final String segment = this.conditions.stream().map(Fragment::getSegment).filter(Objects::isNotBlank)
            .collect(Collectors.joining(Constants.SPACE));
        if (AND_OR_PATTERN.matcher(segment).matches()) {
            builder.append(segment.replaceFirst(AND_OR_REGEX, "$2"));
        } else {
            builder.append(segment);
        }
        builder.append(Constants.BRACKET_CLOSE);
        return builder.toString();
    }
}
