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
package io.github.mybatisx.spring.boot.lock.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Properties;
import java.util.Set;

/**
 * 乐观锁配置
 * @author wvkity
 * @created 2021-07-29
 * @since 1.0.0
 */
@ConfigurationProperties(prefix = MyBatisOptimisticLockProperties.CFG_PREFIX)
public class MyBatisOptimisticLockProperties {

    public static final String CFG_PREFIX = "github.mybatisx.plugin.lock";
    /**
     * 是否启用
     */
    private boolean enable = true;
    /**
     * 更新成功后自动更新到目标对象
     */
    private boolean autoOverrideTarget;
    /**
     * 乐观锁方法
     */
    private Set<String> methods;
    /**
     * 其他配置项
     */
    private Properties properties;

    public MyBatisOptimisticLockProperties() {
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public boolean isAutoOverrideTarget() {
        return autoOverrideTarget;
    }

    public void setAutoOverrideTarget(boolean autoOverrideTarget) {
        this.autoOverrideTarget = autoOverrideTarget;
    }

    public Set<String> getMethods() {
        return methods;
    }

    public void setMethods(Set<String> methods) {
        this.methods = methods;
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }
}
