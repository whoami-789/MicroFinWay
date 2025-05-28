package com.MicroFinWay.controller;

import com.MicroFinWay.dto.UserDTO;
import com.MicroFinWay.model.User;
import com.MicroFinWay.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
}

