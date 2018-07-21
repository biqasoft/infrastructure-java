/*
* Copyright (c) 2016 biqasoft.com




 */

package com.biqasoft.entity.core;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.javers.core.metamodel.annotation.Value;
import org.springframework.data.mongodb.core.index.Indexed;

import java.time.LocalDateTime;

@Data
@Value
@ApiModel(description = "Information about creating this object. Who and when created it")
public class CreatedInfo {

    @Indexed
    private LocalDateTime createdDate;

    @Indexed
    private String createdById;

    public CreatedInfo() {
    }

    public CreatedInfo(LocalDateTime createdDate, String createdById) {
        this.createdDate = createdDate;
        this.createdById = createdById;
    }

    public CreatedInfo(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

}
