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
package io.github.result.core;

import io.github.result.Status;

import java.util.Map;

/**
 * {@link java.util.Map Map}类型响应结果
 * @author wvkity
 * @created 2021-02-16
 * @since 1.0.0
 */
public class MultiDataResult extends AbstractMultiResult implements MultiResult {

    private static final long serialVersionUID = -4095025374855670232L;

    public MultiDataResult() {
    }

    public MultiDataResult(Status status) {
        this.code = status.getCode();
        this.message = status.getDesc();
    }

    public MultiDataResult(Throwable e) {
        this.code = Status.ERR_FAILURE.getCode();
        this.message = e.getMessage();
    }

    public MultiDataResult(Map<?, ?> data) {
        this.putAll(data);
    }

    public MultiDataResult(Status status, Throwable e) {
        this.code = status.getCode();
        this.message = e.getMessage();
    }

    public MultiDataResult(int code, Throwable e) {
        this.code = code;
        this.message = e.getMessage();
    }

    public MultiDataResult(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public MultiDataResult(Map<?, ?> data, String message) {
        this.message = message;
        this.putAll(data);
    }

    public MultiDataResult(Map<?, ?> data, int code, String message) {
        this.code = code;
        this.message = message;
        this.putAll(data);
    }

    @Override
    public String toString() {
        return "MultiDataResult{" +
            "data=" + data +
            ", code=" + code +
            ", message='" + message + '\'' +
            '}';
    }

    ///// Builder /////

    public static class Builder {
        private Map<?, ?> data;
        private int code;
        private String message;

        private Builder() {
        }

        public Builder code(int code) {
            this.code = code;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder data(Map<?, ?> data) {
            this.data = data;
            return this;
        }

        public MultiDataResult build() {
            final MultiDataResult result = new MultiDataResult(this.code, this.message);
            result.putAll(this.data);
            return result;
        }

        @Override
        public String toString() {
            return "Builder{" +
                "data=" + data +
                ", code=" + code +
                ", message='" + message + '\'' +
                '}';
        }
    }

    ///// Static methods /////

    public static Builder create() {
        return new Builder();
    }

    public static MultiDataResult of() {
        return new MultiDataResult();
    }

    public static MultiDataResult of(final Status status) {
        return new MultiDataResult(status);
    }

    public static MultiDataResult of(final int code, final String message) {
        return new MultiDataResult(code, message);
    }

    public static MultiDataResult ok() {
        return of();
    }

    public static MultiDataResult ok(final Map<?, ?> data) {
        return new MultiDataResult(data);
    }

    public static MultiDataResult ok(final Map<?, ?> data, final String message) {
        return new MultiDataResult(data, message);
    }

    public static MultiDataResult fail() {
        return of(Status.ERR_FAILURE);
    }

    public static MultiDataResult fail(final Throwable e) {
        return new MultiDataResult(e);
    }

    public static MultiDataResult fail(final Status status, final Throwable e) {
        return new MultiDataResult(status, e);
    }

    public static MultiDataResult fail(final int code, final Throwable e) {
        return new MultiDataResult(code, e);
    }

    public static MultiDataResult serverError() {
        return of(Status.ERR_SERVER);
    }

}
