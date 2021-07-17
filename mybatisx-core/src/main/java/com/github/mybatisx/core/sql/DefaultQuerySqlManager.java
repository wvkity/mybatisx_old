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
package com.github.mybatisx.core.sql;

import com.github.mybatisx.Objects;
import com.github.mybatisx.RegexMatcher;
import com.github.mybatisx.constant.Constants;
import com.github.mybatisx.core.criteria.AbstractExtCriteria;
import com.github.mybatisx.core.criteria.ExtCriteria;
import com.github.mybatisx.core.support.manager.StandardFragmentManager;
import com.github.mybatisx.support.criteria.Criteria;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 默认查询类型SQL管理器
 * @author wvkity
 * @created 2021-06-28
 * @since 1.0.0
 */
public class DefaultQuerySqlManager extends AbstractSqlManager {

    /**
     * SQL优化禁止去除order by子句注释
     */
    protected static final String KEEP_ORDER_BY_COMMENT = "/*keep orderby*/";
    protected final ExtCriteria<?> refQuery;
    protected final Set<ExtCriteria<?>> foreignSet;

    public DefaultQuerySqlManager(ExtCriteria<?> criteria, ExtCriteria<?> refQuery, Set<ExtCriteria<?>> foreignSet,
                                  StandardFragmentManager<? extends Criteria<?>> fragmentManager) {
        super(criteria, fragmentManager);
        this.refQuery = refQuery;
        this.foreignSet = foreignSet;
    }

    @Override
    public String getSelectSegment(boolean self) {
        final boolean hasRef = Objects.nonNull(this.refQuery);
        final boolean notHasSel = !this.criteria.hasSelect();
        final boolean isLoadAndReturn = (hasRef && notHasSel) || !self;
        if (isLoadAndReturn) {
            this.criteria.fetchSelects();
            return this.getSelectString();
        }
        final String ss = this.getSelectString();
        if (Objects.isNotEmpty(this.foreignSet)) {
            final List<String> it = new ArrayList<>();
            if (Objects.isNotBlank(ss)) {
                it.add(ss);
            }
            if (Objects.isNotEmpty(this.foreignSet)) {
                for (ExtCriteria<?> ec : this.foreignSet) {
                    if (ec.hasSelect() || ec.isFetch()) {
                        final String fss = ec.getSelectSegment(false);
                        if (Objects.isNotBlank(fss)) {
                            it.add(fss);
                        }
                    }
                }
            }
            if (it.size() == 1) {
                return it.get(0);
            }
            return String.join(Constants.COMMA_SPACE, it);
        }
        return ss;
    }

    @Override
    public String getSelectString() {
        return this.fragmentManager.getSelectString();
    }

    @Override
    public String getGroupSegment() {
        if (Objects.isNotEmpty(this.foreignSet)) {
            final List<String> it = new ArrayList<>(this.foreignSet.size() + 1);
            final String ss = this.fragmentManager.getSelectString(false);
            if (Objects.isNotBlank(ss)) {
                it.add(ss.trim());
            }
            for (ExtCriteria<?> ec : this.foreignSet) {
                if (ec.hasSelect() || ec.isFetch()) {
                    final String fss = ec.getGroupSegment();
                    if (Objects.isNotBlank(fss)) {
                        it.add(fss.trim());
                    }
                }
            }
            return it.size() == 1 ? it.get(0) : String.join(Constants.COMMA_SPACE, it);
        }
        return this.fragmentManager.getSelectString(false);
    }

    @Override
    public String getWhereSegment() {
        return this.getWhereSegment(true, true, this.criteria.isGroupAll() ? this.getGroupSegment() : null);
    }

    @Override
    public String getWhereSegment(boolean self, boolean appendWhere, String groupByReplacement) {
        return this.mergeWhereSegment(super.getWhereSegment(self, appendWhere, groupByReplacement));
    }

    /**
     * 合并条件片段
     * @param condition 条件片段
     * @return 完整条件
     */
    protected String mergeWhereSegment(final String condition) {
        if (Objects.isNotEmpty(this.foreignSet)) {
            final List<String> it = new ArrayList<>(this.foreignSet.size() + 1);
            for (ExtCriteria<?> ec : this.foreignSet) {
                final AbstractExtCriteria<?> aec = ec.transfer();
                final String fcc = aec.getWhereSegment(false, false, null);
                if (Objects.isNotBlank(fcc)) {
                    final StringBuilder csb = new StringBuilder(80);
                    csb.append(aec.getJoin().getSegment().trim()).append(Constants.SPACE);
                    csb.append(aec.getTableName().trim()).append(" ON ");
                    if (RegexMatcher.startWithAndOr(fcc)) {
                        csb.append(RegexMatcher.startWithAndOrRemove(fcc.trim()));
                    } else {
                        csb.append(fcc.trim());
                    }
                    it.add(csb.toString());
                }
            }
            if (Objects.isNotEmpty(it)) {
                it.add(condition);
                return String.join(Constants.SPACE, it);
            }
        }
        return condition;
    }

    @Override
    public String intactString() {
        final StringBuilder it = new StringBuilder(150);
        it.append("SELECT");
        if (this.criteria.isDistinct()) {
            it.append(" DISTINCT ");
        }
        it.append(Constants.SPACE).append(this.getSelectSegment().trim());
        it.append(" FROM ").append(this.criteria.transfer().getTableName().trim());
        final String whereStr = this.getWhereSegment();
        if (Objects.isNotBlank(whereStr)) {
            it.append(Constants.SPACE).append(whereStr.trim());
            if (this.criteria.isKeepOrderBy() && this.fragmentManager.hasOrderBy()) {
                it.append(Constants.SPACE).append(KEEP_ORDER_BY_COMMENT).append(Constants.SPACE);
            }
        }
        return it.toString().trim();
    }
}
