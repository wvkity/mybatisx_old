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
package com.wvkity.mybatis.sequence;

import com.wvkity.sequence.snowflake.SnowflakeConfig;
import com.wvkity.sequence.snowflake.Strategy;
import com.wvkity.sequence.snowflake.core.AtomicStampedSnowflakeSequence;
import com.wvkity.sequence.snowflake.core.CacheableSnowflakeSequence;
import com.wvkity.sequence.snowflake.core.DefaultSnowflakeSequence;
import com.wvkity.sequence.snowflake.core.SnowflakeSequence;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author wvkity
 * @created 2021-02-18
 * @since 1.0.0
 */
public class SequenceApp {

    private static final Logger log = LoggerFactory.getLogger(SequenceApp.class);

    @Test
    public void defSequenceTest() {
        final SnowflakeSequence defSeq = new DefaultSnowflakeSequence(SnowflakeConfig.millisSnowflakeConfig(Strategy.DEFAULT));
        log.info("{}", defSeq.parse(defSeq.nextValue()).toJsonString());
        log.info("{}", defSeq.parse(defSeq.nextValue()).toJsonString());
        log.info("{}", defSeq.parse(defSeq.nextValue()).toJsonString());
        log.info("{}", defSeq.parse(defSeq.nextValue()).toJsonString());
        log.info("{}", defSeq.parse(defSeq.nextValue()).toJsonString());
        log.info("{}", defSeq.parse(defSeq.nextValue()).toJsonString());
        log.info("{}", defSeq.parse(defSeq.nextValue()).toJsonString());
    }

    @Test
    public void cacheableSequenceTest() {
        final SnowflakeSequence cacheSeq = new CacheableSnowflakeSequence(SnowflakeConfig.secondSnowflakeConfig());
        log.info("{}", cacheSeq.parse(cacheSeq.nextValue()).toJsonString());
        log.info("{}", cacheSeq.parse(cacheSeq.nextValue()).toJsonString());
        log.info("{}", cacheSeq.parse(cacheSeq.nextValue()).toJsonString());
        log.info("{}", cacheSeq.parse(cacheSeq.nextValue()).toJsonString());
        log.info("{}", cacheSeq.parse(cacheSeq.nextValue()).toJsonString());
        log.info("{}", cacheSeq.parse(cacheSeq.nextValue()).toJsonString());
        log.info("{}", cacheSeq.parse(cacheSeq.nextValue()).toJsonString());
    }

    @Test
    public void atomicSequenceTest() {
        final SnowflakeSequence atomicSeq = new AtomicStampedSnowflakeSequence(SnowflakeConfig.millisSnowflakeConfig(Strategy.ATOMIC));
        log.info("{}", atomicSeq.parse(atomicSeq.nextValue()).toJsonString());
        log.info("{}", atomicSeq.parse(atomicSeq.nextValue()).toJsonString());
        log.info("{}", atomicSeq.parse(atomicSeq.nextValue()).toJsonString());
        log.info("{}", atomicSeq.parse(atomicSeq.nextValue()).toJsonString());
        log.info("{}", atomicSeq.parse(atomicSeq.nextValue()).toJsonString());
        log.info("{}", atomicSeq.parse(atomicSeq.nextValue()).toJsonString());
        log.info("{}", atomicSeq.parse(atomicSeq.nextValue()).toJsonString());
    }


}
