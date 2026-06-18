package com.waimai.service;

import com.waimai.common.UserRole;
import com.waimai.entity.User;
import com.waimai.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;  
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    //构造函数
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    //用户注册
    public User register(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("用户名已存在");
        }
        return userRepository.save(user);
    }
    //用户登录
    public Optional<User> login(String username, String password) {
        return userRepository.findByUsername(username)
                .filter(u -> u.getPassword().equals(password));
    }
    //查找ID
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }
    //查询所有用户
    public List<User> findAll() {
        return userRepository.findAll();
    }

    //按角色查询
    public List<User> findByRole(UserRole role) {
        return userRepository.findByRole(role);
    }

    //管理员创建用户（可指定角色）
    public User adminCreate(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("用户名已存在");
        }
        return userRepository.save(user);
    }
    //更新用户信息
    public User update(User user) {
        return userRepository.save(user);
    }
    //删除用户
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    //修改密码
    public boolean changePassword(Long userId, String oldPassword, String newPassword) {
        return userRepository.findById(userId)
                .filter(u -> u.getPassword().equals(oldPassword))
                .map(u -> {
                    u.setPassword(newPassword);
                    userRepository.save(u);
                    return true;
                })
                .orElse(false);
    }
}
