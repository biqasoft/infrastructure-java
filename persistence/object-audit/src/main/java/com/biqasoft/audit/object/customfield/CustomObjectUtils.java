/*
 * Copyright 2016 the original author or authors.
 */

package com.biqasoft.audit.object.customfield;

import com.biqasoft.common.exceptions.InvalidStateException;
import com.biqasoft.entity.core.objects.CustomObjectData;
import org.springframework.util.StringUtils;

/**
 * @author Nikita Bakaev, ya@nbakaev.ru
 *         Date: 8/8/2016
 *         All Rights Reserved
 */
public class CustomObjectUtils {
    // every type of custom object have `collectionId`
    // this is ID of custom object template object (`CustomObjectDto` collection)
    // and every type of objects is stored in separate collection in database
    public static final String CUSTOM_OBJECT_COLLECTION_PREFIX = "co_";

    public static String getCollectionNameFromCustomObject(CustomObjectData customObject) {
        if (StringUtils.isEmpty(customObject.getCollectionId())) {
            throw new InvalidStateException("EVERY CUSTOM OBJECT SHOULD HAVE COLLECTION_ID");
        }

        return CUSTOM_OBJECT_COLLECTION_PREFIX + customObject.getCollectionId();
    }
}
