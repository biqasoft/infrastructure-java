/*
* Copyright (c) 2016 biqasoft.com




 */

package com.biqasoft.entity.tasks;

import com.biqasoft.entity.core.BaseClass;
import com.biqasoft.entity.system.ConnectedInfo;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bson.types.ObjectId;
import org.javers.core.metamodel.annotation.Value;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.TextIndexed;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(value = "This is TASK object", discriminator = "One of the most frequent used class")
public class Task extends BaseClass {

    @ApiModelProperty("Date when task was completed")
    private Date completedDate;

    @ApiModelProperty("Array of checklists")
    private List<CheckListItem> checkListItems = new ArrayList<>();

    @Indexed
    private boolean completed;

    @Indexed
    private boolean systemIssued;

    @Indexed
    private boolean favourite;

    @Indexed
    private int priority;

    @Indexed
    private String taskProjectID;

    @Indexed
    private Date startDate;
    @Indexed
    private Date finalDate;

    @Indexed
    private UserAccountPredicate responsibles;

    @TextIndexed
    private ConnectedInfo connectedInfo = new ConnectedInfo();

    @Data
    @Value
    @ApiModel("Check list for task")
    public static class CheckListItem {

        private String id = new ObjectId().toString();

        @ApiModelProperty("Name for check list")
        @TextIndexed
        private String name;

        @ApiModelProperty("Does check list item done")
        private boolean done;

    }
}
