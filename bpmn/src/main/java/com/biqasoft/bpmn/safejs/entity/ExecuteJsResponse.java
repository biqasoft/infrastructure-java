/*
* Copyright (c) 2016 biqasoft.com




 */

package com.biqasoft.bpmn.safejs.entity;

import io.swagger.annotations.ApiModel;

/**
 * Created by Nikita Bakaev, ya@nbakaev.ru on 2/16/2016.
 * All Rights Reserved
 */
@ApiModel("DTO for result of executing javascript code on server with result")
public class ExecuteJsResponse {

    Object result = null;

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}
