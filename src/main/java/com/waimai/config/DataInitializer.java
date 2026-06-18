package com.waimai.config;

import com.waimai.common.UserRole;
import com.waimai.entity.*;
import com.waimai.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;
    private final CategoryRepository categoryRepository;
    private final DishRepository dishRepository;

    public DataInitializer(UserRepository userRepository,
                           RestaurantRepository restaurantRepository,
                           CategoryRepository categoryRepository,
                           DishRepository dishRepository) {
        this.userRepository = userRepository;
        this.restaurantRepository = restaurantRepository;
        this.categoryRepository = categoryRepository;
        this.dishRepository = dishRepository;
    }

    @Override
    public void run(String... args) {
        // ===== 1. 创建默认用户 =====
        User merchant = null;

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

        if (!userRepository.existsByUsername("merchant1")) {
            merchant = new User();
            merchant.setUsername("merchant1");
            merchant.setPassword("123456");
            merchant.setNickname("老王餐厅");
            merchant.setRole(UserRole.MERCHANT);
            merchant.setPhone("13800000002");
            merchant.setAddress("科技路88号");
            merchant = userRepository.save(merchant);
            System.out.println("✅ 默认商家已创建: merchant1 / 123456");
        } else {
            merchant = userRepository.findByUsername("merchant1").orElse(null);
        }

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

        // 创建默认配送员
        if (!userRepository.existsByUsername("delivery1")) {
            User delivery = new User();
            delivery.setUsername("delivery1");
            delivery.setPassword("123456");
            delivery.setNickname("李四");
            delivery.setRole(UserRole.DELIVERY);
            delivery.setPhone("13800000004");
            delivery.setAddress("配送站");
            userRepository.save(delivery);
            System.out.println("✅ 默认配送员已创建: delivery1 / 123456");
        }

        // ===== 2. 创建默认餐厅 =====
        Restaurant restaurant = null;
        if (merchant != null && !restaurantRepository.findByNameContaining("老王中餐").isEmpty()) {
            // 已存在，不做操作
        } else if (merchant != null) {
            restaurant = new Restaurant();
            restaurant.setName("老王中餐");
            restaurant.setDescription("主打川湘菜，二十年老店，味道正宗，价格实惠");
            restaurant.setAddress("科技路88号");
            restaurant.setPhone("029-88886666");
            restaurant.setDeliveryFee(BigDecimal.valueOf(3.00));
            restaurant.setRating(BigDecimal.valueOf(4.8));
            restaurant.setMerchantId(merchant.getId());
            restaurant.setActive(true);
            restaurant = restaurantRepository.save(restaurant);
            System.out.println("✅ 默认餐厅已创建: 老王中餐");
        } else {
            // merchant 不存在，尝试直接用 merchantId=2
            if (!restaurantRepository.findByNameContaining("老王中餐").isEmpty()) {
                // 已存在
            } else {
                restaurant = new Restaurant();
                restaurant.setName("老王中餐");
                restaurant.setDescription("主打川湘菜，二十年老店，味道正宗，价格实惠");
                restaurant.setAddress("科技路88号");
                restaurant.setPhone("029-88886666");
                restaurant.setDeliveryFee(BigDecimal.valueOf(3.00));
                restaurant.setRating(BigDecimal.valueOf(4.8));
                restaurant.setMerchantId(2L);
                restaurant.setActive(true);
                restaurant = restaurantRepository.save(restaurant);
                System.out.println("✅ 默认餐厅已创建: 老王中餐");
            }
        }

        // 获取餐厅（可能是刚创建或已存在）
        if (restaurant == null) {
            var list = restaurantRepository.findByNameContaining("老王中餐");
            if (!list.isEmpty()) restaurant = list.get(0);
        }

        if (restaurant != null) {
            Long restId = restaurant.getId();

            // ===== 3. 创建默认分类 =====
            Category cat1 = null;
            Category cat2 = null;

            var existingCats = categoryRepository.findByRestaurantIdOrderBySortOrder(restId);
            boolean hasHot = existingCats.stream().anyMatch(c -> c.getName().equals("热销推荐"));
            boolean hasMain = existingCats.stream().anyMatch(c -> c.getName().equals("招牌主食"));

            if (!hasHot) {
                cat1 = new Category();
                cat1.setName("热销推荐");
                cat1.setRestaurantId(restId);
                cat1.setSortOrder(0);
                cat1 = categoryRepository.save(cat1);
                System.out.println("✅ 默认分类已创建: 热销推荐");
            } else {
                cat1 = existingCats.stream().filter(c -> c.getName().equals("热销推荐")).findFirst().orElse(null);
            }

            if (!hasMain) {
                cat2 = new Category();
                cat2.setName("招牌主食");
                cat2.setRestaurantId(restId);
                cat2.setSortOrder(1);
                cat2 = categoryRepository.save(cat2);
                System.out.println("✅ 默认分类已创建: 招牌主食");
            } else {
                cat2 = existingCats.stream().filter(c -> c.getName().equals("招牌主食")).findFirst().orElse(null);
            }

            // ===== 4. 创建默认菜品 =====
            if (cat1 != null && dishRepository.findByNameContainingAndActiveTrue("鱼香肉丝").isEmpty()) {
                Dish d1 = new Dish();
                d1.setName("鱼香肉丝");
                d1.setDescription("经典川菜，酸甜可口，下饭必备");
                d1.setPrice(BigDecimal.valueOf(28.00));
                d1.setCategoryId(cat1.getId());
                d1.setRestaurantId(restId);
                d1.setStock(100);
                d1.setActive(true);
                dishRepository.save(d1);
                System.out.println("✅ 默认菜品已创建: 鱼香肉丝(¥28)");
            }

            if (cat1 != null && dishRepository.findByNameContainingAndActiveTrue("宫保鸡丁").isEmpty()) {
                Dish d2 = new Dish();
                d2.setName("宫保鸡丁");
                d2.setDescription("花生与鸡肉的绝配，麻辣鲜香");
                d2.setPrice(BigDecimal.valueOf(32.00));
                d2.setCategoryId(cat1.getId());
                d2.setRestaurantId(restId);
                d2.setStock(100);
                d2.setActive(true);
                dishRepository.save(d2);
                System.out.println("✅ 默认菜品已创建: 宫保鸡丁(¥32)");
            }

            if (cat2 != null && dishRepository.findByNameContainingAndActiveTrue("蛋炒饭").isEmpty()) {
                Dish d3 = new Dish();
                d3.setName("蛋炒饭");
                d3.setDescription("粒粒分明，香气扑鼻，简单美味");
                d3.setPrice(BigDecimal.valueOf(15.00));
                d3.setCategoryId(cat2.getId());
                d3.setRestaurantId(restId);
                d3.setStock(200);
                d3.setActive(true);
                dishRepository.save(d3);
                System.out.println("✅ 默认菜品已创建: 蛋炒饭(¥15)");
            }
        }
    }
}
