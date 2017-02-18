/*
 * Copyright (c) 2016. com.biqasoft
 */

package com.biqasoft.audit.object.customfield;

import com.biqasoft.audit.object.BiqaClassService;
import com.biqasoft.common.exceptions.InvalidStateException;
import com.biqasoft.entity.core.BaseClass;
import com.biqasoft.entity.core.DomainSettings;
import com.biqasoft.entity.core.objects.CustomField;
import com.biqasoft.microservice.common.MicroserviceDomainSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Process {@link CustomField} for {@link BaseClass}
 */
@Service
public class BiqaObjectsCustomFieldProcessorService {

    private final CustomFieldProcessingService customFieldProcessingRepository;
    private final MicroserviceDomainSettings microserviceDomainSettings;
    private final BiqaClassService biqaClassService;

    @Autowired
    public BiqaObjectsCustomFieldProcessorService(MicroserviceDomainSettings microserviceDomainSettings, CustomFieldProcessingService customFieldProcessingRepository, BiqaClassService biqaClassService) {
        this.microserviceDomainSettings = microserviceDomainSettings;
        this.customFieldProcessingRepository = customFieldProcessingRepository;
        this.biqaClassService = biqaClassService;
    }

    /**
     * Update {@link CustomField} for {@link BaseClass} objects
     *
     * @param domainSettings new domainSettings
     * @return
     */
    public DomainSettings parseCustomFields(DomainSettings domainSettings) {
        DomainSettings oldDomainSettings = microserviceDomainSettings.findDomainSetting();

        if (oldDomainSettings.equals(domainSettings)) return domainSettings;

        Map<String, Class<? extends BaseClass>> classesExtendsBiqaAbstract = biqaClassService.getBiqaClasses();

        for (Map.Entry<String, Class<? extends BaseClass>> entry : classesExtendsBiqaAbstract.entrySet()) {
            String key = entry.getKey();
            customFieldProcessingRepository.processFields(oldDomainSettings.getDefaultCustomFields().get(key),
                                                          domainSettings.getDefaultCustomFields().get(key),
                                                          key);
        }

        return domainSettings;
    }

    /**
     * set default {@link CustomField} for {@link BaseClass} objects
     * This function is called before adding new object to database
     *
     * @param baseClass
     * @param domain
     * @param <T>
     * @return
     */
    public <T extends BaseClass> T setDefaultCustomFields(T baseClass, String domain) {
        DomainSettings domainSettings = microserviceDomainSettings.unsafeFindDomainSettingsById(domain);
        if (domainSettings == null) throw new InvalidStateException("ERROR DOMAIN GET");

        List<CustomField> customFields = domainSettings.getCustomFieldForClass(biqaClassService.getName(baseClass.getClass()));

        baseClass.setCustomFields(customFields);
        return baseClass;
    }

}
