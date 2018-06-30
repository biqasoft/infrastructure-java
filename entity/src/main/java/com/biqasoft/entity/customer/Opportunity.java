/*
 * Copyright (c) 2016 biqasoft.com




 */

package com.biqasoft.entity.customer;

import com.biqasoft.entity.payments.Payment;
import com.biqasoft.entity.system.ConnectedInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.index.Indexed;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
public class Opportunity extends Payment {

    @Indexed
    private int priority;

    @Indexed
    private boolean active;

    private Date startDate;

    private Date finalDate;

    private ConnectedInfo connectedInfo = new ConnectedInfo();

}
