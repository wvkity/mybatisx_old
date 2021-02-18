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
package com.wvkity.mybatis.core.sequence.snowflake;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.NetworkInterface;

/**
 * 序列工具
 * @author wvkity
 * @created 2021-02-17
 * @since 1.0.0
 */
public final class Sequences {

    private static final Logger log = LoggerFactory.getLogger(Sequences.class);

    private Sequences() {
    }

    public static long getMaxWorkerId(final int workerBits) {
        return ~(-1L << workerBits);
    }

    public static long getMaxDataCenterId(final int dataCenterBits) {
        return ~(-1L << dataCenterBits);
    }

    public static long getDefDataCenterId(final int dataCenterBits) {
        long id = 0L;
        try {
            final InetAddress ip = InetAddress.getLocalHost();
            final NetworkInterface ni = NetworkInterface.getByInetAddress(ip);
            if (ni == null) {
                id = 1L;
            } else {
                final byte[] mac = ni.getHardwareAddress();
                if (mac != null) {
                    id = ((0x000000FF & (long) mac[mac.length - 2])
                        | (0x0000FF00 & (((long) mac[mac.length - 1]) << 8))) >> 6;
                    id = id % (getMaxDataCenterId(dataCenterBits) + 1);
                }
            }
        } catch (Exception e) {
            log.warn("Failed to get the corresponding worker id according to MAC address: ", e);
        }
        return id;
    }

    public static long getDefWorkerId(int workerBits, int dataCenterBits) {
        final StringBuilder builder = new StringBuilder();
        builder.append(getDefDataCenterId(dataCenterBits));
        String name = ManagementFactory.getRuntimeMXBean().getName();
        if (name != null && name.length() > 0) {
            builder.append(name.split("@")[0]);
        }
        return (builder.toString().hashCode() & 0xffff) % (getMaxWorkerId(workerBits) + 1);
    }

}
