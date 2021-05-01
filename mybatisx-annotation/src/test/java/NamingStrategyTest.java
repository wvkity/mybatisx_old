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

import com.wvkity.mybatis.annotation.NamingStrategy;
import org.junit.jupiter.api.Test;

/**
 * @author wvkity
 * @created 2020-10-18
 * @since 1.0.0
 */
public class NamingStrategyTest {

    @Test
    public void test() {
        System.out.println(NamingStrategy.UPPER.to(NamingStrategy.LOWER, "ADMIN"));
        System.out.println(NamingStrategy.UPPER_CAMEL.to(NamingStrategy.UPPER, "Admin"));
        System.out.println(NamingStrategy.LOWER.to(NamingStrategy.UPPER, "admin"));
        System.out.println(NamingStrategy.LOWER.to(NamingStrategy.UPPER_CAMEL, "admin"));
        System.out.println(NamingStrategy.UPPER_UNDERSCORE.to(NamingStrategy.LOWER, "ADMIN_ROOT"));
        System.out.println(NamingStrategy.UPPER_UNDERSCORE.to(NamingStrategy.UPPER, "ADMIN_ROOT"));
        System.out.println(NamingStrategy.UPPER_UNDERSCORE.to(NamingStrategy.UPPER_CAMEL, "ADMIN_ROOT"));
        System.out.println(NamingStrategy.UPPER_UNDERSCORE.to(NamingStrategy.LOWER_CAMEL, "ADMIN_ROOT"));
        System.out.println("================");
        System.out.println(NamingStrategy.LOWER_CAMEL.to(NamingStrategy.NORMAL, "maxWidth"));
        System.out.println(NamingStrategy.LOWER_CAMEL.to(NamingStrategy.LOWER, "maxWidth"));
        System.out.println(NamingStrategy.LOWER_CAMEL.to(NamingStrategy.UPPER, "maxWidth"));
        System.out.println(NamingStrategy.LOWER_CAMEL.to(NamingStrategy.UPPER_CAMEL, "maxWidth"));
        System.out.println(NamingStrategy.LOWER_CAMEL.to(NamingStrategy.UPPER_UNDERSCORE, "maxWidth"));
        System.out.println(NamingStrategy.LOWER_CAMEL.to(NamingStrategy.LOWER_UNDERSCORE, "maxWidth"));
        System.out.println("===============");
        System.out.println(NamingStrategy.UPPER_CAMEL.to(NamingStrategy.NORMAL, "UserName"));
        System.out.println(NamingStrategy.UPPER_CAMEL.to(NamingStrategy.LOWER, "UserName"));
        System.out.println(NamingStrategy.UPPER_CAMEL.to(NamingStrategy.LOWER_CAMEL, "UserName"));
        System.out.println(NamingStrategy.UPPER_CAMEL.to(NamingStrategy.LOWER_UNDERSCORE, "UserName"));
        System.out.println(NamingStrategy.UPPER_CAMEL.to(NamingStrategy.UPPER_UNDERSCORE, "UserName"));
        System.out.println("=============");
        System.out.println(NamingStrategy.LOWER_UNDERSCORE.to(NamingStrategy.NORMAL, "my_school"));
        System.out.println(NamingStrategy.LOWER_UNDERSCORE.to(NamingStrategy.LOWER, "my_school"));
        System.out.println(NamingStrategy.LOWER_UNDERSCORE.to(NamingStrategy.UPPER, "my_school"));
        System.out.println(NamingStrategy.LOWER_UNDERSCORE.to(NamingStrategy.UPPER_UNDERSCORE, "my_school"));
        System.out.println(NamingStrategy.LOWER_UNDERSCORE.to(NamingStrategy.LOWER_CAMEL, "my_school"));
        System.out.println(NamingStrategy.LOWER_UNDERSCORE.to(NamingStrategy.UPPER_CAMEL, "my_school"));
    }
}