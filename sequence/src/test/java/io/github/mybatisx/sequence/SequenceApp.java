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

import io.github.sequence.snowflake.SnowflakeConfig;
import io.github.sequence.snowflake.Strategy;
import io.github.sequence.snowflake.core.AtomicStampedSnowflakeSequence;
import io.github.sequence.snowflake.core.CacheableSnowflakeSequence;
import io.github.sequence.snowflake.core.DefaultSnowflakeSequence;
import io.github.sequence.snowflake.core.SnowflakeSequence;
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
        for (int i = 0; i < 15; i++) {
            log.info("id = {}", defSeq.nextId());
        }
        /*log.info("{}", defSeq.parse(defSeq.nextId()).toJsonString());
        log.info("{}", defSeq.parse(defSeq.nextId()).toJsonString());
        log.info("{}", defSeq.parse(defSeq.nextId()).toJsonString());
        log.info("{}", defSeq.parse(defSeq.nextId()).toJsonString());
        log.info("{}", defSeq.parse(defSeq.nextId()).toJsonString());
        log.info("{}", defSeq.parse(defSeq.nextId()).toJsonString());
        log.info("{}", defSeq.parse(defSeq.nextId()).toJsonString());*/
    }

    @Test
    public void cacheableSequenceTest() {
        final SnowflakeSequence cacheSeq = new CacheableSnowflakeSequence(SnowflakeConfig.secondSnowflakeConfig());
        for (int i = 0; i < 15; i++) {
            log.info("id = {}", cacheSeq.nextId());
        }
        /*log.info("{}", cacheSeq.parse(cacheSeq.nextId()).toJsonString());
        log.info("{}", cacheSeq.parse(cacheSeq.nextId()).toJsonString());
        log.info("{}", cacheSeq.parse(cacheSeq.nextId()).toJsonString());
        log.info("{}", cacheSeq.parse(cacheSeq.nextId()).toJsonString());
        log.info("{}", cacheSeq.parse(cacheSeq.nextId()).toJsonString());
        log.info("{}", cacheSeq.parse(cacheSeq.nextId()).toJsonString());
        log.info("{}", cacheSeq.parse(cacheSeq.nextId()).toJsonString());*/
    }

    @Test
    public void atomicSequenceTest() {
        final SnowflakeSequence atomicSeq = new AtomicStampedSnowflakeSequence(SnowflakeConfig.millisSnowflakeConfig(Strategy.ATOMIC));
        for (int i = 0; i < 30; i++) {
            log.info("id = {}", atomicSeq.nextId());
        }
        /*log.info("{}", atomicSeq.parse(atomicSeq.nextId()).toJsonString());
        log.info("{}", atomicSeq.parse(atomicSeq.nextId()).toJsonString());
        log.info("{}", atomicSeq.parse(atomicSeq.nextId()).toJsonString());
        log.info("{}", atomicSeq.parse(atomicSeq.nextId()).toJsonString());
        log.info("{}", atomicSeq.parse(atomicSeq.nextId()).toJsonString());
        log.info("{}", atomicSeq.parse(atomicSeq.nextId()).toJsonString());
        log.info("{}", atomicSeq.parse(atomicSeq.nextId()).toJsonString());*/
    }


}
