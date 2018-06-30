/*
* Copyright (c) 2016 biqasoft.com




 */

package com.biqasoft.entity.tasks;

import lombok.Data;
import org.javers.core.metamodel.annotation.Value;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@Value
public class UserAccountPredicate {

    /**
     * see {@link UserAccount}
     */
    private List<String> userAccountsIDs = new ArrayList<>();

    /**
     * see {@link UserAccountGroup}
     */
    private List<String> userGroupsIDs = new ArrayList<>();

}
