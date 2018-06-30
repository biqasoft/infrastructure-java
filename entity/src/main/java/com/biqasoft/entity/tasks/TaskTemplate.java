/*
* Copyright (c) 2016 biqasoft.com




 */

package com.biqasoft.entity.tasks;

import com.biqasoft.entity.core.BaseClass;
import com.biqasoft.entity.filters.TaskFilter;
import io.swagger.annotations.ApiModel;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@ApiModel(description = "This is dynamic segment with customer builder")
public class TaskTemplate extends BaseClass {

    private TaskFilter taskBuilder;

    public TaskFilter getTaskBuilder() {
        return taskBuilder;
    }

    public void setTaskBuilder(TaskFilter taskBuilder) {
        this.taskBuilder = taskBuilder;
    }
}
