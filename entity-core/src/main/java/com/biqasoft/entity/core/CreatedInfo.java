/*
* Copyright (c) 2016 biqasoft.com




 */

package com.biqasoft.entity.core;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.javers.core.metamodel.annotation.Value;
import org.springframework.data.mongodb.core.index.Indexed;

import java.io.Serializable;
import java.util.Date;

@Data
@Value
@ApiModel(description = "Information about creating this object. Who and when created it")
public class CreatedInfo implements Serializable {

    @Indexed
    private Date createdDate;
    @Indexed
    private String createdById;

    public CreatedInfo() {
    }


    public CreatedInfo(Date createdDate, String createdById) {
        this.createdDate = createdDate;
        this.createdById = createdById;
    }

    public CreatedInfo(Date createdDate) {
        this.createdDate = createdDate;
    }

}
