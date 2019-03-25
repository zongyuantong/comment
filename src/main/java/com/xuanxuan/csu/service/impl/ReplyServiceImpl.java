package com.xuanxuan.csu.service.impl;

import com.xuanxuan.csu.core.AbstractService;
import com.xuanxuan.csu.dao.CommentMapper;
import com.xuanxuan.csu.dao.ReplyMapper;
import com.xuanxuan.csu.dto.ReplyDTO;
import com.xuanxuan.csu.model.Comment;
import com.xuanxuan.csu.model.Reply;
import com.xuanxuan.csu.service.ReplyService;
import com.xuanxuan.csu.util.CommonUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import java.util.ArrayList;
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


    /**
     * 得到根据评论id 得到的回复(时间排序)
     *
     * @param commentId
     * @return
     */
    @Override
    public List<Reply> findReplyByCommentId(String commentId) {
        Condition condition = new Condition(Reply.class);
        condition.createCriteria().andCondition("comment_id=", commentId);
        condition.orderBy("create_time").asc();
        return replyMapper.selectByCondition(condition);
    }

    /**
     * 添加新的回复
     *
     * @param replyDTO
     */
    @Override
    public void addNewReply(ReplyDTO replyDTO) {
        Reply reply = new Reply();
        BeanUtils.copyProperties(replyDTO, reply);
        // 编码为unicode
        reply.setContent(CommonUtil.unicode(replyDTO.getContent()));
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

    /**
     * 删除评论
     *
     * @param replyId
     */
    @Override
    public void deleteReply(String replyId) {
        Reply reply = replyMapper.selectByPrimaryKey(replyId);
        System.out.println(replyId);
        if (reply == null) {
            return;
        }
        List<Reply> replyList = findReplyByCommentId(reply.getCommentId());
        List<String> deleteList = this.findReplysToDelete(reply, replyList);
        System.out.println("deleteList:" + deleteList);
        //批量主键删除
        replyMapper.batchDeleteReplys(deleteList);
        //更新comment
        Comment comment = commentMapper.selectByPrimaryKey(reply.getCommentId());
        comment.setReplyNum(comment.getReplyNum() - deleteList.size() > 0 ? comment.getReplyNum() - deleteList.size() : 0);
        commentMapper.updateByPrimaryKeySelective(comment);

    }

    /**
     * 查找算法
     *
     * @param target
     * @param replyList
     * @return
     */
    private List<String> findReplysToDelete(Reply target, List<Reply> replyList) {
        System.out.println("replyList:" + replyList);
        System.out.println("target:" + target);
        List<String> targetList = new ArrayList<>();//目标集合,reply_id = replyid;
        targetList.add(target.getId());
        replyList.forEach(reply -> {
            //递归
            if (reply.getReplyId().equals(target.getId())) {
                targetList.addAll(findReplysToDelete(reply, replyList));
            }
        });
        return targetList;
    }

}
