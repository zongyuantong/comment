package com.xuanxuan.csu.service.impl;

import com.xuanxuan.csu.core.ServiceException;
import com.xuanxuan.csu.dao.CommentMapper;
import com.xuanxuan.csu.dao.ReplyMapper;
import com.xuanxuan.csu.dto.ReplyDTO;
import com.xuanxuan.csu.model.Comment;
import com.xuanxuan.csu.model.Reply;
import com.xuanxuan.csu.service.CommentService;
import com.xuanxuan.csu.service.ReplyService;
import com.xuanxuan.csu.core.AbstractService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import java.util.List;


/**
 * Created by PualrDwade on 2018/12/03.
 */
@Service
@Transactional
public class ReplyServiceImpl extends AbstractService<Reply> implements ReplyService {
    @Resource
    private ReplyMapper replyMapper;

    @Resource
    private CommentMapper commentMapper;

    @Override
    public List<Reply> findReplyByCommentId(String commentId) {
        Condition condition = new Condition(Reply.class);
        condition.createCriteria().andCondition("comment_id=", commentId);
        condition.orderBy("create_time").asc();
        return replyMapper.selectByCondition(condition);
    }

    @Override
    public void addNewReply(ReplyDTO replyDTO) {
        Reply reply = new Reply();
        BeanUtils.copyProperties(replyDTO, reply);
        //得到对应的评论
        Comment comment = null;
        if (replyDTO.getReplyType() == 1) {
            comment = commentMapper.selectByPrimaryKey(replyDTO.getReplyId());
        } else {
            Reply to_reply = replyMapper.selectByPrimaryKey(replyDTO.getReplyId());
            comment = commentMapper.selectByPrimaryKey(to_reply.getCommentId());
        }
        reply.setCommentId(comment.getId());
        replyMapper.insertSelective(reply);
        //同时,对应的评论的回复量要+1
        comment.setReplyNum(comment.getReplyNum() + 1);
        commentMapper.updateByPrimaryKeySelective(comment);
    }

    @Override
    public void deleteReply(String replyId) {
        //找到回复此回复的所有回复,然后删除
        Condition condition = new Condition(Reply.class);
        condition.createCriteria().andCondition("reply_id=", replyId);
        int size = replyMapper.selectCountByCondition(condition);
        replyMapper.deleteByCondition(condition);
        //随后删除本条
        Reply reply = replyMapper.selectByPrimaryKey(replyId);
        replyMapper.delete(reply);
        //随后对应的评论的回复数量要减少对应的数量
        Comment comment = commentMapper.selectByPrimaryKey(reply.getCommentId());
        comment.setReplyNum(comment.getReplyNum() - size >= 0 ? comment.getReplyNum() - size : 0);
        //更新评论
        commentMapper.updateByPrimaryKeySelective(comment);
    }

}
