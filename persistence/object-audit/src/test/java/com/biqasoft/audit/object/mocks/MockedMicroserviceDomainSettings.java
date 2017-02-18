package com.biqasoft.audit.object.mocks;

import com.biqasoft.entity.core.CurrentUser;
import com.biqasoft.entity.core.DomainSettings;
import com.biqasoft.microservice.common.MicroserviceDomainSettings;
import com.biqasoft.microservice.communicator.interfaceimpl.annotation.MicroPathVar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

/**
 * Created by Nikita on 9/7/2016.
 */
@Primary
@Service
public class MockedMicroserviceDomainSettings implements MicroserviceDomainSettings{

    @Autowired
    private CurrentUser currentUser;

    @Override
    public DomainSettings create(DomainSettings domainSettings) {
        return null;
    }

    @Override
    public void unsafeDelete(@MicroPathVar("id") String domainSettings) {

    }

    @Override
    public DomainSettings findDomainSetting() {
        return null;
    }

    @Override
    public DomainSettings unsafeUpdateDomainSettings(DomainSettings domainSettings) {
        return currentUser.getCurrentUserDomain();
    }

    @Override
    public DomainSettings updateDomainSettings(DomainSettings domainSettings) {
        return null;
    }

    @Override
    public DomainSettings unsafeFindDomainSettingsById(@MicroPathVar("id") String domainSettings) {
        return currentUser.getCurrentUserDomain();
    }
}
