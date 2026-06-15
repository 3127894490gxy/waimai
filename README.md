# 外卖配送系统 (waimai)

基于 Spring Boot 3.2 + Maven + JPA + MySQL 的外卖配送后端系统。

## 技术栈

- **框架**: Spring Boot 3.2.5
- **构建**: Maven
- **ORM**: Spring Data JPA + Hibernate
- **数据库**: MySQL 8.0+
- **语言**: Java 17
- **工具**: Lombok

## 项目结构

```
waimai/
├── src/main/java/com/waimai/
│   ├── config/          # 配置类 (JPA 审计等)
│   ├── controller/      # REST API 控制器
│   ├── service/         # 业务逻辑层
│   ├── repository/      # 数据访问层
│   ├── entity/          # 实体类
│   ├── dto/             # 数据传输对象
│   └── common/          # 公共基类/枚举
├── src/main/resources/
│   └── application.yml  # 应用配置
├── sql/
│   └── init-data.sql    # 初始化示例数据
└── pom.xml
```

## 快速开始

### 1. 环境要求

- JDK 17+
- Maven 3.8+
- MySQL 8.0+

### 2. 创建数据库

```sql
CREATE DATABASE IF NOT EXISTS waimai DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 3. 修改配置

编辑 `src/main/resources/application.yml`，修改 MySQL 连接信息：

```yaml
spring:
  datasource:
    username: root          # 改为你的 MySQL 用户名
    password: root          # 改为你的 MySQL 密码
```

### 4. 运行

```bash
# 编译
mvn clean compile

# 运行
mvn spring-boot:run
```

启动后访问：http://localhost:8080

### 5. 初始化数据（可选）

```bash
mysql -u root -p waimai < sql/init-data.sql
```

## API 接口

| 模块 | 方法 | 路径 | 说明 |
|------|------|------|------|
| 用户 | POST | /api/users/register | 注册 |
| 用户 | POST | /api/users/login | 登录 |
| 用户 | GET  | /api/users | 用户列表 |
| 餐厅 | GET  | /api/restaurants | 餐厅列表 |
| 餐厅 | GET  | /api/restaurants/search?name= | 搜索餐厅 |
| 分类 | GET  | /api/categories/restaurant/{id} | 餐厅分类 |
| 菜品 | GET  | /api/dishes/restaurant/{id} | 餐厅菜单 |
| 购物车 | POST | /api/cart | 加入购物车 |
| 订单 | GET  | /api/orders/user/{id} | 用户订单 |

## 实体关系

- **User** - 用户（顾客/商家/配送员/管理员）
- **Restaurant** - 餐厅，关联商家用户
- **Category** - 菜品分类，归属餐厅
- **Dish** - 菜品，归属分类和餐厅
- **Order** - 订单，关联用户和餐厅
- **OrderItem** - 订单项，关联订单和菜品
- **ShoppingCart** - 购物车，关联用户和菜品
