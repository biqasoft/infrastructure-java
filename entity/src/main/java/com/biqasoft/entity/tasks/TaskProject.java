/*
* Copyright (c) 2016 biqasoft.com




 */

package com.biqasoft.entity.tasks;

import com.biqasoft.entity.core.BaseClass;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class TaskProject extends BaseClass {

    private List<Task> tasks = new ArrayList<>();

}
