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
package io.github.mybatisx.backup.meta;

import io.github.mybatisx.CommandType;

import java.util.List;

/**
 * 备份元数据
 * @author wvkity
 * @created 2021-07-18
 * @since 1.0.0
 */
public class DefaultBackupMetadata implements BackupMetadata {

    private static final long serialVersionUID = -3820277056986180059L;
    /**
     * 唯一标识(MappedStatement.id)
     */
    private final String uniqueCode;
    /**
     * 原数据ID属性
     */
    private final String orgIdProp;
    /**
     * 备份数据ID属性
     */
    private final String targetIdProp;
    /**
     * 数据备份处理bean名
     */
    private final String processBean;
    /**
     * 数据备份处理方法名
     */
    private final String processMethod;
    /**
     * 原始参数
     */
    private final Object orgParam;
    /**
     * 真实参数
     */
    private final Object realParam;
    /**
     * 备份数据类型
     */
    private final Class<?> target;
    /**
     * 备份数据列表
     */
    private final List<Object> sources;
    /**
     * 参数类型列表
     */
    private final Class<?>[] args;
    /**
     * 当前执行操作类型
     */
    private final CommandType commandType;

    public DefaultBackupMetadata(String uniqueCode, String orgIdProp, String targetIdProp, String processBean,
                                 String processMethod, Object orgParam, Object realParam, Class<?> target,
                                 List<Object> sources, Class<?>[] args, CommandType commandType) {
        this.uniqueCode = uniqueCode;
        this.orgIdProp = orgIdProp;
        this.targetIdProp = targetIdProp;
        this.processBean = processBean;
        this.processMethod = processMethod;
        this.orgParam = orgParam;
        this.realParam = realParam;
        this.target = target;
        this.sources = sources;
        this.args = args;
        this.commandType = commandType;
    }

    public static final class Builder {
        private String uniqueCode;
        private String orgIdProp;
        private String targetIdProp;
        private String processBean;
        private String processMethod;
        private Object orgParam;
        private Object realParam;
        private Class<?> target;
        private List<Object> sources;
        private Class<?>[] args;
        private CommandType commandType;

        private Builder() {
        }

        public DefaultBackupMetadata build() {
            return new DefaultBackupMetadata(this.uniqueCode, this.orgIdProp, this.targetIdProp, this.processBean,
                this.processMethod, this.orgParam, this.realParam, this.target, this.sources, this.args,
                this.commandType);
        }

        public static Builder create() {
            return new Builder();
        }

        public Builder uniqueCode(String uniqueCode) {
            this.uniqueCode = uniqueCode;
            return this;
        }

        public Builder orgIdProp(String orgIdProp) {
            this.orgIdProp = orgIdProp;
            return this;
        }

        public Builder targetIdProp(String targetIdProp) {
            this.targetIdProp = targetIdProp;
            return this;
        }

        public Builder processBean(String processBean) {
            this.processBean = processBean;
            return this;
        }

        public Builder processMethod(String processMethod) {
            this.processMethod = processMethod;
            return this;
        }

        public Builder orgParam(Object orgParam) {
            this.orgParam = orgParam;
            return this;
        }

        public Builder realParam(Object realParam) {
            this.realParam = realParam;
            return this;
        }

        public Builder target(Class<?> target) {
            this.target = target;
            return this;
        }

        public Builder sources(List<Object> sources) {
            this.sources = sources;
            return this;
        }

        public Builder args(Class<?>[] args) {
            this.args = args;
            return this;
        }

        public Builder commandType(CommandType commandType) {
            this.commandType = commandType;
            return this;
        }
    }

    @Override
    public String getUniqueCode() {
        return this.uniqueCode;
    }

    @Override
    public String getOrgIdProp() {
        return this.orgIdProp;
    }

    @Override
    public String getTargetIdProp() {
        return this.targetIdProp;
    }

    @Override
    public String getProcessBean() {
        return this.processBean;
    }

    @Override
    public String getProcessMethod() {
        return this.processMethod;
    }

    @Override
    public Object getOrgParam() {
        return this.orgParam;
    }

    @Override
    public Object getRealParam() {
        return this.realParam;
    }

    @Override
    public Class<?> getTarget() {
        return this.target;
    }

    @Override
    public List<Object> getSources() {
        return this.sources;
    }

    @Override
    public Class<?>[] getArgs() {
        return this.args;
    }

    @Override
    public CommandType getCommandType() {
        return this.commandType;
    }

    @Override
    public String toString() {
        return "DefaultBackupMetadata{" +
            "uniqueCode='" + uniqueCode + '\'' +
            ", orgIdProp='" + orgIdProp + '\'' +
            ", targetIdProp='" + targetIdProp + '\'' +
            ", processBean='" + processBean + '\'' +
            ", processMethod='" + processMethod + '\'' +
            ", orgParam=" + orgParam +
            ", realParam=" + realParam +
            ", target=" + target +
            ", sources=" + sources +
            ", commandType=" + commandType +
            '}';
    }
}
