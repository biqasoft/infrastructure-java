///*
// * Copyright (c) 2016. com.biqasoft
// */
//
//package com.biqasoft.audit.object.mocks.useraccount;
//
//import com.biqasoft.audit.object.BiqaClassService;
//import com.biqasoft.entity.constants.CUSTOM_FIELD_TYPES;
//import com.biqasoft.entity.core.Domain;
//import com.biqasoft.entity.core.DomainSettings;
//import com.biqasoft.entity.core.objects.CustomField;
//import com.biqasoft.entity.core.objects.field.DataSourcesTypes;
//import com.biqasoft.entity.customer.Customer;
//import com.biqasoft.users.domain.useraccount.UserAccount;
//import org.bson.types.ObjectId;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Primary;
//import org.springframework.context.annotation.Profile;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Service;
//
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
///**
// * Created by Nikita Bakaev, ya@nbakaev.ru on 2/21/2016.
// * All Rights Reserved
// */
//
//@Service
//@Primary
//@Profile("test")
//public class CurrentUserMock implements CurrentUser {
//    @Override
//    public UserAccount getCurrentUser() {
//        UserAccount userAccount = new UserAccount();
//        return userAccount;
//    }
//
//    private final BiqaClassService biqaClassService;
//
//    static String customStringId = new ObjectId().toString();
//
//    @Override
//    public DomainSettings getCurrentUserDomain() {
//        DomainSettings domainSettings = new DomainSettings();
//        domainSettings.setId(getDomain().getDomain());
//
//        List<CustomField> customerCustomFields = new ArrayList<>();
//
//        {
//            CustomField customField = new CustomField();
//            customField.setFieldId(customStringId);
//            customField.setName("Custom Field #1");
//            customField.setType(CUSTOM_FIELD_TYPES.STRING);
//            customField.setValue(new DataSourcesTypes("some string"));
//            customerCustomFields.add(customField);
//        }
//
//        {
//            CustomField customField = new CustomField();
//            customField.setFieldId(customStringId);
//            customField.setName("Custom Field #2");
//            customField.setType(CUSTOM_FIELD_TYPES.STRING);
//            customField.setValue(new DataSourcesTypes("some another string"));
//            customerCustomFields.add(customField);
//        }
//
//        domainSettings.setCustomFieldForClass(biqaClassService.getName(Customer.class), customerCustomFields);
//
//        return domainSettings;
//    }
//
//    @Override
//    public Domain getDomain() {
//        Domain domain = new Domain();
//        domain.setDomain("biqa_domain_test");
//        return domain;
//    }
//
//    @Override
//    public String printWithDateFormat(Date date) {
//        return null;
//    }
//
//    @Override
//    public String getLanguage() {
//        return DEFAULT_LANGUAGE;
//    }
//
//
//    @Autowired
//    public CurrentUserMock(BiqaClassService biqaClassService) {
//        this.biqaClassService = biqaClassService;
//    }
//
//    @Override
//    public boolean haveRole(String role) {
//        return SecurityContextHolder.getContext().getAuthentication().getAuthorities().contains(new SimpleGrantedAuthority(role));
//    }
//}
//
