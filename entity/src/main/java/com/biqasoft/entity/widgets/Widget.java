/*
* Copyright (c) 2016 biqasoft.com




 */

package com.biqasoft.entity.widgets;

import com.biqasoft.entity.datasources.DataSource;
import com.biqasoft.entity.core.BaseClass;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.index.Indexed;
import com.biqasoft.entity.filters.DataSourceFilter;
import com.biqasoft.entity.system.NameValueMap;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class Widget extends BaseClass {

    private String jsExec;
    private String htmlTemplate;

    @Indexed
    private String type;

    /**
     * {@link com.biqasoft.entity.constants.WIDGET_LOCATION}
     */
    @Indexed
    private String location; // место где находится

    /**
     * {@link com.biqasoft.entity.constants.WIDGET_CONTROLLERS}
     */
    @Indexed
    private String templateController;

    @Indexed
    private String ownerId;

    private boolean enabled = true;

    private int row;
    private int col;
    private int sizeX;
    private int sizeY;

    private boolean demo;

    private DataSourceFilter biqaClassBuilderAbstract;
    private List<NameValueMap> additionalData = new ArrayList<>();
    private List<DataSource> dataSources = new ArrayList<>();

}
