package com.example.mrs.dto;

import com.example.mrs.entity.User;
import lombok.Data;

@Data
public class UserDTO {
    private String userId;
    private String userName;
    private String email;

    public UserDTO(String userId, String userName, String email) {
        this.userId = userId;
        this.userName = userName;
        this.email = email;
    }

    public static UserDTO fromEntity(User user) {
        return new UserDTO(
                user.getUserId(),
                user.getUserName(),
                user.getUserEmail()
        );
    }
}
