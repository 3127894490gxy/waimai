package com.waimai.controller;

import com.waimai.common.UserRole;
import com.waimai.dto.ApiResponse;
import com.waimai.dto.ChangePasswordRequest;
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
        // 允许用户选择角色，但不能注册为管理员
        if (user.getRole() == null || user.getRole() == UserRole.ADMIN) {
            user.setRole(UserRole.CUSTOMER);
        }
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

    /** 管理员：按角色查询用户 */
    @GetMapping("/role/{role}")
    public ApiResponse<List<User>> listByRole(@PathVariable String role) {
        try {
            UserRole userRole = UserRole.valueOf(role.toUpperCase());
            return ApiResponse.success(userService.findByRole(userRole));
        } catch (IllegalArgumentException e) {
            return ApiResponse.error(400, "无效的角色: " + role);
        }
    }

    /** 管理员：创建任意角色的用户 */
    @PostMapping("/admin/create")
    public ApiResponse<User> adminCreate(@RequestBody User user) {
        if (user.getRole() == null) user.setRole(UserRole.CUSTOMER);
        return ApiResponse.success(userService.adminCreate(user));
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

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteUser(@PathVariable Long id) {
        userService.deleteById(id);
        return ApiResponse.success(null);
    }

    /** 修改密码 */
    @PutMapping("/password")
    public ApiResponse<Void> changePassword(@RequestBody ChangePasswordRequest request) {
        boolean success = userService.changePassword(
                request.getUserId(),
                request.getOldPassword(),
                request.getNewPassword()
        );
        if (success) {
            return ApiResponse.success(null);
        } else {
            return ApiResponse.error(400, "原密码错误或用户不存在");
        }
    }
}
