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
package com.wvkity.result.core;

import com.wvkity.result.Status;

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
        this.error = status.getDesc();
    }

    public MultiDataResult(Throwable e) {
        this.code = Status.ERR_FAILURE.getCode();
        this.error = e.getMessage();
    }

    public MultiDataResult(Status status, Throwable e) {
        this.code = status.getCode();
        this.error = e.getMessage();
    }

    public MultiDataResult(int code, Throwable e) {
        this.code = code;
        this.error = e.getMessage();
    }

    public MultiDataResult(int code, String message) {
        this.code = code;
        this.error = message;
    }

    public MultiDataResult(Map<String, Object> data, int code, String message) {
        this.code = code;
        this.error = message;
        this.putAll(data);
    }

    @Override
    public String toString() {
        return "MultiDataResult{" +
            "data=" + data +
            ", code=" + code +
            ", error='" + error + '\'' +
            '}';
    }

    ///// Builder /////
    public static class MultiDataResultBuilder {
        private Map<String, Object> data;
        private int code;
        private String error;

        private MultiDataResultBuilder() {
        }

        public MultiDataResultBuilder code(int code) {
            this.code = code;
            return this;
        }

        public MultiDataResultBuilder error(String error) {
            this.error = error;
            return this;
        }

        public MultiDataResultBuilder data(Map<String, Object> data) {
            this.data = data;
            return this;
        }

        public MultiDataResult build() {
            return new MultiDataResult(this.data, this.code, this.error);
        }

        @Override
        public String toString() {
            return "MultiDataResultBuilder{" +
                "data=" + data +
                ", code=" + code +
                ", error='" + error + '\'' +
                '}';
        }
    }

    ///// Static methods /////

    public static MultiDataResultBuilder builder() {
        return new MultiDataResultBuilder();
    }

    public static MultiDataResult of() {
        return new MultiDataResult();
    }

    public static MultiDataResult of(final Status status) {
        return new MultiDataResult(status);
    }

    public static MultiDataResult of(final Throwable e) {
        return new MultiDataResult(e);
    }

    public static MultiDataResult of(final Status status, final Throwable e) {
        return new MultiDataResult(status, e);
    }

    public static MultiDataResult of(final int code, final Throwable e) {
        return new MultiDataResult(code, e);
    }

    public static MultiDataResult of(final int code, final String message) {
        return new MultiDataResult(code, message);
    }

    public static MultiDataResult ok() {
        return of();
    }

    public static MultiDataResult failure() {
        return new MultiDataResult(Status.ERR_FAILURE);
    }

    public static  MultiDataResult serverError() {
        return new MultiDataResult(Status.ERR_SERVER);
    }

}
