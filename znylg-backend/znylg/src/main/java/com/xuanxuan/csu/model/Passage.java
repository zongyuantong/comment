package com.xuanxuan.csu.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Table(name = "passage")
public class Passage {
    @Id
    private String id;

    //公众号文章url
    @Column(name = "url")
    private String url = "";

    //后期接入多个平台
    @Column(name = "platform_id")
    private String platformId = "";

}