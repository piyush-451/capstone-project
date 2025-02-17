package com.Capstone.Ecommerce.User;

import com.Capstone.Ecommerce.Exceptions.EmailNotUniqueDBException;
import com.Capstone.Ecommerce.Exceptions.ResourceNotFoundException;
import com.Capstone.Ecommerce.Jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepo userRepo;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authManager;
    private final JwtService jwtService;

    public String register(RegistrationRequest request) {
        User newUser = userMapper.toUser(request);
        newUser.setPassword(passwordEncoder.encode(request.password()));
        try{
            return userRepo.save(newUser).getId().toString();
        }
        catch (Exception e){
            throw new EmailNotUniqueDBException("User with given email already exists");
        }
    }

    public String login(LoginRequest request) {
        try {
            Authentication authentication = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.email(), request.password()));

            if (authentication.isAuthenticated()) {
                User user = userRepo.findByEmail(request.email())
                        .orElseThrow(() -> new BadCredentialsException("User not found, please try again."));
                return jwtService.generateToken(user);
            } else {
                throw new BadCredentialsException("Bad Credentials. Please try again.");
            }
        }
        catch (Exception e){
            throw new BadCredentialsException("Bad Credentials. Please try again.");
        }

    }

    public UserResponse getUserById(Long id) {
        User user = userRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with given id do not exist"));

        return userMapper.toUserResponse(user);
    }

    public List<UserResponse> getAllUsers() {
        List<User> users = userRepo.findAll();
        return users.stream()
                .map(userMapper::toUserResponse)
                .toList();

    }
}
