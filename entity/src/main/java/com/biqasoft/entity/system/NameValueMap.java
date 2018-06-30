/*
* Copyright (c) 2016 biqasoft.com




 */

package com.biqasoft.entity.system;

import lombok.Data;
import org.javers.core.metamodel.annotation.Value;

import java.io.Serializable;

@Data
@Value
public class NameValueMap implements Serializable {

    private String name;
    private String value;
    private String type;


    public NameValueMap(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public NameValueMap(String name, String value, String type) {
        this.name = name;
        this.value = value;
        this.type = type;
    }

    public NameValueMap() {
    }

}
