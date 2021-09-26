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
package io.github.mybatisx.sequence;

import io.github.sequence.snowflake.distributor.DefaultSecondDistributor;
import io.github.sequence.snowflake.distributor.Distributor;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author wvkity
 * @created 2021-02-18
 * @since 1.0.0
 */
public class DistributorApp {

    private static final Logger log = LoggerFactory.getLogger(DistributorApp.class);

    @Test
    public void test1() {
        final Distributor distributor = new DefaultSecondDistributor(5, 5);
        log.info("{}", distributor);
    }
}
