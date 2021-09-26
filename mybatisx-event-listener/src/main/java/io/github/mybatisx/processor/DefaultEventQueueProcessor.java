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
package io.github.mybatisx.processor;

import io.github.mybatisx.Objects;
import io.github.mybatisx.event.Event;
import io.github.mybatisx.event.handle.Handler;
import io.github.mybatisx.queue.EventQueue;
import io.github.mybatisx.queue.QueueMetadata;
import io.github.mybatisx.thread.QueueThreadExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 默认事件队列处理器
 * @author wvkity
 * @created 2021-07-25
 * @since 1.0.0
 */
public class DefaultEventQueueProcessor implements EventQueueProcessor {

    private static final Logger log = LoggerFactory.getLogger(DefaultEventQueueProcessor.class);
    protected final QueueThreadExecutor executor;
    protected final EventQueue queue;
    protected final List<Handler> delegates;
    protected final Map<String, Handler> delegateCache;
    protected final AtomicBoolean running = new AtomicBoolean(false);

    public DefaultEventQueueProcessor(QueueThreadExecutor executor, EventQueue queue,
                                      List<Handler> delegates) {
        this.executor = executor;
        this.queue = queue;
        this.delegates = delegates;
        this.delegateCache = new ConcurrentHashMap<>();
        if (Objects.isNotEmpty(delegates)) {
            for (Handler it : delegates) {
                this.delegateCache.putIfAbsent(it.getEventUnique(), it);
            }
        }
    }

    @PostConstruct
    public void init() {
        this.executor.submit(this);
        this.running.set(true);
    }

    @Override
    public void start() {
        this.running.set(true);
    }

    @Override
    public void stop() {
        this.running.set(false);
    }

    @Override
    public void run() {
        try {
            while (this.running.get()) {
                final QueueMetadata metadata = this.queue.take();
                this.process(metadata);
            }
        } catch (Exception e) {
            log.error("Queue data processing failed: {}", e.getMessage(), e);
        }
    }

    @Override
    public void process(QueueMetadata data) {
        try {
            final Event<?> event = data.getEvent();
            final String cacheKey = event.getEventUnique();
            final Handler handler = this.delegateCache.get(cacheKey);
            if (handler != null) {
                handler.handle(event, data.getEventPhase());
            } else {
                throw new NullPointerException("The corresponding event handler cannot be found according to the '"
                    + cacheKey + "' identifier");
            }
        } catch (Exception e) {
            log.error("Queue data processing failed: eventUnique -- {}, {}",
                data.getEvent().getEventUnique(), e.getMessage(), e);
        }
    }
}
