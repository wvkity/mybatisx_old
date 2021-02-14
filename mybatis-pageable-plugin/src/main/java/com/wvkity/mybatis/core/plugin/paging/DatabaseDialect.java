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
package com.wvkity.mybatis.core.plugin.paging;

/**
 * 数据库方言枚举
 * @author wvkity
 * @created 2021-02-09
 * @since 1.0.0
 */
public enum DatabaseDialect {
    /**
     * ORACLE
     */
    ORACLE,

    /**
     * POSTGRESQL
     */
    POSTGRESQL,

    /**
     * H2
     */
    H2,

    /**
     * DB2
     */
    DB2,

    /**
     * MYSQL
     */
    MYSQL,

    /**
     * MARIADB
     */
    MARIADB,

    /**
     * SQL SERVER
     */
    SQLSERVER,

    /**
     * SQL SERVER 2012 OR LATER
     */
    SQLSERVERLATER,

    /**
     * SQLITE
     */
    SQLITE,

    /**
     * CLOUDSCAPE
     */
    CLOUDSCAPE,

    /**
     * DERBY
     */
    DERBY,

    /**
     * HSQLDB
     */
    HSQLDB,

    /**
     * SYBASE
     */
    SYBASE,

    /**
     * INFORMIX
     */
    INFORMIX,

    /**
     * INFORMIXSQLI
     */
    INFORMIXSQLI,

    /**
     * DM(达梦)
     */
    DM,

    /**
     * 未定义
     */
    UNDEFINED
}
