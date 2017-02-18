/*
 * Copyright (c) 2016. com.biqasoft
 */

package com.biqasoft.audit.object.diffs;

import java.io.Serializable;

public class ChangedLog implements Serializable {

    private String stringLog;

    public String getStringLog() {
        return stringLog;
    }

    public void setStringLog(String stringLog) {
        this.stringLog = stringLog;
    }
}
