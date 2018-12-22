package com.xuanxuan.csu.model;

import lombok.Data;

import javax.persistence.*;


@Data
@Table(name = "user_info")
public class UserInfo {
    @Id
    private String id;

    //设置默认的username为中南大学学生
    @Column(name = "username")
    private String nickName = "";

    @Column(name = "avatar")
    private String avatarUrl = "";

    @Column(name = "gender")
    private int gender = 1;

    @Column(name = "country")
    private String country = "";

    @Column(name = "province")
    private String province = "";

    @Column(name = "city")
    private String city = "";

    @Column(name = "language")
    private String language = "";

}