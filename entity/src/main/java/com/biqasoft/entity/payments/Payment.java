/*
 * Copyright (c) 2016 biqasoft.com




 */

package com.biqasoft.entity.payments;

import com.biqasoft.entity.core.BaseClass;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.index.Indexed;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class Payment extends BaseClass {

    protected BigDecimal amount = new BigDecimal("0");

    @Indexed
    protected String leadGenProjectId = null;

    @Indexed
    protected String leadGenMethodId = null;

    @Indexed
    protected boolean dynamicControlled = false;

}
