package com.biqasoft.audit.object.aspect;

import com.biqasoft.auth.CurrentUserContextProvider;

import java.util.Arrays;
import java.util.Optional;

public abstract class BaseAspect {

    Optional<CurrentUserContextProvider> getAuthContextFromParams(Object[] params) {
        Optional first = Arrays.stream(params).filter(x -> x instanceof CurrentUserContextProvider).findFirst();
        return first;
    }

}
