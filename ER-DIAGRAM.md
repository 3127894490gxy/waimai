# 外卖配送系统 E-R 图

```mermaid
erDiagram
    USER ||--o{ ORDER : "顾客下单"
    USER ||--o{ RESTAURANT : "商家拥有"
    USER ||--o{ ORDER : "配送员接单"
    RESTAURANT ||--o{ CATEGORY : "包含"
    RESTAURANT ||--o{ DISH : "提供"
    CATEGORY ||--o{ DISH : "归类"
    ORDER ||--|{ ORDER_ITEM : "包含"
    DISH ||--o{ ORDER_ITEM : "被购买"
    USER ||--o{ SHOPPING_CART : "拥有"
    DISH ||--o{ SHOPPING_CART : "加入"

    USER {
        bigint id PK
        varchar username UK "登录名，禁止中文"
        varchar password "密码"
        varchar nickname "昵称/商家名/配送员姓名"
        varchar phone "手机号"
        varchar avatar "头像URL"
        enum role "CUSTOMER|MERCHANT|DELIVERY|ADMIN"
        varchar address "地址"
        datetime create_time "创建时间"
        datetime update_time "更新时间"
    }

    RESTAURANT {
        bigint id PK
        varchar name "餐厅名称"
        varchar description "描述"
        varchar address "地址"
        varchar phone "电话"
        varchar image "图片URL"
        decimal delivery_fee "配送费"
        decimal rating "评分"
        bigint merchant_id FK "商家用户ID"
        boolean active "营业状态"
        datetime create_time
        datetime update_time
    }

    CATEGORY {
        bigint id PK
        varchar name "分类名称"
        bigint restaurant_id FK "所属餐厅"
        int sort_order "排序"
        datetime create_time
        datetime update_time
    }

    DISH {
        bigint id PK
        varchar name "菜品名称"
        varchar description "描述"
        varchar image "图片"
        decimal price "价格"
        bigint category_id FK "分类ID"
        bigint restaurant_id FK "餐厅ID"
        int stock "库存"
        boolean active "在售状态"
        datetime create_time
        datetime update_time
    }

    ORDER {
        bigint id PK
        varchar order_no UK "订单号(WM+时间+随机)"
        bigint user_id FK "顾客ID"
        bigint restaurant_id FK "餐厅ID"
        decimal total_amount "总金额"
        decimal delivery_fee "配送费"
        enum status "PENDING_PAYMENT|PAID|ACCEPTED|PREPARING|DELIVERING|DELIVERED|COMPLETED|CANCELLED"
        varchar delivery_address "配送地址"
        varchar contact_phone "联系电话"
        varchar contact_name "联系人"
        varchar remark "备注"
        bigint delivery_id FK "配送员ID"
        varchar delivery_name "配送员姓名"
        datetime pickup_time "接单时间"
        datetime delivered_time "送达时间"
        boolean deleted "逻辑删除"
        datetime create_time
        datetime update_time
    }

    ORDER_ITEM {
        bigint id PK
        bigint order_id FK "订单ID"
        bigint dish_id FK "菜品ID"
        varchar dish_name "菜品名(快照)"
        int quantity "数量"
        decimal price "单价"
        decimal subtotal "小计"
        datetime create_time
        datetime update_time
    }

    SHOPPING_CART {
        bigint id PK
        bigint user_id FK "用户ID"
        bigint dish_id FK "菜品ID"
        bigint restaurant_id FK "餐厅ID"
        varchar dish_name "菜品名"
        decimal dish_price "单价"
        int quantity "数量"
        datetime create_time
        datetime update_time
    }
```

## 实体关系说明

| 实体 | 说明 | 关键字段 |
|------|------|---------|
| `USER` | 用户（含顾客/商家/配送员/管理员四种角色） | `role` 区分身份 |
| `RESTAURANT` | 餐厅，通过 `merchant_id` 关联商家用户 | `active` 控制营业状态 |
| `CATEGORY` | 菜品分类，归属某个餐厅 | `sort_order` 控制显示顺序 |
| `DISH` | 菜品，归属分类和餐厅 | `price` 可被商家修改，`active` 控制上下架 |
| `ORDER` | 订单，关联顾客和餐厅 | `delivery_id` 关联配送员，`status` 状态机流转 |
| `ORDER_ITEM` | 订单明细，记录购买时的菜品快照 | 即使菜品被删除，订单中仍保留名称和价格 |
| `SHOPPING_CART` | 购物车，每个用户在不同餐厅有独立购物车 | 切换餐厅时提示清空 |

## 状态流转

```
订单状态机:

PENDING_PAYMENT ──支付──→ PAID ──接单──→ DELIVERING ──送达──→ DELIVERED ──→ COMPLETED
       │                     │
       └──取消──→ CANCELLED   └──取消──→ CANCELLED
```

## 主外键关系

```
USER.id ───→ RESTAURANT.merchant_id    (商家→餐厅)
USER.id ───→ ORDER.user_id             (顾客→订单)
USER.id ───→ ORDER.delivery_id         (配送员→订单)
USER.id ───→ SHOPPING_CART.user_id     (用户→购物车)
RESTAURANT.id ───→ CATEGORY.restaurant_id  (餐厅→分类)
RESTAURANT.id ───→ DISH.restaurant_id      (餐厅→菜品)
CATEGORY.id ───→ DISH.category_id           (分类→菜品)
ORDER.id ───→ ORDER_ITEM.order_id           (订单→明细)
DISH.id ───→ ORDER_ITEM.dish_id             (菜品→订单明细)
DISH.id ───→ SHOPPING_CART.dish_id          (菜品→购物车)
```
