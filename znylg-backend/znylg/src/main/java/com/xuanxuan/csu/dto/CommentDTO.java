package com.xuanxuan.csu.dto;


import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

/**
 * 评论的数据传输对象,服务客户端需要传递次类型数据来调用rest api接口
 */

@Data
public class CommentDTO {

    private String commentId;

    @NotNull(message = "文章id不能为空")
    //评论对应的文章id
    private String passageId;

    @NotNull(message = "评论content不能为空")
    //评论的内容
    private String content;

    @NotNull(message = "用户id不能为空")
    //所属用户的id
    private String fromUid;

    @NotNull(message = "评论是否通过审核不能为空")
    // 评论是否通过了审核
    private int is_verified;

}
