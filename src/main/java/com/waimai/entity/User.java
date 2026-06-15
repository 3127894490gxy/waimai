package com.waimai.entity;

import com.waimai.common.BaseEntity;
import com.waimai.common.UserRole;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User extends BaseEntity {
    //用户名称
    @Column(nullable = false, unique = true, length = 50)
    private String username;
    //密码
    @Column(nullable = false)
    private String password;
    //昵称
    @Column(length = 100)
    private String nickname;
    //电话
    @Column(length = 20)
    private String phone;
    //展示图片地址
    @Column(length = 200)
    private String avatar;
    //角色
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private UserRole role = UserRole.CUSTOMER;
    //地址
    @Column(length = 500)
    private String address;
}
