package com.waimai.dto;

import lombok.Data;

@Data
public class UserUpdateRequest {

    private String nickname;

    private String phone;

    private String avatar;

    private String address;
}
