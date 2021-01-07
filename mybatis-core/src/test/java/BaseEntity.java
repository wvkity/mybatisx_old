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

import com.wvkity.mybatis.annotation.Table;

/**
 * @author wvkity
 * @created 2020-10-06
 * @since 1.0.0
 */
@Table(name = "base_table")
public class BaseEntity {

    private Long id;
    private String userName;

    public Long getId() {
        return id;
    }

    public BaseEntity setId(Long id) {
        this.id = id;
        return this;
    }

    public String getUserName() {
        return userName;
    }

    public BaseEntity setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public BaseEntity() {
    }

    public BaseEntity(Long id, String userName) {
        this.id = id;
        this.userName = userName;
    }
}
