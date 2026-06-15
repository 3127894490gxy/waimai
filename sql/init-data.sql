-- =============================================
-- 外卖系统 - 数据库初始化脚本
-- 数据库会在 JPA 启动时自动创建，此脚本为可选初始化数据
-- =============================================

-- 创建数据库（如果还没创建）
-- CREATE DATABASE IF NOT EXISTS waimai DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- =============================================
-- 示例数据：用户
-- =============================================
INSERT INTO users (username, password, nickname, role, phone, address, create_time, update_time)
SELECT 'admin', 'admin123', '管理员', 'ADMIN', '13800000001', '管理办公室', NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'admin');

INSERT INTO users (username, password, nickname, role, phone, address, create_time, update_time)
SELECT 'merchant1', '123456', '老王餐厅', 'MERCHANT', '13800000002', '科技路88号', NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'merchant1');

INSERT INTO users (username, password, nickname, role, phone, address, create_time, update_time)
SELECT 'user1', '123456', '张三', 'CUSTOMER', '13800000003', '长安路100号', NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'user1');

-- =============================================
-- 示例数据：餐厅
-- =============================================
INSERT INTO restaurant (name, description, address, phone, image, delivery_fee, rating, merchant_id, active, create_time, update_time)
SELECT '老王中餐', '主打川湘菜，二十年老店', '科技路88号', '029-88886666', '/images/rest1.jpg', 3.00, 4.8, 2, true, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM restaurant WHERE name = '老王中餐');

-- =============================================
-- 示例数据：分类
-- =============================================
INSERT INTO category (name, restaurant_id, sort_order, create_time, update_time)
SELECT '热销推荐', 1, 0, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM category WHERE name = '热销推荐' AND restaurant_id = 1);

INSERT INTO category (name, restaurant_id, sort_order, create_time, update_time)
SELECT '招牌主食', 1, 1, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM category WHERE name = '招牌主食' AND restaurant_id = 1);

-- =============================================
-- 示例数据：菜品
-- =============================================
INSERT INTO dish (name, description, image, price, category_id, restaurant_id, stock, active, create_time, update_time)
SELECT '鱼香肉丝', '经典川菜，酸甜可口', '/images/dish1.jpg', 28.00, 1, 1, 100, true, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM dish WHERE name = '鱼香肉丝' AND restaurant_id = 1);

INSERT INTO dish (name, description, image, price, category_id, restaurant_id, stock, active, create_time, update_time)
SELECT '宫保鸡丁', '花生与鸡肉的绝配', '/images/dish2.jpg', 32.00, 1, 1, 100, true, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM dish WHERE name = '宫保鸡丁' AND restaurant_id = 1);

INSERT INTO dish (name, description, image, price, category_id, restaurant_id, stock, active, create_time, update_time)
SELECT '蛋炒饭', '粒粒分明，香气扑鼻', '/images/dish3.jpg', 15.00, 2, 1, 200, true, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM dish WHERE name = '蛋炒饭' AND restaurant_id = 1);
