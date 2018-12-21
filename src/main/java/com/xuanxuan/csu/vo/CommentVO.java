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
public class CommentVO extends AbstractVO {
    //根据表信息构造评论的字段内容
    private String id;//评论的id
    private String avatar;//评论用户头像url
    private String username;//评论用户名
    private String content;//评论内容
    private String createTime;//创建时间
    private Integer zanNum;//点赞数量
    private Integer replyNum;//回复数量
    private Integer floor;//楼层
    //一个评论有多个回复
    private List<ReplyVO> replyList = new ArrayList<>();

    public CommentVO(Comment comment) {
        super();
        BeanUtils.copyProperties(comment, this);
        //将时间转化为需要的字符串格式
        this.createTime = date2String(comment.getCreateTime());
    }
}
