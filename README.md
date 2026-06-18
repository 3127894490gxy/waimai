# 🍜 外卖配送系统 (waimai)

基于 **Spring Boot 3.2 + Maven + JPA + MySQL** 的全栈外卖配送平台，支持网页端操作。

## 技术栈

| 层级 | 技术 |
|------|------|
| **后端** | Spring Boot 3.2.5, JPA / Hibernate, MySQL 8.0+ |
| **前端** | 纯 HTML + CSS + JavaScript（REST API 调用） |
| **构建** | Maven, JDK 17 |
| **工具** | Lombok, 内嵌 Tomcat |

## 快速开始

### 1. 环境要求
- JDK 17+
- Maven 3.8+
- MySQL 8.0+

### 2. 配置数据库

编辑 `src/main/resources/application.yml`：

```yaml
spring:
  datasource:
    username: root          # 改为你的 MySQL 用户名
    password: root          # 改为你的 MySQL 密码
```

### 3. 运行

```bash
mvn spring-boot:run
```

启动后访问 **http://localhost:8080**

> 数据库和表结构由 JPA 自动创建，无需手动建表。
> 示例数据（账号、餐厅、菜品）由 `DataInitializer.java` 自动初始化。

## 默认账号

| 用户名 | 密码 | 角色 | 说明 |
|--------|------|------|------|
| `admin` | `admin123` | 🔑 管理员 | 管理后台：餐厅/菜品/分类/外卖员 |
| `merchant1` | `123456` | 🏪 商家 | 测试商家账号 |
| `delivery1` | `123456` | 🚚 配送员 | 配送工作台：自主接单/送达 |
| `user1` | `123456` | 👤 普通用户 | 点餐流程测试 |

## 项目结构

```
waimai/
├── src/main/java/com/waimai/
│   ├── config/              # 配置类 + DataInitializer 自动初始化
│   ├── controller/          # REST API 控制器
│   │   ├── UserController       # 用户注册/登录/密码修改
│   │   ├── RestaurantController # 餐厅 CRUD
│   │   ├── DishController       # 菜品 CRUD
│   │   ├── CategoryController   # 分类 CRUD
│   │   ├── OrderController      # 订单/支付/配送调度
│   │   └── ShoppingCartController # 购物车
│   ├── service/             # 业务逻辑层
│   ├── repository/          # JPA 数据访问层
│   ├── entity/              # 实体类（7张表）
│   ├── dto/                 # 数据传输对象
│   └── common/              # 基类/枚举
├── src/main/resources/
│   ├── application.yml      # 应用配置
│   └── static/              # 前端页面
│       ├── index.html         # 🏠 首页（餐厅列表）
│       ├── login.html         # 🔐 登录
│       ├── register.html      # 📝 注册
│       ├── restaurant.html    # 🍽️ 餐厅菜单/点餐
│       ├── cart.html          # 🛒 购物车
│       ├── checkout.html      # 💳 结算支付
│       ├── orders.html        # 📋 我的订单（含配送进度）
│       ├── delivery.html      # 🚚 配送员工作台
│       ├── merchant.html      # 🏪 商家管理
│       ├── admin.html         # ⚙️ 管理后台
│       └── css/style.css      # 全局动画样式
└── pom.xml
```

## 功能总览

### 👤 普通用户流程

```
首页 → 选择餐厅 → 浏览菜单 → 加入购物车
    → 结算 → 填写地址 → 支付 → 查看订单进度
    → 查看配送员信息/电话/配送耗时
```

| 功能 | 页面 | 说明 |
|------|------|------|
| 注册/登录 | `/login.html` `/register.html` | 创建账号 |
| 餐厅列表 | `/` | 浏览附近餐厅 |
| 点餐 | `/restaurant.html?id=X` | 按分类浏览、加购 |
| 购物车 | `/cart.html` | 修改数量、去结算 |
| 结算支付 | `/checkout.html` | 填地址、选择支付方式 |
| 订单列表 | `/orders.html` | 订单进度条、配送员信息 |
| 订单详情 | 点击订单卡片 | 时间轴、配送员电话、配送耗时 |
| 修改密码 | 首页 👤 个人 ▾ | 自助修改 |

### 🏪 商家管理

```
登录商家账号 → 首页直接显示自己的餐厅
    → 菜品管理 → 新增/编辑/上下架菜品、修改价格
    → 分类管理 → 新增/删除分类
    → 店铺设置 → 修改餐厅信息/营业状态
```

