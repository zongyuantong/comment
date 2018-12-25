package com.xuanxuan.csu.model;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 评论详情DO
 */
@Data
public class CommentDetail {
    //根据表信息构造评论的字段内容
    @Id
    private String id;//评论的id
    @Column(name = "passage_id")
    private String passageId;
    @Column(name = "content")
    private String content;//评论内容
    @Column(name = "from_uid")
    private String fromUid;
    @Column(name = "createTime")
    private Date createTime;//创建时间
    @Column(name = "star_number")
    private Integer zanNum;//点赞数量
    @Column(name = "reply_number")
    private Integer replyNum;//回复数量
    @Column(name = "floor")
    private Integer floor;//楼层
    //一个评论有多个回复,与回复表映射
    private List<Reply> replyList;
}
