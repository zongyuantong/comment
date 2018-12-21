package com.xuanxuan.csu.service.impl;

import com.xuanxuan.csu.core.ServiceException;
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
    private CommentService commentService;

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
        //判断回复的类型
        Comment comment = commentService.findById(replyDTO.getReplyId());
        if (comment == null) {
            //再判断是否回复了回复
            Reply reply1 = replyMapper.selectByPrimaryKey(replyDTO.getReplyId());
            if (reply1 == null) throw new ServiceException("回复目标id不存在");
            else {
                reply.setReplyType(2);//回复的回复
                String commentId = reply1.getCommentId();
                reply.setCommentId(commentId);
            }
        } else {
            reply.setReplyType(1);//回复评论
            reply.setCommentId(comment.getId());
        }
        //持久化
        replyMapper.insert(reply);
    }

}
