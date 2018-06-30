/*
 * Copyright (c) 2016. com.biqasoft
 */

package com.biqasoft.audit.object;

import com.biqasoft.audit.object.customfield.BiqaObjectsCustomFieldProcessorService;
import com.biqasoft.auth.CurrentUserContextProvider;
import com.biqasoft.entity.core.BaseClass;
import com.biqasoft.entity.core.CreatedInfo;
import com.biqasoft.entity.core.GlobalStoredBaseClass;
import com.biqasoft.entity.core.objects.CustomField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Validate and set default fields for {@link BaseClass}
 */
@Service
public class BiqaObjectValidatorRestService {

    private final BiqaObjectsCustomFieldProcessorService biqaObjectsCustomFieldProcessorService;

    @Autowired
    public BiqaObjectValidatorRestService(BiqaObjectsCustomFieldProcessorService biqaObjectsCustomFieldProcessorService) {
        this.biqaObjectsCustomFieldProcessorService = biqaObjectsCustomFieldProcessorService;
    }

    /**
     * Set default {@link CustomField} for entity
     *
     * @param baseClass entity object
     * @param <T>
     * @return
     */
    public <T extends BaseClass> T setDefaultCustomFields(T baseClass, CurrentUserContextProvider currentUser) {
        biqaObjectsCustomFieldProcessorService.setDefaultCustomFields(baseClass, currentUser.getDomain().getDomain());
        return baseClass;
    }

    /**
     * Set default {@link CustomField} for entity and {@link CreatedInfo}
     * Primary used on saving object to database
     *
     * @param biqaClassObject     entity object
     * @param forceAddCustomField add {@link CustomField} to object or not
     * @param <T> processed object
     * @return
     */
    public <T extends BaseClass> T checkAndSetDefaultBiqa(T biqaClassObject, boolean forceAddCustomField, CurrentUserContextProvider currentUser) {
        if (biqaClassObject instanceof GlobalStoredBaseClass){
            ((GlobalStoredBaseClass) biqaClassObject).setDomain(currentUser.getDomain().getDomain());
        }

        // if object has not own created info - set current user as creator and current time
        if (biqaClassObject.getCreatedInfo() == null ||
                (biqaClassObject.getCreatedInfo() != null &&
                        (biqaClassObject.getCreatedInfo().getCreatedById() == null &&
                                biqaClassObject.getCreatedInfo().getCreatedDate() == null))) {
            biqaClassObject.setCreatedInfo(new CreatedInfo(new Date(), currentUser.getUserAccount().getId()));
        }

        if (forceAddCustomField) {
            setDefaultCustomFields(biqaClassObject, currentUser);
        }

        return biqaClassObject;
    }

}
