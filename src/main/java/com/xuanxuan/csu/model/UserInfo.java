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
    private String nickName;

    @Column(name = "avatar")
    private String avatarUrl;

    @Column(name = "gender")
    private Integer gender = 1;

    @Column(name = "country")
    private String country = "中国";

    @Column(name = "province")
    private String province = "湖南";

    @Column(name = "city")
    private String city = "";

    @Column(name = "language")
    private String language = "";

    public static UserInfo getDefaultUserInfo() {
        UserInfo userInfo = new UserInfo();
        userInfo.setNickName("云麓学子");
        userInfo.setAvatarUrl("https://ss0.baidu.com/6ONWsjip0QIZ8tyhnq/it/u=1397393643,1057616612&fm=58&bpow=483&bpoh=490");
        userInfo.setGender(1);
        userInfo.setCountry("中国");
        userInfo.setProvince("湖南");
        userInfo.setLanguage("zh_cn");
        userInfo.setId("undefined");
        return userInfo;
    }

}