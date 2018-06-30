/*
* Copyright (c) 2016 biqasoft.com




 */

package com.biqasoft.entity.datasources;

import com.biqasoft.entity.datasources.history.scratch.AbstractDataSourceHistoryItem;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.javers.core.metamodel.annotation.Value;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.math.BigInteger;

@EqualsAndHashCode(callSuper = true)
@Data
@Document
@Value
@ApiModel("This object is statistics for leadGenProject or leadGenMethod")
public class IndicatorsDTO extends AbstractDataSourceHistoryItem {

    private String type;

    private String indicatorID;

    @ApiModelProperty("Return On Investment")
    private BigDecimal ROI = new BigDecimal(BigInteger.ZERO);

    @ApiModelProperty("Amount of all deals")
    private BigDecimal dealsAmounts = new BigDecimal(BigInteger.ZERO);

    @ApiModelProperty("Amount of all costs")
    private BigDecimal costsAmount = new BigDecimal(BigInteger.ZERO);

    @ApiModelProperty("Leads number")
    private long leadsNumber;

    @ApiModelProperty("customer number")
    private long customersNumber;

    @ApiModelProperty("Deals number")
    private long dealsNumber;

    @ApiModelProperty("Costs number")
    private long costsNumber;

    @ApiModelProperty("Conversion (CV) from lead to customer")
    private BigDecimal conversionFromLeadToCustomer = new BigDecimal(BigInteger.ZERO);

    @ApiModelProperty("Lead cost - How much we pay for a Lead borrowed")
    private BigDecimal leadCost = new BigDecimal(BigInteger.ZERO);

    @ApiModelProperty("Lead cost - How much we pay for a Lead borrowed")
    private BigDecimal customerCost = new BigDecimal(BigInteger.ZERO);

    @ApiModelProperty("Average payment ( deal ) from customer")
    private BigDecimal averagePayment = new BigDecimal(BigInteger.ZERO);

    @ApiModelProperty("Formula: (all deals amount) / ( customer number )")
    private BigDecimal ltv;

    @ApiModelProperty("time between creating opportunity and transfer to deal IN SECONDS")
    private BigDecimal dealsCycle = new BigDecimal(BigInteger.ZERO);

}
