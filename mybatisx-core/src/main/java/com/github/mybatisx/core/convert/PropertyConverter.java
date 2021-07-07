package com.github.mybatisx.core.convert;

import com.github.mybatisx.basic.metadata.Column;
import com.github.mybatisx.core.property.Property;

/**
 * 属性转换器
 * @author wvkity
 * @created 2021-05-21
 * @since 1.0.0
 */
public interface PropertyConverter extends Converter<Property<?, ?>, Column> {

    /**
     * 属性转{@link Column}
     * @param property 属性
     * @return {@link Column}
     */
    Column convert(final String property);

    /**
     * 属性转{@link Column}
     * @param column 字段名
     * @return {@link Column}
     */
    Column convertOfOrg(final String column);

    /**
     * Lambda属性转字符串属性
     * @param property {@link Property}
     * @return 字符串属性
     */
    String toProperty(final Property<?, ?> property);

}
