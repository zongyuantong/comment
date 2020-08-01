package com.xuanxuan.csu.dto;


import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

/**
 * 回复的数据传输对象
 */

@Data
public class ReplyDTO {
    @NotNull(message = "回复目标id不能为空")
    //回复目标的id
    private String replyId;

    @NotNull(message = "回复content不能为空")
    //回复的内容
    private String content;

    @NotNull(message = "用户id不能为空")
    //所属用户的id
    private String fromUid;

    @NotNull(message = "回复类型不能为空")
    @Max(value = 2, message = "回复类型错误")
    @Min(value = 1, message = "回复类型错误")
    private Integer replyType;

    @NotNull(message = "评论是否通过审核不能为空")
    // 评论是否通过了审核
    private int is_verified;
}
