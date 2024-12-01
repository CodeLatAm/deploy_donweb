package com.abel.sistema_gestion.serviceimpl.service;

import com.abel.sistema_gestion.dto.user.UserBasicInfoResponse;
import com.abel.sistema_gestion.dto.user.UserStatusResponse;
import com.abel.sistema_gestion.dto.user.UserUpdateRequest;
import com.abel.sistema_gestion.entity.User;

import java.util.List;

public interface UserService {
    String getAuthenticatedUsername();

    User getUserByUserId(Integer userId);


    UserBasicInfoResponse getInfoBasicUserId(Integer userId);

    void save(User user);

    User getUserByEmail(String userEmail);

    List<User> getPremiumUsers();

    UserStatusResponse getStatus(Integer userId);

    List<UserBasicInfoResponse> getAllUsersByAdmin();

    Long countUsers();

    void update(Integer id, UserUpdateRequest request);
}
