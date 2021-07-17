package com.github.mybatisx.reflection.wrapper;

import com.github.mybatisx.annotation.NamingPolicy;
import com.github.mybatisx.constant.Constants;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.wrapper.MapWrapper;

import java.util.Arrays;
import java.util.Map;

/**
 * 列名下划线转驼峰
 * @author wvkity
 */
public class MyBatisMapWrapper extends MapWrapper {

    static final String DEF_SPLIT_SEPARATOR = "\\.";

    public MyBatisMapWrapper(MetaObject metaObject, Map<String, Object> map) {
        super(metaObject, map);
    }

    @Override
    public String findProperty(String name, boolean useCamelCaseMapping) {
        final String realName = this.handleRevSingleQuotes(name);
        if (useCamelCaseMapping) {
            return handleLowerCamel(realName);
        }
        return realName;
    }

    String handleRevSingleQuotes(final String name) {
        if (name.contains(Constants.REV_SINGLE_QUOTES)) {
            return name.replaceAll(Constants.REV_SINGLE_QUOTES, "");
        }
        return name;
    }

    String handleLowerCamel(final String name) {
        if (name.contains(Constants.DOT)) {
            final String[] it = name.split(DEF_SPLIT_SEPARATOR);
            final int last = it.length - 1;
            it[last] = NamingPolicy.UPPER_UNDERSCORE.to(NamingPolicy.LOWER_CAMEL, it[last]);
            return String.join(Constants.DOT, Arrays.asList(it));
        }
        return NamingPolicy.UPPER_UNDERSCORE.to(NamingPolicy.LOWER_CAMEL, name);
    }
}
