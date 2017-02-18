/*
 * Copyright (c) 2016. com.biqasoft
 */

package com.biqasoft.datasource.kpi;

import com.biqasoft.entity.datasources.DataSource;
import org.springframework.stereotype.Service;
import com.biqasoft.entity.constants.DATA_SOURCES_RETURNED_TYPES;

/**
 * Created by Nikita Bakaev, ya@nbakaev.ru on 4/11/2016.
 * All Rights Reserved
 */
@Service
public class DataSourceValueResolverService {

    public Object getResolvedDataSource(DataSource dataSource) {
        if (dataSource == null) return null;

        if (dataSource.getReturnType() == null) return null;
        if (dataSource.getValues() == null) return null;

        Object returnValue = null;

        switch (dataSource.getReturnType()) {
            case DATA_SOURCES_RETURNED_TYPES.INTEGER:
                returnValue = dataSource.getValues().getIntVal();
            break;

            case DATA_SOURCES_RETURNED_TYPES.DOUBLE:
                returnValue = dataSource.getValues().getDoubleVal();
            break;

            case DATA_SOURCES_RETURNED_TYPES.STRING:
                returnValue = dataSource.getValues().getStringVal();
            break;

            default:
                returnValue = null;
        }

        return returnValue;
    }

}
