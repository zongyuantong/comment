package com.xuanxuan.csu.service.impl;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.xuanxuan.csu.Tester;
import com.xuanxuan.csu.core.ServiceException;
import com.xuanxuan.csu.dto.CommentDTO;
import com.xuanxuan.csu.dto.ReplyDTO;
import com.xuanxuan.csu.model.Comment;
import com.xuanxuan.csu.model.Passage;
import com.xuanxuan.csu.model.Reply;
import com.xuanxuan.csu.model.UserInfo;
import com.xuanxuan.csu.service.CommentService;
import com.xuanxuan.csu.service.PassageService;
import com.xuanxuan.csu.service.ReplyService;
import com.xuanxuan.csu.service.UserInfoService;
import com.xuanxuan.csu.vo.CommentVO;
import org.junit.Before;
import org.junit.Test;

import javax.annotation.Resource;
import javax.servlet.ServletContainerInitializer;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

public class CommentServiceImplTest extends Tester {

    @Resource
    CommentService commentService;
    @Resource
    PassageService passageService;
    @Resource
    UserInfoService userInfoService;
    @Resource
    ReplyService replyService;


    /**
     * 得到评论详情
     */
    @Test
    public void getCommentDetail() {
        List<Comment> commentList = commentService.findAll();
        for (Comment comment : commentList) {
            CommentVO commentVO = commentService.getCommentDetail(comment.getId());
            assertNotNull(commentVO);
            System.out.println(commentVO);
        }


        //设置为空值
        String commentId = null;
        try {
            CommentVO commentVO = commentService.getCommentDetail(commentId);
            System.out.println(commentVO);
            throw new RuntimeException("测试未通过,没有正确抛出异常");
        } catch (ServiceException e) {
            System.out.println(e.getMessage());
        }


        //设置为不存在的值
        commentId = "null";
        try {
            CommentVO commentVO = commentService.getCommentDetail(commentId);
            System.out.println(commentVO);
            throw new RuntimeException("测试未通过,没有正确抛出异常");
        } catch (ServiceException e) {
            System.out.println(e.getMessage());
        }


    }


    /**
     * 添加新评论
     */
    @Test
    public void addNewComment() {

        //进行文章评论
        int sum = commentService.findAll().size();
        CommentDTO commentDTO1 = new CommentDTO();
        CommentDTO commentDTO2 = new CommentDTO();
        commentDTO1.setFromUid("test1");
        commentDTO2.setFromUid("test1");
        commentDTO1.setPassageId("test1");
        commentDTO2.setPassageId("test1");
        commentDTO1.setContent("测试评论");
        commentDTO2.setContent("测试评论2");
        commentService.addNewComment(commentDTO1);
        commentService.addNewComment(commentDTO1);
        //得到文章的评论
        List<Comment> commentVOList = commentService.findAll();
        //进行断言
        assertEquals(2 + sum, commentVOList.size());//只有一条评论
        System.out.println(commentVOList.get(0).getFloor() + "楼" + commentVOList.get(1).getFloor() + "楼");
        assertEquals(Optional.of(0).get(), commentVOList.get(0).getReplyNum());
        assertEquals(Optional.of(0).get(), commentVOList.get(0).getZanNum());
        assertEquals(Optional.of(0).get(), commentVOList.get(1).getReplyNum());
        assertEquals(Optional.of(0).get(), commentVOList.get(1).getZanNum());

        //若文章没有回复

    }

    /**
     * 测试删除评论
     */
    @Test
    public void deleteComment() {
        //得到当前评论数
        int sum = passageService.getComments("test1", 1).size();
        //首先进行评论
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setContent("测试添加评论");
        commentDTO.setPassageId("test1");
        commentDTO.setFromUid("test2");
        commentService.addNewComment(commentDTO);
        //得到文章评论
        List<CommentVO> commentVOList = passageService.getComments("test1", 1);
        //测试删除评论
        commentVOList = passageService.getComments("test1", 1);

        //评论Id不存在
        String commentId = "null";
        try {
            commentService.deleteComment(commentId);
            throw new RuntimeException("测试未通过,没有正常抛出异常");

        } catch (ServiceException e) {
            System.out.println(e.getMessage());
        }


        //对评论进行回复
        ReplyDTO replyDTO = new ReplyDTO();
        replyDTO.setContent("测试回复");
        replyDTO.setFromUid("test1");
        replyDTO.setReplyType(1);
        //设置回复目标的id
        replyDTO.setReplyId("test1");
        replyService.addNewReply(replyDTO);
        //对评论进行删除
        commentService.deleteComment("test1");
        //得到文章回复
        List<Reply> reply = replyService.findReplyByCommentId("test1");
        assertEquals(0, reply.size());

    }

    /**
     * 测试更新评论
     */
    @Test
    public void updateComment() {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setContent("测试更新new");
        commentDTO.setPassageId("test1");
        commentDTO.setFromUid("test1");
        commentDTO.setCommentId("test1");
        commentService.updateComment(commentDTO);
        Comment comment = commentService.findById("test1");
        //进行断言
        assertEquals("测试更新new", comment.getContent());

        //特殊情况测试
        commentDTO.setFromUid("null");
        try {
            commentService.updateComment(commentDTO);
            throw new RuntimeException("测试未通过,没有正常抛出异常");
        } catch (ServiceException e) {
            System.out.println(e.getMessage());
        }

        //特殊情况测试
        commentDTO.setFromUid("test");
        commentDTO.setPassageId("null");
        try {
            commentService.updateComment(commentDTO);
            throw new RuntimeException("测试未通过,没有正常抛出异常");
        } catch (ServiceException e) {
            System.out.println(e.getMessage());
        }

        //特殊情况测试
        commentDTO.setPassageId("test");
        try {
            commentService.updateComment(commentDTO);
            throw new RuntimeException("测试未通过,没有正常抛出异常");
        } catch (ServiceException e) {
            System.out.println(e.getMessage());
        }
    }


    /**
     * 测试评论点赞的过滤器
     */
    @Test
    public void testCommentsFilter() {

    }

}