package com.Capstone.Ecommerce.User;

import com.Capstone.Ecommerce.Role.RoleRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

@Service
@RequiredArgsConstructor
public class UserMapper {
    private final RoleRepo roleRepo;
    public User toUser(RegistrationRequest request) {
        return User.builder()
                .username(request.username())
                .password(request.password())
                .email(request.email())
                .roles(new HashSet<>(Collections.singletonList(roleRepo.findByRolename("USER"))))
                .build();
    }

    public UserResponse toUserResponse(User user){
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail()
        );
    }
}
