package com.xuanxuan.csu.dto;


import lombok.Data;

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

}
