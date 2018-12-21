package com.xuanxuan.csu.vo;


import com.xuanxuan.csu.dto.CommentDTO;
import lombok.Data;

import java.util.List;

/**
 * 评论刷新的vo传输对象
 */

@Data
public class CommentRefreshVO {
    //最新的值
    private List<CommentVO> newComments;
    //新增评论数:
    private int addNum;
    //刷新的值
    private List<CommentVO> refreshComments;
}
