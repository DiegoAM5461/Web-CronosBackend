package com.example.backend_integrador.mapper;

import com.example.backend_integrador.dto.UserDto;
import com.example.backend_integrador.entity.User;

public class UserMapper {
    
    public static UserDto mapToUserDto(User user) {
        return new UserDto(
            user.getUserId(),
            user.getUsername(),
            user.getPassword(),
            user.getUserEstado(),
            user.getUserRol(),
            user.getEmployee().getEmployeeId()
        );
    }

    public static User mapToUser(UserDto userDto) {
        User user = new User();
        user.setUserId(userDto.getUserId());
        user.setUsername(userDto.getUsername());
        user.setPassword(userDto.getPassword());
        user.setUserEstado(userDto.getUserEstado());
        user.setUserRol(userDto.getUserRol());
        return user;
    }
}
