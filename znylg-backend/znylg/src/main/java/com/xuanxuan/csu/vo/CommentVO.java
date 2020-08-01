package com.xuanxuan.csu.vo;


import com.xuanxuan.csu.model.Comment;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 评论的数据传输对象实体
 */

@Data
public class CommentVO {
    //根据表信息构造评论的字段内容
    private String id;//评论的id
    private String avatar;//评论用户头像url
    private String fromUid;//评论所属用户id
    private String username;//评论用户名
    private String content;//评论内容
    private String createTime;//创建时间
    private Integer zanNum;//点赞数量
    private Integer replyNum;//回复数量
    private Integer floor;//楼层
    private Boolean isZan = false;//是否点赞
    private List<ReplyVO> replyList;//一个评论有多个回复
}
