/*
* Copyright (c) 2016 biqasoft.com




 */

package com.biqasoft.entity.customer;

import com.biqasoft.entity.core.BaseClass;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import com.biqasoft.entity.filters.CustomerFilter;

import java.util.Date;

@Document
@Data
@ApiModel(description = "This is dynamic segment with customer builder")
public class DynamicSegment extends BaseClass {

    private CustomerFilter customerBuilder;


    @ApiModelProperty(" limit this segments with pagination\n" +
            " WARNINGS: this can exclude some customers\n" +
            " and you will see only some PART\n" +
            " of customers")
    private boolean usePagination;

    private Date lastUpdate = null;
    private Long lastUpdateNumber = 0L;

}
