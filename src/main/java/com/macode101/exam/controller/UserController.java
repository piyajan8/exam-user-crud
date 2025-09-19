package com.macode101.exam.controller;

import com.macode101.exam.model.UserDto;
import com.macode101.exam.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        log.debug("Retrieving all users");
        List<UserDto> users = userService.getAllUsers();
        log.debug("Retrieved {} users", users.size());
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        log.debug("Retrieving user with ID: {}", id);
        UserDto user = userService.getUserById(id);
        log.debug("Successfully retrieved user: {}", user.getUsername());
        return ResponseEntity.ok(user);
    }

    @PostMapping
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto) {
        log.debug("Creating new user with username: {}", userDto.getUsername());
        
        if (userDto.getId() != null) {
            log.warn("Attempt to create user with non-null ID: {}", userDto.getId());
            userDto.setId(null);
        }
        
        UserDto createdUser = userService.createUser(userDto);
        log.info("Successfully created user with ID: {} and username: {}", createdUser.getId(), createdUser.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserDto userDto
    ) {
        log.debug("Updating user with ID: {} and username: {}", id, userDto.getUsername());
        if (userDto.getId() != null && !userDto.getId().equals(id)) {
            log.warn("ID mismatch - Path ID: {}, DTO ID: {}", id, userDto.getId());
        }
        userDto.setId(id);
        
        UserDto updatedUser = userService.updateUser(id, userDto);
        log.info("Successfully updated user with ID: {} and username: {}", updatedUser.getId(), updatedUser.getUsername());
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        log.debug("Deleting user with ID: {}", id);
        userService.deleteUser(id);
        log.info("Successfully deleted user with ID: {}", id);
        return ResponseEntity.noContent().build();
    }
}
