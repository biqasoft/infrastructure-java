package com.biqasoft.audit.object.mocks;

import com.biqasoft.entity.annotations.BiqaAddObject;
import com.biqasoft.entity.customer.Customer;
import com.biqasoft.entity.tasks.Task;
import org.springframework.stereotype.Service;

/**
 * Created by Nikita on 9/7/2016.
 */
@Service
public class CoolService {

    @BiqaAddObject(forceAddCustomField = false)
    public TestBaseClass testAddBiqaClass(TestBaseClass testBiqaClass){
        return testBiqaClass;
    }

    @BiqaAddObject
    public Customer testAddCustomer(Customer customer){
        return customer;
    }

    @BiqaAddObject
    public Task testAddTask(Task customer){
        return customer;
    }

}
