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

/**
 * 响应结果
 * @param <T> 数据类型
 * @author wvkity
 * @created 2021-02-16
 * @since 1.0.0
 */
public class DataResult<T> extends AbstractResult<T> implements Result<T> {

    private static final long serialVersionUID = 2215253711559101029L;

    public DataResult() {
    }

    public DataResult(T data) {
        this.data = data;
    }

    public DataResult(T data, String message) {
        this.data = data;
        this.message = message;
    }

    public DataResult(Status status) {
        this.code = status.getCode();
        this.message = status.getDesc();
    }

    public DataResult(Throwable e) {
        this.code = Status.ERR_FAILURE.getCode();
        this.message = e.getMessage();
    }

    public DataResult(Status status, Throwable e) {
        this.code = status.getCode();
        this.message = e.getMessage();
    }

    public DataResult(int code, Throwable e) {
        this.code = code;
        this.message = e.getMessage();
    }

    public DataResult(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public DataResult(T data, int code, String error) {
        this.data = data;
        this.code = code;
        this.message = error;
    }

    @Override
    public DataResult<T> setData(T data) {
        this.data = data;
        return this;
    }

    @Override
    public String toString() {
        return "DataResult{" +
            "data=" + data +
            ", code=" + code +
            ", message='" + message + '\'' +
            '}';
    }

    ///// Builder /////

    public static class Builder<T> {
        private T data;
        private int code = Status.OK.getCode();
        private String error = "success";

        private Builder() {
        }

        public Builder<T> data(T data) {
            this.data = data;
            return this;
        }

        public Builder<T> code(int code) {
            this.code = code;
            return this;
        }

        public Builder<T> error(String error) {
            this.error = error;
            return this;
        }

        public DataResult<T> build() {
            return new DataResult<>(this.data, this.code, this.error);
        }

        @Override
        public String toString() {
            return "Builder{" +
                "data=" + data +
                ", code=" + code +
                ", error='" + error + '\'' +
                '}';
        }
    }

    ///// Static methods /////

    public static <T> Builder<T> create() {
        return new Builder<>();
    }

    public static <T> DataResult<T> of() {
        return new DataResult<>();
    }

    public static <T> DataResult<T> of(final T data) {
        return new DataResult<>(data);
    }

    public static <T> DataResult<T> of(final T data, final String message) {
        return new DataResult<>(data, message);
    }

    public static <T> DataResult<T> of(final Status status) {
        return new DataResult<>(status);
    }

    public static <T> DataResult<T> of(final int code, final String message) {
        return new DataResult<>(code, message);
    }

    public static <T> DataResult<T> ok() {
        return new DataResult<>();
    }

    public static <T> DataResult<T> ok(final T data) {
        return new DataResult<>(data);
    }

    public static <T> DataResult<T> ok(final T data, final String message) {
        return new DataResult<>(data, message);
    }

    public static <T> DataResult<T> fail() {
        return of(Status.ERR_FAILURE);
    }

    public static <T> DataResult<T> fail(final Throwable e) {
        return new DataResult<>(e);
    }

    public static <T> DataResult<T> fail(final Status status, final Throwable e) {
        return new DataResult<>(status, e);
    }

    public static <T> DataResult<T> fail(final int code, final Throwable e) {
        return new DataResult<>(code, e);
    }

    public static <T> DataResult<T> serverError() {
        return of(Status.ERR_SERVER);
    }

}
