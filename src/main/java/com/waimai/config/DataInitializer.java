package com.waimai.config;

import com.waimai.common.UserRole;
import com.waimai.entity.User;
import com.waimai.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;

    public DataInitializer(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) {
        // 创建默认管理员
        if (!userRepository.existsByUsername("admin")) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword("admin123");
            admin.setNickname("管理员");
            admin.setRole(UserRole.ADMIN);
            admin.setPhone("13800000001");
            admin.setAddress("系统管理办公室");
            userRepository.save(admin);
            System.out.println("✅ 默认管理员已创建: admin / admin123");
        }

        // 创建默认商家
        if (!userRepository.existsByUsername("merchant1")) {
            User merchant = new User();
            merchant.setUsername("merchant1");
            merchant.setPassword("123456");
            merchant.setNickname("老王餐厅");
            merchant.setRole(UserRole.MERCHANT);
            merchant.setPhone("13800000002");
            merchant.setAddress("科技路88号");
            userRepository.save(merchant);
            System.out.println("✅ 默认商家已创建: merchant1 / 123456");
        }

        // 创建默认普通用户
        if (!userRepository.existsByUsername("user1")) {
            User user = new User();
            user.setUsername("user1");
            user.setPassword("123456");
            user.setNickname("张三");
            user.setRole(UserRole.CUSTOMER);
            user.setPhone("13800000003");
            user.setAddress("长安路100号");
            userRepository.save(user);
            System.out.println("✅ 默认用户已创建: user1 / 123456");
        }
    }
}
