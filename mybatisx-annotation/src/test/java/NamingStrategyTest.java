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

import io.github.mybatisx.annotation.NamingPolicy;
import org.junit.jupiter.api.Test;

/**
 * @author wvkity
 * @created 2020-10-18
 * @since 1.0.0
 */
public class NamingStrategyTest {

    @Test
    public void test() {
        System.out.println(NamingPolicy.UPPER.to(NamingPolicy.LOWER, "ADMIN"));
        System.out.println(NamingPolicy.UPPER_CAMEL.to(NamingPolicy.UPPER, "Admin"));
        System.out.println(NamingPolicy.LOWER.to(NamingPolicy.UPPER, "admin"));
        System.out.println(NamingPolicy.LOWER.to(NamingPolicy.UPPER_CAMEL, "admin"));
        System.out.println(NamingPolicy.UPPER_UNDERSCORE.to(NamingPolicy.LOWER, "ADMIN_ROOT"));
        System.out.println(NamingPolicy.UPPER_UNDERSCORE.to(NamingPolicy.UPPER, "ADMIN_ROOT"));
        System.out.println(NamingPolicy.UPPER_UNDERSCORE.to(NamingPolicy.UPPER_CAMEL, "ADMIN_ROOT"));
        System.out.println(NamingPolicy.UPPER_UNDERSCORE.to(NamingPolicy.LOWER_CAMEL, "ADMIN_ROOT"));
        System.out.println("================");
        System.out.println(NamingPolicy.LOWER_CAMEL.to(NamingPolicy.NORMAL, "maxWidth"));
        System.out.println(NamingPolicy.LOWER_CAMEL.to(NamingPolicy.LOWER, "maxWidth"));
        System.out.println(NamingPolicy.LOWER_CAMEL.to(NamingPolicy.UPPER, "maxWidth"));
        System.out.println(NamingPolicy.LOWER_CAMEL.to(NamingPolicy.UPPER_CAMEL, "maxWidth"));
        System.out.println(NamingPolicy.LOWER_CAMEL.to(NamingPolicy.UPPER_UNDERSCORE, "maxWidth"));
        System.out.println(NamingPolicy.LOWER_CAMEL.to(NamingPolicy.LOWER_UNDERSCORE, "maxWidth"));
        System.out.println("===============");
        System.out.println(NamingPolicy.UPPER_CAMEL.to(NamingPolicy.NORMAL, "UserName"));
        System.out.println(NamingPolicy.UPPER_CAMEL.to(NamingPolicy.LOWER, "UserName"));
        System.out.println(NamingPolicy.UPPER_CAMEL.to(NamingPolicy.LOWER_CAMEL, "UserName"));
        System.out.println(NamingPolicy.UPPER_CAMEL.to(NamingPolicy.LOWER_UNDERSCORE, "UserName"));
        System.out.println(NamingPolicy.UPPER_CAMEL.to(NamingPolicy.UPPER_UNDERSCORE, "UserName"));
        System.out.println("=============");
        System.out.println(NamingPolicy.LOWER_UNDERSCORE.to(NamingPolicy.NORMAL, "my_school"));
        System.out.println(NamingPolicy.LOWER_UNDERSCORE.to(NamingPolicy.LOWER, "my_school"));
        System.out.println(NamingPolicy.LOWER_UNDERSCORE.to(NamingPolicy.UPPER, "my_school"));
        System.out.println(NamingPolicy.LOWER_UNDERSCORE.to(NamingPolicy.UPPER_UNDERSCORE, "my_school"));
        System.out.println(NamingPolicy.LOWER_UNDERSCORE.to(NamingPolicy.LOWER_CAMEL, "my_school"));
        System.out.println(NamingPolicy.LOWER_UNDERSCORE.to(NamingPolicy.UPPER_CAMEL, "my_school"));
    }
}
