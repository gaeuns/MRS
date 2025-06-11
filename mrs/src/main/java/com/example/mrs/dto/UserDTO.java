package com.example.mrs.dto;

import com.example.mrs.entity.User;
import com.example.mrs.entity.UserRole;
import lombok.Data;

@Data
public class UserDTO {
    private String userId;
    private String userName;
    private String email;
    private UserRole userRole;  // ✅ 추가된 필드

    public UserDTO(String userId, String userName, String email, UserRole userRole) {
        this.userId = userId;
        this.userName = userName;
        this.email = email;
        this.userRole = userRole;
    }

    public static UserDTO fromEntity(User user) {
        return new UserDTO(
                user.getUserId(),
                user.getUserName(),
                user.getUserEmail(),
                user.getUserRole() // ✅ 추가된 역할 정보
        );
    }
}
