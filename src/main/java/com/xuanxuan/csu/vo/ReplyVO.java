package com.xuanxuan.csu.vo;


import com.xuanxuan.csu.model.Reply;
import lombok.Data;
import org.springframework.beans.BeanUtils;

/**
 * @author PualrDwade
 * @Create at 2018-12-4
 * @note 回复内容的数据传输对象
 */

@Data
public class ReplyVO extends AbstractVO {
    private String id;//此条回复的id
    private String fromUid;//所属用户id
    private String fromUname;//所属用户name
    private String replyId;//回复的目标id
    private String toUid;//回复目标用户id
    private String toUname;//回复目标用户name
    private Integer replyType;//回复类型
    private String content;//回复内容
    private String createTime;//回复时间
    private String commentId;//所属评论的id

    //创建属性赋值的方法
    public ReplyVO(Reply reply) {
        super();
        BeanUtils.copyProperties(reply, this);
        //转化为需要显示的字符串格式
        this.createTime = date2String(reply.getCreateTime());
    }

}
