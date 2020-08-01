package com.xuanxuan.csu.util;


import com.xuanxuan.csu.model.Comment;

import java.util.List;

/**
 * @author PualrDwade
 * @note 抽象策略类, 提供抽象的选取热门评论方法
 */


public interface HotCommentStrategy {
    //得到热门评论id list

    /**
     * @param commentList 评论列表
     * @param size        选择的数量
     * @return
     * @author PualrDwade
     */
    List<Comment> getHotCommentList(List<Comment> commentList);
}
