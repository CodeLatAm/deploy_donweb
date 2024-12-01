package com.abel.sistema_gestion.mapper;

import com.abel.sistema_gestion.dto.user.UserBasicInfoResponse;
import com.abel.sistema_gestion.dto.user.UserStatusResponse;
import com.abel.sistema_gestion.dto.user.UserUpdateRequest;
import com.abel.sistema_gestion.entity.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    public UserBasicInfoResponse mapToBasicUserResponse(User user) {
        return UserBasicInfoResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .username(user.getUsername())
                .startOfActivity(user.getStartOfActivity().toString())
                .companyName(user.getCompanyName())
                .userStatus(user.getUserStatus())
                .build();
    }

    public UserStatusResponse mapToUserStatusResponse(User user) {
        return UserStatusResponse.builder()
                .userStatus(user.getUserStatus().name())
                .build();
    }


    public List<UserBasicInfoResponse> mapToListUsers(List<User> users) {
        return users.stream().map(this::mapToBasicUserResponse).collect(Collectors.toList());
    }


}
