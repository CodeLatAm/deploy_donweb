package com.abel.sistema_gestion.dto.user;

import com.abel.sistema_gestion.enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserBasicInfoResponse {
    private Integer id;
    private String name;
    private String username;
    private String startOfActivity;
    private String companyName;
    private UserStatus userStatus;
}
