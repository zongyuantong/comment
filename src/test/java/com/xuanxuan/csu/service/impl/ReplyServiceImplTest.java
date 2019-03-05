package com.xuanxuan.csu.service.impl;

import com.xuanxuan.csu.Tester;
import com.xuanxuan.csu.dto.ReplyDTO;
import com.xuanxuan.csu.model.Comment;
import com.xuanxuan.csu.model.Reply;
import com.xuanxuan.csu.service.CommentService;
import com.xuanxuan.csu.service.ReplyService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.*;

public class ReplyServiceImplTest extends Tester {

    @Autowired
    CommentService commentService;

    @Autowired
    ReplyService replyService;

    @Test
    public void testFindReplyByCommentId() {
        assertNotNull(replyService.findReplyByCommentId("test1"));
    }

    /**
     * 测试添加回复对于评论回复数的正确性
     */
    @Test
    public void testAddNewReply() {
        ReplyDTO replyDTO = new ReplyDTO();
        replyDTO.setReplyType(1);
        replyDTO.setReplyId("test1");
        replyDTO.setFromUid("test1");
        replyDTO.setContent("测试添加新回复");
        int oldSize = replyService.findReplyByCommentId("test1").size();
        replyService.addNewReply(replyDTO);
        int newSize = replyService.findReplyByCommentId("test1").size();
        assertEquals(newSize - 1, oldSize);
    }


    /**
     * 测试删除回复评论对于评论回复数的影响
     */
    @Test
    public void testDeleteReply() {
        List<Reply> replyList = replyService.findReplyByCommentId("test1");
        replyList.forEach(reply -> replyService.deleteReply(reply.getId()));
        assertEquals(0, replyService.findReplyByCommentId("test1").size());
    }
}