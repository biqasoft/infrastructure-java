/*
* Copyright (c) 2016 biqasoft.com




 */

package com.biqasoft.entity.system;

import com.biqasoft.entity.constants.DOCUMENT_FILE;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.javers.core.metamodel.annotation.Value;
import org.springframework.data.mongodb.core.index.Indexed;

import java.util.ArrayList;
import java.util.List;

@Data
@Value
@ApiModel("Some other documents that are connected with this record")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ConnectedInfo {

    /**
     * {@link DOCUMENT_FILE}
     */
    @ApiModelProperty("Does file connected with `TASK`, `CUSTOMER` ...")
    @Indexed
    private String connectedType = null;

    @ApiModelProperty("Connected customer ID")
    @Indexed
    private String connectedCustomerId = null;

    @ApiModelProperty("Connected task ID")
    @Indexed
    private String connectedTaskId = null;

    @ApiModelProperty("Connected opportunity ID")
    @Indexed
    private String connectedOpportunityId = null;

    @ApiModelProperty(" IDs of files")
    @Indexed
    private List<String> connectedFiles = new ArrayList<>();

}
