package com.sarthi.e_Saksham.service.user;

import com.sarthi.e_Saksham.entity.user.UserMstEntity;
import com.sarthi.e_Saksham.enumeration.UserLoginType;

public interface UserService {

    UserMstEntity getUserMstEntityByUserName(String userName);

    UserMstEntity updateLoginAttempt(String userName, UserLoginType userLogin);

}
