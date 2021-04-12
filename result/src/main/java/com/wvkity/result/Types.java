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
package com.wvkity.result;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 类型工具
 * @author wvkity
 * @created 2021-02-16
 * @since 1.0.0
 */
public final class Types {

    private Types() {
    }

    private static final Pattern PATTERN_NUMBER_WITH_TRAILING_ZEROS = Pattern.compile("\\.0*$");
    private static final Pattern PATTERN_INTEGER = Pattern.compile("^([\\-+])?(0|[1-9]\\d*)");

    public static String toString(final Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof String) {
            return (String) value;
        }
        return value.toString();
    }

    public static Character toChar(final Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Character) {
            return (Character) value;
        }
        if (value instanceof String) {
            final String str = ((String) value).trim();
            final int len = str.length();
            if (len == 0) {
                return null;
            }
            if (len != 1) {
                throw new IllegalArgumentException("can not cast to char, value: " + value);
            }
            return str.charAt(0);
        }
        throw new IllegalArgumentException("can not cast to char, value: " + value);
    }

    public static Short toShort(final Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Short) {
            return (Short) value;
        }
        if (value instanceof BigDecimal) {
            return shortValue((BigDecimal) value);
        }
        if (value instanceof Number) {
            return ((Number) value).shortValue();
        }
        if (value instanceof String) {
            final String str = ((String) value).trim();
            if (str.length() == 0 || "null".equalsIgnoreCase(str)) {
                return 0;
            }
            return Short.parseShort(str);
        }
        throw new IllegalArgumentException("can not cast to short, value: " + value);
    }

    public static Integer toInt(final Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Integer) {
            return (Integer) value;
        }
        if (value instanceof BigDecimal) {
            return intValue((BigDecimal) value);
        }
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        if (value instanceof String) {
            final String str = trim(value);
            if (str == null) {
                return null;
            }
            if (PATTERN_INTEGER.matcher(str).matches()) {
                return Integer.parseInt(str);
            }
        }
        if (value instanceof Boolean) {
            return (Boolean) value ? 1 : 0;
        }
        throw new IllegalArgumentException("can not cast to integer, value: " + value);
    }

    public static Long toLong(final Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Long) {
            return (Long) value;
        }
        if (value instanceof BigDecimal) {
            return longValue((BigDecimal) value);
        }
        if (value instanceof Number) {
            return ((Number) value).longValue();
        }
        if (value instanceof String) {
            final String str = trim(value);
            if (str == null) {
                return null;
            }
            if (PATTERN_INTEGER.matcher(str).matches()) {
                return Long.parseLong(str);
            }
        }
        throw new IllegalArgumentException("can not cast to long, value: " + value);
    }

    public static Float toFloat(final Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Float) {
            return (Float) value;
        }
        if (value instanceof Number) {
            return ((Number) value).floatValue();
        }
        if (value instanceof String) {
            final String str = trim(value);
            if (str == null) {
                return null;
            }
            if (PATTERN_INTEGER.matcher(str).matches()) {
                return Float.parseFloat(str);
            }
        }
        throw new IllegalArgumentException("can not cast to float, value: " + value);
    }

    public static Double toDouble(final Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Double) {
            return (Double) value;
        }
        if (value instanceof Number) {
            return ((Number) value).doubleValue();
        }
        if (value instanceof String) {
            final String str = trim(value);
            if (str == null) {
                return null;
            }
            if (PATTERN_INTEGER.matcher(str).matches()) {
                return Double.parseDouble(str);
            }
        }
        throw new IllegalArgumentException("can not cast to double, value: " + value);
    }

    public static Boolean toBoolean(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Boolean) {
            return (Boolean) value;
        }

        if (value instanceof BigDecimal) {
            return intValue((BigDecimal) value) == 1;
        }

        if (value instanceof Number) {
            return ((Number) value).intValue() == 1;
        }

        if (value instanceof String) {
            final String str = (String) value;
            if (str.length() == 0 || "null".equalsIgnoreCase(str)) {
                return null;
            }
            if ("true".equalsIgnoreCase(str) || "1".equals(str)) {
                return Boolean.TRUE;
            }
            if ("false".equalsIgnoreCase(str) || "0".equals(str)) {
                return Boolean.FALSE;
            }
            if ("Y".equalsIgnoreCase(str) || "T".equals(str)) {
                return Boolean.TRUE;
            }
            if ("F".equalsIgnoreCase(str) || "N".equals(str)) {
                return Boolean.FALSE;
            }
        }
        throw new IllegalArgumentException("can not cast to boolean, value : " + value);
    }

    public static short shortValue(final BigDecimal decimal) {
        if (decimal == null) {
            return 0;
        }
        return checkScale(decimal.scale()) ? decimal.shortValue() : decimal.shortValueExact();
    }

    public static int intValue(final BigDecimal decimal) {
        if (decimal == null) {
            return 0;
        }
        return checkScale(decimal.scale()) ? decimal.intValue() : decimal.intValueExact();
    }

    public static long longValue(final BigDecimal decimal) {
        if (decimal == null) {
            return 0;
        }
        return checkScale(decimal.scale()) ? decimal.longValue() : decimal.longValueExact();
    }

    private static boolean checkScale(final int scale) {
        return scale >= -100 && scale <= 100;
    }

    private static String trim(final Object value) {
        String str = ((String) value).trim();
        if (str.length() == 0 || "null".equalsIgnoreCase(str)) {
            return null;
        }
        if (str.contains(",")) {
            str = str.replaceAll(",", "");
        }
        final Matcher matcher = PATTERN_NUMBER_WITH_TRAILING_ZEROS.matcher(str);
        if (matcher.find()) {
            str = matcher.replaceAll("");
        }
        return str;
    }
}
