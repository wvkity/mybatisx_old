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
package com.wvkity.mybatis.core.criteria.sql;

import com.wvkity.mybatis.basic.constant.Constants;
import com.wvkity.mybatis.basic.exception.MyBatisException;
import com.wvkity.mybatis.basic.utils.Objects;
import com.wvkity.mybatis.core.criteria.ExtCriteria;
import com.wvkity.mybatis.core.support.manager.StandardFragmentManager;
import com.wvkity.mybatis.support.criteria.Criteria;

import java.util.regex.Pattern;

/**
 * 抽象SQL管理器
 * @author wvkity
 * @created 2021-06-28
 * @since 1.0.0
 */
public abstract class AbstractSqlManager implements SqlManager {

    protected final ExtCriteria<?> criteria;
    protected final StandardFragmentManager<? extends Criteria<?>> fragmentManager;
    /**
     * AND、OR运算符正则字符串
     */
    protected static final String DEF_REGEX_AND_OR_STR = "^(?i)(\\s*and\\s+|\\s*or\\s+)(.*)";
    /**
     * AND、OR运算符正则
     */
    protected static final Pattern DEF_PATTERN_AND_OR = Pattern.compile(DEF_REGEX_AND_OR_STR, Pattern.CASE_INSENSITIVE);

    public AbstractSqlManager(ExtCriteria<?> criteria, StandardFragmentManager<? extends Criteria<?>> fragmentManager) {
        this.criteria = criteria;
        this.fragmentManager = fragmentManager;
    }

    /**
     * 禁止调用方法
     */
    protected void invokeThrow() {
        throw new MyBatisException("The current instance object cannot call this method.");
    }

    @Override
    public String getWhereSegment(boolean self, boolean appendWhere, String groupByReplacement) {
        final String condition = this.fragmentManager.getSegment(groupByReplacement);
        if (Objects.isNotBlank(condition)) {
            if (DEF_PATTERN_AND_OR.matcher(condition).matches()) {
                return (appendWhere ? "WHERE " : Constants.EMPTY) + condition.replaceFirst(DEF_REGEX_AND_OR_STR, "$2");
            }
            return (appendWhere ? "WHERE " : Constants.EMPTY) + condition;
        }
        return Constants.EMPTY;
    }

    @Override
    public String getSelectSegment(boolean self) {
        this.invokeThrow();
        return null;
    }

    @Override
    public String getSelectString() {
        this.invokeThrow();
        return null;
    }

    @Override
    public String getGroupSegment() {
        this.invokeThrow();
        return null;
    }

    @Override
    public String getUpdateSegment() {
        this.invokeThrow();
        return null;
    }

}
