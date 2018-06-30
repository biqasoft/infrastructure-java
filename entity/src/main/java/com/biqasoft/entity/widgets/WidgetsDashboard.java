/*
* Copyright (c) 2016 biqasoft.com




 */

package com.biqasoft.entity.widgets;

import com.biqasoft.entity.core.BaseClass;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class WidgetsDashboard extends BaseClass {

    @DBRef
    private List<Widget> widgets = new ArrayList<>();

    @Indexed
    private String ownerId;

}
