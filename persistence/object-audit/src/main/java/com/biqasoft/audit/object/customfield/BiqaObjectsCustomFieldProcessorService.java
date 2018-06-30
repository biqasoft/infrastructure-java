/*
 * Copyright (c) 2016. com.biqasoft
 */

package com.biqasoft.audit.object.customfield;

import com.biqasoft.audit.object.BiqaClassService;
import com.biqasoft.auth.CurrentUserContextProvider;
import com.biqasoft.common.exceptions.InvalidStateException;
import com.biqasoft.entity.core.BaseClass;
import com.biqasoft.entity.core.objects.CustomField;
import com.biqasoft.microservice.common.MicroserviceDomainSettings;
import com.biqasoft.microservice.common.dto.DomainSettingsDto;
import com.biqasoft.microservice.common.dto.core.objects.CustomFieldDto;
import com.fasterxml.jackson.core.type.TypeReference;
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
    private final MicroserviceDomainSettings microserviceDomainSettingsDto;
    private final BiqaClassService biqaClassService;

    @Autowired
    public BiqaObjectsCustomFieldProcessorService(MicroserviceDomainSettings microserviceDomainSettingsDto, CustomFieldProcessingService customFieldProcessingRepository, BiqaClassService biqaClassService) {
        this.microserviceDomainSettingsDto = microserviceDomainSettingsDto;
        this.customFieldProcessingRepository = customFieldProcessingRepository;
        this.biqaClassService = biqaClassService;
    }

    /**
     * Update {@link CustomField} for {@link BaseClass} objects
     *
     * @param DomainSettingsDto new DomainSettingsDto
     * @return
     */
    public DomainSettingsDto parseCustomFields(DomainSettingsDto DomainSettingsDto, CurrentUserContextProvider currentUserContextProvider) {
        DomainSettingsDto oldDomainSettingsDto = microserviceDomainSettingsDto.findDomainSetting();

        if (oldDomainSettingsDto.equals(DomainSettingsDto)) return DomainSettingsDto;

        Map<String, Class<? extends BaseClass>> classesExtendsBiqaAbstract = biqaClassService.getBiqaClasses();

        for (Map.Entry<String, Class<? extends BaseClass>> entry : classesExtendsBiqaAbstract.entrySet()) {
            String key = entry.getKey();
            customFieldProcessingRepository.processFields(oldDomainSettingsDto.getDefaultCustomFields().get(key),
                    DomainSettingsDto.getDefaultCustomFields().get(key),
                    key, currentUserContextProvider);
        }

        return DomainSettingsDto;
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
        DomainSettingsDto DomainSettingsDto = microserviceDomainSettingsDto.unsafeFindDomainSettingsById(domain);
        if (DomainSettingsDto == null) throw new InvalidStateException("ERROR DOMAIN GET");

        List<CustomFieldDto> customFields = DomainSettingsDto.getCustomFieldForClass(biqaClassService.getName(baseClass.getClass()));

        baseClass.setCustomFields(com.biqasoft.common.utils.ObjectUtils.clone(customFields, new TypeReference<List<CustomField>>() {
        }));
        return baseClass;
    }

}
