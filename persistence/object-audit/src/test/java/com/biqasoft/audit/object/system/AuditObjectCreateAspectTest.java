///*
// * Copyright (c) 2016. com.biqasoft
// */
//
//package com.biqasoft.audit.object.system;
//
//import com.biqasoft.audit.object.BiqaClassService;
//import com.biqasoft.audit.object.mocks.CoolService;
//import com.biqasoft.audit.object.StartApplication;
//import com.biqasoft.audit.object.mocks.TestBaseClass;
//import com.biqasoft.entity.customer.Customer;
//import com.biqasoft.entity.tasks.Task;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
//import org.springframework.test.context.web.WebAppConfiguration;
//import org.testng.annotations.Test;
//
//import static org.mockito.Matchers.any;
//import static org.testng.Assert.assertEquals;
//import static org.testng.Assert.assertNotNull;
//import static org.testng.Assert.assertTrue;
//
///**
// * Created by Nikita Bakaev, ya@nbakaev.ru on 2/21/2016.
// * All Rights Reserved
// */
//@SpringBootTest(classes = StartApplication.class)
//@WebAppConfiguration
//@Test
//@ActiveProfiles({"development", "test"})
//@Configuration
//public class AuditObjectCreateAspectTest extends AbstractTestNGSpringContextTests {
//
//    @Autowired
//    private CoolService coolService;
//
//    @Autowired
//    private CurrentUser currentUser;
//
//    @Autowired
//    private BiqaClassService biqaClassService;
//
//    @Test
//    public void beforeObjectAddTestFromAnotherModule() {
//        TestBaseClass biqaClass = new TestBaseClass();
//        biqaClass.setCreatedInfo(null);
//
//        coolService.testAddBiqaClass(biqaClass);
//
//        assertNotNull(biqaClass.getCreatedInfo());
//        assertNotNull(biqaClass.getCreatedInfo().getCreatedDate());
//    }
//
//    @Test
//    public void testAddCustomFields() {
//        Customer customer = new Customer();
//        customer.setCreatedInfo(null);
//
////        Mockito.when(microserviceDomainSettings.unsafeFindDomainSettingsById(any())).thenReturn(currentUser.getCurrentUserDomain());
//
//        coolService.testAddCustomer(customer);
//
//        assertNotNull(customer.getCreatedInfo());
//        assertNotNull(customer.getCreatedInfo().getCreatedDate());
//        assertNotNull(customer.getCustomFields());
//        assertTrue(customer.getCustomFields().size() == 2);
//        assertEquals(customer.getCustomFields(), currentUser.getCurrentUserDomain().getCustomFieldForClass(biqaClassService.getName(customer.getClass())));
//    }
//
//    @Test
//    public void testAddCustomFields2() {
//        Task task = new Task();
//        task.setCreatedInfo(null);
//
//        coolService.testAddTask(task);
//
//        assertNotNull(task.getCreatedInfo());
//        assertNotNull(task.getCreatedInfo().getCreatedDate());
//        assertNotNull(task.getCustomFields());
//        assertEquals(task.getCustomFields(), currentUser.getCurrentUserDomain().getCustomFieldForClass(biqaClassService.getName(task.getClass())));
//    }
//}