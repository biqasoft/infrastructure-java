/*
* Copyright (c) 2016 biqasoft.com




 */

package com.biqasoft.entity.customer;

import com.biqasoft.entity.core.BaseClass;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@EqualsAndHashCode(callSuper = true)
@Data
@Document
public class Company extends BaseClass {

    private String site;
    private String telephone;
    private String email;

    @Indexed
    private boolean partner;
    @Indexed
    private boolean client;

    @Indexed
    private String responsibleManagerID;

    @ApiModelProperty(notes = "is this customer active (true, false). Used to filter not relevant customers and leads")
    @Indexed
    private boolean active;

    @ApiModelProperty(notes = "is this customer important (true, false). Used to filter not relevant customers and leads")
    private boolean important;

}
