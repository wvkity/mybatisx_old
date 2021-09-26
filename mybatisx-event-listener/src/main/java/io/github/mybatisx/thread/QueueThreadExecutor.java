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
package io.github.mybatisx.thread;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * 队列线程池
 * @author wvkity
 * @created 2021-07-25
 * @since 1.0.0
 */
public class QueueThreadExecutor {

    private final ThreadPoolExecutor executor;

    public QueueThreadExecutor(ThreadPoolExecutor executor) {
        this.executor = executor;
    }

    public void execute(final Runnable command) {
        this.executor.execute(command);
    }

    public void submit(Runnable command) {
        this.executor.submit(command);
    }

    public void shutdown() {
        this.executor.shutdown();
    }

    public String getStatus() {
        return String
            .format(
                "Completed tasks (%d) : Active threads (%d) : Maximum reached threads (%d) : Maximum allowed threads " +
                    "(%d) : Current threads in pool(%d)",
                executor.getCompletedTaskCount(), executor
                    .getActiveCount(), executor
                    .getLargestPoolSize(), executor
                    .getMaximumPoolSize(), executor.getPoolSize());
    }
}
