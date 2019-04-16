package com.xuanxuan.csu.dao;

import com.xuanxuan.csu.core.Mapper;
import com.xuanxuan.csu.model.Comment;
import com.xuanxuan.csu.model.CommentDetail;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface CommentMapper extends Mapper<Comment> {

    /**
     * 通过评论Id查询评论详情
     *
     * @param commentId
     * @return
     */
    public CommentDetail selectCommentDetailById(@Param("id") String commentId);


    /**
     * 通过文章id查询所有评论信息
     *
     * @param passageId
     * @return
     */
    public List<CommentDetail> selectCommentListByPassageId(@Param("passageId") String passageId,
                                                            @Param("page") int page,
                                                            @Param("size") int size);


    /**
     * 通过条件查找文章的评论内容
     *
     * @param condition
     * @return
     */
    public List<CommentDetail> selectCommentListByCondition(Map<String, Object> condition);
}