| 功能 | 页面标签 | 说明 |
|------|---------|------|
| 菜品管理 | 🍽️ 菜品管理 | 增删改查、修改价格、库存、上下架 |
| 分类管理 | 📂 分类管理 | 新增/删除分类、排序 |
| 店铺设置 | 🏪 店铺设置 | 修改名称/描述/电话/地址/配送费 |

### 🚚 配送员流程

```
登录配送账号 → 配送工作台 → 查看待接单
    → 自主接单 → 配送中 → 点击已送达
```

| 功能 | 页面 | 说明 |
|------|------|------|
| 配送工作台 | `/delivery.html` | 专属页面，仅配送员可访问 |
| 待接单 | 工作台"待接单"标签 | 查看所有待配送订单 |
| 自主接单 | 点击"接单" | 订单分配给自己，开始计时 |
| 我的配送 | 工作台"我的配送"标签 | 配送中/已送达订单 |
| 完成配送 | 点击"已送达" | 停止计时，记录耗时 |

### 🔑 管理员后台

| 功能 | 页面标签 | 说明 |
|------|---------|------|
| 餐厅管理 | 🏪 餐厅管理 | 增删改查、上下架 |
| 菜品管理 | 🍽️ 菜品管理 | 增删改查、修改价格/库存 |
| 分类管理 | 📂 分类管理 | 增删改查、排序 |
| 外卖员管理 | 👤 外卖员管理 | 新增/删除配送员 |
| 配送调度 | 📦 配送调度 | 查看待配送订单、分配配送员 |

#### 角色首页差异

不同角色登录首页看到的内容不同：

| 角色 | 首页导航栏 | 首页内容 | 可用链接 |
|------|-----------|---------|---------|
| 👤 普通用户 | 🟠 橙色渐变 | 附近餐厅列表 | 首页、我的订单、购物车 |
| 🏪 商家 | 🟢 绿色渐变 | 自己的餐厅信息 | 首页、🏪 商家 |
| 🚚 配送员 | 🔵 蓝色渐变 | 配送工作台入口 | 首页、🚚 配送 |
| 🔑 管理员 | 🟠 橙色渐变 | 附近餐厅列表 | 首页、我的订单、购物车、🚚 配送、⚙️ 管理 |

## 配送计时系统

| 事件 | 记录 | 显示位置 |
|------|------|---------|
| 配送员接单 | `pickupTime` 记录 | 配送员页面、订单详情 |
| 配送员送达 | `deliveredTime` 记录 | 配送员页面、订单详情 |
| 耗时计算 | `deliveredTime - pickupTime` | 格式：5分30秒 / 1时15分 |

## 实体关系

```
User ──→ Order ──→ OrderItem ──→ Dish
 │                  ShoppingCart
 │                  Restaurant ──→ Category ──→ Dish
 └──→ DELIVERY角色 → 配送接单
```

| 表 | 说明 |
|----|------|
| `users` | 用户（顾客/商家/配送员/管理员） |
| `restaurant` | 餐厅 |
| `category` | 菜品分类 |
| `dish` | 菜品 |
| `orders` | 订单（含配送员、配送时间） |
| `order_item` | 订单明细 |
| `shopping_cart` | 购物车 |

## API 接口

### 用户
| 方法 | 路径 | 说明 |
|------|------|------|
| POST | `/api/users/register` | 注册 |
| POST | `/api/users/login` | 登录 |
| PUT | `/api/users/password` | 修改密码 |
| GET | `/api/users/role/{role}` | 按角色查询 |
| POST | `/api/users/admin/create` | 管理员创建用户 |

### 餐厅/菜品/分类
| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/api/restaurants` | 餐厅列表 |
| GET | `/api/restaurants?mode=all` | 全部（含已下架） |
| GET | `/api/dishes/restaurant/{id}` | 餐厅菜品 |
| GET | `/api/dishes/admin/all` | 全部菜品 |
| POST/PUT/DELETE | 对应路径 | CRUD 操作 |

#### 商家
| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/api/restaurants?merchantId=X` | 商家查看自己的餐厅 |
| POST/PUT/DELETE | `/api/dishes` | 管理菜品（价格、库存） |
| POST/PUT/DELETE | `/api/categories` | 管理分类 |

### 订单/配送
| 方法 | 路径 | 说明 |
|------|------|------|
| POST | `/api/orders` | 创建订单 |
| POST | `/api/orders/pay` | 支付 |
| GET | `/api/orders/delivery/pending` | 待配送订单 |
| PUT | `/api/orders/{id}/assign-delivery` | 分配配送员 |
| PUT | `/api/orders/{id}/status` | 更新状态 |
| GET | `/api/orders/delivery/{id}/stats` | 配送员统计 |
