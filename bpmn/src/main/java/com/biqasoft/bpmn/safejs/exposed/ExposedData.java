/*
 * Copyright (c) 2016. com.biqasoft
 */

package com.biqasoft.bpmn.safejs.exposed;

import java.io.Serializable;
import java.util.Date;

/**
 * This object is accessed in bpmn:safejs by user
 *
 * @author Nikita Bakaev, ya@nbakaev.ru
 *         Date: 6/1/2016
 *         All Rights Reserved
 */
public class ExposedData implements Serializable {

    private Date startDate = new Date();
    private String data = null;

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
