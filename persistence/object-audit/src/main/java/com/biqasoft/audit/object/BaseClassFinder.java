package com.biqasoft.audit.object;

import com.biqasoft.entity.core.BaseClass;

/**
 * Created by Nikita on 9/13/2016.
 */
public interface BaseClassFinder {

    boolean canFind(Class<?> classToFind, String database);
    <T extends BaseClass> T findClass(T objectToFind, String database);
    <T extends BaseClass> String forceChangeDatabaseForJavers(T objectToFind, String database);

}
