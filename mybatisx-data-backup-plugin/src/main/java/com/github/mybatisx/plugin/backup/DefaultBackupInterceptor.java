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
package com.github.mybatisx.plugin.backup;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;

import java.util.Properties;

/**
 * 默认数据备份拦截器
 * <pre>{@code
 *      // 使用说明:
 *
 *      // 例子:
 *      // @BackupListener可选
 *
 *      @BackupListener(@BackupTarget(value = UserBak.class, args = {List.class}))
 *      public class User {
 *          private Long id;
 *          private String userName;
 *          private Integer state;
 *          private String email;
 *          private Integer version;
 *          // ...
 *      }
 *
 *      public class UserBak {
 *          private Long id;
 *          private String userName;
 *          private Integer state;
 *          private String email;
 *          private Integer version;
 *          //...
 *          private Long orgId;
 *      }
 *
 *      @Mapper
 *      @Repository
 *      public interface UserMapper {
 *
 *          @BackupFilter(selectMethod = "selectUser1")
 *          @Update("UPDATE USER SET USER_NAME = #{userName}, EMAIL = #{email} WHERE ID = #{id} AND VERSION = #{version}")
 *          int updateInfo1(User user);
 *
 *          @BackupFilter(selectMethod = "selectUser2", alias = "user")
 *          @Update("UPDATE USER SET USER_NAME = #{user.userName}, EMAIL = #{user.email} WHERE ID = #{user.id} AND STATE = #{user.state}")
 *          int updateInfo2(@Param("user") User user);
 *
 *          @BackupFilter(source = User.class, target = UserBak.class, args = {List.class})
 *          @Update("UPDATE USER SET USER_NAME = #{userName}, EMAIL = #{email} WHERE ID = #{id} AND VERSION = #{version}")
 *          int updateInfo3(@Param("userName") String userName, @Param("email") String email, @Param("id") Long id,
 *                          @Param("version") Integer version);
 *
 *          @Select("SELECT * FROM USER WHERE ID = #{id} AND VERSION = #{version}")
 *          User selectUser1(User user);
 *
 *          @Select("SELECT * FROM USER WHERE WHERE ID = #{user.id} AND STATE = #{user.state}")
 *          User selectUser2(@Param("user") User user);
 *
 *      }
 *
 *      // 省略UserService类
 *
 *      @Mapper
 *      @Repository
 *      public interface UserBakMapper {
 *
 *          int saveInsert(@Param("batchWrapper") List<UserBak> users);
 *      }
 *
 *      public interface UserBakService {
 *
 *          int saveBatch(List<UserBak> users);
 *      }
 *
 *      // 省略UserBakServiceImpl类
 *
 *      // 配置
 *
 *      // 数据查询配置(可选)
 *      @Component
 *      public class DefaultQueryProcessor implements QueryProcessor {
 *
 *          // 方法优先级: queryList &gt; makeMappedStatement &gt; makeQuerySql
 *          // ... 实现方法即可
 *      }
 *
 *      // 数据备份通知配置
 *      @Component
 *      public class DefaultBroadcast implements Broadcast {
 *
 *          // ... 实现方法即可
 *      }
 *
 *      // yml文件配置
 *      github:
 *          mybatisx:
 *              plugin:
 *                  filter-policies: #拦截策略，可指定多个，默认拦截所有(增、删、改)
 *                  non-condition-filter: #删除、修改操作没有条件是否拦截备份
 *                  policy: #监听处理策略(默认是用队列处理)
 *
 *      // 其他配置，按自己的需求重写类实现即可
 * }</pre>
 * @author wvkity
 * @created 2021-07-17
 * @since 1.0.0
 */
@Intercepts({
    @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})
})
public class DefaultBackupInterceptor implements Interceptor {

    private final BackupHandler backupHandler;

    public DefaultBackupInterceptor(BackupHandler backupHandler) {
        this.backupHandler = backupHandler;
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        return this.backupHandler.intercept(invocation);
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        this.backupHandler.setProperties(properties);
    }
}
