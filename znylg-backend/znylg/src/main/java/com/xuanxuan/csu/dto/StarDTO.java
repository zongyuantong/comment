package com.xuanxuan.csu.dto;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
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

    @NotNull(message = "点赞类型不能为空")
    @Max(value = 2, message = "点赞类型错误")
    @Min(value = 1, message = "点赞类型错误")
    private Integer toType;
}
