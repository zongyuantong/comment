package com.xuanxuan.csu.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;


@Data
@Table(name = "user_star")
public class UserStar {
    @Id
    @GeneratedValue(generator = "UUID")
    private String id;

    @Column(name = "to_id")
    private String toId;

    @Column(name = "to_type")
    private Integer toType;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "create_time")
    private Date createTime = new Date();

}