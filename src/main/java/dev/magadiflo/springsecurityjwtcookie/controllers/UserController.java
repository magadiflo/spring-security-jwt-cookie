package dev.magadiflo.springsecurityjwtcookie.controllers;

import dev.magadiflo.springsecurityjwtcookie.dtos.UserRequest;
import dev.magadiflo.springsecurityjwtcookie.dtos.UserResponse;
import dev.magadiflo.springsecurityjwtcookie.models.UserInfo;
import dev.magadiflo.springsecurityjwtcookie.repositories.IUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@AllArgsConstructor
@RestController
@RequestMapping(path = "/api/v1/users")
public class UserController {

    // Por simplicidad usaré directamente el repositorio, pero se debería usar un Service.
    private final IUserRepository userRepository;

    @GetMapping
    public ResponseEntity<Iterable<UserInfo>> getAllUsers() {
        try {
            Iterable<UserInfo> userResponses = userRepository.findAll();
            return ResponseEntity.ok(userResponses);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping
    public ResponseEntity<UserResponse> saveUser(@RequestBody UserRequest userRequest) {
        if (userRequest.getUsername() == null) {
            throw new RuntimeException("Parameter username is not found in request..!!");
        } else if (userRequest.getPassword() == null) {
            throw new RuntimeException("Parameter password is not found in request..!!");
        }

        UserInfo savedUser = null;

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = userRequest.getPassword();
        String encodedPassword = encoder.encode(rawPassword);

        UserInfo user = new UserInfo();
        user.setId(userRequest.getId());
        user.setUsername(userRequest.getUsername());
        user.setPassword(encodedPassword);
        user.setRoles(userRequest.getRoles());

        if (userRequest.getId() != null) {
            Optional<UserInfo> optional = userRepository.findFirstById(userRequest.getId());
            if (optional.isPresent()) {
                UserInfo oldUser = optional.get();

                oldUser.setId(user.getId());
                oldUser.setPassword(user.getPassword());
                oldUser.setUsername(user.getUsername());
                oldUser.setRoles(user.getRoles());

                savedUser = userRepository.save(oldUser);
            } else {
                throw new RuntimeException("Can't find record with identifier: " + userRequest.getId());
            }
        } else {
            savedUser = this.userRepository.save(user);
        }

        UserResponse userResponse = new UserResponse();
        userResponse.setId(savedUser.getId());
        userResponse.setUsername(savedUser.getUsername());
        userResponse.setRoles(savedUser.getRoles());

        return ResponseEntity.status(HttpStatus.CREATED).body(userResponse);
    }


}
