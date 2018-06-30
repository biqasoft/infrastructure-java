/*
* Copyright (c) 2016 biqasoft.com




 */

package com.biqasoft.entity.customer;

import com.biqasoft.entity.annotations.BiqaDontOverrideField;
import com.biqasoft.entity.core.BaseClass;
import com.biqasoft.entity.datasources.CacheableMetaInfo;
import com.biqasoft.entity.datasources.IndicatorsDTO;
import com.biqasoft.entity.salesfunnel.SalesFunnel;
import com.biqasoft.entity.system.ConnectedInfo;
import lombok.Data;
import org.javers.core.metamodel.annotation.DiffIgnore;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class LeadGenMethod extends BaseClass {

    @Indexed
    private boolean active = true;

    @DBRef
    private SalesFunnel leadGenSalesFunnel = null;

    @DBRef
    private SalesFunnel leadConversionSalesFunnel = null;

    @DBRef
    private SalesFunnel accountManagementSalesFunnel = null;

    private List<LeadGenProject> leadGenProjects = new ArrayList<>();

    private Date startDate;
    private Date finalDate;

    private ConnectedInfo connectedInfo = new ConnectedInfo();

    @DiffIgnore
    @BiqaDontOverrideField
    private CacheableMetaInfo cachedKPIsMetaInfo = new CacheableMetaInfo();

    @DiffIgnore
    @BiqaDontOverrideField
    private IndicatorsDTO cachedKPIsData = new IndicatorsDTO();

}
