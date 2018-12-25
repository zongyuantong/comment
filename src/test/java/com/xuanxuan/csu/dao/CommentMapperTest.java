package com.xuanxuan.csu.dao;

import com.xuanxuan.csu.Tester;
import com.xuanxuan.csu.dto.CommentDTO;
import com.xuanxuan.csu.dto.ReplyDTO;
import com.xuanxuan.csu.model.*;
import com.xuanxuan.csu.service.CommentService;
import com.xuanxuan.csu.service.PassageService;
import com.xuanxuan.csu.service.ReplyService;
import com.xuanxuan.csu.service.UserInfoService;
import com.xuanxuan.csu.vo.CommentVO;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.annotation.Resource;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.List;

/**
 * 高级sql查询的单元测试类
 */
public class CommentMapperTest extends Tester {

    @Resource
    CommentService commentService;
    @Resource
    PassageService passageService;
    @Resource
    UserInfoService userInfoService;
    @Resource
    ReplyService replyService;
    @Resource
    CommentMapper commentMapper;


    /**
     * 创建测试数据
     */
    @Before
    public void setUp() {
        //首先插入用户数据
        UserInfo userInfo = new UserInfo();
        userInfo.setId("test");
        userInfo.setNickName("陈志轩");
        userInfoService.save(userInfo);
        //插入文章
        Passage passage = new Passage();
        passage.setId("test");
        passage.setPlatformId("test");
        passage.setUrl("test");
        passageService.save(passage);
        //插入评论
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setContent("测试");
        commentDTO.setPassageId("test");
        commentDTO.setFromUid("test");
        commentService.addNewComment(commentDTO);
    }


    /**
     * 查询评论详情
     */
    @Test
    public void selectCommentDetailById() {
        //得到评论Id
        List<CommentVO> commentList = passageService.getComments("test");
        String commentId = commentList.get(0).getId();
        //进行回复
        ReplyDTO replyDTO1 = new ReplyDTO();
        replyDTO1.setReplyId(commentId);
        replyDTO1.setFromUid("test");
        replyDTO1.setContent("reply1");


        ReplyDTO replyDTO2 = new ReplyDTO();
        replyDTO2.setContent("reply2");
        replyDTO2.setFromUid("test");
        replyDTO2.setReplyId(commentId);
        replyService.addNewReply(replyDTO1);
        replyService.addNewReply(replyDTO2);
        //查询评论详情
        CommentDetail commentDetail = commentMapper.selectCommentDetailById(commentId);
        System.out.println(commentDetail);
        for (Reply reply : commentDetail.getReplyList()) {
            System.out.println("回复信息:" + reply);
        }

        //测试字段映射
        Comment comment = commentMapper.selectByPrimaryKey("aaaaaa");
        System.out.println(comment);

        //测试为空的情况
        //随便增加一条评论
        comment = new Comment();
        comment.setId("chenzhixuan");
        comment.setContent("test empty list");
        comment.setFloor(0);
        comment.setZanNum(0);
//        comment.setCreateTime();
        comment.setFromUid("aaaaaa");
        comment.setPassageId("aaaaaa");
        comment.setReplyNum(0);
        commentMapper.insert(comment);


        CommentDetail commentDetail1 = commentMapper.selectCommentDetailById("chenzhixuan");
        System.out.println(commentDetail1);
        //评论是否为空？
        assertNotNull(commentDetail1.getReplyList());
        //不为空，size = 0
        assertEquals(commentDetail1.getReplyList().size(), 0);
    }


    /**
     * 查询文章的评论
     */
    @Test
    public void test2() {
        String passageId = "aaaaaa";
        List<CommentDetail> commentDetails = commentMapper.selectCommentListByPassageId(passageId);
        for (CommentDetail commentDetail : commentDetails) {
            System.out.println("评论基本信息为：" + commentDetail);
            for (Reply reply : commentDetail.getReplyList()) {
                System.out.println("有如下评论：" + reply);
            }
        }
    }
}