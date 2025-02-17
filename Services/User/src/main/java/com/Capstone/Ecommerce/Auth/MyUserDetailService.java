package com.Capstone.Ecommerce.Auth;

import com.Capstone.Ecommerce.Exceptions.UserNotPresentException;
import com.Capstone.Ecommerce.Role.Role;
import com.Capstone.Ecommerce.User.User;
import com.Capstone.Ecommerce.User.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MyUserDetailService implements UserDetailsService {
    private final UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user =  userRepo.findByEmail(username)
                .orElseThrow(() -> new UserNotPresentException("User not found"));

        Set<String> roles = user.getRoles().stream()
                .map(Role::getRolename)
                .collect(Collectors.toSet());

        return new MyUserDetails(user.getEmail(), user.getPassword(), roles);
    }
}
