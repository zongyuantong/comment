package com.xuanxuan.csu.service;

import com.xuanxuan.csu.dto.ReplyDTO;
import com.xuanxuan.csu.model.Reply;
import com.xuanxuan.csu.core.Service;

import java.util.List;


/**
 * Created by PualrDwade on 2018/12/03.
 */
public interface ReplyService extends Service<Reply> {


    /**
     * 得到根据评论id 得到的回复(时间排序)
     *
     * @param commentId
     * @param size
     * @return
     */
    public List<Reply> findReplyByCommentId(String commentId);


    /**
     * 添加新的回复
     *
     * @param reply
     */
    public void addNewReply(ReplyDTO replyDTO);


    /**
     * 删除评论
     *
     * @param replyId
     */
    public void deleteReply(String replyId);


}
