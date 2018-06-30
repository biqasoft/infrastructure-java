//package com.biqasoft.audit.object.mocks;
//
//import com.biqasoft.auth.CurrentUserContextProvider;
//import com.biqasoft.auth.core.DomainSettings;
//import com.biqasoft.microservice.common.MicroserviceDomainSettings;
//import com.biqasoft.microservice.common.dto.DomainSettingsDto;
//import com.biqasoft.microservice.communicator.interfaceimpl.annotation.MicroPathVar;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Primary;
//import org.springframework.stereotype.Service;
//
///**
// * Created by Nikita on 9/7/2016.
// */
//@Primary
//@Service
//public class MockedMicroserviceDomainSettings implements MicroserviceDomainSettings{
//
//    @Autowired
//    private CurrentUserContextProvider currentUser;
//
//    @Override
//    public DomainSettingsDto create(DomainSettingsDto domainSettings) {
//        return null;
//    }
//
//    @Override
//    public void unsafeDelete(@MicroPathVar("id") String domainSettings) {
//
//    }
//
//    @Override
//    public DomainSettingsDto findDomainSetting() {
//        return null;
//    }
//
//    @Override
//    public DomainSettingsDto unsafeUpdateDomainSettings(DomainSettingsDto domainSettings) {
//        return currentUser.;
//    }
//
//    @Override
//    public DomainSettingsDto updateDomainSettings(DomainSettingsDto domainSettings) {
//        return null;
//    }
//
//    @Override
//    public DomainSettingsDto unsafeFindDomainSettingsById(@MicroPathVar("id") String domainSettings) {
//        return currentUser.getCurrentUserDomain();
//    }
//}
