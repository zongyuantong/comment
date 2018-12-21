package com.xuanxuan.csu.dto;


import lombok.Data;

import javax.validation.constraints.NotNull;


/**
 * 刷新请求的数据传输对象
 */
@Data
public class RefreshDTO {
    @NotNull(message = "起始楼层不能为空")
    private int startFloor;//起始楼层
    @NotNull(message = "结束楼层不能为空")
    private int endFloor;//结束楼层
    @NotNull(message = "文章Id不能为空")
    private String passageId;//文章Id
}
