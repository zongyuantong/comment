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
     * 创建测试数据
     */
    @Before
    public void setUp() {
        //首先插入用户数据
        UserInfo userInfo = new UserInfo();
        userInfo.setId("test");
        userInfo.setNickName("chenzhixuan");
        userInfoService.save(userInfo);
        //插入文章
        Passage passage = new Passage();
        passage.setId("test");
        passage.setPlatformId("test");
        passage.setUrl("test");
        passageService.save(passage);
    }

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

        //空list
        commentList = new ArrayList<>();
        for (Comment comment : commentList) {
            //
        }

        //设置为空值
        String commentId = null;
        CommentVO commentVO = commentService.getCommentDetail(commentId);
        System.out.println(commentVO);


        //设置为不存在的值
        commentId = "null";
        commentVO = commentService.getCommentDetail(commentId);
        System.out.println(commentVO);


    }


    /**
     * 添加新评论
     */
    @Test
    public void addNewComment() {
        //1. 文章为null
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setContent("test");
        commentDTO.setPassageId("null");
        commentDTO.setFromUid("test");
        try {
            commentService.addNewComment(commentDTO);
            throw new RuntimeException("测试未通过,没有正确抛出异常");
        } catch (ServiceException e) {
            System.out.println(e.getMessage());
        }


        //2.userinfo为null
        commentDTO.setFromUid("null");
        commentDTO.setPassageId("test");
        try {
            commentService.addNewComment(commentDTO);
            throw new RuntimeException("测试未通过,没有正确抛出异常");
        } catch (ServiceException e) {
            System.out.println(e.getMessage());
        }


        //进行文章评论
        CommentDTO commentDTO1 = new CommentDTO();
        CommentDTO commentDTO2 = new CommentDTO();
        commentDTO1.setFromUid("test");
        commentDTO2.setFromUid("test");
        commentDTO1.setPassageId("test");
        commentDTO2.setPassageId("test");
        commentDTO1.setContent("测试评论");
        commentDTO2.setContent("测试评论2");
        commentService.addNewComment(commentDTO1);
        commentService.addNewComment(commentDTO1);
        //得到文章的评论
        List<CommentVO> commentVOList = passageService.getComments("test");
        //进行断言
        assertEquals(2, commentVOList.size());//只有一条评论
        System.out.println(commentVOList.get(0).getFloor() + "楼" + commentVOList.get(1).getFloor() + "楼");
        assertEquals(Optional.of(1 + commentVOList.get(1).getFloor()).get(), commentVOList.get(0).getFloor());
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
        //首先进行评论
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setContent("test");
        commentDTO.setPassageId("test");
        commentDTO.setFromUid("test");
        commentService.addNewComment(commentDTO);
        //得到文章评论
        List<CommentVO> commentVOList = passageService.getComments("test");
        assertEquals(1, commentVOList.size());
        commentService.deleteComment(commentVOList.get(0).getId());
        commentVOList = passageService.getComments("test");
        assertEquals(0, commentVOList.size());

        //评论Id不存在
        String commentId = "null";
        try {
            commentService.deleteComment(commentId);
            throw new RuntimeException("测试未通过,没有正常抛出异常");

        } catch (ServiceException e) {
            System.out.println(e.getMessage());
        }


        //再次进行评论,同时添加回复内容
        CommentDTO commentDTO2 = new CommentDTO();
        commentDTO2.setContent("test");
        commentDTO2.setPassageId("test");
        commentDTO2.setFromUid("test");
        commentService.addNewComment(commentDTO2);
        //得到文章评论
        List<CommentVO> commentVOList2 = passageService.getComments("test");
        System.out.println(commentVOList2);
        //同时对评论进行回复
        ReplyDTO replyDTO = new ReplyDTO();
        replyDTO.setContent("测试回复");
        replyDTO.setFromUid("test");
        //设置回复目标的id
        replyDTO.setReplyId(commentVOList2.get(0).getId());
        replyService.addNewReply(replyDTO);
        //对评论进行删除
        commentService.deleteComment(commentVOList2.get(0).getId());
        //得到文章回复
        List<Reply> reply = replyService.findReplyByCommentId("test");
        assertEquals(0, reply.size());

    }

    /**
     * 测试更新评论
     */
    @Test
    public void updateComment() {
        //首先对文章进行评论
        //首先进行评论
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setContent("test");
        commentDTO.setPassageId("test");
        commentDTO.setFromUid("test");
        commentService.addNewComment(commentDTO);
        //得到评论id
        List<CommentVO> commentDTOList = passageService.getComments("test");
        String commentId = commentDTOList.get(0).getId();
        //进行评论更新
        commentDTO.setContent("test2");
        System.out.println(commentDTO);
        commentService.updateComment(commentDTO, commentId);
        Comment comment = commentService.findById(commentId);
        //进行断言
        assertEquals("test2", comment.getContent());

        //特殊情况测试
        commentDTO.setFromUid("null");
        try {
            commentService.updateComment(commentDTO, commentId);
            throw new RuntimeException("测试未通过,没有正常抛出异常");
        } catch (ServiceException e) {
            System.out.println(e.getMessage());
        }

        //特殊情况测试
        commentDTO.setFromUid("test");
        commentDTO.setPassageId("null");
        try {
            commentService.updateComment(commentDTO, commentId);
            throw new RuntimeException("测试未通过,没有正常抛出异常");
        } catch (ServiceException e) {
            System.out.println(e.getMessage());
        }

        //特殊情况测试
        commentDTO.setPassageId("test");
        try {
            commentService.updateComment(commentDTO, "null");
            throw new RuntimeException("测试未通过,没有正常抛出异常");
        } catch (ServiceException e) {
            System.out.println(e.getMessage());
        }
    }
}