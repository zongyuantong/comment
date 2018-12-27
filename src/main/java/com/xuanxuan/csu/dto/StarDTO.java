package com.xuanxuan.csu.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;


/**
 * @author PualrDwade
 * @note 点赞的数据传输对象, 用户前端传送数据
 */

@Data
public class StarDTO {

    @NotNull(message = "点赞目标id不能为空")
    private String toId;//点赞目标的id

    @NotNull(message = "用户id不能为空")
    private String userId;//点赞用户的id
}
