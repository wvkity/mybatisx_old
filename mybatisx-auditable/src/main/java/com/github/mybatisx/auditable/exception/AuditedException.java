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
package com.github.mybatisx.auditable.exception;

/**
 * 审计异常
 * @author wvkity
 * @created 2021-03-11
 * @since 1.0.0
 */
public class AuditedException extends RuntimeException {

    private static final long serialVersionUID = -2416512247895991178L;

    public AuditedException() {
        super();
    }

    public AuditedException(String message) {
        super(message);
    }

    public AuditedException(Throwable cause) {
        super(cause);
    }

    public AuditedException(String message, Throwable cause) {
        super(message, cause);
    }

    public AuditedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
