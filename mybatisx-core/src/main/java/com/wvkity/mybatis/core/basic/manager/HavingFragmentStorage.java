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
package com.wvkity.mybatis.core.basic.manager;

import com.wvkity.mybatis.basic.constant.Constants;
import com.wvkity.mybatis.core.basic.having.Having;
import com.wvkity.mybatis.support.fragment.AbstractFragmentList;

import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 分组筛选片段存储器
 * @author wvkity
 * @created 2021-04-22
 * @since 1.0.0
 */
public class HavingFragmentStorage extends AbstractFragmentList<Having> {

    private static final long serialVersionUID = 7993405736198850559L;

    private static final String DEF_REGEX_AND_OR_STR = "^(?i)(\\s*and\\s+|\\s*or\\s+)(.*)";
    private static final Pattern DEF_PATTERN_AND_OR = Pattern.compile(DEF_REGEX_AND_OR_STR, Pattern.CASE_INSENSITIVE);

    @Override
    public String getSegment() {
        if (!this.isEmpty()) {
            String segment = this.fragments.stream().map(Having::getSegment)
                .collect(Collectors.joining(Constants.SPACE)).trim();
            if (DEF_PATTERN_AND_OR.matcher(segment).matches()) {
                return " HAVING " + segment.replaceFirst(DEF_REGEX_AND_OR_STR, "$2");
            }
            return " HAVING " + segment;
        }
        return Constants.EMPTY;
    }
}
