package com.biqasoft.entity.customer;

import com.biqasoft.entity.analytics.UTMAllMetricInfo;
import com.biqasoft.entity.core.BaseClass;
import com.biqasoft.entity.datasources.CacheableMetaInfo;
import com.biqasoft.entity.datasources.IndicatorsDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.javers.core.metamodel.annotation.DiffIgnore;
import org.springframework.data.mongodb.core.index.Indexed;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class LeadGenProject extends BaseClass {

    @Indexed
    private String leadGenMethodId;

    @Indexed
    private boolean active = true;

    @Indexed
    private Date startDate;
    @Indexed
    private Date finalDate;

    private double plannedCosts = 0;
    private double plannedDeals = 0;

    private List<UTMAllMetricInfo> utm_metrics = new ArrayList<>();

    private List<String> telephones = new ArrayList<>();
    private List<String> promoCodes = new ArrayList<>();

    private List<Integer> yandexDirectCompaignsIds = new ArrayList<>();
    private List<String> costsIDs = new ArrayList<>();

    @DiffIgnore
    private CacheableMetaInfo cachedKPIsMetaInfo = new CacheableMetaInfo();
    @DiffIgnore
    private IndicatorsDTO cachedKPIsData = new IndicatorsDTO();

}
