/*
* Copyright (c) 2016 biqasoft.com




 */

package com.biqasoft.entity.datasources;

import com.biqasoft.entity.core.CreatedInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
@ApiModel("This is saved KPIs for leadGenMethod and leadGenProject")
public class SavedLeadGenKPI {

    @Id
    @ApiModelProperty("Global object ID")
    protected String id = new ObjectId().toString();

    private IndicatorsDTO cachedKPIsData = new IndicatorsDTO();
    private CreatedInfo createdInfo = new CreatedInfo();

    @Indexed
    private String leadGenMethodId = null;
    @Indexed
    private String leadGenProjectId = null;

}
