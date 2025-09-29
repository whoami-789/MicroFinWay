package com.MicroFinWay.controller;

import com.MicroFinWay.dto.UserDTO;
import com.MicroFinWay.model.User;
import com.MicroFinWay.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * The UserController class is a REST controller that manages user-related operations.
 * It provides endpoints for creating and retrieving users.
 *
 * Mappings:
 * - Base Path: "/api/users"
 *
 * Dependencies:
 * - {@link UserService}: Service layer responsible for user-related business logic.
 *
 * Endpoints:
 * - POST "/api/users": Creates a new user with the given UserDTO.
 * - GET "/api/users/{id}": Retrieves the details of a user by their ID.
 */
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public UserDTO createUser(@RequestBody UserDTO dto) {
        return userService.createUser(dto);
    }

    @GetMapping("/{id}")
    public UserDTO getUser(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    // GET /api/users
    @GetMapping
    public List<User> getUsers(@RequestParam(required = false) String type) {
        if (type != null && !type.isBlank()) {
            return userService.getUsersByType(type);
        }
        return userService.getAllUsers();
    }
}

