package com.sarthi.e_Saksham.utils;

import com.sarthi.e_Saksham.domain.LoggedInUser;
import com.sarthi.e_Saksham.domain.RequestContext;

import java.util.Objects;

public final class DataUtility {

    private DataUtility() {
        throw new AssertionError("No com.sarthi.e_Saksham.utils.DataUtility instances for you!");
    }

    public static LoggedInUser getLoggedInUser() {
        LoggedInUser loggedInUser = RequestContext.getLoggedInUser();
        if (Objects.isNull(loggedInUser)) {
            return new LoggedInUser(0L, "e-Saksham-system");
        }
        return loggedInUser;
    }

}
