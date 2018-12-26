package com.xuanxuan.csu.model;

import lombok.Data;

import java.util.Date;
import javax.persistence.*;


@Data
@Table(name = "reply")
public class Reply {
    @Id
    @GeneratedValue(generator = "UUID")
    private String id;

    @Column(name = "comment_id")
    private String commentId;

    @Column(name = "reply_id")
    private String replyId;

    @Column(name = "reply_type")
    private Integer replyType;//回复评论为1,回复回复为2

    @Column(name = "content")
    private String content;

    @Column(name = "from_uid")
    private String fromUid;

    @Column(name = "create_time")
    private Date createTime = new Date();

    @Column(name = "star_number")
    private Integer zanNum = 0;

}