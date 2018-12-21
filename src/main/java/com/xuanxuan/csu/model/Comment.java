package com.xuanxuan.csu.model;

import lombok.Data;

import java.text.DateFormat;
import java.util.Date;
import javax.persistence.*;


@Data
@Table(name = "comment")
public class Comment {
    @Id
    @GeneratedValue(generator = "UUID")
    private String id;

    @Column(name = "passage_id")
    private String passageId;

    @Column(name = "content")
    private String content;

    @Column(name = "from_uid")
    private String fromUid;

    @Column(name = "create_time")
    private Date createTime = new Date();

    @Column(name = "star_number")
    private Integer zanNum = 0;

    @Column(name = "reply_number")
    private Integer replyNum = 0;


    @Column(name = "floor")
    private Integer floor;

}