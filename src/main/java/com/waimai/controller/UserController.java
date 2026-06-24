package com.waimai.controller;

import com.waimai.common.UserRole;
import com.waimai.dto.ApiResponse;
import com.waimai.dto.ChangePasswordRequest;
import com.waimai.dto.LoginRequest;
import com.waimai.dto.RegisterRequest;
import com.waimai.dto.UserUpdateRequest;
import com.waimai.entity.User;
import com.waimai.service.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ApiResponse<User> register(@Valid @RequestBody RegisterRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        user.setNickname(request.getNickname());
        user.setPhone(request.getPhone());
        user.setAvatar(request.getAvatar());
        user.setAddress(request.getAddress());
        // 允许用户选择角色，但不能注册为管理员
        if (request.getRole() == null || request.getRole() == UserRole.ADMIN) {
            user.setRole(UserRole.CUSTOMER);
        } else {
            user.setRole(request.getRole());
        }
        User saved = userService.register(user);
        log.info("用户注册成功: username={}, role={}", saved.getUsername(), saved.getRole());
        return ApiResponse.success(saved);
    }

    @PostMapping("/login")
    public ApiResponse<User> login(@Valid @RequestBody LoginRequest request) {
        Optional<User> user = userService.login(request.getUsername(), request.getPassword());
        return user.map(u -> {
                    log.info("用户登录成功: username={}", request.getUsername());
                    return ApiResponse.success(u);
                })
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
    public ApiResponse<User> adminCreate(@Valid @RequestBody RegisterRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        user.setNickname(request.getNickname());
        user.setPhone(request.getPhone());
        user.setAvatar(request.getAvatar());
        user.setAddress(request.getAddress());
        if (request.getRole() == null) user.setRole(UserRole.CUSTOMER);
        else user.setRole(request.getRole());
        return ApiResponse.success(userService.adminCreate(user));
    }

    @GetMapping("/{id}")
    public ApiResponse<User> getById(@PathVariable Long id) {
        return userService.findById(id)
                .map(ApiResponse::success)
                .orElse(ApiResponse.error(404, "用户不存在"));
    }

    @PutMapping("/{id}")
    public ApiResponse<User> update(@PathVariable Long id, @Valid @RequestBody UserUpdateRequest request) {
        User user = userService.findById(id)
                .orElse(null);
        if (user == null) {
            return ApiResponse.error(404, "用户不存在");
        }
        if (request.getNickname() != null) user.setNickname(request.getNickname());
        if (request.getPhone() != null) user.setPhone(request.getPhone());
        if (request.getAvatar() != null) user.setAvatar(request.getAvatar());
        if (request.getAddress() != null) user.setAddress(request.getAddress());
        user.setId(id);
        User updated = userService.update(user);
        log.info("用户信息更新: userId={}", id);
        return ApiResponse.success(updated);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteUser(@PathVariable Long id) {
        userService.deleteById(id);
        log.info("用户删除: userId={}", id);
        return ApiResponse.success(null);
    }

    /** 修改密码 */
    @PutMapping("/password")
    public ApiResponse<Void> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        boolean success = userService.changePassword(
                request.getUserId(),
                request.getOldPassword(),
                request.getNewPassword()
        );
        if (success) {
            log.info("密码修改成功: userId={}", request.getUserId());
            return ApiResponse.success(null);
        } else {
            return ApiResponse.error(400, "原密码错误或用户不存在");
        }
    }
}
