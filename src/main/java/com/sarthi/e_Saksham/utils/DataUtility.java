package com.sarthi.e_Saksham.utils;

import com.sarthi.e_Saksham.domain.LoggedInUser;
import com.sarthi.e_Saksham.domain.RequestContext;
import com.sarthi.e_Saksham.entity.user.UserMstEntity;
import com.sarthi.e_Saksham.enumeration.Gender;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Objects;
import java.util.UUID;

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

    public static String getClientIdFromRequest() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (Objects.nonNull(requestAttributes)) {
            HttpServletRequest request = requestAttributes.getRequest();
            return request.getParameter("client_id");
        }
        return null;
    }

    public static LoggedInUser getDefaultSystemLoggedInUser() {
        return new LoggedInUser(
                0L,
                "e-Saksham-system"
        );
    }

    public static UserMstEntity getDefaultUserMstEntity() {
        UserMstEntity userMstEntity = new UserMstEntity();
        userMstEntity.setEsakshamId(UUID.randomUUID().toString());
        userMstEntity.setUserName("e-Saksham-user");
        userMstEntity.setFirstName("User");
        userMstEntity.setMiddleName("User");
        userMstEntity.setLastName("User");
        userMstEntity.setEmail("user@esaksham.com");
        userMstEntity.setPassword("$2a$12$rNGxswI6DMc8LcxL.RovQu2X3cLpk7Wd.SVi8qLyGGoRC4.aoxtku"); // ESaksham@User#123
        userMstEntity.setMobileNo("9999999998");
        userMstEntity.setGender(Gender.MALE.name());
        userMstEntity.setDateOfBirth(Date.valueOf("2000-05-25"));
        userMstEntity.setRegistrationTimestamp(new Timestamp(System.currentTimeMillis()));
        userMstEntity.setLoginAttempts(0);
        userMstEntity.setQrCodeSecret(StringUtils.EMPTY);
        userMstEntity.setMfa(false);
        userMstEntity.setEnabled(true);
        userMstEntity.setAccountNonLocked(true);
        userMstEntity.setAccountNonExpired(true);
        return userMstEntity;
    }

}
