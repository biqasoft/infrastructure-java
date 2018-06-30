/*
* Copyright (c) 2016 biqasoft.com




 */

package com.biqasoft.entity.filters;

import com.biqasoft.entity.core.BaseClassFilter;
import com.biqasoft.entity.tasks.UserAccountPredicate;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class TaskFilter extends BaseClassFilter {

    private String name;
    private boolean onlyDone;
    private boolean onlyActive;

    private UserAccountPredicate responsibles;

    //////////////////////////////////////////
    private boolean useCompletedDateFrom;
    private boolean useCompletedDateTo;

    private String completedDateFrom;
    private String completedDateTo;
//////////////////////////////////////////

    private boolean useConnectedCustomerId;
    private String connectedCustomerId;

    @ApiModelProperty("show only when current user is responsible for task")
    private boolean showOnlyWhenIamResponsible;

    @ApiModelProperty("show only when priority MORE or equals some integer [0-6] ")
    private boolean usePriorityMoreThan = false;
    private int priorityMoreThan;

    /////////////////////////////////////////////
    @ApiModelProperty("show only when priority LESS or equals some integer [0-6] ")
    private boolean usePriorityLessThan = false;
    private int priorityLessThan;
    /////////////////////////////////////////////

    @ApiModelProperty("show only Favourite tasks ")
    private boolean onlyFavourite = false;

}
