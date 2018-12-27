package com.xuanxuan.csu.dto;


import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 前端请求传递过来的用户信息传输对象
 */

@Data
public class UserDTO {
    @NotNull(message = "用户id不能为空")
    private String id;

    @NotNull(message = "用户username不能为空")
    private String nickName;

    @NotNull(message = "用户avatar不能为空")
    private String avatarUrl;


    @NotNull(message = "用户gender不能为空")
    private int gender;

    @NotNull(message = "用户country不能为空")
    private String country;

    @NotNull(message = "用户province不能")
    private String province;

    @NotNull(message = "用户city不能为空")
    private String city;

    @NotNull(message = "用户language不能为空")
    private String language;
}
