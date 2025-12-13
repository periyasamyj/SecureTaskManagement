package com.Application.SecureTaskManagement.service;

import com.Application.SecureTaskManagement.dto.Response;
import com.Application.SecureTaskManagement.dto.UserRequest;
import com.Application.SecureTaskManagement.entity.User;

public interface UserService {

    Response<?> signUp(UserRequest userRequest);
    Response<?> login(UserRequest userRequest);
    User getCurrentLoggedInUser();

}
