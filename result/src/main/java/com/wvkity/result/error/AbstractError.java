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
package com.wvkity.result.error;

import com.wvkity.result.Status;

/**
 * 抽象错误
 * @author wvkity
 * @created 2021-02-16
 * @since 1.0.0
 */
@SuppressWarnings("serial")
public abstract class AbstractError implements Error {

    /**
     * 响应状态码
     */
    protected int code = Status.OK.getCode();
    /**
     * 响应描述信息
     */
    protected String error = "success";

    @Override
    public void error(int code, String error) {
        this.code = code;
        this.error = error;
    }

    @Override
    public int getCode() {
        return this.code;
    }

    @Override
    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public String getError() {
        return this.error;
    }

    @Override
    public void setError(String error) {
        this.error = error;
    }
}
