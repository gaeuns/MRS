package com.example.mrs.dto;

import com.example.mrs.entity.User;
import lombok.Data;

@Data
public class UserUpdateDTO {
    private String userName;
    private String email;
    private String currentPassword;
    private String newPassword;
    private String confirmPassword;

    public static UserUpdateDTO fromEntity(User user) {
        UserUpdateDTO dto = new UserUpdateDTO();
        dto.setUserName(user.getUserName());
        dto.setEmail(user.getUserEmail());
        return dto;
    }
}
