package com.wvkity.mybatis.reflection.wrapper;

import com.wvkity.mybatis.annotation.NamingStrategy;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.wrapper.MapWrapper;

import java.util.Map;

/**
 * 列名下划线转驼峰
 * @author wvkity
 */
public class MyBatisMapWrapper extends MapWrapper {

    public MyBatisMapWrapper(MetaObject metaObject, Map<String, Object> map) {
        super(metaObject, map);
    }

    @Override
    public String findProperty(String name, boolean useCamelCaseMapping) {
        if (useCamelCaseMapping) {
            return NamingStrategy.UPPER_UNDERSCORE.to(NamingStrategy.LOWER_CAMEL, name);
        }
        return name;
    }
}
