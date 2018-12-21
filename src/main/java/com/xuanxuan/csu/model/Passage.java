package com.xuanxuan.csu.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Table(name = "passage")
public class Passage {
    @Id
    @GeneratedValue(generator = "UUID")
    private String id;

    @Column(name = "url")
    private String url;

    @Column(name = "platform_id")
    private String platformId;

}