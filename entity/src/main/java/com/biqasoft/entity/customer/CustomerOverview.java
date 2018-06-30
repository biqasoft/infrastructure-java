/*
* Copyright (c) 2016 biqasoft.com




 */

package com.biqasoft.entity.customer;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.javers.core.metamodel.annotation.Value;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

@Data
@Value
@ApiModel("Some summary information about customer or lead")
public class CustomerOverview {

    @ApiModelProperty("Number of active tasks for customer")
    private long activeTaskNumber = 0;

    @ApiModelProperty("Amount of customer deals")
    private BigDecimal dealsAmount = new BigDecimal(BigInteger.ZERO);

    @ApiModelProperty("Average amount of customer deals")
    private BigDecimal averageDealsAmount = new BigDecimal(BigInteger.ZERO);

    @ApiModelProperty("Amount of customer opportunity")
    private BigDecimal opportunityAmount = new BigDecimal(BigInteger.ZERO);

    @ApiModelProperty("Average amount of customer opportunity")
    private BigDecimal averageOpportunityAmount = new BigDecimal(BigInteger.ZERO);

    @ApiModelProperty("Number of customer Opportunities")
    private long opportunityNumber = 0;

    @ApiModelProperty("Number of customer deals")
    private long dealsNumber = 0;

    @ApiModelProperty("Date when last deal was created")
    private Date lastDealDate;

    @ApiModelProperty("Date when last opportunity was created")
    private Date lastOpportunityDate;

}
