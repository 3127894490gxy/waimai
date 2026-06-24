# 外卖配送系统 E-R 图 — 对照表

## 1. 实体汇总

| 实体 | 中文名 | 主键 | 说明 |
|------|--------|------|------|
| User | 用户 | id | 四种角色：CUSTOMER/MERCHANT/DELIVERY/ADMIN |
| Restaurant | 餐厅 | id | 通过 merchant_id 关联商家 |
| Category | 分类 | id | 通过 restaurant_id 关联餐厅 |
| Dish | 菜品 | id | 通过 category_id 和 restaurant_id 关联 |
| Order | 订单 | id | 通过 user_id 关联顾客，delivery_id 关联配送员 |
| OrderItem | 订单项 | id | 通过 order_id 关联订单，dish_id 关联菜品 |
| ShoppingCart | 购物车 | id | 通过 user_id 关联用户，dish_id 关联菜品 |

## 2. 关系对照（用于 Visio/Rose 画连线）

| 源实体 | 目标实体 | 关系类型 | 外键字段 | 业务含义 |
|--------|---------|---------|---------|---------|
| User | Restaurant | 1:N | Restaurant.merchant_id | 一个商家可以拥有多个餐厅 |
| User | Order | 1:N | Order.user_id | 一个顾客可以有多个订单 |
| User | Order | 1:N | Order.delivery_id | 一个配送员可以接多个配送单 |
| User | ShoppingCart | 1:N | ShoppingCart.user_id | 一个用户可以有多个购物车项 |
| Restaurant | Category | 1:N | Category.restaurant_id | 一个餐厅可以有多个分类 |
| Restaurant | Dish | 1:N | Dish.restaurant_id | 一个餐厅可以有多个菜品 |
| Category | Dish | 1:N | Dish.category_id | 一个分类下可以有多个菜品 |
| Order | OrderItem | 1:N | OrderItem.order_id | 一个订单可以包含多个订单项 |
| Dish | OrderItem | 1:N | OrderItem.dish_id | 一个菜品可以出现在多个订单中 |
| Dish | ShoppingCart | 1:N | ShoppingCart.dish_id | 一个菜品可以被加入多个购物车 |

## 3. 如何在 Visio 中画

1. 打开 Visio → 新建 → **"数据库模型图"** 或 **"UML 模型图"**
2. 从左侧形状区拖出 **"实体"** 形状
3. 双击实体添加属性（字段名、类型、主键标记）
4. 用 **"关系"** 连线连接实体
5. 右键连线设置基数（1:N / 1:1 / N:M）

## 4. 如何在 Rational Rose 中画

1. 打开 Rose → 在 **Logical View** 中右键 → New → **Class Diagram**
2. 使用工具栏的 **Entity** 图标创建实体
3. 双击实体添加属性和主键
4. 使用 **Relation** 工具画连线
5. 双击连线设置 **Multiplicity**（角色 → 1...*）
