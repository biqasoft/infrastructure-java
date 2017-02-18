/*
 * Copyright (c) 2016. com.biqasoft
 */

package com.biqasoft.audit.object;

import com.biqasoft.audit.object.customfield.BiqaObjectsCustomFieldProcessorService;
import com.biqasoft.entity.core.BaseClass;
import com.biqasoft.entity.core.CurrentUser;
import com.biqasoft.entity.core.GlobalStoredBaseClass;
import com.biqasoft.entity.core.objects.CustomField;
import com.biqasoft.entity.core.CreatedInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Validate and set default fields for {@link BaseClass}
 */
@Service
public class BiqaObjectValidatorRestService {

    private final CurrentUser currentUser;
    private final BiqaObjectsCustomFieldProcessorService biqaObjectsCustomFieldProcessorService;

    @Autowired
    public BiqaObjectValidatorRestService(CurrentUser currentUser, BiqaObjectsCustomFieldProcessorService biqaObjectsCustomFieldProcessorService) {
        this.currentUser = currentUser;
        this.biqaObjectsCustomFieldProcessorService = biqaObjectsCustomFieldProcessorService;
    }

    /**
     * Set default {@link CustomField} for entity
     *
     * @param baseClass entity object
     * @param <T>
     * @return
     */
    public <T extends BaseClass> T setDefaultCustomFields(T baseClass) {
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
    public <T extends BaseClass> T checkAndSetDefaultBiqa(T biqaClassObject, boolean forceAddCustomField) {
        if (biqaClassObject instanceof GlobalStoredBaseClass){
            ((GlobalStoredBaseClass) biqaClassObject).setDomain(currentUser.getDomain().getDomain());
        }

        // if object has not own created info - set current user as creator and current time
        if (biqaClassObject.getCreatedInfo() == null ||
                (biqaClassObject.getCreatedInfo() != null &&
                        (biqaClassObject.getCreatedInfo().getCreatedById() == null &&
                                biqaClassObject.getCreatedInfo().getCreatedDate() == null))) {
            biqaClassObject.setCreatedInfo(new CreatedInfo(new Date(), currentUser.getCurrentUser().getId()));
        }

        if (forceAddCustomField) {
            setDefaultCustomFields(biqaClassObject);
        }

        return biqaClassObject;
    }

}
