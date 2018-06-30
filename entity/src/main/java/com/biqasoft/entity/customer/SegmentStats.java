/*
* Copyright (c) 2016 biqasoft.com




 */

package com.biqasoft.entity.customer;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author Nikita Bakaev, ya@nbakaev.ru
 * Date: 5/26/2015
 * All Rights Reserved
 */

@Data
@ApiModel
public class SegmentStats {

    private long leadsCount;
    private long customerCount;

    private BigDecimal dealsAmount = new BigDecimal(BigInteger.ZERO);
    private BigDecimal opportunityAmount = new BigDecimal(BigInteger.ZERO);

    private long dealsNumber;
    private long opportunityNumber;

}
