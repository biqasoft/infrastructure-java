/*
* Copyright (c) 2016 biqasoft.com




 */

package com.biqasoft.bpmn.safejs.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by Nikita Bakaev, ya@nbakaev.ru on 2/16/2016.
 * All Rights Reserved
 */
@ApiModel("DTO for executng javascript code on server and get result")
public class ExecuteJsRequest {

    private String jsCode = null;

    @ApiModelProperty("Sets the maximum CPU time in milliseconds allowed for script execution.")
    private Long maxCPUtime = null;

    public Long getMaxCPUtime() {
        return maxCPUtime;
    }

    public void setMaxCPUtime(Long maxCPUtime) {
        this.maxCPUtime = maxCPUtime;
    }

    public String getJsCode() {
        return jsCode;
    }

    public void setJsCode(String jsCode) {
        this.jsCode = jsCode;
    }
}
