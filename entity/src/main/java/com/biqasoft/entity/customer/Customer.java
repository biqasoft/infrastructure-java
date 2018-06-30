/*
* Copyright (c) 2016 biqasoft.com




 */

package com.biqasoft.entity.customer;

import com.biqasoft.entity.analytics.AnalyticRecord;
import com.biqasoft.entity.analytics.CustomerWebAnalytics;
import com.biqasoft.entity.annotations.BiqaDontOverrideField;
import com.biqasoft.entity.core.BaseClass;
import com.biqasoft.entity.salesfunnel.SalesFunnelStatus;
import com.biqasoft.entity.salesfunnel.SalesFunnelStatusWithoutDataSource;
import com.biqasoft.entity.system.ConnectedInfo;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.javers.core.metamodel.annotation.DiffIgnore;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(description = "This is customer or lead in CRM. Important object")
public class Customer extends BaseClass {

    @TextIndexed
    @ApiModelProperty(notes = "first name of client or customer")
    private String firstName;

    @TextIndexed(weight = 2)
    @ApiModelProperty(notes = "last name of client or customer")
    private String lastName;

    @TextIndexed
    @ApiModelProperty(notes = "patronymic of client or customer")
    private String patronymic;

    @TextIndexed
    @ApiModelProperty(notes = "email of client or customer")
    private String email;

    @ApiModelProperty(notes = "additional list of emails")
    private List<String> emails = new ArrayList<>();

    @ApiModelProperty(notes = "additional list of telephones")
    private List<String> telephones = new ArrayList<>();

    @TextIndexed
    @ApiModelProperty(notes = "main contact telephone of client or customer")
    private String telephone;

    @ApiModelProperty(notes = "is he/she is lead? (true/false). if this property is false, this means, that he/she didn't bought anything", required = true)
    @Indexed
    private boolean lead;

    @ApiModelProperty(notes = "is he/she is customer? (true/false). if this property is true, it means that he/she bought something", required = true)
    @Indexed
    private boolean customer;

    @ApiModelProperty(notes = "is this customer active (true, false). Used to filter not relevant customers and leads")
    @Indexed
    private boolean active;

    @ApiModelProperty(notes = "is this customer important (true, false). Used to filter not relevant customers and leads")
    @Indexed
    private boolean important;

    @ApiModelProperty(notes = "customer address")
    private String address = null;

    @ApiModelProperty(notes = "customer job position")
    private String position = null;

    @ApiModelProperty(notes = "is b2b customer", required = true)
    @Indexed
    private boolean b2b;

    @ApiModelProperty(notes = "19.09.2015 - this parameter is not used", required = false)
    private String sex;

    /**
     * {@link LeadGenMethod}
     */
    @ApiModelProperty(value = "lead gen method ID", required = true)
    @Indexed
    private String leadGenMethod = null;

    /**
     * {@link LeadGenProject}
     */
    @ApiModelProperty(value = "lead gen project ID", required = true)
    @Indexed
    private String leadGenProject = null;

    /**
     * {@link SalesFunnelStatus}
     */
    @ApiModelProperty(value = "current status of sales funnel for customer,lead", required = true)
    @Indexed
    private SalesFunnelStatusWithoutDataSource salesFunnelStatus;

    /**
     * {@link StaticSegment}
     */
    @ApiModelProperty(notes = "array of IDs of static segments")
    @Indexed
    private List<String> staticSegmentsIDs = new ArrayList<>();

    @ApiModelProperty(notes = "b2b company")
    @DBRef
    @Indexed
    private Company company;

    @DiffIgnore
    private AnalyticRecord arec;
    @DiffIgnore
    private CustomerWebAnalytics customerWebAnalytics;

    /**
     * ID of useraccount
     * {@link UserAccount}
     */
    @ApiModelProperty(notes = "ID of useraccount responsible manager")
    @Indexed
    private String responsibleManagerID;

    @ApiModelProperty("Connected Social networks of customer")
    private SocialNetworks socialNetworks;

    private ConnectedInfo connectedInfo = new ConnectedInfo();

    @DiffIgnore
    @BiqaDontOverrideField
    private CustomerOverview customerOverview = new CustomerOverview();

    @BiqaDontOverrideField
    @DiffIgnore
    @ApiModelProperty("Customer dynamic segments; Note: this is near to realtime data and updates in some minutes. " +
            "Segments(filters) that include `showOnlyWhenIamResponsible=true` are excluded. The last update data is `dynamicSegmentsLastDate` property of customer")
    private List<String> dynamicSegments = new ArrayList<>();

    @ApiModelProperty("Last update date of `dynamicSegments` field")
    @BiqaDontOverrideField
    @DiffIgnore
    private Date dynamicSegmentsLastDate;

}
