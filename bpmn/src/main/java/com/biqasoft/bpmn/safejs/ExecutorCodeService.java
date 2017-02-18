/*
 * Copyright (c) 2016. com.biqasoft
 */

package com.biqasoft.bpmn.safejs;

import org.springframework.stereotype.Service;
import com.biqasoft.bpmn.safejs.exposed.ExposedData;
import com.biqasoft.bpmn.safejs.internal.NashornSandbox;
import com.biqasoft.bpmn.safejs.internal.NashornSandboxes;
import com.biqasoft.bpmn.safejs.entity.ExecuteJsRequest;
import com.biqasoft.bpmn.safejs.entity.ExecuteJsResponse;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.Executors;

/**
 * Created by Nikita Bakaev, ya@nbakaev.ru on 2/17/2016.
 * All Rights Reserved
 */
@Service
public class ExecutorCodeService {

    //in milliseconds
    public static final Long maxCPUtime = 10_000L;

    /**
     * @param executeJsRequest user set this object
     * @return result of executing code and status
     */
    public ExecuteJsResponse executeCode(ExecuteJsRequest executeJsRequest) {
        return executeCode(executeJsRequest, null);
    }

    /**
     * @param executeJsRequest user set this object
     * @param globalVariables  object that will be exposed and accessible in javascript. Only system set this object
     * @return result of executing code and status
     */
    public ExecuteJsResponse executeCode(ExecuteJsRequest executeJsRequest, Map<String, Object> globalVariables) {
        String jsCode = executeJsRequest.getJsCode();

        NashornSandbox sandbox = NashornSandboxes.create();
        ExposedData exposedData = new ExposedData();
        exposedData.setData("hello world");

        // global variables
        sandbox.inject("BPMN_META_DATA", exposedData);
        sandbox.inject("BPMN_SCRIPT", executeJsRequest.getJsCode());

        // classes that can be created from javascript
        sandbox.allow(Date.class);
        sandbox.allow(Long.class);
        sandbox.allow(Integer.class);
        sandbox.allow(String.class);

        if (globalVariables != null) {
            globalVariables.forEach(sandbox::inject);
        }

        // CPU time execution limit
        Long maxCpuTimeForJs = maxCPUtime;

        if (executeJsRequest.getMaxCPUtime() != null && executeJsRequest.getMaxCPUtime() < maxCPUtime && executeJsRequest.getMaxCPUtime() != 0) {
            maxCpuTimeForJs = executeJsRequest.getMaxCPUtime();
        }

        sandbox.setMaxCPUTime(maxCpuTimeForJs);
        sandbox.setExecutor(Executors.newSingleThreadExecutor());

        // do not allow network - security issue for internal microservice network
//        sandbox.allow(sun.net.www.protocol.http.HttpURLConnection.class);

        Object o;
        try {
            o = sandbox.eval(jsCode);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        ExecuteJsResponse response1 = new ExecuteJsResponse();
        response1.setResult(o);
        return response1;
    }

}
