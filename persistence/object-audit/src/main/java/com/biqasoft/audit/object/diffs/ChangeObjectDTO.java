/*
 * Copyright (c) 2016. com.biqasoft
 */

package com.biqasoft.audit.object.diffs;

import org.javers.core.diff.Change;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Nikita Bakaev, ya@nbakaev.ru on 5/10/2016.
 * All Rights Reserved
 */
public class ChangeObjectDTO implements Serializable, Comparable {

    private String userId;
    private Date date;

    private Change change;

    private String action;


    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Change getChange() {
        return change;
    }

    public void setChange(Change change) {
        this.change = change;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public int compareTo(Object o) {

        if (o instanceof ChangeObjectDTO) {
            return date.compareTo( ((ChangeObjectDTO) o).getDate() );
        }else{
            throw new ClassCastException("");
        }

    }
}
