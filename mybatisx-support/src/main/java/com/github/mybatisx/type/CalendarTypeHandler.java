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
package com.github.mybatisx.type;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;

/**
 * {@link java.util.Calendar}时间类型处理器
 * @author wvkity
 * @created 2021-03-11
 * @since 1.0.0
 */
public class CalendarTypeHandler extends BaseTypeHandler<Calendar> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Calendar parameter, JdbcType jdbcType) throws SQLException {
        ps.setTimestamp(i, Timestamp.from(parameter.toInstant()));
    }

    @Override
    public Calendar getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return getCalendar(rs.getTimestamp(columnName));
    }

    @Override
    public Calendar getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return getCalendar(rs.getTimestamp(columnIndex));
    }

    @Override
    public Calendar getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return getCalendar(cs.getTimestamp(columnIndex));
    }

    private static Calendar getCalendar(final Timestamp timestamp) {
        if (timestamp != null) {
            final Calendar calendar = Calendar.getInstance();
            calendar.setTime(timestamp);
            return calendar;
        }
        return null;
    }
}
