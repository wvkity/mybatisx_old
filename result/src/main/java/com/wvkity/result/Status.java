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
package com.wvkity.result;

/**
 * 响应状态
 * @author wvkity
 * @created 2021-02-16
 * @since 1.0.0
 */
public enum Status {

    /**
     * OK
     */
    OK(200, "正常"),
    /**
     * FAILURE
     */
    ERR_FAILURE(100000, "失败"),
    /**
     * UNAUTHORIZED
     */
    ERR_UNAUTHORIZED(400401, "未授权"),
    /**
     * INVALID TOKEN
     */
    ERR_INVALID_TOKEN(400402, "无效token"),
    /**
     * FORBIDDEN
     */
    ERR_FORBIDDEN(400403, "无效请求"),
    /**
     * NOT FOUND(
     */
    ERR_NOT_FOUND(400404, "未找到资源"),
    /**
     * TIMEOUT
     */
    ERR_TIMEOUT(400405, "请求超时"),
    /**
     * SERVER ERROR
     */
    ERR_SERVER(500500, "服务器发生异常");

    /**
     * 状态码
     */
    private final int code;
    /**
     * 描述
     */
    private final String desc;

    Status(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
