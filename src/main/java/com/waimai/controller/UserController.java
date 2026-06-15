package com.waimai.controller;

import com.waimai.common.UserRole;
import com.waimai.dto.ApiResponse;
import com.waimai.dto.LoginRequest;
import com.waimai.entity.User;
import com.waimai.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ApiResponse<User> register(@RequestBody User user) {
        user.setRole(UserRole.CUSTOMER);
        return ApiResponse.success(userService.register(user));
    }

    @PostMapping("/login")
    public ApiResponse<User> login(@RequestBody LoginRequest request) {
        Optional<User> user = userService.login(request.getUsername(), request.getPassword());
        return user.map(ApiResponse::success)
                .orElse(ApiResponse.error(401, "用户名或密码错误"));
    }

    @GetMapping
    public ApiResponse<List<User>> list() {
        return ApiResponse.success(userService.findAll());
    }

    @GetMapping("/{id}")
    public ApiResponse<User> getById(@PathVariable Long id) {
        return userService.findById(id)
                .map(ApiResponse::success)
                .orElse(ApiResponse.error(404, "用户不存在"));
    }

    @PutMapping("/{id}")
    public ApiResponse<User> update(@PathVariable Long id, @RequestBody User user) {
        user.setId(id);
        return ApiResponse.success(userService.update(user));
    }
}
